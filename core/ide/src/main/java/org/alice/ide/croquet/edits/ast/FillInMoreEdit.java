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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import org.alice.ide.IDE;
import org.alice.ide.croquet.models.ast.cascade.MoreCascade;
import org.lgna.croquet.Application;
import org.lgna.croquet.Cascade;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ProgramTypeUtilities;
import org.lgna.project.Project;
import org.lgna.project.ast.AbstractArgument;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.SimpleArgument;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class FillInMoreEdit extends AbstractEdit<Cascade<Expression>> {
	private Expression argumentExpression;

	public FillInMoreEdit( UserActivity userActivity, Expression argumentExpression ) {
		super( userActivity );
		this.argumentExpression = argumentExpression;
	}

	public FillInMoreEdit( BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		IDE ide = IDE.getActiveInstance();
		Project project = ide.getProject();
		UUID prevExpressionId = binaryDecoder.decodeId();
		this.argumentExpression = ProgramTypeUtilities.lookupNode( project, prevExpressionId );
	}

	@Override
	public void encode( BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.argumentExpression.getId() );
	}

	private SimpleArgument getArgumentAt( MethodInvocation methodInvocation, int index ) {
		return methodInvocation.requiredArguments.get( index );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		MoreCascade model = (MoreCascade)this.getModel();
		MethodInvocation prevMethodInvocation = model.getPrevMethodInvocation();
		MethodInvocation nextMethodInvocation = model.getNextMethodInvocation();

		Expression instanceExpression = prevMethodInvocation.expression.getValue();
		//prevMethodInvocation.expression.setValue( null );
		nextMethodInvocation.expression.setValue( instanceExpression );
		final int N = prevMethodInvocation.requiredArguments.size();
		for( int i = 0; i < N; i++ ) {
			Expression expressionI = this.getArgumentAt( prevMethodInvocation, i ).expression.getValue();
			//prevMethodInvocation.arguments.get( i ).expression.setValue( null );
			this.getArgumentAt( nextMethodInvocation, i ).expression.setValue( expressionI );
		}
		this.getArgumentAt( nextMethodInvocation, N ).expression.setValue( this.argumentExpression );
		model.getExpressionStatement().expression.setValue( nextMethodInvocation );
		//		this.getModel().updateToolTipText();
	}

	@Override
	protected final void undoInternal() {
		MoreCascade model = (MoreCascade)this.getModel();
		MethodInvocation prevMethodInvocation = model.getPrevMethodInvocation();
		MethodInvocation nextMethodInvocation = model.getNextMethodInvocation();

		Expression instanceExpression = nextMethodInvocation.expression.getValue();
		nextMethodInvocation.expression.setValue( null );
		prevMethodInvocation.expression.setValue( instanceExpression );
		final int N = prevMethodInvocation.requiredArguments.size();
		for( int i = 0; i < N; i++ ) {
			Expression expressionI = this.getArgumentAt( nextMethodInvocation, i ).expression.getValue();
			//nextMethodInvocation.arguments.get( i ).expression.setValue( null );
			this.getArgumentAt( prevMethodInvocation, i ).expression.setValue( expressionI );
		}
		//nextMethodInvocation.arguments.get( N ).expression.setValue( null );

		model.getExpressionStatement().expression.setValue( prevMethodInvocation );

		//		this.getModel().updateToolTipText();
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		MoreCascade model = (MoreCascade)this.getModel();
		MethodInvocation nextMethodInvocation = model.getNextMethodInvocation();
		if( nextMethodInvocation != null ) {
			rv.append( "more: " );
			NodeUtilities.safeAppendRepr( rv, nextMethodInvocation.method.getValue(), Application.getLocale() );
			rv.append( " " );
			final int N = nextMethodInvocation.requiredArguments.size();
			AbstractArgument argument = nextMethodInvocation.requiredArguments.get( N - 1 );
			NodeUtilities.safeAppendRepr( rv, argument, Application.getLocale() );
		}
	}
}
