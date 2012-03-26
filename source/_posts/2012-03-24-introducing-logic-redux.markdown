---
layout: post
title: "Introducing logic-redux"
date: 2012-03-24 18:25
comments: false
categories: [Prolog, Logic Programming, Programming Languages]
---

Introducing logic-redux, a new logic programming language that runs on the JVM that can be used with its own syntax or via a Java API.

logic-redux can be found on github at <https://github.com/brweber2/logic-redux>

## Learning

If you are a professional programmer and you are not familiar with logic programming, then you should add it to your list of things to learn. It should probably be at, or at least very near, the front of the list as well.

One excellent resource to learn Prolog is <http://www.learnprolognow.org>

Logic programming is _not_ wonderful for writing all sorts of general applications, however it is an amazing tool for solving certain types of problems.

## Documentation

More documentation to follow, but start out with the [README](https://github.com/brweber2/logic-redux) file on github and play around with the REPL.  Don't forget about the syntax differences between logic-redux and Prolog proper! The biggest being that in logic-redux variables are always prefixed with the '@' character.

## Why?

Why use logic-redux instead of TuProlog or SWI-Prolog? logic-redux is very simple and lightweight compared to both TuProlog and SWI-Prolog.  If you need to run on the JVM then SWI-Prolog is out.  If you need a full featured Prolog that runs on the JVM then I recommend using TuProlog.  If you need a simple way to declare facts and rules so that you can query them, then I suggest you give logic-redux a try!

