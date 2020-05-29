# WAsys_pojo_http_data Release Note

repo: https://github.com/911992/WAsys_pojo_http_data  
Author: [911992](https://github.com/911992)  
*(NOTE: following list carries mentionable(not all) changes. For detailed changes, check source code(s))*  

**0.1.9** (May 30, 2020)  

0. Repo
    * Updated `README.md` file
        * Added *Implementations* section to list up all available wrappers and implementations
        * Added [Servlet 3.0 impl repo link](https://github.com/911992/WAsys_pojo_http_data_servlet3_wrapper)
        * Marked the *Servlet 3.0 Wrapper* task as completed

<br/>

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