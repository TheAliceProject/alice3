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

/**
 * @author Dennis Cosgrove
 */
class TypeFieldsPane extends AbstractTypeMembersPane {
	public TypeFieldsPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super( type );
	}
	@Override
	protected edu.cmu.cs.dennisc.property.ListProperty< ? >[] getListPropertiesToListenTo( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return new edu.cmu.cs.dennisc.property.ListProperty[] { type.fields };
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Button createDeclareMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return this.getIDE().createButton( new org.alice.ide.operations.ast.DeclareFieldOperation( type ) );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Button createEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return null;
	}
	@Override
	protected Iterable< edu.cmu.cs.dennisc.croquet.Component< ? > > createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? > > rv;
		if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
			rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)member;
			if( getIDE().isEmphasizingClasses() ) {
				//pass
			} else {
				if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
					rv.add( new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field) );
				}
			}
			rv.add( new org.alice.ide.memberseditor.templates.GetterTemplate( field ) );
			if( field.getValueType().isArray() ) {
				rv.add( new org.alice.ide.memberseditor.templates.AccessArrayAtIndexTemplate( field ) );
				rv.add( new org.alice.ide.memberseditor.templates.ArrayLengthTemplate( field ) );
			}
			if( field.isFinal() ) {
				//pass
			} else {
				rv.add( new org.alice.ide.memberseditor.templates.SetterTemplate( field ) );
			}
			
			if( field.getValueType().isArray() ) {
				rv.add( new org.alice.ide.memberseditor.templates.SetArrayAtIndexTemplate( field ) );
			}
			
			if( getIDE().isEmphasizingClasses() ) {
				//pass
			} else {
				rv.add( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalStrut( 16 ) );
			}
		} else {
			rv = null;
		}
		return rv;
	}
}
