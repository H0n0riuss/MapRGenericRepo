package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author H0n0riuss
 */
public class ReturnType {
    private static final Pattern MODIFIER = Pattern.compile("(public|private|package)");
    private final boolean isListType; //TODO nötig?
    private final Class<?> returnType;

    public ReturnType(TypeElement typeElement) {
        this.returnType = typeElement.getClass();
        this.isListType = isListType(this.returnType);
    }

    public ReturnType(String source) {
        Assert.notNull(source, "Source cant be null");

        String[] split = source.split(" ");
        removeModifier(split);
        this.returnType = getReturnType(split[0]);

        this.isListType = isListType(this.returnType);
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public boolean isListType() {
        return this.isListType;
    }

    private void removeModifier(String[] split) {
        if (MODIFIER.matcher(split[0]).find()) {
            split[0] = split[1];
        }
    }

    private Class<?> getReturnType(String returnTypeString) {
        if (returnTypeString.equals("void")) {
            return void.class;
        }
        try {
            return Class.forName(returnTypeString);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isListType(Class<?> returnType) {
        return List.class.isAssignableFrom(returnType);
    }

}
