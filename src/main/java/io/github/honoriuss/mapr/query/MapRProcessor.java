package io.github.honoriuss.mapr.query;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.query.annotations.Repository;
import io.github.honoriuss.mapr.query.models.MetaInformation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

/**
 * @author H0n0riuss
 */
@SupportedAnnotationTypes("io.github.honoriuss.mapr.query.annotations.*")
@AutoService(MapRProcessor.class)
public class MapRProcessor extends AbstractProcessor {
    private final MetaInformation metaInformation = new MetaInformation();
    private final StringBuilder generatedCode = new StringBuilder();
    private TypeSpec.Builder classBuilder;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Repository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                initProcessorAttributes((TypeElement) element);
                generateImplementation((TypeElement) element);
            } else {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Only interfaces can be annotated with @Repository", element);
            }
        }
        return true;
    }


    private void generateImplementation(TypeElement interfaceElement) {
        implementHead(interfaceElement);
        implementAttributes(interfaceElement);
        implementConstructor(interfaceElement);

        // Generiere Implementierungen für jede Methode im Interface
        for (Element enclosedElement : interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                generateMethods((ExecutableElement) enclosedElement);
            }
        }

        generatedCode.append("}\n");

        writeImplementedClass_old(interfaceElement, metaInformation.generatedClassName, metaInformation.packageName);
    }

    private void writeImplementedClass_old(TypeElement interfaceElement, String generatedClassName, String packageName) {
        try {
            JavaFileObject sourceFile = processingEnv.getFiler()
                    .createSourceFile(packageName + "." + generatedClassName, interfaceElement);
            try (Writer writer = sourceFile.openWriter()) {
                writer.write(generatedCode.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void implementHead(TypeElement interfaceElement) {
        generatedCode.append("package ")
                .append(metaInformation.packageName)
                .append(";\n\n");
        generatedCode
                .append("import org.springframework.stereotype.Component;\n") //TODO add imports for class
                .append("import io.github.honoriuss.mapr.query.parser.QueryCreator;\n")
                .append("import io.github.honoriuss.mapr.connections.OjaiConnector;\n")
                .append("import org.ojai.Document;\n")
                .append("import org.ojai.store.*;\n\n");
        generatedCode.append("@Component\n");
        generatedCode.append("public class ")
                .append(metaInformation.generatedClassName)
                .append(" implements ")
                .append(interfaceElement.getSimpleName())
                .append(" {\n\n");
    }

    private void implementAttributes(TypeElement interfaceElement) {
        generatedCode
                .append("    private final Connection connection;\n");

        Repository repositoryAnnotation = interfaceElement.getAnnotation(Repository.class);
        try {
            classBuilder
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(TypeName.get(interfaceElement.asType()))
                    .addField(FieldSpec.builder(metaInformation.entityClassName, "entity", Modifier.PRIVATE).build())
                    .addMethod(MethodSpec.methodBuilder("save")
                            .addModifiers(Modifier.PUBLIC)
                            .returns(void.class)
                            .addParameter(metaInformation.entityClassName, "entity")
                            .addStatement("this.entity = entity")
                            .build());
            JavaFile javaFile = JavaFile.builder(metaInformation.packageName, classBuilder.build())
                    .build();
            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception ex) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }

        String path = repositoryAnnotation.tablePath();
        generatedCode
                .append("    private final String dbPath = \"")
                .append(path)
                .append("\";\n");
    }

    private void implementConstructor(TypeElement interfaceElement) {
        generatedCode
                .append("    public ")
                .append(interfaceElement.getSimpleName())
                .append("Impl") //TODO alles auslagern was Namen hat
                .append("(Connection connection) {\n")
                .append("        this.connection = connection;\n")
                .append("    }\n\n");
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


    private void initProcessorAttributes(TypeElement interfaceElement) {
        metaInformation.generatedClassName = interfaceElement.getSimpleName() + "Impl";
        metaInformation.packageName = processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString();
        // Get the generic type parameter
        DeclaredType declaredType = (DeclaredType) interfaceElement.getInterfaces().get(0);
        TypeMirror entityTypeMirror = declaredType.getTypeArguments().get(0);
        metaInformation.entityClassName = ClassName.get((TypeElement) processingEnv.getTypeUtils().asElement(entityTypeMirror));

        classBuilder = TypeSpec.classBuilder(metaInformation.generatedClassName + "Nutte"); //TODO entfernen "Nutte"
    }
}
