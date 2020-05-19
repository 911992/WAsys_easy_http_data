/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Manipulator.java
Created on: May 13, 2020 4:27:07 AM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 *
 * @author https://github.com/911992
 */
public interface Fillable_Object_Manipulator {

    public Fillable_Object_Field_Signature[] get_field_signatures();

    default public Object_Fill_Mode field_set_mode() {
        return Object_Fill_Mode.Reflection_Type_Fields;
    }

    default public void set_field_value(String param_name, int param_idx, Object arg_value) {
    }
}
