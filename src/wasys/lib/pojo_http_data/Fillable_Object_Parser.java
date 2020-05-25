/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Parser.java
Created on: May 13, 2020 10:49:43 PM
    @author https://github.com/911992
 
History:
    0.1.6(20200525)
        • Finding fields in reflection mode as parent-to-child fields order now

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.api.Fillable_Object_Adapter;
import wasys.lib.pojo_http_data.api.Fillable_Object_Field_Signature;
import wasys.lib.pojo_http_data.api.Object_Fill_Mode;
import wasys.lib.pojo_http_data.api.Fillable_Object_Manipulator;
import wasys.lib.pojo_http_data.api.annotations.Field_Definition;
import wasys.lib.pojo_http_data.api.annotations.Field_Setter_Method;
import wasys.lib.pojo_http_data.api.annotations.No_Param;
import wasys.lib.pojo_http_data.api.annotations.No_Type_Cache;
import wasys.lib.pojo_http_data.api_ex.Fillable_Object_Parse_Cache_Accelerator;

/**
 * Default {@link Fillable_Object} type parser.
 * <p>
 * Parses a {@link Fillable_Object} following the APi specific std. It also would cache the type fingerprint internally(or by the type cache accelerator/cache ({@link Fillable_Object_Parse_Cache_Accelerator})
 * </p>
 * @author https://github.com/911992
 */
public class Fillable_Object_Parser {

    private static final Class FILLABLE_TYPES[] = {
        byte.class, short.class, int.class, long.class, float.class, double.class,
        Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
        String.class
    };
    private static final Class INHERITABLE_FILLABLE_TYPES[] = {
        Fillable_Object.class, OutputStream.class
    };

    private static final Fillable_Object_Signature_Context ctx = Fillable_Object_Signature_Context.get_instance();

    private Fillable_Object_Parser() {
    }

    /**
     * Tries to return the already parsed given {@code arg_obj} type, or attempt to parse and return.
     * @param arg_obj the non-null
     * @return a non-null successful type fingerprint, or <b>{@code null}</b> if the given {@code arg_obj} is not fillable.
     * @throws NullPointerException if the give {@code null} is null.
     */
    static Fillable_Object_Parse_Result find_or_parse_object(Fillable_Object arg_obj) {
        Fillable_Object_Parse_Result _res = ctx.find_result(arg_obj);
        if (_res != null) {
            return _res;
        }
        return parse_object(arg_obj);
    }

    static private Fillable_Object_Parse_Result parse_object(Fillable_Object arg_obj) {
        Class _type = arg_obj.getClass();
        synchronized (_type) {
            Fillable_Object_Parse_Result _res = ctx.find_result(arg_obj);
            if (_res != null) {
                return _res;
            }
            Fillable_Object_Field_Signature _field_signatures[];
            Object_Fill_Mode _parse_fill_mode = arg_obj.fill_mode();
            if (_parse_fill_mode == Object_Fill_Mode.Type_Manipulator) {
                Fillable_Object_Manipulator _desc = arg_obj.get_type_descriptor();
                _field_signatures = _desc.get_field_signatures();
            } else {
                Vector<Field> _fields = new Vector<Field>(7, 7);
                get_all_fields(_type, _fields);
                _field_signatures = new Fillable_Object_Field_Signature[_fields.size()];
                for (int a = 0; a < _fields.size(); a++) {
                    Field _fi = _fields.elementAt(a);
                    _fi.setAccessible(true);
                    Fillable_Object_Field_Signature _field_sig = parse_field(_fi);
                    _field_signatures[a] = _field_sig;
                }
            }
            Vector<Fillable_Object_Field_Signature_Cache> _cached_sigs = new Vector<Fillable_Object_Field_Signature_Cache>(_field_signatures.length);
            for (int a = 0; a < _field_signatures.length; a++) {
                Fillable_Object_Field_Signature _sig_ins = _field_signatures[a];
                if (_sig_ins == null) {
                    continue;
                }
                Field _field_ins = find_field(_type, _sig_ins.getEntity_field_name());
                if (_field_ins == null || (is_field_fillable(_field_ins) == false)) {
                    continue;
                } else if (_parse_fill_mode == Object_Fill_Mode.Reflection_Type_Fields && field_should_be_ignorred(_field_ins)) {
                    continue;
                }
                _field_ins.setAccessible(true);
                Method _setter_method = setter_method_from_field(_field_ins, _type);
                if (_setter_method != null) {
                    _setter_method.setAccessible(true);
                }
                Fillable_Object_Field_Signature_Cache _sig_cache = new Fillable_Object_Field_Signature_Cache(_sig_ins, _field_ins, _setter_method);
                _cached_sigs.addElement(_sig_cache);
            }
            _cached_sigs.trimToSize();
            if (_cached_sigs.size() == 0) {
                return null;
            }
            _res = new Fillable_Object_Parse_Result(_type, _cached_sigs);
            boolean _fast = (arg_obj instanceof Fillable_Object_Parse_Cache_Accelerator);
            if (_fast) {
                Fillable_Object_Parse_Cache_Accelerator _acc = (Fillable_Object_Parse_Cache_Accelerator) arg_obj;
                _acc._api_ex_set_type_parse_result(_res);
            }
            No_Type_Cache _no_cache = (No_Type_Cache)_type.getAnnotation(No_Type_Cache.class);
            if(_no_cache==null){
                ctx.add_result_to_ctx(_res);
            }
            return _res;
        }
    }

