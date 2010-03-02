package org.alice.ide.editorstabbedpane;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;;
/**
* @author Dennis Cosgrove
*/
public class EditFieldsPane extends edu.cmu.cs.dennisc.croquet.KInputPane< Boolean > {
	private javax.swing.JList list;
	private edu.cmu.cs.dennisc.zoot.list.EditableListPane< FieldDeclaredInAlice > editableListPane;
	public EditFieldsPane( final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.list = new javax.swing.JList();
		this.list.setCellRenderer( new edu.cmu.cs.dennisc.croquet.ContentsCachingListCellRenderer< FieldDeclaredInAlice >() {
			@Override
			protected java.awt.Component createComponent(FieldDeclaredInAlice fieldDeclaredInAlice) {
				return new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), fieldDeclaredInAlice );
			}
			@Override
			public void update( java.awt.Component contents, int index, boolean isSelected, boolean cellHasFocus ) {
				this.setOpaque( isSelected );
				this.removeAll();
				this.add( contents );
			}
		} );
		
		final javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
		for( FieldDeclaredInAlice field : declaringType.fields ) {
			listModel.addElement( field );
		}
		this.list.setModel( listModel );

		class EditableFieldListPane extends edu.cmu.cs.dennisc.zoot.list.EditableListPane< FieldDeclaredInAlice > {
			public EditableFieldListPane() {
				super( org.alice.ide.IDE.PROJECT_GROUP, list );
			}
			@Override
			protected FieldDeclaredInAlice create() throws Exception {
				org.alice.ide.createdeclarationpanes.CreateFieldPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldPane( declaringType );
				FieldDeclaredInAlice rv = createFieldPane.showInJDialog( org.alice.ide.IDE.getSingleton() );
				if( rv != null ) {
					//pass
				} else {
					throw new Exception();
				}
				return rv;
			}
			@Override
			protected boolean isEnabledAtAll() {
				return true;
			}

			@Override
			protected void add( int index, FieldDeclaredInAlice fieldDeclaredInAlice ) {
				listModel.add( index, fieldDeclaredInAlice );
			}
			@Override
			protected void remove( int index, FieldDeclaredInAlice fieldDeclaredInAlice ) {
				listModel.remove( index );
			}
			@Override
			protected void setItemsAt( int index, FieldDeclaredInAlice fieldDeclaredInAlice0, FieldDeclaredInAlice fieldDeclaredInAlice1 ) {
				list.setValueIsAdjusting( true );
				listModel.setElementAt( fieldDeclaredInAlice0, index );
				list.setValueIsAdjusting( false );
				listModel.setElementAt( fieldDeclaredInAlice1, index+1 );
			}
		}
		this.editableListPane = new EditableFieldListPane();
		this.editableListPane.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.editableListPane, java.awt.BorderLayout.CENTER );
	}
	@Override
	protected Boolean getActualInputValue() {
		return Boolean.TRUE;
	}
}
