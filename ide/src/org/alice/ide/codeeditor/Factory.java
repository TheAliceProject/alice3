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

abstract class ConvertStatementWithBodyActionOperation extends org.alice.ide.operations.ActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty property;
	private edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody original;
	private edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody replacement;
	public ConvertStatementWithBodyActionOperation( java.util.UUID individualId, edu.cmu.cs.dennisc.alice.ast.StatementListProperty property, edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody original, edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody replacement ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, individualId );
		this.property = property;
		this.original = original;
		this.replacement = replacement;
	}
	@Override
	protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		final int index = this.property.indexOf( this.original );
		final edu.cmu.cs.dennisc.alice.ast.BlockStatement body = this.original.body.getValue();
		if( index >= 0 ) {
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit( context ) {
				@Override
				public void doOrRedo( boolean isDo ) {
					property.remove( index );
					original.body.setValue( null );
					replacement.body.setValue( body );
					property.add( index, replacement );
					//todo: remove
					getIDE().refreshUbiquitousPane();
				}
				@Override
				public void undo() {
					property.remove( index );
					replacement.body.setValue( null );
					original.body.setValue( body );
					property.add( index, original );
					//todo: remove
					getIDE().refreshUbiquitousPane();
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "convert:" );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, original, locale);
					rv.append( " --> " );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, replacement, locale);
					return rv;
				}
			} );
		} else {
			throw new RuntimeException();
		}
	}
}
class ConvertDoInOrderToDoTogetherActionOperation extends ConvertStatementWithBodyActionOperation {
	public ConvertDoInOrderToDoTogetherActionOperation( edu.cmu.cs.dennisc.alice.ast.StatementListProperty property, edu.cmu.cs.dennisc.alice.ast.DoInOrder doInOrder ) {
		super( java.util.UUID.fromString( "d3abb3c6-f016-4687-be00-f0921de7cb39" ), property, doInOrder, new edu.cmu.cs.dennisc.alice.ast.DoTogether() );
		this.setName( "Convert To DoTogether" );
	}
}
class ConvertDoTogetherToDoInOrderActionOperation extends ConvertStatementWithBodyActionOperation {
	public ConvertDoTogetherToDoInOrderActionOperation( edu.cmu.cs.dennisc.alice.ast.StatementListProperty property, edu.cmu.cs.dennisc.alice.ast.DoTogether doTogether ) {
		super( java.util.UUID.fromString( "14aec49f-ae07-4a4c-9c0b-73c5533d514f" ), property, doTogether, new edu.cmu.cs.dennisc.alice.ast.DoInOrder() );
		this.setName( "Convert To DoInOrder" );
	}
}

class DissolveStatementActionOperation extends org.alice.ide.operations.ActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty property;
	private edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody abstractStatementWithBody;
	public DissolveStatementActionOperation( edu.cmu.cs.dennisc.alice.ast.StatementListProperty property, edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody abstractStatementWithBody ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "b48d1d87-9dbf-4fc5-bb07-daa56ae6bd7d" ) );
		this.setName( "Dissolve " + abstractStatementWithBody.getClass().getSimpleName() );
		this.property = property;
		this.abstractStatementWithBody = abstractStatementWithBody;
	}
	@Override
	protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		final int index = this.property.indexOf( this.abstractStatementWithBody );
		if( index >= 0 ) {
			final int N = this.abstractStatementWithBody.body.getValue().statements.size();

			final edu.cmu.cs.dennisc.alice.ast.Statement[] statements = new edu.cmu.cs.dennisc.alice.ast.Statement[ N ];
			this.abstractStatementWithBody.body.getValue().statements.toArray( statements );
			
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit( context ) {
				@Override
				public void doOrRedo( boolean isDo ) {
					property.remove( index );
					property.add( index, statements );
					//todo: remove
					getIDE().refreshUbiquitousPane();
				}
				@Override
				public void undo() {
					for( int i=0; i<N; i++ ) {
						property.remove( index );
					}
					property.add( index, abstractStatementWithBody );
					//todo: remove
					getIDE().refreshUbiquitousPane();
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "dissolve:" );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, abstractStatementWithBody, locale);
					return rv;
				}
			} );
		} else {
			throw new RuntimeException();
		}
	}
}

class DeleteStatementActionOperation extends org.alice.ide.operations.ActionOperation {
	private edu.cmu.cs.dennisc.alice.ast.StatementListProperty property;
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;

	public DeleteStatementActionOperation( edu.cmu.cs.dennisc.alice.ast.StatementListProperty property, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "c2b2810b-68ad-4935-b47f-458fe90f877b" ) );
		StringBuffer sb = new StringBuffer();
		sb.append( "Delete " );
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ExpressionStatement ) {
			sb.append( "Statement" );
		} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalStatement ) {
			sb.append( "If/Else" );
		} else {
			sb.append( statement.getClass().getSimpleName() );
		}
		this.setName( sb.toString() );
		this.property = property;
		this.statement = statement;
	}
