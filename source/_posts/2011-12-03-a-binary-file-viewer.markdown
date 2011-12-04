---
layout: post
title: "A binary file viewer"
date: 2011-12-03 22:20
comments: false
categories: [software, clojure]
---

There are a lot of binary file viewers in the world.  A _lot_. So why one more?  First of all, because I want to be able to view more than a hex dump.  I want to be able to describe the rules of the file format and I want the application to show me semantic meaning in the output.  Surely this exists as well?  You betcha, in spades.  But it is more fun to write the code when you have some time to kill at a conference. :)

Of course, this can all be done trivially until the rules get a little more complex.  What if the fifth section of the file is only present if the third section had a particular value?  What if the length of the sixth section was the value from the fourth section?  What if our rules are so complex they require code to describe the relationships among the sections?  Enter binpv.

Yeah, yeah, yeah, so the name 'binpv' is terrible. I know, but let's take a look at binary file specification example:

{% include_code binpv/spec.clj %}

That's reasonably declarative!  Let's break it into pieces.

``` clojure
binary-protocol (ByteBasedChunker.)
```

We have a binary protocol that reads a byte at a time.  OK, so far so good.

``` clojure
(section :KEY_TOKEN_ID,            (FixedLength. 2))
```

Hmm, it looks like the file starts off with the key token id, which is a fixed length field.  The field must be two bytes, because we know we're reading two bytes at a time.

``` clojure
(section :INCLUDES_PRIVATE_KEY,    (EnumeratedValue. 1 (IncludesPrivateKey.)))
```

OK, it looks like our next section is the private key.  It is a one byte enumerated value.  That means we have a fixed set of possible values, but what could they be?  Well, here we encounter the first place we have to provide some code.

We're going to have to define a function that will take the value (a stream of 1 byte in this case) and return our value.  A Clojure prototype has been defined just for this purpose.  It looks like:

``` clojure
(defprotocol AnEnumeration
    (get-value [this match-seq]))
```

So we just have to extend the prototype.

{% include_code binpv/enum_example.clj %}

There is a fair bit of boilerplate there for converting the stream of bytes to a character so find out if it is a 'v' or a 'p'.  This can undoubtedly be simplified, but like I said before, this is what you get from rushed conference driven development.

I won't bore you going through the rest of the sections in detail, but suffice it to say that there are only a few places you have to plug in code.

* PrivateKeyPresent - extends DependentFun prototype
* PublicKeyLength - extends DependentLength prototype
* [\F \G] - the sequence that ends the section
* AllDone - extends AnEnumeration prototype

So once you have declared your binary specification and written a the logic that is custom to your format, all that there is left to do is to is parse the file and view it.

``` clojure
(def parsed (parse-binary (FileStreamWrapper. test-file) key-token-format))
```

Wait, you keep the parsed file in memory??? So this won't work with large files!?  Correct!  Maybe I should have called this 'can fit into memory binary file parser' or something like that.  Did I mention something about conference driven development yet?  So why did I choose to do this?  Laziness?  Perhaps, but we also have rules that depend on the values of previous sections.  If I went with a streaming model then that information would have been lost.  Unless there was a filter that was smart enough to know which values really mattered and had to be kept.  Maybe someday I'll add a compiler with these smarts, but for now the file has to fit into memory.

So last, but not least we have to display our parsed file.

``` clojure
(visualize-binary (take 7 (repeat (HexVisualizer.))) parsed)
```

What is going on there?  Whoa nelly.  Actually, each section can be viewed with its own visualizer.  In my sample file specification I had seven sections, so I need 7 visualizers.  Here is what the output of running our program looks like.

```
:KEY_TOKEN_ID
00000000:  4A 4B 00 00 00 00 00 00-00 00 00 00 00 00 00 00  JK......-........

:INCLUDES_PRIVATE_KEY
:unknown

:PRIVATE_KEY
nil

:PUBLIC_KEY_LENGTH
00000000:  85 4C 00 00 00 00 00 00-00 00 00 00 00 00 00 00  .L......-........

:PUBLIC_KEY
00000000:  4D 4E 4F 50 51 52 53 54-55 56 57 58 59 5A 5B 5C  MNOPQRST-UVWXYZ[\
00000010:  5D 5E 5F 60 61 62 63 64-65 66 67 68 69 6A 6B 6C  ]^_`abcd-efghijkl
00000020:  6D 6E 6F 70 71 72 73 74-75 76 77 78 79 7A 7B 7C  mnopqrst-uvwxyz{|
00000030:  7D 7E 7F C3 85 C3 87 C3-89 C3 91 C3 96 C3 9C C3  }~......-........
00000040:  A1 C3 A0 C3 A2 C3 A4 C3-A3 C3 A5 C3 A7 C3 A9 C3  ........-........
00000050:  A8 C3 AA C3 AB C3 AD C3-AC C3 AE C3 AF C3 B1 C3  ........-........
00000060:  B3 C3 B2 C3 B4 C3 B6 C3-B5 C3 BA C3 B9 C3 BB C3  ........-........
00000070:  BC E2 80 A0 C2 B0 C2 A2-C2 A3 C2 A7 E2 80 A2 C2  ........-........
00000080:  B6 C3 9F C2 AE 00 00 00-00 00 00 00 00 00 00 00  ........-........

:THROW_AWAY
00000000:  46 47 00 00 00 00 00 00-00 00 00 00 00 00 00 00  FG......-........

:THE_END
false
```

* It is pretty?  No.
* Is it robust?  No.  
* Can the code be improved?  Yes.
* Is it a solid starting point?  Yes.

Hopefully you find it a useful tool if you try to use it because with just a tiny bit of code, you can view your binary files pretty easily.

Thanks to Dave Sletten for the hex dump utility.