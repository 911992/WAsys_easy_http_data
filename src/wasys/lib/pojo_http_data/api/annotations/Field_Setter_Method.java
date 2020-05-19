/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Field_Setter_Method.java
Created on: May 13, 2020 5:30:43 PM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author https://github.com/911992
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Field_Setter_Method {

    public String name();
}
