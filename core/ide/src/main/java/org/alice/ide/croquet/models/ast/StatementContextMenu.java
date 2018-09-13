/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.alice.ide.croquet.models.ast;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.IDE;
import org.alice.ide.ast.delete.DeleteStatementOperation;
import org.alice.ide.clipboard.CopyToClipboardOperation;
import org.alice.ide.croquet.models.ast.keyed.RemoveKeyedArgumentOperation;
import org.alice.ide.issue.croquet.AnomalousSituationComposite;
import org.alice.stageide.run.FastForwardToStatementOperation;
import org.lgna.croquet.MenuModel;
import org.lgna.croquet.StandardMenuItemPrepModel;
import org.lgna.croquet.views.MenuItemContainerUtilities;
import org.lgna.croquet.views.PopupMenu;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AbstractStatementWithBody;
import org.lgna.project.ast.ArgumentOwner;
import org.lgna.project.ast.BlockStatement;
import org.lgna.project.ast.Comment;
import org.lgna.project.ast.ConditionalStatement;
import org.lgna.project.ast.DoInOrder;
import org.lgna.project.ast.DoTogether;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.JavaKeyedArgument;
import org.lgna.project.ast.KeyedArgumentListProperty;
import org.lgna.project.ast.MethodInvocation;
import org.lgna.project.ast.NodeUtilities;
import org.lgna.project.ast.Statement;
import org.lgna.project.ast.UserMethod;

import javax.swing.SwingUtilities;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class StatementContextMenu extends MenuModel {
	private static Map<Statement, StatementContextMenu> map = Maps.newHashMap();

	public static synchronized StatementContextMenu getInstance( Statement statement ) {
		StatementContextMenu rv = map.get( statement );
		if( rv != null ) {
			//pass
		} else {
			rv = new StatementContextMenu( statement );
			map.put( statement, rv );
		}
		return rv;
	}

	private Statement statement;

	private StatementContextMenu( Statement statement ) {
		super( UUID.fromString( "6d152827-60e1-4d1c-b589-843ec554957c" ) );
		this.statement = statement;
	}

	public Statement getStatement() {
		return this.statement;
	}

	private List<StandardMenuItemPrepModel> updatePopupOperations( List<StandardMenuItemPrepModel> rv, final Statement statement ) {
		if( statement instanceof Comment ) {
			//pass
		} else {
			rv.add( new FastForwardToStatementOperation( statement ).getMenuItemPrepModel() );
			rv.add( MenuModel.SEPARATOR );
			rv.add( IsStatementEnabledState.getInstance( statement ).getMenuItemPrepModel() );
		}
		if( statement instanceof ExpressionStatement ) {
			ExpressionStatement expressionStatement = (ExpressionStatement)statement;
			Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof MethodInvocation ) {
				MethodInvocation methodInvocation = (MethodInvocation)expression;
				AbstractMethod method = methodInvocation.method.getValue();
				if( method instanceof UserMethod ) {
					rv.add( IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().getItemSelectionOperationForMethod( method ).getMenuItemPrepModel() );
				}
			}
		}
		rv.add( MenuModel.SEPARATOR );
		rv.add( CopyToClipboardOperation.getInstance( statement ).getMenuItemPrepModel() );
		rv.add( MenuModel.SEPARATOR );

		BlockStatement blockStatement = (BlockStatement)statement.getParent();
		if( blockStatement != null ) {
			rv.add( DeleteStatementOperation.getInstance( statement ).getMenuItemPrepModel() );
		} else {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					StringBuilder sb = new StringBuilder();
					try {
						NodeUtilities.safeAppendRepr( sb, statement );
					} catch( Throwable t ) {
						sb.append( statement );
					}
					sb.append( ";" );
					sb.append( statement.getId() );
					AnomalousSituationComposite.createInstance( "Oh no!  A popup menu has been requested for a statement without a parent.", sb.toString() ).getLaunchOperation().fire();
				}
			} );
			//throw new org.lgna.croquet.CancelException();
		}
		if( statement instanceof AbstractStatementWithBody ) {
			AbstractStatementWithBody statementWithBody = (AbstractStatementWithBody)statement;
			rv.add( DissolveStatementWithBodyOperation.getInstance( statementWithBody ).getMenuItemPrepModel() );
			if( statementWithBody instanceof DoInOrder ) {
				DoInOrder doInOrder = (DoInOrder)statementWithBody;
				rv.add( ConvertDoInOrderToDoTogetherOperation.getInstance( doInOrder ).getMenuItemPrepModel() );
			} else if( statementWithBody instanceof DoTogether ) {
				DoTogether doTogether = (DoTogether)statementWithBody;
				rv.add( ConvertDoTogetherToDoInOrderOperation.getInstance( doTogether ).getMenuItemPrepModel() );
			}
		} else if( statement instanceof ConditionalStatement ) {
			ConditionalStatement conditionalStatement = (ConditionalStatement)statement;
			//todo: dissolve to if, dissolve to else
		} else if( statement instanceof ExpressionStatement ) {
			ExpressionStatement expressionStatement = (ExpressionStatement)statement;
			Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof ArgumentOwner ) {
				ArgumentOwner argumentOwner = (ArgumentOwner)expression;
				KeyedArgumentListProperty argumentListProperty = argumentOwner.getKeyedArgumentsProperty();
				for( JavaKeyedArgument argument : argumentListProperty ) {
					rv.add( RemoveKeyedArgumentOperation.getInstance( argumentListProperty, argument ).getMenuItemPrepModel() );
				}
			}
		}
		return rv;
	}

	@Override
	public void handlePopupMenuPrologue( PopupMenu popupMenu ) {
		super.handlePopupMenuPrologue( popupMenu );
		List<StandardMenuItemPrepModel> models = Lists.newLinkedList();
		this.updatePopupOperations( models, this.statement );
		MenuItemContainerUtilities.setMenuElements( popupMenu, models );
	}
}
