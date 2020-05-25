/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Fillable_Object_Parse_Cache_Accelerator.java
Created on: May 13, 2020 9:32:15 PM
    @author https://github.com/911992
 
History:
    0.1.6(20200525)
        • Updated the documentation, removed the link/ref to Fillable_Object_Adapter

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api_ex;

/**
 * Global type-level cache listener.
 * This type informs the parser that given {@link Fillable_Object} is able to hold its type cache somewhere and return the cahce when needed to gain some performance by ignorring checking the context of cached types(in default parse cache ctx)
 * You may assume this is a static/type level interface(which is technically not possible with java), so make sure how to do it right.
 * <b>Important:</b> This type was added in order to skip context search, by a direct/explicit type-level caching, so if you don't know how to implement it, just ignore it.
 * <b>Note:</b> the cache instance <u>must not</u> keep the given cache as object/instance level var, which will make the reason of type-level cache redundant.
 * @author https://github.com/911992
 */
public interface Fillable_Object_Parse_Cache_Accelerator {

    /**
     * Called by the parser, to ask the type(by one of its instances) to hold a cache of type signature.
     * @param arg_arg something that holds the type signature
     */
    public void _api_ex_set_type_parse_result(Object arg_arg);

    /**
     * Returns the previously given type cache(by {@code _api_ex_set_type_parse_result}), or null, if there is no any such thing yet.
     * Thinking, that calling this method from any instance should result a same cache result, or inconsistency and UB will be happened.
     * @return the same something that holds the type signature
     */
    public Object _api_ex_get_type_parse_result();
}
