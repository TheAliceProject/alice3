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
package org.alice.ide.codeeditor;

abstract class ConvertStatementWithBodyActionOperation extends org.lgna.croquet.ActionOperation {
	private org.lgna.project.ast.StatementListProperty property;
	private org.lgna.project.ast.AbstractStatementWithBody original;
	private org.lgna.project.ast.AbstractStatementWithBody replacement;
	public ConvertStatementWithBodyActionOperation( java.util.UUID individualId, org.lgna.project.ast.StatementListProperty property, org.lgna.project.ast.AbstractStatementWithBody original, org.lgna.project.ast.AbstractStatementWithBody replacement ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, individualId );
		this.property = property;
		this.original = original;
		this.replacement = replacement;
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		final int index = this.property.indexOf( this.original );
		final org.lgna.project.ast.BlockStatement body = this.original.body.getValue();
		if( index >= 0 ) {
			step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
				@Override
				protected final void doOrRedoInternal( boolean isDo ) {
					property.remove( index );
					original.body.setValue( null );
					replacement.body.setValue( body );
					property.add( index, replacement );
					//todo: remove
					org.alice.ide.IDE.getActiveInstance().refreshUbiquitousPane();
				}
				@Override
				protected final void undoInternal() {
					property.remove( index );
					replacement.body.setValue( null );
					original.body.setValue( body );
					property.add( index, original );
					//todo: remove
					org.alice.ide.IDE.getActiveInstance().refreshUbiquitousPane();
				}
				@Override
				protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
					rv.append( "convert:" );
					org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, original, locale);
					rv.append( " --> " );
					org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, replacement, locale);
					return rv;
				}
			} );
		} else {
			throw new RuntimeException();
		}
	}
}
class ConvertDoInOrderToDoTogetherActionOperation extends ConvertStatementWithBodyActionOperation {
	public ConvertDoInOrderToDoTogetherActionOperation( org.lgna.project.ast.StatementListProperty property, org.lgna.project.ast.DoInOrder doInOrder ) {
		super( java.util.UUID.fromString( "d3abb3c6-f016-4687-be00-f0921de7cb39" ), property, doInOrder, new org.lgna.project.ast.DoTogether() );
	}
	@Override
	protected void localize() {
		super.localize();
		this.setName( "Convert To DoTogether" );
	}
}
class ConvertDoTogetherToDoInOrderActionOperation extends ConvertStatementWithBodyActionOperation {
	public ConvertDoTogetherToDoInOrderActionOperation( org.lgna.project.ast.StatementListProperty property, org.lgna.project.ast.DoTogether doTogether ) {
		super( java.util.UUID.fromString( "14aec49f-ae07-4a4c-9c0b-73c5533d514f" ), property, doTogether, new org.lgna.project.ast.DoInOrder() );
	}
	@Override
	protected void localize() {
		super.localize();
		this.setName( "Convert To DoInOrder" );
	}
}

class DissolveStatementActionOperation extends org.lgna.croquet.ActionOperation {
	private org.lgna.project.ast.StatementListProperty property;
	private org.lgna.project.ast.AbstractStatementWithBody abstractStatementWithBody;
	public DissolveStatementActionOperation( org.lgna.project.ast.StatementListProperty property, org.lgna.project.ast.AbstractStatementWithBody abstractStatementWithBody ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "b48d1d87-9dbf-4fc5-bb07-daa56ae6bd7d" ) );
		this.property = property;
		this.abstractStatementWithBody = abstractStatementWithBody;
		this.setName( "Dissolve " + this.abstractStatementWithBody.getClass().getSimpleName() );
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		final int index = this.property.indexOf( this.abstractStatementWithBody );
		if( index >= 0 ) {
			final int N = this.abstractStatementWithBody.body.getValue().statements.size();

			final org.lgna.project.ast.Statement[] statements = new org.lgna.project.ast.Statement[ N ];
			this.abstractStatementWithBody.body.getValue().statements.toArray( statements );
			
			step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
				@Override
				protected final void doOrRedoInternal( boolean isDo ) {
					property.remove( index );
					property.add( index, statements );
					//todo: remove
					org.alice.ide.IDE.getActiveInstance().refreshUbiquitousPane();
				}
				@Override
				protected final void undoInternal() {
					for( int i=0; i<N; i++ ) {
						property.remove( index );
					}
					property.add( index, abstractStatementWithBody );
					//todo: remove
					org.alice.ide.IDE.getActiveInstance().refreshUbiquitousPane();
				}
				@Override
				protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
					rv.append( "dissolve:" );
					org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, abstractStatementWithBody, locale);
					return rv;
				}
			} );
		} else {
			throw new RuntimeException();
		}
	}
}

