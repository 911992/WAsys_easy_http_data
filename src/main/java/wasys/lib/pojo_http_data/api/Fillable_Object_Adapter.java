/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Adapter.java
Created on: May 13, 2020 4:30:45 AM
    @author https://github.com/911992
 
History:
    0.2.5 (20200813)
        • Documentation fix/update.

    0.2 (20200605)
        • Updated/fixed documentation
        • Reseting state of this instance will not mark the err_msg field as null, instead sets the length to zero.

    0.1.6(20200525)
        • VERY STUPID BUG fix: Fillable_Object_Adapter is no more a Fillable_Object_Parse_Cache_Accelerator
        • Drop methods _api_ex_set_type_parse_result, and _api_ex_get_type_parse_result methods
        • Removed static type_parse field

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc
        • Changed method has_failed_field_happened to has_failed_field_happened
        • Changed field last_known_priority_fields_fill to last_known_failed_fields_fill
        • Removed method is_object_in_a_valid_state
        • Marked method has_failed_field_happened as final
        • Moved child_reset_state() method from Poolable_Fillable_Object_Adapter to here
        • Dropped AutoClosable interface implementtion, removed close() method (moved to Poolable_Fillable_Object_Adapter)

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

import wasys.lib.pojo_http_data.api.annotations.No_Param;

/**
 * Adapter class of {@link Fillable_Object} interface, that implements other interfaces too.
 * 
 * @author https://github.com/911992
 */
public abstract class Fillable_Object_Adapter implements Fillable_Object, Fillable_Object_Manipulator{

    /**
     * Private (internal-use) interface definition, about a error message setter.
     */
    private static interface _Err_Message_Setter {
        /**
         * Adds the given {@code arg_msg}, to {@code arg_ins}
         * @param arg_ins the instance of {@code Fillable_Object_Adapter}
         * @param arg_msg the error message
         */
        void add_err_message(Fillable_Object_Adapter arg_ins, String arg_msg);
    };

    /**
     * An implementation of {@link  _Err_Message_Setter}, that perform real error message append op.
     */
    @No_Param
    private static final _Err_Message_Setter DEFAULT_MSG_SETTER = new _Err_Message_Setter() {
        
        /**
         * Appends the {@code arg_msg} to {@code arg_ins}'s {@code err_msg} field.
         */
        @Override
        public void add_err_message(Fillable_Object_Adapter arg_ins, String arg_msg) {
            if (arg_ins.err_msg_need_nl) {
                arg_ins.err_msg.append(arg_ins.err_msg_new_line());
            }
            arg_ins.err_msg.append(arg_msg);
            arg_ins.err_msg_need_nl = true;
        }
    };

    /**
     * Initializes the {@code null} ptr {@code err_msg} field of {@code arg_ins} instance, and then attempt to call teh real {@code DEFAULT_MSG_SETTER} message setter.
     * <p>
     * This is the default {@link _Err_Message_Setter} concreted field for any new instance of {@link Fillable_Object_Adapter}.
     * </p>
     * <p>
     * Once it's called, it replaces its reference of taget {@link Fillable_Object_Adapter}, with real {@code DEFAULT_MSG_SETTER} once.
     * </p>
     */
    @No_Param
    private static final _Err_Message_Setter SETTER_MSG_SETTER = new _Err_Message_Setter() {
        
        /**
         * Replaces itself, with real {@code DEFAULT_MSG_SETTER} message setter.
         * <p>
         * Once the ptr of {@link _Err_Message_Setter} is changed of target {@code arg_ins}(thread-safe), it forwards the exact same message to it.
         * </p>
         */
        @Override
        public void add_err_message(Fillable_Object_Adapter arg_ins, String arg_msg) {
            synchronized (arg_ins) {
                arg_ins.err_msg = new StringBuilder();
                arg_ins.set_msg_setter(Fillable_Object_Adapter.DEFAULT_MSG_SETTER);
                arg_ins.msg_setter.add_err_message(arg_ins, arg_msg);
            }
        }
    };

    /**
     * Tells if the next message append to {@code err_msg} needs a linefeed {@code \n}
     */
    @No_Param
    private boolean err_msg_need_nl = false;
    
    /**
     * Keeps the error messages, were generated/given by filler.
     * <p>
     * By default it's {@code null}, and it's initialized once it's required.
     * </p>
     * <p>
     * If this instance needs to be reset(e.g. reusing), then the length(content) of the ptr will be set to zero.
     * </p>
     */
    @No_Param
    private StringBuilder err_msg = null;
    
    /**
     * The default message setter, by initialization of the instance.
     * <p>
     * this field is initialized to {@code SETTER_MSG_SETTER}, that will sets the real/default by the very first err message event.
     * </p>
     */
    @No_Param
    private _Err_Message_Setter msg_setter = SETTER_MSG_SETTER;
    
