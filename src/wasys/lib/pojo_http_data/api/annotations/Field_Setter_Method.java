/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Field_Setter_Method.java
Created on: May 13, 2020 5:30:43 PM
    @author https://github.com/911992
 
History:
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
 * Mark a method as a setter for a <u>POJO</u>(<b>not</b> a http param) field.
 * The field as pointed should be available, and comes with same type as method input arg.
 * @author https://github.com/911992
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Field_Setter_Method {
    /**
     * @return name of the POJO's field(attribute) name
     */
    public String name();
}
