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
public class FillInExpressionListPropertyEdit extends org.lgna.croquet.edits.AbstractEdit<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> {
	private org.lgna.project.ast.Expression nextExpression;
	private org.lgna.project.ast.Expression prevExpression;

	public FillInExpressionListPropertyEdit( org.lgna.croquet.history.CompletionStep completionStep, org.lgna.project.ast.Expression prevExpression, org.lgna.project.ast.Expression nextExpression ) {
		super( completionStep );
		this.prevExpression = prevExpression;
		this.nextExpression = nextExpression;
	}

	public FillInExpressionListPropertyEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.Project project = ide.getProject();
		java.util.UUID prevExpressionId = binaryDecoder.decodeId();
		this.prevExpression = org.lgna.project.ProgramTypeUtilities.lookupNode( project, prevExpressionId );
		java.util.UUID nextExpressionId = binaryDecoder.decodeId();
		this.nextExpression = org.lgna.project.ProgramTypeUtilities.lookupNode( project, nextExpressionId );
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.prevExpression.getId() );
		binaryEncoder.encode( this.nextExpression.getId() );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.alice.ide.croquet.models.ast.cascade.ExpressionListPropertyCascade model = (org.alice.ide.croquet.models.ast.cascade.ExpressionListPropertyCascade)this.getModel();
		org.lgna.project.ast.ExpressionListProperty expressionListProperty = model.getExpressionListProperty();
		int index = model.getIndex();
		expressionListProperty.set( index, this.nextExpression );
	}

	@Override
	protected final void undoInternal() {
		org.alice.ide.croquet.models.ast.cascade.ExpressionListPropertyCascade model = (org.alice.ide.croquet.models.ast.cascade.ExpressionListPropertyCascade)this.getModel();
		org.lgna.project.ast.ExpressionListProperty expressionListProperty = model.getExpressionListProperty();
		int index = model.getIndex();
		expressionListProperty.set( index, this.prevExpression );
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "set: " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.prevExpression, org.lgna.croquet.Application.getLocale() );
		rv.append( " ===> " );
		org.lgna.project.ast.NodeUtilities.safeAppendRepr( rv, this.nextExpression, org.lgna.croquet.Application.getLocale() );
	}
}
