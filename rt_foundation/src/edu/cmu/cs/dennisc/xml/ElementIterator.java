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
package edu.cmu.cs.dennisc.xml;

/**
 * @author Dennis Cosgrove
 */
public class ElementIterator implements java.util.Iterator< org.w3c.dom.Element > {
	private org.w3c.dom.NodeList m_nodeList;
	private int m_index;

	public ElementIterator( org.w3c.dom.NodeList nodeList ) {
		m_nodeList = nodeList;
		m_index = 0;
	}
	public boolean hasNext() {
		return m_index < m_nodeList.getLength();
	}
	public org.w3c.dom.Element next() {
		return (org.w3c.dom.Element)m_nodeList.item( m_index++ );
	}
	public void remove() {
		throw new RuntimeException( "not supported" );
	}
}
