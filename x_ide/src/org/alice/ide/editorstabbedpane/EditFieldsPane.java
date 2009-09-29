package org.alice.ide.editorstabbedpane;

class FieldDeclarationsPane extends edu.cmu.cs.dennisc.croquet.PageAxisPane {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType;
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > listPropertyAdapter = new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		@Override
		protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		@Override
		protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			FieldDeclarationsPane.this.refresh();
		}
	};
	public FieldDeclarationsPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.declaringType = declaringType;
		this.refresh();
	}
	@Override
	public void addNotify() {
		super.addNotify();
		this.declaringType.fields.addListPropertyListener( this.listPropertyAdapter );
	}
	@Override
	public void removeNotify() {
		this.declaringType.fields.removeListPropertyListener( this.listPropertyAdapter );
		super.removeNotify();
	}
	private void refresh() {
		this.removeAll();
		for( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldDeclaredInAlice : this.declaringType.fields ) {
			this.add( new org.alice.ide.common.FieldDeclarationPane( org.alice.ide.IDE.getSingleton().getTemplatesFactory(), fieldDeclaredInAlice ) );
		}
		this.revalidate();
		this.repaint();
	}

	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice[] getFields() {
		final int N = this.getComponentCount();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice[] rv = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice[ N ];
		for( int i=0; i<N; i++ ) {
			rv[ i ] = ((org.alice.ide.common.FieldDeclarationPane)getComponent( i )).getField();
		}
		return rv;
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 400, 300 );
	}
}

/**
* @author Dennis Cosgrove
*/
public class EditFieldsPane extends edu.cmu.cs.dennisc.zoot.ZInputPane< Boolean > {
	private FieldDeclarationsPane fieldDeclarationsPane;

	//	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > list = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >();
	public EditFieldsPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.setLayout( new java.awt.BorderLayout() );
		this.fieldDeclarationsPane = new FieldDeclarationsPane( declaringType );
		edu.cmu.cs.dennisc.zoot.ZButton addButton = new edu.cmu.cs.dennisc.zoot.ZButton( new org.alice.ide.operations.ast.DeclareFieldOperation( declaringType ) );

		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( fieldDeclarationsPane );
		scrollPane.setBorder( null );
		this.add( scrollPane, java.awt.BorderLayout.CENTER );
		javax.swing.Box box = javax.swing.Box.createVerticalBox();
		box.add( addButton );
		this.add( box, java.awt.BorderLayout.EAST );
	}
	@Override
	protected Boolean getActualInputValue() {
		return Boolean.TRUE;
	}
//	public static void main( String[] args ) {
//		org.alice.ide.FauxIDE ide = new org.alice.ide.FauxIDE();
//		ide.loadProjectFrom( "C:/Users/estrian/Documents/Alice3/MyProjects/a.a3p" );
//		EditFieldsPane editFieldsPane = new EditFieldsPane( ide.getSceneType() );
//		editFieldsPane.showInJDialog( ide );
//	}
}
