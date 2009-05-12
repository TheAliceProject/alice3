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

class DeleteStatementActionOperation extends org.alice.ide.operations.AbstractActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty property;

	public DeleteStatementActionOperation( org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		this.putValue( javax.swing.Action.NAME, "Delete" );
		this.statement = abstractStatementPane.getStatement();
		this.property = abstractStatementPane.getOwner();
	}
	public void perform( zoot.ActionContext actionContext ) {
		int i = this.property.indexOf( this.statement );
		if( i >= 0 ) {
			this.property.remove( i );
			actionContext.put( org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, true );
			actionContext.commit();
			getIDE().refreshUbiquitousPane();
		} else {
			throw new RuntimeException();
		}
	}
}

class StatementEnabledStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;

	public StatementEnabledStateOperation( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		super( statement.isEnabled.getValue() );
		this.statement = statement;
		this.putValue( javax.swing.Action.NAME, "Is Enabled" );
		//update();
	}
	//	private void update() {
	//		String text;
	//		if( this.statement.isEnabled.getValue() ) {
	//			text = "disable";
	//		} else {
	//			text = "enable";
	//		}
	//		this.putValue( javax.swing.Action.NAME, text );
	//	}
	public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
		this.statement.isEnabled.setValue( booleanStateContext.getNextValue() );
		//		this.update();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class Factory extends org.alice.ide.common.Factory {
	@Override
	protected java.awt.Component createArgumentListPropertyPane( edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty argumentListProperty ) {
		return new ArgumentListPropertyPane( this, argumentListProperty );
	}
	@Override
	public java.awt.Component createExpressionPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, java.awt.Component prefixPane ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionProperty.getValue();
//		java.awt.Component rv = ide.getOverrideComponent( expression );
//		if( rv != null ) {
//			//pass
//		} else {
//			rv = new org.alice.ide.common.ExpressionPropertyPane( this, expressionProperty );
//		}
		java.awt.Component rv = new org.alice.ide.common.ExpressionPropertyPane( this, expressionProperty );
		if( org.alice.ide.IDE.getSingleton().isDropDownDesiredFor( expression ) ) {
			//			Object owner = expressionProperty.getOwner();
			//			if( owner instanceof edu.cmu.cs.dennisc.alice.ast.Argument ) {
			//				edu.cmu.cs.dennisc.alice.ast.Argument argument = (edu.cmu.cs.dennisc.alice.ast.Argument)owner;
			//				edu.cmu.cs.dennisc.alice.ast.AbstractParameter paramter = argument.parameter.getValue();
			//				rv = new ArgumentExpressionPropertyDropDownPane( rv, expressionProperty, null, argument );
			//			} else {
			rv = new ExpressionPropertyDropDownPane( prefixPane, rv, expressionProperty );
			//			}
		}
		return rv;
	}
	@Override
	public org.alice.ide.common.AbstractStatementPane createStatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane abstractStatementPane = super.createStatementPane( statement, statementListProperty );
		abstractStatementPane.setDragAndDropOperation( new org.alice.ide.operations.DefaultDragAndDropOperation() );
		abstractStatementPane.setPopupOperation( new zoot.DefaultPopupActionOperation( this.createPopupOperations( abstractStatementPane ) ) );
		return abstractStatementPane;
	}
	protected java.util.List< zoot.Operation > updatePopupOperations( java.util.List< zoot.Operation > rv, org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
			//pass
		} else {
			rv.add( new StatementEnabledStateOperation( abstractStatementPane.getStatement() ) );
		}
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
			edu.cmu.cs.dennisc.alice.ast.ExpressionStatement expressionStatement = (edu.cmu.cs.dennisc.alice.ast.ExpressionStatement)statement;
			edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof edu.cmu.cs.dennisc.alice.ast.MethodInvocation ) {
				edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = (edu.cmu.cs.dennisc.alice.ast.MethodInvocation)expression;
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = methodInvocation.method.getValue();
				if( method instanceof edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice ) {
					rv.add( new org.alice.ide.operations.ast.FocusCodeOperation( method ) );
				}
			}
		}
		rv.add( zoot.ZManager.MENU_SEPARATOR );
		rv.add( new DeleteStatementActionOperation( abstractStatementPane ) );
		return rv;
	}
	private java.util.List< zoot.Operation > createPopupOperations( org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		return this.updatePopupOperations( new java.util.LinkedList< zoot.Operation >(), abstractStatementPane );
	}

}
