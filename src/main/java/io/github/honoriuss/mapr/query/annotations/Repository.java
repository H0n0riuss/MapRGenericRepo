package io.github.honoriuss.mapr.query.annotations;

import io.github.honoriuss.mapr.repositories.entities.AEntity;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Repository {
    Class<? extends AEntity> clazz();
    String tablePath() default "";
}
