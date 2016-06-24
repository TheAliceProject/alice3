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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropDownPane extends org.lgna.croquet.views.DropDown<org.lgna.croquet.CascadeRoot.InternalPopupPrepModel<org.lgna.project.ast.Expression>> {
	private class ExpressionPropertyDropReceptor extends org.lgna.croquet.AbstractDropReceptor {
		@Override
		public boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
			return dragModel instanceof org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel;
		}

		@Override
		public void dragStarted( org.lgna.croquet.history.DragStep dragStep ) {
		}

		@Override
		public void dragEntered( org.lgna.croquet.history.DragStep dragStep ) {
			dragStep.getDragSource().setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), ExpressionPropertyDropDownPane.this.getMainComponent(), ExpressionPropertyDropDownPane.this.getBounds().height, -1 );
		}

		@Override
		public org.lgna.croquet.DropSite dragUpdated( org.lgna.croquet.history.DragStep dragStep ) {
			return null;
		}

		@Override
		protected org.lgna.croquet.Model dragDroppedPostRejectorCheck( org.lgna.croquet.history.DragStep dragStep ) {
			org.lgna.croquet.DragModel dragModel = dragStep.getModel();
			org.lgna.croquet.Model rv;
			if( dragModel instanceof org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel ) {
				org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel expressionDragModel = (org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel)dragModel;
				rv = expressionDragModel.getDropModel( dragStep, new org.alice.ide.ast.draganddrop.ExpressionPropertyDropSite( ExpressionPropertyDropDownPane.this.expressionProperty ) );
			} else {
				rv = null;
			}
			if( rv != null ) {
				//pass
			} else {
				//				source.hideDropProxyIfNecessary();
				dragStep.cancelTransaction( org.lgna.croquet.triggers.NullTrigger.createUserInstance() );
			}
			return rv;
		}

		@Override
		public void dragExited( org.lgna.croquet.history.DragStep dragStep, boolean isDropRecipient ) {
			//			edu.cmu.cs.dennisc.croquet.DragComponent source = dragStep.getDragSource();
			//			source.hideDropProxyIfNecessary();
		}

		@Override
		public void dragStopped( org.lgna.croquet.history.DragStep dragStep ) {
		}

		@Override
		public org.lgna.croquet.views.ViewController<?, ?> getViewController() {
			return ExpressionPropertyDropDownPane.this;
		}

		@Override
		public org.lgna.croquet.views.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
			return ExpressionPropertyDropDownPane.this;
		}
	}

	private final ExpressionPropertyDropReceptor dropReceptor = new ExpressionPropertyDropReceptor();
	private final org.lgna.project.ast.ExpressionProperty expressionProperty;

	public ExpressionPropertyDropDownPane( org.lgna.croquet.CascadeRoot.InternalPopupPrepModel<org.lgna.project.ast.Expression> model, org.lgna.croquet.views.SwingComponentView<?> prefixPane, org.lgna.croquet.views.SwingComponentView<?> component, org.lgna.project.ast.ExpressionProperty expressionProperty ) {
		super( model, prefixPane, component, null );
		this.expressionProperty = expressionProperty;
	}

	public org.lgna.project.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}

	public ExpressionPropertyDropReceptor getDropReceptor() {
		return this.dropReceptor;
	}

	@Override
	protected boolean isInactiveFeedbackDesired() {
		org.lgna.project.ast.Expression expression = this.expressionProperty.getValue();
		if( expression != null ) {
			org.lgna.project.ast.Node parent = expression.getParent();
			if( ( parent instanceof org.lgna.project.ast.InfixExpression ) || ( parent instanceof org.lgna.project.ast.LogicalComplement ) ) {
				org.lgna.project.ast.Node grandparent = parent.getParent();
				return ( grandparent instanceof org.lgna.project.ast.MethodInvocation ) || ( grandparent instanceof org.lgna.project.ast.AssignmentExpression ) || ( grandparent instanceof org.lgna.project.ast.ArrayAccess );
			}
		}
		return super.isInactiveFeedbackDesired();
	}
}
