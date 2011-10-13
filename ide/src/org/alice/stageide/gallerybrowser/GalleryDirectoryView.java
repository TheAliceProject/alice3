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
public class GalleryDirectoryView extends org.lgna.croquet.components.DirectoryView< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > {
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
				return lcName.startsWith( lcFilter ) == false && lcName.contains( lcFilter );
			}
		};
		public abstract boolean accept( String lcName, String lcFilter );
	}

	
	private final org.lgna.croquet.TreeSelectionState.ValueObserver< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > selectionObserver = new org.lgna.croquet.TreeSelectionState.ValueObserver< org.alice.ide.croquet.models.gallerybrowser.GalleryNode >() {
		public void changing( org.lgna.croquet.State< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > state, org.alice.ide.croquet.models.gallerybrowser.GalleryNode prevValue, org.alice.ide.croquet.models.gallerybrowser.GalleryNode nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > state, org.alice.ide.croquet.models.gallerybrowser.GalleryNode prevValue, org.alice.ide.croquet.models.gallerybrowser.GalleryNode nextValue, boolean isAdjusting ) {
			GalleryDirectoryView.this.handleSelectionChanged( nextValue );
		}
	};
	private final org.lgna.croquet.StringState.ValueObserver< String > filterObserver = new org.lgna.croquet.StringState.ValueObserver< String >() {
		public void changing( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< String > state, String prevValue, String nextValue, boolean isAdjusting ) {
			GalleryDirectoryView.this.handleFilterChanged( nextValue );
		}
	};

	public GalleryDirectoryView() {
		super( org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance() );
		//todo
		this.setBackgroundColor( GalleryBrowser.BACKGROUND_COLOR );
	}
	@Override
	protected java.util.List< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > getChildren() {
		String filter = FilterStringState.getInstance().getValue();
		if( filter != null && filter.length() > 0 ) {
			org.alice.ide.croquet.models.gallerybrowser.GalleryNode root = org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance().getTreeModel().getRoot();
			java.util.LinkedList< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > rv = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			String lcFilter = filter.toLowerCase();
			this.update( rv, root, lcFilter, Criterion.STARTS_WITH );
			if( lcFilter.length() > 1 ) {
				this.update( rv, root, lcFilter, Criterion.CONTAINS_BUT_DOES_NOT_START_WITH );
			}
			return rv;
		} else {
			return super.getChildren();
		}
		
	}
	
	private java.util.LinkedList< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > update( java.util.LinkedList< org.alice.ide.croquet.models.gallerybrowser.GalleryNode > rv, org.alice.ide.croquet.models.gallerybrowser.GalleryNode treeNode, String lcFilter, Criterion criterion ) {
		String lcName = treeNode.getText().toLowerCase();
		if( criterion.accept( lcName, lcFilter ) ) {
			rv.add( treeNode );
		}
		for( org.alice.ide.croquet.models.gallerybrowser.GalleryNode child : treeNode ) {
			update( rv, child, lcFilter, criterion );
		}
		return rv;
	}
	
	@Override
	protected org.lgna.croquet.components.JComponent< ? > getComponentFor( org.alice.ide.croquet.models.gallerybrowser.GalleryNode value ) {
		org.lgna.croquet.components.JComponent< ? > component = new org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent( value );
		component.setAlignmentY( java.awt.Component.TOP_ALIGNMENT );
		return component;
	}
	
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance().addValueObserver( this.selectionObserver );
		FilterStringState.getInstance().addAndInvokeValueObserver( this.filterObserver );
	}
	@Override
	protected void handleUndisplayable() {
		FilterStringState.getInstance().removeValueObserver( this.filterObserver );
		org.alice.ide.croquet.models.gallerybrowser.GalleryResourceTreeSelectionState.getInstance().removeValueObserver( this.selectionObserver );
		super.handleUndisplayable();
	}
	private void handleFilterChanged( String filter ) {
		this.refresh();
	}
	private void handleSelectionChanged( org.alice.ide.croquet.models.gallerybrowser.GalleryNode nextValue ) {
		//todo: does not handle case where user clicks on button hooked up to currently selected path node
		FilterStringState.getInstance().setValueTransactionlessly( "" );
		this.refresh();
	}
}
