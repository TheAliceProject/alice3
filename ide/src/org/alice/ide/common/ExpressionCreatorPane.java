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
package org.alice.ide.common;

import org.alice.ide.templates.CascadingExpressionsStatementTemplate;

/**
 * @author Dennis Cosgrove
 */
public abstract class ExpressionCreatorPane extends org.alice.ide.common.ExpressionLikeSubstance {
	@Override
	public void setActive( boolean isActive ) {
		super.setActive( isActive );
		if( isActive ) {
			getIDE().showStencilOver( this, getExpressionType() );
		} else {
			getIDE().hideStencil();
		}
	}
	
	@Override
	protected boolean isAlphaDesiredWhenOverDropReceptor() {
		return true;
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] getBlankExpressionTypes() {
		return null;
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
	protected String getTitleAt( int index ) {
		return null;
	}
	
	public edu.cmu.cs.dennisc.croquet.Operation< ? > createDropOperation( final edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty ) {
		final edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = null;
		final int index = -1;
		
		
		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types = this.getBlankExpressionTypes();
		edu.cmu.cs.dennisc.alice.ast.Expression[] predeterminedExpressions = org.alice.ide.IDE.getSingleton().createPredeterminedExpressionsIfAppropriate( types );
		if( predeterminedExpressions != null ) {
			edu.cmu.cs.dennisc.alice.ast.Expression expression = this.createExpression( predeterminedExpressions ); 
			//return new org.alice.ide.croquet.models.ast.SetExpressionPropertyActionOperation( expressionProperty, expression );
			return null;
		} else {
			if( types.length == 1 ) {
				return new org.alice.ide.croquet.models.ast.FillInSingleExpressionMenuModel( java.util.UUID.fromString( "9a67ff7b-df1f-492e-b128-721f58ea2ad1" ) ) {
					public edu.cmu.cs.dennisc.cascade.CascadingEdit< ? > createEdit( Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
						edu.cmu.cs.dennisc.alice.ast.Expression expression = ExpressionCreatorPane.this.createExpression( (edu.cmu.cs.dennisc.alice.ast.Expression)value ); 
						//return new org.alice.ide.croquet.edits.ast.SetExpressionPropertyEdit( expressionProperty, expression );
						return null;
					}
					@Override
					protected edu.cmu.cs.dennisc.croquet.Group getItemGroup() {
						return edu.cmu.cs.dennisc.alice.Project.GROUP;
					}
					@Override
					protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getDesiredValueType() {
						return types[ 0 ];
					}
					@Override
					public edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
						return null;
					}
					@Override
					protected org.alice.ide.codeeditor.BlockStatementIndexPair getBlockStatementIndexPair() {
						return new org.alice.ide.codeeditor.BlockStatementIndexPair( blockStatement, index );
					}
					@Override
					protected String getTitle() {
						return ExpressionCreatorPane.this.getTitleAt( 0 );
					}
				}.getPopupMenuOperation();
			} else {
				return new org.alice.ide.croquet.models.ast.FillInExpressionsMenuModel( java.util.UUID.fromString( "8fc93b84-f8f6-4280-ba3b-00541a8212f2" ) ) {
					@Override
					protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? >[] getDesiredValueTypes() {
						return types;
					}
					@Override
					protected edu.cmu.cs.dennisc.croquet.Group getItemGroup() {
						return edu.cmu.cs.dennisc.alice.Project.GROUP;
					}
					public edu.cmu.cs.dennisc.cascade.CascadingEdit< ? > createEdit( Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
						edu.cmu.cs.dennisc.alice.ast.Expression expression = ExpressionCreatorPane.this.createExpression( (edu.cmu.cs.dennisc.alice.ast.Expression)value ); 
						//return new org.alice.ide.croquet.edits.ast.SetExpressionPropertyEdit( expressionProperty, expression );
						return null;
					}
					@Override
					public edu.cmu.cs.dennisc.alice.ast.Expression getPreviousExpression() {
						return null;
					}
					@Override
					protected org.alice.ide.codeeditor.BlockStatementIndexPair getBlockStatementIndexPair() {
						return new org.alice.ide.codeeditor.BlockStatementIndexPair( blockStatement, index );
					}
					@Override
					protected String getTitleAt( int index ) {
						return ExpressionCreatorPane.this.getTitleAt( index );
					}
				}.getPopupMenuOperation();
			}
		}
	}

	
////	@Override
//	public final void createExpression( final edu.cmu.cs.dennisc.croquet.ModelContext context, final edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, final edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression > taskObserver ) {
//		final java.util.List< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> > types = getBlankExpressionTypes( new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> >() );
//		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> thisType = this.getExpressionType();
//		edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> propertyType = expressionProperty.getExpressionType();
//		
//		final boolean[] accessible = { false, false };
//		if( propertyType.isAssignableFrom( thisType ) ) {
//			//pass
//		} else {
//			if( thisType.isArray() ) {
//				if( propertyType.isAssignableFrom( thisType.getComponentType() ) ) {
//					types.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE );
//					accessible[ 0 ] = true;
//				}
//				for( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava integerType : edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_TYPES ) {
//					if( propertyType == integerType ) {
//						accessible[ 1 ] = true;
//					}
//				}
//			}
//		}
//		if( types.size() > 0 ) {
//			class ExpressionsTaskObserver implements edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > {
//				public void handleCompletion(edu.cmu.cs.dennisc.alice.ast.Expression[] expressions) {
//					if( expressions != null ) {
//						edu.cmu.cs.dennisc.alice.ast.Expression expression = createExpression( expressions );
//						if( accessible[ 0 ] ) {
//							edu.cmu.cs.dennisc.alice.ast.Expression lastExpression = expressions[ expressions.length-1 ];
//							if( accessible[ 1 ] && lastExpression == null ) {
//								expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
//							} else {
//								expression = new edu.cmu.cs.dennisc.alice.ast.ArrayAccess( thisType, expression, lastExpression );
//							}
//						} else if( accessible[ 1 ] ) {
//							expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
//						}
//						taskObserver.handleCompletion( expression );
//					} else {
//						taskObserver.handleCancelation();
//					}
//				}
//				public void handleCancelation() {
//					taskObserver.handleCancelation();
//				}
//			}
//			ExpressionsTaskObserver expressionsTaskObserver = new ExpressionsTaskObserver();
//			edu.cmu.cs.dennisc.alice.ast.BlockStatement parentStatement = null;
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate parentStatement" );
//			edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? >[] array = new edu.cmu.cs.dennisc.alice.ast.AbstractType[ types.size() ];
//			types.toArray( array );
//			
//			
//			getIDE().getCascadeManager().promptUserForExpressions( parentStatement, -1, array, accessible[ 1 ], (java.awt.event.MouseEvent)context.getAwtEvent(), expressionsTaskObserver );
//			
////			class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Expression[], Void > {
////				@Override
////				protected edu.cmu.cs.dennisc.alice.ast.Expression[] doInBackground() throws java.lang.Exception {
////					edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > expressionsTaskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
////						@Override
////						public void run() {
////							edu.cmu.cs.dennisc.alice.ast.BlockStatement parentStatement = null;
////							edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate parentStatement" );
////							getIDE().promptUserForExpressions( parentStatement, -1, edu.cmu.cs.dennisc.java.util.CollectionUtilities.createArray( types, edu.cmu.cs.dennisc.alice.ast.AbstractType.class ), accessible[ 1 ], (java.awt.event.MouseEvent)context.getAwtEvent(), this );
////						}
////					};
////					return expressionsTaskObserver.getResult();
////				}
////				@Override
////				protected void done() {
////					edu.cmu.cs.dennisc.alice.ast.Expression[] expressions = null;
////					try {
////						expressions = this.get();
////					} catch( InterruptedException ie ) {
////						throw new RuntimeException( ie );
////					} catch( java.util.concurrent.ExecutionException ee ) {
////						throw new RuntimeException( ee );
////					} finally {
////						if( expressions != null ) {
////							edu.cmu.cs.dennisc.alice.ast.Expression expression = createExpression( expressions );
////							if( accessible[ 0 ] ) {
////								edu.cmu.cs.dennisc.alice.ast.Expression lastExpression = expressions[ expressions.length-1 ];
////								if( accessible[ 1 ] && lastExpression == null ) {
////									expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
////								} else {
////									expression = new edu.cmu.cs.dennisc.alice.ast.ArrayAccess( thisType, expression, lastExpression );
////								}
////							} else if( accessible[ 1 ] ) {
////								expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
////							}
////							taskObserver.handleCompletion( expression );
////						} else {
////							taskObserver.handleCancelation();
////						}
////					}
////				}
////			}
////			Worker worker = new Worker();
////			worker.execute();
//		} else {
////			javax.swing.SwingUtilities.invokeLater( new Runnable() {
////				public void run() {
//					edu.cmu.cs.dennisc.alice.ast.Expression expression = createExpression();
//					if( accessible[ 1 ] ) {
//						expression = new edu.cmu.cs.dennisc.alice.ast.ArrayLength( expression );
//					}
//					taskObserver.handleCompletion( expression );
////				}
////			} );
//		}
//	}
	@Override
	protected boolean isKnurlDesired() {
		return true;
	}
}
