/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Object_Fill_Result.java
Created on: May 17, 2020 3:52:31 AM
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
 * Indicates the object fill result (overall of all field sets, the highest priority)
 * @author https://github.com/911992
 */
public enum Object_Fill_Result {
    /**
     * When all required data were given with correct format and check, all set(or passed)successfully.
     */
    Ok,
    /**
     * When some required data were given with correct format and check, but some were absent.
     */
    Ok_Missed_Values,
    /**
     * when the type was associated with another parent {@link Fillable_Object}, and another field with exact type were filled/proceed already, so this instance is ignored.
     */
    Ignorred_Duplicated_Type,
    /**
     * When there was some error while filling the POJO.
     */
    Failed;

    /**
     * Internal APi using.
     * <p>Compares the given {@code arg_new_val} state/enum, and return the one has more priority state.</p>
     * @param arg_new_val the new state need to be compared to the current(this) state
     * @return the priority enum
     */
    public Object_Fill_Result set_if_overridable(Object_Fill_Result arg_new_val) {
        if (arg_new_val.ordinal() > this.ordinal()) {
            return arg_new_val;
        }
        return this;
    }
}
