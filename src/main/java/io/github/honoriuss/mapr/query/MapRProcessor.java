package io.github.honoriuss.mapr.query;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.query.annotations.Repository;
import io.github.honoriuss.mapr.query.models.MetaInformation;
import org.ojai.store.Connection;
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
    private final StringBuilder generatedCode = new StringBuilder();
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

    private void implementMethods() {
        // Generiere Implementierungen für jede Methode im Interface
        for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                generateMethods((ExecutableElement) enclosedElement);
            }
        }
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

    private void generateMethods(ExecutableElement enclosedElement) {
        generatedCode.append("    @Override\n");
        generatedCode.append("    public ")
                .append(enclosedElement.getReturnType())
                .append(" ")
                .append(enclosedElement.getSimpleName())
                .append("(");

        boolean firstParam = true;
        for (Element paramElement : enclosedElement.getParameters()) {
            if (!firstParam) {
                generatedCode.append(", ");
            }
            generatedCode.append(paramElement.asType()).append(" ").append(paramElement.getSimpleName());
            firstParam = false;
        }

        generatedCode.append(") {\n");
        generatedCode.append("        try (DocumentStore store = connection.getStore(dbPath)) {\n");
        if (!enclosedElement.getReturnType().toString().equals("void")) {
            createReturnMethod(generatedCode, enclosedElement);
        }
        generatedCode.append("        }\n");
        generatedCode.append("    }\n\n");
    }

    private void createReturnMethod(StringBuilder generatedCode, ExecutableElement enclosedElement) {
        generatedCode
                .append("            QueryCondition condition = connection.newCondition();\n")
                .append("            var columns = QueryCreator.createCondition(\"findByTitleContains\"")
                .append(enclosedElement.getSimpleName().toString())
                .append("            );\n")
                .append("            for (var column : columns) {\n")
                .append("                condition = condition.like(column, \"test\");")
                .append(enclosedElement.getParameters()) //TODO das ganze in Schleifen packen
                .append("            }")
                .append("            Query query = connection.newQuery().where(condition).build();\n")
                .append("            return store.find(query).toJavaBean(clazz);\n");
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
