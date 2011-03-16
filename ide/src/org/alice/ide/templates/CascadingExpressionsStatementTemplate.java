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
package org.alice.ide.templates;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadingExpressionsStatementTemplate extends StatementTemplate {
	public CascadingExpressionsStatementTemplate( edu.cmu.cs.dennisc.croquet.DragAndDropModel dragModel, Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		super( dragModel, cls );
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] getBlankExpressionTypes();
	protected String getTitleAt( int index ) {
		return null;
	}
	
	protected abstract edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );

	//todo
//	protected abstract edu.cmu.cs.dennisc.croquet.MenuModel createMenuModel( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types );
	protected edu.cmu.cs.dennisc.croquet.MenuModel createMenuModel(edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types) {
		final edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement = blockStatementIndexPair.getBlockStatement();
		final int index = blockStatementIndexPair.getIndex();
		
		if( types.length == 1 ) {
			return new org.alice.ide.croquet.models.ast.FillInSingleExpressionMenuModel( java.util.UUID.fromString( "9a67ff7b-df1f-492e-b128-721f58ea2ad1" ) ) {
				@Override
				protected edu.cmu.cs.dennisc.alice.ast.AbstractType< ?, ?, ? > getDesiredValueType() {
					return types[ 0 ];
				}
				@Override
				protected edu.cmu.cs.dennisc.croquet.Group getItemGroup() {
					return edu.cmu.cs.dennisc.alice.Project.GROUP;
				}
				public org.alice.ide.codeeditor.InsertStatementEdit createEdit( Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
					edu.cmu.cs.dennisc.alice.ast.Statement statement = CascadingExpressionsStatementTemplate.this.createStatement( (edu.cmu.cs.dennisc.alice.ast.Expression)value ); 
					return new org.alice.ide.codeeditor.InsertStatementEdit( blockStatement.statements, index, statement );
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
					return CascadingExpressionsStatementTemplate.this.getTitleAt( 0 );
				}
			};
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
				public org.alice.ide.codeeditor.InsertStatementEdit createEdit( Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
					edu.cmu.cs.dennisc.alice.ast.Statement statement = CascadingExpressionsStatementTemplate.this.createStatement( (edu.cmu.cs.dennisc.alice.ast.Expression[])value ); 
					return new org.alice.ide.codeeditor.InsertStatementEdit( blockStatement.statements, index, statement );
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
					return CascadingExpressionsStatementTemplate.this.getTitleAt( index );
				}
			};
		}
	}
	
	protected edu.cmu.cs.dennisc.croquet.CascadeOperation< edu.cmu.cs.dennisc.alice.ast.Expression > createCascadeOperation( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, final org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair, final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types ) {
//		class ExpressionBlank extends edu.cmu.cs.dennisc.croquet.CascadeBlank< edu.cmu.cs.dennisc.alice.ast.Expression > {
//			private final edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type;
//			public ExpressionBlank( edu.cmu.cs.dennisc.alice.ast.AbstractType< ?,?,? > type ) {
//				super( java.util.UUID.fromString( "4bc689cf-3381-43db-a54e-757f67db30e7" ) );
//				this.type = type;
//			}
//		}
		
		class ExpressionCascadeOperation extends edu.cmu.cs.dennisc.croquet.CascadeOperation< edu.cmu.cs.dennisc.alice.ast.Expression > {
			private final org.alice.ide.croquet.models.cascade.ExpressionBlank<?>[] expressionBlanks;
			public ExpressionCascadeOperation( ) {
				super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "2ac86fcd-f740-480e-8cfa-06501ab7bac5" ), edu.cmu.cs.dennisc.alice.ast.Expression.class );
				this.expressionBlanks = new org.alice.ide.croquet.models.cascade.ExpressionBlank<?>[ types.length ];
				for( int i=0; i<types.length; i++ ) {
					this.expressionBlanks[ i ] = org.alice.ide.croquet.models.cascade.CascadeManager.getBlankForType( types[ i ] );
					assert this.expressionBlanks[ i ] != null : types[ i ];
				}
			}

			@Override
			public org.alice.ide.croquet.models.cascade.ExpressionBlank[] getBlanks() {
				return this.expressionBlanks;
			}
			@Override
			protected edu.cmu.cs.dennisc.croquet.Edit< edu.cmu.cs.dennisc.croquet.CascadeOperation< edu.cmu.cs.dennisc.alice.ast.Expression > > createEdit( edu.cmu.cs.dennisc.alice.ast.Expression[] values ) {
				edu.cmu.cs.dennisc.alice.ast.Statement statement = CascadingExpressionsStatementTemplate.this.createStatement( values );
				return new org.alice.ide.codeeditor.InsertStatementEdit( blockStatementIndexPair.getBlockStatement().statements, blockStatementIndexPair.getIndex(), statement );
			}
		}
		return new ExpressionCascadeOperation();
	}

	@Override
	public final edu.cmu.cs.dennisc.croquet.Operation< ? > createDropOperation( edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index ) {
		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types = this.getBlankExpressionTypes();
//		edu.cmu.cs.dennisc.alice.ast.Expression[] predeterminedExpressions = org.alice.ide.IDE.getSingleton().createPredeterminedExpressionsIfAppropriate( types );
//		if( predeterminedExpressions != null ) {
//			edu.cmu.cs.dennisc.alice.ast.Statement statement = this.createStatement( predeterminedExpressions ); 
//			return new org.alice.ide.croquet.models.ast.InsertStatementActionOperation( blockStatement, index, statement );
//		} else {
//			org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair = new org.alice.ide.codeeditor.BlockStatementIndexPair( blockStatement, index );
//			return this.createMenuModel( dragAndDropContext, blockStatementIndexPair, types ).getPopupMenuOperation();
//		}
		org.alice.ide.codeeditor.BlockStatementIndexPair blockStatementIndexPair = new org.alice.ide.codeeditor.BlockStatementIndexPair( blockStatement, index );
		return this.createCascadeOperation( dragAndDropContext, blockStatementIndexPair, types );
	}
