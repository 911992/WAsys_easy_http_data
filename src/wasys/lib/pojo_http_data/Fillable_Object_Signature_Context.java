/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Signature_Context.java
Created on: May 13, 2020 9:07:35 PM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.util.Vector;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.api_ex.Fillable_Object_Parse_Cache_Accelerator;
import wasys.lib.pojo_http_data.exception.Bad_Type_Cache_Object_Ex;

/**
 *
 * @author https://github.com/911992
 */
public class Fillable_Object_Signature_Context {

    final static private Fillable_Object_Signature_Context INSTANCE = new Fillable_Object_Signature_Context();

    final private Vector<Fillable_Object_Parse_Result> ctx = new Vector<Fillable_Object_Parse_Result>(7, 7);

    private Fillable_Object_Signature_Context() {
    }

    public static Fillable_Object_Signature_Context get_instance() {
        return INSTANCE;
    }

    synchronized void add_result_to_ctx(Fillable_Object_Parse_Result arg_parse_result) {
        ctx.addElement(arg_parse_result);
    }

    Fillable_Object_Parse_Result find_result(Fillable_Object arg_pojo_object) {
        if (arg_pojo_object == null) {
            throw new NullPointerException();
        }
        boolean _fast = (arg_pojo_object instanceof Fillable_Object_Parse_Cache_Accelerator);
        if (_fast) {
            Object _cache_res = ((Fillable_Object_Parse_Cache_Accelerator) arg_pojo_object)._api_ex_get_type_parse_result();
            if (_cache_res != null) {
                if (!(_cache_res instanceof Fillable_Object_Parse_Result)) {
                    throw new Bad_Type_Cache_Object_Ex();
                }
                return (Fillable_Object_Parse_Result) _cache_res;
            }
        }
        for (int a = 0; a < ctx.size(); a++) {
            Fillable_Object_Parse_Result _ins = ctx.elementAt(a);
            if (_ins.getObj_type().equals(arg_pojo_object.getClass())) {
                return _ins;
            }
        }
        return null;
    }

}
