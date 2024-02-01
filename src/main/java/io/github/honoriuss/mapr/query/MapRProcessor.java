package io.github.honoriuss.mapr.query;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.connections.OjaiConnector;
import io.github.honoriuss.mapr.query.annotations.Repository;
import io.github.honoriuss.mapr.query.models.MetaInformation;
import io.github.honoriuss.mapr.query.models.Query;
import io.github.honoriuss.mapr.query.producer.QueryProducer;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.springframework.stereotype.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author H0n0riuss
 */
@Deprecated
@SupportedAnnotationTypes("io.github.honoriuss.mapr.query.annotations.*")
@AutoService(MapRProcessor.class)
public class MapRProcessor extends AbstractProcessor {
    private MetaInformation metaInformation;
    private TypeSpec.Builder classBuilder;
    private TypeElement interfaceElement;
    private List<? extends TypeMirror> extendsInterfaces;
    private Query query;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Repository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                initProcessorAttributes((TypeElement) element);
                generateImplementation();
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Only interfaces can be annotated with @Repository", element);
            }
        }
        return true;
    }

    private void generateImplementation() {
        prepareImplementation();
        implementHead();
        implementAttributes();
        implementConstructor();
        implementMethods();
        implementExtendsMethods();
        writeImplementedClass();
    }

    private void prepareImplementation() {
        extendsInterfaces = interfaceElement.getInterfaces();
    }

    private void implementHead() {
        classBuilder
                .addAnnotation(Component.class)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(TypeName.get(interfaceElement.asType()));
        for (var interfaceToImplement : extendsInterfaces) {
            classBuilder.addSuperinterface(interfaceToImplement);
        }
    }

    private void implementAttributes() {
        Repository repositoryAnnotation = interfaceElement.getAnnotation(Repository.class);
        classBuilder
                .addField(FieldSpec
                        .builder(Connection.class, "connection", Modifier.PRIVATE, Modifier.FINAL)
                        .build())
                .addField(FieldSpec
                        .builder(String.class, "dbPath", Modifier.PRIVATE, Modifier.FINAL)
                        .initializer(String.format("\"%s\"", repositoryAnnotation.tablePath()))
                        .build());
    }

    private void implementConstructor() {
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(OjaiConnector.class, "connection")
                .addStatement("this.connection = connection.getConnection()")
                .build());
    }

    private void implementMethods() {
        // Generiere Implementierungen für jede Methode im Interface
        for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                generateMethod((ExecutableElement) enclosedElement);
            }
        }
    }

    private void implementExtendsMethods() {
        for (var t : extendsInterfaces) {
            if (t.getKind() == TypeKind.DECLARED) {
                Element element = ((DeclaredType) t).asElement(); //TODO hier weiter machen
                for (var e : element.getEnclosedElements()) {
                    generateMethod((ExecutableElement) e);
                }
            }
        }
    }

    private void generateMethod(ExecutableElement enclosedElement) { //TODO den QueryCreator nehmen
        if (enclosedElement.getReturnType().toString().equals(void.class.toString())) {
            //TODO Unterschiedung von void und alles andere
            //TODO inhalt des enclosedElement mit geben
            generateVoidMethod(enclosedElement);
        } else {
            generateReturnMethod(enclosedElement);
        }
    }

    private void generateVoidMethod(ExecutableElement enclosedElement) {
        var comment = "";
        if (this.query.getQueryParts().isEmpty() || this.query.getQueryParts().get().getColumnList().isEmpty()) {
            comment = "your mum";
        } else {
            comment = this.query.getQueryParts().get().getColumnList().get().get(0);
        }
        classBuilder
                .addMethod(MethodSpec.methodBuilder(
                                enclosedElement.getSimpleName().toString())
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .addComment(comment)
                        .returns(void.class)
                        .addParameters(getParameterSpecs(enclosedElement))
                        .beginControlFlow("try ($T store = connection.getStore(dbPath)) ", DocumentStore.class)
                        //TODO hier weiter machen, den Inhalt zu erstellen
                        .addStatement(String.format("store.delete(%s)", getParameterSpecs(enclosedElement).get(0).name)) //TODO variabel gestalten
                        .endControlFlow()
                        .build()); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
    }

    private void generateReturnMethod(ExecutableElement enclosedElement) {
        if (enclosedElement.getReturnType().toString().equals("T")) { //TODO auslagern in den QueryCreator
            classBuilder
                    .addMethod(createCode(enclosedElement, getParameterSpecs(enclosedElement))); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
        } else {
            classBuilder
                    .addMethod(createCode(enclosedElement, getParameterSpecs(enclosedElement))); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
        }
    }

    private void writeImplementedClass() {
        try {
            JavaFile javaFile = JavaFile.builder(metaInformation.packageName, classBuilder.build())
                    .build();
            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }

    private void initProcessorAttributes(TypeElement interfaceElement) {
        metaInformation = new MetaInformation(interfaceElement, processingEnv);
        this.interfaceElement = interfaceElement;
        classBuilder = TypeSpec.classBuilder(metaInformation.generatedClassName);
        this.query = new Query(interfaceElement, metaInformation.entityClassName.getClass());
    }

    private MethodSpec createCode(ExecutableElement enclosedElement, List<ParameterSpec> parameterSpecs) {
        return QueryProducer.createQuery(enclosedElement, metaInformation, interfaceElement, parameterSpecs);
    }

    private List<ParameterSpec> getParameterSpecs(ExecutableElement enclosedElement) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>(); //TODO refactor
        for (VariableElement parameter : enclosedElement.getParameters()) {
            // Konvertiere den Parameter in einen ParameterSpec und füge ihn zur Liste hinzu
            var typeParameter = parameter.asType();
            if (typeParameter.toString().equals("T")) {
                typeParameter = processingEnv.getElementUtils()
                        .getTypeElement(metaInformation.entityClassName.toString())
                        .asType();
            }
            ParameterSpec parameterSpec = ParameterSpec.builder(
                            TypeName.get(typeParameter), // Typ des Parameters
                            parameter.getSimpleName().toString() // Name des Parameters
                    )
                    .build();
            parameterSpecs.add(parameterSpec);
        }
        return parameterSpecs;
    }
}
