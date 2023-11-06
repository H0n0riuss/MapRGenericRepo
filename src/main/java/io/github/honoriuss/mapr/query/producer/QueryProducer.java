package io.github.honoriuss.mapr.query.producer;

import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.query.models.MetaInformation;
import org.ojai.Document;
import org.ojai.store.DocumentStore;
import org.ojai.store.Query;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public abstract class QueryProducer {
    private static final Pattern SAVE_PREFIX = Pattern.compile("^(save|insert)");
    private static final Pattern DELETE_PREFIX = Pattern.compile("^(remove|delete)");
    private static final Pattern READ_PREFIX = Pattern.compile("^(read|find|get)");
    private static final Pattern READ_BY_PREFIX = Pattern.compile("^(read|find|get)(\\p{Lu}.*?)??By");
    private static final Pattern All_SUPPORTED_TYPES = Pattern.compile("Limit"); //"Order|Like|Offset|Limit|By"

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
        } else if (READ_PREFIX.matcher(enclosedElement.toString()).find()) {
            return createReadQuery();
        }

        return null;
    }

    private static MethodSpec createReadQuery() {
        Matcher matcher = READ_BY_PREFIX.matcher(enclosedElement.toString());
        String codeBlock;
        if (!matcher.find())
            codeBlock = createReadByIdCodeBlock(parameterSpecs.get(0));
        /*else if (matcher.group(2).equalsIgnoreCase("id")) { //TODO hier noch was gescheites machen
            codeBlock = createReadByIdCodeBlock(parameterSpecs.get(0));
        }*/
        else {
            codeBlock = createReadCodeBlock();
        }

        return MethodSpec.methodBuilder(
                        enclosedElement.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(metaInformation.entityClassName)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath))", DocumentStore.class)
                .addStatement(codeBlock)
                .endControlFlow()
                .build();
    }

    private static String createReadByIdCodeBlock(ParameterSpec parameterSpec) { //TODO order und so shit noch dazu nehmen
        return CodeBlock.builder()
                .add("return store.findById($L).toJavaBean($T.class)", parameterSpec.name, metaInformation.entityClassName)
                .build().toString();
    }

    private static String createReadCodeBlock() { //TODO hier wird es spannend mit der Parameter Reihenfolge
        var regex = "(By|[A-Z][a-z]*)";
        var matcher = Pattern.compile(regex).matcher(enclosedElement.getSimpleName());
        var parts = new ArrayList<String>();
        while (matcher.find()) {
            parts.add(matcher.group());
        }
        Iterator<String> iterator = parts.iterator();
        String keyword;
        var result = CodeBlock.builder();
        //Example: readByIdOrderByDate
        result.addStatement("var resList = new $T<$T>()", ArrayList.class, metaInformation.entityClassName);
        result.add("$T query = connection.newQuery()", Query.class);
        do //TODO lieber ein normaler for-loop, wegen dem Index
        {//TODO alle möglichen Bausteine die es gibt nehmen und dann den nächsten Teil bis bekannter Baustein existiert nehmen und dann erstellen
            keyword = iterator.next();
            matcher = All_SUPPORTED_TYPES.matcher(keyword);
            if (matcher.find()) {//TODO Methode die String zurückgibt, die aus dem String Keyword das richtige von MapR ausgibt
                result.add(String.format(".%s($L)", matcher.group(0).toLowerCase()), parameterSpecs.get(1).name); //TODO index
            }
        } while (iterator.hasNext());
        result.addStatement(".build()");
        result.addStatement("var queryResult = store.find(query)");
        result.addStatement("queryResult.forEach(entry -> resList.add(entry.toJavaBean($T.class)))", metaInformation.entityClassName);
        result.add("return resList.get(0)"); //TODO liste zurückgeben implementieren

        return result.build().toString();
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
                    .addStatement(createSaveCodeBlocks())
                    .addStatement("return $L", parameterSpecs.get(0).name)
                    //.addCode(createCode(enclosedElement)) //TODO hier weiter machen, den Inhalt zu erstellen
                    //.addStatement("return null") //TODO return type in QueryGenerator auslagern
                    .endControlFlow()
                    .build();
        }
        return MethodSpec.methodBuilder(
                        enclosedElement.getSimpleName().toString())
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassName.get(enclosedElement.getReturnType())) //TODO extract generic Type to class (Unterscheidung T)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath))", DocumentStore.class)
                .addStatement(createSaveCodeBlocks())
                .addStatement("return $L", parameterSpecs.get(0).name)
                //.addCode(createCode(enclosedElement)) //TODO hier weiter machen, den Inhalt zu erstellen
                //.addStatement("return null") //TODO return type in QueryGenerator auslagern
                .endControlFlow()
                .build();
    }

    private static String createSaveCodeBlocks() {
        var parameterLength = parameterSpecs.size();
        var resString = new StringBuilder(); //TODO code Block erstellen
        for (int i = 0; i < parameterLength; ++i) {
            var parameterName = parameterSpecs.get(i).name;
            resString.append(CodeBlock.builder()
                    .addStatement("$L.set_id($T.randomUUID().toString())", parameterName, UUID.class)
                    .addStatement("$T newDoc = connection.newDocument($L)", Document.class, parameterName)
                    .add("store.insert(newDoc)")
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
