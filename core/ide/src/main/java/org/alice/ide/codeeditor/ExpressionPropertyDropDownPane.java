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

import org.alice.ide.ast.draganddrop.ExpressionPropertyDropSite;
import org.alice.ide.ast.draganddrop.expression.AbstractExpressionDragModel;
import org.lgna.croquet.AbstractDropReceptor;
import org.lgna.croquet.CascadeRoot;
import org.lgna.croquet.DragModel;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.views.DropDown;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TrackableShape;
import org.lgna.croquet.views.ViewController;
import org.lgna.project.ast.ArrayAccess;
import org.lgna.project.ast.AssignmentExpression;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionProperty;
import org.lgna.project.ast.InfixExpression;
import org.lgna.project.ast.LogicalComplement;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.Node;

import java.awt.Point;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropDownPane extends DropDown<CascadeRoot.InternalPopupPrepModel<Expression>> {
	private class ExpressionPropertyDropReceptor extends AbstractDropReceptor {
		@Override
		public boolean isPotentiallyAcceptingOf( DragModel dragModel ) {
			return dragModel instanceof AbstractExpressionDragModel;
		}

		@Override
		public void dragStarted( DragStep dragStep ) {
		}

		@Override
		public void dragEntered( DragStep dragStep ) {
			dragStep.getDragSource().setDropProxyLocationAndShowIfNecessary( new Point( 0, 0 ), ExpressionPropertyDropDownPane.this.getMainComponent(), ExpressionPropertyDropDownPane.this.getBounds().height, -1 );
		}

		@Override
		public DropSite dragUpdated( DragStep dragStep ) {
			return null;
		}

		@Override
		protected Triggerable dragDroppedPostRejectorCheck( DragStep dragStep ) {
			DragModel dragModel = dragStep.getModel();
			Triggerable rv;
			if( dragModel instanceof AbstractExpressionDragModel ) {
				AbstractExpressionDragModel expressionDragModel = (AbstractExpressionDragModel)dragModel;
				rv = expressionDragModel.getDropOperation( dragStep, new ExpressionPropertyDropSite( ExpressionPropertyDropDownPane.this.expressionProperty ) );
			} else {
				rv = null;
			}
			if ( rv == null ) {
				dragStep.cancelActivity();
			}
			return rv;
		}

		@Override
		public void dragExited( DragStep dragStep, boolean isDropRecipient ) {
			//			edu.cmu.cs.dennisc.croquet.DragComponent source = dragStep.getDragSource();
			//			source.hideDropProxyIfNecessary();
		}

		@Override
		public void dragStopped( DragStep dragStep ) {
		}

		@Override
		public ViewController<?, ?> getViewController() {
			return ExpressionPropertyDropDownPane.this;
		}

		@Override
		public TrackableShape getTrackableShape( DropSite potentialDropSite ) {
			return ExpressionPropertyDropDownPane.this;
		}
	}

	private final ExpressionPropertyDropReceptor dropReceptor = new ExpressionPropertyDropReceptor();
	private final ExpressionProperty expressionProperty;

	public ExpressionPropertyDropDownPane( CascadeRoot.InternalPopupPrepModel<Expression> model, SwingComponentView<?> prefixPane, SwingComponentView<?> component, ExpressionProperty expressionProperty ) {
		super( model, prefixPane, component, null );
		this.expressionProperty = expressionProperty;
	}

	public ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}

	public ExpressionPropertyDropReceptor getDropReceptor() {
		return this.dropReceptor;
	}

	@Override
	protected boolean isInactiveFeedbackDesired() {
		Expression expression = this.expressionProperty.getValue();
		if( expression != null ) {
			Node parent = expression.getParent();
			if( ( parent instanceof InfixExpression ) || ( parent instanceof LogicalComplement ) ) {
				Node grandparent = parent.getParent();
				return ( grandparent instanceof MethodInvocation ) || ( grandparent instanceof AssignmentExpression ) || ( grandparent instanceof ArrayAccess );
			}
		}
		return super.isInactiveFeedbackDesired();
	}
}
