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
     * <p>For primitive(numeral) type, it's used for the maximum value allowed</p>
     * <p>For {@code OutputStream} type, it is used for the file/part len</p>
     */
    final private double min_len_val;
    
    /**
     * The maximum len or value allowed for the type.
     * <p>For {@link String} type it is used for the literal len</p>
     * <p>For primitive(numeral) type, it's used for the minimum value allowed</p>
     * <p>For {@code OutputStream} type, it is used for the file/part len</p>
     */
    final private double max_len_val;

    /**
     * Constructor that accepts only essential http param name, and the type.
     * <p>
     * It sets the rest values for the fields as default values.
     * </p>
     * @param name name of the http parameter
     * @param type type of the POJO (http data need to be parsed)
     */
    public Fillable_Object_Field_Signature(String name, Class type) {
        this.param_name = name;
        this.type = type;
        this.entity_field_name = name;
        param_idx = 0;
        nullable = false;
        min_len_val = Double.NEGATIVE_INFINITY;
        max_len_val = Double.POSITIVE_INFINITY;
    }

    /**
     * Constructor to accept all fields required signature data, except the related pojo field name.
     * <p>
     * Needs all data to be set for all fields, except the target pojo field name, where it's the same as http param name.
     * </p>
     * @param name name of the http parameter
     * @param param_idx index of the http parameter
     * @param type type of the pojo field
     * @param nullable tells if missed data is allowed or not
     * @param min_len_val the minimum allowed value/size of the parameter data.
     * @param max_len_val the maximum allowed value/size of the parameter data.
     */
    public Fillable_Object_Field_Signature(String name, int param_idx, Class type, boolean nullable, double min_len_val, double max_len_val) {
        this.param_name = name;
        this.param_idx = Math.max(0, param_idx);
        this.entity_field_name = name;
        this.type = type;
        this.nullable = nullable;
        this.min_len_val = Math.min(min_len_val, max_len_val);
        this.max_len_val = Math.max(min_len_val, max_len_val);
    }

    /**
     * Default constructor, to accept all required signature data, also the pojo field name.
     * <p>
     * Since a http parameter name may not be/treated-as a valid pojo field(e.g. {@code 0email}), so this is possible/essential to specify a pojo field name that is mapped to related param.
     * </p>
     * @param param_name name of the http parameter
     * @param param_idx index of the http parameter
     * @param entity_field_name name of the pojo field that {@code param_name} should be mapped to
     * @param type type of the pojo field
     * @param nullable tells if missed data is allowed or not
     * @param min_len_val the minimum allowed value/size of the parameter data.
     * @param max_len_val the maximum allowed value/size of the parameter data.
     */
    public Fillable_Object_Field_Signature(String param_name, int param_idx, String entity_field_name, Class type, boolean nullable, double min_len_val, double max_len_val) {
        this.param_name = param_name;
        this.param_idx = Math.max(0, param_idx);
        this.entity_field_name = entity_field_name;
        this.type = type;
        this.nullable = nullable;
        this.min_len_val = min_len_val;
        this.max_len_val = max_len_val;
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
     * Getter method for {@code min_len_val}
     * @return the {@code min_len_val}
     */
    public double getMin_len_val() {
        return min_len_val;
    }

    /**
     * Getter method for {@code max_len_val}
     * @return the {@code max_len_val}
     */
    public double getMax_len_val() {
        return max_len_val;
    }

}
