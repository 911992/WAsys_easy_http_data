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
    0.1.8(20200528)
        • (a bad bug fix, sorry for that -_- ), fixed the redundant get_param_at() invocation, when the field is an OutputStream

    0.1.4(20200524)
        • File upload processing, using Pass_Stream now is done by a try-catch, since getting stream of http requ may throw an IOException, or returns a null reference
            • Set Object/field filling as failed, becasue of an IOException, or unexpected null stream ptr

    0.1.3(20200521)
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
import java.util.Vector;
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
 * It follows the default filling policy which defined by API({@link Fillable_Object})
 * </p>
 * @author https://github.com/911992
 */
public class Generic_Object_Filler {

    final private static int FIELD_SET_IGNORED_FILLABLE_TYPE = 0;
    final private static int FIELD_SET_SUCCESS_WITH_DATA = 1;
    final private static int FIELD_SET_SUCCESS_NO_DATA = 2;
    final private static int FIELD_SET_FAILED = 3;

    private Generic_Object_Filler() {
    }

    /**
     * Fills the given {@code arg_obj}, from given {@code arg_data_handler}.
     * <p>
     * If the given {@code arg_obj}'s type is not parsed yet, it gets parsed 
     * </p>
     * @param arg_data_handler the wrapper/implementation of actual HTTP request (provided by the HTTP Server Component)
     * @param arg_obj The object is desired to be filled
     * @throws Unfillable_Object_Ex where given {@link arg_obj} is not fillable
     */
    public static void process_request(Request_Data_Handler arg_data_handler, Fillable_Object arg_obj) throws Unfillable_Object_Ex{
        Vector<Class> _ex_classes = new Vector<>(3, 7);
        process_request(arg_data_handler, arg_obj, _ex_classes);
    }

    private static void process_request(Request_Data_Handler arg_data_handler, Fillable_Object arg_obj, Vector<Class> arg_exclude_types) throws Unfillable_Object_Ex{
        if (arg_obj == null) {
            return;
        }
        Fillable_Object_Parse_Result _cache = Fillable_Object_Parser.find_or_parse_object(arg_obj);
        if (_cache == null) {
            throw new Unfillable_Object_Ex();
        }
        Class _arg_obj_typ = arg_obj.getClass();
        arg_exclude_types.add(_arg_obj_typ);
        Vector<Fillable_Object_Field_Signature_Cache> _field_sigs = _cache.getFields();
        Object_Fill_Result _obj_fill = Object_Fill_Result.Ok;
        boolean _gen_err_msg = arg_obj.generate_result_err_msg();
        for (int a = 0; a < _field_sigs.size(); a++) {
            Fillable_Object_Field_Signature_Cache _fsigc = _field_sigs.elementAt(a);
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
                        process_request(arg_data_handler, _fo, arg_exclude_types);
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
            if (_set == FIELD_SET_IGNORED_FILLABLE_TYPE) {

            }
        }
        arg_obj.set_object_fill_result(_obj_fill);
    }

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
        double _min = _fsig.getMin_len_val();
        long _minl = (long) _min;
        double _max = _fsig.getMax_len_val();
        long _maxl = (long) _max;
        if (_type == String.class) {
            set_field_data(arg_obj, arg_field_sig_cache, _data, _fill_mode);
            if ((_data.length() < _minl) || (_data.length() > _maxl)) {
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
            if (_part_size < _minl || _part_size > _maxl) {
                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_Outof_Range,arg_gen_err_msg);
                _set_res = FIELD_SET_FAILED;
            } else {
                boolean _part_ok = arg_obj.prepare_for_part(_param_name, _param_idx, _part_name, _part_size, _part_mime);
                if (_part_ok == true) {
                    switch (arg_obj.part_io_stream_mode()) {
                        case Stream_To_Field: {
                            try {
                                OutputStream _out = (OutputStream) arg_field_sig_cache.getType_field().get(arg_obj);
                                long _written = arg_data_handler.stream_part_at(_param_name, _param_idx, _out);
                                if (_written != _part_size) {
                                    result_event(arg_obj, _fsig, Field_Fill_Result.Failed_IO_Error,arg_gen_err_msg);
                                    _set_res = FIELD_SET_FAILED;
                                }
                                _out.flush();
                            } catch (NullPointerException | IOException e) {
                                result_event(arg_obj, _fsig, Field_Fill_Result.Failed_IO_Error,arg_gen_err_msg);
                                _set_res = FIELD_SET_FAILED;
                            } catch (Throwable e) {
                            }
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
                if ((_d < _min) || (_d > _max)) {
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
                if (_out_of_range || (_l < _minl) || (_l > _maxl)) {
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
                    if (arg_fsig.getMin_len_val() > Double.NEGATIVE_INFINITY) {
                        _r.append("min: ").append(arg_fsig.getMin_len_val());
                        _need_com = true;
                    }
                    if (arg_fsig.getMax_len_val() < Double.POSITIVE_INFINITY) {
                        if (_need_com) {
                            _r.append(",");
                        }
                        _r.append("max: ").append(arg_fsig.getMax_len_val());
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
