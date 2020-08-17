# WAsys_pojo_http_data Release Note

repo: https://github.com/911992/WAsys_pojo_http_data  
Author: [911992](https://github.com/911992)  
*(NOTE: following list carries mentionable(not all) changes. For detailed changes, check source code(s))*  

**0.2.5.1** (Aug 16, 2020)
0. Repo
    * Updated `README.md`file
        * Fixed the link "Servlet 3.0 Wrapper" link in "Implementations" sections
        * Added Meta section to link related links/stuffs

<hr/>

**0.2.5** (Aug 13, 2020)

0. Dedicated value bound check/rule for float-point(`float`,`double`) fields when using `Field_Definition` annotation, to avoid loosy float-to-integer conversion for string/stream size, and integer bound checking.
1. `Source_Code::Fillable_Object_Parser`
    * Calling related getter of `Field_Definition` based on annotated field's type
    * Fixes/changes becasue of `Field_Definition` and `Fillable_Object_Field_Signature` types changes
2. `Source_Code::Generic_Object_Filler`
    * Fixes/changes becasue of `Field_Definition` and `Fillable_Object_Field_Signature` types changes (err msg generating, and size/len check)
    * Getting related min/max values of related field signature during value bound/size check
    * Using `Number` instead of pair of `double`/`long` for keeping a cache of min/max values
    * `String` value lenght now is checked by `int` value, rather than long
3. `Source_Code::Fillable_Object_Field_Signature`
    * Removed `min_len_val` and `max_len_val` fields (and their getter and setters)
    * Added `min_val:Number`, and `max_val:Number` (with setter and getters funcs)
    * Updated constructors, to follow the new field changes(above)
4. `Source_Code::Field_Definition`
    * Removed `min_len_val` and `max_len_val` fields
    * Added `min_float_point_val:double`, and `max_float_point_val:double` for double/float related param definitions
    * Added `min_val_or_len:long` and `max_val_or_len:long` for size/len and integer related fields
5. `Source_Code::All`
    * Updated some documentations (including inheriting docs from supertypes where missed)
6. Diagrams
    * Updated class diagram (check changes [here](./_docs/diagrams/class_diagram_version_history.md))
7. Repo
    * Updated `pom.xml` file
        * Artifact to version `0.2.5` now
    * Updated `README.md`file
        * Updated maven repo version
        * Added *Field/Param Definition* section
        * Added [https://github.com/911992/WAsys_pojo_http_data_servlet3_wrapper_test](WAsys_pojo_http_data_servlet3_wrapper_test) repo link in 
        * Removed completed TODO tasks in TODOs section

<hr/>

**0.2.1** (Jul 24, 2020)

0. New type parsing policy, to ignore any field is started by dobule underscore(`__`), and is not `Field_Definition` annotated.
1. `Source_Code::Fillable_Object_Parser`
    * Updated `parse_field` method to support new type policy(above) about ignorring fields started with double-underscore `__` (and are not `Field_Definition`).
2. Repo
    * Updated `pom.xml` file
        * Artifact to version `0.2.1` now
    * Updated `README.md`file
        * Stating new type parsing policy in *POJO Parsing Method* section
        * Updated maven repo version
        * Adding new task about [`WAsys_Java_type_util`](https://github.com/911992/WAsys_Java_type_util) and integration in *TODOs* section.

<hr/>

**0.2.0** (Jun 5, 2020)  

0. Artifact now is accessible from **Maven** central repository (wonderful, thanks sonatype, and apache)
1. Dependency to [WAsys_generic_object_pool](https://github.com/911992/WAsys_simple_generic_object_pool) now is an **essential** now.
2. Moved all source codes from `src` folder to `src/main/java` to make it as a std maven project
3. `Source_Code::all`
    * Reworked, and fixed many issues about javadoc of all source codes
4. Using non-`synchronized` `ArrayList`, instead of `Vector`(as a `synchronized` list) where thread-safety is not an issue, or already covered by another level in almost all source files.
5. `Source_Code::Fillable_Object_Parse_Result`
    * Using `ArrayList`(non thread-safe) instead of `Vector`(as thread-safe) for `fields` attribute/field
6. `Source_Code::Fillable_Object_Parser`
    * Using `ArrayList`(non thread-safe) instead of `Vector`(thread-safe), as the `Fillable_Object_Parse_Result` constructor now works/needs the same way
    * Changed method `find_marked_setter_method(:Class,:String,:Class):Method` signature to `find_marked_setter_method(:Class,:Field):Method`
7. `Source_Code::Fillable_Object_Signature_Context`
    * Using `ArrayList`(non thread-safe) instead of `Vector`(thread-safe) for `ctx` field
8. `Source_Code::Generic_Object_Filler`
    * Using `ArrayList`(non thread-safe) instead of `Vector`(thread-safe) as thread-safety is not considered, or covered
    * Using internal pooled version of `ArrayList`(`Poolable_ArrayList`) when a filling is requested for holding filling types in `process_request()`, and `process_request_internal()` methods
    * Added `ARRAYLIST_POOL_MAX_VAL_LOOKUP_KEY:String`, and `ARRAYLIST_POOL:Object_Pool` `static` fields
    * Added `static init_arraylist_pool()` method, and a `static` block that calls it during class loading
    * Renamed method `process_request(:Request_Data_Handler,:Fillable_Object,:ArrayList<Class>):void` to `process_request_internal` (polymorphism is good, but *unique names* are better)
9. `Source_Code::Fillable_Object`
    * Changed `arg_success` arg name to `arg_result` of method `set_object_fill_result`
10. `Source_Code::Fillable_Object_Adapter`
    * Reseting state of `this` instance will not mark/set the `err_msg` field as `null`, instead sets the length to `zero`.
11. `Source_Code::Request_Data_Handler_Adapter`
    * `copy_stream` method now performs a manual streaming (drop for `transferTo` method, since it required java 9+)
12. Added `package-info.java` file for each packages contains member.
13. Added `Poolable_ArrayList` class, and its inner class `Factory` to act as a `Poolable_Object` of `ArrayList`
14. Diagrams
    * Updated class diagram (check changes [here](./_docs/diagrams/class_diagram_version_history.md))
    * Updated composition structure diagram (check changes [here](./_docs/diagrams/composite_struct_diagram_release_note.md))
15. Repo
    * Added `pom.xml` maven file
    * Added `README.txt`, and `README.txt` files
    * Added `target`(as maven output) to `.gitignore`
    * Updated `README.md` file
        * Fixed soem typos, and misnamed const vals
        * Marking the [WAsys_generic_object_pool](https://github.com/911992/WAsys_simple_generic_object_pool) as an **essential requirements**
        * Removed the note, about optional dependency to [WAsys_generic_object_pool](https://github.com/911992/WAsys_simple_generic_object_pool) repo
        * Added **Maven Repository** section (hell yeses)
        * Added **JNDI** section, as new component dependency
        * Edited the **Utilizing The Lib** section, based on recent changes
        * Added **Maven repo** as a completed task in TODOs section
        * All **TODOs** are now marked as completed (awesome)
    * Added `composite_struct_diagram_release_note.md` file, as a dedicated releas-note for *composition structure diagram*.

<hr/>

**0.1.11** (Jun 1, 2020)  

0. **API Change**: POJO Filler(`Generic_Object_Filler` or any other concreted one) should inform the POJO about a `Stream_To_Field` streaming op, if the io op was ok, or nu by invoking the `Filleble_Object.part_streaming_done()` method.
1. `Source_Code::Generic_Object_Filler`
    * Calling method `part_streaming_done()`, when a part stream should be performed as `Stream_To_Field` mode, to inform if io stream was ok by related `Request_Data_Handler`
2. `Source_Code::Fillable_Object`
    * Added `part_streaming_done(:String,:int.:bool):void` method
3. Diagrams
    * Updated class diagram (check changes [here](./_docs/diagrams/class_diagram_version_history.md))
4. Repo
    * Update `README.md` file
        * Updated "HTTP File(part) Upload Handling" section, explained more about streamable fields.

<hr/>

**0.1.10** (May 31, 2020)  

0. `Source_Code::Generic_Object_Filler`
    * Removed a redundant `if` block, from `process_request()` method

<hr/>

**0.1.9** (May 30, 2020)  

0. Repo
    * Updated `README.md` file
        * Added *Implementations* section to list up all available wrappers and implementations
        * Added [Servlet 3.0 impl repo link](https://github.com/911992/WAsys_pojo_http_data_servlet3_wrapper)
        * Marked the *Servlet 3.0 Wrapper* task as completed

<hr/>

**0.1.8** (May 28, 2020)  

0. `Source_Code::Generic_Object_Filler`
    * *(a bad bug fix, sorry for that `-_-` )*, fixed the redundant `get_param_at()` invocation, when the field is an `OutputStream` by field filling(`read_and_set_param()` method)
1. Repo
    * Updated `README.md` file
        * Added *Reading Parameter Order And Policy*, abd *`Request_Data_Handler` And `multipart` Request Data* sections
        * Explinations about possible stremable parts with a `multipart` request(including an example)

<hr/>

**0.1.7** (May 26, 2020)  

0. `Source_Code::Field_Definition`
    * `param_name()` now comes with `default` val as an empty String value (when param name is same as field name)
1. `Source_Code::Poolable_Fillable_Object_Adapter`
    * Added missed `Poolable_Object` `post_create()`, and `pre_destroy()` methods (default method stab) to unforce target type to implement them when not required.
3. Diagrams
    * Updated class diagram (check changes [here](./_docs/diagrams/class_diagram_version_history.md))

<hr/>

**0.1.6** (May 25, 2020)  

0. **Important**: Parsing and filling a `Fillable_Object` type now is done as **`parent(s) -> child`** field processing when the fill mode is set to `Reflection_Type_Fields`. This means top-parent fields will be considered for filling first, while actual/real(the type was asked for filling) field(s) type will be the last one.
1. **Importand**: one **very stupid bug** by me, about global-fast cache, now fixed.
    * `Fillable_Object_Adapter` is no more a `Fillable_Object_Parse_Cache_Accelerator`
    * Fixed the globally type fingerprint cache by `Fillable_Object_Adapter` static field(which is a totally stupid idea), now fixed/removed.
2. `Source_Code::Fillable_Object_Parser`
    * Finding fields in reflection mode now follows the parent-to-child order (new type parsing/filling policy)
3. `Source_code::Fillable_Object_Parse_Cache_Accelerator`
    * Updated the documentation, removed the link/ref to `Fillable_Object_Adapter`
3. Diagrams
    * Updated class diagram (check changes [here](./_docs/diagrams/class_diagram_version_history.md))
    * Updated Fillable_Object Scenario 0 diagram (check changes [here](./_docs/diagrams/inner_fillable_scenario0_version_history.md))
4. Repo
    * Updated `README.md` file
        * Added "Filling Policy For `Reflection_Type_Fields` Type Prsing Mode" missed heading
        * Updated filling scenario order (as inluded in "Fillable_Object Scenario 0" [file](./_docs/diagrams/inner_fillable_scenario0.svg))
        * Added caption for diagrams
        * Dedicated part about global cache keeping using `Fillable_Object_Parse_Cache_Accelerator`
    * [`File::class_diagram_version_history.md`](./_docs/diagrams/class_diagram_version_history.md)
        * Fixed wrong class-diagram file link
    * Added "Fillable_Object Scenario 0" dedicated versioning [file](./_docs/diagrams/inner_fillable_scenario0_version_history.md)
    * Added "Typical Fillable_Object Type that supports Fillable_Object_Parse_Cache_Accelerator" [diagram](./_docs/diagrams/typical_fillable_object_type_with_cache_acc_partial.svg), and its change history [file](./_docs/diagrams/typical_fillable_object_type_with_cache_acc_version_history.md).

<hr/>

**0.1.5** (May 24, 2020)  

0. `Source_Code::Request_Data_Handler`
    * Renamed method `get_params` to `get_param_vals`, now it looks less confusing
1. `Source_Code::Request_Data_Handler_Adapter`
    * Added missed `Unfillable_Object_Ex` throw type for `fill_object()` method
2. Diagrams
    * Updated class diagram (check changes [here](./_docs/diagrams/class_diagram_version_history.md))

<hr/>

**0.1.4** (May 24, 2020)  

0. `Source_Code::Generic_Object_Filler`
    * `Pass_Stream` file upload processing now may result an IOException, or enexpected `null` ptr from http request impl, so op now is done using a `try-catch` block
        * Marking the related field/object filling state as failed
1. `Source_Code::Request_Data_Handler`
    * `get_part_stream_at` and `get_part_stream` methods now may throw an `IOException`
2. `Request_Data_Handler_Adapter`
    * `IOException` throw clause for `stream_part` method

<hr/>

**0.1.3** (May 21, 2020)  

0. Added some doc for API(public)-level types (oh, it was hard `-_-` )  
1. `Source_Code::Generic_Object_Filler`
    * Added missed `Unfillable_Object_Ex` throws clause for `process_request()` methods
    * Asking for field error message generation is done at object level now(previously field level). `Fillable_Object.generate_result_err_msg()` is called once for each fill(no matter how many errs)
    * Updated(added param) `read_and_set_param`, and `set_result` methods. Added a `bool` to specify if the error mesage generation should be considered (completes above)
    * Triggering the "genrated failed fill message" now happens once it's set(if applicable)
2. `Source_Code::Fillable_Object`
    * Set the `default` err message gen to `false`
3. `Source_Code::Fillable_Object_Adapter`
    * Renamed method `has_failed_field_happened` to `has_failed_field_happened`
    * Renamed field `last_known_priority_fields_fill` to `last_known_failed_fields_fill`
    * Removed method `is_object_in_a_valid_state`
    * Method `has_failed_field_happened` is `final` now
    * Moved `child_reset_state()` method from `Poolable_Fillable_Object_Adapter` to here
    * Dropped `AutoClosable` interface implementtion, removed `close()` method (moved to `Poolable_Fillable_Object_Adapter`)
4. `Source_Code::Poolable_Fillable_Object_Adapter`
    * Added `back_to_pool()` method
    * `AutoClosable` implementation, `close()` now just asks for backing to pool by `calling back_to_pool()` method
    * Removed `child_reset_state()` method, moved to `super` type
    * Removed call of `child_reset_state()` method in `reset_state()` method
5. `Source_Code::Request_Data_Handler`
    * Added missed `Unfillable_Object_Ex` extion for `fill_object(:Fillable_Object):void` method
6. `Source_Code::all`
    * Removed the `last edit: mmm dd, yyyy` from header parts(since hard to update)
7. Diagrams
    * Updated object pool state diagram
    * Updated class diagram (check dedicated [versioning file](./_docs/diagrams/class_diagram_version_history.md))
8. Repo
    * Added class diagram dedicated versioning [file](./_docs/diagrams/class_diagram_version_history.md)
    * Removed `lib` folder(gitignore)
    * Updated this file layout/format
    * Updated the `README.md` file
        * updated the TODOs section

<hr/>

**0.1.2** (May 20, 2020)  

0. `Source_Code::Generic_Object_Filler`
    * Fixed the wrong type check at read_and_set_param (`Poolable_Object` -> `Fillable_Object`) thanks [WAsys_pojo_http_data_test](https://github.com/911992/WAsys_pojo_http_data_test)  
    * Removed the redundant `Poolable_Object` type import
1. Repo
    * Fixed the wrong date format and layout of this release_note file
    * Update the repo README file, added the test sample repo link

<hr/>

**0.1.1** (May 19, 2020)  

0. Diagrams
    * Updated composition/struct diagram

<hr/>

**Initial Release 0.1** (May 10, 2020)