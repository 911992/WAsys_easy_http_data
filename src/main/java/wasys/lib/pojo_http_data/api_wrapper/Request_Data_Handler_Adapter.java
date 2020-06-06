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
    0.2 (20200605)
        • Updated/fixed documentation
        • copy_stream method now performs a manual streaming (drop for transferTo method, since it required java 9+)
        • Added DEFAULT_BUFFER_SIZE static field

    0.1.5(20200524)
        • Added missed Unfillable_Object_Ex throw type for fill_object method

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
import wasys.lib.pojo_http_data.exception.Unfillable_Object_Ex;

/**
 * Adapter class for {@link Request_Data_Handler} which implements some possible/common methods.
 * <p>
 * This is recommended to override this class, rather than implementing {@link Request_Data_Handler} when possible.
 * </p>
 * @author https://github.com/911992
 */
public abstract class Request_Data_Handler_Adapter<A> implements Request_Data_Handler<A> {

    /**
     * Default IO buffer size.
     * <p>
     * Default buffer size for streaming data from an in stream, to an output one
     * </p>
     */
    public static final int DEFAULT_BUFFER_SIZE = 1024;
    
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
    public void fill_object(Fillable_Object arg_object) throws Unfillable_Object_Ex{
        Generic_Object_Filler.process_request(this, arg_object);
    }

    /**
     * Copies content of {@code arg_ins} to {@code arg_out}.
     * <p>
     * This method could be overriden by target type if dedicated/custom action need to be taken.
     * </p>
     * <p>
     * This is recommended to not catch for any IO related exception during stream copying.
     * </p>
     * @param arg_ins the input stream contains data(probably part)
     * @param arg_out the output stream that need to be written
     * @return number of bytes have copied/transfer from {@code arg_ins} to {@code arg_out}
     * @throws IOException any io related exception
     */
    protected long copy_stream(InputStream arg_ins, OutputStream arg_out) throws IOException {
        if(arg_ins == null || arg_out==null){
            return -1;
        }
        byte _buff[] = new byte[DEFAULT_BUFFER_SIZE];
        long _res=0;
        int _r;
        while((_r = arg_ins.read(_buff))>0){
           arg_out.write(_buff, 0, _r);
           _res = _res +_r; 
        }
        return _res;
    }

}
