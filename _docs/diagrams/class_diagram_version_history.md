# Class Diagram Version History
repo: https://github.com/911992/WAsys_pojo_http_data  
file: [class_diagram](./_docs/diagrams/class_diagram.svg)  
Author: [911992](https://github.com/911992)  

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