@Override
	protected final void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
		final int index = this.property.indexOf( this.statement );
		if( index >= 0 ) {
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit( context ) {
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
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "d0199421-49e6-49eb-9307-83db77dfa28b" ), statement.isEnabled.getValue(), "Is Enabled" );
		this.statement = statement;
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
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createArgumentListPropertyPane( edu.cmu.cs.dennisc.alice.ast.ArgumentListProperty argumentListProperty ) {
		return new ArgumentListPropertyPane( this, argumentListProperty );
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.JComponent< ? > createExpressionPropertyPane( edu.cmu.cs.dennisc.alice.ast.ExpressionProperty expressionProperty, edu.cmu.cs.dennisc.croquet.Component< ? > prefixPane, edu.cmu.cs.dennisc.alice.ast.AbstractType desiredValueType ) {
		edu.cmu.cs.dennisc.alice.ast.Expression expression = expressionProperty.getValue();
		edu.cmu.cs.dennisc.croquet.JComponent< ? > rv = new org.alice.ide.common.ExpressionPropertyPane( this, expressionProperty );
		if( org.alice.ide.IDE.getSingleton().isDropDownDesiredFor( expression ) ) {
			rv = new ExpressionPropertyDropDownPane( prefixPane, rv, expressionProperty, desiredValueType );
		}
		return rv;
	}
	@Override
	public org.alice.ide.common.AbstractStatementPane createStatementPane( edu.cmu.cs.dennisc.alice.ast.Statement statement, edu.cmu.cs.dennisc.alice.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane abstractStatementPane = super.createStatementPane( statement, statementListProperty );
		abstractStatementPane.setDragAndDropOperation( new org.alice.ide.operations.DefaultDragOperation( edu.cmu.cs.dennisc.alice.Project.GROUP ) );
		abstractStatementPane.setPopupMenuOperation( new edu.cmu.cs.dennisc.croquet.PopupMenuOperation(
				java.util.UUID.fromString( "6190553d-309e-453f-b9eb-ded8aaf7ce63" ),
				this.createPopupOperations( abstractStatementPane ) 
		) );
		return abstractStatementPane;
	}
	protected java.util.List< edu.cmu.cs.dennisc.croquet.Model > updatePopupOperations( java.util.List< edu.cmu.cs.dennisc.croquet.Model > rv, org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		edu.cmu.cs.dennisc.alice.ast.StatementListProperty property = abstractStatementPane.getOwner();
		edu.cmu.cs.dennisc.alice.ast.Statement statement = abstractStatementPane.getStatement();
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.Comment ) {
			//pass
		} else {
			rv.add( new StatementEnabledStateOperation( statement ) );
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
		rv.add( edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR );
		rv.add( new DeleteStatementActionOperation( property, statement ) );
		if( statement instanceof edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody abstractStatementWithBody = (edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)statement;
			rv.add( new DissolveStatementActionOperation( property, abstractStatementWithBody ) );
			if( abstractStatementWithBody instanceof edu.cmu.cs.dennisc.alice.ast.DoInOrder ) {
				edu.cmu.cs.dennisc.alice.ast.DoInOrder doInOrder = (edu.cmu.cs.dennisc.alice.ast.DoInOrder)abstractStatementWithBody;
				rv.add( new ConvertDoInOrderToDoTogetherActionOperation( property, doInOrder ) );
			} else if( abstractStatementWithBody instanceof edu.cmu.cs.dennisc.alice.ast.DoTogether ) {
				edu.cmu.cs.dennisc.alice.ast.DoTogether doTogether = (edu.cmu.cs.dennisc.alice.ast.DoTogether)abstractStatementWithBody;
				rv.add( new ConvertDoTogetherToDoInOrderActionOperation( property, doTogether ) );
			}
		} else if( statement instanceof edu.cmu.cs.dennisc.alice.ast.ConditionalStatement ) {
			edu.cmu.cs.dennisc.alice.ast.ConditionalStatement conditionalStatement = (edu.cmu.cs.dennisc.alice.ast.ConditionalStatement)statement;
			//todo: dissolve to if, dissolve to else
		}
		return rv;
	}
	private java.util.List< edu.cmu.cs.dennisc.croquet.Model > createPopupOperations( org.alice.ide.common.AbstractStatementPane abstractStatementPane ) {
		return this.updatePopupOperations( new java.util.LinkedList< edu.cmu.cs.dennisc.croquet.Model >(), abstractStatementPane );
	}

}
