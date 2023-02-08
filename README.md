# findnpe
manage null pointer contracts

## Introduction

FindNPE offers a solution for controlling null pointers in Java code. Thus, the risk of NPEs (NullPointerExceptions) being thrown on customer's side can be minimized. FindNPE offers annotations for a fine-grained control of which variables are allowed to be null at runtime. Since FindNPE uses static analysis of the Java program at compile time, a program's execution behavior is not affected.

FindNPEs basic rule to reduce NPEs is: If there is a possibility that an object can be null, then compilation errors are produced at places where it is accessed. This is demonstrated in the following example:

![Link14_nm](https://user-images.githubusercontent.com/7977207/217451073-97e9ebdd-3c4e-4648-87fa-d08f341b2e95.GIF)

## Getting started

After installing FindNPE, errors like the example on the Inroduction page are shown. However such NPE hazards are generally quite few. NPEs more likely happen by having methods which are assumed to return a non-null value but in fact can return null. Without preparation, also FindNPE cannot detect such inter-method NPE hazards:

![Image1_nm](https://user-images.githubusercontent.com/7977207/217451266-05e3c100-3142-4200-acc5-1f68b8d1178d.GIF)

To make visible the obviously not reported problem, the first step is to annotate the class with @NonNullByDefault (which is equivalent to annotating each method /parameter / field of the class with @NonNull):

![Link11_nm](https://user-images.githubusercontent.com/7977207/217451374-ca9feeee-9f0a-427c-b31e-0c76c68b52e6.GIF)

@NonNull for a method means, that all return statements are checked to return a non-null value, so we have a violation of this contract in methodA. Similar, it would mean for parameters and fields, that they can only be assigned a non-null value. Now, to solve the reported problem, one can annotate the method explicitely with @CanBeNull by just applying the proposed quick fix:

![Image2_nm](https://user-images.githubusercontent.com/7977207/217451447-073cbc6e-a51a-4ead-bad2-e34f18796e5d.GIF)

=>

![Link8_nm](https://user-images.githubusercontent.com/7977207/217451498-8bd85ff4-bb94-4b02-938f-37a45cf7d259.GIF)

Finally, the desired error is displayed, saying that "s" can be null when it is accessed. The programmer can fix this error like here:

![Link9_nm](https://user-images.githubusercontent.com/7977207/217451564-6fb13bfb-a2b5-4474-81fd-b94b8dbaefc4.GIF)

The reason why such errors can not be fixed automatically, is mainly because the error could also have been fixed differently by coding that methodA not returns null but e.g. an empty string. So it has to be decided whether the calling method or the called method has to be fixed. This choice depends on what the programmer "had in mind" whether the method is allowed to return null or not allowed to do so. Of course it can also be that he never thought about it and is forced to do so right now. The NPE annotations can be used to explicitely express the desired contract.

## Problems

Kinds of reported problems:

"NPE Hazard"
 * Means that the source code expression in question returns an object which can be null, but this object is subsequently accessed.
 
 "Expected NonNull value"
 * Means that the source code expression in question returns an object which can be null, but it is returned by a method / passed to a parameter / assigned to a field / variable, which is declared as @NonNull.
 
 "Expected NonNull value, but value is always null"
 * Like the previous one, but with the hint that the expression is always null

## Installation

Update Site for Eclipse 3.6:http://findnpe.kiegeland.com/3.6/update

After installation, you should restart Eclipse and make sure, that Preferences -> JDT Weaving is ENABLED.

The FindNPE annotations must be downloaded separately and linked as a normal JAR to the Java project, see Q2.

## FAQ

Q1 I get new error messages which was not there before:
* "The type %x% cannot be resolved. It is indirectly referenced from required .class files"
* FindNPE requires to evaluate annotations from super classes of a referenced class. For this reason, the superclass must be in the build path of the project. To achieve this, you have to add a dependency to the project or plugin which contains this class file.

Q2 I get the error message :
* "The import findnpe cannot be resolved"
* You try to import one of the three annotations CanBeNull, NonNull or NonNullByDefault, but they are unknown to the Java compiler. As normal Java classes, annotations must be on the build path. For this reason, download findnpe.annotations.jar, copy it to your Java project e.g. in its root folder and choose "Add to build path" from the context menu.

## Advanced

* Checking of loops: In some situations, it may be that some variables surely contain no nullpointers in the first iteration of a loop, but in the second (or third, ...) they could be null (by the logic executed in a previous iteration). Such special cases are recognized and NPE hazards reported accordingly. Nested loops are also handled, so there are really no "leaks" in detecting NPE hazards.
* Private fields: Privat class fields can be handled as local variables to some extend. So after checking a private field against NULL, it can be savely accessed.
* Overriding: when a method is overriden in a subclass, different FindNPE annotations can be declared to the method declaration, with the restriction, that they must be compatible with the FindNPE annotations on the overridden method. This follows exactly the rationale, why the return type of an overriding method is allowed to be a sub class of the return type of the overridden method (but not the other way round).
* Incremental compilation: as FindNPE is integrated into Eclipse's Java compiler (JDT), it benefits from JDT's incremental compilation capability and automatically inherits the capability, that Java source files do not need to be saved in order that NPE hazards can be checked. So NPE hazards are reported "while you type" and often can be solved by proposed quick fixes
