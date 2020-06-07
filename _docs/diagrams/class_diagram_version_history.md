# Class Diagram Version History
repo: https://github.com/911992/WAsys_pojo_http_data  
file: [class_diagram](./class_diagram.svg)  
Author: [911992](https://github.com/911992)  

**v0.2.1** (Jun 8, 2020)

* Fixed the duplicated **`X`** in-page reference.
    * Named the correct reference to `Pool_Context` as **`Z`**
* Added missed dependency from `Generic_Object_Filler` to `Pool_Context`

<hr/>

**v0.2** (Jun 5, 2020)

* Updated class `Generic_Object_Filler`
    * Changed(renamed) `-process_request(:Request_Data_Handler,:Fillable_Object,:Vector<Class>):void` to `-process_request_internal(:Request_Data_Handler,:Fillable_Object,:ArrayList<Class>):void` in class `Generic_Object_Filler`
    * Added `ARRAYLIST_POOL_MAX_VAL_LOOKUP_KEY`, `ARRAYLIST_DEFAULT_POOL_MAX_VAL`, and `ARRAYLIST_POOL` static fields
    * Added a `static{}` (static block), and a note about it
    * Added `init_arraylist_pool(void):void` static method 
* Added `JNDI`, `javax.naming` package, and `InitialContext` class ref
* Added `ArrayList` class ref
* Added `Poolable_ArrayList` class
* Added `Factory` class
* Added `Pool_Context` class ref in `WAsys Generic Object Pool` component
* Using `ArrayList`(non-synchronized) list, instead of `Vector`(synchronized)
    * `~fields:Vector<Fillable_Object_Field_Signature_Cache>` to `~fields:ArrayList<Fillable_Object_Field_Signature_Cache>` in class `Fillable_Object_Parse_Result`
    * `ctx:Vector<Fillable_Object_Parse_Result>` to `ctx:ArrayList<Fillable_Object_Parse_Result>` in class `Fillable_Object_Signature_Context`
    * `-get_all_fields(:Class,arg_to_ctx:Vector<Field>):int` to `-get_all_fields(:Class,arg_to_ctx:ArrayList<Field>):int` in class `Fillable_Object_Parser`
* Added `DEFAULT_BUFFER_SIZE` static field in `Request_Data_Handler_Adapter` class
* Changed signature of  method `find_marked_setter_method(:Class,:String,:Class):Method` to `find_marked_setter_method(:Class,:Field):Method`

<hr/>

**v0.1.11** (Jun 1, 2020)

* Added method `part_streaming_done(:String,:int:bool):void`, for `Fillable_Object`

<hr/>

**v0.1.7** (May 26, 2020)

* Added missed `post_create(void):void`, and `pre_destroy(void):void` methods for type `Poolable_Fillable_Object_Adapter`
* Default value for `param_name(void):String` attrib/method in type `Field_Definition`

<hr/>

**v0.1.6** (May 25, 2020)  

* **very stupid bug, sorry about that**: removed `type_parse_cache` static field from `Fillable_Object_Adapter` class. (now fast cache is supposed to work, and should implemented by the user)
* Dropped implementation associate between `Fillable_Object_Adapter` and `Fillable_Object_Parse_Cache_Accelerator`
    * removed `_api_ex_set_type_parse_result` , and `_api_ex_get_type_parse_result` inherited members

<hr/>

**v0.1.4** (May 24, 2020)  

* Renamed method `get_params` to `get_param_vals` in class `Request_Data_Handler`

<hr/>

**v0.1.3** (May 21, 2020)  

* linked missed `Unfillable_Object_Ex` exception on `Request_Data_Handler.file_object(:Fillable_Object:void` method
* Updated `Generic_Object_Filler.read_and_set_param()`, and `Generic_Object_Filler.result_event()` methods. Now accept cached err message generation
* Changed method `has_failed_field_happened()` to `has_failed_field_happened()` in `Fillable_Object_Adapter` class
* Changed field `last_known_priority_fields_fill` to `last_known_failed_fields_fill` in `Fillable_Object_Adapter` class
* Removed method `is_object_in_a_valid_state()` of Fillable_Object_Adapter class
* Marking method `has_failed_field_happened` as `final` in `Fillable_Object_Adapter` class
* Moved `child_reset_state()` method from `Poolable_Fillable_Object_Adapter` to `Fillable_Object_Adapter` class
* `AutoClosable` implementation dropped from `Fillable_Object_Adapte`r, and now is implemented by `Poolable_Fillable_Object_Adapter` instead
* Removed `close(`) from `Fillable_Object_Adapter` method
* Added `back_to_pool()` method in `Poolable_Fillable_Object_Adapter`

<hr/>

**v0.1** (May 10, 2020)

* Initial release