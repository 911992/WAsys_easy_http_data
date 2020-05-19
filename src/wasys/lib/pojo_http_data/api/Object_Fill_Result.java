/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Object_Fill_Result.java
Created on: May 17, 2020 3:52:31 AM | last edit: May 17, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 *
 * @author https://github.com/911992
 */
public enum Object_Fill_Result {
    Ok,
    Ok_Missed_Values,
    Ignorred_Duplicated_Type,
    Failed;

    public Object_Fill_Result set_if_overridable(Object_Fill_Result arg_new_val) {
        if (arg_new_val.ordinal() > this.ordinal()) {
            return arg_new_val;
        }
        return this;
    }
}
