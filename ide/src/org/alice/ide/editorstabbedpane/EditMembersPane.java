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
package org.alice.ide.editorstabbedpane;

import edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice;;
/**
* @author Dennis Cosgrove
*/
public abstract class EditMembersPane< T extends MemberDeclaredInAlice > extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType;
	private edu.cmu.cs.dennisc.croquet.List< T > list;
	public EditMembersPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, edu.cmu.cs.dennisc.property.ListProperty< T > listProperty ) {
		this.declaringType = declaringType;
		this.list = new edu.cmu.cs.dennisc.croquet.List< T >();
		this.list.setRenderer( new edu.cmu.cs.dennisc.javax.swing.renderers.ContentsCachingListCellRenderer< T >() {
			@Override
			protected java.awt.Component createComponent(T value) {
				return createCellRendererComponent( value );
			}
			@Override
			public void update( java.awt.Component contents, int index, boolean isSelected, boolean cellHasFocus ) {
				this.setOpaque( isSelected );
				this.removeAll();
				this.add( contents );
			}
		} );

		final edu.cmu.cs.dennisc.javax.swing.models.ListPropertyListModel< T > listModel = edu.cmu.cs.dennisc.javax.swing.models.ListPropertyListModel.createInstance( listProperty );
		
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: list.getAwtComponent().setModel" );
		list.getAwtComponent().setModel( listModel );
		
		
		class EditableMemberListPane extends AbstractEditableListPane< T > {
			public EditableMemberListPane() {
				super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID, list );
			}
			@Override
			protected edu.cmu.cs.dennisc.croquet.AbstractActionOperation createEditOperation( java.util.UUID groupUUID ) {
				return EditMembersPane.this.createEditOperation( groupUUID, "Edit..." );
			}
			@Override
			protected void add( int index, T item ) {
				listModel.add( index, item );
			}
			@Override
			protected void remove( int index, T item ) {
				listModel.remove( index, item );
			}
			@Override
			protected void setItemsAt( int index, T item0, T item1 ) {
				listModel.set( index, item0, item1 );
			}
			@Override
			protected boolean isRemoveItemEnabled( int index ) {
				if( index != -1 ) {
					T e = listModel.getElementAt( index );
					if( e != null ) {
						return isRemoveItemEnabledFor( e );
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			@Override
			protected boolean isEditItemEnabled( int index ) {
				if( index != -1 ) {
					T e = listModel.getElementAt( index );
					if( e != null ) {
						return isEditItemEnabledFor( e );
					} else {
						return false;
					}
				} else {
					return false;
				}
			}
			@Override
			protected T createItem() throws Exception {
				return createMember( EditMembersPane.this.declaringType );
			}
		}
		AbstractEditableListPane< T > editableListPane = new EditableMemberListPane();
		editableListPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		this.addComponent( editableListPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
	}
	protected T getSelectedItem() {
		return (T)this.list.getSelectedValue();
	}
	protected abstract java.awt.Component createCellRendererComponent( T item );
	protected abstract edu.cmu.cs.dennisc.croquet.AbstractActionOperation createEditOperation( java.util.UUID groupUUID, String name );
	protected abstract T createMember( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType );
	protected abstract boolean isRemoveItemEnabledFor( T item );
	protected abstract boolean isEditItemEnabledFor( T item );
}
