/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Poolable_Fillable_Object_Adapter.java
Created on: May 19, 2020 3:29:19 AM | last edit: May 19, 2020
    @author https://github.com/911992
 
History:
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
public abstract class Poolable_Fillable_Object_Adapter extends Fillable_Object_Adapter implements Poolable_Object {

    @No_Param
    private Object_Pool pool;
    
    protected void child_reset_state() {

    }
    
    @Override
    public void set_pool(Object_Pool arg_pool) {
        this.pool = arg_pool;
    }

    @Override
    final public void reset_state() {
        super.reset_fill_state();
        child_reset_state();
    }

    @Override
    public void close() throws Exception {
        super.close();
        if (pool == null) {
            return;
        }
        pool.release_an_instance(this);
    }
}
