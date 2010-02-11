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
package org.alice.ide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class ArrayChooser extends AbstractChooser< edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation > {
	private org.alice.ide.initializer.BogusNode bogusNode = new org.alice.ide.initializer.BogusNode( null, false );
	private org.alice.ide.createdeclarationpanes.TypePane typePane;
	private org.alice.ide.initializer.ArrayInitializerPane arrayInitializerPane;
	private static final String[] LABEL_TEXTS = { "type:", "value:" };
	private java.awt.Component[] components;
	
	public ArrayChooser() {
		bogusNode.isArray.setValue( true );
		this.typePane = new org.alice.ide.createdeclarationpanes.TypePane( bogusNode.componentType, bogusNode.isArray, false );
		this.arrayInitializerPane = new org.alice.ide.initializer.ArrayInitializerPane( bogusNode.arrayExpressions );
		this.components = new java.awt.Component[] { this.typePane, this.arrayInitializerPane };
		bogusNode.componentType.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
			public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			}
			public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				ArrayChooser.this.arrayInitializerPane.handleTypeChange( bogusNode.getType() );
				ArrayChooser.this.handleChanged();
			}
		} );
		bogusNode.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
			@Override
			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
			}
			@Override
			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
				ArrayChooser.this.handleChanged();
			}
		} );
		edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation arrayInstanceCreation = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( this.getPreviousExpression(), edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation.class );
		if( arrayInstanceCreation != null ) {
			//typePane.setAndLockType( arrayInstanceCreation.arrayType.getValue() );
			edu.cmu.cs.dennisc.alice.ast.AbstractType type = arrayInstanceCreation.arrayType.getValue().getComponentType();
			bogusNode.componentType.setValue( type );
			for( edu.cmu.cs.dennisc.alice.ast.Expression expression : arrayInstanceCreation.expressions ) {
				bogusNode.arrayExpressions.add( expression );
			}
			typePane.disableComboBox();
		}
	}
	
	private void handleChanged() {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				ArrayChooser.this.getInputPane().updateOKButton();
			}
		} );
		
	}
	public String getTitleDefault() {
		return "Enter Custom Array";
	}
	
	public boolean isInputValid() {
		return this.typePane.getValueType() != null;
	}
	@Override
	public String[] getLabelTexts() {
		return LABEL_TEXTS;
	}
	@Override
	public java.awt.Component[] getComponents() {
		return this.components;
	}
	public edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation getValue() {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.Expression > expressions = this.bogusNode.arrayExpressions.getValue();
		return org.alice.ide.ast.NodeUtilities.createArrayInstanceCreation( this.bogusNode.getType(), expressions );
	}
	
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		org.alice.ide.cascade.customfillin.CustomArrayFillIn customArrayFillIn = new org.alice.ide.cascade.customfillin.CustomArrayFillIn();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( customArrayFillIn.getValue() );
	}
}
