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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class MenuFillIn< E > extends FillIn< E > {
	private javax.swing.JLabel label = new javax.swing.JLabel(); 
	public MenuFillIn( String title ) {
		this.label.setText( title );
	}
	protected abstract void addChildrenToBlank( Blank blank );
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return this.label;
	}
	@Override
	final protected void addChildren() {
		this.addChild( new Blank() {
			@Override
			protected void addChildren() {
				MenuFillIn.this.addChildrenToBlank( this );
			}	
		} );
	}
	@Override
	final public E getValue() {
		return (E)((Blank)this.getChildren().get( 0 )).getSelectedFillIn().getValue();
	}
}
