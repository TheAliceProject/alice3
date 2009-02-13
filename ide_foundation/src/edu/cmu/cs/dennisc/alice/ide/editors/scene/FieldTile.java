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
package edu.cmu.cs.dennisc.alice.ide.editors.scene;

import edu.cmu.cs.dennisc.alice.ide.editors.type.PaintUtilities;

/**
 * @author Dennis Cosgrove
 */
class MyLabelThatDoesntLockOverlay extends javax.swing.JButton {
	public MyLabelThatDoesntLockOverlay() {
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		this.setOpaque( false );
		this.setBackground( edu.cmu.cs.dennisc.awt.ColorUtilities.GARISH_COLOR );
	}
	@Override
	public boolean contains( int x, int y ) {
		return false;
	}
}

//class MyLabelThatDoesntLockOverlay extends javax.swing.JComponent {
//	private String text = "";
//	public MyLabelThatDoesntLockOverlay() {
//		this.setOpaque( true );
//	}
//	public String getText() {
//		return this.text;
//	}
//	public void setText( String text ) {
//		this.text = text;
//		this.repaint();
//	}
//	
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		java.awt.Graphics g = edu.cmu.cs.dennisc.swing.SwingUtilities.getGraphics();
//		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.text, g );
//		return new java.awt.Dimension( (int)( bounds.getWidth()+0.5 ), (int)( bounds.getHeight()+0.5 ) );
//	}
//	
//	@Override
//	protected void paintComponent( java.awt.Graphics g ) {
//		//super.paintComponent( g );
//		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.text, g );
//		g.drawString( this.text, (int)bounds.getX(), (int)bounds.getY() );
//	}
//}



/**
 * @author Dennis Cosgrove
 */
public class FieldTile extends edu.cmu.cs.dennisc.alice.ide.editors.common.ExpressionLikeSubstance {
	private MyLabelThatDoesntLockOverlay label = new MyLabelThatDoesntLockOverlay();
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;

	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			FieldTile.this.updateLabel();
		}
	}
	public FieldTile() {
		this( null );
	}
	public FieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		this.setField( field );
		if( field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			((edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)field).name.addPropertyListener( new NamePropertyAdapter() );
		}
//		this.add( this.label );
		if( this.isFieldTileSelectionSelectionDesired() ) {
			this.addMouseListener( new java.awt.event.MouseListener() {
				public void mouseClicked( java.awt.event.MouseEvent e ) {
				}
				public void mousePressed( java.awt.event.MouseEvent e ) {
					if( e.isPopupTrigger() ) {
						//pass
					} else {
						getIDE().performIfAppropriate( new SelectFieldOperation( FieldTile.this.field ), e );
					}
				}
				public void mouseReleased( java.awt.event.MouseEvent e ) {
				} 
				public void mouseEntered( java.awt.event.MouseEvent e ) {
				}
				public void mouseExited( java.awt.event.MouseEvent e ) {
				}
			} );
//			this.label.addActionListener( new java.awt.event.ActionListener() {
//				public void actionPerformed( java.awt.event.ActionEvent e ) {
//					getIDE().performIfAppropriate( new SelectFieldOperation( FieldTile.this.field ), e );
//				}
//			} );
		}
	}
	
	protected boolean isFieldTileSelectionSelectionDesired() {
		return true;
	}
	
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		if( this.field != null ) {
			return this.field.getValueType();
		} else {
			return null;
		}
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractField getField() {
		return this.field;
	}
	public void setField( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		this.field = field;
		this.updateLabel();
	}

	@Override
	protected boolean isActuallyPotentiallyActive() {
		return true;
	}
	@Override
	protected boolean isActuallyPotentiallySelectable() {
		return false;
	}
	@Override
	protected boolean isActuallyPotentiallyDraggable() {
		return false;
	}
	
	protected java.awt.Color calculateColor() {
		edu.cmu.cs.dennisc.alice.ide.IDE ide = getIDE();
		java.awt.Color color;
		if( this.field == ide.getFieldSelection() ) {
			//color = ide.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class );
			color = java.awt.Color.YELLOW;
		} else {
			if( ide.isFieldInScope( this.field ) ) {
				color = java.awt.Color.WHITE;
			} else {
				color = java.awt.Color.GRAY;
			}
		}
		return color;
	}

	public void updateLabel() {
		if( this.field != null ) {
			String text = getIDE().getInstanceTextForField( this.field, false );
			this.setBackground( this.calculateColor() );
			this.label.setText( text );
		} else {
			this.setBackground( java.awt.Color.RED );
			this.label.setText( "null" );
		}
		this.revalidate();
		this.repaint();
	}
	protected boolean isInScope() {
		return getIDE().isFieldInScope( field );
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		java.awt.Dimension rv = super.getPreferredSize();
		java.awt.Graphics g = edu.cmu.cs.dennisc.swing.SwingUtilities.getGraphics();
		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.label.getText(), g );
		rv.width += (int)( bounds.getWidth()+0.5 );
		rv.height += (int)( bounds.getHeight()+0.5 );
		return rv;
	}
	
	@Override
	protected void paintComponent( java.awt.Graphics g ) {
		super.paintComponent( g );
		java.awt.geom.Rectangle2D bounds = g.getFontMetrics().getStringBounds( this.label.getText(), g );
		g.drawString( this.label.getText(), this.getBorder().getBorderInsets( this ).left-(int)bounds.getX(), this.getBorder().getBorderInsets( this ).top-(int)bounds.getY() );
	}
	@Override
	public void paint( java.awt.Graphics g ) {
		super.paint( g );
		if( isInScope() ) {
			//pass
		} else {
			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			g2.setPaint( PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
