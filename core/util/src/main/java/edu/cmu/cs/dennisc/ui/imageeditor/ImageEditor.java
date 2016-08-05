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
package edu.cmu.cs.dennisc.ui.imageeditor;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditor extends javax.swing.JFrame {
	private ImagePane m_imagePane = new ImagePane();

	public ImageEditor() {
		class MyFileTransferHandler extends edu.cmu.cs.dennisc.javax.swing.transfer.FileTransferHandler {
			@Override
			protected void handleFiles( java.util.List<java.io.File> files ) {
				java.io.File file0 = files.get( 0 );
				try {
					ImageEditor.this.load( file0 );
				} catch( java.io.IOException ioe ) {
					throw new RuntimeException( file0.getAbsolutePath(), ioe );
				}
			}
		}

		MyFileTransferHandler myFileTransferHandler = new MyFileTransferHandler();
		m_imagePane.setTransferHandler( myFileTransferHandler );
		getContentPane().add( m_imagePane, java.awt.BorderLayout.CENTER );
	}

	private void load( java.io.File file ) throws java.io.IOException {
		m_imagePane.setImage( edu.cmu.cs.dennisc.image.ImageUtilities.read( file ) );
	}

	public static void main( String[] args ) {
		ImageEditor imageEditor = new ImageEditor();
		imageEditor.setDefaultCloseOperation( javax.swing.WindowConstants.EXIT_ON_CLOSE );
		imageEditor.pack();
		imageEditor.setVisible( true );
	}
}
