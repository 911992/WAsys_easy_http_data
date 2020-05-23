/*
 * Copyright (c) 2020, https://github.com/911992 All rights reserved.
 * License BSD 3-Clause (https://opensource.org/licenses/BSD-3-Clause)
 */

 /*
WAsys_pojo_http_data
File: Request_Data_Handler.java
Created on: May 12, 2020 10:49:35 PM
    @author https://github.com/911992
 
History:
    0.1.3(20200521)
        • Updated the header(this comment) part
        • Added some javadoc
        • Added missed Unfillable_Object_Ex extion for fill_object(:Fillable_Object):void method

    initial version: 0.1(20200510)
 */
package wasys.lib.pojo_http_data.api.container;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import wasys.lib.pojo_http_data.api.Fillable_Object;
import wasys.lib.pojo_http_data.exception.Unfillable_Object_Ex;

/**
 * This interface works a bridge/proxy between the lib, user and the actual HTTP request object that has http data ready for business.
 * This interface probably should be implemented by the HTTP Server Component, and not the user.
 * Has essential methods for POJO filling op
 * @author https://github.com/911992
 */
public interface Request_Data_Handler<A> {

    /**
     * Returns a http parameter(not a file-upload from form data) in string scheme from index 0
     * @param arg_param name of the parameter
     * @return the parameter value, or {@code null} if the parameter is missed
     */
    public String get_param(String arg_param);

    /**
     * Returns a http parameter(not a file-upload from form data) in string scheme from given index
     * @param arg_param_name name of the http parameter
     * @param arg_idx index of the parameter
     * @return the parameter value, or {@code null} if the parameter is missed
     */
    public String get_param_at(String arg_param_name, int arg_idx);

    /**
     * Return all parameter(not a file-upload from form data) values of given parameter name
     * @param arg_param_name name of the parameter
     * @return an array of String contains all parameter values
     */
    public String[] get_params(String arg_param_name);

    /**
     * @return Returns the unique parameter(not a file-upload from form data) names
     */
    public int param_count();

    /**
     * @return {@code true} if the http request is a multi-part form data(regardless if it has any file/part upload or not)
     */
    public boolean is_multipart_request();

    /**
     * Returns the file-upload/part associated name of given parameter, from index 0
     * @param arg_param name of the parameter 
     * @return name of the uploaded file name if applicable, or {@code null} otherwise
     */
    public String get_part_filename(String arg_param);

    /**
     * Returns the file-upload/part associated name of given parameter, from given index
     * @param arg_param name of the parameter 
     * @param arg_idx index of the parameter name
     * @return name of the uploaded file name if applicable, or {@code null} otherwise
     */
    public String get_part_filename_at(String arg_param, int arg_idx);

    /**
     * Returns the file-upload/part associated mime of given parameter, from index 0
     * @param arg_param name of the parameter 
     * @return mime type of the uploaded file, or {@code null} if missed
     */
    public String get_part_mime_part(String arg_param);

    /**
     * Returns the file-upload/part associated mime of given parameter, from given index
     * @param arg_param name of the parameter 
     * @param arg_idx index of the parameter name
     * @return mime type of the uploaded file, or {@code null} if missed
     */
    public String get_part_mime_part_at(String arg_param, int arg_idx);

    /**
     * Returns the file-upload/part associated size of given parameter, from index 0
     * @param arg_param name of the parameter 
     * @return A <b>zero or positive</b> value indicates the uploaded file size, or {@code -1} if there is no part/file-upload available
     */
    public long get_part_size(String arg_param);
    
    /**
     * Returns the file-upload/part associated size of given parameter, from given index
     * @param arg_param name of the parameter
     * @param arg_idx index of the parameter name
     * @return A <b>zero or positive</b> value indicates the uploaded file size, or {@code -1} if there is no part/file-upload available
     */
    public long get_part_size_at(String arg_param, int arg_idx);

    /**
     * Streams part/file-upload from given parameter name(index 0), to the given output stream
     * @param arg_param name of the parameter
     * @param arg_out_to the stream part(file-upload) need to be copied
     * @return number of bytes could be written
     * @throws IOException 
     */
    public long stream_part(String arg_param, OutputStream arg_out_to) throws IOException;

    /**
     * Streams part/file-upload from given parameter name at given index, to the given output stream
     * @param arg_param name of the parameter
     * @param arg_index index of the parameter name
     * @param arg_out_to the stream part(file-upload) need to be copied
     * @return number of bytes could be written
     * @throws IOException 
     */
    public long stream_part_at(String arg_param, int arg_index, OutputStream arg_out_to) throws IOException;

    /**
     * Returns the input stream(probably buffered) of the part/file-upload named as given parameter
     * @param arg_param name of the parameter
     * @return the stream contains part/file-upload data
     */
    public InputStream get_part_stream(String arg_param);

    /**
     * Returns the input stream(probably buffered) of the part/file-upload named as given parameter at given index
     * @param arg_param name of the parameter
     * @param arg_idx index of the parameter name
     * @return the stream contains part/file-upload data
     */
    public InputStream get_part_stream_at(String arg_param, int arg_idx);

    /**
     * Fills the given {@link Fillable_Object} argument, either using a custom filler/parser or default ones.
     * @param arg_object
     * @throws Unfillable_Object_Ex 
     */
    public void fill_object(Fillable_Object arg_object)throws Unfillable_Object_Ex;

    /**
     * @return returns the associated real http request object(if any/applicable)
     */
    public A get_associated_request();
}
