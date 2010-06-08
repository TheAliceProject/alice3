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
package org.alice.ide.ubiquitouspane;

import org.alice.ide.ubiquitouspane.templates.*;

class ReturnStatementWrapper extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
	private ReturnStatementTemplate re = new ReturnStatementTemplate();
	public void refresh() {
		this.removeAllComponents();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = org.alice.ide.IDE.getSingleton().getFocusedCode();
		edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( code, edu.cmu.cs.dennisc.alice.ast.AbstractMethod.class );
		if( method != null && method.isFunction() ) {
			this.addComponent( re );
		}
		this.revalidateAndRepaint();
	}
}

class TransientStatementsWrapper extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
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
		this.removeAllComponents();
		edu.cmu.cs.dennisc.alice.ast.AbstractCode code = org.alice.ide.IDE.getSingleton().getFocusedCode();
		if( code != null ) {
			for( edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter : code.getParameters() ) {
				if( parameter instanceof edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice ) {
					edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameterInAlice = (edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice)parameter;
					edu.cmu.cs.dennisc.alice.ast.AbstractType type = parameterInAlice.getValueType();
					if( type.isArray() ) {
						this.addComponent( this.getParameterArrayAssignmentStatementTemplate( parameterInAlice ) );
					}
				}
			}
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement >( edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement.class );
			code.crawl( crawler, false );
			for( edu.cmu.cs.dennisc.alice.ast.VariableDeclarationStatement variableDeclarationStatement : crawler.getList() ) {
				edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice variable = variableDeclarationStatement.variable.getValue();
				this.addComponent( this.getVariableAssignmentStatementTemplate( variable ) );
				edu.cmu.cs.dennisc.alice.ast.AbstractType type = variable.valueType.getValue();
				if( type.isArray() ) {
					this.addComponent( this.getVariableArrayAssignmentStatementTemplate( variable ) );
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
public class UbiquitousPane extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
	private DoInOrderTemplate doInOrderTemplate = new DoInOrderTemplate();
//	private LoopTemplate loopTemplate = new LoopTemplate();
	private CountLoopTemplate countLoopTemplate = new CountLoopTemplate();
	private WhileLoopTemplate whileLoopTemplate = new WhileLoopTemplate();
	private ForEachInArrayLoopTemplate forEachInArrayLoopTemplate = new ForEachInArrayLoopTemplate();
	private ConditionalStatementTemplate conditionalStatementTemplate = new ConditionalStatementTemplate();
	private DoTogetherTemplate doTogetherTemplate = new DoTogetherTemplate();
	private EachInArrayTogetherTemplate eachInArrayTogetherTemplate = new EachInArrayTogetherTemplate();
	private DoInThreadTemplate doInThreadTemplate = new DoInThreadTemplate();
	private DeclareLocalTemplate declareLocalTemplate = new DeclareLocalTemplate();
	private CommentTemplate commentTemplate = new CommentTemplate();
	
	private ReturnStatementWrapper returnStatementWrapper = new ReturnStatementWrapper();
	private TransientStatementsWrapper transientStatementsWrapper = new TransientStatementsWrapper();

	private edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode > selectionObserver = new edu.cmu.cs.dennisc.croquet.ItemSelectionState.ValueObserver< edu.cmu.cs.dennisc.alice.ast.AbstractCode >() {
		public void changed(edu.cmu.cs.dennisc.alice.ast.AbstractCode nextValue) {
			UbiquitousPane.this.refresh();
		}
	};

	public UbiquitousPane() {
		final int PAD = 6;
		this.addComponent( this.doInOrderTemplate );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( PAD ) );
//		this.addComponent( this.loopTemplate );

		this.addComponent( this.countLoopTemplate );
		this.addComponent( this.whileLoopTemplate );
		this.addComponent( this.forEachInArrayLoopTemplate );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( PAD ) );
		this.addComponent( this.conditionalStatementTemplate );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( PAD ) );
		this.addComponent( this.doTogetherTemplate );
		this.addComponent( this.eachInArrayTogetherTemplate );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( PAD ) );
		this.addComponent( this.doInThreadTemplate );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( PAD ) );
		this.addComponent( this.declareLocalTemplate );
		this.addComponent( this.transientStatementsWrapper );
		this.addComponent( this.returnStatementWrapper );
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( PAD ) );
		this.addComponent( this.commentTemplate );
		
		this.setBackgroundColor( edu.cmu.cs.dennisc.croquet.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
	}
	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().addAndInvokeValueObserver( this.selectionObserver );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		org.alice.ide.IDE.getSingleton().getEditorsTabSelectionState().removeValueObserver( this.selectionObserver );
		super.handleRemovedFrom( parent );
	}

	public void refresh() {
		this.returnStatementWrapper.refresh();
		this.transientStatementsWrapper.refresh();
		this.revalidateAndRepaint();
	}
}
