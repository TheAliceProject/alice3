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
		edu.cmu.cs.dennisc.swing.ForgetUtilities.forgetAndRemoveAllComponents( this );
		final edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)getStatement();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
			this.add( new AssignmentExpressionPane( this.getFactory(), (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)expression ) );
		} else {
			this.add( this.getFactory().createComponent( expressionStatement.expression.getValue() ) );
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) { 
				final edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
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
			}
		}
		if( getIDE().isJava() ) {
			this.add( zoot.ZLabel.acquire( ";" ) );
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
