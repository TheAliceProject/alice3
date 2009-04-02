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
package org.alice.ide.createdeclarationpanes;

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateDeclarationPane<E> extends zoot.ZInputPane< E > {
	protected static zoot.ZLabel createLabel( String s ) {
		zoot.ZLabel rv = new zoot.ZLabel( s );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}
	class IsReassignableStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public IsReassignableStateOperation( boolean initialValue ) {
			super( initialValue );
			//this.putValue( javax.swing.Action.NAME, "is constant" );
		}
		public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
			CreateDeclarationPane.this.handleIsReassignableChange( booleanStateContext );
			booleanStateContext.commit();
		}
	}
	class MyTypePane extends TypePane {
		@Override
		protected void handleComponentTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
			CreateDeclarationPane.this.handleTypeChange();
		}
		@Override
		protected void handleIsArrayChange( boolean isArray ) {
			CreateDeclarationPane.this.handleTypeChange();
		}
	}
	class DeclarationNameTextField extends zoot.ZSuggestiveTextField {
		public DeclarationNameTextField() {
			super( "", "<unset>" );
			this.setFont( this.getFont().deriveFont( 18.0f ) );
			this.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
				public void changedUpdate( javax.swing.event.DocumentEvent e ) {
					DeclarationNameTextField.this.handleUpdate( e );
				}
				public void insertUpdate( javax.swing.event.DocumentEvent e ) {
					DeclarationNameTextField.this.handleUpdate( e );
				}
				public void removeUpdate( javax.swing.event.DocumentEvent e ) {
					DeclarationNameTextField.this.handleUpdate( e );
				}
			} );
		}
		@Override
		public java.awt.Dimension getPreferredSize() {
			return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 240 );
		}
		@Override
		public java.awt.Dimension getMaximumSize() {
			return this.getPreferredSize();
		}
		private void handleUpdate( javax.swing.event.DocumentEvent e ) {
			CreateDeclarationPane.this.handleDeclarationNameUpdate( e );
		}
	}
	class MyInitializerPane extends InitializerPane {
		@Override
		protected void handleInitializerChange() {
			CreateDeclarationPane.this.handleInitializerChange();
		}
	}

	class PreviewPane extends swing.BorderPane {
		public void refresh() {
			this.removeAll();
			this.add( CreateDeclarationPane.this.createPreviewSubComponent(), java.awt.BorderLayout.WEST );
			this.revalidate();
			this.repaint();
		}
		@Override
		public boolean contains( int x, int y ) {
			return false;
		}
	}
	private void updatePreview() {
		this.previewPane.refresh();
	}
	protected abstract java.awt.Component createPreviewSubComponent();
	protected final boolean isPreviewComponentDesired() {
		return true;
	}
	protected final java.awt.Component createPreviewComponent() {
		if( this.isPreviewComponentDesired() ) {
			this.previewPane = new PreviewPane();
		} else {
			this.previewPane = null;
		}
		return this.previewPane;
	}
	protected final java.awt.Component[] createPreviewRow() {
		java.awt.Component component = this.createPreviewComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "preview:" ), component );
		} else {
			return null;
		}
	}

	
	private java.awt.Component pane;
	private PreviewPane previewPane;
	private zoot.ZCheckBox isReassignableCheckBox;
	private TypePane typePane;
	private DeclarationNameTextField declarationNameTextField = new DeclarationNameTextField();
	private InitializerPane initializerPane;

	public CreateDeclarationPane() {
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
	}

	protected boolean isReassignable() {
		if( this.isReassignableCheckBox != null ) {
			return this.isReassignableCheckBox.isSelected();
		} else {
			throw new RuntimeException( "override" );
		}
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getValueType() {
		if( this.typePane != null ) {
			return this.typePane.getValueType();
		} else {
			throw new RuntimeException( "override" );
		}
	}
	protected final String getDeclarationName() {
		return this.declarationNameTextField.getText();
	}
	protected edu.cmu.cs.dennisc.alice.ast.Expression getInitializer() {
		if( this.initializerPane != null ) {
			return this.initializerPane.getInitializer();
		} else {
			throw new RuntimeException( "override" );
		}
	}

	protected abstract boolean isIsReassignableComponentDesired();
	protected boolean isIsReassignableComponentEnabled() {
		return true;
	}
	protected boolean getIsReassignableInitialState() {
		return true;
	}
	protected final java.awt.Component createIsReassignableComponent() {
		if( isIsReassignableComponentDesired() ) {
			this.isReassignableCheckBox = new zoot.ZCheckBox( new IsReassignableStateOperation( this.getIsReassignableInitialState() ) );
			this.isReassignableCheckBox.setEnabled( this.isIsReassignableComponentEnabled() );
			this.isReassignableCheckBox.setOpaque( false );
		} else {
			this.isReassignableCheckBox = null;
		}
		return this.isReassignableCheckBox;
	}
	


	protected abstract boolean isEditableValueTypeComponentDesired();
	protected java.awt.Component createValueTypeComponent() {
		if( this.isEditableValueTypeComponentDesired() ) {
			this.typePane = new MyTypePane();
		} else {
			this.typePane = null;
		}
		return this.typePane;
	}



	protected final java.awt.Component[] createIsReassignableRow() {
		java.awt.Component component = this.createIsReassignableComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "is re-assignable:" ), component );
		} else {
			return null;
		}
	}

	protected String getValueTypeText() {
		return "value type:";
	}

	protected final java.awt.Component[] createValueTypeRow() {
		java.awt.Component component = this.createValueTypeComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( this.getValueTypeText() ), component );
		} else {
			return null;
		}
	}

	protected final java.awt.Component[] createNameRow() {
		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "name:" ), this.declarationNameTextField );
	}
	
	protected abstract boolean isEditableInitializerComponentDesired();
	protected java.awt.Component createInitializerComponent() {
		if( this.isEditableInitializerComponentDesired() ) {
			this.initializerPane = new MyInitializerPane();
		} else {
			this.initializerPane = null;
		}
		return this.initializerPane;
	}
	protected final java.awt.Component[] createInitializerRow() {
		java.awt.Component component = this.createInitializerComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "initializer:" ), component );
		} else {
			return null;
		}
	}


	private java.awt.Component spacer;

	private java.awt.Component createRowsSpringPane() {
//		final java.awt.Component[] declarationRow = createDeclarationRow();
		final java.awt.Component[] previewRow = createPreviewRow();
		final java.awt.Component[] isReassignableRow = createIsReassignableRow();
		final java.awt.Component[] valueTypeRow = createValueTypeRow();
		final java.awt.Component[] nameRow = createNameRow();
		final java.awt.Component[] initializerRow = createInitializerRow();
		this.spacer = javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 32 ) );
		this.repaint();
		return new swing.RowsSpringPane( 16, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
//				assert declarationRow != null;
//				rv.add( declarationRow );
				assert previewRow != null;
				rv.add( previewRow );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( CreateDeclarationPane.this.spacer, null ) );
				if( isReassignableRow != null ) {
					rv.add( isReassignableRow );
				}
				if( valueTypeRow != null ) {
					rv.add( valueTypeRow );
				}
				if( nameRow != null ) {
					rv.add( nameRow );
				}
				if( initializerRow != null ) {
					rv.add( initializerRow );
				}
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
				return rv;
			}
		};
	}
	@Override
	public void addNotify() {
		if( this.pane != null ) {
			//pass
		} else {
			this.setLayout( new java.awt.BorderLayout() );
			this.add( this.createRowsSpringPane(), java.awt.BorderLayout.CENTER );
			this.previewPane.refresh();
			this.updateOKButton();
		}
		super.addNotify();
		this.declarationNameTextField.requestFocus();
	}

	protected void handleIsReassignableChange( zoot.BooleanStateContext booleanStateContext ) {
		this.updatePreview();
		this.updateSizeIfNecessary();
	}
	protected void handleDeclarationNameUpdate( javax.swing.event.DocumentEvent e ) {
		this.updatePreview();
		this.updateOKButton();
		this.updateSizeIfNecessary();
	}
	private void handleTypeChange() {
		if( this.initializerPane != null ) {
			this.initializerPane.handleTypeChange( this.typePane.getValueType() );
		}
		this.updatePreview();
		this.updateOKButton();
		this.updateSizeIfNecessary();
	}
	private void handleInitializerChange() {
		this.updatePreview();
		this.updateOKButton();
		this.updateSizeIfNecessary();
	}


	protected boolean isDeclarationNameValid() {
		return this.declarationNameTextField.getText().length() > 0;
	}
	protected boolean isValueTypeValid() {
		if( this.typePane != null ) {
			return this.typePane.getValueType() != null;
		} else {
			return true;
		}
	}
	@Override
	public boolean isOKButtonValid() {
		return super.isOKButtonValid() && this.isDeclarationNameValid() && this.isValueTypeValid();
	}

	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		if( this.spacer != null ) {
			int y = this.spacer.getY() + this.spacer.getHeight();
			java.awt.Insets insets = this.getInsets();
			g.setColor( java.awt.Color.GRAY );
			g.drawLine( insets.left, y, this.getWidth()-insets.right, y );
		}
	}
//	@Override
//	public void paint( java.awt.Graphics g ) {
//		super.paint( g );
//	}
}
