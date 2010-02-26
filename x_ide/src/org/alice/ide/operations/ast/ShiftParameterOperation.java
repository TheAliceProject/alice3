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

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public abstract class ShiftParameterOperation extends AbstractCodeParameterOperation {
	public ShiftParameterOperation( NodeListProperty< ParameterDeclaredInAlice > parametersProperty, ParameterDeclaredInAlice parameter ) {
		super( parametersProperty, parameter );
	}
	protected abstract boolean isAppropriate( int index, int n );
	protected abstract int getIndexA();
	public boolean isIndexAppropriate() {
		return this.isAppropriate( this.getIndex(), this.getParameterCount() );
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( this.getCode(), edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
		final int aIndex = this.getIndexA();
		final int bIndex = aIndex + 1;
		if( method != null ) {
			actionContext.commitAndInvokeDo(new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					swap( method, aIndex, bIndex );
				}
				@Override
				public void undo() {
					swap( method, bIndex, aIndex );
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "Parameter" );
					rv.append( getActionForConfiguringSwing().getValue( javax.swing.Action.NAME ) );
					return rv;
				}
			});
		} else {
			throw new RuntimeException();
		}
	}
	private void swap( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice method, int aIndex, int bIndex ) {
		java.util.List< edu.cmu.cs.dennisc.alice.ast.MethodInvocation > methodInvocations = this.getIDE().getMethodInvocations( method );
		edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice aParam = method.parameters.get( aIndex );
		edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice bParam = method.parameters.get( bIndex );
		method.parameters.set( aIndex, bParam, aParam );
		for( edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation : methodInvocations ) {
			edu.cmu.cs.dennisc.alice.ast.Argument aArg = methodInvocation.arguments.get( aIndex );
			edu.cmu.cs.dennisc.alice.ast.Argument bArg = methodInvocation.arguments.get( bIndex );
			assert aArg.parameter.getValue() == aParam;
			assert bArg.parameter.getValue() == bParam;
			methodInvocation.arguments.set( aIndex, bArg, aArg );
		}
	}
}
