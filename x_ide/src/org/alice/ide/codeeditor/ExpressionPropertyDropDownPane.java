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
	public ExpressionPropertyDropDownPane( javax.swing.JComponent component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, javax.swing.JComponent prefixPane ) {
		super( prefixPane, component, null );
		this.expressionProperty = expressionProperty;
		this.setLeftButtonPressOperation( new org.alice.ide.operations.ast.FillInExpressionPropertyOperation( this.expressionProperty ) );
	}
	public ExpressionPropertyDropDownPane( javax.swing.JComponent component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		this( component, expressionProperty, null );
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
		class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Expression, Object > {
			private zoot.event.DragAndDropEvent dragAndDropEvent;
			public Worker() {
				this.dragAndDropEvent = new zoot.event.DragAndDropEvent( source, ExpressionPropertyDropDownPane.this, eSource );
			}
			@Override
			protected edu.cmu.cs.dennisc.alice.ast.Expression doInBackground() throws java.lang.Exception {
				zoot.ZDragComponent source = this.dragAndDropEvent.getTypedSource();
				if( source.getSubject() instanceof org.alice.ide.common.AccessiblePane ) {
					org.alice.ide.common.AccessiblePane accessiblePane = (org.alice.ide.common.AccessiblePane)source.getSubject();
//					try {
						edu.cmu.cs.dennisc.alice.ast.Expression expression = accessiblePane.createExpression( this.dragAndDropEvent );
						return expression;
//					} catch( Throwable t ) {
//						this.cancel( true );
//						throw new RuntimeException( t );
//					}
				} else {
					return null;
				}
			}
			@Override
			protected void done() {
				try {
					edu.cmu.cs.dennisc.alice.ast.Expression expression = this.get();
					if( expression != null ) {
						ExpressionPropertyDropDownPane.this.expressionProperty.setValue( expression );
						getIDE().markChanged( "expression dropped" );
					} else {
						//todo?
					}
				} catch( InterruptedException ie ) {
					throw new RuntimeException( ie );
				} catch( java.util.concurrent.ExecutionException ee ) {
					throw new RuntimeException( ee );
				} finally {
					source.hideDropProxyIfNecessary();
				}
			}
		}
		Worker worker = new Worker();
		worker.execute();
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
}
