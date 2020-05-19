/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

/*
WAsys_pojo_http_data
File: Poolable_Request_Data_Handler_Adapter.java
Created on: May 19, 2020 3:25:53 AM | last edit: May 19, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
*/

package wasys.lib.pojo_http_data.api_wrapper;

import wasys.lib.generic_object_pool.Object_Pool;
import wasys.lib.generic_object_pool.api.Poolable_Object;


/**
 * 
 * @author https://github.com/911992
 */
public abstract class Poolable_Request_Data_Handler_Adapter<A> extends Request_Data_Handler_Adapter implements Poolable_Object,AutoCloseable{
    
    private Object_Pool pool;
    
    @Override
    public void set_pool(Object_Pool arg_pool) {
        this.pool = arg_pool;
    }

    @Override
    public void post_create() {

    }

    @Override
    public void pre_destroy() {

    }

    @Override
    public void close() throws Exception {
        if (pool == null) {
            return;
        }
        pool.release_an_instance(this);
    }
}
