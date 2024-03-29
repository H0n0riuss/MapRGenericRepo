package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author H0n0riuss
 */
@Deprecated
public class TypeArgs {
    private final ArrayList<TypeArgModel> typeArgModelList = new ArrayList<>();

    public TypeArgs(TypeElement typeElement) {
        for (var arg : typeElement.getTypeParameters()) {
            var typeArgMode = new TypeArgModel(arg.getClass().getTypeName(), arg.getSimpleName().toString());
            this.typeArgModelList.add(typeArgMode);
        }
    }

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

    public Optional<List<TypeArgModel>> getTypeArgModelList() {
        if (this.typeArgModelList.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.typeArgModelList);
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
