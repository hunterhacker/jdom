

		JDK1.2 Collections API's for JDK1.1 Environments.
		-------------------------------------------------

		Note: this version is based upon the final (FCS) version
		of the Collection APIs from Java2 (1.2.0) and is the final
		version of this package that corresponds to those Java2
		APIs.

	1. Introduction.
	----------------

	As part of the Java Development Kit Software Version 1.2 (java 2)
	a significant number of new API's have been added to provide a richer
	set of abstract data types for modelling "collections" of Objects.

	As part of the InfoBus 1.1 specification effort we at Java Software
	identified a need for these API's for use in the standard exchange
	of DataItem's between InfoBus Producers and Consumers. But since 
	Infobus 1.1 is targeted at a JDK1.1 base platform we could not
	use these API's unchanged from JDK1.2.

	In particular these API's could not be distributed in their 1.2
	package (java.util) since all the popular browsers prohibit the
	downloading of classes packaged in either the java. or sun. 
	packahe hierarchies, and since it is not possible to re-release the
	JDK1.1 with these classes in their current JDK1.2 package, the only
	alternative was to re-package them for use in a standard JDK1.1.x
	environment.

	*********************************************************************
	*1.1 Applications are free to use these APIs' in accordance with the*
	*attached license. However 1.2 (Java2) based Applications are       *
	*required to use the java.util.* types in order to be compliant.    *
	*********************************************************************

	2. Contents.
	------------

	This package contants the following classes/interfaces from JDK1.2:

	java.util.AbstractCollection
	java.util.AbstractList
	java.util.AbstractMap
	java.util.AbstractSequentialList
	java.util.AbstractSet
	java.util.ArrayList
	java.util.Arrays
	java.util.Collection
	java.util.Collections
	java.util.Comparator
	java.util.ConcurrentModificationException
	java.util.HashMap
	java.util.HashSet
	java.util.Hashtable
	java.util.Iterator
	java.util.LinkedList
	java.util.List
	java.util.ListIterator
	java.util.Map
	java.util.NoSuchElementException
	java.util.Random
	java.util.Set
	java.util.SortedMap
	java.util.SortedSet
	java.util.TreeMap
	java.util.TreeSet
	java.util.Vector

	java.lang.Comparable
	java.lang.UnsupportedOperationException

	3. Repackaging.
	---------------

	This package was mechanically constructed from the JDK1.2 source for
	the Collections API's using a sed script to substitute the new 
	package name (com.sun.java.util.collections) for the existing JDK1.2
	package name(s) (java.util and java.lang). Therefoe they are bug for 
	bug compatible!!!

	4. Installation/Use.
	--------------------

	Application wishing to use the Collections API's should add the
	collections.zip archive to their CLASSPATH. In addition, Applets or
	Applications that are packaged as jar files, or in web pages should
	reference or include the collections.zip archive in their packaging 
	or CODEBASE to ensure that the implementation is available upon
	download into the browser environment.


	5. Compatibility with JDK1.2.
	-----------------------------

	Since these classes are direct copies of their 1.2 counterparts with
	the package names changed, the API's are 100% identical with the one
	significant exception that since they are in different packages they
	are distinct Java types and cannot be cast/assigned between each
	other.

	However source code using the 1.1 API's should be easily (mechanically)
	ported to use the JDK1.2 counterparts if required.

	6. Licensing.
	-------------

	See associated license for details.


	Joshua Bloch,
	Larry  Cable.

	Java Software, January 1999.
