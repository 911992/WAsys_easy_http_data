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
    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 * Specifies a field policy.
 * An immutable class, need to initialized and declared by the user(custom type definition) or filler(as cache), that explains one(1) field of the POJO
 * @author https://github.com/911992
 */
public class Fillable_Object_Field_Signature {
    /**
     * the http parameter name should be read.
     */
    final private String param_name;
    /**
     * the http parameter index, default as 0.
     */
    final private int param_idx;
    /**
     * the field name of the POJO, by default as http parameter name(if applicable).
     */
    final private String entity_field_name;
    /**
     * the type of the field should be considered/decoded.
     * Supported types are listed as below
     * •primitive types and their wrappers, except for boolean, and char.
     * •@{code OutputStream} or inherited type(for part/file upload)
     * •Any {@code Fillable_Object} POJO (please considering duplicated type per fill op will be ignored)
     */
    final private Class type;
    /**
     * Indicates if the type support for being-null(missed) data or not.
     */
    final private boolean nullable;
    /**
     * The minimum len or value allowed for the type.
     * For {@code String} type it is used for the literal len
     * For {@code OutputStream} type, it is used for the file len
     */
    final private double min_len_val;
    /**
     * The maximum len or value allowed for the type.
     * For {@code String} type it is used for the literal len
     * For {@code OutputStream} type, it is used for the file len
     */
    final private double max_len_val;

    public Fillable_Object_Field_Signature(String name, Class type) {
        this.param_name = name;
        this.type = type;
        this.entity_field_name = name;
        param_idx = 0;
        nullable = false;
        min_len_val = Double.NEGATIVE_INFINITY;
        max_len_val = Double.POSITIVE_INFINITY;
    }

    public Fillable_Object_Field_Signature(String name, int param_idx, Class type, boolean nullable, double min_len_val, double max_len_val) {
        this.param_name = name;
        this.param_idx = Math.max(0, param_idx);
        this.entity_field_name = name;
        this.type = type;
        this.nullable = nullable;
        this.min_len_val = Math.min(min_len_val, max_len_val);
        this.max_len_val = Math.max(min_len_val, max_len_val);
    }

    public Fillable_Object_Field_Signature(String param_name, int param_idx, String entity_field_name, Class type, boolean nullable, double min_len_val, double max_len_val) {
        this.param_name = param_name;
        this.param_idx = Math.max(0, param_idx);
        this.entity_field_name = entity_field_name;
        this.type = type;
        this.nullable = nullable;
        this.min_len_val = min_len_val;
        this.max_len_val = max_len_val;
    }

    public int getParam_idx() {
        return param_idx;
    }

    public String getParam_name() {
        return param_name;
    }

    public String getEntity_field_name() {
        return entity_field_name;
    }

    public Class getType() {
        return type;
    }

    public boolean isNullable() {
        return nullable;
    }

    public double getMin_len_val() {
        return min_len_val;
    }

    public double getMax_len_val() {
        return max_len_val;
    }

}
