package org.alice.ide.editorstabbedpane;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;;
/**
* @author Dennis Cosgrove
*/
public class EditFieldsPane extends edu.cmu.cs.dennisc.croquet.KInputPane< Boolean > {
	private edu.cmu.cs.dennisc.croquet.KFauxList< FieldDeclaredInAlice > fauxList;
	private edu.cmu.cs.dennisc.zoot.list.EditableListPane< FieldDeclaredInAlice > editableListPane;
	public EditFieldsPane( final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.fauxList = new edu.cmu.cs.dennisc.croquet.KFauxList< FieldDeclaredInAlice >() {
			@Override
			protected java.awt.Component createComponent(FieldDeclaredInAlice fieldDeclaredInAlice) {
				return new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), fieldDeclaredInAlice );
			}
			@Override
			protected edu.cmu.cs.dennisc.croquet.KFauxListItem< FieldDeclaredInAlice > createFauxListItem() {
				return new edu.cmu.cs.dennisc.croquet.KFauxListItem< FieldDeclaredInAlice >() {
					private javax.swing.JComponent component = new edu.cmu.cs.dennisc.croquet.swing.BorderPane();
					public java.awt.Component getComponent() {
						return this.component;
					}
					public void update( java.awt.Component contents, int index, boolean isSelected, boolean cellHasFocus ) {
						java.awt.Color background;
						if( isSelected ) {
							background = new java.awt.Color( 127, 127, 255 );
						} else {
							background = java.awt.Color.WHITE;
						}
						this.component.setBackground( background );
						this.component.setOpaque( true );
						this.component.removeAll();
						this.component.add( contents, java.awt.BorderLayout.CENTER );
					}
				};
			}
			@Override
			protected java.awt.Component getComponentForNull() {
				return new javax.swing.JLabel( "null" );
			}
		};
		
		
		final javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();
		
		for( FieldDeclaredInAlice field : declaringType.fields ) {
			listModel.addElement( field );
		}
		
		this.fauxList.setModel( listModel );
		class EditableFieldListPane extends edu.cmu.cs.dennisc.zoot.list.EditableListPane< FieldDeclaredInAlice > {
			public EditableFieldListPane() {
				super( org.alice.ide.IDE.PROJECT_GROUP, fauxList );
			}
			@Override
			protected FieldDeclaredInAlice create() {
				org.alice.ide.createdeclarationpanes.CreateFieldPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldPane( declaringType );
				return createFieldPane.showInJDialog( org.alice.ide.IDE.getSingleton() );
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
				fauxList.setValueIsAdjusting( true );
				listModel.setElementAt( fieldDeclaredInAlice0, index );
				fauxList.setValueIsAdjusting( false );
				listModel.setElementAt( fieldDeclaredInAlice1, index+1 );
			}
		}
		this.editableListPane = new EditableFieldListPane();
		this.add( this.editableListPane );
	}
	@Override
	protected Boolean getActualInputValue() {
		return Boolean.TRUE;
	}
}
