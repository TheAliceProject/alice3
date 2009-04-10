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
package org.alice.ide.ubiquitouspane;

import org.alice.ide.ubiquitouspane.templates.*;

class ReturnStatementWrapper extends swing.BorderPane {
	private ReturnStatementTemplate re = new ReturnStatementTemplate();
	public void refresh() {
		this.removeAll();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = org.alice.ide.IDE.getSingleton().getFocusedCode();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( code, edu.cmu.cs.dennisc.alice.ast.AbstractMethod.class );
		if( method != null && method.isFunction() ) {
			this.add( re );
		}
	}
}

class TransientStatementsWrapper extends swing.BorderPane {
	public void refresh() {
		this.removeAll();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = org.alice.ide.IDE.getSingleton().getFocusedCode();
		if( code != null ) {
			for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : code.getParameters() ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractType type = parameter.getValueType();
				if( type.isArray() ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: add parameter[ i ] <- ???" );
				}
			}
			for( edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement variableDeclarationStatement : org.alice.ide.IDE.getSingleton().getVariableDeclarationStatements( code ) ) {
				edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = variableDeclarationStatement.variable.getValue();
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: add variable <- ???" );
				edu.cmu.cs.dennisc.alice.ast.AbstractType type = variable.valueType.getValue();
				if( type.isArray() ) {
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: add variable[ i ] = ???" );
				}
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class UbiquitousPane extends swing.LineAxisPane {
	private DoInOrderTemplate doInOrderTemplate = new DoInOrderTemplate();
	private CountLoopTemplate countLoopTemplate = new CountLoopTemplate();
	private WhileLoopTemplate whileLoopTemplate = new WhileLoopTemplate();
	private ForEachInArrayLoopTemplate forEachInArrayLoopTemplate = new ForEachInArrayLoopTemplate();
	private ConditionalStatementTemplate conditionalStatementTemplate = new ConditionalStatementTemplate();
	private DoTogetherTemplate doTogetherTemplate = new DoTogetherTemplate();
	private EachInArrayTogetherTemplate eachInArrayTogetherTemplate = new EachInArrayTogetherTemplate();
	private DeclareLocalTemplate declareLocalTemplate = new DeclareLocalTemplate();
	private CommentTemplate commentTemplate = new CommentTemplate();
	
	private ReturnStatementWrapper returnStatementWrapper = new ReturnStatementWrapper();
	private TransientStatementsWrapper transientStatementsWrapper = new TransientStatementsWrapper();

	public UbiquitousPane() {
		this.add( this.doInOrderTemplate );
		this.add( this.countLoopTemplate );
		this.add( this.whileLoopTemplate );
		this.add( this.forEachInArrayLoopTemplate );
		this.add( this.conditionalStatementTemplate );
		this.add( this.doTogetherTemplate );
		this.add( this.eachInArrayTogetherTemplate );
		this.add( this.declareLocalTemplate );
		this.add( this.commentTemplate );
		
		this.add( this.returnStatementWrapper );
		this.add( this.transientStatementsWrapper );
		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		ide.addIDEListener( new org.alice.ide.event.IDEAdapter() {
			@Override
			public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
				UbiquitousPane.this.handleFocusedCodeChanged( e );
			}
		} );
	}
	private void handleFocusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		this.returnStatementWrapper.refresh();
		this.transientStatementsWrapper.refresh();
	}
}
