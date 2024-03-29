package io.github.honoriuss.mapr.query.models;

import io.github.honoriuss.mapr.utils.Assert;

import javax.lang.model.element.TypeElement;
import java.util.Optional;

/**
 * @author H0n0riuss
 */
@Deprecated
public class Query {
    private final ReturnType returnType;
    private final Subject subject;
    private final QueryPart queryPart;
    private final TypeArgs typeArgs;
    private final OrderBy orderBy;

    public Query(TypeElement typeElement, Class<?> clazz) {
        this.returnType = new ReturnType(typeElement);
        this.subject = new Subject(typeElement);
        this.queryPart = new QueryPart(typeElement, clazz);
        this.typeArgs = new TypeArgs(typeElement);
        this.orderBy = new OrderBy(typeElement);
    }

    public Query(String source, Class<?> clazz) {
        Assert.notNull(source, "Source cant be null");
        Assert.notNull(clazz, "Class (clazz) cant be null. Needed for attributes.");

        this.returnType = new ReturnType(source);
        this.subject = new Subject(source);
        this.queryPart = new QueryPart(source, clazz);
        this.typeArgs = new TypeArgs(source);
        this.orderBy = new OrderBy(source);
    }

    public ReturnType getReturnType() {
        return returnType;
    }

    public Optional<Subject> getSubject() {
        if (this.subject.getSubjectType().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.subject);
    }

    public Optional<QueryPart> getQueryParts() {
        if (this.queryPart.getEQueryTypeList().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.queryPart);
    }

    public Optional<TypeArgs> getTypeArgs() {
        if (this.typeArgs.getTypeArgModelList().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.typeArgs);
    }

    public Optional<OrderBy> getOrderBy() {
        if (this.orderBy.getAttributesToOrderBy().isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(this.orderBy);
    }
}
