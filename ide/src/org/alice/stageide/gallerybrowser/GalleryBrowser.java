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
public class GalleryBrowser extends edu.cmu.cs.dennisc.croquet.BorderPanel {

	class DirectoryView extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
		private edu.cmu.cs.dennisc.croquet.TreeSelectionState.SelectionObserver<String> selectionObserver = new edu.cmu.cs.dennisc.croquet.TreeSelectionState.SelectionObserver<String>() {
			public void selectionChanged(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> nextValue) {
				DirectoryView.this.handleSelectionChanged( nextValue );
			}
		};
//		@Override
//		protected boolean isMaximumSizeClampedToPreferredSize() {
//			return true;
//		}
		@Override
		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			super.handleAddedTo(parent);
			treeSelectionState.addAndInvokeSelectionObserver(this.selectionObserver);
		}
		@Override
		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			treeSelectionState.addAndInvokeSelectionObserver(this.selectionObserver);
			super.handleRemovedFrom(parent);
		}
		
		private void handleSelectionChanged(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> nextValue) {
			this.removeAllComponents();
			if( nextValue != null ) {
				java.util.Enumeration<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> enumeration = nextValue.children();
				while( enumeration.hasMoreElements() ) {
					edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> child = enumeration.nextElement();
					String name = GalleryBrowser.this.getTextFor(child, false);
					if( name != null ) {
						if( child.isLeaf() ) {
							this.addComponent( new GalleryDragComponent( child, name ) );
						} else {
							edu.cmu.cs.dennisc.croquet.Operation<?, ?> operation = edu.cmu.cs.dennisc.croquet.SelectDirectoryActionOperation.getInstance(treeSelectionState, child, null);
							java.net.URL url = ResourceManager.getLargeIconResource( child );
							if (child instanceof edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode) {
								edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode directoryZipTreeNode = (edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode) child;
								edu.cmu.cs.dennisc.zip.ZipTreeNode thumbnailNode = directoryZipTreeNode.getChildNamed( "directoryThumbnail.png" );
								url = ResourceManager.getLargeIconResource( thumbnailNode );
							} else {
								url = null;
							}
							if( url != null ) {
								edu.cmu.cs.dennisc.javax.swing.icons.CompositeIcon icon = new edu.cmu.cs.dennisc.javax.swing.icons.CompositeIcon( new javax.swing.ImageIcon( url ), FOLDER_SMALL_ICON );
								operation.setSmallIcon( icon );
							} else {
								operation.setSmallIcon( FOLDER_LARGE_ICON );
							}
							if( operation != null ) {
								operation.setName( name );
								edu.cmu.cs.dennisc.croquet.Button button = operation.createButton();
								button.setVerticalTextPosition( edu.cmu.cs.dennisc.croquet.VerticalTextPosition.BOTTOM );
								button.setHorizontalTextPosition( edu.cmu.cs.dennisc.croquet.HorizontalTextPosition.CENTER );
								button.setAlignmentY( 0.0f );
								this.addComponent( button );
							}
						}
					}
				}
				this.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue() );
			}
			this.revalidateAndRepaint();
		}
	}
	
	private static java.util.Map<String, String> map;
	static {
		map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
		map.put( "thumbnails", "gallery" );
		map.put( "gallery", "Generic Alice Models" );
		map.put( "characters", "Looking Glass Characters" );
		map.put( "scenes", "Looking Glass Scenery" );
	}

	private static final int GAP = 4;
	private edu.cmu.cs.dennisc.croquet.TreeSelectionState<String> treeSelectionState;
	private static final javax.swing.ImageIcon FOLDER_LARGE_ICON = new javax.swing.ImageIcon(GalleryBrowser.class.getResource("images/folder.png"));
	private static final javax.swing.ImageIcon FOLDER_SMALL_ICON = new javax.swing.ImageIcon(GalleryBrowser.class.getResource("images/folder24.png"));
	
	public GalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> root ) {
		super(GAP * 2, 0);

		this.treeSelectionState = new edu.cmu.cs.dennisc.croquet.TreeSelectionState<String>( org.alice.ide.IDE.IDE_GROUP, java.util.UUID.fromString( "42798d37-0815-4ca8-9fb6-107d47e4642f" ), root, root ) {
			@Override
			protected edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
				throw new RuntimeException("todo");
			}
			@Override
			protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> value) {
				throw new RuntimeException("todo");
			}
		};
		
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
		
		edu.cmu.cs.dennisc.croquet.BorderPanel borderPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		borderPanel.setBackgroundColor( null );
		borderPanel.addComponent( this.treeSelectionState.createPathControl( this.createInitializer() ), Constraint.NORTH );

		edu.cmu.cs.dennisc.croquet.BorderPanel clampSizePanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		clampSizePanel.addComponent( new DirectoryView(), Constraint.WEST );
		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( clampSizePanel );
		scrollPane.getAwtComponent().getHorizontalScrollBar().setUnitIncrement( 16 );
		scrollPane.setBorder( null );
		scrollPane.setBackgroundColor( null );
		borderPanel.addComponent( scrollPane, Constraint.CENTER );
		
		this.setBackgroundColor( null );
		this.addComponent( createPersonButton, Constraint.WEST);
		this.addComponent( buttonPane, Constraint.EAST);
		this.addComponent( borderPanel, Constraint.CENTER );
	}

	private String getAdornedTextFor(String name, boolean isDirectory, boolean isRequestedByPath) {
		String rv;
		if (map != null) {
			if (map.containsKey(name)) {
				name = map.get(name);
			}
		}
		if (isRequestedByPath) {
			rv = name;
		} else {
			if (isDirectory) {
				rv = "<html><i>package:</i><br><strong>" + name + "</strong></html>";
			} else {
				rv = "<html><i>class:</i><br><strong>" + name + "</strong></html>";
			}
		}
		return rv;
	}

	private final String getTextFor(javax.swing.tree.TreeNode treeNode, boolean isRequestedByPath) {
		String name;
		if (treeNode instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode) {
			edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode) treeNode;
			name = zipTreeNode.getName();
			if (zipTreeNode.isLeaf()) {
				name = name.substring(0, name.length() - 4);
			}

		} else {
			name = treeNode.toString();
		}
		if( "directoryThumbnail".equals( name ) ) {
			return null;
		} else {
			return this.getAdornedTextFor(name, treeNode.isLeaf() == false, isRequestedByPath);
		}
	}

	private edu.cmu.cs.dennisc.croquet.PathControl.Initializer createInitializer() {
		return new edu.cmu.cs.dennisc.croquet.PathControl.Initializer() {
			public edu.cmu.cs.dennisc.croquet.ActionOperation configure(edu.cmu.cs.dennisc.croquet.ActionOperation rv, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
				if (treeNode instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode) {
					edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode) treeNode;
					rv.setName( zipTreeNode.getName() );
				} else {
					rv.setName( treeNode.toString() );
				}
				rv.setSmallIcon( FOLDER_SMALL_ICON );
				return rv;
			}
			public edu.cmu.cs.dennisc.croquet.Operation<?, ?> getOperationForLeaf(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
				String name = GalleryBrowser.this.getTextFor(treeNode, true);
				if( name != null ) {
					edu.cmu.cs.dennisc.croquet.Operation<?, ?> rv = GalleryFileActionOperation.getInstance( treeNode );
					rv.setName( name );
					return rv;
				} else {
					return null;
				}
			}
		};
	}
}
