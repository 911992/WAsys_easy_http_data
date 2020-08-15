/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Field_Signature.java
Created on: May 13, 2020 4:02:03 AM
    @author https://github.com/911992
 
History:
    0.2.5 (20200813)
        • Removed min_len_val and max_len_val fields (and their getter and setters)
        • Added min_val:Number, and max_val:Number (with setter and getters funcs)
        • Updated constructors, to follow the new field changes(above)
        • Updated the docs!

    0.2 (20200605)
        • Updated/fixed documentation

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

import java.io.OutputStream;

/**
 * Specifies a field policy.
 * <p>
 * An immutable class, need to initialized and declared by the user(custom type definition) or filler(as cache), that explains one(1) field of the POJO
 * </p>
 * <p>
 * The http parameter name, its index, range/length check, and {@code null}ablity are the available data a field signature may carry. 
 * </p>
 * @author https://github.com/911992
 */
public class Fillable_Object_Field_Signature {
    
    /**
     * The http parameter name should be read.
     */
    final private String param_name;
    
    /**
     * The http parameter index, default as 0.
     */
    final private int param_idx;
    
    /**
     * The field name of the POJO, by default as http parameter name(if applicable).
     */
    final private String entity_field_name;
    
    /**
     * the type of the field should be considered/decoded.
     * <p>Supported types are listed as below<p/>
     * <ul>
     * <li>Primitive types and their wrappers, except for boolean, and char.</li>
     * <li>Any {@link OutputStream} assignable(inherited) type(for part/file upload)</li>
     * <li>Any {@link Fillable_Object} POJO (please considering duplicated type per fill op will be ignored)</li>
     * </ul>
     */
    final private Class type;
    
    /**
     * Indicates if the type support for being-null(missed) data or not.
     */
    final private boolean nullable;
    
    /**
     * The minimum len or value allowed for the type.
     * <p>For {@link String} type it is used for the literal len</p>
     * <p>For primitive(numeral) type, it's used for the maximum value allowed.</p>
     * <p><b>Note: For each related primite type, related {@code getType} will be called,(e.g. calling {@code getLong} when long is requried.)</b></p>
     * <p>For {@link OutputStream} type, it is used for the file/part len</p>
     * <p>
     * <b>Note:</b> Since 0.2.5, the value is not used for float-point(real) types.
     * </p>
     * @since 0.2.5
     */
    final private Number min_val;
    
    /**
     * The maximum len or value allowed for the type.
     * <p>For {@link String} type it is used for the literal len</p>
     * <p>For primitive(numeral) type, it's used for the minimum value allowed</p>
     * <p><b>Note: For each related primite type, related {@code getType} will be called,(e.g. calling {@code getLong} when long is requried.)</b></p>
     * <p>For {@link OutputStream} type, it is used for the file/part len</p>
     * <p>
     * <b>Note:</b> Since 0.2.5, the value is not used for float-point(real) types.
     * </p>
     * @since 0.2.5
     */
    final private Number max_val;

    /**
     * Constructor that accepts only essential http param name, and the type.
     * <p>
     * It sets the rest values for the fields as default values.
     * </p>
     * <p>
     * It has no value/size check.
     * </p>
     * @param arg_name name of the http parameter
     * @param arg_type type of the POJO (http data need to be parsed)
     */
    public Fillable_Object_Field_Signature(String arg_name, Class arg_type) {
        this(arg_name, 0, arg_name, arg_type, false, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    /**
     * Constructor to accept all fields required signature data, except the related pojo field name.
     * <p>
     * Needs all data to be set for all fields, except the target pojo field name, where it's the same as http param name.
     * </p>
     * @param arg_name name of the http parameter
     * @param arg_param_idx index of the http parameter
     * @param arg_type type of the pojo field
     * @param arg_nullable tells if missed data is allowed or not
     * @param arg_min_val the minimum allowed value/size of the parameter data.
     * @param arg_max_val the maximum allowed value/size of the parameter data.
     * @since 0.2.5
     */
    public Fillable_Object_Field_Signature(String arg_name, int arg_param_idx, Class arg_type, boolean arg_nullable, Number arg_min_val, Number arg_max_val) {
        this(arg_name, arg_param_idx, arg_name, arg_type, arg_nullable, arg_min_val, arg_max_val);
    }

    /**
     * Default constructor, to accept all required signature data, also the pojo field name.
     * <p>
     * Since a http parameter name may not be/treated-as a valid pojo field(e.g. {@code 0email}), so this is possible/essential to specify a pojo field name that is mapped to related param.
     * </p>
     * <p>
     * <b>Note:</b> {@code arg_min_val}, and {@code arg_max_val} are not cloned/copied(ptr reference). Changing associated value might cause undefined behaviors during object filling.<br>
     * This is suggested to provide a immutable copy(like {@link Long}, {@link Double},...).
     * </p>
     * @param arg_param_name name of the http parameter
     * @param arg_param_idx index of the http parameter
     * @param arg_entity_field_name name of the pojo field that {@code param_name} should be mapped to
     * @param arg_type type of the pojo field
     * @param arg_nullable tells if missed data is allowed or not
     * @param arg_min_val the minimum allowed value/size(len) of of the parameter data (if {@code null}, then {@code Double.NEGATIVE_INFINITY} will be used).
     * @param arg_max_val the maximum allowed value/size(len) of of the parameter data (if {@code null}, then {@code Double.POSITIVE_INFINITY} will be used).
     * @since 0.2.5
     */
    public Fillable_Object_Field_Signature(String arg_param_name, int arg_param_idx, String arg_entity_field_name, Class arg_type, boolean arg_nullable, Number arg_min_val,Number arg_max_val) {
        this.param_name = arg_param_name;
        this.param_idx = Math.max(0,arg_param_idx);
        this.entity_field_name = arg_entity_field_name;
        this.type = arg_type;
        this.nullable = arg_nullable;
        if(arg_min_val == null){
            arg_min_val = Double.NEGATIVE_INFINITY;
        }
        if(arg_max_val == null){
            arg_max_val = Double.POSITIVE_INFINITY;
        }
        this.min_val = arg_min_val;
        this.max_val = arg_max_val;
    }

    /**
     * Getter method for {@code param_idx}
     * @return the {@code param_idx} field
     */
    public int getParam_idx() {
        return param_idx;
    }

    /**
     * Getter name for the {@code param_name}
     * @return the {@code param_name}
     */
    public String getParam_name() {
        return param_name;
    }

    /**
     * Getter method for {@code entity_field_name}
     * @return the {@code entity_field_name}
     */
    public String getEntity_field_name() {
        return entity_field_name;
    }

    /**
     * Getter method for {@code type}
     * @return the {@code type}
     */
    public Class getType() {
        return type;
    }

    /**
     * Getter method for {@code nullable}
     * @return the {@code nullable}
     */
    public boolean isNullable() {
        return nullable;
    }

    /**
     * Getter method for {@code min_val}
     * @return the {@code min_val}
     */
    public Number getMin_val() {
        return min_val;
    }

    /**
     * Getter method for {@code max_val}
     * @return the {@code max_val}
     */
    public Number getMax_val() {
        return max_val;
    }

}
