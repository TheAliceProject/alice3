/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.pattern;

public class IsInstanceCrawler< E > implements edu.cmu.cs.dennisc.pattern.Crawler {
	private Class<E> cls;
	private java.util.List< E > list = new java.util.LinkedList< E >();
	public IsInstanceCrawler( Class<E> cls ) {
		this.cls = cls;
	}
	public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
		if( this.cls.isAssignableFrom( crawlable.getClass() ) ) {
			this.list.add( (E)crawlable );
		}
	}
	public java.util.List<E> getList() {
		return this.list;
	}
}
