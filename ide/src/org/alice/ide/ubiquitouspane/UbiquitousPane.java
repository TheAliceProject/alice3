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

class ReturnStatementWrapper extends org.lgna.croquet.components.LineAxisPanel {
	private ReturnStatementTemplate re = new ReturnStatementTemplate();
	public void refresh() {
		this.removeAllComponents();
		org.lgna.project.ast.AbstractCode code = org.alice.ide.IDE.getActiveInstance().getFocusedCode();
		org.lgna.project.ast.AbstractMethod method = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( code, org.lgna.project.ast.AbstractMethod.class );
		if( method != null && method.isFunction() ) {
			this.addComponent( re );
		}
		this.revalidateAndRepaint();
	}
}

class TransientStatementsWrapper extends org.lgna.croquet.components.LineAxisPanel {
	private java.util.Map< org.lgna.project.ast.UserVariable, VariableAssignmentStatementTemplate > mapVariableToVariableAssignmentStatementTemplate = new java.util.HashMap< org.lgna.project.ast.UserVariable, VariableAssignmentStatementTemplate >();
	private java.util.Map< org.lgna.project.ast.UserVariable, VariableArrayAssignmentStatementTemplate > mapVariableToVariableArrayAssignmentStatementTemplate = new java.util.HashMap< org.lgna.project.ast.UserVariable, VariableArrayAssignmentStatementTemplate >();
	private java.util.Map< org.lgna.project.ast.UserParameter, ParameterArrayAssignmentStatementTemplate > mapParameterToParameterAssignmentStatementTemplate = new java.util.HashMap< org.lgna.project.ast.UserParameter, ParameterArrayAssignmentStatementTemplate >();
	private VariableAssignmentStatementTemplate getVariableAssignmentStatementTemplate( org.lgna.project.ast.UserVariable variable ) {
		VariableAssignmentStatementTemplate rv = this.mapVariableToVariableAssignmentStatementTemplate.get( variable );
		if( rv != null ) {
			//pass
		} else {
			rv = new VariableAssignmentStatementTemplate( variable );
			this.mapVariableToVariableAssignmentStatementTemplate.put( variable, rv );
		}
		return rv;
	}
	private VariableArrayAssignmentStatementTemplate getVariableArrayAssignmentStatementTemplate( org.lgna.project.ast.UserVariable variable ) {
		VariableArrayAssignmentStatementTemplate rv = this.mapVariableToVariableArrayAssignmentStatementTemplate.get( variable );
		if( rv != null ) {
			//pass
		} else {
			rv = new VariableArrayAssignmentStatementTemplate( variable );
			this.mapVariableToVariableArrayAssignmentStatementTemplate.put( variable, rv );
		}
		return rv;
	}
	private ParameterArrayAssignmentStatementTemplate getParameterArrayAssignmentStatementTemplate( org.lgna.project.ast.UserParameter parameter ) {
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
		org.lgna.project.ast.AbstractCode code = org.alice.ide.IDE.getActiveInstance().getFocusedCode();
		if( code != null ) {
			for( org.lgna.project.ast.AbstractParameter parameter : code.getParameters() ) {
				if( parameter instanceof org.lgna.project.ast.UserParameter ) {
					org.lgna.project.ast.UserParameter parameterInAlice = (org.lgna.project.ast.UserParameter)parameter;
					org.lgna.project.ast.AbstractType<?,?,?> type = parameterInAlice.getValueType();
					if( type.isArray() ) {
						this.addComponent( this.getParameterArrayAssignmentStatementTemplate( parameterInAlice ) );
					}
				}
			}
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.VariableDeclarationStatement > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< org.lgna.project.ast.VariableDeclarationStatement >( org.lgna.project.ast.VariableDeclarationStatement.class );
			code.crawl( crawler, false );
			for( org.lgna.project.ast.VariableDeclarationStatement variableDeclarationStatement : crawler.getList() ) {
				org.lgna.project.ast.UserVariable variable = variableDeclarationStatement.variable.getValue();
				this.addComponent( this.getVariableAssignmentStatementTemplate( variable ) );
				org.lgna.project.ast.AbstractType<?,?,?> type = variable.valueType.getValue();
				if( type.isArray() ) {
					this.addComponent( this.getVariableArrayAssignmentStatementTemplate( variable ) );
				}
			}
		}
		this.revalidateAndRepaint();
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
public class UbiquitousPane extends org.lgna.croquet.components.ViewPanel {
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

	private org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.editorstabbedpane.CodeComposite > selectionObserver = new org.lgna.croquet.ListSelectionState.ValueObserver< org.alice.ide.editorstabbedpane.CodeComposite >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.editorstabbedpane.CodeComposite > state, org.alice.ide.editorstabbedpane.CodeComposite prevValue, org.alice.ide.editorstabbedpane.CodeComposite nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.editorstabbedpane.CodeComposite > state, org.alice.ide.editorstabbedpane.CodeComposite prevValue, org.alice.ide.editorstabbedpane.CodeComposite nextValue, boolean isAdjusting ) {
			UbiquitousPane.this.refresh();
		}
	};
	private org.lgna.croquet.State.ValueObserver< Boolean > isAlwaysShowingBlocksObserver = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			UbiquitousPane.this.removeAllComponents();
			UbiquitousPane.this.getAwtComponent().setLayout( UbiquitousPane.this.createLayoutManager( UbiquitousPane.this.getAwtComponent() ) );
			UbiquitousPane.this.addComponents();
			UbiquitousPane.this.revalidateAndRepaint();
		}
	};

	public UbiquitousPane() {
		super( org.alice.ide.croquet.models.templates.BlockTemplateComposite.getInstance() );
		this.addComponents();
		this.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
	}
	private void addComponents() {
		this.addComponent( this.doInOrderTemplate );
		this.addSliver();
		this.addComponent( this.countLoopTemplate );
		this.addComponent( this.whileLoopTemplate );
		this.addComponent( this.forEachInArrayLoopTemplate );
		this.addSliver();
		this.addComponent( this.conditionalStatementTemplate );
		this.addSliver();
		this.addComponent( this.doTogetherTemplate );
		this.addComponent( this.eachInArrayTogetherTemplate );
		this.addSliver();
		this.addComponent( this.doInThreadTemplate );
		this.addSliver();
		this.addComponent( this.declareLocalTemplate );
		this.addComponent( this.transientStatementsWrapper );
		this.addComponent( this.returnStatementWrapper );
		this.addSliver();
		this.addComponent( this.commentTemplate );
	}
	
	private boolean isHorizontal() {
		return org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue();
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		if( this.isHorizontal() ) {
			return new wrap.WrappedFlowLayout( wrap.WrappedFlowLayout.LEADING, 0, 0 );
		} else {
			return new javax.swing.BoxLayout( jPanel, javax.swing.BoxLayout.PAGE_AXIS );
		}
	}
	public void addComponent( org.lgna.croquet.components.Component<?> component ) {
		this.internalAddComponent( component );
	}

	private static final int PAD = 6;
	private void addSliver() {
		org.lgna.croquet.components.JComponent< ? > sliver;
		if( this.isHorizontal() ) {
			sliver = org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( PAD );
		} else {
			sliver = org.lgna.croquet.components.BoxUtilities.createVerticalSliver( PAD );
		}
		this.addComponent( sliver );
	}
	
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().addAndInvokeValueObserver( this.selectionObserver );
		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().addValueObserver( this.isAlwaysShowingBlocksObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().removeValueObserver( this.isAlwaysShowingBlocksObserver );
		org.alice.ide.editorstabbedpane.EditorsTabSelectionState.getInstance().removeValueObserver( this.selectionObserver );
		super.handleUndisplayable();
	}

	public void refresh() {
		org.alice.ide.IDE.getActiveInstance().refreshAccessibles();
		this.returnStatementWrapper.refresh();
		this.transientStatementsWrapper.refresh();
		this.revalidateAndRepaint();
	}
}
