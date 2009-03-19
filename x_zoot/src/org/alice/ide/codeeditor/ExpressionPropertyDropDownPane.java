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

import org.alice.ide.ast.AccessiblePane;
import org.alice.ide.ast.StatementListPropertyPane;
import org.alice.ide.dnd.PotentiallyDraggableComponent;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionPropertyDropDownPane extends DropDownPane implements org.alice.ide.dnd.DropReceptor {
	class FillInExpressionOperation extends zoot.AbstractActionOperation {
		public void perform( zoot.ActionContext actionContext ) {
//			getIDE().promptUserForExpression( this.getDesiredType(), this.expressionProperty.getValue(), e, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
//				public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Expression e ) {
//					ExpressionPropertyDropDownPane.this.expressionProperty.setValue( e );
//					ExpressionPropertyDropDownPane.this.revalidate();
//					ExpressionPropertyDropDownPane.this.repaint();
//					getIDE().unsetPreviousExpression();
//					getIDE().markChanged( "expression menu" );
//				}
//				public void handleCancelation() {
//					getIDE().unsetPreviousExpression();
//				}			
//			} );
		}
	}

	private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty;
	public ExpressionPropertyDropDownPane( javax.swing.JComponent component, edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, javax.swing.JComponent prefixPane ) {
		super( prefixPane, component, null );
		this.expressionProperty = expressionProperty;
		this.setLeftButtonPressOperation( new FillInExpressionOperation() );
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


	public boolean isPotentiallyAcceptingOf( org.alice.ide.dnd.DragPane source ) {
		return source.getDraggableComponent() instanceof org.alice.ide.ast.ExpressionLikeSubstance;
	}
	public void dragStarted( org.alice.ide.dnd.DragPane source, java.awt.event.MouseEvent e ) {
	}
	public void dragEntered( org.alice.ide.dnd.DragPane source, java.awt.event.MouseEvent e ) {
		source.setDropProxyLocationAndShowIfNecessary( new java.awt.Point( 0, 0 ), this.getMainComponent(), this.getBounds().height );
	}
	public void dragUpdated( org.alice.ide.dnd.DragPane source, java.awt.event.MouseEvent e ) {
	}
	public void dragDropped( final org.alice.ide.dnd.DragPane source, final java.awt.event.MouseEvent e ) {
		class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Expression, Object > {
			private org.alice.ide.dnd.DragAndDropEvent dragAndDropEvent;
			private StatementListPropertyPane statementListPropertyPane;

			public Worker() {
				this.dragAndDropEvent = new org.alice.ide.dnd.DragAndDropEvent( source, ExpressionPropertyDropDownPane.this, e );
			}
			@Override
			protected edu.cmu.cs.dennisc.alice.ast.Expression doInBackground() throws java.lang.Exception {
				org.alice.ide.dnd.DragPane source = this.dragAndDropEvent.getTypedSource();
				if( source.getDraggableComponent() instanceof AccessiblePane ) {
					AccessiblePane accessiblePane = (AccessiblePane)source.getDraggableComponent();
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
	public void dragExited( org.alice.ide.dnd.DragPane source, java.awt.event.MouseEvent e, boolean isDropRecipient ) {
		source.hideDropProxyIfNecessary();
	}
	public void dragStopped( org.alice.ide.dnd.DragPane source, java.awt.event.MouseEvent e ) {
	}
	public java.awt.Component getAWTComponent() {
		return this;
	}
}
