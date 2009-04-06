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
	private MyTypePane myTypePane = new MyTypePane();
	private org.alice.ide.initializer.ArrayInitializerPane arrayInitializerPane = new org.alice.ide.initializer.ArrayInitializerPane() {
		@Override
		protected void handleInitializerChange() {
			ArrayChooser.this.getInputPane().updateOKButton();
		}
	};
	private swing.BorderPane pane = new swing.BorderPane();
	public ArrayChooser() {
		this.pane.add( this.myTypePane, java.awt.BorderLayout.NORTH );
		this.pane.add( this.arrayInitializerPane, java.awt.BorderLayout.CENTER );
		this.arrayInitializerPane.handleTypeChange( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( Object.class ) );
	}
	public boolean isInputValid() {
		return true;
	}
	public java.awt.Component getComponent() {
		return this.pane;
	}
	public edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation getValue() {
		return (edu.cmu.cs.dennisc.alice.ast.ArrayInstanceCreation)this.arrayInitializerPane.getInitializer();
	}
}
