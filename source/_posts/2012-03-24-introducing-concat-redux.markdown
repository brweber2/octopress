---
layout: post
title: "Introducing Concat-Redux"
date: 2012-03-24 18:25
comments: true
categories: [Concatenative, Forth, Factor, Programming Language]
---
Introducing concat-redux, a new concatenative programming language that runs on the JVM!

Concat-redux can be found here: https://github.com/brweber2/concat-redux

concat-redux introduces one new piece of syntactic sugar to help with reading the language.

A typical example would look like (reverse Polish notation):

5 3 +

Another example might be:

"hello" "world" concat 

concat-redux introduces a new meaning to ':'

Any word prefixed by a colon ':' is pushed before the next period '.'

This enables the author to write the same code as:

:concat "hello" "world"
