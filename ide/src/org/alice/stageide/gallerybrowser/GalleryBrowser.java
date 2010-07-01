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
	private static java.util.Map<String, String> map;
	static {
		map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		map.put( "thumbnails", "gallery" );
		map.put( "gallery", "Generic Alice Models" );
		map.put( "characters", "Looking Glass Characters" );
		map.put( "scenes", "Looking Glass Scenery" );
	}

	public GalleryBrowser(javax.swing.tree.TreeNode thumbnailRoot) {
		super( thumbnailRoot );

		CreateTextActionOperation createTextActionOperation = new CreateTextActionOperation();
		CreateBillboardActionOperation createBillboardActionOperation = new CreateBillboardActionOperation();
		CreateMyInstanceActionOperation createMyInstanceActionOperation = new CreateMyInstanceActionOperation();
		CreateTextbookInstanceActionOperation createTextbookInstanceActionOperation = new CreateTextbookInstanceActionOperation();

		edu.cmu.cs.dennisc.croquet.GridPanel fromFilePane = edu.cmu.cs.dennisc.croquet.GridPanel.createGridPane( 2, 1, 0, 4 );
		fromFilePane.addComponent( createMyInstanceActionOperation.createButton());
		fromFilePane.addComponent(createTextbookInstanceActionOperation.createButton());

		edu.cmu.cs.dennisc.croquet.GridPanel bonusPane = edu.cmu.cs.dennisc.croquet.GridPanel.createGridPane( 2, 1, 0, 4 );
		bonusPane.addComponent(createBillboardActionOperation.createButton());
		bonusPane.addComponent(createTextActionOperation.createButton());

		edu.cmu.cs.dennisc.croquet.BorderPanel buttonPane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		buttonPane.addComponent(fromFilePane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH);
		buttonPane.addComponent(bonusPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.SOUTH);

		// this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4,
		// 4 ) );
		
		
		this.setBackgroundColor(new java.awt.Color(220, 220, 255));

		IndirectCreatePersonActionOperation indirectCreatePersonActionOperation = new IndirectCreatePersonActionOperation();
		edu.cmu.cs.dennisc.croquet.Button createPersonButton = indirectCreatePersonActionOperation.createButton();
		createPersonButton.setHorizontalTextPosition( edu.cmu.cs.dennisc.croquet.HorizontalTextPosition.CENTER );
		createPersonButton.setVerticalTextPosition( edu.cmu.cs.dennisc.croquet.VerticalTextPosition.BOTTOM );

		indirectCreatePersonActionOperation.setSmallIcon(new javax.swing.ImageIcon(GalleryBrowser.class.getResource("images/create_person.png")));
		
		this.addComponent(createPersonButton, Constraint.WEST);
		this.addComponent(buttonPane, Constraint.EAST);
	}

	@Override
	protected String getAdornedTextFor(String name, boolean isDirectory, boolean isRequestedByPath) {
		if (map != null) {
			if (map.containsKey(name)) {
				name = map.get(name);
			}
		}
		return super.getAdornedTextFor(name, isDirectory, isRequestedByPath);
	}

	
	@Override
	protected edu.cmu.cs.dennisc.croquet.PathControl.Initializer createInitializer() {
		
		return new edu.cmu.cs.dennisc.croquet.PathControl.Initializer() {
			public edu.cmu.cs.dennisc.croquet.ActionOperation configure(edu.cmu.cs.dennisc.croquet.ActionOperation rv, javax.swing.tree.TreeNode treeNode) {
				if (treeNode instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode) {
					edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode) treeNode;
					rv.setName( zipTreeNode.getName() );
				} else {
					rv.setName( treeNode.toString() );
				}
				return rv;
			}
			public edu.cmu.cs.dennisc.croquet.Operation<?, ?> getOperationForLeaf(javax.swing.tree.TreeNode treeNode) {
				String name;
				if (treeNode instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode) {
					edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode) treeNode;
					name = zipTreeNode.getName();
				} else {
					name = treeNode.toString();
				}
				if( "directoryThumbnail.png".equals( name ) ) {
					return null;
				} else {
					edu.cmu.cs.dennisc.croquet.Operation<?, ?> rv = GalleryFileActionOperation.getInstance( treeNode );
					rv.setName( name );
					return rv;
				}
			}
		};
	}

//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.constrainToMinimumHeight(super.getPreferredSize(), 256);
//	}

//	public static void main(String[] args) {
//
//		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
//
//		java.io.File thumbnailRoot = new java.io.File(org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails");
//		edu.cmu.cs.dennisc.javax.swing.ApplicationFrame frame = new edu.cmu.cs.dennisc.javax.swing.ApplicationFrame() {
//			@Override
//			protected void handleWindowOpened(java.awt.event.WindowEvent e) {
//			}
//			
//			@Override
//			protected void handleAbout( java.util.EventObject e ) {
//			}
//			@Override
//			protected void handlePreferences( java.util.EventObject e ) {
//			}
//
//			@Override
//			protected void handleQuit(java.util.EventObject e) {
//				System.exit(0);
//			}
//		};
//		frame.setSize(new java.awt.Dimension(1024, 256));
//		frame.getContentPane().add(new GalleryBrowser(thumbnailRoot, null));
//		frame.setVisible(true);
//	}
}
