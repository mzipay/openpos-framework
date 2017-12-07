package org.jumpmind.pos.core.model.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jumpmind.pos.core.model.FieldElementType;
import org.jumpmind.pos.core.model.FieldInputType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FormTextField {
    FieldElementType fieldElementType() default FieldElementType.Input;
    FieldInputType fieldInputType() default FieldInputType.AlphanumericText;
    String label() default "";
    String placeholder();
    String pattern() default ".*";
    boolean required() default true;
}
