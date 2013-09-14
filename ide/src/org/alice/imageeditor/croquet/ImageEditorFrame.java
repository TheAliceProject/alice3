/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.imageeditor.croquet;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorFrame extends org.lgna.croquet.FrameComposite<org.alice.imageeditor.croquet.views.ImageEditorPane> {
	private final org.lgna.croquet.ValueHolder<java.awt.Image> imageHolder = new org.lgna.croquet.ValueHolder<java.awt.Image>();
	private final java.util.List<java.awt.Shape> shapes = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();

	public ImageEditorFrame() {
		super( java.util.UUID.fromString( "19b37463-3d9a-44eb-9682-6d5ddf73f5b3" ), org.lgna.croquet.Application.DOCUMENT_UI_GROUP ); //todo?
	}

	public org.lgna.croquet.ValueHolder<java.awt.Image> getImageHolder() {
		return this.imageHolder;
	}

	public void addShape( java.awt.Shape shape ) {
		this.shapes.add( shape );
		java.awt.Image image = this.getView().render();
		if( image != null ) {
			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
		}
	}

	public void removeShape( java.awt.Shape shape ) {
		this.shapes.remove( shape );
		java.awt.Image image = this.getView().render();
		if( image != null ) {
			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
		}
	}

	public void clearShapes() {
		this.shapes.clear();
		java.awt.Image image = this.imageHolder.getValue();
		if( image != null ) {
			edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
		}
	}

	public java.util.List<java.awt.Shape> getShapes() {
		return java.util.Collections.unmodifiableList( this.shapes );
	}

	@Override
	protected org.alice.imageeditor.croquet.views.ImageEditorPane createView() {
		return new org.alice.imageeditor.croquet.views.ImageEditorPane( this );
	}

	public void setImageClearShapesAndShowFrame( java.awt.Image image ) {
		this.clearShapes();
		this.imageHolder.setValue( image );
		this.getBooleanState().setValueTransactionlessly( true );
		org.lgna.croquet.components.AbstractWindow<?> window = this.getView().getRoot();
		if( window != null ) {
			window.pack();
		}
	}

	public static void main( String[] args ) {
		javax.swing.ImageIcon icon = new javax.swing.ImageIcon( org.alice.stageide.StageIDE.class.getResource( "images/SplashScreen.png" ) );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		ImageEditorFrame imageComposite = new ImageEditorFrame();
		imageComposite.setImageClearShapesAndShowFrame( icon.getImage() );
	}
}
