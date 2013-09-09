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
package org.alice.stageide.gallerybrowser.views;

/**
 * @author Dennis Cosgrove
 */
public class SearchTabView extends GalleryTabView {

	public static final javax.swing.Icon SEARCH_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( SearchTabView.class.getResource( "images/system-search.png" ) );

	private static enum Criterion {
		STARTS_WITH {
			@Override
			public boolean accept( String lcName, String lcFilter ) {
				return lcName.startsWith( lcFilter );
			}
		},
		CONTAINS_BUT_DOES_NOT_START_WITH {
			@Override
			public boolean accept( String lcName, String lcFilter ) {
				return ( lcName.startsWith( lcFilter ) == false ) && lcName.contains( lcFilter );
			}
		};
		public abstract boolean accept( String lcName, String lcFilter );
	}

	private final org.lgna.croquet.StringState.ValueListener<String> filterListener = new org.lgna.croquet.StringState.ValueListener<String>() {
		public void changing( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<String> state, String prevValue, String nextValue, boolean isAdjusting ) {
			SearchTabView.this.handleFilterChanged( nextValue );
		}
	};

	private static void appendIfMatch( java.util.List<org.alice.stageide.modelresource.ResourceNode> rv, org.alice.stageide.modelresource.ResourceNode node, String lcFilter, Criterion criterion, String text ) {
		if( rv.contains( node ) ) {
			//pass
		} else {
			String lcName = text.toLowerCase();
			if( criterion.accept( lcName, lcFilter ) ) {
				rv.add( node );
			}
		}
	}

	private static void appendMatches( java.util.List<org.alice.stageide.modelresource.ResourceNode> matches, org.alice.stageide.modelresource.ResourceNode node, String lcFilter, Criterion criterion, boolean isTag ) {
		if( isTag ) {
			String[] tags = node.getResourceKey().getTags();
			if( ( tags != null ) && ( tags.length > 0 ) ) {
				for( String tag : tags ) {
					appendIfMatch( matches, node, lcFilter, criterion, tag );
				}
			}
		} else {
			String searchText = node.getResourceKey().getSearchText();
			if( ( searchText != null ) && ( searchText.length() > 0 ) ) {
				appendIfMatch( matches, node, lcFilter, criterion, searchText );
			}
		}
		if( node.getResourceKey().isLeaf() ) {
			//pass
		} else {
			for( org.alice.stageide.modelresource.ResourceNode child : node.getNodeChildren() ) {
				appendMatches( matches, child, lcFilter, criterion, isTag );
			}
		}
	}

	private final org.lgna.croquet.components.AbstractLabel noMatchesLabel;
	private final org.lgna.croquet.components.AbstractLabel noEntryLabel;

	private class FilteredResourcesView extends org.lgna.croquet.components.LineAxisPanel {
		@Override
		protected void internalRefresh() {
			super.internalRefresh();
			this.internalRemoveAllComponents();

			org.alice.stageide.gallerybrowser.SearchTab composite = (org.alice.stageide.gallerybrowser.SearchTab)SearchTabView.this.getComposite();
			String filter = composite.getFilterState().getValue();

			if( filter.length() > 0 ) {
				org.alice.stageide.modelresource.ResourceNode root = org.alice.stageide.modelresource.TreeUtilities.getTreeBasedOnClassHierarchy();
				java.util.List<org.alice.stageide.modelresource.ResourceNode> matchingNodes = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				String lcFilter = filter.toLowerCase();
				for( boolean isTag : new boolean[] { false, true } ) {
					appendMatches( matchingNodes, root, lcFilter, Criterion.STARTS_WITH, isTag );
					if( lcFilter.length() > 2 ) {
						appendMatches( matchingNodes, root, lcFilter, Criterion.CONTAINS_BUT_DOES_NOT_START_WITH, isTag );
					}
				}
				if( matchingNodes.size() > 0 ) {
					for( org.alice.stageide.modelresource.ResourceNode matchingNode : matchingNodes ) {
						this.addComponent( SearchTabView.this.getGalleryDragComponent( matchingNode ) );
					}
				} else {
					this.addComponent( noMatchesLabel );
				}
			} else {
				this.addComponent( noEntryLabel );
			}
		}
	}

	private final FilteredResourcesView filteredResourcesView = new FilteredResourcesView();
	private final org.lgna.croquet.components.TextField filterTextField;

	public SearchTabView( org.alice.stageide.gallerybrowser.SearchTab composite ) {
		super( composite );

		this.noMatchesLabel = composite.getNoMatchesLabel().createLabel( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		this.noEntryLabel = composite.getNoEntryLabel().createLabel( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
		this.noMatchesLabel.setForegroundColor( java.awt.Color.DARK_GRAY );
		this.noEntryLabel.setForegroundColor( java.awt.Color.DARK_GRAY );

		this.filterTextField = composite.getFilterState().createTextField();
		this.filterTextField.setMinimumPreferredWidth( 320 );
		this.filterTextField.setMaximumSizeClampedToPreferredSize( true );
		this.filterTextField.scaleFont( 1.2f );
		this.filterTextField.enableSelectAllWhenFocusGained();

		org.lgna.croquet.components.ScrollPane scrollPane = createGalleryScrollPane( this.filteredResourcesView );
		this.filteredResourcesView.setBackgroundColor( GalleryView.BACKGROUND_COLOR );

		this.addPageStartComponent( new org.lgna.croquet.components.LineAxisPanel(
				composite.getFilterState().getSidekickLabel().createLabel(),
				this.filterTextField
				) );
		this.addCenterComponent( scrollPane );
	}

	@Override
	public void handleCompositePreActivation() {
		super.handleCompositePreActivation();
		org.alice.stageide.gallerybrowser.SearchTab composite = (org.alice.stageide.gallerybrowser.SearchTab)this.getComposite();
		composite.getFilterState().addAndInvokeValueListener( this.filterListener );
		this.filterTextField.requestFocusLater();
	}

	@Override
	public void handleCompositePostDeactivation() {
		org.alice.stageide.gallerybrowser.SearchTab composite = (org.alice.stageide.gallerybrowser.SearchTab)this.getComposite();
		composite.getFilterState().removeValueListener( this.filterListener );
		super.handleCompositePostDeactivation();
	}

	private void handleFilterChanged( String filter ) {
		this.filteredResourcesView.refreshLater();
	}
}