//	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] getBlankExpressionTypes();
//	protected abstract edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
//	@Override
//	public final void createStatement( final java.awt.event.MouseEvent e, final edu.cmu.cs.dennisc.alice.ast.BlockStatement block, final int index, final edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) {
//		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types = getBlankExpressionTypes();
//		if( types != null && types.length > 0 ) {
//			class ExpressionsTaskObserver implements edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > {
//				public void handleCompletion(edu.cmu.cs.dennisc.alice.ast.Expression[] expressions) {
//					if( expressions != null ) {
//						taskObserver.handleCompletion( createStatement( expressions ) );
//					} else {
//						taskObserver.handleCancelation();
//					}
//				}
//				public void handleCancelation() {
//					taskObserver.handleCancelation();
//				}
//			}
//			ExpressionsTaskObserver expressionsTaskObserver = new ExpressionsTaskObserver();
//			getIDE().getCascadeManager().promptUserForExpressions( block, index, types, false, e, expressionsTaskObserver );
////			class Worker extends org.jdesktop.swingworker.SwingWorker< edu.cmu.cs.dennisc.alice.ast.Expression[], Void > {
////				@Override
////				protected edu.cmu.cs.dennisc.alice.ast.Expression[] doInBackground() throws java.lang.Exception {
////					edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] > expressionsTaskObserver = new edu.cmu.cs.dennisc.task.BlockingTaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression[] >() {
////						@Override
////						public void run() {
////							getIDE().promptUserForExpressions( block, index, types, false, e, this );
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
////							taskObserver.handleCompletion( createStatement( expressions ) );
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
//					edu.cmu.cs.dennisc.alice.ast.Statement statement = createStatement();
//					taskObserver.handleCompletion( statement );
////				}
////			} );
//		}
//	}
}
