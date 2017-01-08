# Javascript - Passing reference
"Passing in an object, however, passes it in by reference."  is simply wrong.  
It simply passes in a reference - by value. The reference itself cannot be altered -
only the contents of the thing it refers to.  
There is a big difference between "passing-by-reference" and "passing-a-reference-by-value".
```javascript
var struct = {id:"123", name:"jason"};
    
function setStruct(par) {
    par.name = "jack";
    par = {id:"432", name:"jini"};
}
    
setStruct(struct);
```