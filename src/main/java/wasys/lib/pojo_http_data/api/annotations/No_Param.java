/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: No_Param.java
Created on: May 14, 2020 4:51:41 AM
    @author https://github.com/911992
 
History:
    0.2 (20200605)
        • Updated/fixed documentation

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
 * Mark a POJO's field as no param.
 * <p>Parser will ignore it, as filler will not fill it too.</p>
 * @author https://github.com/911992
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface No_Param {

}
