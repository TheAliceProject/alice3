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

package org.alice.ide.preview.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class PanelWithPreview extends org.lgna.croquet.components.BorderPanel {
	private static final int PAD = 16;
	public PanelWithPreview() {
		this( null );
	}
	public PanelWithPreview( org.lgna.croquet.Composite<?> composite ) {
		super( composite );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( PAD, PAD, 0, PAD ) );
		this.setMinimumPreferredWidth( 320 );
	}
	protected boolean isPreviewDesired() {
		return true;
	}
	private org.alice.ide.croquet.components.PreviewPanel previewPanel;
	
	public abstract org.lgna.croquet.components.JComponent< ? > createPreviewSubComponent();
	public org.alice.ide.croquet.components.PreviewPanel getPreviewPanel() {
		return this.previewPanel;
	}
	
	protected final org.alice.ide.croquet.components.PreviewPanel createPreviewPanel() {
		return new org.alice.ide.croquet.components.PreviewPanel( this );
	}
	public void updatePreview() {
		org.alice.ide.croquet.components.PreviewPanel previewPanel = this.getPreviewPanel();
		if( previewPanel != null ) {
			previewPanel.refreshLater();
		}
	}
	protected abstract org.lgna.croquet.components.JComponent< ? > createMainComponent();
	private void initializeIfNecessary() {
		if( this.previewPanel != null ) {
			//pass
		} else {
			this.addCenterComponent( this.createMainComponent() );

			if( this.isPreviewDesired() ) {
				this.previewPanel = this.createPreviewPanel();
				org.lgna.croquet.components.PageAxisPanel northPanel = new org.lgna.croquet.components.PageAxisPanel(
						new org.lgna.croquet.components.LineAxisPanel( 
								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 16 ),
								new org.lgna.croquet.components.Label( "preview:", edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE, edu.cmu.cs.dennisc.java.awt.font.TextWeight.LIGHT ),
								org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 16 ),
								this.previewPanel
						),
						org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ),
						new org.lgna.croquet.components.HorizontalSeparator(),
						org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 )
				);
				this.addPageStartComponent( northPanel );
			}
		}
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.initializeIfNecessary();
	}
}
