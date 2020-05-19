/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object.java
Created on: May 13, 2020 1:17:53 AM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

import java.io.InputStream;

/**
 *
 * @author https://github.com/911992
 */
public interface Fillable_Object {

    default public Object_Fill_Mode fill_mode() {
        return Object_Fill_Mode.Reflection_Type_Fields;
    }

    default public Part_Field_Stream_Mode part_io_stream_mode() {
        return Part_Field_Stream_Mode.Stream_To_Field;
    }

    default public boolean generate_result_err_msg() {
        return true;
    }

    default public void set_object_fill_result(Object_Fill_Result arg_success) {
    }

    default public void set_field_fill_result(String arg_param_name, int arg_idx, Field_Fill_Result arg_result) {
    }

    default public void set_field_fill_result_err_msg(String arg_param_name, int arg_idx, String arg_err_mes[]) {
    }

    default public Fillable_Object_Manipulator get_type_descriptor() {
        return null;
    }

    default public boolean prepare_for_part(String arg_param_name, int arg_param_idx, String arg_part_filename, long arg_part_size, String arg_part_mime) {
        return true;
    }

    default public void part_stream(String arg_param_name, int arg_idx, InputStream arg_part_stream) {
    }

}
