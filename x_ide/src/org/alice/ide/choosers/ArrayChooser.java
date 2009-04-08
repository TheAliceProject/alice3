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
	class MyTypePane extends org.alice.ide.createdeclarationpanes.TypePane {
		public MyTypePane() {
			super( true, false );
		}
		@Override
		protected void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
			ArrayChooser.this.arrayInitializerPane.handleTypeChange( type.getArrayType() );
		}
		@Override
		protected void handleIsArrayChange( boolean isArray ) {
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation arrayInstanceCreation = new edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation();
	private MyTypePane myTypePane = new MyTypePane();
	private org.alice.ide.initializer.ArrayInitializerPane arrayInitializerPane = new org.alice.ide.initializer.ArrayInitializerPane( arrayInstanceCreation ) {
		@Override
		protected void handleInitializerChange() {
			ArrayChooser.this.getInputPane().updateOKButton();
		}
	};
	private static final String[] LABEL_TEXTS = { "type:", "value:" };
	private java.awt.Component[] components = { this.myTypePane, this.arrayInitializerPane };
	
	public ArrayChooser() {
		this.arrayInitializerPane.handleTypeChange( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) );
	}
	public boolean isInputValid() {
		return true;
	}
	@Override
	public java.lang.String[] getLabelTexts() {
		return LABEL_TEXTS;
	}
	public java.awt.Component[] getComponents() {
		return this.components;
	}
	public edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation getValue() {
		//return (edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation)this.arrayInitializerPane.getInitializer();
		return this.arrayInstanceCreation;
	}
	
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		org.alice.ide.cascade.customfillin.CustomArrayFillIn customArrayFillIn = new org.alice.ide.cascade.customfillin.CustomArrayFillIn();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( customArrayFillIn.getValue() );
	}
}
