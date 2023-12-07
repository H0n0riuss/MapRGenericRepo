package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author H0n0riuss
 */
public class TypeArgs {
    private final ArrayList<TypeArgModel> typeArgModelList = new ArrayList<>();

    public TypeArgs(String source) {
        Assert.notNull(source, "Source can`t be null");
        extractArgs(source);
    }

    private void extractArgs(String source) {
        var split = source
                .split("\\(")[1]
                .split("\\)")[0];
        if (split.isEmpty()) {
            return;
        }
        var args = split.split(",");
        for (var arg : args) {
            arg = arg.trim();
            var ar = arg.split(" ");
            var typeArgModel = new TypeArgModel(ar[0], ar[1]);
            typeArgModelList.add(typeArgModel);
        }
    }

    public List<TypeArgModel> getTypeArgModelList() {
        return this.typeArgModelList;
    }

    public class TypeArgModel {
        public final String argType;
        public final String argName;

        public TypeArgModel(String argType, String argName) {
            this.argType = argType;
            this.argName = argName;
        }
    }
}
