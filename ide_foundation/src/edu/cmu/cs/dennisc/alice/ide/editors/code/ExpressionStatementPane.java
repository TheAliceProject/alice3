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
package edu.cmu.cs.dennisc.alice.ide.editors.code;

/**
 * @author Dennis Cosgrove
 */
class ExpressionStatementPane extends AbstractStatementPane {
	private static edu.cmu.cs.dennisc.alice.ast.MethodInvocation createNextMethodInvocation( edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextMethod, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.MethodInvocation prevMethodInvocation ) {
		edu.cmu.cs.dennisc.alice.ast.MethodInvocation rv = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation();
		rv.expression.setValue( prevMethodInvocation.expression.getValue() );
		rv.method.setValue( nextMethod );
		
		java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextMethod.getParameters();
		final int N = parameters.size();
		for( int i=0; i<N-1; i++ ) {
			rv.arguments.add( new edu.cmu.cs.dennisc.alice.ast.Argument( parameters.get( i ), prevMethodInvocation.arguments.get( i ).expression.getValue() ) );
		}
		rv.arguments.add( new edu.cmu.cs.dennisc.alice.ast.Argument( parameters.get( N-1 ), expression ) );
		return rv;
	}
	public ExpressionStatementPane( edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty owner ) {
		super( expressionStatement, owner );
		this.refresh();
//		this.addMouseMotionListener( new java.awt.event.MouseMotionListener() {
//			public void mouseDragged( java.awt.event.MouseEvent e ) {
//			}
//			public void mouseMoved( java.awt.event.MouseEvent e ) {
//				if( edu.cmu.cs.dennisc.swing.SwingUtilities.isControlDown( e ) ) {
//					//set method font to underlined
//				}
//			}
//		} );
	}

	private void refresh() {
		this.removeAll();
		final edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)getStatement();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
			this.add( new AssignmentExpressionPane( (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)expression ) );
		} else {
			this.add( new TemplatePane( expressionStatement.expression.getValue() ) );
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) { 
				final edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
				edu.cmu.cs.dennisc.alice.ast.AbstractMember nextLonger = method.getNextLongerInChain();
				if( nextLonger != null ) {
					final edu.cmu.cs.dennisc.alice.ast.AbstractMethod nextLongerMethod = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)nextLonger;
					this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
					this.add( new DropDownPane( null, new edu.cmu.cs.dennisc.alice.ide.editors.common.Label( "more" ), null ) {
						@Override
						protected void handleLeftMousePress( java.awt.event.MouseEvent e ) {
							super.handleLeftMousePress( e );
							java.util.ArrayList< ? extends edu.cmu.cs.dennisc.alice.ast.AbstractParameter > parameters = nextLongerMethod.getParameters();
							edu.cmu.cs.dennisc.alice.ast.AbstractParameter lastParameter = parameters.get( parameters.size()-1 );
							getIDE().promptUserForMore( lastParameter, e, new edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
								public void handleCompletion( edu.cmu.cs.dennisc.alice.ast.Expression e ) {
									expressionStatement.expression.setValue( ExpressionStatementPane.createNextMethodInvocation( nextLongerMethod, e, methodInvocation ) );
									ExpressionStatementPane.this.refresh();
									//getIDE().unsetPreviousExpression();
									getIDE().markChanged( "more menu" );
								}
								public void handleCancelation() {
									getIDE().unsetPreviousExpression();
								}			
							} );
						}
					} );
					this.add( javax.swing.Box.createHorizontalStrut( 8 ) );
				}
			}
		}
		if( "java".equals( getIDE().getLocale().getVariant() ) ) {
			this.add( new edu.cmu.cs.dennisc.zoot.ZLabel( ";" ) );
		}
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
	
	@Override
	protected void handleControlClick( java.awt.event.MouseEvent e) {
		//super.handleControlClick();
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getMethodDeclaredInAlice();
		if( methodDeclaredInAlice != null ) {
			getIDE().performIfAppropriate( new edu.cmu.cs.dennisc.alice.ide.operations.FocusCodeOperation( methodDeclaredInAlice ), e );
		}
	}
	
	protected edu.cmu.cs.dennisc.alice.ast.ExpressionStatement getExpressionStatement() {
		return (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)this.getStatement();
	}
	
	@Override
	protected java.util.List< edu.cmu.cs.dennisc.alice.ide.Operation > updateOperationsListForAltMenu( java.util.List< edu.cmu.cs.dennisc.alice.ide.Operation > rv ) {
		rv = super.updateOperationsListForAltMenu( rv );
		edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice methodDeclaredInAlice = this.getMethodDeclaredInAlice();
		if( methodDeclaredInAlice != null ) {
			rv.add( new edu.cmu.cs.dennisc.alice.ide.operations.FocusCodeOperation( methodDeclaredInAlice ) );
		}
		return rv;
	}
}
