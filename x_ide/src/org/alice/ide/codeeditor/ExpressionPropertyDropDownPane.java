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
package org.alice.ide.codeeditor;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropDownPane extends DropDownPane implements zoot.DropReceptor {
	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
	public ExpressionPropertyDropDownPane( java.awt.Component prefixPane, java.awt.Component component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		super( prefixPane, component, null );
		this.expressionProperty = expressionProperty;
		this.setLeftButtonPressOperation( new org.alice.ide.operations.ast.FillInExpressionPropertyOperation( this.expressionProperty ) );
	}
	public edu.cmu.cs.dennisc.alice.ast.ExpressionProperty getExpressionProperty() {
		return this.expressionProperty;
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getDesiredType() {
		return this.expressionProperty.getExpressionType();
	}


	public boolean isPotentiallyAcceptingOf( zoot.ZDragComponent source ) {
		return source.getSubject() instanceof org.alice.ide.common.ExpressionLikeSubstance;
	}
	public void dragStarted( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragEntered( zoot.DragAndDropContext dragAndDropContext ) {
		zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		source.setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), this.getMainComponent(), this.getBounds().height );
	}
	public void dragUpdated( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public void dragDropped( zoot.DragAndDropContext dragAndDropContext ) {
		final zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		final java.awt.event.MouseEvent eSource = dragAndDropContext.getLatestMouseEvent();
		if( source.getSubject() instanceof org.alice.ide.common.ExpressionCreatorPane ) {
			final org.alice.ide.common.ExpressionCreatorPane expressionCreatorPane = (org.alice.ide.common.ExpressionCreatorPane)source.getSubject();
			final zoot.event.DragAndDropEvent dragAndDropEvent = new zoot.event.DragAndDropEvent( source, ExpressionPropertyDropDownPane.this, eSource );
			class DropOperation extends org.alice.ide.operations.AbstractActionOperation {
				public void perform( final zoot.ActionContext actionContext ) {
					edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression> taskObserver = new edu.cmu.cs.dennisc.task.TaskObserver<edu.cmu.cs.dennisc.alice.ast.Expression>() {
						public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
							if( expression != null ) {
								ExpressionPropertyDropDownPane.this.expressionProperty.setValue( expression );
								source.hideDropProxyIfNecessary();
							} else {
								this.handleCancelation();
							}
						}
						public void handleCancelation() {
							actionContext.cancel();
							source.hideDropProxyIfNecessary();
						}
					};
					actionContext.setTaskObserver( taskObserver );
					expressionCreatorPane.createExpression( dragAndDropEvent, ExpressionPropertyDropDownPane.this.expressionProperty, taskObserver );
				}
			}
			dragAndDropContext.perform( new DropOperation(), dragAndDropEvent, zoot.ZManager.CANCEL_IS_WORTHWHILE );
		} else {
			source.hideDropProxyIfNecessary();
		}
	}
	public void dragExited( zoot.DragAndDropContext dragAndDropContext, boolean isDropRecipient ) {
		zoot.ZDragComponent source = dragAndDropContext.getDragSource();
		source.hideDropProxyIfNecessary();
	}
	public void dragStopped( zoot.DragAndDropContext dragAndDropContext ) {
	}
	public java.awt.Component getAWTComponent() {
		return this;
	}

	@Override
	protected boolean isInactiveFeedbackDesired() {
		edu.cmu.cs.dennisc.alice.ast.Expression expression = this.expressionProperty.getValue();
		if( expression != null ) {
			edu.cmu.cs.dennisc.alice.ast.Node parent = expression.getParent();
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
				if( parent instanceof edu.cmu.cs.dennisc.alice.ast.Expression  ) {
					return parent instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation;
				} else {
					return true;
				}
			} else {
				return ( parent instanceof edu.cmu.cs.dennisc.alice.ast.InfixExpression || parent instanceof edu.cmu.cs.dennisc.alice.ast.LogicalComplementExpression ) == false;
			}
		} else {
			return true;
		}
	}
}
