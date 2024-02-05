package io.github.honoriuss.mapr.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import io.github.honoriuss.mapr.utils.StringUtils;
import org.ojai.store.DocumentStore;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
abstract class AMethodGenerator {
    protected static MethodSpec generateMethod(ExecutableElement enclosedElement, ProcessingEnvironment processingEnvironment,
                                               ClassName entityClassName, List<String> classAttributeList) {
        var methodName = enclosedElement.getSimpleName().toString();
        var parameterSpecs = ProcessorUtils.getParameterSpecs(enclosedElement, processingEnvironment, entityClassName);

        var argumentStringList = new ArrayList<String>();
        for (var argument : enclosedElement.getParameters()) {
            argumentStringList.add(argument.getSimpleName().toString());
        }

        var returnClass = enclosedElement.getReturnType();
        var returnClassType = ProcessorUtils.getClassType(returnClass);
        if (returnClassType.toString().equals("T")) {
            returnClassType = entityClassName;
        }

        var hasReturnType = enclosedElement.getReturnType().toString().equals(void.class.toString());
        var hasListReturnType = ProcessorUtils.isListType(returnClass);

        var queryString = getStoreQuery(methodName, argumentStringList, entityClassName, hasReturnType, hasListReturnType, classAttributeList);

        return MethodSpec.methodBuilder(
                        methodName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnClassType)
                .addParameters(parameterSpecs)
                .beginControlFlow("try ($T store = connection.getStore(dbPath)) ", DocumentStore.class)
                .addStatement(queryString)
                .endControlFlow()
                .build();
    }

    private static String createQueryConditionString(List<QueryModel> eQueryPartList, List<QueryModel> conditionPartList) {
        var res = "";
        var hasCondition = !conditionPartList.isEmpty();

        if (hasCondition) {
            res += String.format("org.ojai.store.QueryCondition condition = connection.newCondition()%s",
                    AQueryConditionCreator.createConditionCodeBlock(conditionPartList));
            res += ".build();\n";
        }
        res += String.format("org.ojai.store.Query query = connection.newQuery()%s",
                AQueryConditionCreator.createQueryCodeBlock(eQueryPartList));
        if (hasCondition) {
            res += ".where(condition)";
        }
        return res + ".build();\n";
    }

    protected static String getStoreQuery(String methodName, ArrayList<String> argumentStringList, ClassName entityClassName,
                                          boolean hasReturnType, boolean hasListReturnType, List<String> classAttributeList) {
        var split = methodName.split("By", 2);
        var crud = ACrudDecider.getCrudType(methodName);
        if (split.length >= 2) {
            methodName = split[1];
        } else {
            methodName = "";
        }

        if (crud == ECrudType.READ) {
            if (methodName.toLowerCase().startsWith("id")) { //TODO add other conditions etc.
                return String.format("return store.findById(%s).toJavaBean(%s.class)", argumentStringList.get(0), entityClassName.simpleName());
            }
            var firstCol = StringUtils.getFirstColumnArgumentInMethodName(methodName, classAttributeList);
            if (firstCol.isPresent()) {
                if (methodName.toLowerCase().startsWith(firstCol.get().toLowerCase())) {
                    methodName = "Like" + methodName;
                }
            }
        }

        var queryConditionModel = AQueryConditionExtractor.extractQueryCondition(methodName, argumentStringList, classAttributeList);

        var queryString = createQueryConditionString(queryConditionModel.getQueryPartList(), queryConditionModel.getConditionPartList());

        return switch (crud) {
            case CREATE -> ACRUDQueryCreator.getCreateString(argumentStringList, hasReturnType);
            case READ -> ACRUDQueryCreator.getReadString(entityClassName.simpleName(), queryString, hasListReturnType);
            case UPDATE -> ACRUDQueryCreator.getUpdateString(argumentStringList);
            case DELETE -> ACRUDQueryCreator.getDeleteString(argumentStringList);
        };
    }

    private static String extractArgumentFirstArg(ArrayList<String> argumentStringList) {
        if (!argumentStringList.isEmpty() && argumentStringList.get(0).toLowerCase().contains("id") && argumentStringList.size() == 1) {
            return argumentStringList.get(0);
        }
        return "";
    }
}
