---
layout: post
title: "Introducing Protogrid"
date: 2012-03-24 18:24
comments: true
categories: [Protogrid, Prototype, Programming Language, JVM]
---
Protogrid is a prototype based programming language that runs on the JVM.

More details can be found [here](http://www.brweber2.com/blog) and the project can be found on github at <https://github.com/brweber2/protogrid>

## Why?

* It runs on the JVM
* It allows simple interop with Java
* It has a REPL
* You can have a prototype working in no time

## Introduction

Protogrid is a dynamically typed, prototype based programming language that runs on the JVM.

It can be found on github at <a title="http://www.github.com/brweber2/protogrid" href="http://www.github.com/brweber2/protogrid">http://www.github.com/brweber2/protogrid</a>
<h1><strong>For users:</strong></h1>
Protogrid strives to be simple to write and read.  Each form is intended to mean one thing so there should not be any confusion once you become familiar with the syntax.  In many ways this one to one mapping of syntax and semantics has been a simplifying and limiting factor in the design of the language.  This is especially true for developers who come to protogrid with pre-conceived notions from past experience.
<ul>
	<li>There is a single keyword 'var'.</li>
	<li>nil, undefined, true and false are built in prototypes.</li>
	<li>In protogrid there is a single numeric type.</li>
	<li>Variable names cannot contain numbers.</li>
	<li>Parenthesis always indicate function application, even when there is no function to apply.  For example, parenthesis used for changing or specifying order of operation precedence are strictly evaluated and the resulting value is left as the current context, but no function invocation actually takes place.</li>
	<li>Brackets indicate a function declaration.</li>
	<li>Braces indicate a scoped block.</li>
	<li>Single = denotes assignment.</li>
</ul>
<h1><strong>For programming language enthusiasts:</strong></h1>
Unfortunately, there isn't much here that will excite you.  Protogrid introduces <del>virtually</del> no new programming paradigms, it merely strives to make the developer experience more enjoyable.  The current form of Java interop is not something I've seen in any other language that runs on the JVM, but it is not something new or revolutionary.  It is arguably a nicer reflection API.  And yes, you will cringe if you look under the hood.  The implementation is insanely naive and non-performant in every aspect imaginable.

&nbsp;
<h1><strong>Java Interop</strong></h1>
To start interacting with the underlying Java platform, the first step is typically to wrap a Java class.
<blockquote>var JavaString = WrapClass( java.lang.String )</blockquote>
&nbsp;

The JavaString prototype now is a wrapped Java class.  It can be used in one of two ways.
<ol>
	<li>If invoked the arguments are used to determine which constructor should be called at runtime and if successful a wrapped instance is returned.</li>
	<li>Accessing a slot on the wrapped class will look for a field.  If one is found a wrapped field is returned.  If no field is found, a wrapped methods prototype is returned that wraps all the static methods on the class with the matching name.  If this wrapped methods prototype is invoked it uses the arguments passed to it to determine at runtime which specific method should be invoked and a wrapped instance is returned with the result of the invocation.</li>
</ol>
&nbsp;
<blockquote>var message = JavaString.format( "Hello %s", "World!" )</blockquote>
There are a few points of interest here.  There is more than one static format method on java.lang.String.  Additionally, the String "World!" is converted to an Object[] to support the variadic version of the format method used here.

A wrapped instance is a prototype that can be used with the protogrid, but that can be coerced back to its native Java type when necessary.

&nbsp;
<blockquote>println( message.toUpperCase() )</blockquote>
<h2>What's missing from the interop story?</h2>
Quite a bit.  There is currently no way to implement an interface, to extend a class or to create a new interface or class.
<h1>Show me more!</h1>
<h2>Creating a new prototype</h2>
Simply clone an existing prototype
<blockquote>var MyProto = clone( Object )</blockquote>
Setting and accessing slots on the prototype is trivial
<blockquote>MyProto.foo = "barbaz"

println( MyProto.foo )</blockquote>
Inspecting the prototype
<blockquote>println( MyProto.slots() )</blockquote>
<h2>Viewing the AST</h2>
At any point, the built in ast function can be called to print out a readable form of the Abstract Syntax Tree if there is every any doubt about what the interpreter is seeing.
<blockquote>ast( 2 + 3 * 4 ^ 5 % 6 )</blockquote>
This will result in the following AST
<blockquote>(  2   +  ( (  3   *  (  4   ^   5  )  )   %   6  )  )</blockquote>
<h2>Loading code from a file</h2>
When loading code from a file, any side effects will occur at load time and a scope object with all the top level definitions from the file will be returned.  This can be assigned to a variable and values and functions can be accessed without worrying about collisions within the current scope.

Suppose we have a file located at src/hello_world.grid relative to the current directory with the following content.
<blockquote>var sayHelloTo = [name] { println( strf( "Hello %s" List(name) ) ) }</blockquote>
To load this file, simply call the built in load function.
<blockquote>var ns = load( "src/hello_world.grid" )

ns.sayHelloTo( "Flynn" )</blockquote>
If you do not want to namespace any particular functions it is possible to map them into the curent scope by simply assigning them to a variable.
<blockquote>var sayHelloTo = ns.sayHelloTo</blockquote>
<h2>Operators</h2>
Math + - * / % ^

Logic &amp;&amp; ||

Comparison == != &lt; &lt;= &gt; &gt;=
<h2>Control Flow</h2>
There are built in functions for conditionals, looping, exceptions and exception handling.

Conditionals
<blockquote>if ( true println( "true" ) println( "false" ) )

if ( &lt;cond&gt; &lt;true expr&gt; &lt;false expr&gt; )</blockquote>
Looping
<blockquote>loop( &lt;structure&gt; &lt;function&gt; )</blockquote>
The function passed to loop must have two parameters, the value for each iteration and in the index number of the iteration (zero based).
<blockquote>while( &lt;condition&gt; &lt;block&gt; )</blockquote>
Exceptions
<blockquote>var err = error( "Uh oh" )

raise( err )

try ( &lt;block&gt; &lt;finally-block&gt; )</blockquote>
But what about catching the exceptions?  A try block does not return the result of the block, it returns an object that contains a boolean indicating whether or not an exception was thrown and either the exception or the resultant value.  It is therefore possible to access slots on the resultant prototype to perform whatever error handling is necessary.
<blockquote>var t = try( socket.write() socket.close() )

if ( t.hasError raise( t.error ) t.result )</blockquote>
<h2>Composite Data Structures</h2>
There are lists. There is a built in function to construct them and they can contain an object of any type.  Lists are zero indexed for access operations.
<blockquote>var names = List( "Kevin", "Sam", "Quorra" )

println( names.get( 1 ) )</blockquote>
<h1>Because I have to...</h1>
<h2></h2>
<h2>Hello World</h2>
<blockquote>println( "Hello world!" )</blockquote>
<h2>Factorial</h2>
<blockquote>var fac = [n] {
<p style="padding-left: 30px;">if ( n == 0 || n == 1</p>
<p style="padding-left: 30px;">1</p>
<p style="padding-left: 30px;">n * recur( n - 1 )  )</p>
}</blockquote>
In protogrid, recursion is achieved by calling the recur function instead of the current function by name.
<h1><strong>Getting started</strong></h1>
The best way to get started is to download the executable protogrid jar and start experimenting with the REPL.  Syntax highlighting for IntelliJ IDEA and TextMate will be added to github in the very near future.
<h2><strong>Down the road</strong></h2>
<ul>
	<li>Syntax highlighting</li>
	<li>Compiled version</li>
	<li>Debugger</li>
	<li>Static typing</li>
	<li>Lazy evaluation</li>
	<li>Macros</li>
	<li>Named arguments</li>
	<li>Default values for parameters</li>
	<li>Richer Java interop</li>
	<li>De-structuring</li>
	<li>Targeting other platforms</li>
</ul>
&nbsp;

