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
package org.alice.stageide.gallerybrowser.search.core;

/**
 * @author Dennis Cosgrove
 */
public class SearchGalleryWorker extends edu.cmu.cs.dennisc.worker.WorkerWithProgress<java.util.List<org.alice.stageide.modelresource.ResourceNode>, org.alice.stageide.modelresource.ResourceNode> {
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

	public SearchGalleryWorker( String filter, org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView searchTabView ) {
		this.filter = filter;
		this.searchTabView = searchTabView;
	}

	private void appendIfMatch( java.util.List<org.alice.stageide.modelresource.ResourceNode> rv, org.alice.stageide.modelresource.ResourceNode node, String lcFilter, Criterion criterion, String text ) {
		if( rv.contains( node ) ) {
			//pass
		} else {
			String lcName = text.toLowerCase();
			if( criterion.accept( lcName, lcFilter ) ) {
				rv.add( node );
				this.publish( node );
			}
		}
	}

	private void appendMatches( java.util.List<org.alice.stageide.modelresource.ResourceNode> matches, org.alice.stageide.modelresource.ResourceNode node, String lcFilter, Criterion criterion, boolean isTag ) {
		if( this.isCancelled() ) {
			//pass
		} else {
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
	}

	@Override
	protected java.util.List<org.alice.stageide.modelresource.ResourceNode> do_onBackgroundThread() throws Exception {
		if( filter.length() > 0 ) {
			org.alice.stageide.modelresource.ResourceNode root = org.alice.stageide.modelresource.TreeUtilities.getTreeBasedOnClassHierarchy();
			java.util.List<org.alice.stageide.modelresource.ResourceNode> matchingNodes = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			String lcFilter = filter.toLowerCase();
			for( boolean isTag : new boolean[] { false, true } ) {
				appendMatches( matchingNodes, root, lcFilter, Criterion.STARTS_WITH, isTag );
				if( lcFilter.length() > 2 ) {
					appendMatches( matchingNodes, root, lcFilter, Criterion.CONTAINS_BUT_DOES_NOT_START_WITH, isTag );
				}
			}
			return matchingNodes;
		} else {
			return java.util.Collections.emptyList();
		}
	}

	@Override
	protected void handleProcess_onEventDispatchThread( java.util.List<org.alice.stageide.modelresource.ResourceNode> chunks ) {
		this.searchTabView.addGalleryDragComponents( chunks );
	}

	@Override
	protected void handleDone_onEventDispatchThread( java.util.List<org.alice.stageide.modelresource.ResourceNode> matchingNodes ) {
		this.searchTabView.setComponentsToGalleryDragComponents( this.filter, matchingNodes );
	}

	private final String filter;
	private final org.alice.stageide.gallerybrowser.search.croquet.views.SearchTabView searchTabView;
}
