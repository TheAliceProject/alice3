/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.croquet.edits.ast;

/**
 * @author Dennis Cosgrove
 */
public class FillInMoreEdit extends org.lgna.croquet.edits.AbstractEdit<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> {
	private org.lgna.project.ast.Expression argumentExpression;

	public FillInMoreEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.Expression argumentExpression ) {
		super( completionStep );
		this.argumentExpression = argumentExpression;
	}

	public FillInMoreEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project = ide.getProject();
		java.util.UUID prevExpressionId = binaryDecoder.decodeId();
		this.argumentExpression = org.lgna.project.ProgramTypeUtilities.lookupNode( project, prevExpressionId );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.argumentExpression.getId() );
	}

	private org.lgna.project.ast.SimpleArgument getArgumentAt( org.lgna.project.ast.MethodInvocation methodInvocation, int index ) {
		return methodInvocation.requiredArguments.get( index );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.alice.ide.croquet.models.ast.cascade.MoreCascade model = (org.alice.ide.croquet.models.ast.cascade.MoreCascade)this.getModel();
		org.lgna.project.ast.MethodInvocation prevMethodInvocation = model.getPrevMethodInvocation();
		org.lgna.project.ast.MethodInvocation nextMethodInvocation = model.getNextMethodInvocation();

		org.lgna.project.ast.Expression instanceExpression = prevMethodInvocation.expression.getValue();
		//prevMethodInvocation.expression.setValue( null );
		nextMethodInvocation.expression.setValue( instanceExpression );
		final int N = prevMethodInvocation.requiredArguments.size();
		for( int i = 0; i < N; i++ ) {
			org.lgna.project.ast.Expression expressionI = this.getArgumentAt( prevMethodInvocation, i ).expression.getValue();
			//prevMethodInvocation.arguments.get( i ).expression.setValue( null );
			this.getArgumentAt( nextMethodInvocation, i ).expression.setValue( expressionI );
		}
		this.getArgumentAt( nextMethodInvocation, N ).expression.setValue( this.argumentExpression );
		model.getExpressionStatement().expression.setValue( nextMethodInvocation );
		//		this.getModel().updateToolTipText();
	}

	@Override
	protected final void undoInternal() {
		org.alice.ide.croquet.models.ast.cascade.MoreCascade model = (org.alice.ide.croquet.models.ast.cascade.MoreCascade)this.getModel();
		org.lgna.project.ast.MethodInvocation prevMethodInvocation = model.getPrevMethodInvocation();
		org.lgna.project.ast.MethodInvocation nextMethodInvocation = model.getNextMethodInvocation();

		org.lgna.project.ast.Expression instanceExpression = nextMethodInvocation.expression.getValue();
		nextMethodInvocation.expression.setValue( null );
		prevMethodInvocation.expression.setValue( instanceExpression );
		final int N = prevMethodInvocation.requiredArguments.size();
		for( int i = 0; i < N; i++ ) {
			org.lgna.project.ast.Expression expressionI = this.getArgumentAt( nextMethodInvocation, i ).expression.getValue();
			//nextMethodInvocation.arguments.get( i ).expression.setValue( null );
			this.getArgumentAt( prevMethodInvocation, i ).expression.setValue( expressionI );
		}
		//nextMethodInvocation.arguments.get( N ).expression.setValue( null );

		model.getExpressionStatement().expression.setValue( prevMethodInvocation );

		//		this.getModel().updateToolTipText();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		org.alice.ide.croquet.models.ast.cascade.MoreCascade model = (org.alice.ide.croquet.models.ast.cascade.MoreCascade)this.getModel();
		org.lgna.project.ast.MethodInvocation nextMethodInvocation = model.getNextMethodInvocation();
		if( nextMethodInvocation != null ) {
			rv.append( "more: " );
			org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, nextMethodInvocation.method.getValue(), org.lgna.croquet.Application.getLocale() );
			rv.append( " " );
			final int N = nextMethodInvocation.requiredArguments.size();
			org.lgna.project.ast.AbstractArgument argument = nextMethodInvocation.requiredArguments.get( N - 1 );
			org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, argument, org.lgna.croquet.Application.getLocale() );
		}
	}
}
