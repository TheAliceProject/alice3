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
package org.alice.ide.memberseditor.templates;

/**
 * @author Dennis Cosgrove
 */
public class ArrayLengthTemplate extends org.alice.ide.templates.CascadingExpressionsExpressionTemplate {
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
	public ArrayLengthTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( new edu.cmu.cs.dennisc.alice.ast.ArrayLength( org.alice.ide.ast.NodeUtilities.createIncompleteFieldAccess( field ) ) );
		this.field = field;
		if( this.field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.field;
			this.setPopupOperation( new FieldPopupOperation( fieldInAlice ) );
		}
	}
	@Override
	protected java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > getBlankExpressionTypes( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		return rv;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = new edu.cmu.cs.dennisc.alice.ast.FieldAccess();
		fieldAccess.expression.setValue( getIDE().createInstanceExpression() );
		fieldAccess.field.setValue( this.field );
		edu.cmu.cs.dennisc.alice.ast.ArrayLength rv = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( fieldAccess );
		return rv;
	}
}
