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
public abstract class AbstractTypeMembersPane extends edu.cmu.cs.dennisc.croquet.PageAxisPanel {
	private static final int INDENT = 16;

	private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type;
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
	private edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver isEmphasizingClassesObserver = new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
		public void changing( boolean nextValue ) {
		}
		public void changed( boolean nextValue ) {
			AbstractTypeMembersPane.this.refresh();
		}
	};
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo( parent );
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().addAndInvokeValueObserver( this.isEmphasizingClassesObserver );
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().removeValueObserver( this.isEmphasizingClassesObserver );
		super.handleRemovedFrom( parent );
	}
	public AbstractTypeMembersPane( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> type ) {
		this.type = type;
		if( this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			for( edu.cmu.cs.dennisc.property.ListProperty< ? extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice > listProperty : this.getListPropertiesToListenTo( (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.type ) ) {
				listProperty.addListPropertyListener( this.listPropertyAdapter );
			}
		}
		this.refresh();
	}
	protected abstract edu.cmu.cs.dennisc.property.ListProperty< ? extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice >[] getListPropertiesToListenTo( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
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
	protected abstract Iterable< edu.cmu.cs.dennisc.croquet.Component< ? > > createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member );

	protected abstract edu.cmu.cs.dennisc.croquet.Button createDeclareMemberButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	protected abstract edu.cmu.cs.dennisc.croquet.Button createEditConstructorButton( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type );
	protected void refresh() {
		this.removeAllComponents();
		edu.cmu.cs.dennisc.croquet.PageAxisPanel page = new edu.cmu.cs.dennisc.croquet.PageAxisPanel();
		for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : type.getDeclaredFields() ) {
			if( isInclusionDesired( field ) ) {
				Iterable< edu.cmu.cs.dennisc.croquet.Component< ? > > templates = this.createTemplates( field );
				if( templates != null ) {
					for( edu.cmu.cs.dennisc.croquet.Component< ? > template : templates ) {
						page.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 1 ) );
						page.addComponent( template );
					}
				}
			}
		}
		for( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method : type.getDeclaredMethods() ) {
			if( isInclusionDesired( method ) ) {
				method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)method.getShortestInChain();
				Iterable< edu.cmu.cs.dennisc.croquet.Component< ? > > templates = this.createTemplates( method );
				if( templates != null ) {
					for( edu.cmu.cs.dennisc.croquet.Component< ? > template : templates ) {
						page.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( 1 ) );
						page.addComponent( template );
					}
				}
			}
		}
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() == false && this.type instanceof edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)type;
			edu.cmu.cs.dennisc.croquet.Button createAndAddMemberButton = this.createDeclareMemberButton( typeInAlice );
			edu.cmu.cs.dennisc.croquet.Button editConstructorButton = this.createEditConstructorButton( typeInAlice );
			if( createAndAddMemberButton != null ) {
				page.addComponent( createAndAddMemberButton );
			}
			if( editConstructorButton != null ) {
				page.addComponent( editConstructorButton );
			}
		}
		int pad;
		if( page.getComponentCount() > 0 ) {
			this.addComponent( new edu.cmu.cs.dennisc.croquet.LineAxisPanel( 
					edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( INDENT ), 
					page 
			) );
			pad = 8;
		} else {
			pad = 2;
		}
		this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createVerticalSliver( pad ) );
		this.revalidateAndRepaint();
	}
}
