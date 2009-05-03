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

	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener () {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
			AbstractTypeMembersPane.this.refresh();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
			AbstractTypeMembersPane.this.refresh();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
			AbstractTypeMembersPane.this.refresh();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
			AbstractTypeMembersPane.this.refresh();
		}
	};
	private org.alice.ide.common.TypeComponent typeComponent;
	public AbstractTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		this.type = type;
		this.typeComponent = new org.alice.ide.common.TypeComponent( this.type );
		if( this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			for( edu.cmu.cs.dennisc.property.ListProperty listProperty : this.getListPropertiesToListenTo( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.type ) ) {
				listProperty.addListPropertyListener( this.listPropertyAdapter );
			}
		}
		this.refresh();
	}
	protected abstract edu.cmu.cs.dennisc.property.ListProperty< ? >[] getListPropertiesToListenTo( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
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

	protected abstract zoot.ZButton createDeclareMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	protected abstract zoot.ZButton createEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	protected void refresh() {
		this.removeAll();
		this.add( this.typeComponent );
		swing.PageAxisPane page = new swing.PageAxisPane();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
			if( isInclusionDesired( field ) ) {
				java.awt.Component[] templates = this.createTemplates( field );
				if( templates != null ) {
					for( java.awt.Component template : templates ) {
						page.add( javax.swing.Box.createVerticalStrut( 1 ) );
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
						page.add( javax.swing.Box.createVerticalStrut( 1 ) );
						page.add( template );
					}
				}
			}
		}
		if( getIDE().isEmphasizingClasses() == false && this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
			zoot.ZButton createAndAddMemberButton = this.createDeclareMemberButton( typeInAlice );
			zoot.ZButton editConstructorButton = this.createEditConstructorButton( typeInAlice );
			if( createAndAddMemberButton != null ) {
				page.add( createAndAddMemberButton );
			}
			if( editConstructorButton != null ) {
				page.add( editConstructorButton );
			}
		}
		int pad;
		if( page.getComponentCount() > 0 ) {
			this.add( new swing.LineAxisPane( javax.swing.Box.createHorizontalStrut( INDENT ), page ) );
			pad = 8;
		} else {
			pad = 2;
		}
		this.add( javax.swing.Box.createVerticalStrut( pad ) );
		this.revalidate();
		this.repaint();
	}
}
