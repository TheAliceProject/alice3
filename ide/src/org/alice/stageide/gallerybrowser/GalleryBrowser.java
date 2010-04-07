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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class GalleryBrowser extends org.alice.ide.gallerybrowser.AbstractGalleryBrowser {
	private java.util.Map<String, String> map;

	public GalleryBrowser(java.io.File thumbnailRoot, java.util.Map<String, String> map) {
		this.map = map;
		this.initialize(thumbnailRoot);
		
		CreateTextActionOperation createTextActionOperation = new CreateTextActionOperation();
		CreateBillboardActionOperation createBillboardActionOperation = new CreateBillboardActionOperation();
		CreateMyInstanceActionOperation createMyInstanceActionOperation = new CreateMyInstanceActionOperation();
		CreateTextbookInstanceActionOperation createTextbookInstanceActionOperation = new CreateTextbookInstanceActionOperation();

		edu.cmu.cs.dennisc.javax.swing.components.JPane fromFilePane = new edu.cmu.cs.dennisc.javax.swing.components.JPane();
		fromFilePane.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
		fromFilePane.add(edu.cmu.cs.dennisc.zoot.ZManager.createButton(createMyInstanceActionOperation));
		fromFilePane.add(edu.cmu.cs.dennisc.zoot.ZManager.createButton(createTextbookInstanceActionOperation));

		edu.cmu.cs.dennisc.javax.swing.components.JPane bonusPane = new edu.cmu.cs.dennisc.javax.swing.components.JPane();
		bonusPane.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
		bonusPane.add(edu.cmu.cs.dennisc.zoot.ZManager.createButton(createBillboardActionOperation));
		bonusPane.add(edu.cmu.cs.dennisc.zoot.ZManager.createButton(createTextActionOperation));

		edu.cmu.cs.dennisc.javax.swing.components.JBorderPane buttonPane = new edu.cmu.cs.dennisc.javax.swing.components.JBorderPane();
		buttonPane.add(fromFilePane, java.awt.BorderLayout.NORTH);
		buttonPane.add(bonusPane, java.awt.BorderLayout.SOUTH);

		// this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4,
		// 4 ) );
		
		
		this.setBackground(new java.awt.Color(220, 220, 255));

		IndirectCreatePersonActionOperation indirectCreatePersonActionOperation = new IndirectCreatePersonActionOperation();
		javax.swing.JButton createPersonButton = edu.cmu.cs.dennisc.zoot.ZManager.createButton(indirectCreatePersonActionOperation);
		createPersonButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		createPersonButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

		//todo
		java.io.InputStream is = GalleryBrowser.class.getResourceAsStream("images/create_person.png");
		java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read(edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is);
		indirectCreatePersonActionOperation.setSmallIcon(new javax.swing.ImageIcon(image));
		
		this.add(createPersonButton, java.awt.BorderLayout.WEST);
		this.add(buttonPane, java.awt.BorderLayout.EAST);
	}

	@Override
	protected String getAdornedTextFor(String name, boolean isDirectory, boolean isRequestedByPath) {
		if (this.map != null) {
			if (this.map.containsKey(name)) {
				name = this.map.get(name);
			}
		}
		return super.getAdornedTextFor(name, isDirectory, isRequestedByPath);
	}

	@Override
	protected void handleFileActivation(java.io.File file) {
		assert file.isFile();
		edu.cmu.cs.dennisc.zoot.ZManager.performIfAppropriate(new GalleryFileActionOperation(file), null, edu.cmu.cs.dennisc.zoot.ZManager.CANCEL_IS_WORTHWHILE);
	}

	@Override
	public java.awt.Dimension getPreferredSize() {
		return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumHeight(super.getPreferredSize(), 256);
	}

	public static void main(String[] args) {

		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();

		java.io.File thumbnailRoot = new java.io.File(org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails");
		edu.cmu.cs.dennisc.javax.swing.ApplicationFrame frame = new edu.cmu.cs.dennisc.javax.swing.ApplicationFrame() {
			@Override
			protected void handleWindowOpened(java.awt.event.WindowEvent e) {
			}
			
			@Override
			protected void handleAbout( java.util.EventObject e ) {
			}
			@Override
			protected void handlePreferences( java.util.EventObject e ) {
			}

			@Override
			protected void handleQuit(java.util.EventObject e) {
				System.exit(0);
			}
		};
		frame.setSize(new java.awt.Dimension(1024, 256));
		frame.getContentPane().add(new GalleryBrowser(thumbnailRoot, null));
		frame.setVisible(true);
	}
}
