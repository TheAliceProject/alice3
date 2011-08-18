/**
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import org.alice.stageide.croquet.models.gallerybrowser.Create3dTextOperation;
import org.alice.stageide.croquet.models.gallerybrowser.CreateMyInstanceOperation;
import org.alice.stageide.croquet.models.gallerybrowser.GalleryClassOperation;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.DefaultTreeSelectionState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.State;
import org.lgna.croquet.StringState;
import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.HorizontalTextPosition;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.croquet.components.TextField;
import org.lgna.croquet.components.VerticalTextPosition;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.resourceutilities.ModelResourceTreeNode;
import org.lgna.story.resourceutilities.ModelResourceTreeNodeModel;
import org.lgna.story.resourceutilities.ModelResourceUtilities;

/**
 * @author dculyba
 *
 */
public class ClassBasedGalleryBrowser extends BorderPanel {

	private static org.alice.stageide.croquet.components.gallerybrowser.ClassBasedPathControl.Initializer initializer = new org.alice.stageide.croquet.components.gallerybrowser.ClassBasedPathControl.Initializer() {
		public ActionOperation configure(ActionOperation rv, ModelResourceTreeNode treeNode) {
			javax.swing.Icon icon;
			NamedUserType classValue = treeNode.getUserType();
			BufferedImage thumbnail = null;
			Class<?> resourceClass = treeNode.getResourceClass();
			thumbnail = ModelResourceUtilities.getThumbnail(resourceClass);
			if( thumbnail != null ) {
				icon = new ImageIcon(thumbnail);
				rv.setSmallIcon( new edu.cmu.cs.dennisc.javax.swing.icons.CompositeIcon( icon, FOLDER_SMALL_ICON ) );
			} else {
				rv.setSmallIcon( FOLDER_LARGE_ICON );
			}
			rv.setName( ClassBasedGalleryBrowser.getTextFor(treeNode, false) );
			return rv;
		}
		public Operation<?> getOperationForLeaf(ModelResourceTreeNode treeNode) {
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

	class DirectoryView extends LineAxisPanel {
		private org.lgna.croquet.TreeSelectionState.ValueObserver<ModelResourceTreeNode> selectionObserver = new org.lgna.croquet.TreeSelectionState.ValueObserver<ModelResourceTreeNode>() {
			public void changing( org.lgna.croquet.State< org.lgna.story.resourceutilities.ModelResourceTreeNode > state, org.lgna.story.resourceutilities.ModelResourceTreeNode prevValue, org.lgna.story.resourceutilities.ModelResourceTreeNode nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< org.lgna.story.resourceutilities.ModelResourceTreeNode > state, org.lgna.story.resourceutilities.ModelResourceTreeNode prevValue, org.lgna.story.resourceutilities.ModelResourceTreeNode nextValue, boolean isAdjusting ) {
				DirectoryView.this.handleSelectionChanged( nextValue );
			}
		};
		private org.lgna.croquet.StringState.ValueObserver<String> filterObserver = new org.lgna.croquet.StringState.ValueObserver<String>() {
			public void changed(State<String> state, String prevValue,
					String nextValue, boolean isAdjusting) {
				DirectoryView.this.handleFilterChanged( nextValue );
				
			}
			public void changing(State<String> state, String prevValue,
					String nextValue, boolean isAdjusting) {
				// TODO Auto-generated method stub
				
			}		
		};
		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			treeSelectionState.addAndInvokeValueObserver(this.selectionObserver);
			filterState.addAndInvokeValueObserver( this.filterObserver );
		}
		@Override
		protected void handleUndisplayable() {
			filterState.removeValueObserver( this.filterObserver );
			treeSelectionState.removeValueObserver(this.selectionObserver);
			super.handleUndisplayable();
		}
		
		private java.util.LinkedList<ModelResourceTreeNode> update( java.util.LinkedList<ModelResourceTreeNode> rv, ModelResourceTreeNode treeNode, String lcFilter, Criterion criterion ) {

			//TODO: do this
//			String path = treeNode.getValue();
//			if( path != null ) {
//				String[] chunks = path.split( "/" );
//				if( chunks.length > 0 ) {
//					String name = chunks[ chunks.length-1 ];
//					String lcName = name.toLowerCase();
//					if( criterion.accept(lcName, lcFilter) ) {
//						rv.add( treeNode );
//					}
//				}
//			}
//			for( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> child : treeNode ) {
//				update( rv, child, lcFilter, criterion );
//			}
			return rv;
		}

		private void refresh() {
			this.removeAllComponents();
			String filter = filterState.getValue();
			Iterable<ModelResourceTreeNode> iterable;
			if( filter != null && filter.length() > 0 ) {
				java.util.LinkedList<ModelResourceTreeNode> list = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				String lcFilter = filter.toLowerCase();
				update( list, treeSelectionState.getTreeModel().getRoot(), lcFilter, Criterion.STARTS_WITH );
				if( lcFilter.length() > 1 ) {
					update( list, treeSelectionState.getTreeModel().getRoot(), lcFilter, Criterion.CONTAINS_BUT_DOES_NOT_START_WITH );
				}
				iterable = list;
			} else {
				Object i = treeSelectionState.getSelectedNode();
				iterable = (Iterable<ModelResourceTreeNode>)i;
			}
			if( iterable != null ) {
				for( ModelResourceTreeNode child : iterable ) {
					String name = ClassBasedGalleryBrowser.getTextFor(child, false);
					if( name != null ) {
						if( child.isLeaf() ) {
							this.addComponent( ClassBasedGalleryDragComponent.getInstance( child ) );
						} else {
							org.lgna.croquet.Operation<?> operation = org.lgna.croquet.SelectClassActionOperation.getInstance(treeSelectionState, child, initializer );
							if( operation != null ) {
								Button button = operation.createButton();
								button.setVerticalTextPosition( VerticalTextPosition.BOTTOM );
								button.setHorizontalTextPosition( HorizontalTextPosition.CENTER );
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
		private void handleSelectionChanged(ModelResourceTreeNode nextValue) {
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
	private static final javax.swing.ImageIcon FOLDER_LARGE_ICON = new javax.swing.ImageIcon(ClassBasedGalleryBrowser.class.getResource("images/folder.png"));
	private static final javax.swing.ImageIcon FOLDER_SMALL_ICON = new javax.swing.ImageIcon(ClassBasedGalleryBrowser.class.getResource("images/folder24.png"));
	
	private DefaultTreeSelectionState< ModelResourceTreeNode > treeSelectionState;
	private StringState filterState;

	public ClassBasedGalleryBrowser( ModelResourceTreeNode root ) {
		//super(GAP * 2, 0);

		edu.cmu.cs.dennisc.javax.swing.models.TreeModel<ModelResourceTreeNode> treeModel = new ModelResourceTreeNodeModel( root );
		
		org.lgna.croquet.ItemCodec< ModelResourceTreeNode > itemCodec = new org.lgna.croquet.ItemCodec< ModelResourceTreeNode >()
		{
			public Class getValueClass() {
				return ModelResourceTreeNode.class;
			}
			public ModelResourceTreeNode decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
				throw new RuntimeException( "todo" );
			}
			public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, ModelResourceTreeNode value ) {
				throw new RuntimeException( "todo" );
			}
			public StringBuilder appendRepresentation( StringBuilder rv, ModelResourceTreeNode value, java.util.Locale locale ) {
				throw new RuntimeException( "todo" );
			}
		};
		this.treeSelectionState = new DefaultTreeSelectionState<ModelResourceTreeNode>( 
				org.alice.ide.IDE.UI_STATE_GROUP, 
				java.util.UUID.fromString( "d374d9cd-fd27-46da-ba27-8ccdab4f2b67" ), 
				itemCodec, 
				treeModel.getRoot(), 
				treeModel 
		);
		
		this.filterState = new StringState( org.alice.ide.IDE.UI_STATE_GROUP, java.util.UUID.fromString( "62d9d56d-6145-4c58-a20c-4b5d9797ef39" ), "" ) {};
		
		this.treeSelectionState.addValueObserver( new DefaultTreeSelectionState.ValueObserver< ModelResourceTreeNode >() {
			public void changing(org.lgna.croquet.State<ModelResourceTreeNode> state, ModelResourceTreeNode prevValue, ModelResourceTreeNode nextValue, boolean isAdjusting) {
				
			}
			public void changed(org.lgna.croquet.State<ModelResourceTreeNode> state, ModelResourceTreeNode prevValue, ModelResourceTreeNode nextValue, boolean isAdjusting) {
				filterState.setValue( "" );
			}
		} );

		final DirectoryView directoryView = new DirectoryView();
		
		final TextField filterTextField = this.filterState.createTextField();
		filterTextField.setMinimumPreferredWidth( 320 );
		filterTextField.setMaximumSizeClampedToPreferredSize( true );
		filterTextField.getAwtComponent().setTextForBlankCondition( "search entire gallery" );
		filterTextField.scaleFont( 1.5f );
		org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance().addAndInvokeValueObserver( new BooleanState.ValueObserver<Boolean>() {
			public void changing(State<Boolean> state, Boolean prevValue,
					Boolean nextValue, boolean isAdjusting) {
				// TODO Auto-generated method stub
				
			}
			public void changed(State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						filterTextField.requestFocus();
					}
				} );
			}
		} );

		GridPanel fromFilePane = GridPanel.createGridPane( 2, 1, 0, 4 );
		fromFilePane.addComponent( CreateMyInstanceOperation.getInstance().createButton());

		GridPanel bonusPane = GridPanel.createGridPane( 2, 1, 0, 4 );
		//bonusPane.addComponent(CreateBillboardOperation.getInstance().createButton());
		bonusPane.addComponent( org.alice.stageide.croquet.models.declaration.BillboardFieldDeclarationOperation.getInstance().createButton() );
		
		bonusPane.addComponent(Create3dTextOperation.getInstance().createButton());

		BorderPanel buttonPane = new BorderPanel();
		buttonPane.addComponent(fromFilePane, BorderPanel.Constraint.PAGE_START);
		buttonPane.addComponent(bonusPane, BorderPanel.Constraint.PAGE_END);

		
		this.setBackgroundColor(new java.awt.Color(220, 220, 255));

		org.alice.stageide.croquet.models.gallerybrowser.CreateTypeFromPersonResourceOperation createTypeFromPersonResourceOperation = org.alice.stageide.croquet.models.gallerybrowser.CreateTypeFromPersonResourceOperation.getInstance();
		Button createPersonButton = createTypeFromPersonResourceOperation.createButton();
		createPersonButton.setHorizontalTextPosition( HorizontalTextPosition.CENTER );
		createPersonButton.setVerticalTextPosition( VerticalTextPosition.BOTTOM );

		createTypeFromPersonResourceOperation.setSmallIcon(new javax.swing.ImageIcon(ClassBasedGalleryBrowser.class.getResource("images/create_person.png")));

		LineAxisPanel pathControlPanel = new LineAxisPanel();
		pathControlPanel.addComponent( new org.alice.stageide.croquet.components.gallerybrowser.ClassBasedPathControl( this.treeSelectionState, this.createInitializer() ) );
		pathControlPanel.addComponent( BoxUtilities.createHorizontalGlue() );
		pathControlPanel.addComponent( filterTextField );
		
		BorderPanel borderPanel = new BorderPanel( 0, GAP );
		borderPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
		borderPanel.setBackgroundColor( null );
		borderPanel.addComponent( pathControlPanel, Constraint.PAGE_START );

		BorderPanel clampSizePanel = new BorderPanel();
		clampSizePanel.addComponent( directoryView, Constraint.LINE_START );
		ScrollPane scrollPane = new org.lgna.croquet.components.ScrollPane( clampSizePanel );
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
		if (treeNode instanceof ModelResourceTreeNode) {
			ModelResourceTreeNode node = (ModelResourceTreeNode) treeNode;
			name = node.getName();
		} else {
			name = treeNode.toString();
		}
		if( "directoryThumbnail".equals( name ) ) {
			return null;
		} else {
			return getAdornedTextFor(name, treeNode.isLeaf() == false, isRequestedByPath);
		}
	}

	private org.alice.stageide.croquet.components.gallerybrowser.ClassBasedPathControl.Initializer createInitializer() {
		return new org.alice.stageide.croquet.components.gallerybrowser.ClassBasedPathControl.Initializer() {
			public ActionOperation configure(ActionOperation rv, ModelResourceTreeNode treeNode) {
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
			public Operation<?> getOperationForLeaf(ModelResourceTreeNode treeNode) {
				String name = ClassBasedGalleryBrowser.getTextFor(treeNode, true);
				if( name != null ) {
					Operation<?> rv = GalleryClassOperation.getInstance( treeNode );
					rv.setName( name );
					if (treeNode instanceof ModelResourceTreeNode)
					{
						ModelResourceTreeNode modelNode = (ModelResourceTreeNode)treeNode;
						BufferedImage thumbnail = ModelResourceUtilities.getThumbnail(modelNode.getResourceClass());
						ImageIcon smallIcon = new ImageIcon(thumbnail);
						rv.setSmallIcon( smallIcon );
					}
					return rv;
				} else {
					return null;
				}
			}
		};
	}
}
