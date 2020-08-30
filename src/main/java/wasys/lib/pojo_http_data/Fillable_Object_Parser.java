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
    0.3.3 (20200829)
        • Small documentation fix

    0.2.5 (20200813)
        • Calling related getter of Field_Definition based on annotated field's type
        • Fixes/changes becasue of Field_Definition and Fillable_Object_Field_Signature types changes

    0.2.1 (20200724)
        • Updated parse_field method to support new type policy about ignorring fields started with double-underscore "__" (and are not Field_Definition).

    0.2 (20200605)
        • Added some documentation
        • Using ArrayList(non thread-safe) instead of Vector(thread-safe), as the Fillable_Object_Parse_Result constructor now works the same way
        • Changed method find_marked_setter_method(:Class,:String,:Class):Method signature to find_marked_setter_method(:Class,:Field):Method

    0.1.6 (20200525)
        • Finding fields in reflection mode as parent-to-child fields order now

    0.1.3 (20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
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
 * <p>
 * This class is part of default-impl of this lib, and could be replaced with another module that performs the POJO({@link Fillable_Object}) parsing.
 * </p>
 * @author https://github.com/911992
 */
public class Fillable_Object_Parser {

    /**
     * List of types, would be filled using an associated setter method, or directly by related field reflected pointer.
     * <p>
     * List contains String, all primitives, and their wrappers (except for char, and boolean types)
     * </p>
     * <p>
     * Any filler/http-req implementation may support for more type, but the types are given here are essentially require.
     * </p>
     * <p>
     * Any field that could be listed as this list should be filled explicitly using teh field reflected ptr, or associated setter method.
     * </p>
     */
    private static final Class FILLABLE_TYPES[] = {
        byte.class, short.class, int.class, long.class, float.class, double.class,
        Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class,
        String.class
    };
    
    /**
     * List of fields that should not be filled explicitly(unlike defined in {@code FILLABLE_TYPES}).
     * <p>
     * Types listed may or may not have some dedicated processes during POJO filling.
     * </p>
     * <p>
     * Having a setter(neitehr getter) method for fields come with types included are not essential, however please refer to the filler, that may perform some out-of spec behavior.
     * </p>
     * <p>
     * <b>Note:</b> Types defined in this array, may be abstract types(such as {@link OutputStream}), this means the target field should an inherited child.
     * </p>
     * <p>
     * For more information, please refer to repo(home page) readme file.
     * </p>
     */
    private static final Class INHERITABLE_FILLABLE_TYPES[] = {
        Fillable_Object.class, OutputStream.class
    };

    /**
     * Pointer to {@link Fillable_Object_Signature_Context} instance(probably the global/singleton).
     * <p>
     * Depending to caching scope, this instance could be at jvm/application, or session, etc... level.
     * </p>
     * <p>
     * By default the reference is supposed to be global across the application(considering singleton).
     * </p>
     */
    private static final Fillable_Object_Signature_Context ctx = Fillable_Object_Signature_Context.get_instance();

    /**
     * Default constructor as private, since there is no need for any object of this type, as all methods are static.
     */
    private Fillable_Object_Parser() {
    }

    /**
     * Tries to return the already parsed given {@code arg_obj} type, or attempt to parse and return.
     * <p>
     * It asks the local {@code ctx:}{@link Fillable_Object_Signature_Context} to find a signature of given {@link Fillable_Object} object, if any.
     * </p>
     * <p>
     * This is {@link Fillable_Object_Signature_Context} policy to either return already exist obj type cache, or null to force the parser perform another object parsing.
     * </p>
     * <p>
     * <b>Note:</b> The fast-global cache lookup is done at {@link Fillable_Object_Signature_Context} side.
     * </p>
     * <p>
     * <b>Note:</b> this method is not thread-safe, so calling for cache look up may result an unexpected data. But mind method(s) are related for context data manipulation(add/remove) will be essentially thread-safe.
     * </p>
     * <p>
     * <b>Important Note:</b> if the given {@code arg_obj} is annotated with a {@link No_Type_Cache}, then once it's parsed, it won't be placed at central type signature cache as {@link Fillable_Object_Signature_Context}
     * </p>
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

    /**
     * Tries to find or parse the given {@code arg_obj:}{@link Fillable_Object}.
     * <p>
     * <b>Note:</b> This method is called by {@code find_or_parse_object()} method, that performs a lookup over context, and then ask this method for parsing.
     * <br>
     * However, this method asks the {@code ctx:}{@link Fillable_Object_Signature_Context} <b>AGAIN</b>, but in a thread-safe manner mode, in order to avoid any thread related issues.
     * </p>
     * <p>There is no exception, but if the given {@code arg_obj} has no any field for filling, then a {@code null} will be returned, and the cache won't be saved.</p>
     * @param arg_obj a non-{@code null} instance of a concreted pojo
     * @return a type signature cache(with at-least one field cache) if given {@code arg_obj} has a right type definition for filling, or {@code null} otherwise.
     */
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
                ArrayList<Field> _fields = new ArrayList<Field>();
                get_all_fields(_type, _fields);
                _field_signatures = new Fillable_Object_Field_Signature[_fields.size()];
                for (int a = 0; a < _fields.size(); a++) {
                    Field _fi = _fields.get(a);
                    _fi.setAccessible(true);
                    Fillable_Object_Field_Signature _field_sig = parse_field(_fi);
                    _field_signatures[a] = _field_sig;
                }
            }
            ArrayList<Fillable_Object_Field_Signature_Cache> _cached_sigs = new ArrayList<Fillable_Object_Field_Signature_Cache>(_field_signatures.length);
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
                _cached_sigs.add(_sig_cache);
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

    /**
     * Checks if a fillable field should ignored.
     * <p>It check if the given {@code arg_field} carry a {@link No_Param} annotation, that tells the filler/parser to explicitly ignore the field, regardless if it's fillable or not.</p>
     * @param arg_field the field should be checked
     * @return {@code true} if the field is annotated with {@link No_Param}, {@code false} otherwise
     */
    private static boolean field_should_be_ignorred(Field arg_field) {
        No_Param _not_a_param = arg_field.getAnnotation(No_Param.class);
        return _not_a_param != null;
    }

    /**
     * Checks if the field is a fillable or not.
     * <p>It simply checks if the field's type either is listed in {@code FILLABLE_TYPES} array, or assignable to types of {@code INHERITABLE_FILLABLE_TYPES} array.</p>
     * @param arg_field the field should be checked if it's a fillable one
     * @return {@code true} if the field could be filled, {@code false} otherwise.
     */
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

    /**
     * Parse the given field, and returns the signature.
     * <p>
     * Check if the given field is a fillable type (by calling {@code is_field_fillable} method), or not.
     * </p>
     * <p>
     * By default, if field has a associated {@link Field_Definition}, it consider the value of {@code param_name}(form annotation) as param name.
     * </p>
     * <p>
     * If field has no annotation, or the param name is empty(default), then it consider the name of the field as the name of the param should be looked up of target http request.
     * </p>
     * <p>
     * By existing the {@link Field_Definition}, it also grabs the other meta info of the field, such as being nullable, min, max, etc...
     * </p>
     * @param arg_field the field need to be parsed
     * @return the field signature if the field is fillable, or {@code null} otherwise
     */
    private static final Fillable_Object_Field_Signature parse_field(Field arg_field) {
        boolean _is_fillable = is_field_fillable(arg_field);
        if (_is_fillable == false) {
            return null;
        }
        Class _ftyp = arg_field.getType();
        Field_Definition _fannot = arg_field.getAnnotation(Field_Definition.class);
        if (_fannot == null) {
            if(_ftyp.getName().startsWith("__")){
                return null;
            }
            return new Fillable_Object_Field_Signature(arg_field.getName(), _ftyp);
        }
        String _param_name = _fannot.param_name();
        if (_param_name == null || _param_name.length() == 0) {
            _param_name = arg_field.getName();
        }
        Number _min_val;
        Number _max_val;
        if(_ftyp == float.class || _ftyp == Float.class || _ftyp == double.class || _ftyp == Double.class){
            _min_val = _fannot.min_float_point_val();
            _max_val = _fannot.max_float_point_val();
        }else{
            _min_val = _fannot.min_val_or_len();
            _max_val = _fannot.max_val_or_len();
        }
        return new Fillable_Object_Field_Signature(_param_name, _fannot.param_idx(), arg_field.getName(), _ftyp, _fannot.nullable(), _min_val,_max_val);
    }

    /**
     * Tries to find the setter method for a field.
     * <p>
     * A setter method could be defined for the field either implicitly, or explicitly.
     * </p>
     * <p>
     * The explicit way is doe using explicitly defined method name using {@link Field_Definition} by related field, or using {@link Field_Setter_Method} by a method(first index found).
     * </p>
     * <p>
     * Please considering following steps are taken to find a setter method for the field, if the first one fails, it goes for second and so on.
     * </p>
     * <ol>
     * <li>Method name that defined by {@link Field_Definition}, and exist, and comes with correct signature.</li>
     * <li>Method name that that annotated by {@link Field_Setter_Method}, and name equals to the field name (<b>NOTE:</b> field name, not param name), and comes with a correct signature.</li>
     * <li>Method name that comes with java std common name as {@code setAaa}, where {@code aaa} is the name of the field, and comes with correct signature.</li>
     * </ol>
     * <p>
     * If there is no any setter method, the field still be able to get filled, by direct/explicit field accessing using its reflected pointer.
     * </p>
     * @param arg_field the field need to be used for looking up its setter method
     * @param arg_type type of the POJO {@link Fillable_Object} that hosts the {@code arg_field}
     * @return a valid reflected method ptr, that marked/found as setter method for give {@code arg_field}, or {@code null} otherwise
     */
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
            _mres = find_marked_setter_method(arg_type, arg_field);
        }
        if (_mres == null) {
            String _field_name = arg_field.getName();
            String _method_name = String.format("set%c%s", Character.toUpperCase(_field_name.charAt(0)), _field_name.substring(1));
            _mres = find_method(arg_type, _method_name, arg_field.getType());
        }
        return _mres;
    }

    /**
     * Tries to find a method with the given signature.
     * <p>
     * This method tries to find a method that accepts(as arg) the given {@code arg_arg_input}, and comes with given {@code arg_method_name} from type {@code arg_type}
     * </p>
     * <p>
     * If there is no such method, it will return null.
     * </p>
     * @param arg_type the type/class(which is supposed to be a {@link Fillable_Object}) of POJO need to be checked
     * @param arg_method_name name of the method should be checked
     * @param arg_arg_input the only argument input type method must have
     * @return a valid method reflected pointer that comes with signature given, or {@code null} otherwise
     */
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

    /**
     * Search through all defined methods of given {@code arg_type} type, to find an explicitly setter method.
     * <p>
     * This method is called when a field has not set any explicitly setter method with its {@link Field_Definition} annotation.
     * </p>
     * <p>
     * Any method that comes without a {@link Field_Setter_Method} annotation will be skipped.
     * </p>
     * <p>
     * Then parser tries to find any explicitly marked method(regardless of its name) that would point out to the given {@code arg_field_name}
     * </p>
     * <p>
     * The target marked method must come with correct signature. It must accepts only one input arg, with the expected given {@code arg_field} type.
     * </p>
     * @param arg_type the type needs to be looking up the method(probably a {@link Fillable_Object})
     * @param arg_field the field should be considered, to find its marked setter method
     * @return a valid annotated method that works as a setter for given {@code arg_field}, or {@code null} otherwise
     */
    private static Method find_marked_setter_method(Class arg_type, Field arg_field) {
        Method _methods[];
        Method _m;
        String _field_name = arg_field.getName();
        Class _field_type = arg_field.getType();
        do {
            _methods = arg_type.getDeclaredMethods();
            for (int a = 0; a < _methods.length; a++) {
                _m = _methods[a];
                Field_Setter_Method _set_annot = _m.getAnnotation(Field_Setter_Method.class);
                if (_set_annot != null && _set_annot.name().equals(_field_name) && _m.getParameterCount() == 1 && _m.getParameterTypes()[a].isAssignableFrom(_field_type)) {
                    return _m;
                }
            }
        } while ((arg_type = arg_type.getSuperclass()) != Object.class);
        return null;
    }

    /**
     * Tries to find a field from the given type.
     * @param arg_type the type need to be looked up
     * @param arg_field_name the field name should be searched
     * @return a field reflected ptr that can be used(exist) for give {@code arg_type}, or {@code null} otherwise.
     */
    private static Field find_field(Class arg_type, String arg_field_name) {
        do {
            try {
                return arg_type.getDeclaredField(arg_field_name);
            } catch (Exception e) {
            }
        } while ((arg_type = arg_type.getSuperclass()) != Object.class);
        return null;
    }

    /**
     * Return all fields from top-level parent to this(child) order.
     * <p>
     * It grabs and return a list of all inherited, and self-declared fields of given {@code arg_type}
     * </p>
     * <p>
     * <b>Important note:</b> The order is from top-parent to child as field were declared first come with lower index.
     * </p>
     * <p>
     * <b>Important Note:</b> Field scraping stop at a parent it's the top level {@link Object}, or {@link Fillable_Object_Adapter} type.
     * </p>
     * @param arg_type the type need to be scraped
     * @param arg_to_ctx the context field should be put
     * @return number of fields have scraped and added to the given {@code arg_to_ctx}
     */
    private static int get_all_fields(Class arg_type, ArrayList<Field> arg_to_ctx) {
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
