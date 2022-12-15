package kratos.oms.seedwork;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Length {
    public int max() default 255;
    public int min() default 0;
    public String message() default "Given input must have valid length between 0 and 255 characters.";
}
