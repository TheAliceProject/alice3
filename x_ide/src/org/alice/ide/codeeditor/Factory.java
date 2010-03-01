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
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty property;
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;

	public DeleteStatementActionOperation( org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		super( org.alice.ide.IDE.PROJECT_GROUP );
		this.putValue( javax.swing.Action.NAME, "Delete" );
		this.property = abstractStatementPane.getOwner();
		this.statement = abstractStatementPane.getStatement();
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final int index = this.property.indexOf( this.statement );
		if( index >= 0 ) {
			actionContext.commitAndInvokeDo( new edu.cmu.cs.dennisc.zoot.AbstractEdit() {
				@Override
				public void doOrRedo( boolean isDo ) {
					property.remove( index );
					//todo: remove
					getIDE().refreshUbiquitousPane();
				}
				@Override
				public void undo() {
					property.add( index, statement );
					//todo: remove
					getIDE().refreshUbiquitousPane();
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "delete:" );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, statement, locale);
					return rv;
				}
			} );
		} else {
			throw new RuntimeException();
		}
	}
}

class StatementEnabledStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;

	public StatementEnabledStateOperation( edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, statement.isEnabled.getValue() );
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
	@Override
	protected void handleStateChange( boolean value ) {
		this.statement.isEnabled.setValue( value );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: undo/redo support for", this );
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
	public java.awt.Component createExpressionPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, java.awt.Component prefixPane, edu.cmu.cs.dennisc.alice.ast.AbstractType desiredValueType ) {
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
			rv = new ExpressionPropertyDropDownPane( prefixPane, rv, expressionProperty, desiredValueType );
			//			}
		}
		return rv;
	}
	@Override
	public org.alice.ide.common.AbstractStatementPane createStatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane abstractStatementPane = super.createStatementPane( statement, statementListProperty );
		abstractStatementPane.setDragAndDropOperation( new org.alice.ide.operations.DefaultDragAndDropOperation() );
		abstractStatementPane.setPopupOperation( new edu.cmu.cs.dennisc.zoot.DefaultPopupActionOperation( this.createPopupOperations( abstractStatementPane ) ) );
		return abstractStatementPane;
	}
	protected java.util.List< edu.cmu.cs.dennisc.zoot.Operation > updatePopupOperations( java.util.List< edu.cmu.cs.dennisc.zoot.Operation > rv, org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
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
		rv.add( edu.cmu.cs.dennisc.zoot.ZManager.MENU_SEPARATOR );
		rv.add( new DeleteStatementActionOperation( abstractStatementPane ) );
		return rv;
	}
	private java.util.List< edu.cmu.cs.dennisc.zoot.Operation > createPopupOperations( org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		return this.updatePopupOperations( new java.util.LinkedList< edu.cmu.cs.dennisc.zoot.Operation >(), abstractStatementPane );
	}

}
