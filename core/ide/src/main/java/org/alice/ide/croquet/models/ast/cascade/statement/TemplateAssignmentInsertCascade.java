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

import edu.cmu.cs.dennisc.java.util.Lists;
import org.alice.ide.IDE;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.alice.ide.croquet.edits.ast.InsertStatementEdit;
import org.alice.ide.statementfactory.LocalArrayAtIndexAssignmentFillIn;
import org.alice.ide.statementfactory.LocalAssignmentFillIn;
import org.lgna.croquet.Application;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.CascadeWithInternalBlank;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.imp.cascade.BlankNode;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.ExpressionStatement;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserLocal;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class TemplateAssignmentInsertCascade extends CascadeWithInternalBlank<Expression> {
	public static TemplateAssignmentInsertCascade createInstance( BlockStatementIndexPair blockStatementIndexPair ) {
		return new TemplateAssignmentInsertCascade( blockStatementIndexPair );
	}

	private final BlockStatementIndexPair blockStatementIndexPair;

	private TemplateAssignmentInsertCascade( BlockStatementIndexPair blockStatementIndexPair ) {
		super( Application.PROJECT_GROUP, UUID.fromString( "4bdde6fa-166e-42a0-9843-3696955f5ed6" ), Expression.class );
		this.blockStatementIndexPair = blockStatementIndexPair;
	}

	@Override
	protected Edit createEdit( UserActivity userActivity, Expression[] values ) {
		return new InsertStatementEdit( userActivity,
				this.blockStatementIndexPair,
				new ExpressionStatement( values[ 0 ] ) );
	}

	@Override
	protected List<CascadeBlankChild> updateBlankChildren( List<CascadeBlankChild> rv, BlankNode<Expression> blankNode ) {
		AbstractType<?, ?, ?> selectedType = IDE.getActiveInstance().getDocumentFrame().getTypeMetaState().getValue();
		if( selectedType != null ) {
			List<UserField> nonFinalUserFields = Lists.newLinkedList();
			for( AbstractField field : selectedType.getDeclaredFields() ) {
				if( field instanceof UserField ) {
					UserField userField = (UserField)field;
					if( userField.isFinal() ) {
						//pass
					} else {
						nonFinalUserFields.add( userField );
					}
				}
			}
			if( nonFinalUserFields.size() > 0 ) {
				rv.add( FieldsSeparatorModel.getInstance() );
				for( UserField field : nonFinalUserFields ) {
					rv.add( FieldAssignmentFillIn.getInstance( field ) );
					if( field.getValueType().isArray() ) {
						rv.add( FieldArrayAtIndexAssignmentFillIn.getInstance( field ) );
					}
				}
			}
		}

		List<UserLocal> nonFinalLocals = Lists.newLinkedList();
		for( UserLocal local : IDE.getActiveInstance().getExpressionCascadeManager().getAccessibleLocals( this.blockStatementIndexPair ) ) {
			if( local.isFinal.getValue() ) {
				//pass
			} else {
				nonFinalLocals.add( local );
			}
		}

		if( nonFinalLocals.size() > 0 ) {
			rv.add( VariablesSeparatorModel.getInstance() );
			for( UserLocal local : nonFinalLocals ) {
				rv.add( LocalAssignmentFillIn.getInstance( local ) );
				AbstractType<?, ?, ?> type = local.getValueType();
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
