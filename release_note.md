# WAsys_pojo_http_data Release Note

repo: https://github.com/911992/WAsys_pojo_http_data  
Author: [911992](https://github.com/911992)  
*(NOTE: following list carries mentionable(not all) changes. For detailed changes, check source code(s))*  

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

**0.1.2** (May 20, 2020)  

0. `Source_Code::Generic_Object_Filler`
    * Fixed the wrong type check at read_and_set_param (`Poolable_Object` -> `Fillable_Object`) thanks [WAsys_pojo_http_data_test](https://github.com/911992/WAsys_pojo_http_data_test)  
    * Removed the redundant `Poolable_Object` type import
1. Repo
    * Fixed the wrong date format and layout of this release_note file
    * Update the repo README file, added the test sample repo link

**0.1.1** (May 19, 2020)  

0. Diagrams
    * Updated composition/struct diagram

**Initial Release 0.1** (May 10, 2020)