class DeleteStatementActionOperation extends org.lgna.croquet.ActionOperation {
	private org.lgna.project.ast.StatementListProperty property;
	private org.lgna.project.ast.Statement statement;

	public DeleteStatementActionOperation( org.lgna.project.ast.StatementListProperty property, org.lgna.project.ast.Statement statement ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "c2b2810b-68ad-4935-b47f-458fe90f877b" ) );
		this.property = property;
		this.statement = statement;
		StringBuffer sb = new StringBuffer();
		sb.append( "Delete " );
		if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
			sb.append( "Statement" );
		} else if( statement instanceof org.lgna.project.ast.ConditionalStatement ) {
			sb.append( "If/Else" );
		} else {
			sb.append( this.statement.getClass().getSimpleName() );
		}
		this.setName( sb.toString() );
	}
	@Override
	protected final void perform(org.lgna.croquet.history.ActionOperationStep step) {
		final int index = this.property.indexOf( this.statement );
		if( index >= 0 ) {
			step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
				@Override
				protected final void doOrRedoInternal( boolean isDo ) {
					property.remove( index );
					//todo: remove
					org.alice.ide.IDE.getActiveInstance().refreshUbiquitousPane();
				}
				@Override
				protected final void undoInternal() {
					property.add( index, statement );
					//todo: remove
					org.alice.ide.IDE.getActiveInstance().refreshUbiquitousPane();
				}
				@Override
				protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
					rv.append( "delete:" );
					org.lgna.project.ast.NodeUtilities.safeAppendRepr(rv, statement, locale);
					return rv;
				}
			} );
		} else {
			throw new RuntimeException();
		}
	}
}

class StatementEnabledStateOperation extends org.lgna.croquet.BooleanState {
	private org.lgna.project.ast.Statement statement;

	public StatementEnabledStateOperation( org.lgna.project.ast.Statement statement ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "d0199421-49e6-49eb-9307-83db77dfa28b" ), statement.isEnabled.getValue() );
		this.statement = statement;
		this.addValueObserver( new ValueObserver<Boolean>() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				StatementEnabledStateOperation.this.statement.isEnabled.setValue( nextValue );
			}
		} );
		this.setTextForBothTrueAndFalse( "IsEnabled" );
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
}

/**
 * @author Dennis Cosgrove
 */
