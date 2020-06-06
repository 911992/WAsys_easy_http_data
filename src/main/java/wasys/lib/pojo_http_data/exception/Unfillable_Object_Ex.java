/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Unfillable_Object_Ex.java
Created on: May 14, 2020 6:43:23 PM
    @author https://github.com/911992
 
History:
    0.2 (20200605)
        • Updated/fixed documentation

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.exception;

import wasys.lib.pojo_http_data.api.Fillable_Object;

/**
 * This ex is thrown by filler, when a given POJO({@link Fillable_Object}) has no any fillable field.
 * @author https://github.com/911992
 */
public class Unfillable_Object_Ex extends java.lang.IllegalStateException {

}
