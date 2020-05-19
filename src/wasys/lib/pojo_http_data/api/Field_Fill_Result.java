/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Field_Fill_Result.java
Created on: May 12, 2020 11:34:03 PM | last edit: May 12, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 *
 * @author https://github.com/911992
 */
public enum Field_Fill_Result {
    Ok,
    Ok_Missed_Data,
    Failed_IO_Error,
    Failed_Parse_Error,
    Failed_Outof_Range,
    Failed_Missed_Data;

    public Field_Fill_Result set_if_overridable(Field_Fill_Result arg_new_val) {
        if (arg_new_val.ordinal() > this.ordinal()) {
            return arg_new_val;
        }
        return this;
    }
}
