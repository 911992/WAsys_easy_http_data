/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Bad_Type_Cache_Object_Ex.java
Created on: May 13, 2020 10:30:31 PM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    0.2 (20200605)
        • Updated/fixed documentation

    0.1.3(20200521)
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.exception;

import wasys.lib.pojo_http_data.Fillable_Object_Parse_Result;

/**
 * Exception by parser/filler, when the returned global cache(impl-specific, but probably {@link Fillable_Object_Parse_Result}) object is not the one should be.
 * @author https://github.com/911992
 */
public class Bad_Type_Cache_Object_Ex extends java.lang.ClassCastException {

}
