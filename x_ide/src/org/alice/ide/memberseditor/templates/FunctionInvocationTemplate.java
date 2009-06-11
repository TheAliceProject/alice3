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
public class FunctionInvocationTemplate extends org.alice.ide.templates.CascadingExpressionsExpressionTemplate {
	private edu.cmu.cs.dennisc.alice.ast.AbstractMethod method;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > parameterAdapter = new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice >() {
		@Override
		protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > e ) {
		}
		@Override
		protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice > e ) {
			FunctionInvocationTemplate.this.refresh();
		}
	};
	public FunctionInvocationTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method ) {
		this.method = method;
		if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodInAlice = (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
			this.setPopupOperation( new MethodPopupOperation( methodInAlice ) );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression() {
		return org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( this.method );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return this.method.getReturnType();
	}
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			((edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.method).parameters.addListPropertyListener( this.parameterAdapter );
		}
	}
	@Override
	public void removeNotify() {
		if( this.method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
			((edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)this.method).parameters.removeListPropertyListener( this.parameterAdapter );
		}
		super.removeNotify();
	}
	@Override
	protected java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > getBlankExpressionTypes( java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		return org.alice.ide.ast.NodeUtilities.getDesiredParameterValueTypes( rv, this.method );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = org.alice.ide.ast.NodeUtilities.createIncompleteMethodInvocation( method );
		org.alice.ide.ast.NodeUtilities.completeMethodInvocation( rv, expressions );
		return rv;
	}
}
