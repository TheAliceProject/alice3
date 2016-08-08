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
package org.lgna.debug.tree.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public class DebugFrameView<T> extends org.lgna.croquet.views.BorderPanel {
	private static org.lgna.croquet.views.Panel createPanel( org.lgna.croquet.Operation operation, org.lgna.croquet.BooleanState isPruneDesiredState, javax.swing.JTree jTree ) {
		org.lgna.croquet.views.BorderPanel rv = new org.lgna.croquet.views.BorderPanel();
		if( isPruneDesiredState != null ) {
			rv.addPageStartComponent( new org.lgna.croquet.views.FlowPanel( operation.createButton(), isPruneDesiredState.createCheckBox() ) );
		} else {
			rv.addPageStartComponent( operation.createButton() );
		}
		jTree.setCellRenderer( new org.lgna.debug.tree.croquet.views.renderers.ZTreeNodeRenderer() );
		rv.getAwtComponent().add( new edu.cmu.cs.dennisc.javax.swing.components.JScrollPane( jTree ), java.awt.BorderLayout.CENTER );
		return rv;
	}

	public DebugFrameView( org.lgna.debug.tree.croquet.DebugFrame<T> composite ) {
		super( composite );

		this.jMarkTree = new javax.swing.JTree( composite.getMarkTreeModel() );
		this.jCurrentTree = new javax.swing.JTree( composite.getCurrentTreeModel() );

		org.lgna.croquet.views.Panel markPanel = createPanel( composite.getMarkOperation(), null, this.jMarkTree );
		org.lgna.croquet.views.Panel currentPanel = createPanel( composite.getRefreshOperation(), composite.getIsPruningDesiredState(), this.jCurrentTree );

		javax.swing.JSplitPane jSplitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT, markPanel.getAwtComponent(), currentPanel.getAwtComponent() );
		jSplitPane.setDividerLocation( 0.5 );
		this.getAwtComponent().add( jSplitPane, java.awt.BorderLayout.CENTER );
	}

	public void expandAllRowsAndUpdateCurrentTreeRenderer( java.util.Set<T> set ) {
		org.lgna.debug.tree.croquet.views.renderers.ZTreeNodeRenderer<T> currentSgTreeNodeRenderer = (org.lgna.debug.tree.croquet.views.renderers.ZTreeNodeRenderer<T>)this.jCurrentTree.getCellRenderer();
		currentSgTreeNodeRenderer.setValuesToMute( set );

		for( javax.swing.JTree jTree : new javax.swing.JTree[] { this.jMarkTree, this.jCurrentTree } ) {
			for( int i = 0; i < jTree.getRowCount(); i++ ) {
				jTree.expandRow( i );
			}
		}
		this.repaint();
	}

	private final javax.swing.JTree jMarkTree;
	private final javax.swing.JTree jCurrentTree;
}
