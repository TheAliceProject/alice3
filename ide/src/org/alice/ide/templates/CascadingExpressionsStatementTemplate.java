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
	public CascadingExpressionsStatementTemplate( Class< ? extends edu.cmu.cs.dennisc.alice.ast.Statement > cls ) {
		super( cls );
	}
	protected abstract edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] getBlankExpressionTypes();
	protected abstract edu.cmu.cs.dennisc.alice.ast.Statement createStatement( edu.cmu.cs.dennisc.alice.ast.Expression... expressions );
	
	private static edu.cmu.cs.dennisc.alice.ast.Expression[] createPredeterminedExpressions( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types) {
		if( types == null || types.length == 0 ) {
			return new edu.cmu.cs.dennisc.alice.ast.Expression[]{};
		} else {
			if( types.length == 1 ) {
				//todo
				return null;
			} else {
				return null;
			}
		}
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Operation< ? > createDropOperation( final edu.cmu.cs.dennisc.croquet.DragAndDropContext dragAndDropContext, final edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, final int index ) {
		final edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?>[] types = this.getBlankExpressionTypes();
		edu.cmu.cs.dennisc.alice.ast.Expression[] predeterminedExpressions = createPredeterminedExpressions( types );
		if( predeterminedExpressions != null ) {
			edu.cmu.cs.dennisc.alice.ast.Statement statement = this.createStatement( predeterminedExpressions ); 
			return new org.alice.ide.croquet.models.ast.InsertStatementActionOperation( statement, blockStatement, index );
		} else {
			if( types.length == 1 ) {
				return new org.alice.ide.croquet.models.ast.FillInSingleExpressionPopupMenuOperation( java.util.UUID.fromString( "9a67ff7b-df1f-492e-b128-721f58ea2ad1" ) ) {
					@Override
					protected edu.cmu.cs.dennisc.croquet.ActionOperation createActionOperation( edu.cmu.cs.dennisc.cascade.FillIn< ? > fillIn ) {
						return new edu.cmu.cs.dennisc.cascade.CascadingActionOperation< edu.cmu.cs.dennisc.cascade.CascadingPopupMenuOperation >( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "62505e7a-e585-4e5e-9c5e-37a580b98b97" ), this, fillIn ) {
							
						};
					}
					@Override
					public edu.cmu.cs.dennisc.croquet.Edit< ? extends edu.cmu.cs.dennisc.croquet.ActionOperation > createEdit( Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
						edu.cmu.cs.dennisc.alice.ast.Statement statement = CascadingExpressionsStatementTemplate.this.createStatement( (edu.cmu.cs.dennisc.alice.ast.Expression)value ); 
						return new org.alice.ide.codeeditor.InsertStatementEdit( blockStatement.statements, index, statement );
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
					protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.BlockStatement, java.lang.Integer > getBlockStatementAndIndex() {
						return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( blockStatement, index );
					}
					@Override
					protected java.lang.String getTitle() {
						return null;
					}
				};
			} else {
				return null;
			}
		}
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
