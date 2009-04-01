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
class InstanceNameTextField extends javax.swing.JTextField {
	public InstanceNameTextField() {
		this.setFont( this.getFont().deriveFont( 18.0f ) );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 240 );
	}
	@Override
	public java.awt.Dimension getMaximumSize() {
		return this.getPreferredSize();
	}
}

/**
 * @author Dennis Cosgrove
 */
public abstract class CreateDeclarationPane<E>  extends zoot.ZInputPane< E > {
	protected static zoot.ZLabel createLabel( String s ) {
		zoot.ZLabel rv = new zoot.ZLabel( s );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		return rv;
	}

	public CreateDeclarationPane() {
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		//this.setAlignmentX( CENTER_ALIGNMENT );
	}
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}

	private java.awt.Component pane;
	private InstanceNameTextField instanceNameTextField = new InstanceNameTextField();
	protected abstract java.awt.Component[] createDeclarationRow();
	protected abstract java.awt.Component createIsFinalComponent();
	protected abstract java.awt.Component createValueTypeComponent();

	
	protected final java.awt.Component[] createIsFinalRow() {
		java.awt.Component component = this.createIsFinalComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "is final:" ), component );
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
		return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "name:" ), this.instanceNameTextField );
	}
	protected abstract java.awt.Component createInitializerComponent();
	protected final java.awt.Component[] createInitializerRow() {
		java.awt.Component component = this.createInitializerComponent();
		if( component != null ) {
			return edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "initializer:" ), component );
		} else {
			return null;
		}
	}
	
	protected String getDeclarationName() {
		return this.instanceNameTextField.getText();
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.pane != null ) {
			//pass
		} else {
			this.setLayout( new java.awt.BorderLayout() );
			this.add( this.createRowsSpringPane(), java.awt.BorderLayout.CENTER );
		}
	}
	private java.awt.Component createRowsSpringPane() {
		final java.awt.Component[] declarationRow = createDeclarationRow();
		final java.awt.Component[] valueTypeRow = createValueTypeRow();
		final java.awt.Component[] nameRow = createNameRow();
		final java.awt.Component[] initializerRow = createInitializerRow();
		final java.awt.Component[] isFinalRow = createIsFinalRow();
		return new swing.RowsSpringPane() {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				if( declarationRow != null ) {
					rv.add( declarationRow );
					rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createRigidArea( new java.awt.Dimension( 10, 0 ) ), null ) );
				}
				if( isFinalRow != null ) {
					rv.add( isFinalRow );
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
//				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
				return rv;
			}
		};
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
	}
}
