/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

/*
WAsys_pojo_http_data
File: Poolable_ArrayList.java
Created on: Jun 4, 2020 8:38:17 AM
    @author https://github.com/911992
 
History:
    initial version: 0.2(20200605)
*/

package wasys.lib.pojo_http_data;

import java.util.ArrayList;
import wasys.lib.generic_object_pool.Object_Pool;
import wasys.lib.generic_object_pool.api.Object_Factory;
import wasys.lib.generic_object_pool.api.Poolable_Object;


/**
 * A poolable {@link ArrayList}{@code &lt;Class&gt;}.
 * <p>
 * This type is a poolable version of {@link ArrayList}{@code &lt;Class&gt;}, which is used by generic/default POJO filler class({@link Generic_Object_Filler}), 
 * to reuse a frequently generated {@link ArrayList} that is supposed to hold filled types to avoid recursive type filling.
 * </p>
 * @author https://github.com/911992
 */
public class Poolable_ArrayList extends ArrayList<Class> implements Poolable_Object{

    @Override
    public void post_create() {
        
    }

    @Override
    public void pre_destroy() {
        reset_state();
    }

    /**
     * Clears the super({@link ArrayList}) indexed items, by calling the {@code clear()} method.
     * <p>
     * Set the state ready for reusing this pooled list, for next call.
     * </p>
     */
    @Override
    public void reset_state() {
        super.clear();
    }

    /**
     * Does not nothing.
     * <p>
     * This method i supposed to hold the associated/parent pool, so could call for a object releasing by this instance when nedded.
     * </p>
     * <p>
     * Since the life-cycle and usage of this type is completely managed by the caller({@link Generic_Object_Filler}), so keeping the associated pool is redundant.
     * </p>
     * @param arg_pool the pool ptr that {@code this} object belongs to it
     */
    @Override
    public void set_pool(Object_Pool arg_pool) {
        
    }  
    
    /**
     * The default factory class for {@link Poolable_ArrayList}.
     * <p>
     * Simply implements {@code Object_Factory} type as needed for the target {@link Object_Pool}.
     * </p>
     */
    public static class Factory implements Object_Factory{

        /**
         * 
         * @return An empty {@link Poolable_ArrayList}, with default capacity/state.
         */
        @Override
        public Poolable_Object create_object() {
            return new Poolable_ArrayList();
        }
        
    }
    
}
