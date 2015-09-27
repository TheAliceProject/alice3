/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.imageeditor.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class SaveOverPane extends org.lgna.croquet.views.MigPanel {
	private final ImageView toBeReplacedImageView = new ImageView();
	private final ImageView nextImageView = new ImageView();
	private final org.lgna.croquet.views.Label toBeReplacedHeaderLabel = new org.lgna.croquet.views.Label();
	private final org.lgna.croquet.views.Label toBeReplacedDetailsLabel = new org.lgna.croquet.views.Label();
	private final org.lgna.croquet.views.Label nextDetailsLabel = new org.lgna.croquet.views.Label();

	public SaveOverPane( org.alice.imageeditor.croquet.SaveOverComposite composite ) {
		super( composite, "fill", "[50%][grow 0][50%]", "[grow 0, shrink 0][grow, shrink][grow 0,shrink 0]" );
		this.addComponent( this.toBeReplacedHeaderLabel );
		this.addComponent( org.lgna.croquet.views.Separator.createInstanceSeparatingLeftFromRight(), "spany 3, growy" );
		this.addComponent( composite.getNextHeader().createLabel(), "wrap" );
		this.addComponent( this.toBeReplacedImageView, "grow, shrink" );
		this.addComponent( this.nextImageView, "skip 1, grow, shrink, wrap" );
		this.addComponent( this.toBeReplacedDetailsLabel );
		this.addComponent( this.nextDetailsLabel, "skip 1" );
		this.setMinimumPreferredHeight( 300 );
		this.setMinimumPreferredWidth( 800 );
	}

	@Override
	public org.alice.imageeditor.croquet.SaveOverComposite getComposite() {
		return (org.alice.imageeditor.croquet.SaveOverComposite)super.getComposite();
	}

	private static String getResolutionText( java.awt.Image image ) {
		StringBuilder sb = new StringBuilder();
		//sb.append( "(" );
		sb.append( "resolution: " );
		sb.append( image.getWidth( null ) );
		sb.append( " x " );
		sb.append( image.getHeight( null ) );
		//sb.append( ")" );
		return sb.toString();
	}

	@Override
	public void handleCompositePreActivation() {
		org.alice.imageeditor.croquet.ImageEditorFrame frame = this.getComposite().getOwner().getOwner();
		java.io.File file = frame.getFile();
		try {
			java.awt.Image toBeReplacedImage = edu.cmu.cs.dennisc.image.ImageUtilities.read( file );
			java.awt.Image nextImage = frame.getView().render();
			this.toBeReplacedImageView.setImage( toBeReplacedImage );
			this.nextImageView.setImage( nextImage );

			java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance( java.text.DateFormat.SHORT, java.util.Locale.getDefault() );
			java.text.DateFormat timeFormat = java.text.DateFormat.getTimeInstance( java.text.DateFormat.SHORT, java.util.Locale.getDefault() );
			java.util.Date date = new java.util.Date( file.lastModified() );
			this.toBeReplacedHeaderLabel.setText( this.getComposite().getPrevHeader().getText() + " (last modified: " + dateFormat.format( date ) + " " + timeFormat.format( date ) + ")" );
			this.toBeReplacedDetailsLabel.setText( getResolutionText( toBeReplacedImage ) );
			this.nextDetailsLabel.setText( getResolutionText( nextImage ) );
			super.handleCompositePreActivation();
			org.lgna.croquet.views.AbstractWindow<?> window = this.getRoot();
			if( window instanceof org.lgna.croquet.views.Dialog ) {
				org.lgna.croquet.views.Dialog dialog = (org.lgna.croquet.views.Dialog)window;
				dialog.setTitle( "Save Over " + file );
				dialog.pack();
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( file.getAbsolutePath(), ioe );
		}
	}
}
