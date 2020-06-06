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
     * Specifies the minimum len/size is allowed for the type(after decoding)
     * @return the min value
     */
    public double min_len_val() default Double.NEGATIVE_INFINITY;
    
    /**
     * Specifies the maximum len/size is allowed for the type(after decoding)
     * @return the max value
     */
    public double max_len_val() default Double.POSITIVE_INFINITY;
    
    /**
     * Indicates the setter name related to the field(if it comes out of java {@code setAaa} common way).
     * <p>
     * <b>Note:</b> Field types are {@link OutputStream}, or {@link Fillable_Object} do not required any setter method.
     * </p>
     * @return name of the setter method for the field
     */
    public String setter_method_name() default "";
}
