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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public class DeclareMethodParameterOperation extends AbstractCodeOperation {
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method;

	public DeclareMethodParameterOperation( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method ) {
		this.method = method;
		this.putValue( javax.swing.Action.NAME, "Add Parameter..." );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.CodeDeclaredInAlice getCode() {
		return this.method;
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		org.alice.ide.declarationpanes.CreateMethodParameterPane createMethodParameterPane = new org.alice.ide.declarationpanes.CreateMethodParameterPane( method, this.getIDE().getMethodInvocations( method ) );
		final edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter = createMethodParameterPane.showInJDialog( getIDE() );
		if( parameter != null ) {
			final int index = method.parameters.size();
			final java.util.Map< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument > map = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.MethodInvocation, edu.cmu.cs.dennisc.alice.ast.Argument >();
			actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					org.alice.ide.ast.NodeUtilities.addParameter( map, method, parameter, index, getIDE().getMethodInvocations( method ) );
				}
				@Override
				public void undo() {
					org.alice.ide.ast.NodeUtilities.removeParameter( map, method, parameter, index, getIDE().getMethodInvocations( method ) );
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "declare:" );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, parameter, locale);
					return rv;
				}
			} );
		} else {
			actionContext.cancel();
		}
	}
}
