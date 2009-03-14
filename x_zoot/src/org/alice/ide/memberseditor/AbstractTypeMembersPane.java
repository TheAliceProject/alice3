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
abstract class AbstractTypeMembersPane extends swing.PageAxisPane {
	private static final int INDENT = 16;
	private static final int TYPE_PAD = 8;

	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private zoot.ZButton createAndAddMemberButton;
	private zoot.ZButton editConstructorButton;
	public AbstractTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.type = type;
		if( this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			this.createAndAddMemberButton = this.createCreateAndAddMemberButton( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type );
			this.editConstructorButton = this.createEditConstructorButton( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type );
		} else {
			this.createAndAddMemberButton = null;
			this.editConstructorButton = null;
		}
		this.refresh();
	}
	private static boolean isInclusionDesired( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)member;
			if( method.isStatic() ) {
				return false;
			}
		} else if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractField ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractField field = (edu.cmu.cs.dennisc.alice.ast.AbstractField)member;
			if( field.isStatic() ) {
				return false;
			}
		}
		if( member.isPublicAccess() || member.isDeclaredInAlice() ) {
			edu.cmu.cs.dennisc.alice.annotations.Visibility visibility = member.getVisibility();
			return visibility == null || visibility.equals( edu.cmu.cs.dennisc.alice.annotations.Visibility.PRIME_TIME );
		} else {
			return false;
		}
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract java.awt.Component[] createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member );

	protected abstract zoot.ZButton createCreateAndAddMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	protected abstract zoot.ZButton createEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	protected void refresh() {
		javax.swing.JComponent component = getIDE().getFactory().create( this, this.type );
		this.add( component );

		swing.PageAxisPane page = new swing.PageAxisPane();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
			if( isInclusionDesired( field ) ) {
				java.awt.Component[] templates = this.createTemplates( field );
				if( templates != null ) {
					for( java.awt.Component template : templates ) {
						page.add( javax.swing.Box.createVerticalStrut( 2 ) );
						page.add( template );
					}
				}
			}
		}
		for( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method : type.getDeclaredMethods() ) {
			if( isInclusionDesired( method ) ) {
				method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getShortestInChain();
				java.awt.Component[] templates = this.createTemplates( method );
				if( templates != null ) {
					for( java.awt.Component template : templates ) {
						page.add( javax.swing.Box.createVerticalStrut( 2 ) );
						page.add( template );
					}
				}
			}
		}
		if( this.createAndAddMemberButton != null ) {
			page.add( this.createAndAddMemberButton );
		}
		if( this.editConstructorButton != null ) {
			page.add( this.editConstructorButton );
		}
		this.add( new swing.LineAxisPane( javax.swing.Box.createHorizontalStrut( INDENT ), page ) );
		this.add( javax.swing.Box.createVerticalStrut( TYPE_PAD ) );
	}
}
