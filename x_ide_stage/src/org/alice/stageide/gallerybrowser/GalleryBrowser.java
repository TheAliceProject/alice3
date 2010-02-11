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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
public class GalleryBrowser extends org.alice.ide.gallerybrowser.AbstractGalleryBrowser {
	private java.util.Map<String, String> map;

	public GalleryBrowser(java.io.File thumbnailRoot, java.util.Map<String, String> map) {
		this.map = map;
		this.initialize(thumbnailRoot);
		edu.cmu.cs.dennisc.zoot.ZButton createPersonButton = new edu.cmu.cs.dennisc.zoot.ZButton(new IndirectCreatePersonActionOperation());
		edu.cmu.cs.dennisc.zoot.ZButton createTextButton = new edu.cmu.cs.dennisc.zoot.ZButton(new CreateTextActionOperation());
		edu.cmu.cs.dennisc.zoot.ZButton createBillboardButton = new edu.cmu.cs.dennisc.zoot.ZButton(new CreateBillboardActionOperation());
		edu.cmu.cs.dennisc.zoot.ZButton createMyInstanceButton = new edu.cmu.cs.dennisc.zoot.ZButton(new CreateMyInstance());
		edu.cmu.cs.dennisc.zoot.ZButton createTextbookInstanceButton = new edu.cmu.cs.dennisc.zoot.ZButton(new CreateTextbookInstance());

		java.io.InputStream is = GalleryBrowser.class.getResourceAsStream("images/create_person.png");
		java.awt.Image image = edu.cmu.cs.dennisc.image.ImageUtilities.read(edu.cmu.cs.dennisc.image.ImageUtilities.PNG_CODEC_NAME, is);
		createPersonButton.setIcon(new javax.swing.ImageIcon(image));
		createPersonButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		createPersonButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

		edu.cmu.cs.dennisc.croquet.swing.Pane fromFilePane = new edu.cmu.cs.dennisc.croquet.swing.Pane();
		fromFilePane.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
		fromFilePane.add(createMyInstanceButton);
		fromFilePane.add(createTextbookInstanceButton);

		edu.cmu.cs.dennisc.croquet.swing.Pane bonusPane = new edu.cmu.cs.dennisc.croquet.swing.Pane();
		bonusPane.setLayout(new java.awt.GridLayout(2, 1, 0, 4));
		bonusPane.add(createBillboardButton);
		bonusPane.add(createTextButton);

		edu.cmu.cs.dennisc.croquet.swing.BorderPane buttonPane = new edu.cmu.cs.dennisc.croquet.swing.BorderPane();
		buttonPane.add(fromFilePane, java.awt.BorderLayout.NORTH);
		buttonPane.add(bonusPane, java.awt.BorderLayout.SOUTH);

		// this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4,
		// 4 ) );
		this.setBackground(new java.awt.Color(220, 220, 255));
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
		edu.cmu.cs.dennisc.zoot.ZFrame frame = new edu.cmu.cs.dennisc.zoot.ZFrame() {
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
