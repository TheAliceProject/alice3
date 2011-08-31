/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.operations.ast;

import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.UserParameter;

/**
 * @author Dennis Cosgrove
 */
public abstract class ShiftParameterOperation extends AbstractCodeParameterOperation {
	public ShiftParameterOperation( java.util.UUID individualId, NodeListProperty< UserParameter > parametersProperty, UserParameter parameter ) {
		super( individualId, parametersProperty, parameter );
	}
	protected abstract boolean isAppropriate( int index, int n );
	protected abstract int getIndexA();
	public boolean isIndexAppropriate() {
		return this.isAppropriate( this.getIndex(), this.getParameterCount() );
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		final org.lgna.project.ast.UserMethod method = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( this.getCode(), org.lgna.project.ast.UserMethod.class );
		final int aIndex = this.getIndexA();
		final int bIndex = aIndex + 1;
		if( method != null ) {
			step.commitAndInvokeDo(new org.alice.ide.ToDoEdit( step ) {
				@Override
				protected final void doOrRedoInternal( boolean isDo ) {
					swap( method, aIndex, bIndex );
				}
				@Override
				protected final void undoInternal() {
					swap( method, bIndex, aIndex );
				}
				@Override
				protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
					rv.append( "Parameter" );
					rv.append( ShiftParameterOperation.this.getName() );
					return rv;
				}
			});
		} else {
			throw new RuntimeException();
		}
	}
	private void swap( org.lgna.project.ast.UserMethod method, int aIndex, int bIndex ) {
		java.util.List< org.lgna.project.ast.MethodInvocation > methodInvocations = org.alice.ide.IDE.getActiveInstance().getMethodInvocations( method );
		org.lgna.project.ast.UserParameter aParam = method.parameters.get( aIndex );
		org.lgna.project.ast.UserParameter bParam = method.parameters.get( bIndex );
		method.parameters.set( aIndex, bParam, aParam );
		for( org.lgna.project.ast.MethodInvocation methodInvocation : methodInvocations ) {
			org.lgna.project.ast.Argument aArg = methodInvocation.arguments.get( aIndex );
			org.lgna.project.ast.Argument bArg = methodInvocation.arguments.get( bIndex );
			assert aArg.parameter.getValue() == aParam;
			assert bArg.parameter.getValue() == bParam;
			methodInvocation.arguments.set( aIndex, bArg, aArg );
		}
	}
}
