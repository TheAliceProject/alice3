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
public class MyTypesView extends org.lgna.croquet.components.GridBagPanel {
	//	private static final javax.swing.Icon REFRESH_ICON = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( MyTypesView.class.getResource( "images/refresh.png" ) );
	//	private class RefreshAction extends javax.swing.AbstractAction {
	//		public RefreshAction() {
	//			this.putValue( NAME, "refresh" );
	//			this.putValue( SMALL_ICON, REFRESH_ICON );
	//		}
	//		public void actionPerformed( java.awt.event.ActionEvent e ) {
	//			MyTypesView.this.refreshLater();
	//		}
	//	}
	//	private final org.lgna.croquet.components.SwingAdapter refreshAdapter;
	public MyTypesView() {
		this.refreshLater();
		//		javax.swing.JButton jButton = new javax.swing.JButton( new RefreshAction() );
		//		jButton.setHorizontalTextPosition( javax.swing.SwingConstants.CENTER );
		//		jButton.setVerticalTextPosition( javax.swing.SwingConstants.BOTTOM );
		//		this.refreshAdapter = new org.lgna.croquet.components.SwingAdapter( jButton );
	}

	private final org.alice.ide.ast.AstEventManager.TypeHierarchyListener typeHierarchyListener = new org.alice.ide.ast.AstEventManager.TypeHierarchyListener() {
		public void typeHierarchyHasPotentiallyChanged() {
			MyTypesView.this.refreshLater();
		}
	};

	@Override
	protected void handleAddedTo( org.lgna.croquet.components.Component<?> parent ) {
		org.alice.ide.ast.AstEventManager.addAndInvokeTypeHierarchyListener( this.typeHierarchyListener );
	}

	@Override
	protected void handleRemovedFrom( org.lgna.croquet.components.Component<?> parent ) {
		org.alice.ide.ast.AstEventManager.removeTypeHierarchyListener( this.typeHierarchyListener );
	}

	private void addComponents( edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> node, int depth, java.awt.GridBagConstraints gbc ) {
		if( node != null ) {
			org.lgna.project.ast.NamedUserType type = node.getValue();
			if( ( type == null ) || type.isAssignableTo( org.lgna.story.STurnable.class ) ) {
				if( type != null ) {
					org.lgna.croquet.components.JComponent<?> component = new org.alice.ide.croquet.components.gallerybrowser.GalleryDragComponent( TypeDragModel.getInstance( type ) );
					gbc.insets.top = 8 + ( depth * 12 );
					this.addComponent( component, gbc );
				}
				for( edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> child : node.getChildren() ) {
					addComponents( child, depth + 1, gbc );
				}
			}
		}
	}

	@Override
	protected void internalRefresh() {
		this.forgetAndRemoveAllComponents();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		edu.cmu.cs.dennisc.tree.Node<org.lgna.project.ast.NamedUserType> root = ide.getApiConfigurationManager().getNamedUserTypesAsTreeFilteredForGallery();

		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.insets.top = 4;
		gbc.insets.left = 4;
		gbc.insets.right = 24;
		//		this.addComponent( refreshAdapter, gbc );
		gbc.insets.right = 2;
		this.addComponents( root, -1, gbc );
		gbc.weightx = 1.0;
		this.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalGlue(), gbc );
	}
}
