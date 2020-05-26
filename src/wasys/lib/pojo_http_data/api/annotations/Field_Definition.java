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
    0.1.7(20200526)
        • param_name now comes with default an empty value, when param name is same as field name

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
*/

package wasys.lib.pojo_http_data.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks a field(attribute) of a POJO({@link Fillable_Object}) as fillable one with more meta data.
 * This type is almost like {@link Fillable_Object_Field_Signature}, which is useful when some meta data about the param is needed to be introduces when the fill mode is reflection.
 * @author https://github.com/911992
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field_Definition {
    /**
     * @return specifies the http parameter name
     */
    public String param_name() default "";
    /**
     * @return specifies the parameter index
     */
    public int param_idx() default 0;
    /**
     * @return specifies if the parameter is able to null(missed), or not
     */
    public boolean nullable() default false;
    /**
     * @return specifies the minimum len/size is allowed for the type(after decoding)
     */
    public double min_len_val() default Double.NEGATIVE_INFINITY;
    /**
     * @return specifies the maximum len/size is allowed for the type(after decoding)
     */
    public double max_len_val() default Double.POSITIVE_INFINITY;
    /**
     * @return indicates the setter name related to the field(if it comes out of java setAaa common way)
     */
    public String setter_method_name() default "";
}
