/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Field_Signature_Cache.java
Created on: May 13, 2020 7:36:37 PM
    @author https://github.com/911992
 
History:
    0.1.3(20200521)
        • Updated the header(this comment) part

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import wasys.lib.pojo_http_data.api.Fillable_Object_Field_Signature;

/**
 *
 * @author https://github.com/911992
 */
public class Fillable_Object_Field_Signature_Cache {

    final private Fillable_Object_Field_Signature field_signature;
    final private Field type_field;
    final private Method setter_method;

    public Fillable_Object_Field_Signature_Cache(Fillable_Object_Field_Signature field_signature, Field type_field, Method setter_method) {
        this.field_signature = field_signature;
        this.type_field = type_field;
        this.setter_method = setter_method;
    }

    Fillable_Object_Field_Signature getField_signature() {
        return field_signature;
    }

    Field getType_field() {
        return type_field;
    }

    Method getSetter_method() {
        return setter_method;
    }

}
