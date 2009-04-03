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
package org.alice.stageide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class PortionChooser extends org.alice.ide.choosers.AbstractChooser< org.alice.apis.moveandturn.Portion > {
	private javax.swing.JSlider slider = new javax.swing.JSlider( 0, 100, 100 );
	public PortionChooser() {
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		//todo: handle previous expression
	}
	@Override
	public void setInputPane( final zoot.ZInputPane< ? > inputPane ) {
		super.setInputPane( inputPane );
		this.slider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				inputPane.updateOKButton();
			}
		} );
	}
	public java.awt.Component getComponent() {
		return this.slider;
	}
	public org.alice.apis.moveandturn.Portion getValue() {
		double value = this.slider.getValue() / 100.0;
		return new org.alice.apis.moveandturn.Portion( value );
	}
	public boolean isInputValid() {
		return true;
	}
}
