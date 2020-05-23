/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

/*
WAsys_pojo_http_data
File: No_Type_Cache.java
Created on: May 19, 2020 4:45:23 AM
    @author https://github.com/911992
 
History:
    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
*/

package wasys.lib.pojo_http_data.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a {@link Fillable_Type} as non-cachable, this forces the parser to <b>not</b> the cache of the type fingerprint for next filling.
 * This is useful for very rare(almost once) type filling, or if the signature of the type would be changed, so cache will invalidate it.
 * This is not recommended to be used, as parsing a type may be a heavy process thing that could hit some performance.(try not t use, or know what does it mean)
 * @author https://github.com/911992
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface No_Type_Cache {

}
