/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Field_Signature.java
Created on: May 13, 2020 4:02:03 AM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 *
 * @author https://github.com/911992
 */
public class Fillable_Object_Field_Signature {

    final private String param_name;
    final private int param_idx;
    final private String entity_field_name;
    final private Class type;
    final private boolean nullable;
    final private double min_len_val;
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
