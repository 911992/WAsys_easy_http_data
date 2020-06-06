/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Manipulator.java
Created on: May 13, 2020 4:27:07 AM
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

/**
 * The POJO type descriptor, where user wishes to provide a customized fingerprint of a POJO.
 * <p>
 * This interface should be implemented and returned by a POJO which is needed to be parsed manually(not by type scraping/reflection)
 * </p>
 * <p>
 * Please refer to method {@code fill_mode} and {@code get_type_descriptor}  methods in {@link Fillable_Object} class for more info
 * </p>
 * @author https://github.com/911992
 */
public interface Fillable_Object_Manipulator {

    /**
     * An array of {@link Fillable_Object_Field_Signature} that each instance explains about a field signature related to POJO.
     * <p>Any invalid (missed field) of field will be ignored.</p>
     * @return list of signatures of fillable fields of the type
     */
    public Fillable_Object_Field_Signature[] get_field_signatures();

    /**
     * Returns the filling mode(not the parsing mode, they are different) of the POJO.
     * <p>
     * Please mind a POJO could be introduces manually by providing list of field signatures, as explained in {@code get_field_signatures} method, but the filling mode could be done in reflection mode, where filler automatically calls the associated setter method, or set the value explicitly to the field.
     * </p>
     * <p>
     * If {@code Type_Manipulator} is returned, so the filler will call {@code set_field_value} for each field setting value
     * </p>
     * @return the POJO filling mode (by default using Reflection)
     */
    default public Object_Fill_Mode field_set_mode() {
        return Object_Fill_Mode.Reflection_Type_Fields;
    }

    /**
     * This method is called when parser could find the related data, and successfully decode it to the target field type, when POJO asks for manual filling mode.
     * <p>
     * Filler calls this method if POJO returned {@code Type_Manipulator} in {@code field_set_mode} method, <u>only</u> for String, and supported primitive types.
     * </p>
     * <p>
     * If parser could decode any value to target type, it first set the value, and later check if the value is valid in range, so any error related to a filed is called after it gets set(if possible)
     * </p>
     * <p>
     * User is in charge to set the related fields, based on arguments given.
     * </p>
     * @param param_name the http parameter name
     * @param param_idx the index of the parameter
     * @param arg_value <u>non-null</u> decoded/casted value for the target field
     */
    default public void set_field_value(String param_name, int param_idx, Object arg_value) {
    }
}
