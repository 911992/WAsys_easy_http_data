/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Part_Field_Stream_Mode.java
Created on: May 18, 2020 4:33:53 AM
    @author https://github.com/911992
 
History:
    0.2 (20200605)
        • Updated/fixed documentation

    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api;

/**
 * Specifies how an available file/part stream should be proceed.
 * @author https://github.com/911992
 */
public enum Part_Field_Stream_Mode {
    /**
     * indicates that actual HTTP Request should copy/stream the file/part to the related POJO output stream.
     */
    Stream_To_Field,
    /**
     * Indicates that POJO wishes to handle the file streaming itself, so stream will be passed to the POJO({@link Fillable_Object}, method:{@code part_stream}).
     */
    Pass_Stream
}
