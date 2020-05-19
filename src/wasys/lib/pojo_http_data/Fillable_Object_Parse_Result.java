/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Parse_Result.java
Created on: May 13, 2020 7:41:01 PM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.util.Vector;

/**
 *
 * @author https://github.com/911992
 */
public class Fillable_Object_Parse_Result {

    final private Class obj_type;

    final private Vector<Fillable_Object_Field_Signature_Cache> fields;

    public Fillable_Object_Parse_Result(Class obj_type, Vector<Fillable_Object_Field_Signature_Cache> fields) {
        this.obj_type = obj_type;
        this.fields = fields;
    }

    Class getObj_type() {
        return obj_type;
    }

    Vector<Fillable_Object_Field_Signature_Cache> getFields() {
        return fields;
    }

}
