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
package org.alice.stageide.sceneeditor;

class IsExpandedCheckBoxUI extends javax.swing.plaf.basic.BasicButtonUI {
	private final static IsExpandedCheckBoxUI singleton = new IsExpandedCheckBoxUI();

	public static javax.swing.plaf.ComponentUI createUI( javax.swing.JComponent c ) {
		return singleton;
	}

	@Override
	protected void paintText( java.awt.Graphics g, javax.swing.JComponent c, java.awt.Rectangle textRect, String text ) {
		//		super.paintText( g, b, textRect, text )

		javax.swing.AbstractButton button = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( c, javax.swing.AbstractButton.class );
		javax.swing.ButtonModel model = button.getModel();
		java.awt.Paint forePaint;
		java.awt.Paint backPaint;
		if( model.isPressed() ) {
			forePaint = new java.awt.Color( 128, 128, 0 );
			backPaint = null;
		} else {
			if( model.isRollover() || model.isArmed() ) {
				forePaint = java.awt.Color.WHITE;
				backPaint = java.awt.Color.BLACK;
			} else {
				forePaint = new java.awt.Color( 255, 255, 0, 127 );
				backPaint = java.awt.Color.DARK_GRAY;
			}
		}
		java.awt.Graphics2D g2 = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( g, java.awt.Graphics2D.class );
		g2.setRenderingHint( java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON );

		java.awt.FontMetrics fm = g.getFontMetrics();
		int x = textRect.x + getTextShiftOffset();
		int y = textRect.y + fm.getAscent() + getTextShiftOffset();
		if( backPaint != null ) {
			g2.setPaint( backPaint );
			g2.drawString( text, x + 2, y + 2 );
		}
		g2.setPaint( forePaint );
		g2.drawString( text, x, y );
	}
	@Override
	public void paint( java.awt.Graphics g, javax.swing.JComponent c ) {
		super.paint( g, c );
		javax.swing.AbstractButton button = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( c, javax.swing.AbstractButton.class );
		javax.swing.ButtonModel model = button.getModel();
		java.awt.Graphics2D g2 = edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( g, java.awt.Graphics2D.class );
		g2.setStroke( new java.awt.BasicStroke( 3.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND ) );
		if( model.isSelected() ) {
			int x = 0;
			int y = 0;
			int minor = 4;
			int MAJOR = 32;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
			minor += 6;
			MAJOR -= 4;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
		} else {
			int x = c.getWidth();
			int y = c.getHeight();
			int minor = -4;
			int MAJOR = -32;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
			minor += -6;
			MAJOR -= -4;
			g2.drawLine( x + minor, y + MAJOR, x + minor, y + minor );
			g2.drawLine( x + minor, y + minor, x + MAJOR, y + minor );
		}
	}
}

class IsExpandedCheckBox extends zoot.ZCheckBox {
	private final int X_PAD = 16;
	private final int Y_PAD = 8;

