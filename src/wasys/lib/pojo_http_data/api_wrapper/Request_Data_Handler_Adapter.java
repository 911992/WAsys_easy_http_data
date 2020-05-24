/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Request_Data_Handler_Adapter.java
Created on: May 13, 2020 6:33:05 PM | last edit: May 13, 2020
    @author https://github.com/911992
 
History:
    0.1.4(20200524)
        • IOException throw clause for stream_part method, as the parent/interface def has changed

    0.1.3(20200521)
        • Added some javadoc

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api_wrapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import wasys.lib.pojo_http_data.Generic_Object_Filler;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.api.container.Request_Data_Handler;

/**
 * Adapter class for Request_Data_Handler which implements some possible methods
 * @author https://github.com/911992
 */
public abstract class Request_Data_Handler_Adapter<A> implements Request_Data_Handler<A> {

    @Override
    public String get_param(String arg_param) {
        return get_param_at(arg_param, 0);
    }

    @Override
    public String get_part_filename(String arg_param) {
        return get_part_filename_at(arg_param, 0);
    }

    @Override
    public String get_part_mime_part(String arg_param) {
        return get_part_mime_part_at(arg_param, 0);
    }

    @Override
    public long get_part_size(String arg_param) {
        return get_part_size_at(arg_param, 0);
    }

    @Override
    public long stream_part(String arg_param, OutputStream arg_out_to) throws IOException {
        return stream_part_at(arg_param, 0, arg_out_to);
    }

    @Override
    public long stream_part_at(String arg_param, int arg_index, OutputStream arg_out_to) throws IOException {
        InputStream _is = get_part_stream_at(arg_param, arg_index);
        if (_is == null) {
            return -1;
        }
        return copy_stream(_is, arg_out_to);
    }

    @Override
    public InputStream get_part_stream(String arg_param)throws IOException {
        return get_part_stream_at(arg_param, 0);
    }

    @Override
    public void fill_object(Fillable_Object arg_object) {
        Generic_Object_Filler.process_request(this, arg_object);
    }

    protected long copy_stream(InputStream arg_ins, OutputStream arg_out) throws IOException {
        return arg_ins.transferTo(arg_out);
    }

}
