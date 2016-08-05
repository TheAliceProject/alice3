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
package org.alice.ide.croquet.models.ast.cascade;

class AddExpressionBlank extends org.lgna.croquet.CascadeBlank<org.lgna.project.ast.Expression> {
	private final org.lgna.project.ast.DeclarationProperty<org.lgna.project.ast.AbstractType<?, ?, ?>> componentTypeProperty;

	public AddExpressionBlank( org.lgna.project.ast.DeclarationProperty<org.lgna.project.ast.AbstractType<?, ?, ?>> componentTypeProperty ) {
		super( java.util.UUID.fromString( "c8fa59ec-80ba-4776-8b56-4c0e23848a5d" ) );
		this.componentTypeProperty = componentTypeProperty;
	}

	@Override
	protected void updateChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> children, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		ide.getExpressionCascadeManager().appendItems( children, blankNode, this.componentTypeProperty.getValue(), null );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AddExpressionCascade extends ExpressionsCascade {
	private final org.lgna.project.ast.ExpressionListProperty expressionListProperty;

	public AddExpressionCascade( org.lgna.project.ast.DeclarationProperty<org.lgna.project.ast.AbstractType<?, ?, ?>> componentTypeProperty, org.lgna.project.ast.ExpressionListProperty expressionListProperty ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "4f3ccba2-c44f-49b3-b20e-c9e847e90db2" ), new AddExpressionBlank( componentTypeProperty ) );
		this.expressionListProperty = expressionListProperty;
	}

	@Override
	protected org.alice.ide.croquet.edits.ast.AddExpressionEdit createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> step, org.lgna.project.ast.Expression[] values ) {
		return new org.alice.ide.croquet.edits.ast.AddExpressionEdit( step, this.expressionListProperty, values[ 0 ] );
	}

	//	public org.lgna.project.ast.ExpressionListProperty getExpressionListProperty() {
	//		return this.expressionListProperty;
	//	}
	//	@Override
	//	protected org.lgna.project.ast.AbstractType< ?, ?, ? > getDesiredValueType() {
	//		return componentTypeProperty.getValue();
	//	}
	//	@Override
	//	public org.lgna.project.ast.Expression getPreviousExpression() {
	//		return null;
	//	}
	//	
	//	@Override
	//	protected edu.cmu.cs.dennisc.croquet.Group getItemGroup() {
	//		return edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP;
	//	}
	//
	//	@Override
	//	protected String getTitle() {
	//		return null;
	//	}
	////	@Override
	////	protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
	////		class AddExpressionEdit extends org.alice.ide.ToDoEdit {
	////			private org.lgna.project.ast.Expression expression;
	////			private int index;
	////			@Override
	////			protected final void doOrRedoInternal( boolean isDo ) {
	////				this.index = expressionListProperty.size();
	////				expressionListProperty.add( this.expression );
	////			}
	////			@Override
	////			protected final void undoInternal() {
	////				//expressionListProperty.indexOf( this.expression )
	////				expressionListProperty.remove( this.index );
	////			}
	////			@Override
	////			protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
	////				rv.append( "add: " );
	////				org.lgna.project.ast.Node.safeAppendRepr(rv, this.expression, locale);
	////				return rv;
	////			}
	////		}
	////		final edu.cmu.cs.dennisc.croquet.ViewController<?, ?> viewController = context.getViewController();
	////		final java.awt.Point p = context.getPoint();
	////		context.pend( new edu.cmu.cs.dennisc.croquet.PendResolver< AddExpressionEdit, org.lgna.project.ast.Expression >() {
	////			public AddExpressionEdit createEdit() {
	////				return new AddExpressionEdit();
	////			}
	////			public AddExpressionEdit initialize(AddExpressionEdit rv, edu.cmu.cs.dennisc.croquet.ModelContext context, java.util.UUID id, edu.cmu.cs.dennisc.task.TaskObserver<org.lgna.project.ast.Expression> taskObserver) {
	////				org.lgna.project.ast.AbstractType<?,?,?> type = componentTypeProperty.getValue();
	////				org.alice.ide.IDE.getActiveInstance().getCascadeManager().promptUserForExpression( type, rv.expression, viewController, p, taskObserver );
	////				return rv;
	////			}
	////			public AddExpressionEdit handleCompletion( AddExpressionEdit rv, org.lgna.project.ast.Expression expression ) {
	////				//todo: remove?
	////				org.alice.ide.IDE.getActiveInstance().getCascadeManager().unsetPreviousExpressionAndDropStatement();
	////				rv.expression = expression;
	////				return rv;
	////			}
	////			public void handleCancelation() {
	////				//todo: remove?
	////				org.alice.ide.IDE.getActiveInstance().getCascadeManager().unsetPreviousExpressionAndDropStatement();
	////			}
	////		} );
	////	}
}
