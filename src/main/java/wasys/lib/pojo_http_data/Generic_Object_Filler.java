/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Generic_Object_Filler.java
Created on: May 14, 2020 5:04:45 PM
    @author https://github.com/911992
 
History:
    0.3.3 (20200829)
        • Removed import of Pool_Context(since it's no more)
        • Creating the pooled internal array-list by Generic_Object_Pool

    0.2.9 (20200823)
        • Changed ARRAYLIST_DEFAULT_POOL_MAX_VAL to 1

    0.2.5 (20200813)
        • Fixes/changes becasue of Field_Definition and Fillable_Object_Field_Signature types changes (err msg generating, and size/len check)
        • Getting related min/max values of related field signature during value bound/size check
        • Using Number instead of pair of double/long for keeping a cache of min/max values
        • String value lenght now is checked by int value, rather than long

    0.2 (20200605)
        • Added some documentation
        • Using ArrayList(non thread-safe) instead of Vector(thread-safe) as thread-safety is not considered, or covered
        • Using internal pooled version of ArrayList(Poolable_ArrayList) when a filling is requested for holding filling types in process_request, and process_request_internal methods
        • Added ARRAYLIST_POOL_MAX_VAL_LOOKUP_KEY:String, and ARRAYLIST_POOL:Object_Pool static fields
        • Added static init_arraylist_pool() method, and a static block that calls it during class loading
        • Renamed method process_request(:Request_Data_Handler,:Fillable_Object,:ArrayList<Class>):void to process_request_internal (polymorphism is good, but unique names are better)

    0.1.11 (20200601)
        • Calling method part_streaming_done(), when a part stream should be performed as Stream_To_Field mode, to inform if io stream was ok by related Request_Data_Handler

    0.1.10 (20200531)
        • Removed a redundant if block, from process_request() method

    0.1.8 (20200528)
        • (a bad bug fix, sorry for that -_- ), fixed the redundant get_param_at() invocation, when the field is an OutputStream

    0.1.4 (20200524)
        • File upload processing, using Pass_Stream now is done by a try-catch, since getting stream of http requ may throw an IOException, or returns a null reference
            • Set Object/field filling as failed, becasue of an IOException, or unexpected null stream ptr

    0.1.3 (20200521)
        • Updated the header(this comment) part
        • Added some javadoc
        • Added missed Unfillable_Object_Ex throws clause for process_request methods
        • Cache the "field fill err message generation" to fill(object) level, now the Fillable_Object.generate_result_err_msg() is called once for each fill(no matter how many errs)
        • Updated(added param) read_and_set_param, and set_result methods. Added a bool to specify if the error mesage generation is a thing
        • Method result_event(), Calling the failed set field error message, after field state event (first: set_field_fill_result() , then: set_field_fill_result_err_msg())

    0.1.2 (20200520)
        • Fixed the wrong type check at read_and_set_param (Poolable_Object -> Fillable_Object)
        • Removed the redundant Poolable_Object type import

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.naming.InitialContext;
import wasys.lib.generic_object_pool.Full_Pool_Object_Creation_Policy;
import wasys.lib.generic_object_pool.Generic_Object_Pool;
import wasys.lib.generic_object_pool.Generic_Object_Pool_Policy;
import wasys.lib.generic_object_pool.Object_Pool;
import wasys.lib.pojo_http_data.api.Field_Fill_Result;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.api.Fillable_Object_Field_Signature;
import wasys.lib.pojo_http_data.api.Object_Fill_Mode;
import wasys.lib.pojo_http_data.api.Object_Fill_Result;
import wasys.lib.pojo_http_data.api.container.Request_Data_Handler;
import wasys.lib.pojo_http_data.exception.Unfillable_Object_Ex;

/**
 * Generic(default) Object filler
 * <p>
 * This class asks for parsing({@link Fillable_Object_Parser}) an object's type for filling, if the type has not been parsed yet
 * </p>
 * <p>
 * It follows the default filling policy which defined by API({@link Fillable_Object}).
 * </p>
 * <hr>
 * <p>
 * <b>{@code ARRAY_LIST_POOL} Initialization</b>
 * </p>
 * <p>
 * {@code ARRAY_LIST_POOL} initialization is done during the class loading(by the related class loader), using {@code init_arraylist_pool()} method, that is called by a static block({@code static{}}).
 * </p>
 * @author https://github.com/911992
 */
public class Generic_Object_Filler {

    /**
     * (internal-impl-usage) int value to state if related field is a fillable type, and need to be checked as a dedicated fillable object.
     */
    final private static int FIELD_SET_IGNORED_FILLABLE_TYPE = 0;
    
    /**
     * (internal-impl-usage) int value to state if a field fill was successful with a non-{@code null} value.
     */
    final private static int FIELD_SET_SUCCESS_WITH_DATA = 1;
    
    /**
     * (internal-impl-usage) int value to state if field filling was not placed, because of missed required http parameter, but ok op, since field is a {@code null}able one.
     */
    final private static int FIELD_SET_SUCCESS_NO_DATA = 2;
    
    /**
     * (internal-impl-usage) int value that state the field setting failed!, due to {@code null}, or out of range value.
     */
    final private static int FIELD_SET_FAILED = 3;
    
    /**
     * The string key value to search for "the maximum type array list pool size".
     * <p>
     * This key is used for looking up a <b>String</b> value that contains a prsable/decodable int value, which is used for initializing the local
     * pooled array list ({@code ARRAY_LIST_POOL}).
     * </p>
     * <p>
     * <b>JNDI</b> is searched first, and if the value is missed or invalid, then <b>system properties</b> will be searched.
     * </p>
     * <p>
     * In case of any missed, or invalid(non-decodable, or out of range) value either from JNDI, or system properties, the default {@code ARRAYLIST_DEFAULT_POOL_MAX_VAL} will be used then.
     * </p>
     * @since 0.2
     */
    final public static String ARRAYLIST_POOL_MAX_VAL_LOOKUP_KEY = "WAsys_Generic_Object_Filler_ARRAYLIST_POOL_MAX";
    
    /**
     * Default "pooled array list" maximum pool number.
     * <p>
     * This value is used when looking for user-defined value is failed.
     * </p>
     * @since 0.2
     */
    final private static int ARRAYLIST_DEFAULT_POOL_MAX_VAL = 1;
    
    /**
     * The pool, that handles {@link Poolable_ArrayList} pooled array lists.
     * <p>
     * This value is initialized during the class loading process, using a static block({@code static{}}).
     * </p>
     * <p>
     * The default initialize value for the pool initialization, is {@code 0}.
     * </p>
     * <p>
     * And the default full-pool policy is {@code Full_Pool_Object_Creation_Policy.Create_New_No_Pooling}.
     * </p>
     * <p>
     * Please read documentation of {@link Generic_Object_Filler}.
     * </p>
     * @since 0.2
     */
    private static Object_Pool ARRAY_LIST_POOL;
    
    /*
    Calls the init_arraylist_pool() method, as class gets loaded for the first time.
    since 0.2
     */
    static{
        init_arraylist_pool();
    }
    
    /**
     * Default private constructor, to avoid any redundant object creation.
     * <p>
     * This is not a singleton class, instead it provides its methods as static members.
     * </p>
     */
    private Generic_Object_Filler() {
    }
    
    
    /**
     * The default initializer for the local {@code ARRAY_LIST_POOL} variable.
     * <p>It ties to get the maximum pool size from JNDI, or system properties(if JNDI fails) to check if there is any valid user-defined value for that.</p>
     * <p>The "maximum pool instance number" is the value that related static block tries to find out, since it's possible user to specify it.</p>
     * <p>In case of both JDNI, and sys properties failed, the default {@code ARRAYLIST_DEFAULT_POOL_MAX_VAL} will be used.</p>
     * <p>This method must be called in order to init the pool(to avoid any unexpected null ptr ex later), which is called by a {@code static{}} block of this type.</p>
     * @since 0.2
     */
    private static void init_arraylist_pool(){
        String _val;
        try {
            String _key = String.format("java:comp/env/%s", ARRAYLIST_POOL_MAX_VAL_LOOKUP_KEY);
            InitialContext _ic =new InitialContext();
            _val = (String)_ic.lookup(_key);
        } catch (Exception e) {
            _val = null;
        }
        if(_val == null){
            _val = System.getProperty(ARRAYLIST_POOL_MAX_VAL_LOOKUP_KEY);
        }
        int _max_val;
        if(_val!=null){
            try {
                _max_val = Integer.decode(_val);
                if(_max_val<1){
                    _max_val = ARRAYLIST_DEFAULT_POOL_MAX_VAL;
                }
            } catch (Exception e) {
                _max_val = ARRAYLIST_DEFAULT_POOL_MAX_VAL;
            }
        }else{
            _max_val = ARRAYLIST_DEFAULT_POOL_MAX_VAL;
        }
        Generic_Object_Pool_Policy _pol = new Generic_Object_Pool_Policy(0, _max_val, Full_Pool_Object_Creation_Policy.Create_New_No_Pooling);
        ARRAY_LIST_POOL = Generic_Object_Pool.new_pool_instance(new Poolable_ArrayList.Factory(), _pol);
    }

    /**
     * Fills the given {@code arg_obj}, from given {@code arg_data_handler}.
     * <p>
     * If the given {@code arg_obj}'s type is not parsed yet, it gets parsed first by default parser({@link Fillable_Object_Parser}).
     * </p>
     * @param arg_data_handler the wrapper/implementation of actual HTTP request (provided by the HTTP Server Component)
     * @param arg_obj The object is desired to be filled
     * @throws Unfillable_Object_Ex where given {@code arg_obj} is not valid fillable
     */
    public static void process_request(Request_Data_Handler arg_data_handler, Fillable_Object arg_obj) throws Unfillable_Object_Ex{
        Poolable_ArrayList _ex_classes = (Poolable_ArrayList)ARRAY_LIST_POOL.get_an_instance();
        try{
            process_request_internal(arg_data_handler, arg_obj, _ex_classes);
        }finally{
            ARRAY_LIST_POOL.release_an_instance(_ex_classes);
        }        
    }

    /**
     * Performs the actual filling process (internal-impl).
     * <p>
     * It tries to fill given {@code arg_obj}, from {@code arg_data_handler}. Both {@code arg_obj}, and {@code arg_data_handler} must be non-{@code null} ptrs.
     * </p>
     * <p>
     * It avoid filling a type twice for a specific POJO type.
     * </p>
     * <p>
     * It means if given {@code arg_obj} has a ptr to another {@link Fillable_Object}, then it will be filled only-and-only, if the type of fillable has not filled yet.
     * </p>
     * <p>
     * For example, if a type {@code A} has two pointers to {@code b0:B}, and {@code b1:B}, so only the {@code b0} will be filled(as field definition order in type {@code A}.
     * </p>
     * <p>
     * This policy is an essential to avoid any unexpected recursion during the filling process.
     * </p>
     * <p>
     * <b>Note:</b> Please mind, the super type of any {@link Fillable_Object} (including the {@code arg_obj}) won't be counted as a unique fillable type.<br>
     * This means, if type {@code arg_obj:A} has inherited from type {@code B}, and there is a ptr to {@code b0:B} in {@code arg_obj}, so it will be filled once(regardless as the type is the same type of the ptr)
     * </p>
     * @param arg_data_handler the wrapper/implementation of actual HTTP request (provided by the HTTP Server Component)
     * @param arg_obj The object is desired to be filled
     * @param arg_exclude_types list of types need to ignored during filling to avoid any recursion
     * @throws Unfillable_Object_Ex if the given {@code arg_obj} is not a logically fillable POJO(nothing to do)
     * @since 2.0 (renamed from {@code process_request} to {@code process_request_internal})
     */
    private static void process_request_internal(Request_Data_Handler arg_data_handler, Fillable_Object arg_obj, ArrayList<Class> arg_exclude_types) throws Unfillable_Object_Ex{
        if (arg_obj == null) {
            return;
        }
        Fillable_Object_Parse_Result _cache = Fillable_Object_Parser.find_or_parse_object(arg_obj);
        if (_cache == null) {
            throw new Unfillable_Object_Ex();
        }
        Class _arg_obj_typ = arg_obj.getClass();
        arg_exclude_types.add(_arg_obj_typ);
        ArrayList<Fillable_Object_Field_Signature_Cache> _field_sigs = _cache.getFields();
        Object_Fill_Result _obj_fill = Object_Fill_Result.Ok;
        boolean _gen_err_msg = arg_obj.generate_result_err_msg();
        for (int a = 0; a < _field_sigs.size(); a++) {
            Fillable_Object_Field_Signature_Cache _fsigc = _field_sigs.get(a);
            int _set = read_and_set_param(arg_data_handler, arg_obj, _fsigc,_gen_err_msg);
            switch (_set) {
                case FIELD_SET_IGNORED_FILLABLE_TYPE: {
                    Fillable_Object_Field_Signature _fsig = _fsigc.getField_signature();
                    Fillable_Object _fo;
                    try {
                        _fo = (Fillable_Object) _fsigc.getType_field().get(arg_obj);
                    } catch (Exception e) {
                        _fo = null;
                    }
                    if (_fo == null) {
                        continue;
                    }
                    Class _type = _fsig.getType();
                    Class _extype = null;
                    for (int m = 0; m < arg_exclude_types.size(); m++) {
                        _extype = arg_exclude_types.get(m);
                        if (_extype == _type) {
                            _fo.set_object_fill_result(Object_Fill_Result.Ignorred_Duplicated_Type);
                            _extype = null;
                            break;
                        }
                    }
                    if (_extype != null) {
                        process_request_internal(arg_data_handler, _fo, arg_exclude_types);
                    }
                    break;
                }
                case FIELD_SET_SUCCESS_NO_DATA: {
                    _obj_fill = _obj_fill.set_if_overridable(Object_Fill_Result.Ok_Missed_Values);
                }
                case FIELD_SET_SUCCESS_WITH_DATA: {
                    break;
                }
                case FIELD_SET_FAILED: {
                    _obj_fill = _obj_fill.set_if_overridable(Object_Fill_Result.Failed);
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        arg_obj.set_object_fill_result(_obj_fill);
    }

    /**
     * Reads and set a field of given fillable pojo.
     * <p>
     * This is a private internal-impl function, that tries to read and fill a non-{@link Fillable_Object} field.
     * </p>
     * <p>
     * If the given field is a {@link Fillable_Object}, it returns an state to inform the object filled the field could be filled as a dedictaed fillable(if applicable, e.g. the type has not filled yet)
     * </p>
     * @param arg_data_handler the wrapper/implementation of actual HTTP request (provided by the HTTP Server Component)
     * @param arg_obj The object is desired its {@code arg_field_sig_cache}(field) to be filled
     * @param arg_field_sig_cache the signature of the field
     * @param arg_gen_err_msg tells if error message should be generated in case of any failed filling or not
     * @return an int state the filling result for the given field, either one of {@code FIELD_SET_???} values
     */
    private static int read_and_set_param(Request_Data_Handler arg_data_handler, Fillable_Object arg_obj, Fillable_Object_Field_Signature_Cache arg_field_sig_cache,boolean arg_gen_err_msg) {
        Fillable_Object_Field_Signature _fsig = arg_field_sig_cache.getField_signature();
        Class _type = _fsig.getType();
        if (Fillable_Object.class.isAssignableFrom(_type)) {
            return FIELD_SET_IGNORED_FILLABLE_TYPE;
        }
        String _param_name = _fsig.getParam_name();
        int _param_idx = _fsig.getParam_idx();
        String _data;
        long _part_size = -1;
        boolean _data_null = false;
        if (OutputStream.class.isAssignableFrom(_type) == false ) {
            _data = arg_data_handler.get_param_at(_param_name, _param_idx);
            _data_null = (_data == null);
        }else{
            _data = null;
            _part_size = arg_data_handler.get_part_size_at(_param_name, _param_idx);
            _data_null = (_part_size == -1);
        }
        if( _data_null ){
            if (_fsig.isNullable()) {
                return FIELD_SET_SUCCESS_NO_DATA;
            } else {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Missed_Data,arg_gen_err_msg);
                return FIELD_SET_FAILED;
            }
        }
        int _set_res = FIELD_SET_SUCCESS_WITH_DATA;
        Object_Fill_Mode _fill_mode = arg_obj.fill_mode();
        if (_fill_mode == Object_Fill_Mode.Type_Manipulator) {
            _fill_mode = arg_obj.get_type_descriptor().field_set_mode();
        }
        Number _minv = _fsig.getMin_val();
        Number _maxv = _fsig.getMax_val();
        if (_type == String.class) {
            set_field_data(arg_obj, arg_field_sig_cache, _data, _fill_mode);
            if ((_data.length() < _minv.intValue()) || (_data.length() > _maxv.intValue())) {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Outof_Range,arg_gen_err_msg);
                _set_res = FIELD_SET_FAILED;
            }
        } else if (OutputStream.class.isAssignableFrom(_type)) {
            String _part_name = arg_data_handler.get_part_filename_at(_param_name, _param_idx);
            String _part_mime = arg_data_handler.get_part_mime_part_at(_param_name, _param_idx);
            if (_part_size == -1) {
                _part_size = arg_data_handler.get_part_size_at(_param_name, _param_idx);
            }
            if (_part_size == -1) {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_IO_Error,arg_gen_err_msg);
                _set_res = FIELD_SET_FAILED;
            }
            if (_part_size < _minv.longValue() || _part_size > _maxv.longValue()) {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Outof_Range,arg_gen_err_msg);
                _set_res = FIELD_SET_FAILED;
            } else {
                boolean _part_ok = arg_obj.prepare_for_part(_param_name, _param_idx, _part_name, _part_size, _part_mime);
                if (_part_ok == true) {
                    switch (arg_obj.part_io_stream_mode()) {
                        case Stream_To_Field: {
                            boolean _io_ok;
                            try {
                                OutputStream _out = (OutputStream) arg_field_sig_cache.getType_field().get(arg_obj);
                                long _written = arg_data_handler.stream_part_at(_param_name, _param_idx, _out);
                                if (_written != _part_size) {
                                    result_event(arg_obj, _fsig, Field_Fill_Result.Failed_IO_Error,arg_gen_err_msg);
                                    _set_res = FIELD_SET_FAILED;
                                }
                                _out.flush();
                                _io_ok = true;
                            } catch (NullPointerException | IOException e) {
                                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_IO_Error,arg_gen_err_msg);
                                _set_res = FIELD_SET_FAILED;
                                _io_ok = false;
                            } catch (Throwable e) {
                                e.printStackTrace();
                                _io_ok = false;
                            }
                            arg_obj.part_streaming_done(_param_name, _param_idx, _io_ok);
                            break;
                        }
                        case Pass_Stream: {
                            boolean _io_ok;
                            try {
                                InputStream _part_stream = arg_data_handler.get_part_stream_at(_param_name, _param_idx);
                                if(_part_stream !=null){
                                    arg_obj.part_stream(_param_name, _param_idx, _part_stream);
                                    _io_ok = true;
                                }else{
                                    _io_ok = false;
                                }
                            } catch (IOException e) {
                                _io_ok=false;
                            }
                            if(_io_ok == false){
                                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_IO_Error,arg_gen_err_msg);
                                _set_res = FIELD_SET_FAILED;
                            }
                            
                            break;
                        }
                        default: {
                            throw new AssertionError();
                        }
                    }
                }
            }
        } else if (_type == Double.class || _type == double.class || _type == Float.class || _type == float.class) {
            try {
                double _d = Double.parseDouble(_data);
                if (_type == Float.class || _type == float.class) {
                    set_field_data(arg_obj, arg_field_sig_cache, (float) _d, _fill_mode);
                } else {
                    set_field_data(arg_obj, arg_field_sig_cache, _d, _fill_mode);
                }
                if ((_d < _minv.doubleValue()) || (_d > _maxv.doubleValue())) {
                    result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Outof_Range,arg_gen_err_msg);
                    _set_res = FIELD_SET_FAILED;
                }
            } catch (Exception e) {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Parse_Error,arg_gen_err_msg);
                _set_res = FIELD_SET_FAILED;
            }
        } else {
            try {
                Long _l = Long.decode(_data);
                Object _lv;
                boolean _out_of_range;
                if (_type == Byte.class || _type == byte.class) {
                    _lv = _l.byteValue();
                    _out_of_range = (_l > Byte.MAX_VALUE || _l < Byte.MIN_VALUE);
                } else if (_type == Short.class || _type == short.class) {
                    _lv = _l.shortValue();
                    _out_of_range = (_l > Short.MAX_VALUE || _l < Short.MIN_VALUE);
                } else if (_type == Integer.class || _type == int.class) {
                    _lv = _l.intValue();
                    _out_of_range = (_l > Integer.MAX_VALUE || _l < Integer.MIN_VALUE);
                } else {
                    _lv = _l.longValue();
                    _out_of_range = false;
                }
                set_field_data(arg_obj, arg_field_sig_cache, _lv, _fill_mode);
                if (_out_of_range || (_l < _minv.longValue()) || (_l > _maxv.longValue())) {
                    result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Outof_Range,arg_gen_err_msg);
                    _set_res = FIELD_SET_FAILED;
                }
            } catch (Exception e) {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Parse_Error,arg_gen_err_msg);
                _set_res = FIELD_SET_FAILED;
            }
        }

        return _set_res;
    }

    /**
     * Sets a non-streamable fields value.
     * <p>
     * Setting a field could be done by associated setter method, or explicitly to the field when there is any setter method defined.
     * </p>
     * <p>
     * If the given {@code arg_fill_mode} is {@code Object_Fill_Mode.Type_Manipulator}, this will be user/pojo duty to perform the field as as asked for manual field set.
     * </p>
     * @param arg_obj The object is desired its {@code arg_field_sig_cache}(field) to be filled
     * @param arg_field_sig_cache the field signature that needs to be filled
     * @param arg_data the data needs to be set to the field
     * @param arg_fill_mode specifies the filling mode should be applied (please check {@link Object_Fill_Mode})
     * @return {@code true} if setting field with given value was success, {@code false} otherwise
     */
    private static boolean set_field_data(Fillable_Object arg_obj, Fillable_Object_Field_Signature_Cache arg_field_sig_cache, Object arg_data, Object_Fill_Mode arg_fill_mode) {
        if (arg_fill_mode == Object_Fill_Mode.Type_Manipulator) {
            arg_obj.get_type_descriptor().set_field_value(arg_field_sig_cache.getType_field().getName(), arg_field_sig_cache.getField_signature().getParam_idx(), arg_data);
            return true;
        }
        Method _setter_method = arg_field_sig_cache.getSetter_method();
        Field _field = arg_field_sig_cache.getType_field();
        boolean _is_primitive = _field.getType().isPrimitive();
        if (_is_primitive && arg_data == null) {
            return false;
        }
        if (_setter_method != null) {
            try {
                _setter_method.invoke(arg_obj, arg_data);
            } catch (Exception e) {
                return false;
            }
        } else {
            try {
                _field.set(arg_obj, arg_data);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * Trigger an event(error message) to the POJO in case of any error during filling a field(if needed).
     * <p>
     * For any success field setting op, there will be no any event as API specifies. So if the field was success, this method does nothing.
     * </p>
     * <p>
     * For any failed field set, there will once call to pojo about the related failed field.
     * </p>
     * <p>
     * The pojo also will have a generated error message about the failed field setting, that is(supposed to be) human friendly, explaining the reason about failed field setting(if asked).
     * </p>
     * <p><b>Note:</b> Any failed event is done after the field is set(if applicable), besides that, teh error message will be <b>after</b> the error message were sent.</p>
     * @param arg_obj The object is desired its {@code arg_field_sig_cache}(field) to be filled
     * @param arg_fsig The signature of the field were filled
     * @param arg_result The field filling result
     * @param arg_gen_err_msg Tells if error message about the failed filling result should be generated and sent or nor.
     */
    private static void result_event(Fillable_Object arg_obj, Fillable_Object_Field_Signature arg_fsig, Field_Fill_Result arg_result,boolean arg_gen_err_msg) {
        if (arg_result == Field_Fill_Result.Ok || arg_gen_err_msg == false) {
            return;
        }
        String _arg_param = arg_fsig.getParam_name();
        int _arg_param_idx = arg_fsig.getParam_idx();
        String msg;
        if (arg_result != Field_Fill_Result.Ok_Missed_Data) {
            switch (arg_result) {
                case Failed_Missed_Data: {
                    msg = "Missed(empty) data";
                    break;
                }
                case Failed_Outof_Range: {
                    StringBuilder _r = new StringBuilder("Out of range value(");
                    boolean _need_com = false;
                    if (arg_fsig.getMin_val().doubleValue() > Double.NEGATIVE_INFINITY) {
                        _r.append("min: ").append(arg_fsig.getMin_val().toString());
                        _need_com = true;
                    }
                    if (arg_fsig.getMax_val().doubleValue() < Double.POSITIVE_INFINITY) {
                        if (_need_com) {
                            _r.append(",");
                        }
                        _r.append("max: ").append(arg_fsig.getMax_val().toString());
                    }
                    _r.append(')');
                    msg = _r.toString();
                    break;
                }
                case Failed_Parse_Error: {
                    msg = "Bad data format";
                    break;
                }
                default: {
                    throw new AssertionError();
                }
            }
        } else {
            msg = null;
        }
        arg_obj.set_field_fill_result(_arg_param, _arg_param_idx, arg_result);
        if (msg != null) {
            arg_obj.set_field_fill_result_err_msg(_arg_param, _arg_param_idx, new String[]{msg});
        }
    }

}
