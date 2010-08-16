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

import org.alice.stageide.croquet.models.gallerybrowser.Create3dTextOperation;
import org.alice.stageide.croquet.models.gallerybrowser.CreateBillboardOperation;
import org.alice.stageide.croquet.models.gallerybrowser.CreateMyInstanceOperation;
import org.alice.stageide.croquet.models.gallerybrowser.CreateTextbookInstanceOperation;
import org.alice.stageide.croquet.models.gallerybrowser.GalleryFileOperation;

import edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint;

/**
 * @author Dennis Cosgrove
 */
public class GalleryBrowser extends edu.cmu.cs.dennisc.croquet.BorderPanel {

	private static edu.cmu.cs.dennisc.croquet.PathControl.Initializer initializer = new edu.cmu.cs.dennisc.croquet.PathControl.Initializer() {
		public edu.cmu.cs.dennisc.croquet.ActionOperation configure(edu.cmu.cs.dennisc.croquet.ActionOperation rv, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
			javax.swing.Icon icon;
			if (treeNode instanceof edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode) {
				edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode directoryZipTreeNode = (edu.cmu.cs.dennisc.zip.DirectoryZipTreeNode) treeNode;
				edu.cmu.cs.dennisc.zip.ZipTreeNode thumbnailNode = directoryZipTreeNode.getChildNamed( "directoryThumbnail.png" );
				icon = ResourceManager.getLargeIcon( thumbnailNode );
			} else {
				icon = ResourceManager.getLargeIcon( treeNode );
			}
			if( icon != null ) {
				rv.setSmallIcon( new edu.cmu.cs.dennisc.javax.swing.icons.CompositeIcon( icon, FOLDER_SMALL_ICON ) );
			} else {
				rv.setSmallIcon( FOLDER_LARGE_ICON );
			}
			rv.setName( GalleryBrowser.getTextFor(treeNode, false) );
			return rv;
		}
		public edu.cmu.cs.dennisc.croquet.Operation<?> getOperationForLeaf(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
			return null;
		}
	};
	
	private enum Criterion {
		STARTS_WITH {
			@Override
			public boolean accept(String lcName, String lcFilter) {
				return lcName.startsWith(lcFilter);
			}
		},
		CONTAINS_BUT_DOES_NOT_START_WITH {
			@Override
			public boolean accept(String lcName, String lcFilter) {
				return lcName.startsWith(lcFilter)==false && lcName.contains(lcFilter);
			}
		};
		public abstract boolean accept( String lcName, String lcFilter );
	}

