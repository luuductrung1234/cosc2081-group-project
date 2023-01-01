package kratos.oms.seedwork;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Checking for array-like data structure (String/SequenceChar, List, array)
 * should not contain specified String
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotContain {
    public String value() default "";
    public String message() default "Given input must not contains character ''";
}