public class Factory extends org.alice.ide.common.Factory {
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createArgumentListPropertyPane( org.lgna.project.ast.ArgumentListProperty argumentListProperty ) {
		return new ArgumentListPropertyPane( this, argumentListProperty );
	}
	public org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.croquet.components.Component< ? > prefixPane, org.lgna.project.ast.AbstractType<?,?,?> desiredValueType, org.lgna.croquet.Group group, boolean isBogus ) {
		org.lgna.project.ast.Expression expression = expressionProperty.getValue();
		org.lgna.croquet.components.JComponent< ? > rv = new org.alice.ide.common.ExpressionPropertyPane( this, expressionProperty );
		if( org.alice.ide.IDE.getActiveInstance().isDropDownDesiredFor( expression ) ) {
			org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade model;
			if( isBogus ) {
				model = org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade.EPIC_HACK_createInstance( group, expressionProperty, desiredValueType );
			} else {
				model = org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade.getInstance( group, expressionProperty, desiredValueType );
			}
			ExpressionPropertyDropDownPane expressionPropertyDropDownPane = new ExpressionPropertyDropDownPane( model.getRoot().getPopupPrepModel(), prefixPane, rv, expressionProperty );
			rv = expressionPropertyDropDownPane;
		}
		return rv;
	}
	@Override
	public org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.croquet.components.Component< ? > prefixPane, org.lgna.project.ast.AbstractType<?,?,?> desiredValueType ) {
		return this.createExpressionPropertyPane( expressionProperty, prefixPane, desiredValueType, org.alice.ide.IDE.PROJECT_GROUP, false );
	}
	@Override
	public org.alice.ide.common.AbstractStatementPane createStatementPane( org.lgna.project.ast.Statement statement, org.lgna.project.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane abstractStatementPane = super.createStatementPane( statement, statementListProperty );
		abstractStatementPane.setDragModel( new org.alice.ide.croquet.models.ToDoDragModel() );
		abstractStatementPane.setPopupPrepModel( new org.lgna.croquet.PredeterminedMenuModel(
				java.util.UUID.fromString( "6190553d-309e-453f-b9eb-ded8aaf7ce63" ),
				this.createPopupOperations( abstractStatementPane ) 
		).getPopupPrepModel() );
		return abstractStatementPane;
	}
	protected java.util.List< org.lgna.croquet.StandardMenuItemPrepModel > updatePopupOperations( java.util.List< org.lgna.croquet.StandardMenuItemPrepModel > rv, org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		org.lgna.project.ast.StatementListProperty property = abstractStatementPane.getOwner();
		org.lgna.project.ast.Statement statement = abstractStatementPane.getStatement();
		if( statement instanceof org.lgna.project.ast.Comment ) {
			//pass
		} else {
			rv.add( new StatementEnabledStateOperation( statement ).getMenuItemPrepModel() );
		}
		if( statement instanceof org.lgna.project.ast.ExpressionStatement ) {
			org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
			org.lgna.project.ast.Expression expression = expressionStatement.expression.getValue();
			if( expression instanceof org.lgna.project.ast.MethodInvocation ) {
				org.lgna.project.ast.MethodInvocation methodInvocation = (org.lgna.project.ast.MethodInvocation)expression;
				org.lgna.project.ast.AbstractMethod method = methodInvocation.method.getValue();
				if( method instanceof org.lgna.project.ast.UserMethod ) {
					rv.add( org.alice.ide.operations.ast.FocusCodeOperation.getInstance( method ).getMenuItemPrepModel() );
				}
			}
		}
		rv.add( org.lgna.croquet.MenuModel.SEPARATOR );
		rv.add( new DeleteStatementActionOperation( property, statement ).getMenuItemPrepModel() );
		if( statement instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
			org.lgna.project.ast.AbstractStatementWithBody abstractStatementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)statement;
			rv.add( new DissolveStatementActionOperation( property, abstractStatementWithBody ).getMenuItemPrepModel() );
			if( abstractStatementWithBody instanceof org.lgna.project.ast.DoInOrder ) {
				org.lgna.project.ast.DoInOrder doInOrder = (org.lgna.project.ast.DoInOrder)abstractStatementWithBody;
				rv.add( new ConvertDoInOrderToDoTogetherActionOperation( property, doInOrder ).getMenuItemPrepModel() );
			} else if( abstractStatementWithBody instanceof org.lgna.project.ast.DoTogether ) {
				org.lgna.project.ast.DoTogether doTogether = (org.lgna.project.ast.DoTogether)abstractStatementWithBody;
				rv.add( new ConvertDoTogetherToDoInOrderActionOperation( property, doTogether ).getMenuItemPrepModel() );
			}
		} else if( statement instanceof org.lgna.project.ast.ConditionalStatement ) {
			org.lgna.project.ast.ConditionalStatement conditionalStatement = (org.lgna.project.ast.ConditionalStatement)statement;
			//todo: dissolve to if, dissolve to else
		}
		return rv;
	}
	private java.util.List< org.lgna.croquet.StandardMenuItemPrepModel > createPopupOperations( org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		return this.updatePopupOperations( new java.util.LinkedList< org.lgna.croquet.StandardMenuItemPrepModel >(), abstractStatementPane );
	}

}
