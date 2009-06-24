package org.alice.ide.editorstabbedpane;

//
//class RemoveItemOperation extends org.alice.ide.operations.AbstractActionOperation {
//	public RemoveItemOperation() {
//		this.putValue( javax.swing.Action.NAME, "Remove" );
//	}
//	public void perform( zoot.ActionContext actionContext ) {
//		int index = ArrayInitializerPane.this.list.getSelectedIndex();
//		if( index >= 0 ) {
//			ArrayInitializerPane.this.arrayExpressions.remove( index );
//		}
//	}
//}
//
//abstract class AbstractMoveItemOperation extends org.alice.ide.operations.AbstractActionOperation {
//	protected void swapWithNext( int index ) {
//		ArrayInitializerPane.this.swapWithNext( index );
//	}
//}
//
//class MoveItemUpOperation extends AbstractMoveItemOperation {
//	public MoveItemUpOperation() {
//		this.putValue( javax.swing.Action.NAME, "Move Up" );
//	}
//	public void perform( zoot.ActionContext actionContext ) {
//		int index = ArrayInitializerPane.this.list.getSelectedIndex();
//		this.swapWithNext( index - 1 );
//		list.setSelectedIndex( index-1 );
//	}
//}
//
//class MoveItemDownOperation extends AbstractMoveItemOperation {
//	public MoveItemDownOperation() {
//		this.putValue( javax.swing.Action.NAME, "Move Down" );
//	}
//	public void perform( zoot.ActionContext actionContext ) {
//		int index = ArrayInitializerPane.this.list.getSelectedIndex();
//		this.swapWithNext( index );
//		list.setSelectedIndex( index+1 );
//	}
//}
//
//class ItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > {
//	public ItemSelectionOperation( javax.swing.ComboBoxModel comboBoxModel ) {
//		super( comboBoxModel );
//	}
//	public void performSelectionChange( zoot.ItemSelectionContext< edu.cmu.cs.dennisc.alice.ast.Expression > context ) {
//		ArrayInitializerPane.this.handleSelectionChange( context );
//	}
//}
//
//class ExpressionList extends swing.GridBagPane {
//	private java.awt.Component glue = javax.swing.Box.createGlue();
//	private javax.swing.ButtonGroup group;
//	private java.awt.GridBagConstraints gbc;
//
//	public ExpressionList( zoot.ItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.Expression > itemSelectionOperation ) {
//		//			super( itemSelectionOperation );
//		this.group = new javax.swing.ButtonGroup();
//		this.gbc = new java.awt.GridBagConstraints();
//		this.gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
//		this.gbc.fill = java.awt.GridBagConstraints.BOTH;
//		this.gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		this.gbc.weightx = 1.0;
//		this.setOpaque( true );
//		this.setBackground( java.awt.Color.WHITE );
//		this.refresh();
//	}
//	public int getSelectedIndex() {
//		javax.swing.ButtonModel selection = this.group.getSelection();
//		int index = 0;
//		java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
//		while( e.hasMoreElements() ) {
//			javax.swing.AbstractButton button = (javax.swing.AbstractButton)e.nextElement();
//			if( button.getModel() == selection ) {
//				return index;
//			}
//			index ++;
//		}
//		return -1;
//	}
//	
//	public void setSelectedIndex( int index ) {
//		if( index >= 0 ) {
//			FauxItem fauxItem = (FauxItem)this.getComponent( index );
//			fauxItem.setSelected( true );
//		} else {
//			javax.swing.ButtonModel model = this.group.getSelection();
//			if( model != null ) {
//				model.setSelected( false );
//			}
//		}
//	}
//	public void refresh() {
//		int N = ArrayInitializerPane.this.arrayExpressions.size();
//		int M = this.group.getButtonCount();
//		for( int i=M; i<N; i++ ) {
//			FauxItem fauxItem = new FauxItem( i, ArrayInitializerPane.this.arrayExpressions ) {
//				@Override
//				protected edu.cmu.cs.dennisc.alice.ast.AbstractType getFillInType() {
//					return ArrayInitializerPane.this.type.getComponentType();
//				}
//				@Override
//				public void setSelected( boolean b ) {
//					super.setSelected( b );
//					updateButtons();
//				}
//			};
//			this.group.add( fauxItem );
//		}
//		for( int i=N; i<M; i++ ) {
//			FauxItem fauxItem = (FauxItem)this.getComponent( i );
//			this.group.remove( fauxItem );
//		}
//		this.removeAll();
//		java.util.Enumeration< javax.swing.AbstractButton > e = this.group.getElements();
//		while( e.hasMoreElements() ) {
//			FauxItem fauxItem = (FauxItem)e.nextElement();
//			fauxItem.refresh();
//			this.add( fauxItem, this.gbc );
//		}
//		this.gbc.weighty = 1.0;
//		this.add( this.glue, this.gbc );
//		this.gbc.weighty = 0.0;
//	}
//}
//
///**
// * @author Dennis Cosgrove
// */
//class ArrayInitializerPane extends swing.LineAxisPane {
//	private ExpressionList list;
//
//	private edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions;
//
//	private zoot.ZButton addButton = new zoot.ZButton( new AddItemOperation() );
//	private zoot.ZButton removeButton = new zoot.ZButton( new RemoveItemOperation() );
//	private zoot.ZButton moveUpButton = new zoot.ZButton( new MoveItemUpOperation() );
//	private zoot.ZButton moveDownButton = new zoot.ZButton( new MoveItemDownOperation() );
//
//	private edu.cmu.cs.dennisc.alice.ast.AbstractType type;
//
//	public ArrayInitializerPane( edu.cmu.cs.dennisc.alice.ast.ExpressionListProperty arrayExpressions ) {
//		this.arrayExpressions = arrayExpressions;
//		this.arrayExpressions.addListPropertyListener( new edu.cmu.cs.dennisc.property.event.SimplifiedListPropertyAdapter< edu.cmu.cs.dennisc.alice.ast.Expression >() {
//			@Override
//			protected void changing( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
//			}
//			@Override
//			protected void changed( edu.cmu.cs.dennisc.property.event.ListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.Expression > e ) {
//				javax.swing.SwingUtilities.invokeLater( new Runnable() {
//					public void run() {
//						ArrayInitializerPane.this.list.refresh();
//					}
//				} );
//			}
//		} );
//		ListPropertyComboBoxModel< edu.cmu.cs.dennisc.alice.ast.Expression > comboBoxModel = new ListPropertyComboBoxModel< edu.cmu.cs.dennisc.alice.ast.Expression >( this.arrayExpressions );
//		this.list = new ExpressionList( new ItemSelectionOperation( comboBoxModel ) );
//		this.setLayout( new java.awt.BorderLayout( 8, 0 ) );
//		this.updateButtons();
//
//		swing.GridBagPane buttonPane = new swing.GridBagPane();
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		buttonPane.add( this.addButton, gbc );
//		buttonPane.add( this.removeButton, gbc );
//		gbc.insets.top = 8;
//		buttonPane.add( this.moveUpButton, gbc );
//		gbc.insets.top = 0;
//		buttonPane.add( this.moveDownButton, gbc );
//		gbc.weighty = 1.0;
//		buttonPane.add( javax.swing.Box.createGlue(), gbc );
//
//		this.add( buttonPane, java.awt.BorderLayout.EAST );
//
//		javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane( this.list ) {
//			@Override
//			public java.awt.Dimension getPreferredSize() {
//				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 240, 180 );
//			}
//		};
//		scrollPane.setBorder( null );
//		this.add( scrollPane, java.awt.BorderLayout.CENTER );
//	}
//
//	private void updateButtons() {
//		boolean isTypeValid = this.type != null && this.type.isArray();
//		this.addButton.setEnabled( isTypeValid );
//		int index = this.list.getSelectedIndex();
//		final int N = this.list.getComponentCount()-1;
//		edu.cmu.cs.dennisc.print.PrintUtilities.println( index, N );
//		this.removeButton.setEnabled( isTypeValid && index != -1 );
//		this.moveUpButton.setEnabled( isTypeValid && index > 0 );
//		this.moveDownButton.setEnabled( isTypeValid && index >= 0 && index < N - 1 );
//	}
//	private void handleSelectionChange( zoot.ItemSelectionContext< edu.cmu.cs.dennisc.alice.ast.Expression > context ) {
//		this.updateButtons();
//	}
//	private void swapWithNext( int index ) {
//		if( index >= 0 ) {
//			edu.cmu.cs.dennisc.alice.ast.Expression expression0 = this.arrayExpressions.get( index );
//			edu.cmu.cs.dennisc.alice.ast.Expression expression1 = this.arrayExpressions.get( index + 1 );
//			this.arrayExpressions.set( index, expression1, expression0 );
//		}
//	}
//}
//

class FieldDeclarationsPane extends swing.PageAxisPane {
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
public class EditFieldsPane extends zoot.ZInputPane< Boolean > {
	private FieldDeclarationsPane fieldDeclarationsPane;

	//	private java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > list = new java.util.LinkedList< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >();
	public EditFieldsPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType ) {
		this.setLayout( new java.awt.BorderLayout() );
		this.fieldDeclarationsPane = new FieldDeclarationsPane( declaringType );
		zoot.ZButton addButton = new zoot.ZButton( new org.alice.ide.operations.ast.DeclareFieldOperation( declaringType ) );

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