	public IsExpandedCheckBox() {
		super( org.alice.ide.IDE.getSingleton().getIsSceneEditorExpandedOperation() );
		this.setOpaque( false );
		this.setFont( this.getFont().deriveFont( 24.0f ) );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( Y_PAD, X_PAD, Y_PAD, X_PAD ) );
	}
	@Override
	public void updateUI() {
		this.setUI( IsExpandedCheckBoxUI.createUI( this ) );
	}
	@Override
	public String getText() {
		if( isSelected() ) {
			return "edit code";
		} else {
			return "edit scene";
		}
	}

	private java.awt.Rectangle innerAreaBuffer = new java.awt.Rectangle();

	@Override
	public boolean contains( int x, int y ) {
		java.awt.Rectangle bounds = javax.swing.SwingUtilities.calculateInnerArea( this, innerAreaBuffer );
		if( this.isSelected() ) {
			bounds.x -= X_PAD;
			bounds.y -= Y_PAD;
		}
		bounds.width += X_PAD;
		bounds.height += Y_PAD;
		return bounds.contains( x, y );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class ControlsForOverlayPane extends edu.cmu.cs.dennisc.swing.CompassPointSpringPane {
	private edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > fieldsAdapter = new edu.cmu.cs.dennisc.property.event.ListPropertyListener< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice >() {
		public void adding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void added( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}

		public void clearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void cleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}

		public void removing( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void removed( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}

		public void setting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
		}
		public void set( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice > e ) {
			ControlsForOverlayPane.this.refreshFields();
		}
	};

	private org.alice.interact.AbstractDragAdapter dragAdapter;
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField;
	private java.util.List< FieldTile > fieldTiles = new java.util.LinkedList< FieldTile >();
	private IsExpandedCheckBox isSceneEditorExpandedCheckBox;
	private zoot.ZButton runButton;
	private org.alice.interact.CameraNavigatorWidget cameraNavigatorWidget;

	public ControlsForOverlayPane( org.alice.interact.AbstractDragAdapter dragAdapter ) {
		this.dragAdapter = dragAdapter;
		this.setOpaque( false );
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		//		ide.addIDEListener( this.ideAdapter );

		this.cameraNavigatorWidget = new org.alice.interact.CameraNavigatorWidget( this.dragAdapter );
		this.isSceneEditorExpandedCheckBox = new IsExpandedCheckBox();
		this.runButton = new zoot.ZButton( ide.getRunOperation() );
		this.setSouthEastComponent( this.isSceneEditorExpandedCheckBox );
		this.setNorthEastComponent( this.runButton );
		this.setSouthComponent( this.cameraNavigatorWidget );
		
		this.setExpanded( this.isSceneEditorExpandedCheckBox.isSelected() );
		this.isSceneEditorExpandedCheckBox.addItemListener( new java.awt.event.ItemListener() {
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				ControlsForOverlayPane.this.setExpanded( e.getStateChange() == java.awt.event.ItemEvent.SELECTED );
			}
		} );
	}

	public void fieldSelectionChanged( org.alice.ide.event.FieldSelectionEvent e ) {
		this.refreshFields();
	}
	public void focusedCodeChanged( org.alice.ide.event.FocusedCodeChangeEvent e ) {
		for( FieldTile fieldTile : this.fieldTiles ) {
			fieldTile.updateLabel();
		}
	}

	public void setExpanded( boolean isExpaned ) {
		if( this.cameraNavigatorWidget != null ) {
			this.cameraNavigatorWidget.setExpanded( isExpaned );
		}
	}
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getRootTypeDeclaredInAlice() {
		return (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)this.rootField.valueType.getValue();
	}
	public edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice getRootField() {
		return this.rootField;
	}
	public void setRootField( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice rootField ) {
		if( this.rootField != null ) {
			getRootTypeDeclaredInAlice().fields.removeListPropertyListener( this.fieldsAdapter );
		}
		this.rootField = rootField;
		if( this.getRootField() != null ) {
			getRootTypeDeclaredInAlice().fields.addListPropertyListener( this.fieldsAdapter );
		}
		this.refreshFields();
	}
	protected FieldTile createFieldTile( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		assert field != null;
		return new FieldTile( field );
	}
	private void refreshFields() {
		javax.swing.SpringLayout springLayout = this.getSpringLayout();
		for( FieldTile fieldTile : this.fieldTiles ) {
			springLayout.removeLayoutComponent( fieldTile );
			this.remove( fieldTile );
		}
		this.fieldTiles.clear();
		if( this.rootField != null ) {
			FieldTile rootFieldTile = this.createFieldTile( this.rootField );
			this.setNorthWestComponent( rootFieldTile );
			this.fieldTiles.add( rootFieldTile );
			java.awt.Component prev = rootFieldTile;
			if( rootField != null ) {
				for( edu.cmu.cs.dennisc.alice.ast.AbstractField field : rootField.valueType.getValue().getDeclaredFields() ) {
					FieldTile fieldTile = createFieldTile( field );
					this.add( fieldTile );
					this.fieldTiles.add( fieldTile );
					springLayout.putConstraint( javax.swing.SpringLayout.NORTH, fieldTile, 1, javax.swing.SpringLayout.SOUTH, prev );
					springLayout.putConstraint( javax.swing.SpringLayout.WEST, fieldTile, 10, javax.swing.SpringLayout.WEST, rootFieldTile );
					prev = fieldTile;
				}
			}
		}
		revalidate();
		repaint();
	}
}
