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

package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class StatementContextMenu extends org.lgna.croquet.MenuModel {
	private static java.util.Map<org.lgna.project.ast.Statement, StatementContextMenu> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static synchronized StatementContextMenu getInstance( org.lgna.project.ast.Statement statement ) {
		StatementContextMenu rv = map.get( statement );
		if( rv != null ) {
			//pass
		} else {
			rv = new StatementContextMenu( statement );
			map.put( statement, rv );
		}
		return rv;
	}

	private org.lgna.project.ast.Statement statement;

	private StatementContextMenu( org.lgna.project.ast.Statement statement ) {
		super( java.util.UUID.fromString( "6d152827-60e1-4d1c-b589-843ec554957c" ) );
		this.statement = statement;
	}

	public org.lgna.project.ast.Statement getStatement() {
		return this.statement;
	}

	private java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> updatePopupOperations( java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> rv, final org.lgna.project.ast.Statement statement ) {
		if( statement instanceof org.lgna.project.ast.Comment ) {
			//pass
		} else {
			rv.add( new org.alice.stageide.run.FastForwardToStatementOperation( statement ).getMenuItemPrepModel() );
			rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
			rv.add( org.alice.ide.croquet.models.ast.IsStatementEnabledState.getInstance( statement ).getMenuItemPrepModel() );
		}
		if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
			org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
			org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
				org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
				org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
				if( method instanceof org.lgna.project.ast.UserMethod ) {
					rv.add( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getItemSelectionOperationForMethod( method ).getMenuItemPrepModel() );
				}
			}
		}
		rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
		rv.add( org.alice.ide.clipboard.CopyToClipboardOperation.getInstance( statement ).getMenuItemPrepModel() );
		rv.add( org.lgna.croquet.MenuModel.SEPARATOR );

		org.lgna.project.ast.BlockStatement blockStatement = (org.lgna.project.ast.BlockStatement)statement.getParent();
		if( blockStatement != null ) {
			rv.add( org.alice.ide.ast.delete.DeleteStatementOperation.getInstance( statement ).getMenuItemPrepModel() );
		} else {
			javax.swing.SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					StringBuilder sb = new StringBuilder();
					try {
						org.lgna.project.ast.NodeUtilities.safeAppendRepr( sb, statement );
					} catch( Throwable t ) {
						sb.append( statement );
					}
					sb.append( ";" );
					sb.append( statement.getId() );
					org.alice.ide.issue.croquet.AnomalousSituationComposite.createInstance( "Oh no!  A popup menu has been requested for a statement without a parent.", sb.toString() ).getLaunchOperation().fire();
				}
			} );
			//throw new org.lgna.croquet.CancelException();
		}
		if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
			org.lgna.project.ast.AbstractStatementWithBody statementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
			rv.add( org.alice.ide.croquet.models.ast.DissolveStatementWithBodyOperation.getInstance( statementWithBody ).getMenuItemPrepModel() );
			if( statementWithBody instanceof org.lgna.project.ast.DoInOrder ) {
				org.lgna.project.ast.DoInOrder doInOrder = (org.lgna.project.ast.DoInOrder)statementWithBody;
				rv.add( org.alice.ide.croquet.models.ast.ConvertDoInOrderToDoTogetherOperation.getInstance( doInOrder ).getMenuItemPrepModel() );
			} else if( statementWithBody instanceof org.lgna.project.ast.DoTogether ) {
				org.lgna.project.ast.DoTogether doTogether = (org.lgna.project.ast.DoTogether)statementWithBody;
				rv.add( org.alice.ide.croquet.models.ast.ConvertDoTogetherToDoInOrderOperation.getInstance( doTogether ).getMenuItemPrepModel() );
			}
		} else if( statement instanceof org.lgna.project.ast.ConditionalStatement ) {
			org.lgna.project.ast.ConditionalStatement conditionalStatement = (org.lgna.project.ast.ConditionalStatement)statement;
			//todo: dissolve to if, dissolve to else
		} else if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
			org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
			org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof org.lgna.project.ast.ArgumentOwner ) {
				org.lgna.project.ast.ArgumentOwner argumentOwner = (org.lgna.project.ast.ArgumentOwner)expression;
				org.lgna.project.ast.KeyedArgumentListProperty argumentListProperty = argumentOwner.getKeyedArgumentsProperty();
				for( org.lgna.project.ast.JavaKeyedArgument argument : argumentListProperty ) {
					rv.add( org.alice.ide.croquet.models.ast.keyed.RemoveKeyedArgumentOperation.getInstance( argumentListProperty, argument ).getMenuItemPrepModel() );
				}
			}
		}
		return rv;
	}

	@Override
	public void handlePopupMenuPrologue( org.lgna.croquet.views.PopupMenu popupMenu, org.lgna.croquet.history.PopupPrepStep context ) {
		super.handlePopupMenuPrologue( popupMenu, context );
		java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.updatePopupOperations( models, this.statement );
		org.lgna.croquet.views.MenuItemContainerUtilities.setMenuElements( popupMenu, models );
	}
}
