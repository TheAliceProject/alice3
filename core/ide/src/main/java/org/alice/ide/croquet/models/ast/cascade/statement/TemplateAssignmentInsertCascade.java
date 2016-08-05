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
package org.alice.ide.croquet.models.ast.cascade.statement;

import org.alice.ide.statementfactory.LocalArrayAtIndexAssignmentFillIn;
import org.alice.ide.statementfactory.LocalAssignmentFillIn;

/**
 * @author Dennis Cosgrove
 */
public class TemplateAssignmentInsertCascade extends org.lgna.croquet.CascadeWithInternalBlank<org.lgna.project.ast.Expression> {
	public static TemplateAssignmentInsertCascade createInstance( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		return new TemplateAssignmentInsertCascade( blockStatementIndexPair );
	}

	private final org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair;

	private TemplateAssignmentInsertCascade( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "4bdde6fa-166e-42a0-9843-3696955f5ed6" ), org.lgna.project.ast.Expression.class );
		this.blockStatementIndexPair = blockStatementIndexPair;
	}

	@Override
	protected org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> completionStep, org.lgna.project.ast.Expression[] values ) {
		return new org.alice.ide.croquet.edits.ast.InsertStatementEdit(
				completionStep,
				this.blockStatementIndexPair,
				new org.lgna.project.ast.ExpressionStatement( values[ 0 ] ) );
	}

	@Override
	protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.project.ast.Expression> blankNode ) {
		org.lgna.project.ast.AbstractType<?, ?, ?> selectedType = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue();
		if( selectedType != null ) {
			java.util.List<org.lgna.project.ast.UserField> nonFinalUserFields = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			for( org.lgna.project.ast.AbstractField field : selectedType.getDeclaredFields() ) {
				if( field instanceof org.lgna.project.ast.UserField ) {
					org.lgna.project.ast.UserField userField = (org.lgna.project.ast.UserField)field;
					if( userField.isFinal() ) {
						//pass
					} else {
						nonFinalUserFields.add( userField );
					}
				}
			}
			if( nonFinalUserFields.size() > 0 ) {
				rv.add( FieldsSeparatorModel.getInstance() );
				for( org.lgna.project.ast.UserField field : nonFinalUserFields ) {
					rv.add( FieldAssignmentFillIn.getInstance( field ) );
					if( field.getValueType().isArray() ) {
						rv.add( FieldArrayAtIndexAssignmentFillIn.getInstance( field ) );
					}
				}
			}
		}

		java.util.List<org.lgna.project.ast.UserLocal> nonFinalLocals = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( org.lgna.project.ast.UserLocal local : org.alice.ide.IDE.getActiveInstance().getExpressionCascadeManager().getAccessibleLocals( this.blockStatementIndexPair ) ) {
			if( local.isFinal.getValue() ) {
				//pass
			} else {
				nonFinalLocals.add( local );
			}
		}

		if( nonFinalLocals.size() > 0 ) {
			rv.add( VariablesSeparatorModel.getInstance() );
			for( org.lgna.project.ast.UserLocal local : nonFinalLocals ) {
				rv.add( LocalAssignmentFillIn.getInstance( local ) );
				org.lgna.project.ast.AbstractType<?, ?, ?> type = local.getValueType();
				if( type.isArray() ) {
					rv.add( LocalArrayAtIndexAssignmentFillIn.getInstance( local ) );
				}
			}
		}

		//todo: check nonFinalUserFields and nonFinalLocals instead?
		if( rv.size() == 0 ) {
			rv.add( NoVariablesOrFieldsAccessibleCancelFillIn.getInstance() );
		}

		return rv;
	}
}
