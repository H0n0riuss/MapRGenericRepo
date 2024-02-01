package io.github.honoriuss.mapr.query.producer;

import com.squareup.javapoet.CodeBlock;
import org.ojai.store.QueryCondition;

import java.util.List;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
@Deprecated
public abstract class QueryMapper {
    private static final Pattern ORDER_TYPE = Pattern.compile("(order|Order|orderBy|OrderBy)");
    private static final Pattern LIMIT_TYPE = Pattern.compile("(limit|Limit)");
    private static final Pattern OFFSET_TYPE = Pattern.compile("(offset|Offset)");
    private static final Pattern SINGLE_CONDITION_TYPE = Pattern.compile("(Is|In|Exists|NotExists)"); //TODO Rest aufnehmen
    private static final Pattern NO_CONDITION_TYPE = Pattern.compile("(Or|And)");
    private static final Pattern DOUBLE_CONDITION_TYPE = Pattern.compile("(LIKE)");

    public static String mapKeywordToSupportedType(String keyword) {
        if (ORDER_TYPE.matcher(keyword).find()) {
            return ".orderBy($L, $L)";
        } else if (LIMIT_TYPE.matcher(keyword).find()) {
            return ".limit($L)";
        } else if (OFFSET_TYPE.matcher(keyword).find()) {
            return ".offset($L)";
        }
        return "";
    }

    public static String createCondition(List<String> literals) {
        var codeBlock = CodeBlock.builder()
                .add("$T condition = connection.newCondition()", QueryCondition.class);
        var iterator = literals.iterator();
        while (iterator.hasNext()) {
            var literal = iterator.next();
            if (NO_CONDITION_TYPE.matcher(literal).find()) {
                codeBlock.add(".$L()", literal);
            } else if (SINGLE_CONDITION_TYPE.matcher(literal).find()) {
                codeBlock.add(".$L($L)", literal, iterator.next());
            } else if (DOUBLE_CONDITION_TYPE.matcher(literal).find()) {
                codeBlock.add(".$L($L, $L)", literal, iterator.next(), iterator.next());
            }
        }

        return codeBlock
                .add(".build()")
                .build().toString();
    }
}
