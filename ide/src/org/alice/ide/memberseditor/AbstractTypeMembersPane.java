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
public abstract class AbstractTypeMembersPane extends org.lgna.croquet.components.PageAxisPanel {
	private static final int INDENT = 16;

	private org.lgna.project.ast.AbstractType<?,?,?> type;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener() {
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
	private org.lgna.croquet.State.ValueObserver<Boolean> isEmphasizingClassesObserver = new org.lgna.croquet.State.ValueObserver<Boolean>() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			AbstractTypeMembersPane.this.refresh();
		}
	};
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().addAndInvokeValueObserver( this.isEmphasizingClassesObserver );
	}
	@Override
	protected void handleUndisplayable() {
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().removeValueObserver( this.isEmphasizingClassesObserver );
		super.handleUndisplayable();
	}
	public AbstractTypeMembersPane( org.lgna.project.ast.AbstractType<?,?,?> type ) {
		this.type = type;
		if( this.type instanceof org.lgna.project.ast.TypeDeclaredInAlice ) {
			for( edu.cmu.cs.dennisc.property.ListProperty< ? extends org.lgna.project.ast.MemberDeclaredInAlice > listProperty : this.getListPropertiesToListenTo( (org.lgna.project.ast.TypeDeclaredInAlice)this.type ) ) {
				listProperty.addListPropertyListener( this.listPropertyAdapter );
			}
		}
		this.refresh();
	}
	protected abstract edu.cmu.cs.dennisc.property.ListProperty< ? extends org.lgna.project.ast.MemberDeclaredInAlice >[] getListPropertiesToListenTo( org.lgna.project.ast.TypeDeclaredInAlice type );
	private static boolean isInclusionDesired( org.lgna.project.ast.AbstractMember member ) {
		if( member instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)member;
			if( method.isStatic() ) {
				return false;
			}
		} else if( member instanceof org.lgna.project.ast.AbstractField ) {
			org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)member;
			if( field.isStatic() ) {
				return false;
			}
		}
		if( member.isPublicAccess() || member.isDeclaredInAlice() ) {
			org.lgna.project.annotations.Visibility visibility = member.getVisibility();
			return visibility == null || visibility.equals( org.lgna.project.annotations.Visibility.PRIME_TIME );
		} else {
			return false;
		}
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}
	protected abstract Iterable< org.lgna.croquet.components.Component< ? > > createTemplates( org.lgna.project.ast.AbstractMember member );

	protected abstract org.lgna.croquet.components.Button createDeclareMemberButton( org.lgna.project.ast.TypeDeclaredInAlice type );
	protected abstract org.lgna.croquet.components.Button createEditConstructorButton( org.lgna.project.ast.TypeDeclaredInAlice type );
	protected void refresh() {
		this.removeAllComponents();
		org.lgna.croquet.components.PageAxisPanel page = new org.lgna.croquet.components.PageAxisPanel();
		for( org.lgna.project.ast.AbstractField field : type.getDeclaredFields() ) {
			if( isInclusionDesired( field ) ) {
				Iterable< org.lgna.croquet.components.Component< ? > > templates = this.createTemplates( field );
				if( templates != null ) {
					for( org.lgna.croquet.components.Component< ? > template : templates ) {
						page.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 1 ) );
						page.addComponent( template );
					}
				}
			}
		}
		for( org.lgna.project.ast.AbstractMethod method : type.getDeclaredMethods() ) {
			if( isInclusionDesired( method ) ) {
				method = (org.lgna.project.ast.AbstractMethod)method.getShortestInChain();
				Iterable< org.lgna.croquet.components.Component< ? > > templates = this.createTemplates( method );
				if( templates != null ) {
					for( org.lgna.croquet.components.Component< ? > template : templates ) {
						page.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 1 ) );
						page.addComponent( template );
					}
				}
			}
		}
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() == false && this.type instanceof org.lgna.project.ast.TypeDeclaredInAlice ) {
			org.lgna.project.ast.TypeDeclaredInAlice typeInAlice = (org.lgna.project.ast.TypeDeclaredInAlice)type;
			org.lgna.croquet.components.Button createAndAddMemberButton = this.createDeclareMemberButton( typeInAlice );
			org.lgna.croquet.components.Button editConstructorButton = this.createEditConstructorButton( typeInAlice );
			if( createAndAddMemberButton != null ) {
				page.addComponent( createAndAddMemberButton );
			}
			if( editConstructorButton != null ) {
				page.addComponent( editConstructorButton );
			}
		}
		int pad;
		if( page.getComponentCount() > 0 ) {
			this.addComponent( new org.lgna.croquet.components.LineAxisPanel( 
					org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( INDENT ), 
					page 
			) );
			pad = 8;
		} else {
			pad = 2;
		}
		this.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( pad ) );
		this.revalidateAndRepaint();
	}
}
