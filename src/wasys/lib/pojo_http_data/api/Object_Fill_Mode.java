/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Object_Fill_Mode.java
Created on: May 13, 2020 1:56:51 AM | last edit: May 21, 2020
    @author https://github.com/911992
 
History:
    0.1.3(20200521)
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 * Specifying the parsing/filling type(instance) policy.
 * {@see Fillable_Object}
 * {@see Fillable_Object_Manipulator}
 * @author https://github.com/911992
 */
public enum Object_Fill_Mode {
    
    /**
     * Tells the parser, to parse/set-state(fill) the object by reflection.
     */
    Reflection_Type_Fields,
    
    /**
     * Tells the parser, calling the associated {@link Fillable_Object_Manipulator} for parsing/set-state(fill).
     */
    Type_Manipulator
}
