/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Adapter.java
Created on: May 13, 2020 4:30:45 AM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

import wasys.lib.pojo_http_data.api.annotations.No_Param;
import wasys.lib.pojo_http_data.api_ex.Fillable_Object_Parse_Cache_Accelerator;

/**
 *
 * @author https://github.com/911992
 */
public abstract class Fillable_Object_Adapter implements Fillable_Object, Fillable_Object_Manipulator, Fillable_Object_Parse_Cache_Accelerator, AutoCloseable {

    @No_Param
    private static Object type_parse = null;

    private static interface _Err_Message_Setter {

        void add_err_message(Fillable_Object_Adapter arg_ins, String arg_msg);
    };

    @No_Param
    private static final _Err_Message_Setter DEFAULT_MSG_SETTER = new _Err_Message_Setter() {
        @Override
        public void add_err_message(Fillable_Object_Adapter arg_ins, String arg_msg) {
            if (arg_ins.err_msg_need_nl) {
                arg_ins.err_msg.append(arg_ins.err_msg_new_line());
            }
            arg_ins.err_msg.append(arg_msg);
            arg_ins.err_msg_need_nl = true;
        }
    };

    @No_Param
    private static final _Err_Message_Setter SETTER_MSG_SETTER = new _Err_Message_Setter() {
        @Override
        public void add_err_message(Fillable_Object_Adapter arg_ins, String arg_msg) {
            synchronized (arg_ins) {
                arg_ins.err_msg = new StringBuilder();
                arg_ins.set_msg_setter(Fillable_Object_Adapter.DEFAULT_MSG_SETTER);
                arg_ins.msg_setter.add_err_message(arg_ins, arg_msg);
            }
        }
    };

    @No_Param
    private boolean err_msg_need_nl = false;
    @No_Param
    private StringBuilder err_msg = null;
    @No_Param
    private _Err_Message_Setter msg_setter = SETTER_MSG_SETTER;
    @No_Param
    private Object_Fill_Result object_field_result = null;
    @No_Param
    private Field_Fill_Result last_known_priority_fields_fill = null;

    private void set_msg_setter(_Err_Message_Setter arg_msg_settter) {
        this.msg_setter = arg_msg_settter;
    }

    public StringBuilder get_fields_fill_msg() {
        return err_msg;
    }

    protected String err_msg_new_line() {
        return "\n";//or maybe <br/>?
    }

    public boolean was_object_filling_success() {
        return (object_field_result == Object_Fill_Result.Ok || object_field_result == Object_Fill_Result.Ok_Missed_Values);
    }

    public boolean has_object_filling_happened() {
        return last_known_priority_fields_fill != null;
    }

    public Object_Fill_Result get_object_field_result() {
        return object_field_result;
    }

    @Override
    public void set_object_fill_result(Object_Fill_Result arg_result) {
        this.object_field_result = arg_result;
    }

    protected void set_field_fill_result_child(String arg_param_name, int arg_idx, Field_Fill_Result arg_result) {

    }

    @Override
    final public void set_field_fill_result(String arg_param_name, int arg_idx, Field_Fill_Result arg_result) {
        last_known_priority_fields_fill = (last_known_priority_fields_fill == null) ? arg_result : last_known_priority_fields_fill.set_if_overridable(arg_result);
        set_field_fill_result_child(arg_param_name, arg_idx, arg_result);
    }

    @Override
    public void set_field_fill_result_err_msg(String arg_param_name, int arg_idx, String[] arg_err_mes) {
        for (int a = 0; a < arg_err_mes.length; a++) {
            String msg = String.format("%s (%s)", arg_err_mes[a], arg_param_name);
            msg_setter.add_err_message(this, msg);
        }
    }

    @Override
    public Fillable_Object_Manipulator get_type_descriptor() {
        return this;
    }

    @Override
    public Fillable_Object_Field_Signature[] get_field_signatures() {
        return null;
    }

    final protected boolean is_object_in_a_valid_state() {
        return (last_known_priority_fields_fill == null || last_known_priority_fields_fill == Field_Fill_Result.Ok || last_known_priority_fields_fill == Field_Fill_Result.Ok_Missed_Data);
    }

    @Override
    final public void _api_ex_set_type_parse_result(Object arg_arg) {
        Fillable_Object_Adapter.type_parse = arg_arg;
    }

    @Override
    final public Object _api_ex_get_type_parse_result() {
        return Fillable_Object_Adapter.type_parse;
    }
    
    final public void reset_fill_state(){
        last_known_priority_fields_fill = null;
        object_field_result = null;
        err_msg_need_nl = false;
        err_msg = null;
        set_msg_setter(SETTER_MSG_SETTER);
    }

    @Override
    public void close() throws Exception{
        reset_fill_state();
    }
    
}
