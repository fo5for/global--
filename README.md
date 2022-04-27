A crude, probably broken parser for detecting global variables in C++ code

## Usage
Place files to parse in `input/` or its subdirectories. Run the program using `sbt run` to parse all files. Files with global variables will fail to parse.

## Defects
- top-level wildcards deprecated since scala 2.13.7, wait for fastparse to migrate to scala 3

### False positives
- function declarations
    - `int b(a);` can be a function or a variable declaration depending on whether `a` is a type or a variable.
- static member variables?
    - but they are just scoped global variables
