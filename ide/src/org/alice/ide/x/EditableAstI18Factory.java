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

package org.alice.ide.x;

/**
 * @author Dennis Cosgrove
 */
public class EditableAstI18Factory extends AstI18nFactory {
	private static class SingletonHolder {
		private static EditableAstI18Factory projectGroupInstance = new EditableAstI18Factory( org.alice.ide.IDE.PROJECT_GROUP );
		private static EditableAstI18Factory inheritGroupInstance = new EditableAstI18Factory( org.alice.ide.IDE.INHERIT_GROUP );
	}
	public static EditableAstI18Factory getProjectGroupInstance() {
		return SingletonHolder.projectGroupInstance;
	}
	public static EditableAstI18Factory getInheritGroupInstance() {
		return SingletonHolder.inheritGroupInstance;
	}
	private final org.lgna.croquet.Group group;
	private EditableAstI18Factory( org.lgna.croquet.Group group ) {
		this.group = group;
	}
	
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createKeyedArgumentListPropertyPane( org.lgna.project.ast.KeyedArgumentListProperty argumentListProperty ) {
		org.lgna.project.ast.ArgumentOwner owner = argumentListProperty.getOwner();
		org.lgna.project.ast.DeclarationProperty< ? extends org.lgna.project.ast.AbstractCode > codeProperty = owner.getParameterOwnerProperty();
		org.lgna.project.ast.AbstractCode code = codeProperty.getValue();
		if( code.getKeyedParameter() != null ) {
			return new org.alice.ide.x.components.KeyedArgumentListPropertyView( this, argumentListProperty );
		} else {
			return new org.lgna.croquet.components.Label();
		}
		
	}

	@Override
	protected org.lgna.croquet.components.JComponent< ? > createIdeExpressionPane( org.alice.ide.ast.IdeExpression ideExpression ) {
		throw new RuntimeException( ideExpression.toString() );
	}
	
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createTypeComponent( org.lgna.project.ast.AbstractType< ?, ?, ? > type ) {
		return org.alice.ide.common.TypeComponent.createInstance( type );
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createSimpleArgumentListPropertyPane( org.lgna.project.ast.SimpleArgumentListProperty argumentListProperty ) {
		return new org.alice.ide.codeeditor.ArgumentListPropertyPane( this, argumentListProperty );
	}
	@Override
	public org.lgna.croquet.components.JComponent< ? > createExpressionPropertyPane( org.lgna.project.ast.ExpressionProperty expressionProperty, org.lgna.project.ast.AbstractType<?,?,?> desiredValueType ) {
		org.lgna.project.ast.Expression expression = expressionProperty.getValue();
		org.lgna.croquet.components.JComponent< ? > rv = new org.alice.ide.x.components.ExpressionPropertyView( this, expressionProperty );
		if( org.alice.ide.IDE.getActiveInstance().isDropDownDesiredFor( expression ) ) {
			org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade model;
			if( this.group == org.lgna.croquet.Application.INHERIT_GROUP ) {
				model = org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade.EPIC_HACK_createInstance( group, expressionProperty, desiredValueType );
			} else {
				model = org.alice.ide.croquet.models.ast.DefaultExpressionPropertyCascade.getInstance( group, expressionProperty, desiredValueType );
			}
			org.alice.ide.codeeditor.ExpressionPropertyDropDownPane expressionPropertyDropDownPane = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane( model.getRoot().getPopupPrepModel(), null, rv, expressionProperty );
			rv = expressionPropertyDropDownPane;
		}
		return rv;
	}
	@Override
	public org.alice.ide.common.AbstractStatementPane createStatementPane( org.lgna.croquet.DragModel dragModel, org.lgna.project.ast.Statement statement, org.lgna.project.ast.StatementListProperty statementListProperty ) {
		org.alice.ide.common.AbstractStatementPane abstractStatementPane = super.createStatementPane( dragModel, statement, statementListProperty );
		abstractStatementPane.setPopupPrepModel( org.alice.ide.croquet.models.ast.StatementContextMenu.getInstance( statement ).getPopupPrepModel() );
		return abstractStatementPane;
	}
}
