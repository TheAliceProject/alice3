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
package org.alice.ide.memberseditor;

import org.alice.ide.memberseditor.templates.TemplateFactory;

/**
 * @author Dennis Cosgrove
 */
public class TypeFieldsPane extends AbstractTypeMembersPane {
	public TypeFieldsPane( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		super( type );
	}
	@Override
	protected edu.cmu.cs.dennisc.property.ListProperty< ? extends org.lgna.project.ast.UserMember >[] getListPropertiesToListenTo( org.lgna.project.ast.NamedUserType type ) {
		return new edu.cmu.cs.dennisc.property.ListProperty[] { type.fields };
	}
	@Override
	protected org.lgna.croquet.components.Button createDeclareMemberButton( org.lgna.project.ast.NamedUserType type ) {
		return org.alice.ide.croquet.models.declaration.UnmanagedFieldDeclarationOperation.getInstance( type ).createButton();
	}
	@Override
	protected org.lgna.croquet.components.Button createEditConstructorButton( org.lgna.project.ast.NamedUserType type ) {
		return null;
	}
	@Override
	protected java.lang.Iterable< org.lgna.croquet.components.Component< ? >> createTemplates( org.lgna.project.ast.JavaGetterSetterPair getterSetterPair ) {
		java.util.List< org.lgna.croquet.components.Component< ? > > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		rv.add( org.alice.ide.memberseditor.templates.TemplateFactory.getFunctionInvocationTemplate( getterSetterPair.getGetter() ) );
		org.lgna.project.ast.AbstractMethod setter = getterSetterPair.getSetter();
		if( setter != null ) {
			rv.add( org.alice.ide.memberseditor.templates.TemplateFactory.getProcedureInvocationTemplate( (org.lgna.project.ast.AbstractMethod)setter.getShortestInChain() ) );
		}
		return rv;
	}
	@Override
	protected Iterable< org.lgna.croquet.components.Component< ? > > createTemplates( org.lgna.project.ast.AbstractMember member ) {
		java.util.List< org.lgna.croquet.components.Component< ? > > rv;
		if( member instanceof org.lgna.project.ast.AbstractField ) {
			rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)member;
			if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
				//pass
			} else {
				if( field instanceof org.lgna.project.ast.UserField ) {
					org.lgna.project.ast.UserField fieldInAlice = (org.lgna.project.ast.UserField)field;
					org.lgna.croquet.components.Component<?> declarationPane = new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.x.TemplateAstI18nFactory.getInstance(), fieldInAlice);
					edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: add popup menu to field declaration pane" );
////					class EditFieldDeclarationOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation { 
////						public EditFieldDeclarationOperation() {
////							super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "cb8936e6-a011-427a-bc64-0a4e646dc869" ) );
////							this.setName( "Edit..." );
////						}
////						@Override
////						protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
////							edu.cmu.cs.dennisc.croquet.Application.getSingleton().showMessageDialog( "todo" );
////						}
////					}
//					rv.add( new org.lgna.croquet.components.LineAxisPanel(
//								new org.alice.ide.operations.ast.EditFieldOperation( fieldInAlice ).createButton(),
//								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 8 ),
//								declarationPane
//							) 
//					);
				}
			}
			rv.add( TemplateFactory.getAccessorTemplate( field ) );
			if( field.getValueType().isArray() ) {
				rv.add( TemplateFactory.getAccessArrayAtIndexTemplate( field ) );
				rv.add( TemplateFactory.getArrayLengthTemplate( field ) );
			}
			if( field.isFinal() ) {
				//pass
			} else {
				rv.add( 
						new org.lgna.croquet.components.LineAxisPanel( 
								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( org.alice.ide.common.ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT ),
								TemplateFactory.getMutatorTemplate( field )
						)
				);
			}
			
			if( field.getValueType().isArray() ) {
				rv.add( 
						new org.lgna.croquet.components.LineAxisPanel( 
								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( org.alice.ide.common.ExpressionLikeSubstance.DOCKING_BAY_INSET_LEFT ),
								TemplateFactory.getMutateArrayAtIndexTemplate( field )
						)
				);
			}
			
			if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
				//pass
			} else {
				rv.add( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 16 ) );
			}
		} else {
			rv = null;
		}
		return rv;
	}
}
