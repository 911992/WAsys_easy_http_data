/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Field_Fill_Result.java
Created on: May 12, 2020 11:34:03 PM
    @author https://github.com/911992
 
History:
    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 * Specifies filling result related to a field of a @{link Fillable_object}
 * @author https://github.com/911992
 */
public enum Field_Fill_Result {
    /**
     * Means the field has been in non-null and valid state
     */
    Ok,
    /**
     * Means the field has not been set, since the required parameter were missed by related http request
     */
    Ok_Missed_Data,
    /**
     * Means there was some IO related exception during stream copying(streaming)
     * Example: target output stream is closed, or closed connection from client that may causes io exception from input stream
     */
    Failed_IO_Error,
    /**
     * Means the required http parameter data could not get parsed(decoded) to field type (example: bad number format)
     */
    Failed_Parse_Error,
    /**
     * Means the validation of related field failed (example: expected 9 len string, but got 11)
     */
    Failed_Outof_Range,
    /**
     * Failed when the related http request has missed data to fulfill a non-null field
     */
    Failed_Missed_Data;

    /**
     * Internal APi using
     * Compares the given {@code arg_new_val} state/enum, and return the one has more priority state.
     * @param arg_new_val the new state need to be compared to the current(this) state
     * @return the priority enum
     */
    public Field_Fill_Result set_if_overridable(Field_Fill_Result arg_new_val) {
        if (arg_new_val.ordinal() > this.ordinal()) {
            return arg_new_val;
        }
        return this;
    }
}
