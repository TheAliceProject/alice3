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

package org.alice.ide.croquet.models.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class PreviousExpressionBasedFillInWithBlanks<F extends org.lgna.project.ast.Expression, B> extends ExpressionFillInWithBlanks<F, B> {
	public PreviousExpressionBasedFillInWithBlanks( java.util.UUID id, Class<B> cls, org.lgna.croquet.CascadeBlank<B>... blanks ) {
		super( id, cls, blanks );
	}

	private org.lgna.project.ast.Expression getPreviousExpression() {
		return org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().getPreviousExpression();
	}

	private org.lgna.project.ast.Expression createCopyOfPreviousExpression() {
		org.lgna.project.ast.Expression prevExpression = this.getPreviousExpression();
		if( prevExpression != null ) {
			return org.alice.ide.IDE.getActiveInstance().createCopy( prevExpression );
		} else {
			return null;
		}
	}

	private org.lgna.project.ast.Expression cleanExpression;

	@Override
	protected void markClean() {
		super.markClean();
		this.cleanExpression = this.getPreviousExpression();
	}

	@Override
	protected boolean isDirty() {
		boolean isPrevExpressionChanged = this.cleanExpression != this.getPreviousExpression();
		return super.isDirty() || isPrevExpressionChanged;
	}

	//	protected abstract boolean isInclusionDesired( org.lgna.croquet.steps.CascadeFillInStep<F,B> context, org.lgna.project.ast.Expression previousExpression );
	//	@Override
	//	public final boolean isInclusionDesired( org.lgna.croquet.steps.CascadeFillInPrepStep<F,B> context ) {
	//		org.lgna.project.ast.Expression previousExpression = this.getPreviousExpression();
	//		return super.isInclusionDesired( context ) && previousExpression != null && this.isInclusionDesired( context, previousExpression );
	//	}
	protected abstract F createValue( org.lgna.project.ast.Expression previousExpression, B[] expressions );

	@Override
	protected final F createValue( B[] expressions ) {
		return this.createValue( this.createCopyOfPreviousExpression(), expressions );
	}
}
