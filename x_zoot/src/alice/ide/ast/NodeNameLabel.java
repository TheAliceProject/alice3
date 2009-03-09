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
package alice.ide.ast;

/**
 * @author Dennis Cosgrove
 */
public class NodeNameLabel extends Label {
	private edu.cmu.cs.dennisc.alice.ast.Node node;

	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			NodeNameLabel.this.updateText();
		}
	}

	private NamePropertyAdapter namePropertyAdapter = null;

	public NodeNameLabel( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		this.node = node;
		this.updateText();
		edu.cmu.cs.dennisc.property.StringProperty nameProperty = null;
		if( this.node instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.node;
			nameProperty = field.name;
		} else if( this.node instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.node;
			nameProperty = method.name;
		} else if( this.node instanceof edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter = (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice)this.node;
			nameProperty = parameter.name;
		}
		if( nameProperty != null ) {
			this.namePropertyAdapter = new NamePropertyAdapter();
			nameProperty.addPropertyListener( this.namePropertyAdapter );
		}
//		if( this.node instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField ) {
//			//pass
//		} else {
//			this.scaleFont( 1.25f );
//		}
	}
	protected edu.cmu.cs.dennisc.alice.ast.Node getNode() {
		return this.node;
	}
	protected String getTextForNullName() {
		return "null";
	}
	private void updateText() {
		String text;
		if( this.node != null ) {
			text = this.node.getName();
		} else {
			text = null;
		}
		if( text != null ) {
			//pass
		} else {
			text = this.getTextForNullName();
		}
		this.setText( text );
		this.repaint();
	}
}
