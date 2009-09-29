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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public abstract class TypedDeclarationPane extends edu.cmu.cs.dennisc.croquet.LineAxisPane  {
	public TypedDeclarationPane( java.awt.Component... components ) {
		super( components );
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
		this.setForeground( java.awt.Color.GRAY );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		g.drawRect( 0, 0, this.getWidth()-1, this.getHeight()-1);
	}
}
