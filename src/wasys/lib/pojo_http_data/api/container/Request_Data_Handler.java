/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Request_Data_Handler.java
Created on: May 12, 2020 10:49:35 PM | last edit: May 12, 2020
    @author https://github.com/911992
 
History:
    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api.container;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import wasys.lib.pojo_http_data.api.Fillable_Object;

/**
 *
 * @author https://github.com/911992
 */
public interface Request_Data_Handler<A> {

    public String get_param(String arg_param);

    public String get_param_at(String arg_param_name, int arg_idx);

    public String[] get_params(String arg_param_name);

    public int param_count();

    public boolean is_multipart_request();

    public String get_part_filename(String arg_param);

    public String get_part_filename_at(String arg_param, int arg_idx);

    public String get_part_mime_part(String arg_param);

    public String get_part_mime_part_at(String arg_param, int arg_idx);

    public long get_part_size(String arg_param);

    public long get_part_size_at(String arg_param, int arg_idx);

    public long stream_part(String arg_param, OutputStream arg_out_to) throws IOException;

    public long stream_part_at(String arg_param, int arg_index, OutputStream arg_out_to) throws IOException;

    public InputStream get_part_stream(String arg_param);

    public InputStream get_part_stream_at(String arg_param, int arg_idx);

    public void fill_object(Fillable_Object arg_object);

    public A get_associated_request();
}
