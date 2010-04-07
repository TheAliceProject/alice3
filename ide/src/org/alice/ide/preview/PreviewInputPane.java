/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.preview;

/**
 * @author Dennis Cosgrove
 */
public abstract class PreviewInputPane<E> extends edu.cmu.cs.dennisc.inputpane.KInputPane< E > {
//	protected static javax.swing.JLabel createLabel( String s ) {
//		javax.swing.JLabel rv = edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( s );
//		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
//		return rv;
//	}
	class PreviewPane extends edu.cmu.cs.dennisc.javax.swing.components.JBorderPane {
		public void refresh() {
			edu.cmu.cs.dennisc.javax.swing.ForgetUtilities.forgetAndRemoveAllComponents( this );
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
		this.setLayout( new java.awt.BorderLayout() );
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
		return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
	}

	protected abstract java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv );
	
	private java.awt.Component createRowsSpringPane() {
		this.previewPane = new PreviewPane();
		this.spacer = javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 32 ) );
		final java.awt.Component[] previewRow = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createColumn0Label( "preview:" ), this.previewPane );
		final java.awt.Component[] spacerRow = edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( this.spacer, null );
		this.repaint();
		javax.swing.JComponent rv = new edu.cmu.cs.dennisc.javax.swing.components.JRowsSpringPane( 16, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				assert previewRow != null;
				rv.add( previewRow );
				rv.add( spacerRow );
				PreviewInputPane.this.updateRows( rv );
				rv.add( edu.cmu.cs.dennisc.javax.swing.SpringUtilities.createRow( null, null ) );
				return rv;
			}
		};
		rv.setAlignmentX( 0.0f );
		return rv;
	}
	@Override
	public void addNotify() {
		super.addNotify();
		if( this.rowsSpringPane != null ) {
			//pass
		} else {
			this.rowsSpringPane = this.createRowsSpringPane();
			this.add( this.rowsSpringPane, java.awt.BorderLayout.WEST );
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
