package io.github.honoriuss.mapr.query.producer;

import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.query.models.MetaInformation;
import org.ojai.Document;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public abstract class QueryProducer {
    private static final Pattern SAVE_PREFIX = java.util.regex.Pattern.compile("^(save|insert)");
    private static final Pattern DELETE_PREFIX = java.util.regex.Pattern.compile("^(remove|delete)");

    private static MetaInformation metaInformation;
    private static TypeElement interfaceElement;
    private static List<ParameterSpec> parameterSpecs;
    private static ExecutableElement enclosedElement;

    public static MethodSpec createQuery(ExecutableElement enclosedElement,
                                         MetaInformation metaInformation,
                                         TypeElement interfaceElement,
                                         List<ParameterSpec> parameterSpecs) {
        QueryProducer.metaInformation = metaInformation;
        QueryProducer.interfaceElement = interfaceElement;
        QueryProducer.parameterSpecs = parameterSpecs;
        QueryProducer.enclosedElement = enclosedElement;

        if (SAVE_PREFIX.matcher(enclosedElement.toString()).find()) {
            return createSaveQuery();
        } else if (DELETE_PREFIX.matcher(enclosedElement.toString()).find()) {
            return createDeleteQuery();
        }

        return null;
    }

    private static MethodSpec createSaveQuery() {
        if (enclosedElement.getReturnType().toString().equals("T")) {
            return MethodSpec.methodBuilder(
                            enclosedElement.getSimpleName().toString())
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(metaInformation.entityClassName) //TODO extract generic Type to class (Unterscheidung T)
                    .addParameters(parameterSpecs)
                    .beginControlFlow("try ($T store = connection.getStore(dbPath))", DocumentStore.class)
                    .addStatement(createSaveCodeBlocks(parameterSpecs))
                    .addStatement("return $L", parameterSpecs.get(0).name)
                    //.addCode(createCode(enclosedElement)) //TODO hier weiter machen, den Inhalt zu erstellen
                    //.addStatement("return null") //TODO return type in QueryGenerator auslagern
                    .endControlFlow()
                    .build();
        }
        return MethodSpec.methodBuilder(
                        interfaceElement.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(enclosedElement.getReturnType())) //TODO extract generic Type to class (Unterscheidung T)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath))", DocumentStore.class)
                .addStatement(createSaveCodeBlocks(parameterSpecs))
                .addStatement("return $L", parameterSpecs.get(0).name)
                //.addCode(createCode(enclosedElement)) //TODO hier weiter machen, den Inhalt zu erstellen
                //.addStatement("return null") //TODO return type in QueryGenerator auslagern
                .endControlFlow()
                .build();
    }

    private static String createSaveCodeBlocks(List<ParameterSpec> parameterSpec) {
        var parameterLength = parameterSpec.size();
        var resString = new StringBuilder();
        for (int i = 0; i < parameterLength; ++i) {
            var parameterName = parameterSpec.get(i).name;
            resString.append(CodeBlock.builder()
                    .addStatement("$L.set_id($T.randomUUID().toString())", parameterName, UUID.class)
                    .addStatement("$T newDoc = connection.newDocument($L)", Document.class, parameterName)
                    .addStatement("store.insert(newDoc)")
                    .build());
        }
        return resString.toString();
    }

    private static MethodSpec createDeleteQuery() {
        return MethodSpec.methodBuilder(
                        interfaceElement.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(enclosedElement.getReturnType())) //TODO extract generic Type to class (Unterscheidung T)
                .addParameters(parameterSpecs) //TODO wie createSaveCodeBlocks erstellen
                .beginControlFlow("try ($T store = connection.getStore(dbPath))", DocumentStore.class)
                .addStatement("store.delete($L)", parameterSpecs.get(0).name)
                .endControlFlow()
                .build();
    }
}
