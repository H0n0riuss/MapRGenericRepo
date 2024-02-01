package io.github.honoriuss.mapr.generator.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@Documented
public @interface Repository {
    String tablePath() default "";
}
