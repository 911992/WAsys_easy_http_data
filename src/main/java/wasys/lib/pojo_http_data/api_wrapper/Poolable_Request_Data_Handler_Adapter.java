/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

/*
WAsys_pojo_http_data
File: Poolable_Request_Data_Handler_Adapter.java
Created on: May 19, 2020 3:25:53 AM
    @author https://github.com/911992
 
History:
    0.2.5 (20200813)
        • Documentation fix/update.

    0.2 (20200605)
        • Updated/fixed documentation

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
*/

package wasys.lib.pojo_http_data.api_wrapper;

import wasys.lib.generic_object_pool.Object_Pool;
import wasys.lib.generic_object_pool.api.Poolable_Object;


/**
 * Adapter class for {@link Request_Data_Handler_Adapter} which makes it as a {@link Poolable_Object}.
 * <p>
 * It has proper/required implementation to being a valid poolable object, that could be pooled/reused under term of {@code WAsys_simple_generic_object_pool} project, using a {@link Object_Pool} type.
 * </p>
 * @author https://github.com/911992
 * @param <A> type of target concreted HTTP request
 */
public abstract class Poolable_Request_Data_Handler_Adapter<A> extends Request_Data_Handler_Adapter implements Poolable_Object,AutoCloseable{
    
    /**
     * Holds the related/associated pool object that should handle this instance.
     * <p>
     * The ptr is passed during initialization of this type from the related {@link Object_Pool}
     * </p>
     */
    private Object_Pool pool;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void set_pool(Object_Pool arg_pool) {
        this.pool = arg_pool;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void post_create() {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pre_destroy() {

    }

    /**
     * Asks for backing {@code this} instance to the associated pool.
     * @throws Exception (almost zero ex), it's there as part of {@link AutoCloseable} definition.
     */
    @Override
    public void close() throws Exception {
        if (pool == null) {
            return;
        }
        pool.release_an_instance(this);
    }
}
