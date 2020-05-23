/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Poolable_Fillable_Object_Adapter.java
Created on: May 19, 2020 3:29:19 AM
    @author https://github.com/911992
 
History:
    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc
        • Added back_to_pool() method
        • AutoClosable implementation, close() now just asks for backing to pool by calling back_to_pool() method
        • Removed child_reset_state method, moved to super type
        • Removed call of child_reset_state method in reset_state method

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

import wasys.lib.generic_object_pool.Object_Pool;
import wasys.lib.generic_object_pool.api.Poolable_Object;
import wasys.lib.pojo_http_data.api.annotations.No_Param;

/**
 *
 * @author https://github.com/911992
 */
public abstract class Poolable_Fillable_Object_Adapter extends Fillable_Object_Adapter implements Poolable_Object , AutoCloseable {

    @No_Param
    private Object_Pool pool;
    
    /**
     * Release this {@code POolable_Object} by asking the associated pool instance(if not null).
     * This method <b>WILL NOT</b> cause a fill state restarting explicitly, but the pool may decide to reset the object(which will reset the fill state) before the object gets back to the pool.
     */
    public void back_to_pool(){
        if (pool == null) {
            return;
        }
        pool.release_an_instance(this);
    }
    
    @Override
    public void set_pool(Object_Pool arg_pool) {
        this.pool = arg_pool;
    }

    /**
     * Resetting the {@code Poolable_Object} by calling the super reset_fill_state().
     * This method asks super method for a fill state resetting.
     */
    @Override
    final public void reset_state() {
        super.reset_fill_state();
    }

    /**
     * Asks for backing to pool, no fill state resetting call explicitly.
     * @throws Exception 
     */
    @Override
    public void close() throws Exception {
        back_to_pool();
    }
}
