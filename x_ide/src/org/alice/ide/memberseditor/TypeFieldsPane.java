/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
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
	protected zoot.ZButton createCreateAndAddMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return new zoot.ZButton( new org.alice.ide.operations.ast.CreateAndAddFieldOperation( type ) );
	}
	@Override
	protected zoot.ZButton createEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type ) {
		return null;
	}
	@Override
	protected java.awt.Component[] createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		java.awt.Component[] rv;
		if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
			java.util.List< java.awt.Component > components = new java.util.LinkedList< java.awt.Component >();
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)member;
			if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
				components.add( new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field) );
			}
			components.add( new org.alice.ide.memberseditor.templates.GetterTemplate( field ) );
			if( field.isFinal() ) {
				//pass
			} else {
				components.add( new org.alice.ide.memberseditor.templates.SetterTemplate( field ) );
			}
			rv = new java.awt.Component[ components.size() ];
			components.toArray( rv );
		} else {
			rv = null;
		}
		return rv;
	}
}
