package kratos.oms.seedwork;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply regular expression matching for input type String
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Match {
    public String regex() default "";
    public boolean caseSensitive() default true;
    public String message() default "Given input is not matched";
}