    private static boolean field_should_be_ignorred(Field arg_field) {
        No_Param _not_a_param = arg_field.getAnnotation(No_Param.class);
        return _not_a_param != null;
    }

    private static boolean is_field_fillable(Field arg_field) {
        int _mods = arg_field.getModifiers();
        if (Modifier.isStatic(_mods)) {
            return false;
        }
        Class _f_type = arg_field.getType();
        Class _tp;
        for (int a = 0; a < FILLABLE_TYPES.length; a++) {
            _tp = FILLABLE_TYPES[a];
            if (_tp == _f_type) {
                if (Modifier.isFinal(_mods)) {
                    break;
                }
                return true;
            }
        }
        for (int a = 0; a < INHERITABLE_FILLABLE_TYPES.length; a++) {
            _tp = INHERITABLE_FILLABLE_TYPES[a];
            if (_f_type == _tp || _tp.isAssignableFrom(_f_type)) {
                return true;
            }
        }
        return false;
    }

    private static final Fillable_Object_Field_Signature parse_field(Field arg_field) {
        boolean _is_fillable = is_field_fillable(arg_field);
        if (_is_fillable == false) {
            return null;
        }
        Class _ftyp = arg_field.getType();
        Field_Definition _fannot = arg_field.getAnnotation(Field_Definition.class);
        if (_fannot == null) {
            return new Fillable_Object_Field_Signature(arg_field.getName(), _ftyp);
        }
        String _param_name = _fannot.param_name();
        if (_param_name == null || _param_name.length() == 0) {
            _param_name = arg_field.getName();
        }
        return new Fillable_Object_Field_Signature(_param_name, _fannot.param_idx(), arg_field.getName(), _ftyp, _fannot.nullable(), _fannot.min_len_val(), _fannot.max_len_val());
    }

    private static Method setter_method_from_field(Field arg_field, Class arg_type) {
        Method _mres;
        Field_Definition _fannot = (Field_Definition) arg_field.getAnnotation(Field_Definition.class);
        if (_fannot != null) {
            String _method_name = _fannot.setter_method_name();
            _mres = find_method(arg_type, _method_name, arg_field.getType());
        } else {
            _mres = null;
        }
        if (_mres == null) {
            _mres = find_marked_setter_method(arg_type, arg_field.getName(), arg_field.getType());
        }
        if (_mres == null) {
            String _field_name = arg_field.getName();
            String _method_name = String.format("set%c%s", Character.toUpperCase(_field_name.charAt(0)), _field_name.substring(1));
            _mres = find_method(arg_type, _method_name, arg_field.getType());
        }
        return _mres;
    }

    private static Method find_method(Class arg_type, String arg_method_name, Class arg_arg_input) {
        do {
            try {
                Method _m = arg_type.getDeclaredMethod(arg_method_name, arg_arg_input);
                int _mods = _m.getModifiers();
                if(!Modifier.isStatic(_mods)){
                    return _m;
                }
            } catch (Exception e) {
            }
        } while ((arg_type = arg_type.getSuperclass()) != Object.class);
        return null;
    }

    private static Method find_marked_setter_method(Class arg_type, String arg_param, Class arg_param_type) {
        Method _methods[];
        Method _m;
        do {
            _methods = arg_type.getDeclaredMethods();
            for (int a = 0; a < _methods.length; a++) {
                _m = _methods[a];
                Field_Setter_Method _set_annot = _m.getAnnotation(Field_Setter_Method.class);
                if (_set_annot != null && _set_annot.name().equals(arg_param) && _m.getParameterCount() == 1 && _m.getParameterTypes()[a].isAssignableFrom(arg_param_type)) {
                    return _m;
                }
            }
        } while ((arg_type = arg_type.getSuperclass()) != Object.class);
        return null;
    }

    private static Field find_field(Class arg_type, String arg_field_name) {
        do {
            try {
                return arg_type.getDeclaredField(arg_field_name);
            } catch (Exception e) {
            }
        } while ((arg_type = arg_type.getSuperclass()) != Object.class);
        return null;
    }

    private static int get_all_fields(Class arg_type, Vector<Field> arg_to_ctx) {
        int _res = 0;
        do {
            Field[] _dfs = arg_type.getDeclaredFields();
            arg_to_ctx.ensureCapacity(_dfs.length + arg_to_ctx.size());
            for (int a = 0; a < _dfs.length; a++) {
                arg_to_ctx.add(a,_dfs[a]);
                _res++;
            }
            arg_type = arg_type.getSuperclass();
        } while (arg_type != Object.class && arg_type != Fillable_Object_Adapter.class);
        return _res;
    }

}
