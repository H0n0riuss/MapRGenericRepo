package io.github.honoriuss.mapr.query.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Repository {
    String tablePath() default "";
}
