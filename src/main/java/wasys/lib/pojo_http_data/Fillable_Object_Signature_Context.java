/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Signature_Context.java
Created on: May 13, 2020 9:07:35 PM
    @author https://github.com/911992
 
History:
    0.2 (20200605)
        • Added some documentation
        • Using ArrayList(non thread-safe) instead of Vector(thread-safe) for ctx

    0.1.3(20200521)
        • Updated the header(this comment) part

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data;

import java.util.ArrayList;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.api_ex.Fillable_Object_Parse_Cache_Accelerator;
import wasys.lib.pojo_http_data.exception.Bad_Type_Cache_Object_Ex;

/**
 * Class that holds all type signatures globally.
 * <p>
 * The parser and/or filler asks this class to check if any previously parsed type exist, or a parsing processing should be performed again.
 * </p>
 * <p>
 * This type also takes care if a type has implemented(ready for) the fast-global caching.
 * </p>
 * @author https://github.com/911992
 */
public class Fillable_Object_Signature_Context {

    /**
     * The only instance of this type, to make it as singleton.
     */
    final static private Fillable_Object_Signature_Context INSTANCE = new Fillable_Object_Signature_Context();

    /**
     * The context that object type caches are stored.
     */
    final private ArrayList<Fillable_Object_Parse_Result> ctx = new ArrayList<Fillable_Object_Parse_Result>();

    /**
     * Default private constructor to avoid any unexpected local caching, and instancing.
     */
    private Fillable_Object_Signature_Context() {
    }

    /**
     * Returns the globally eager-initialized(by class load/call) instance.
     * @return the {@code INSTANCE}
     */
    public static Fillable_Object_Signature_Context get_instance() {
        return INSTANCE;
    }

    /**
     * Adds the given object/type signature to the context.
     * <p>
     * <b>Note:</b> it does not check if the given {@code arg_parse_result} has already in context or not.
     * </p>
     * @param arg_parse_result the non-{@code null} type signature need to be added to the context
     */
    synchronized void add_result_to_ctx(Fillable_Object_Parse_Result arg_parse_result) {
        ctx.add(arg_parse_result);
    }

    /**
     * Searches for a type signature cache.
     * <p>
     * It first check if the type is a fast-global cache ready, if it's an instance of {@link Fillable_Object_Parse_Cache_Accelerator} or not.
     * </p>
     * <p>
     * <b>Important Note:</b> this method,(neither the default filler) <b>WILL NOT</b> check and validate the returned object cache by fast-global implemented, so a invalid cache will result unexpected behaviors(including exceptions)
     * </p>
     * @param arg_pojo_object the type should be considered for searching
     * @return the type signature if found, or {@code null} otherwise
     */
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
            Fillable_Object_Parse_Result _ins = ctx.get(a);
            if (_ins.getObj_type().equals(arg_pojo_object.getClass())) {
                return _ins;
            }
        }
        return null;
    }

}
