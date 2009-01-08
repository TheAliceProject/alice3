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
package edu.cmu.cs.dennisc.alice.ide.editors.type;

/**
 * @author Dennis Cosgrove
 */
public class EmptyParameterPane extends edu.cmu.cs.dennisc.zoot.ZLineAxisPane {
	public EmptyParameterPane( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter ) {
		String name = parameter.getName();
		if( name != null ) {
			this.add( new edu.cmu.cs.dennisc.zoot.ZLabel( name + ": " ) );
		}
		this.add( new edu.cmu.cs.dennisc.alice.ide.editors.code.EmptyExpressionPane( parameter.getValueType() ) );
		this.setBackground( java.awt.Color.WHITE );
		this.setOpaque( true );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 4, 2, 4 ) );
	}
	@Override
	protected void paintBorder( java.awt.Graphics g ) {
		//super.paintBorder( g );
		g.setColor( java.awt.Color.LIGHT_GRAY );
		g.drawRect( 0,0,getWidth()-1, getHeight()-1 );
	}
}
