/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Parse_Result.java
Created on: May 13, 2020 7:41:01 PM
    @author https://github.com/911992
 
History:
    0.2 (20200605)
        • Added some documentation
        • Using ArrayList(non thread-safe) instead of Vector(as thread-safe) for fields

    0.1.3(20200521)
        • Updated the header(this comment) part

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.util.ArrayList;

/**
 * An immutable type to Hold fields' signature of a fillable pojo.
 * <p>
 * Since a type may have multiple fields, so this type holds the signatures of each field for a POJO type. (considering a one-to-many relation)
 * </p>
 * <p>
 * Default parser {@link Fillable_Object_Parser} uses this type for keeping the caches/parsed POJOs.
 * </p>
 * @author https://github.com/911992
 */
public class Fillable_Object_Parse_Result {

    /**
     * Refers to type(class) of the related POJO.
     * <p>
     * This is a non-{@code null} field
     * </p>
     */
    final private Class obj_type;

    /**
     * Holds signatures of fields related to the type({@code obj_type} field).
     * <p>
     * Should contains at-least one member, and non-{@code null}, otherwise would make inconsistency and unexpected exceptions
     * </p>
     */
    final private ArrayList<Fillable_Object_Field_Signature_Cache> fields;

    /**
     * Default constructor.
     * @param obj_type refers to the type of POJO
     * @param fields the list contains POJO fields signatures
     */
    public Fillable_Object_Parse_Result(Class obj_type, ArrayList<Fillable_Object_Field_Signature_Cache> fields) {
        this.obj_type = obj_type;
        this.fields = fields;
    }

    /**
     * Getter method for {@code obj_type}
     * @return the {@code obj_type}
     */
    Class getObj_type() {
        return obj_type;
    }

    /**
     * Getter method for {@code fields}
     * @return the {@code fields}
     */
    ArrayList<Fillable_Object_Field_Signature_Cache> getFields() {
        return fields;
    }

}
