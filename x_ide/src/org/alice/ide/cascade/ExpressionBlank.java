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
package org.alice.ide.cascade;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionBlank extends edu.cmu.cs.dennisc.cascade.Blank {
	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private boolean isArrayLengthDesired;
	public ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this( type, false );
	}
	public ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType type, boolean isArrayLengthDesired ) {
		this.type = type;
		this.isArrayLengthDesired = isArrayLengthDesired;
	}
	@Override
	protected void addChildren() {
		if( this.isArrayLengthDesired ) {
			this.addFillIn( new edu.cmu.cs.dennisc.cascade.FillIn() {
				@Override
				public Object getValue() {
					return null;
				}
				@Override
				public Object getTransientValue() {
					return null;
				};
				@Override
				protected void addChildren() {
				}
				@Override
				protected javax.swing.JComponent getMenuProxy() {
					return edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( "length" );
				}
			} );
			this.addSeparator();
		}
		org.alice.ide.IDE.getSingleton().addFillIns( this, this.type );
	}
}
