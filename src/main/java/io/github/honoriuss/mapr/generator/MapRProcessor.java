package io.github.honoriuss.mapr.generator;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import io.github.honoriuss.mapr.connections.OjaiConnector;
import io.github.honoriuss.mapr.connections.interfaces.ITableCreator;
import io.github.honoriuss.mapr.generator.annotations.CreateTable;
import io.github.honoriuss.mapr.generator.annotations.Entity;
import io.github.honoriuss.mapr.generator.annotations.Repository;
import org.ojai.store.Connection;
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
import java.util.logging.Logger;

/**
 * @author H0n0riuss
 */
@SupportedAnnotationTypes("io.github.honoriuss.mapr.generator.annotations.*")
@AutoService(MapRProcessor.class)
public class MapRProcessor extends AbstractProcessor {
    private String packageName;
    private ClassName entityClassName;
    private List<? extends TypeMirror> extendsInterfaces;
    private TypeElement interfaceElement;
    private TypeSpec.Builder classBuilder;
    private final List<String> classAttributeList = new ArrayList<>(); //TODO consider convert in map with className and attributelist
    private final Logger logger = Logger.getLogger(MapRProcessor.class.getSimpleName());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Entity.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                for (Element enclosedElement : element.getEnclosedElements()) {
                    if (enclosedElement.getKind() == ElementKind.FIELD) {
                        var variableElement = (VariableElement) enclosedElement;
                        var attributeName = variableElement.getSimpleName().toString();
                        classAttributeList.add(attributeName);
                    }
                }
            } else {
                logger.warning("Illegal use of Entity: " + element.getSimpleName().toString());
            }
        }
        for (Element element : roundEnv.getElementsAnnotatedWith(Repository.class)) {
            if (element.getKind() == ElementKind.INTERFACE) {
                initProcessorAttributes((TypeElement) element);
                generateImplementation();
            } else {
                logger.warning("Only interfaces can be annotated with @Repository: " + element.getSimpleName());
            }
        }
        return true;
    }

    private void initProcessorAttributes(TypeElement interfaceElement) {
        this.packageName = this.processingEnv.getElementUtils().getPackageOf(interfaceElement).getQualifiedName().toString();
        // Get the generic type parameter
        var declaredType = (DeclaredType) interfaceElement.getInterfaces().get(0);
        var entityTypeMirror = declaredType.getTypeArguments().get(0);
        this.entityClassName = ClassName.get((TypeElement) this.processingEnv.getTypeUtils().asElement(entityTypeMirror));
        this.interfaceElement = interfaceElement;
        this.classBuilder = TypeSpec.classBuilder(interfaceElement.getSimpleName() + "ImplB");
    }

    private void generateImplementation() {
        prepareImplementation();
        implementHead();
        implementMethodAttributes();
        implementConstructor();
        implementMethods();
        implementExtendsMethods();
        writeImplementedClass();
    }

    private void prepareImplementation() {
        this.extendsInterfaces = this.interfaceElement.getInterfaces();
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

    private void implementMethodAttributes() {
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
        var repositoryAnnotation = interfaceElement.getAnnotation(CreateTable.class);
        if (repositoryAnnotation != null) {
            classBuilder.addMethod(MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(OjaiConnector.class, "connection")
                    .addParameter(ITableCreator.class, "tableCreator")
                    .addStatement("this.connection = connection.getConnection()")
                    .addStatement(ATableCreator.createTable(repositoryAnnotation.viaRest()))
                    .build());
            return;

        }
        classBuilder.addMethod(MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(OjaiConnector.class, "connection")
                .addStatement("this.connection = connection.getConnection()")
                .build());
    }

    private void implementMethods() {
        for (Element enclosedElement : this.interfaceElement.getEnclosedElements()) {
            if (enclosedElement.getKind() == ElementKind.METHOD) {
                classBuilder.addMethod(AMethodGenerator.generateMethod((ExecutableElement) enclosedElement, processingEnv, entityClassName, classAttributeList));
            }
        }
    }

    private void implementExtendsMethods() {
        for (var t : this.extendsInterfaces) {
            if (t.getKind() == TypeKind.DECLARED) {
                Element element = ((DeclaredType) t).asElement();
                for (var e : element.getEnclosedElements()) {
                    classBuilder.addMethod(AMethodGenerator.generateMethod((ExecutableElement) e, processingEnv, entityClassName, classAttributeList));
                }
            }
        }
    }

    private void writeImplementedClass() {
        try {
            JavaFile javaFile = JavaFile.builder(this.packageName, this.classBuilder.build())
                    .build();
            javaFile.writeTo(this.processingEnv.getFiler());
        } catch (Exception ex) {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage());
        }
    }
}
