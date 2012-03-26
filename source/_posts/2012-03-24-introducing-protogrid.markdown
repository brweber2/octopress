---
layout: post
title: "Introducing Protogrid"
date: 2012-03-24 18:24
comments: true
categories: [Protogrid, Prototype, Programming Language, JVM]
---
Protogrid is a dynamically typed, prototype based programming language that runs on the JVM.

The project can be found on github at <https://github.com/brweber2/protogrid>

## Why?

* It runs on the JVM
* It allows simple interop with Java
* It has a REPL
* You can have a prototype working in no time

# Introduction

## For users

Protogrid strives to be simple to write and read.  Each form is intended to mean one thing so there should not be any confusion once you become familiar with the syntax.  In many ways this one to one mapping of syntax and semantics has been a simplifying and limiting factor in the design of the language.  This is especially true for developers who come to protogrid with pre-conceived notions from past experience.

* There is a single keyword 'var'.
* nil, undefined, true and false are built in prototypes.
* In protogrid there is a single numeric type.
* Variable names cannot contain numbers.
* Parenthesis always indicate function application, even when there is no function to apply.  For example, parenthesis used for changing or specifying order of operation precedence are strictly evaluated and the resulting value is left as the current context, but no function invocation actually takes place.
* Brackets indicate a function declaration.
* Braces indicate a scoped block.
* Single = denotes assignment.


## For programming language enthusiasts

Unfortunately, there isn't much here that will excite you.  Protogrid introduces <del>virtually</del> no new programming paradigms, it merely strives to make the developer experience more enjoyable.  The current form of Java interop is not something I've seen in any other language that runs on the JVM, but it is not something new or revolutionary.  It is arguably a nicer reflection API.  And yes, you will cringe if you look under the hood.  The implementation is insanely naive and non-performant in every aspect imaginable.


## Java Interop
To start interacting with the underlying Java platform, the first step is typically to wrap a Java class.

	var JavaString = WrapClass( java.lang.String )

The JavaString prototype now is a wrapped Java class.  It can be used in one of two ways.

* If invoked the arguments are used to determine which constructor should be called at runtime and if successful a wrapped instance is returned.
* Accessing a slot on the wrapped class will look for a field. If one is found a wrapped field is returned.  If no field is found, a wrapped methods prototype is returned that wraps all the static methods on the class with the matching name.  If this wrapped methods prototype is invoked it uses the arguments passed to it to determine at runtime which specific method should be invoked and a wrapped instance is returned with the result of the invocation.
 
&nbsp;

	var message = JavaString.format( "Hello %s", "World!" )
	
There are a few points of interest here.  There is more than one static format method on java.lang.String.  Additionally, the String "World!" is converted to an Object[] to support the variadic version of the format method used here.

A wrapped instance is a prototype that can be used with the protogrid, but that can be coerced back to its native Java type when necessary.

	println( message.toUpperCase() )
	
### What's missing from the interop story?

Quite a bit.  There is currently no way to implement an interface, to extend a class or to create a new interface or class.

## Show me more!

### Creating a new prototype

Simply clone an existing prototype

	var MyProto = clone( Object )
	
Setting and accessing slots on the prototype is trivial

	MyProto.foo = "barbaz"

	println( MyProto.foo )

Inspecting the prototype

	println( MyProto.slots() )
	
### Viewing the AST

At any point, the built in ast function can be called to print out a readable form of the Abstract Syntax Tree if there is every any doubt about what the interpreter is seeing.

	ast( 2 + 3 * 4 ^ 5 % 6 )
	
This will result in the following AST

	(  2   +  ( (  3   *  (  4   ^   5  )  )   %   6  )  )
	
### Loading code from a file

When loading code from a file, any side effects will occur at load time and a scope object with all the top level definitions from the file will be returned.  This can be assigned to a variable and values and functions can be accessed without worrying about collisions within the current scope.

Suppose we have a file located at src/hello_world.grid relative to the current directory with the following content.

	var sayHelloTo = [name] { println( strf( "Hello %s" List(name) ) ) }
	
To load this file, simply call the built in load function.

	var ns = load( "src/hello_world.grid" )

	ns.sayHelloTo( "Flynn" )
	
If you do not want to namespace any particular functions it is possible to map them into the curent scope by simply assigning them to a variable.

	var sayHelloTo = ns.sayHelloTo
	
### Operators

Math +  -  *  /  %  ^

Logic &&  ||

Comparison ==  !=  <  <=  >  >=


### Control Flow

There are built in functions for conditionals, looping, exceptions and exception handling.

Conditionals

	if ( true println( "true" ) println( "false" ) )

	if ( <cond> <true expr> <false expr> )
	
Looping

	loop( <structure> <function> )
	
The function passed to loop must have two parameters, the value for each iteration and in the index number of the iteration (zero based).

	while( <condition> <block> )
	
Exceptions

	var err = error( "Uh oh" )

	raise( err )

	try ( <block> <finally-block> )
	
But what about catching the exceptions?  A try block does not return the result of the block, it returns an object that contains a boolean indicating whether or not an exception was thrown and either the exception or the resultant value.  It is therefore possible to access slots on the resultant prototype to perform whatever error handling is necessary.

	var t = try( socket.write() socket.close() )
	
	if ( t.hasError raise( t.error ) t.result )
	

### Composite Data Structures

There are lists. There is a built in function to construct them and they can contain an object of any type.  Lists are zero indexed for access operations.

	var names = List( "Kevin", "Sam", "Quorra" )

	println( names.get( 1 ) )
	
## Because I have to...


### Hello World

	println( "Hello world!" )
	
### Factorial

	var fac = [n] {
		if ( n == 0 || n == 1
		1
		n * recur( n - 1 )  )
	}
In protogrid, recursion is achieved by calling the recur function instead of the current function by name.

## Getting started

The best way to get started is to download the executable protogrid jar and start experimenting with the REPL.  Syntax highlighting for IntelliJ IDEA and TextMate will be added to github in the very near future.

#### Down the road

* Syntax highlighting
* Compiled version
* Debugger
* Static typing
* Lazy evaluation
* Macros
* Named arguments
* Default values for parameters
* Richer Java interop
* De-structuring
* Targeting other platforms
