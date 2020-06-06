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
    0.2 (20200605)
        • Added some documentation

    0.1.3(20200521)
        • Updated the header(this comment) part

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import wasys.lib.pojo_http_data.api.Fillable_Object_Field_Signature;

/**
 * A immutable type to hold associated fields and setter method of a field's signature.
 * <p>
 * Holds the cached setter {@link Method}(if applicable), and related {@link Field}(supposed to not be null) for fast POJO object state changing.
 * </p>
 * <p>
 * Any concreted API artifact may use this, or a dedicated type for meta/member-signature data.
 * </p>
 * <p>
 * Default parser {@link Fillable_Object_Parser} uses this type for keeping the caches/parsed POJOs by {@link Fillable_Object_Parse_Result} class.
 * </p>
 * @author https://github.com/911992
 */
public class Fillable_Object_Field_Signature_Cache {

    /**
     * Holds the non-{@code null} reference to a parsable field info/signature.
     */
    final private Fillable_Object_Field_Signature field_signature;
    /**
     * Holds the associated field of target pojo that were marked(by user or at runtime) as a fillable field.
     */
    final private Field type_field;
    /**
     * Holds a {@code null}able setter method for related field.
     */
    final private Method setter_method;

    /**
     * Default constructor.
     * @param field_signature the parsable/plain and valid field signature
     * @param type_field the reflected {@link Field} which is associated to the field
     * @param setter_method the reflected {@link Method} that is supposed for field({@code type_field} param)
     */
    public Fillable_Object_Field_Signature_Cache(Fillable_Object_Field_Signature field_signature, Field type_field, Method setter_method) {
        this.field_signature = field_signature;
        this.type_field = type_field;
        this.setter_method = setter_method;
    }

    /**
     * Getter for {@code field_signature}
     * @return the {@code field_signature} field
     */
    Fillable_Object_Field_Signature getField_signature() {
        return field_signature;
    }

    /**
     * Getter for {@code type_field}
     * @return {@code type_field} field
     */
    Field getType_field() {
        return type_field;
    }

    /**
     * Getter for {@code setter_method}
     * @return {@code setter_method} field
     */
    Method getSetter_method() {
        return setter_method;
    }

}
