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
package org.alice.ide.capture;

/**
 * @author Dennis Cosgrove
 */
public class ImageCaptureComposite extends org.lgna.croquet.FrameCompositeWithInternalIsShowingState<org.alice.ide.capture.views.ImageCaptureView> {
	//todo
	private static final org.lgna.croquet.Group IMAGE_CAPTURE_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "220c4c60-aea1-4c18-95f9-7a0ef0a1f30b" ), "IMAGE_CAPTURE_GROUP" );

	private static class SingletonHolder {
		private static ImageCaptureComposite instance = new ImageCaptureComposite();
	}

	public static ImageCaptureComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static final int LAYER_ID = javax.swing.JLayeredPane.POPUP_LAYER + 1;

	private org.alice.ide.capture.views.ImageCaptureRectangleStencilView getImageCaptureRectangleStencilView( org.lgna.croquet.views.AbstractWindow<?> window ) {
		return mapWindowToStencilView.getInitializingIfAbsent( window, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.croquet.views.AbstractWindow<?>, org.alice.ide.capture.views.ImageCaptureRectangleStencilView>() {
			@Override
			public org.alice.ide.capture.views.ImageCaptureRectangleStencilView initialize( org.lgna.croquet.views.AbstractWindow<?> key ) {
				return new org.alice.ide.capture.views.ImageCaptureRectangleStencilView( key, LAYER_ID, ImageCaptureComposite.this );
			}
		} );
	}

	private final org.lgna.croquet.Operation captureEntireWindowOperation = this.createActionOperation( "captureEntireWindow", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			org.lgna.croquet.Application app = org.lgna.croquet.Application.getActiveInstance();
			org.lgna.croquet.views.AbstractWindow<?> window = app.getDocumentFrame().peekWindow();
			java.awt.Image image = edu.cmu.cs.dennisc.capture.ImageCaptureUtilities.captureComplete( window.getAwtComponent(), getDpi() );
			image = convertToRgbaIfNecessary( image );

			org.alice.ide.capture.views.ImageCaptureRectangleStencilView stencilView = getImageCaptureRectangleStencilView( window );
			stencilView.getImageComposite().setImageClearShapesAndShowFrame( image );
			//edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
			return null;
		}
	} );

	private final org.lgna.croquet.Operation captureEntireContentPaneOperation = this.createActionOperation( "captureEntireContentPane", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			org.lgna.croquet.Application app = org.lgna.croquet.Application.getActiveInstance();
			org.lgna.croquet.views.AbstractWindow<?> window = app.getDocumentFrame().peekWindow();
			java.awt.Image image = edu.cmu.cs.dennisc.capture.ImageCaptureUtilities.captureComplete( window.getRootPane().getAwtComponent(), getDpi() );
			image = convertToRgbaIfNecessary( image );
			org.alice.ide.capture.views.ImageCaptureRectangleStencilView stencilView = getImageCaptureRectangleStencilView( window );
			stencilView.getImageComposite().setImageClearShapesAndShowFrame( image );
			//edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities.setClipboardContents( image );
			return null;
		}
	} );

	private final org.lgna.croquet.Operation captureRectangleOperation = this.createActionOperation( "captureRectangle", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			org.lgna.croquet.Application app = org.lgna.croquet.Application.getActiveInstance();
			org.lgna.croquet.views.AbstractWindow<?> window = app.getDocumentFrame().peekWindow();
			org.alice.ide.capture.views.ImageCaptureRectangleStencilView stencilView = getImageCaptureRectangleStencilView( window );
			stencilView.setStencilShowing( stencilView.isStencilShowing() == false );
			return null;
		}
	} );

	private final org.lgna.croquet.PlainStringValue operationsHeader = this.createStringValue( "operationsHeader" );
	private final org.lgna.croquet.PlainStringValue propertiesHeader = this.createStringValue( "propertiesHeader" );
	private final org.lgna.croquet.BoundedIntegerState dpiState = this.createBoundedIntegerState( "dpiState", new BoundedIntegerDetails().minimum( 0 ).maximum( 3000 ).initialValue( 300 ) );
	private final org.lgna.croquet.BooleanState isAlphaChannelState = this.createPreferenceBooleanState( "isAlphaChannelState", false );

	private final edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.croquet.views.AbstractWindow<?>, org.alice.ide.capture.views.ImageCaptureRectangleStencilView> mapWindowToStencilView = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	private ImageCaptureComposite() {
		super( java.util.UUID.fromString( "84f73ef2-a5d1-4784-a902-45343434b0f0" ), IMAGE_CAPTURE_GROUP );
	}

	public java.awt.Image convertToRgbaIfNecessary( java.awt.Image image ) {
		if( this.isAlphaChannelState.getValue() ) {
			int imageType = java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE; //to pre or not to pre
			return edu.cmu.cs.dennisc.image.ImageUtilities.createBufferedImage( image, imageType );
		} else {
			return image;
		}
	}

	public org.lgna.croquet.PlainStringValue getOperationsHeader() {
		return this.operationsHeader;
	}

	public org.lgna.croquet.PlainStringValue getPropertiesHeader() {
		return this.propertiesHeader;
	}

	public org.lgna.croquet.Operation getCaptureEntireWindowOperation() {
		return this.captureEntireWindowOperation;
	}

	public org.lgna.croquet.Operation getCaptureEntireContentPaneOperation() {
		return this.captureEntireContentPaneOperation;
	}

	public org.lgna.croquet.Operation getCaptureRectangleOperation() {
		return this.captureRectangleOperation;
	}

	public org.lgna.croquet.BoundedIntegerState getDpiState() {
		return this.dpiState;
	}

	public org.lgna.croquet.BooleanState getIsAlphaChannelState() {
		return this.isAlphaChannelState;
	}

	public int getDpi() {
		final boolean IS_SPINNER_UPDATING_CORRECTLY = false;
		if( IS_SPINNER_UPDATING_CORRECTLY ) {
			return this.dpiState.getValue();
		} else {
			return (Integer)this.dpiState.getSwingModel().getSpinnerModel().getValue();
		}
	}

	@Override
	protected org.alice.ide.capture.views.ImageCaptureView createView() {
		return new org.alice.ide.capture.views.ImageCaptureView( this );
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );
		org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
		ImageCaptureComposite.getInstance().getIsFrameShowingState().getImp().getSwingModel().getButtonModel().setSelected( true );
	}

}
