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

class ReturnStatementWrapper extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
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

class TransientStatementsWrapper extends edu.cmu.cs.dennisc.croquet.swing.LineAxisPane {
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice, VariableAssignmentStatementTemplate > mapVariableToVariableAssignmentStatementTemplate = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice, VariableAssignmentStatementTemplate >();
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice, VariableArrayAssignmentStatementTemplate > mapVariableToVariableArrayAssignmentStatementTemplate = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice, VariableArrayAssignmentStatementTemplate >();
	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice, ParameterArrayAssignmentStatementTemplate > mapParameterToParameterAssignmentStatementTemplate = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice, ParameterArrayAssignmentStatementTemplate >();
	private VariableAssignmentStatementTemplate getVariableAssignmentStatementTemplate( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable ) {
		VariableAssignmentStatementTemplate rv = this.mapVariableToVariableAssignmentStatementTemplate.get( variable );
		if( rv != null ) {
			//pass
		} else {
			rv = new VariableAssignmentStatementTemplate( variable );
			this.mapVariableToVariableAssignmentStatementTemplate.put( variable, rv );
		}
		return rv;
	}
	private VariableArrayAssignmentStatementTemplate getVariableArrayAssignmentStatementTemplate( edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable ) {
		VariableArrayAssignmentStatementTemplate rv = this.mapVariableToVariableArrayAssignmentStatementTemplate.get( variable );
		if( rv != null ) {
			//pass
		} else {
			rv = new VariableArrayAssignmentStatementTemplate( variable );
			this.mapVariableToVariableArrayAssignmentStatementTemplate.put( variable, rv );
		}
		return rv;
	}
	private ParameterArrayAssignmentStatementTemplate getParameterArrayAssignmentStatementTemplate( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter ) {
		ParameterArrayAssignmentStatementTemplate rv = this.mapParameterToParameterAssignmentStatementTemplate.get( parameter );
		if( rv != null ) {
			//pass
		} else {
			rv = new ParameterArrayAssignmentStatementTemplate( parameter );
			this.mapParameterToParameterAssignmentStatementTemplate.put( parameter, rv );
		}
		return rv;
	}
	
	public void refresh() {
		this.removeAll();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = org.alice.ide.IDE.getSingleton().getFocusedCode();
		if( code != null ) {
			for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : code.getParameters() ) {
				if( parameter instanceof edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice ) {
					edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameterInAlice = (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice)parameter;
					edu.cmu.cs.dennisc.alice.ast.AbstractType type = parameterInAlice.getValueType();
					if( type.isArray() ) {
						this.add( this.getParameterArrayAssignmentStatementTemplate( parameterInAlice ) );
					}
				}
			}
			
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement >( edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement.class );
			code.crawl( crawler, false );
			for( edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement variableDeclarationStatement : crawler.getList() ) {
				edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = variableDeclarationStatement.variable.getValue();
				this.add( this.getVariableAssignmentStatementTemplate( variable ) );
				edu.cmu.cs.dennisc.alice.ast.AbstractType type = variable.valueType.getValue();
				if( type.isArray() ) {
					this.add( this.getVariableArrayAssignmentStatementTemplate( variable ) );
				}
			}
		}
	}
}

//class LoopTemplate extends org.alice.ide.templates.StatementTemplate {
//	public LoopTemplate() {
//		super( edu.cmu.cs.dennisc.alice.ast.AbstractLoop.class );
//	}
//	@Override
//	public void createStatement( zoot.event.DragAndDropEvent e, edu.cmu.cs.dennisc.alice.ast.BlockStatement block, edu.cmu.cs.dennisc.task.TaskObserver< edu.cmu.cs.dennisc.alice.ast.Statement > taskObserver ) {
//		taskObserver.handleCancelation();
//	}
//}

/**
 * @author Dennis Cosgrove
 */
public class UbiquitousPane extends edu.cmu.cs.dennisc.croquet.swing.LineAxisPane {
	private DoInOrderTemplate doInOrderTemplate = new DoInOrderTemplate();
//	private LoopTemplate loopTemplate = new LoopTemplate();
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
		final int PAD = 6;
		this.add( this.doInOrderTemplate );
		this.add( javax.swing.Box.createHorizontalStrut( PAD ) );
//		this.add( this.loopTemplate );

		this.add( this.countLoopTemplate );
		this.add( this.whileLoopTemplate );
		this.add( this.forEachInArrayLoopTemplate );
		this.add( javax.swing.Box.createHorizontalStrut( PAD ) );
		this.add( this.conditionalStatementTemplate );
		this.add( javax.swing.Box.createHorizontalStrut( PAD ) );
		this.add( this.doTogetherTemplate );
		this.add( this.eachInArrayTogetherTemplate );
		this.add( javax.swing.Box.createHorizontalStrut( PAD ) );
		this.add( this.declareLocalTemplate );
		this.add( this.transientStatementsWrapper );
		this.add( this.returnStatementWrapper );
		this.add( javax.swing.Box.createHorizontalStrut( PAD ) );
		this.add( this.commentTemplate );
		
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		ide.addIDEListener( new org.alice.ide.event.IDEAdapter() {
			@Override
			public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
				UbiquitousPane.this.handleFocusedCodeChanged( e );
			}
		} );
	}
	private void handleFocusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		this.refresh();
	}
	
	public void refresh() {
		this.returnStatementWrapper.refresh();
		this.transientStatementsWrapper.refresh();
		this.revalidate();
		this.repaint();
	}
}
