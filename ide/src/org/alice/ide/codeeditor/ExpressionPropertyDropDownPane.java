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
package org.alice.ide.codeeditor;

import org.alice.ide.operations.ast.AbstractExpressionPropertyActionOperation;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropDownPane extends DropDownPane implements edu.cmu.cs.dennisc.zoot.DropReceptor {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
	public ExpressionPropertyDropDownPane( edu.cmu.cs.dennisc.croquet.KComponent< ? > prefixPane, edu.cmu.cs.dennisc.croquet.KComponent< ? > component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, edu.cmu.cs.dennisc.alice.ast.AbstractType desiredValueType ) {
		super( prefixPane, component, null );
		this.expressionProperty = expressionProperty;
		this.setLeftButtonPressOperation( new org.alice.ide.operations.ast.FillInExpressionPropertyActionOperation( this.expressionProperty, desiredValueType ) );
	}
	@Deprecated
	public ExpressionPropertyDropDownPane( edu.cmu.cs.dennisc.croquet.KComponent< ? > prefixPane, edu.cmu.cs.dennisc.croquet.KComponent< ? > component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		this( prefixPane, component, expressionProperty, null );
	}
	public edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}

	@Override
	protected int getInsetLeft() {
		int rv = super.getInsetLeft();
		if( this.getIDE().isExpressionTypeFeedbackDesired() ) {
			//pass
		} else {
			java.awt.Component mainComponent = this.getMainComponent();
			if( mainComponent instanceof org.alice.ide.common.ExpressionPropertyPane ) {
				org.alice.ide.common.ExpressionPropertyPane expressionPropertyPane = (org.alice.ide.common.ExpressionPropertyPane)mainComponent;
				if( expressionPropertyPane.getComponentCount()==1 ) {
					java.awt.Component component0 = expressionPropertyPane.getComponent( 0 );
					if( component0 instanceof org.alice.ide.common.InstancePropertyPane ) {
						//org.alice.ide.common.InstancePropertyPane instancePropertyPane = (org.alice.ide.common.InstancePropertyPane)component0;
						rv += 2;
					}
				}
			}
		}
		return rv;	
	}


	public boolean isPotentiallyAcceptingOf( edu.cmu.cs.dennisc.zoot.ZDragComponent source ) {
		return source.getSubject() instanceof org.alice.ide.common.ExpressionLikeSubstance;
	}
	public void dragStarted( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragEntered( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
//		zoot.ZDragComponent source = dragAndDropContext.getDragSource();
//		source.setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), this.getMainComponent(), this.getBounds().height );
	}
	public void dragUpdated( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragDropped( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
		final edu.cmu.cs.dennisc.zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		final java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		if( source.getSubject() instanceof org.alice.ide.common.ExpressionCreatorPane ) {
			final org.alice.ide.common.ExpressionCreatorPane expressionCreatorPane = (org.alice.ide.common.ExpressionCreatorPane)source.getSubject();
			final edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent dragAndDropEvent = new edu.cmu.cs.dennisc.zoot.event.DragAndDropEvent( source, ExpressionPropertyDropDownPane.this, eSource );
			class DropOperation extends AbstractExpressionPropertyActionOperation {
				public DropOperation() {
					super( java.util.UUID.fromString( "43bbcede-3da7-4597-a093-9727e5b63f29" ), ExpressionPropertyDropDownPane.this.expressionProperty );
				}
				@Override
				protected void initializeInternal(edu.cmu.cs.dennisc.zoot.Context<? extends edu.cmu.cs.dennisc.zoot.Operation> context, edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver, edu.cmu.cs.dennisc.alice.ast.Expression prevExpression) {
					expressionCreatorPane.createExpression( dragAndDropEvent, this.getExpressionProperty(), taskObserver );
				}
			}
			dragAndDropContext.perform( new DropOperation(), dragAndDropEvent, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE );
		} else {
			source.hideDropProxyIfNecessary();
		}
	}
	public void dragExited( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext, boolean isDropRecipient ) {
		edu.cmu.cs.dennisc.zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		source.hideDropProxyIfNecessary();
	}
	public void dragStopped( edu.cmu.cs.dennisc.zoot.DragAndDropContext dragAndDropContext ) {
	}
	public java.awt.Component getAWTComponent() {
		return this;
	}

	@Override
	protected boolean isInactiveFeedbackDesired() {
		edu.cmu.cs.dennisc.alice.ast.Expression expression = this.expressionProperty.getValue();
		if( expression != null ) {
			edu.cmu.cs.dennisc.alice.ast.Node parent = expression.getParent();
			if( parent instanceof edu.cmu.cs.dennisc.alice.ast.InfixExpression || parent instanceof edu.cmu.cs.dennisc.alice.ast.LogicalComplement ) { 
				edu.cmu.cs.dennisc.alice.ast.Node grandparent = parent.getParent();
				return grandparent instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation || grandparent instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression || grandparent instanceof edu.cmu.cs.dennisc.alice.ast.ArrayAccess; 
			}
		}
		return true;
	}
}
