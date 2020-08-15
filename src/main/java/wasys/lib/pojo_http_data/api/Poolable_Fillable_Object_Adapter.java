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
    0.2.5 (20200813)
        • Documentation fix/update.

    0.2 (20200605)
        • Updated/fixed documentation

    0.1.7(20200526)
        • Added missed Poolable_Object post_create(), and pre_destroy() methods (default method stab) to unforce target type to implement them when not required

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
 * The {@link Poolable_Object}(poolable) adapter of {@link Fillable_Object}.
 * <p>
 * This type is a poolable version of {@link Fillable_Object}, that extends from its adapter {@link Fillable_Object_Adapter}.
 * </p>
 * <p>
 * It has proper/required implementation to being a valid poolable object, that could be pooled/reused under term of {@code WAsys_simple_generic_object_pool} project, using a {@link Object_Pool} type.
 * </p>
 * @author https://github.com/911992
 */
public abstract class Poolable_Fillable_Object_Adapter extends Fillable_Object_Adapter implements Poolable_Object , AutoCloseable {

    /**
     * Holds the related/associated pool object that should handle this instance.
     * <p>
     * The ptr is passed during initialization of this type from the related {@link Object_Pool}
     * </p>
     */
    @No_Param
    private Object_Pool pool;
    
    /**
     * Release this {@code POolable_Object} by asking the associated pool instance(if not null).
     * <p>
     * This method <b>WILL NOT</b> cause a fill state restarting explicitly, but the pool may decide to reset the object(which will reset the fill state) before the object gets back to the pool.
     * </p>
     */
    public void back_to_pool(){
        if (pool == null) {
            return;
        }
        pool.release_an_instance(this);
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void post_create() {
        
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void pre_destroy() {
        
    }
    
    /**
     * {@inheritDoc }
     */
    @Override
    public void set_pool(Object_Pool arg_pool) {
        this.pool = arg_pool;
    }

    /**
     * Resetting the {@code Poolable_Object} by calling the super {@code reset_fill_state()}.
     * <p>
     * This method asks super method for a fill state resetting.
     * </p>
     * <p>
     * Once {@code super}({@link Fillable_Object_Adapter}) performs its reset state, it will call {@code child_reset_state()} method, that it's a overridable method.
     * </p>
     */
    @Override
    final public void reset_state() {
        super.reset_fill_state();
    }

    /**
     * Asks for backing to pool, no fill state resetting call explicitly.
     * @throws Exception (almost zero ex), it's there as part of {@link AutoCloseable} definition.
     */
    @Override
    public void close() throws Exception {
        back_to_pool();
    }
}
