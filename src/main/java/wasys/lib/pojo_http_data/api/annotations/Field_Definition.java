/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

/*
WAsys_pojo_http_data
File: Field_Definition.java
Created on: May 13, 2020 3:20:13 PM
    @author https://github.com/911992
 
History:
    0.2.5 (20200813)
        • Removed min_len_val and max_len_val fields
        • Added min_float_point_val:double, and max_float_point_val:double for double/float related param definitions
        • Added min_val_or_len:long and max_val_or_len:long for size/len and integer related fields
        • Updated the docs!

    0.2 (20200605)
        • Updated/fixed documentation

    0.1.7(20200526)
        • param_name now comes with default an empty value, when param name is same as field name

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
*/

package wasys.lib.pojo_http_data.api.annotations;

import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.api.Fillable_Object_Field_Signature;


/**
 * Marks a field(attribute) of a POJO({@link Fillable_Object}) as fillable one with more meta data.
 * <p>
 * This type is almost like {@link Fillable_Object_Field_Signature}, which is useful when some meta data about the param is needed to be introduces when the fill mode is reflection.
 * </p>
 * <p>
 * <b>Note:</b> if annotating a float-point field(like {@code double}, or {@code double}), then mind to set only {@code max_float_point_val}, and/or {@code max_float_point_val} attributes if checking is a thing.<br>
 * Same for non floating-point field, only set {@code min_val_or_len} and/or {@code max_val_or_len} fields only.
 * </p>
 * @author https://github.com/911992
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field_Definition {
    /**
     * Specifies the http parameter name.
     * @return a non-{@code null}, anon empty string 
     */
    public String param_name() default "";
    
    /**
     * Specifies the parameter index.
     * @return a non-negative int val
     */
    public int param_idx() default 0;
    
    /**
     * Specifies if the parameter is able to {@code null}(missed), or not
     * @return {@code true} if param is not essential
     */
    public boolean nullable() default false;
    
    /**
     * Specifies the minimum value for integer-like({@code long},{@code int},etc), or the minimum size/len allowed for sizable types({@code String}/streams).
     * <p>
     * For sizable types as {@code String}, or any {@link OutputStream}, the minimum size allowed is considered.
     * </p>
     * <p>
     * <b>Note:</b> if annotating a float-point field, use {@code min_float_point_val} instead.
     * </p>
     * @since 0.2.5
     * @return the minimum value/size allowed for integer-based/sizable type
     */
    public long min_val_or_len() default Long.MIN_VALUE;
    
    /**
     * Specifies the maximum value for integer-like({@code long},{@code int},etc), or the maximum size/len allowed for sizable types({@code String}/streams).
     * <p>
     * For sizable types as {@code String}, or any {@link OutputStream}, the maximum size allowed is considered.
     * </p>
     * <p>
     * <b>Note:</b> if annotating a float-point field, use {@code max_float_point_val} instead.
     * </p>
     * @since 0.2.5
     * @return the maximum value/size allowed for integer-based/sizable type
     */
    public long max_val_or_len() default Long.MIN_VALUE;
    
    /**
     * Specifies the minimum real-precision value is allowed for the {@code double}, or {@code float} type/param(after decoding).
     * <p>
     * <b>Note:</b> since version 0.2.5 this value is used only for {@code double} or {@code float} annotated fields/param.
     * </p>
     * <p>
     * <b>Note:</b> if annotating a <b><i>non</i></b> float-point field, use {@code min_val_or_len} instead.
     * </p>
     * @since 0.2.5
     * @return the min value allowed for {@code double} or {@code float} type
     */
    public double min_float_point_val() default Double.NEGATIVE_INFINITY;
    
    /**
     * Specifies the maximum real-precision value is allowed for the {@code double}, or {@code float} type/param(after decoding).
     * <p>
     * <b>Note:</b> since version 0.2.5 this value is used only for {@code double} or {@code float} annotated fields/param.
     * </p>
     * <p>
     * <b>Note:</b> if annotating a <b><i>non</i></b> float-point field, use {@code max_val_or_len} instead.
     * </p>
     * @since 0.2.5
     * @return the max value allowed for {@code double} or {@code float} type
     */
    public double max_float_point_val() default Double.POSITIVE_INFINITY;
    
    /**
     * Indicates the setter name related to the field(if it comes out of java {@code setAaa} common way).
     * <p>
     * <b>Note:</b> Field types are {@link OutputStream}, or {@link Fillable_Object} do not required any setter method.
     * </p>
     * @return name of the setter method for the field
     */
    public String setter_method_name() default "";
}
