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
package edu.cmu.cs.dennisc.zoot;

/**
 * @author Dennis Cosgrove
 */
public class ZScrollPane extends javax.swing.JScrollPane {
	public enum VerticalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
		private int internal;
		private VerticalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}
	public enum HorizontalScrollbarPolicy {
		NEVER( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER ),
		AS_NEEDED( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED ),
		ALWAYS( javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
		private int internal;
		private HorizontalScrollbarPolicy( int internal ) {
			this.internal = internal;
		}
	}
	public ZScrollPane() {
	}
	public ZScrollPane( java.awt.Component view ) {
		super( view );
	}
	public ZScrollPane( java.awt.Component view, VerticalScrollbarPolicy verticalScrollbarPolicy, HorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		super( view, verticalScrollbarPolicy.internal, horizontalScrollbarPolicy.internal );
	}
	public ZScrollPane( VerticalScrollbarPolicy verticalScrollbarPolicy, HorizontalScrollbarPolicy horizontalScrollbarPolicy ) {
		super( verticalScrollbarPolicy.internal, horizontalScrollbarPolicy.internal );
	}
}