	class DirectoryView extends edu.cmu.cs.dennisc.croquet.LineAxisPanel {
		private edu.cmu.cs.dennisc.croquet.TreeSelectionState.SelectionObserver<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> selectionObserver = new edu.cmu.cs.dennisc.croquet.TreeSelectionState.SelectionObserver<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>>() {
			public void selectionChanged(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> nextValue) {
				DirectoryView.this.handleSelectionChanged( nextValue );
			}
		};
		private edu.cmu.cs.dennisc.croquet.StringState.ValueObserver filterObserver = new edu.cmu.cs.dennisc.croquet.StringState.ValueObserver() {
			public void changed(String nextValue) {
				DirectoryView.this.handleFilterChanged( nextValue );
			}		
		};
		@Override
		protected void handleAddedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			super.handleAddedTo(parent);
			treeSelectionState.addAndInvokeSelectionObserver(this.selectionObserver);
			filterState.addAndInvokeValueObserver( this.filterObserver );
		}
		@Override
		protected void handleRemovedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
			filterState.removeValueObserver( this.filterObserver );
			treeSelectionState.removeSelectionObserver(this.selectionObserver);
			super.handleRemovedFrom(parent);
		}
		
		private java.util.LinkedList<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> update( java.util.LinkedList<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> rv, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode, String lcFilter, Criterion criterion ) {
			String path = treeNode.getValue();
			if( path != null ) {
				String[] chunks = path.split( "/" );
				if( chunks.length > 0 ) {
					String name = chunks[ chunks.length-1 ];
					String lcName = name.toLowerCase();
					if( criterion.accept(lcName, lcFilter) ) {
						rv.add( treeNode );
					}
				}
			}
			for( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> child : treeNode ) {
				update( rv, child, lcFilter, criterion );
			}
			return rv;
		}

		private void refresh() {
			this.removeAllComponents();
			String filter = filterState.getValue();
			Iterable<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> iterable;
			if( filter != null && filter.length() > 0 ) {
				java.util.LinkedList<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>> list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				String lcFilter = filter.toLowerCase();
				update( list, treeSelectionState.getTreeModel().getRoot(), lcFilter, Criterion.STARTS_WITH );
				if( lcFilter.length() > 1 ) {
					update( list, treeSelectionState.getTreeModel().getRoot(), lcFilter, Criterion.CONTAINS_BUT_DOES_NOT_START_WITH );
				}
				iterable = list;
			} else {
				iterable = treeSelectionState.getSelection();
			}
			if( iterable != null ) {
				for( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> child : iterable ) {
					String name = GalleryBrowser.this.getTextFor(child, false);
					if( name != null ) {
						if( child.isLeaf() ) {
							this.addComponent( GalleryDragComponent.getInstance( child ) );
						} else {
							edu.cmu.cs.dennisc.croquet.Operation<?> operation = edu.cmu.cs.dennisc.croquet.SelectDirectoryActionOperation.getInstance(treeSelectionState, child, initializer );
							if( operation != null ) {
								edu.cmu.cs.dennisc.croquet.Button button = operation.createButton();
								button.setVerticalTextPosition( edu.cmu.cs.dennisc.croquet.VerticalTextPosition.BOTTOM );
								button.setHorizontalTextPosition( edu.cmu.cs.dennisc.croquet.HorizontalTextPosition.CENTER );
								button.setAlignmentY( java.awt.Component.TOP_ALIGNMENT );
								this.addComponent( button );
							}
						}
					}
				}
			}
			this.revalidateAndRepaint();
		}
		private void handleFilterChanged( String filter ) {
			this.refresh();
		}
		private void handleSelectionChanged(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> nextValue) {
			this.refresh();
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
	private static final javax.swing.ImageIcon FOLDER_LARGE_ICON = new javax.swing.ImageIcon(GalleryBrowser.class.getResource("images/folder.png"));
	private static final javax.swing.ImageIcon FOLDER_SMALL_ICON = new javax.swing.ImageIcon(GalleryBrowser.class.getResource("images/folder24.png"));
	
	private edu.cmu.cs.dennisc.croquet.TreeSelectionState< edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> > treeSelectionState;
	private edu.cmu.cs.dennisc.croquet.StringState filterState;

	public GalleryBrowser( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> root ) {
		//super(GAP * 2, 0);

		edu.cmu.cs.dennisc.javax.swing.models.DefaultTreeModel<String> treeModel = new edu.cmu.cs.dennisc.javax.swing.models.DefaultTreeModel<String>( root );
		this.treeSelectionState = new edu.cmu.cs.dennisc.croquet.TreeSelectionState<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>>( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "42798d37-0815-4ca8-9fb6-107d47e4642f" ), treeModel, treeModel.getRoot() ) {
			
			@Override
			protected edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> decodeValue(edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder) {
				throw new RuntimeException("todo");
			}
			@Override
			protected void encodeValue(edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> value) {
				throw new RuntimeException("todo");
			}
		};
		
		this.filterState = new edu.cmu.cs.dennisc.croquet.StringState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "8648d640-5676-4627-a002-44db06ce58ce" ), "" );
		
		this.treeSelectionState.addSelectionObserver( new edu.cmu.cs.dennisc.croquet.TreeSelectionState.SelectionObserver<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>>() {
			public void selectionChanged(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> nextValue) {
				filterState.setValue( "" );
			}
		} );

		final DirectoryView directoryView = new DirectoryView();
		
		final edu.cmu.cs.dennisc.croquet.TextField filterTextField = this.filterState.createTextField();
		filterTextField.setMinimumPreferredWidth( 320 );
		filterTextField.setMaximumSizeClampedToPreferredSize( true );
		filterTextField.getAwtComponent().setTextForBlankCondition( "search entire gallery" );
		filterTextField.scaleFont( 1.5f );
		org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().addAndInvokeValueObserver( new edu.cmu.cs.dennisc.croquet.BooleanState.ValueObserver() {
			public void changing(boolean nextValue) {
			}
			public void changed(boolean nextValue) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						filterTextField.requestFocus();
					}
				} );
			}
		} );

		edu.cmu.cs.dennisc.croquet.GridPanel fromFilePane = edu.cmu.cs.dennisc.croquet.GridPanel.createGridPane( 2, 1, 0, 4 );
		fromFilePane.addComponent( CreateMyInstanceOperation.getInstance().createButton());
		fromFilePane.addComponent( CreateTextbookInstanceOperation.getInstance().createButton());

		edu.cmu.cs.dennisc.croquet.GridPanel bonusPane = edu.cmu.cs.dennisc.croquet.GridPanel.createGridPane( 2, 1, 0, 4 );
		bonusPane.addComponent(CreateBillboardOperation.getInstance().createButton());
		bonusPane.addComponent(Create3dTextOperation.getInstance().createButton());

		edu.cmu.cs.dennisc.croquet.BorderPanel buttonPane = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		buttonPane.addComponent(fromFilePane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_START);
		buttonPane.addComponent(bonusPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.PAGE_END);

		
		this.setBackgroundColor(new java.awt.Color(220, 220, 255));

		org.alice.stageide.croquet.models.gallerybrowser.CreatePersonFieldOperation createPersonFieldOperation = org.alice.stageide.croquet.models.gallerybrowser.CreatePersonFieldOperation.getInstance();
		edu.cmu.cs.dennisc.croquet.Button createPersonButton = createPersonFieldOperation.createButton();
		createPersonButton.setHorizontalTextPosition( edu.cmu.cs.dennisc.croquet.HorizontalTextPosition.CENTER );
		createPersonButton.setVerticalTextPosition( edu.cmu.cs.dennisc.croquet.VerticalTextPosition.BOTTOM );

		createPersonFieldOperation.setSmallIcon(new javax.swing.ImageIcon(GalleryBrowser.class.getResource("images/create_person.png")));

		edu.cmu.cs.dennisc.croquet.LineAxisPanel pathControlPanel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel();
		pathControlPanel.addComponent( this.treeSelectionState.createPathControl( this.createInitializer() ) );
		pathControlPanel.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalGlue() );
		pathControlPanel.addComponent( filterTextField );
		
		edu.cmu.cs.dennisc.croquet.BorderPanel borderPanel = new edu.cmu.cs.dennisc.croquet.BorderPanel( 0, GAP );
		borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		borderPanel.setBackgroundColor( null );
		borderPanel.addComponent( pathControlPanel, Constraint.PAGE_START );

		edu.cmu.cs.dennisc.croquet.BorderPanel clampSizePanel = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		clampSizePanel.addComponent( directoryView, Constraint.LINE_START );
		edu.cmu.cs.dennisc.croquet.ScrollPane scrollPane = new edu.cmu.cs.dennisc.croquet.ScrollPane( clampSizePanel );
		scrollPane.getAwtComponent().getHorizontalScrollBar().setUnitIncrement( 16 );
		scrollPane.setBorder( null );
		scrollPane.setBackgroundColor( null );
		borderPanel.addComponent( scrollPane, Constraint.CENTER );
		
		this.setBackgroundColor( null );
		this.addComponent( createPersonButton, Constraint.LINE_START);
		this.addComponent( buttonPane, Constraint.LINE_END);
		this.addComponent( borderPanel, Constraint.CENTER );
	}

	private static String getAdornedTextFor(String name, boolean isDirectory, boolean isRequestedByPath) {
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
	
	/*package-private*/ final static String getTextFor(javax.swing.tree.TreeNode treeNode, boolean isRequestedByPath) {
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
			return getAdornedTextFor(name, treeNode.isLeaf() == false, isRequestedByPath);
		}
	}

	private edu.cmu.cs.dennisc.croquet.PathControl.Initializer createInitializer() {
		return new edu.cmu.cs.dennisc.croquet.PathControl.Initializer() {
			public edu.cmu.cs.dennisc.croquet.ActionOperation configure(edu.cmu.cs.dennisc.croquet.ActionOperation rv, edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
//				if (treeNode instanceof edu.cmu.cs.dennisc.zip.ZipTreeNode) {
//					edu.cmu.cs.dennisc.zip.ZipTreeNode zipTreeNode = (edu.cmu.cs.dennisc.zip.ZipTreeNode) treeNode;
//					rv.setName( zipTreeNode.getName() );
//				} else {
//					rv.setName( treeNode.toString() );
//				}
				rv.setName( getTextFor(treeNode, true) );
				rv.setSmallIcon( FOLDER_SMALL_ICON );
				return rv;
			}
			public edu.cmu.cs.dennisc.croquet.Operation<?> getOperationForLeaf(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
				String name = GalleryBrowser.this.getTextFor(treeNode, true);
				if( name != null ) {
					edu.cmu.cs.dennisc.croquet.Operation<?> rv = GalleryFileOperation.getInstance( treeNode );
					rv.setName( name );
					rv.setSmallIcon( ResourceManager.getSmallIcon(treeNode) );
					return rv;
				} else {
					return null;
				}
			}
		};
	}
}
