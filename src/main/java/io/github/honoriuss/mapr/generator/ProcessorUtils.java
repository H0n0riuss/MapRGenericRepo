package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
public abstract class ProcessorUtils {
    public static List<ParameterSpec> getParameterSpecs(ExecutableElement enclosedElement,
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

    public static boolean isListType(Class<?> returnType) {
        return List.class.isAssignableFrom(returnType);
    }
}
