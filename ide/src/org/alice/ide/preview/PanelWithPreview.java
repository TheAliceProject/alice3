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
public abstract class PanelWithPreview< F > extends org.lgna.croquet.components.CascadeInputDialogPanel< F > {
	class PreviewPane extends org.lgna.croquet.components.JComponent<javax.swing.JPanel> {
		public void refresh() {
			this.internalForgetAndRemoveAllComponents();
			this.internalAddComponent( PanelWithPreview.this.createPreviewSubComponent(), java.awt.BorderLayout.CENTER );
			this.revalidateAndRepaint();
		}
		@Override
		protected javax.swing.JPanel createAwtComponent() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				@Override
				public boolean contains(int x, int y) {
					return false;
				}
				@Override
				public java.awt.Dimension getPreferredSize() {
					return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumWidth( super.getPreferredSize(), 320 );
				}
			};
			rv.setOpaque( false );
			rv.setLayout(new java.awt.BorderLayout());
			return rv;
		}
		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			this.refresh();
		}
		@Override
		protected void handleUndisplayable() {
			this.internalForgetAndRemoveAllComponents();
			super.handleUndisplayable();
		}
	}

	private PreviewPane previewPane;
	
	protected boolean isPreviewDesired() {
		return true;
	}
	
	public PanelWithPreview() {
		final int PAD = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD, PAD, 0, PAD ) );
	}
	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.BorderLayout();
	}
	public void addComponent( org.lgna.croquet.components.Component< ? > component, org.lgna.croquet.components.BorderPanel.Constraint constraint ) {
		this.internalAddComponent( component, constraint.getInternal() );
	}
	protected abstract org.lgna.croquet.components.Component< ? > createPreviewSubComponent();
	protected abstract org.lgna.croquet.components.Component< ? > createMainComponent();
	private void initializeIfNecessary() {
		if( this.previewPane != null ) {
			//pass
		} else {
			this.addComponent( this.createMainComponent(), org.lgna.croquet.components.BorderPanel.Constraint.CENTER );

			if( this.isPreviewDesired() ) {
				this.previewPane = new PreviewPane();
				org.lgna.croquet.components.PageAxisPanel northPanel = new org.lgna.croquet.components.PageAxisPanel(
						new org.lgna.croquet.components.LineAxisPanel( 
								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 16 ),
								new org.lgna.croquet.components.Label( "preview:", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ),
								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 16 ),
								this.previewPane
						),
						org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ),
						new org.lgna.croquet.components.HorizontalSeparator(),
						org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 )
				);
				this.addComponent( northPanel, org.lgna.croquet.components.BorderPanel.Constraint.PAGE_START );
			}
		}
	}

	public void updatePreview() {
		if( this.previewPane != null ) {
			this.previewPane.refresh();
		}
	}

	protected org.alice.ide.IDE getIDE() {
		return org.alice.ide.IDE.getActiveInstance();
	}
	
	public abstract String getExplanationIfOkButtonShouldBeDisabled();
	public abstract String getDialogTitle();
	
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.initializeIfNecessary();
	}
//	@Override
//	protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
//		this.removeComponent( this.centerPanel );
//		super.handleRemovedFrom(parent);
//	}
}

