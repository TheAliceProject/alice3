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
package org.alice.ide.editorstabbedpane;

/**
* @author Dennis Cosgrove
*/
public abstract class EditMembersPane< E extends edu.cmu.cs.dennisc.alice.ast.MemberDeclaredInAlice > extends edu.cmu.cs.dennisc.croquet.KInputPane< Boolean > {
	public EditMembersPane( edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
		final javax.swing.JList list = new javax.swing.JList();
		list.setCellRenderer( new edu.cmu.cs.dennisc.javax.swing.ContentsCachingListCellRenderer< E >() {
			@Override
			protected java.awt.Component createComponent(E e) {
				return createCellRendererComponent( e  );
			}
			@Override
			public void update( java.awt.Component contents, int index, boolean isSelected, boolean cellHasFocus ) {
				this.setOpaque( isSelected );
				this.removeAll();
				this.add( contents );
			}
		} );
		
		final edu.cmu.cs.dennisc.javax.swing.ListPropertyListModel< E > listModel = edu.cmu.cs.dennisc.javax.swing.ListPropertyListModel.createInstance( listProperty );
		list.setModel( listModel );
		class EditableMemberListPane extends edu.cmu.cs.dennisc.zoot.list.AbstractEditableListPane< E > {
			public EditableMemberListPane() {
				super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID, list );
			}
			@Override
			protected edu.cmu.cs.dennisc.zoot.ActionOperation createEditOperation( java.util.UUID groupUUID ) {
				return EditMembersPane.this.createEditOperation( groupUUID, "Edit..." );
			}
			@Override
			protected void add( int index, E e ) {
				listModel.add( index, e );
			}
			@Override
			protected void remove( int index, E e ) {
				listModel.remove( index, e );
			}
			@Override
			protected void setItemsAt( int index, E e0, E e1 ) {
				listModel.set( index, e0, e1 );
			}
			@Override
			protected boolean isRemoveItemEnabled( int index ) {
				return true;
			}
			@Override
			protected boolean isEditItemEnabled( int index ) {
				return true;
			}
			@Override
			protected E createItem() throws Exception {
				return createMember();
			}
		}
		edu.cmu.cs.dennisc.zoot.list.AbstractEditableListPane< E > editableListPane = new EditableMemberListPane();
		editableListPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( editableListPane, java.awt.BorderLayout.CENTER );
	}
	@Override
	protected Boolean getActualInputValue() {
		return Boolean.TRUE;
	}
	protected abstract java.awt.Component createCellRendererComponent(E e);
	protected abstract edu.cmu.cs.dennisc.zoot.ActionOperation createEditOperation( java.util.UUID groupUUID, String name );
	protected abstract E createMember();
}
