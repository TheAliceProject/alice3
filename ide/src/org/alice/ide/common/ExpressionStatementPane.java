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

/**
 * @author Dennis Cosgrove
 */
public class ExpressionStatementPane extends AbstractStatementPane {
	private edu.cmu.cs.dennisc.property.event.PropertyListener refreshAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			ExpressionStatementPane.this.refresh();
		}
	};
	public ExpressionStatementPane( Factory factory, edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner ) {
		super( factory, expressionStatement, owner );
		this.refresh();
	}

	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		final edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)getStatement();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
			edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
			if( methodInvocation.isValid() ) {
				//pass
			} else {
				return java.awt.Color.RED;
			}
		}
		return super.getBackgroundPaint( x, y, width, height );
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.getExpressionStatement().expression.addPropertyListener( this.refreshAdapter );
	}
	@Override
	public void removeNotify() {
		this.getExpressionStatement().expression.removePropertyListener( this.refreshAdapter );
		super.removeNotify();
	}

	private void refresh() {
		edu.cmu.cs.dennisc.javax.swing.ForgetUtilities.forgetAndRemoveAllComponents( this );
		final edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)getStatement();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
			this.add( new AssignmentExpressionPane( this.getFactory(), (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)expression ) );
		} else {
			this.add( this.getFactory().createComponent( expressionStatement.expression.getValue() ) );
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) { 
				final edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
				
				if( methodInvocation.isValid() ) {
					//pass
				} else {
					this.setBackground( java.awt.Color.RED );
				}
				
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
				//todo:
				if( this.getFactory() instanceof org.alice.ide.codeeditor.Factory ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractMember nextLonger = method.getNextLongerInChain();
					if( nextLonger != null ) {
						final edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)nextLonger;
						this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
						this.add( new org.alice.ide.codeeditor.MoreDropDownPane( expressionStatement ) );
					}
				}


//			not worth while since instance creations are not normally (ever) the expression of an expression statements 				
//			} else if( expression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) { 
//				final edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)expression;
//				
//				if( instanceCreation.isValid() ) {
//					//pass
//				} else {
//					this.setBackground( java.awt.Color.RED );
//				}
//				
//				edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
//				//todo:
//				if( this.getFactory() instanceof org.alice.ide.codeeditor.Factory ) {
//					edu.cmu.cs.dennisc.alice.ast.AbstractMember nextLonger = constructor.getNextLongerInChain();
//					if( nextLonger != null ) {
//						final edu.cmu.cs.dennisc.alice.ast.AbstractConstructor nextLongerAbstractConstructor = (edu.cmu.cs.dennisc.alice.ast.AbstractConstructor)nextLonger;
//						this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
//						this.add( new org.alice.ide.codeeditor.MoreDropDownPane( expressionStatement ) );
//					}
//				}
			}
		}
		if( getIDE().isJava() ) {
			this.add( edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( ";" ) );
		}
		this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
		ExpressionStatementPane.this.revalidate();
		ExpressionStatementPane.this.repaint();
	}
	private edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice getMethodDeclaredInAlice() {
		edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = this.getExpressionStatement();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
			edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
			if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
				return (edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice)method;
			}
		}
		return null;
	}
	
//	@Override
//	protected void handleControlClick( java.awt.event.MouseEvent e) {
//		//super.handleControlClick();
//		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getMethodDeclaredInAlice();
//		if( methodDeclaredInAlice != null ) {
//			getIDE().performIfAppropriate( new org.alice.ide.operations.ast.FocusCodeOperation( methodDeclaredInAlice ), e, true );
//		}
//	}
	
	protected edu.cmu.cs.dennisc.alice.ast.ExpressionStatement getExpressionStatement() {
		return (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)this.getStatement();
	}
	
//	@Override
//	protected java.util.List< org.alice.ide.operations.AbstractActionOperation > updateOperationsListForAltMenu( java.util.List< org.alice.ide.operations.AbstractActionOperation > rv ) {
//		rv = super.updateOperationsListForAltMenu( rv );
//		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getMethodDeclaredInAlice();
//		if( methodDeclaredInAlice != null ) {
//			rv.add( new org.alice.ide.operations.ast.FocusCodeOperation( methodDeclaredInAlice ) );
//		}
//		return rv;
//	}
}
