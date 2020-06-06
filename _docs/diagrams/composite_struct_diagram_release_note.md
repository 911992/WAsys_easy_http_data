# Composite Structure Diagram Version History
repo: https://github.com/911992/WAsys_pojo_http_data  
file: [class_diagram](./composite_struct_diagram.svg)  
Author: [911992](https://github.com/911992)  

**v0.2** (Jun 5, 2020)

* Fixed the misnamed `Poolable_Object` as `Object_Pool` in `WAsys Generic Object Pool` component
* Added `Object_Pool` to `WAsys Generic Object Pool` Component, and its association(s)
* Using On-page references
    * Added "On-page reference" to diagram(not-partial) "Meta Legends"
* Removed "Version History" from the diagram, in favor of this releasing note file
* Using correct `<<impl>>` associated between `Poolable_Fillable_Object_Adapter`, and `Fillable_Object` (previously as `<<inherit>>`)
* Using correct `<<impl>>` associated between `Poolable_Fillable_Object_Adapter`, and `Poolable_Object` (previously as `<<inherit>>`)
* Using class notation for type `Poolable_Fillable_Object_Adapter`
    * Removed the note at bottom about static abstract `Poolable_Fillable_Object_Adapter` class in interface notation
* Added **JNDI** Component
* Created this dedicated release note file

<hr/>

**v0.1.1** (May 19, 2020)

 * Fixed `Fillable_Object` associaten link

<hr/>

**v0.1** (May 10, 2020)

* Initial release