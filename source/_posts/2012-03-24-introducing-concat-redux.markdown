---
layout: post
title: "Introducing concat-redux"
date: 2012-03-24 18:25
comments: false
categories: [concat-redux, Concatenative, Forth, Factor, Programming Language, JVM]
---

Introducing concat-redux, a new concatenative programming language that runs on the JVM!

concat-redux is conceptually similar to [Forth](http://en.wikipedia.org/wiki/Forth_\(programming_language\)) and [Factor](http://factorcode.org)

concat-redux can be found on github at <https://github.com/brweber2/concat-redux>

## New Syntax

concat-redux introduces one new piece of syntactic sugar to help with reading the language.

A typical example would look like (reverse Polish notation):

	5 3 +

Another example might be:

	"hello" "world" concat 

concat-redux introduces a new meaning to ':'

Any word prefixed by a colon ':' is pushed before the next period '.'

This enables the author to write the same code as:

	:concat "hello" "world" .

or

	"hello" :concat "world" .

This flexibility allows you to move words around so that sections of code are easier to read by adding just one simple, unambiguous rule.

## Why?

* It runs on the JVM
* It has a REPL
* It can be used to quickly prototype an idea

Give it a try!