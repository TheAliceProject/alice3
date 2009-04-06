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
package org.alice.ide.initializer;

/**
 * @author Dennis Cosgrove
 */
class FauxItem extends javax.swing.AbstractButton {
	private ItemInitializerPane itemInitializerPane = new ItemInitializerPane() {
		@Override
		protected void handleInitializerChange() {
			this.revalidate();
			this.repaint();
		}
	};
	private zoot.ZLabel label = new zoot.ZLabel();
	public FauxItem( int index, edu.cmu.cs.dennisc.alice.ast.Expression initializer ) {
		this.setIndex( index );
		this.itemInitializerPane.setInitializer( initializer );
		this.setModel( new javax.swing.JToggleButton.ToggleButtonModel() );
		this.setLayout( new java.awt.BorderLayout( 8, 0 ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 2 ) );
		this.add( this.label, java.awt.BorderLayout.WEST );
		this.add( new swing.LineAxisPane( this.itemInitializerPane, new javax.swing.JLabel() ), java.awt.BorderLayout.CENTER );
		//this.add( javax.swing.Box.createGlue() );
		this.addMouseListener( new java.awt.event.MouseListener() {
			public void mouseEntered( java.awt.event.MouseEvent e ) {
			}
			public void mouseExited( java.awt.event.MouseEvent e ) {
			}
			public void mousePressed( java.awt.event.MouseEvent e ) {
				FauxItem.this.setSelected( !FauxItem.this.isSelected() );
				FauxItem.this.repaint();
			}
			public void mouseReleased( java.awt.event.MouseEvent e ) {
			}
			public void mouseClicked( java.awt.event.MouseEvent e ) {
			}
		} );
		//todo
		//		this.setLeftButtonPressOperation( new org.alice.ide.operations.AbstractActionOperation() {
		//			public void perform( zoot.ActionContext actionContext ) {
		//				FauxItem.this.setSelected( !FauxItem.this.isSelected() );
		//			}
		//		} );
	}
	public void setIndex( int index ) {
		this.label.setText( "[ " + index + " ]" );
	}
	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType componentType ) {
		this.itemInitializerPane.handleTypeChange( componentType );
	}
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		//super.paintComponent( g );
		if( this.getModel().isSelected() ) {
			java.awt.Color color = new java.awt.Color( 191, 191, 255 );
			g.setColor( color );
			g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
		}
	}
}

//class FauxList extends swing.GridBagPane {
//	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
//	public FauxList() {
//		buttonGroup.add( new FauxItem( 0 ) );
//		buttonGroup.add( new FauxItem( 1 ) );
//		buttonGroup.add( new FauxItem( 2 ) );
//		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
//		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
//		gbc.fill = java.awt.GridBagConstraints.BOTH;
//		gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
//		gbc.weightx = 1.0;
//		java.util.Enumeration< javax.swing.AbstractButton > e = this.buttonGroup.getElements();
//		while( e.hasMoreElements() ) {
//			javax.swing.AbstractButton button = e.nextElement();
//			this.add( button, gbc );
//		}
//		gbc.weighty = 1.0;
//		this.add( new javax.swing.JLabel(), gbc );
//	}
//	public void handleTypeChange( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
//		java.util.Enumeration< javax.swing.AbstractButton > e = this.buttonGroup.getElements();
//		while( e.hasMoreElements() ) {
//			javax.swing.AbstractButton button = e.nextElement();
//			if( button instanceof FauxItem ) {
//				FauxItem fauxItem = (FauxItem)button;
//				fauxItem.handleTypeChange( type );
//			}
//		}
//	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumSize( super.getPreferredSize(), 240, 180 );
//	}
//}
