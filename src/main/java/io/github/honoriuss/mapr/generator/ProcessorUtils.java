package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
abstract class ProcessorUtils {
    protected static List<ParameterSpec> getParameterSpecs(ExecutableElement enclosedElement,
                                                        ProcessingEnvironment processingEnvironment,
                                                        ClassName entityClassName) {
        List<ParameterSpec> parameterSpecs = new ArrayList<>(); //TODO refactor
        for (VariableElement parameter : enclosedElement.getParameters()) {
            // Konvertiere den Parameter in einen ParameterSpec und füge ihn zur Liste hinzu
            var typeParameter = parameter.asType();
            if (typeParameter.toString().equals("T")) {
                typeParameter = processingEnvironment.getElementUtils()
                        .getTypeElement(entityClassName.toString())
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

    protected static boolean isListType(TypeMirror returnType) {
        if (returnType.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredReturnType = (DeclaredType) returnType;
            TypeElement returnElement = (TypeElement) declaredReturnType.asElement();
            return returnElement.getQualifiedName().contentEquals(List.class.getName());
        }
        return false;
    }

    protected static TypeName getClassType(TypeMirror returnClass) {
        return TypeName.get(returnClass);
    }
}
