package org.alice.ide.editorstabbedpane;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;;
/**
* @author Dennis Cosgrove
*/
public class EditFieldsPane extends edu.cmu.cs.dennisc.croquet.KInputPane< Boolean > {
	private javax.swing.JList list;
	private edu.cmu.cs.dennisc.zoot.list.AbstractEditableListPane< FieldDeclaredInAlice > editableListPane;
	public EditFieldsPane( final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.list = new javax.swing.JList();
		this.list.setCellRenderer( new edu.cmu.cs.dennisc.javax.swing.ContentsCachingListCellRenderer< FieldDeclaredInAlice >() {
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
		
		
		class ListPropertyListModel< E > extends javax.swing.AbstractListModel {
			private edu.cmu.cs.dennisc.property.ListProperty< E > listProperty;
			public ListPropertyListModel( edu.cmu.cs.dennisc.property.ListProperty< E > listProperty ) {
				this.listProperty = listProperty;
			}
			public int getSize() {
				return this.listProperty.size();
			}
			public E getElementAt( int index ) {
				return this.listProperty.get( index );
			}
			public void add( int index, E e ) {
				this.listProperty.add( index, e );
				this.fireIntervalAdded( this, index, index );
			}
			public void remove( int index, E e ) {
				this.listProperty.remove( index );
				this.fireIntervalRemoved( this, index, index );
			}
			public void set( int index, E... es ) {
				if( es.length > 0 ) {
					this.listProperty.set( index, es );
					this.fireContentsChanged( this, index, index+es.length-1 );
				}
			}
		}

		final ListPropertyListModel< FieldDeclaredInAlice > fieldsListModel = new ListPropertyListModel( declaringType.fields );
		this.list.setModel( fieldsListModel );

		class EditableFieldListPane extends edu.cmu.cs.dennisc.zoot.list.AbstractEditableListPane< FieldDeclaredInAlice > {
			public EditableFieldListPane() {
				super( org.alice.ide.IDE.PROJECT_GROUP, list );
			}
			@Override
			protected void add( int index, FieldDeclaredInAlice e ) {
				fieldsListModel.add( index, e );
			}
			@Override
			protected void remove( int index, FieldDeclaredInAlice e ) {
				fieldsListModel.remove( index, e );
			}
			@Override
			protected void setItemsAt( int index, FieldDeclaredInAlice e0, FieldDeclaredInAlice e1 ) {
				fieldsListModel.set( index, e0, e1 );
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
