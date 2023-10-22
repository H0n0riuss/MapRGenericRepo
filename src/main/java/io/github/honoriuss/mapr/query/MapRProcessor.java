package io.github.honoriuss.mapr.query;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.query.annotations.Repository;
import io.github.honoriuss.mapr.query.models.MetaInformation;
import org.ojai.store.Connection;
import org.ojai.store.DocumentStore;
import org.springframework.stereotype.Component;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.Set;

/**
 * @author H0n0riuss
 */
@SupportedAnnotationTypes("io.github.honoriuss.mapr.query.annotations.*")
@AutoService(MapRProcessor.class)
public class MapRProcessor extends AbstractProcessor {
    private MetaInformation metaInformation;
    private TypeSpec.Builder classBuilder;
    private TypeElement interfaceElement;

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
        implementHead();
        implementAttributes();
        implementConstructor();
        implementMethods();
        writeImplementedClass();
    }

    private void implementHead() {
        classBuilder
                .addAnnotation(Component.class)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(TypeName.get(interfaceElement.asType()));
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
                .addParameter(Connection.class, "connection")
                .addStatement("this.connection = connection")
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
        classBuilder
                .addMethod(MethodSpec.methodBuilder(
                                enclosedElement.getSimpleName().toString())
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(void.class)
                        .addCode(String.format("try (%s store = connection.getStore(dbPath)) {\n", ClassName.get(DocumentStore.class)))
                        //TODO hier weiter machen, den Inhalt zu erstellen
                        .addCode("}")
                        .build()); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
    }

    private void generateReturnMethod(ExecutableElement enclosedElement) {
        classBuilder
                .addMethod(MethodSpec.methodBuilder(
                                enclosedElement.getSimpleName().toString())
                        .addAnnotation(Override.class)
                        .addModifiers(Modifier.PUBLIC)
                        .returns(ClassName.get(enclosedElement.getReturnType()))
                        .addCode(String.format("try (%s store = connection.getStore(dbPath)) {\n", ClassName.get(DocumentStore.class)))
                        //TODO hier weiter machen, den Inhalt zu erstellen
                        .addCode("return null;\n")
                        .addCode("}")
                        .build()); //TODO den Teil wahrscheinlich erst nach der Schleife machen, damit alles andere drinnen richtig erstellt wird
        /*generatedCode
                .append("            QueryCondition condition = connection.newCondition();\n")
                .append("            var columns = QueryCreator.createCondition(\"findByTitleContains\"")
                .append(enclosedElement.getSimpleName().toString())
                .append("            );\n")
                .append("            for (var column : columns) {\n")
                .append("                condition = condition.like(column, \"test\");")
                .append(enclosedElement.getParameters()) //TODO das ganze in Schleifen packen
                .append("            }")
                .append("            Query query = connection.newQuery().where(condition).build();\n")
                .append("            return store.find(query).toJavaBean(clazz);\n");*/
    }

    private void writeImplementedClass() {
        try {
            /*classBuilder
                    .addField(FieldSpec.builder(metaInformation.entityClassName, "entity", Modifier.PRIVATE).build())
                    .addMethod(MethodSpec.methodBuilder("save")
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class)
                            .addParameter(metaInformation.entityClassName, "entity")
                            .addStatement("this.entity = entity")
                            .build());*/
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
    }
}
