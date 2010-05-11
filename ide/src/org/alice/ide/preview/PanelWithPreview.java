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
public abstract class PanelWithPreview extends edu.cmu.cs.dennisc.croquet.BorderPanel {
//	protected static javax.swing.JLabel createLabel( String s ) {
//		javax.swing.JLabel rv = edu.cmu.cs.dennisc.croquet.CroquetUtilities.createLabel( s );
//		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
//		return rv;
//	}
	class PreviewPane extends org.alice.ide.Component {
		public void refresh() {
			this.forgetAndRemoveAllComponents();
//			java.awt.Component component = new edu.cmu.cs.dennisc.croquet.swing.LineAxisPane(
//					PreviewInputPane.this.createPreviewSubComponent(),
//					javax.swing.Box.createHorizontalGlue()
//			);
			this.addComponent( PanelWithPreview.this.createPreviewSubComponent(), java.awt.BorderLayout.WEST );
			this.revalidateAndRepaint();
		}
		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new java.awt.BorderLayout();
		}
		@Override
		protected boolean contains( int x, int y, boolean jContains ) {
			return false;
		}
		@Override
		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			super.handleAddedTo( parent );
			this.refresh();
		}
		@Override
		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			this.forgetAndRemoveAllComponents();
			super.handleRemovedFrom( parent );
		}
	}

	private PreviewPane previewPane;
	private edu.cmu.cs.dennisc.croquet.Component< ? > spacer;
	private edu.cmu.cs.dennisc.croquet.Component<?> centerPanel;
	public PanelWithPreview() {
		final int INSET = 16;
		edu.cmu.cs.dennisc.croquet.RowsSpringPanel rowsSpringPanel = new edu.cmu.cs.dennisc.croquet.RowsSpringPanel() {
			@Override
			protected java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> updateComponentRows(java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> rv) {
				return PanelWithPreview.this.updateComponentRows( rv );
			}
		};
		rowsSpringPanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		this.centerPanel = edu.cmu.cs.dennisc.croquet.BoxUtilities.createConstrainedToMinimumPreferredWidthComponent(rowsSpringPanel, 320);
	}
	protected abstract edu.cmu.cs.dennisc.croquet.Component< ? > createPreviewSubComponent();
	
//	todo: croquet switch
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 320 );
//	}
//	@Override
//	public void updateOKButton() {
//		super.updateOKButton();
//		this.updatePreview();
//		this.updateSizeIfNecessary();
//	}
//	private void updatePreview() {
//		if( this.previewPane != null ) {
//			this.previewPane.refresh();
//		}
//	}

	protected abstract java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > updateInternalComponentRows( java.util.List< edu.cmu.cs.dennisc.croquet.Component< ? >[] > rv );

	protected java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> updateComponentRows(java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> rv) {
		this.previewPane = new PreviewPane();
		this.spacer = edu.cmu.cs.dennisc.croquet.BoxUtilities.createRigidArea( new java.awt.Dimension( 0, 32 ) );
		rv.add( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow(
						edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "preview:" ),
						this.previewPane
				) 
		);
		this.updateInternalComponentRows( rv );
		rv.add( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow(
						this.spacer,
						null
				) 
		);
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: investigate setAlignmentX" );
		this.setAlignmentX( 0.0f );
		return rv;
	}

	
	@Override
	protected boolean paintComponent( java.awt.Graphics2D g2 ) {
		if( this.spacer != null ) {
			int y = this.spacer.getY() + this.spacer.getHeight();
			java.awt.Insets insets = this.getInsets();
			g2.setColor( java.awt.Color.GRAY );
			g2.drawLine( insets.left, y, this.getWidth()-insets.right, y );
		}
		return super.paintComponent( g2 );
	}
	
	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getSingleton();
	}
//	@Deprecated
//	protected boolean isOKButtonValid() {
//		throw new RuntimeException("todo");
////		return true;
//	}
//	@Deprecated
//	protected void updateOKButton() {
//		throw new RuntimeException("todo");
//	}
	@Deprecated
	protected String getTitleDefault() {
		return null;
	}
	
	@Override
	protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		super.handleAddedTo(parent);
		this.addComponent( this.centerPanel, Constraint.CENTER);
	}
	@Override
	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
		this.removeComponent( this.centerPanel );
		super.handleRemovedFrom(parent);
	}

}
