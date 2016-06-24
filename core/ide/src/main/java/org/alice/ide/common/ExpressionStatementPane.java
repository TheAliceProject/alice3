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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class ExpressionStatementPane extends AbstractStatementPane {
	private edu.cmu.cs.dennisc.property.event.PropertyListener refreshAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		@Override
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}

		@Override
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					ExpressionStatementPane.this.refresh();
				}
			} );
		}
	};

	public ExpressionStatementPane( org.lgna.croquet.DragModel model, org.alice.ide.x.AstI18nFactory factory, org.lgna.project.ast.ExpressionStatement expressionStatement, org.lgna.project.ast.StatementListProperty owner ) {
		super( model, factory, expressionStatement, owner );
		this.refresh();
	}

	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		final org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)getStatement();
		org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
			org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
			if( methodInvocation.isValid() ) {
				//pass
			} else {
				return java.awt.Color.RED;
			}
		}
		return super.getBackgroundPaint( x, y, width, height );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getExpressionStatement().expression.addPropertyListener( this.refreshAdapter );
	}

	@Override
	protected void handleUndisplayable() {
		this.getExpressionStatement().expression.removePropertyListener( this.refreshAdapter );
		super.handleUndisplayable();
	}

	private void refresh() {
		this.forgetAndRemoveAllComponents();
		final org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)getStatement();
		org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof org.lgna.project.ast.AssignmentExpression ) {
			this.addComponent( new AssignmentExpressionPane( this.getFactory(), (org.lgna.project.ast.AssignmentExpression)expression ) );
		} else {
			org.lgna.croquet.views.SwingComponentView<?> expressionPane = this.getFactory().createComponent( expressionStatement.expression.getValue() );
			this.addComponent( expressionPane );
			if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
				final org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
				assert methodInvocation.getParent() == expressionStatement;

				if( ( this.getFactory() == org.alice.ide.x.PreviewAstI18nFactory.getInstance() ) || methodInvocation.isValid() ) {
					//pass
				} else {
					this.setBackgroundColor( java.awt.Color.RED );
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( methodInvocation );
				}

				//				org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
				//				//todo:
				//				if( this.getFactory() == org.alice.ide.x.EditableAstI18Factory.getProjectGroupInstance() ) {
				//					org.lgna.project.ast.AbstractCode nextLonger = method.getNextLongerInChain();
				//					if( nextLonger != null ) {
				//						java.util.ArrayList< ? extends org.lgna.project.ast.AbstractParameter > parameters = nextLonger.getRequiredParameters();
				//						org.lgna.project.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
				//						org.lgna.croquet.Cascade< ? > cascade;
				//						if( lastParameter.isKeyworded() ) {
				//							cascade = org.alice.ide.croquet.models.ast.cascade.keyed.KeyedMoreCascade.getInstance( methodInvocation );
				//						} else {
				//							cascade = org.alice.ide.croquet.models.ast.cascade.MoreCascade.getInstance( methodInvocation );
				//						}
				//						this.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ) );
				//						org.lgna.croquet.components.AbstractButton< ?, ? > button = new org.alice.ide.croquet.PopupButton< org.lgna.croquet.PopupPrepModel >( cascade.getRoot().getPopupPrepModel() );
				//						button.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT );
				//						button.setVerticalAlignment( org.lgna.croquet.components.VerticalAlignment.CENTER );
				//						button.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
				//						this.addComponent( button );
				//					}
				//				}

				//			not worth while since instance creations are not normally (ever) the expression of an expression statements 				
				//			} else if( expression instanceof org.lgna.project.ast.InstanceCreation ) { 
				//				final org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)expression;
				//				
				//				if( instanceCreation.isValid() ) {
				//					//pass
				//				} else {
				//					this.setBackground( java.awt.Color.RED );
				//				}
				//				
				//				org.lgna.project.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
				//				//todo:
				//				if( this.getFactory() instanceof org.alice.ide.codeeditor.Factory ) {
				//					org.lgna.project.ast.AbstractMember nextLonger = constructor.getNextLongerInChain();
				//					if( nextLonger != null ) {
				//						final org.lgna.project.ast.AbstractConstructor nextLongerAbstractConstructor = (org.lgna.project.ast.AbstractConstructor)nextLonger;
				//						this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
				//						this.add( new org.alice.ide.codeeditor.MoreDropDownPane( expressionStatement ) );
				//					}
				//				}
			}
		}
		if( org.alice.ide.croquet.models.ui.formatter.FormatterState.isJava() ) {
			this.addComponent( new org.lgna.croquet.views.Label( ";" ) );
		}
		this.addComponent( org.lgna.croquet.views.BoxUtilities.createHorizontalSliver( 8 ) );
		this.revalidateAndRepaint();
	}

	//	@Override
	//	protected void handleControlClick( java.awt.event.MouseEvent e) {
	//		//super.handleControlClick();
	//		org.lgna.project.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getMethodDeclaredInAlice();
	//		if( methodDeclaredInAlice != null ) {
	//			getIDE().performIfAppropriate( new org.alice.ide.operations.ast.FocusCodeOperation( methodDeclaredInAlice ), e, true );
	//		}
	//	}

	protected org.lgna.project.ast.ExpressionStatement getExpressionStatement() {
		return (org.lgna.project.ast.ExpressionStatement)this.getStatement();
	}
}
