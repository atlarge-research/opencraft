**A maximum depth of 1 in nested function calls**
<br>Example: 
<br>`GetX(GetY()) //is allowed`
<br>`GetX(GetY(GetX())) //is not allowed`


**A maximum depth of 1 for nested class declarations**
<br>Example:
```
public class test() {
    public class innerTest() {}
} //is allowed
```
```
public class test() {
    public class innerTest() {
        public class innerInnerTest() {}
    }
} //is not allowed`
```

**Order of a class: classes -> variables -> constructors -> methods**
<br>Example:
```
class Class {

  // classes

  // variables

  // constructors

  // methods
}
```
**While lines around code blocks; No white line with a single block; else after 
function def and in between every block**
<br>Example:
```
public single() {
  single();
  block();
}

public multiple() {

  first();
  block();

  second();
  block();
}
```


**The regular expressions allow for adding numbers to names but we should 
avoid the use of this.**