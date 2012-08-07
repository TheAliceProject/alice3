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

package org.alice.ide.custom;

/**
 * @author Dennis Cosgrove
 */
public abstract class NumberCustomExpressionCreatorComposite extends CustomExpressionCreatorComposite<org.alice.ide.custom.components.NumberCustomExpressionCreatorView> {
	private final ErrorStatus errorStatus = this.createErrorStatus( this.createKey( "errorStatus" ) );
	private final org.alice.ide.croquet.models.numberpad.NumberModel numberModel;
	public NumberCustomExpressionCreatorComposite( java.util.UUID id, org.alice.ide.croquet.models.numberpad.NumberModel numberModel ) {
		super( id );
		this.numberModel = numberModel;
	}
	@Override
	protected GoldenRatioPolicy getGoldenRatioPolicy() {
		//return GoldenRatioPolicy.HEIGHT_LONG_SIDE;
		return null;
	}
	@Override
	protected org.alice.ide.custom.components.NumberCustomExpressionCreatorView createView() {
		return new org.alice.ide.custom.components.NumberCustomExpressionCreatorView( this );
	}
	public org.alice.ide.croquet.models.numberpad.NumberModel getNumberModel() {
		return this.numberModel;
	}
	@Override
	protected org.lgna.project.ast.Expression createValue() {
		return this.numberModel.getExpressionValue();
	}
	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		String text = this.numberModel.getExplanationIfOkButtonShouldBeDisabled();
		if( text != null ) {
			this.errorStatus.setText( text );
			return errorStatus;
		} else {
			return IS_GOOD_TO_GO_STATUS;
		}
	}
	protected abstract String getTextForPreviousExpression( org.lgna.project.ast.Expression expression );
	@Override
	protected final void initializeToPreviousExpression( org.lgna.project.ast.Expression expression ) {
		String text = this.getTextForPreviousExpression( expression );
		this.numberModel.setText( text );
		this.numberModel.selectAll();
	}
	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		super.handlePreShowDialog( step );
		this.numberModel.getTextField().requestFocusInWindow();
	}
}
