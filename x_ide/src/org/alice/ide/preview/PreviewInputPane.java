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
package org.alice.ide.preview;

/**
 * @author Dennis Cosgrove
 */
public abstract class PreviewInputPane<E> extends edu.cmu.cs.dennisc.zoot.ZInputPane< E > {
//	protected static edu.cmu.cs.dennisc.zoot.ZLabel createLabel( String s ) {
//		edu.cmu.cs.dennisc.zoot.ZLabel rv = edu.cmu.cs.dennisc.zoot.ZLabel.acquire( s );
//		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
//		return rv;
//	}
	class PreviewPane extends edu.cmu.cs.dennisc.croquet.swing.BorderPane {
		public void refresh() {
			edu.cmu.cs.dennisc.swing.ForgetUtilities.forgetAndRemoveAllComponents( this );
//			java.awt.Component component = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane(
//					PreviewInputPane.this.createPreviewSubComponent(),
//					javax.swing.Box.createHorizontalGlue()
//			);
			java.awt.Component component = PreviewInputPane.this.createPreviewSubComponent();
			this.add( component, java.awt.BorderLayout.WEST );
			this.revalidate();
			this.repaint();
		}
		@Override
		public boolean contains( int x, int y ) {
			return false;
		}
		@Override
		public void addNotify() {
			super.addNotify();
			this.refresh();
		}
	}

	private java.awt.Component rowsSpringPane;
	private PreviewPane previewPane;
	private java.awt.Component spacer;

	public PreviewInputPane() {
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
	}
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
	protected abstract java.awt.Component createPreviewSubComponent();
	private void updatePreview() {
		if( this.previewPane != null ) {
			this.previewPane.refresh();
		}
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
	}

	protected abstract java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv );
	
	private java.awt.Component createRowsSpringPane() {
		this.previewPane = new PreviewPane();
		this.spacer = javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 32 ) );
		final java.awt.Component[] previewRow = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( edu.cmu.cs.dennisc.swing.SpringUtilities.createColumn0Label( "preview:" ), this.previewPane );
		final java.awt.Component[] spacerRow = edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( this.spacer, null );
		this.repaint();
		return new edu.cmu.cs.dennisc.croquet.swing.RowsSpringPane( 16, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				assert previewRow != null;
				rv.add( previewRow );
				rv.add( spacerRow );
				PreviewInputPane.this.updateRows( rv );
				rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null ) );
				return rv;
			}
		};
	}
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.rowsSpringPane != null ) {
			//pass
		} else {
			this.rowsSpringPane = this.createRowsSpringPane();
			this.add( this.rowsSpringPane, java.awt.BorderLayout.CENTER );
		}
		this.updateOKButton();
	}

	@Override
	public void updateOKButton() {
		super.updateOKButton();
		this.updatePreview();
		this.updateSizeIfNecessary();
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
}