    /**
     * keeps the filler-provided overall POJO filling result set.
     */
    @No_Param
    private Object_Fill_Result object_field_result = null;
    
    /**
     * keeps the last known failed field filling.
     * <p>
     * <b>Note:</b> If only first field of POJO got failed to fill, but rest are okay, so this field will reminds the failed status.
     * </p>
     */
    @No_Param
    private Field_Fill_Result last_known_failed_fields_fill = null;

    /**
     * Setter method for {@code msg_setter} field
     * @param arg_msg_settter  instance to be set to {@code msg_setter}
     */
    private void set_msg_setter(_Err_Message_Setter arg_msg_settter) {
        this.msg_setter = arg_msg_settter;
    }

    /**
     * Returns the all {@code err_msg_new_line()} separated error message from filler about failed fill results.
     * @return the message contains all failed fields messages, or {@code null} if no any message were provided.
     */
    public StringBuilder get_fields_fill_msg() {
        return err_msg;
    }

    /**
     * The delimiter needed between each failed field error message concat.
     * @return a delimiter string, to be added at the end of each err message
     */
    protected String err_msg_new_line() {
        return "\n";//or maybe <br/>?
    }

    /**
     * Check if the object filling was success or not.
     * <p>This method <b>must</b> be called when filling op is finished, otherwise wrong state would be given.</p>
     * @return {@code true} if the object filling was success, {@code false} otherwise, or no any object filling happened yet.
     */
    public boolean was_object_filling_success() {
        return (object_field_result == Object_Fill_Result.Ok || object_field_result == Object_Fill_Result.Ok_Missed_Values);
    }

    /**
     * Indicates if any field filling happened yet.
     * <p>This method could be called during the filling method. it returns {@code true} if the POJO is already in failed state, so further op could be skipped.</p>
     * @return {@code true} if no any field had failed result yet, otherwise {@code true}
     */
    final public boolean has_failed_field_happened() {
        return last_known_failed_fields_fill != null;
    }

    
    /**
     * Tells the object filling result.
     * @return the filling result was given by filler, or {@code null} if it's not applicable yet
     */
    public Object_Fill_Result get_object_field_result() {
        return object_field_result;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void set_object_fill_result(Object_Fill_Result arg_result) {
        this.object_field_result = arg_result;
    }

    /**
     * A protected method for inherited child types to grab the {@code set_field_fill_result} method.
     * <p>Since it's not overridable(finalized by this type).</p>
     * <p>Simply forwards the {@code set_field_fill_result} message from teh filler to this <u>overridable</u> method.</p>
     * @param arg_param_name the parameter name from http data
     * @param arg_idx index of parameter data
     * @param arg_result the reason about the fail (always a failed one, never a success result)
     */
    protected void set_field_fill_result_child(String arg_param_name, int arg_idx, Field_Fill_Result arg_result) {

    }

    /**
     * {@inheritDoc }
     */
    @Override
    final public void set_field_fill_result(String arg_param_name, int arg_idx, Field_Fill_Result arg_result) {
        last_known_failed_fields_fill = (last_known_failed_fields_fill == null) ? arg_result : last_known_failed_fields_fill.set_if_overridable(arg_result);
        set_field_fill_result_child(arg_param_name, arg_idx, arg_result);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void set_field_fill_result_err_msg(String arg_param_name, int arg_idx, String[] arg_err_mes) {
        for (int a = 0; a < arg_err_mes.length; a++) {
            String msg = String.format("%s (%s)", arg_err_mes[a], arg_param_name);
            msg_setter.add_err_message(this, msg);
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Fillable_Object_Manipulator get_type_descriptor() {
        return this;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Fillable_Object_Field_Signature[] get_field_signatures() {
        return null;
    }

    /**
     * Overridable method, for inherited types would like perform specific ops, when POJO needs to be reset.
     * <p>
     * Since the {@code reset_fill_state} method is marked as {@code final}, so any child type won't be able to override it.
     * </p>
     * <p>
     * This method is called by {@code reset_fill_state}.
     * </p>
     */
    protected void child_reset_state() {

    }
    
    /**
     * Resets this parent fill state to init, is ready now for another fill op.
     * <p>
     * Field {@code err_msg} will be set to zero length, instead of a {@code null} ptr, to avoid re-instancing types.
     * </p>
     */
    final public void reset_fill_state(){
        last_known_failed_fields_fill = null;
        object_field_result = null;
        err_msg_need_nl = false;
        if(err_msg!=null){
            err_msg.setLength(0);
        }
        child_reset_state();
    }
    
}
