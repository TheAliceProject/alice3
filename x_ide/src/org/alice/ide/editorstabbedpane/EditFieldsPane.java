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
		
		final javax.swing.DefaultListModel defaultListModel = new javax.swing.DefaultListModel();
		for( FieldDeclaredInAlice field : declaringType.fields ) {
			defaultListModel.addElement( field );
		}
		this.list.setModel( defaultListModel );

		class EditableFieldListPane extends edu.cmu.cs.dennisc.zoot.list.EditableListPane< FieldDeclaredInAlice > {
			public EditableFieldListPane() {
				super( org.alice.ide.IDE.PROJECT_GROUP, list, defaultListModel, list.getSelectionModel() );
			}
			@Override
			protected FieldDeclaredInAlice createItem() throws Exception {
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
			protected edu.cmu.cs.dennisc.zoot.Edit createEditEdit() {
				javax.swing.JOptionPane.showMessageDialog( this, "todo" );
				return null;
			}
			@Override
			protected void setValueIsAdjusting( boolean isValueAdjusting ) {
				EditFieldsPane.this.list.setValueIsAdjusting( isValueAdjusting );
			}
			@Override
			protected boolean isEnabledAtAll() {
				return true;
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
