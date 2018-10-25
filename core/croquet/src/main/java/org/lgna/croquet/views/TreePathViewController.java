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

package org.lgna.croquet.views;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.models.TreeModel;
import org.lgna.croquet.Operation;
import org.lgna.croquet.SingleSelectTreeState;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;

/**
 * @author Dennis Cosgrove
 */
public class TreePathViewController<T> extends PanelViewController<SingleSelectTreeState<T>> {
	private static class BreadcrumbLayout implements LayoutManager {
		private static final int AMOUNT_TO_SCOOT = 4;
		private static final String SYNTH_UI_CLASS_NAME = "javax.swing.plaf.synth.SynthToggleButtonUI";

		private Component centerComponent;
		private javax.swing.AbstractButton lineEndComponent;

		@Override
		public void addLayoutComponent( String name, Component comp ) {
			if( BorderLayout.CENTER.equals( name ) ) {
				this.centerComponent = comp;
			} else if( BorderLayout.LINE_END.equals( name ) ) {
				this.lineEndComponent = (javax.swing.AbstractButton)comp;
			} else {
				Logger.severe( name, comp );
			}
		}

		@Override
		public void removeLayoutComponent( Component comp ) {
			if( comp == this.centerComponent ) {
				this.centerComponent = null;
			} else if( comp == this.lineEndComponent ) {
				this.lineEndComponent = null;
			} else {
				Logger.severe( comp );
			}
		}

		@Override
		public Dimension minimumLayoutSize( Container parent ) {
			return this.preferredLayoutSize( parent );
		}

		@Override
		public Dimension preferredLayoutSize( Container parent ) {
			Dimension rv = new Dimension( 0, 0 );
			if( this.centerComponent != null ) {
				Dimension size = this.centerComponent.getPreferredSize();
				rv.width += size.width;
				rv.height = Math.max( rv.height, size.height );
			}
			if( this.lineEndComponent != null ) {
				Dimension size = this.lineEndComponent.getPreferredSize();
				rv.width += size.width;
				rv.height = Math.max( rv.height, size.height );
				if( SYNTH_UI_CLASS_NAME.equals( this.lineEndComponent.getUI().getClass().getName() ) ) {
					rv.width -= AMOUNT_TO_SCOOT;
				}
			}
			return rv;
		}

		@Override
		public void layoutContainer( Container parent ) {
			Dimension parentSize = parent.getSize();
			int x = 0;
			if( this.centerComponent != null ) {
				int width = this.centerComponent.getPreferredSize().width;
				this.centerComponent.setBounds( x, 0, width, parentSize.height );
				x += width;
			}
			if( this.lineEndComponent != null ) {
				int width = this.lineEndComponent.getPreferredSize().width;
				this.lineEndComponent.setBounds( x, 0, width, parentSize.height );
				if ( SYNTH_UI_CLASS_NAME.equals( this.lineEndComponent.getUI().getClass().getName() ) ) {
					Point p = this.lineEndComponent.getLocation();
					this.lineEndComponent.setLocation( p.x - AMOUNT_TO_SCOOT, p.y );
				}
			}
		}
	}

	private static Insets MARGIN = new Insets( 2, 2, 2, 0 );

	private static class SelectDirectoryPanel<T> extends Panel {
		private SelectDirectoryPanel( SingleSelectTreeState<T> treeSelectionState, T treeNode, Color breadCrumbColor ) {
			PopupButton selectChildButton = treeSelectionState.getCascadeFor( treeNode ).getRoot().getPopupPrepModel().createPopupButton();
			if( UIManager.getLookAndFeel().getName().contains( "Nimbus" ) ) {
				selectChildButton.setBorder( BorderFactory.createEmptyBorder( 0, 4, 0, 4 ) );
			} else {
				selectChildButton.setBorder( BorderFactory.createLineBorder( Color.GRAY ) );
			}
			Operation operation = treeSelectionState.getItemSelectionOperation( treeNode );
			operation.initializeIfNecessary();
			Button button = operation.createButton();

			//button.tightenUpMargin( MARGIN );
			button.setIconTextGap( 0 );

			this.internalAddComponent( selectChildButton, BorderLayout.LINE_END );
			this.internalAddComponent( button, BorderLayout.CENTER );

			if( breadCrumbColor != null ) {
				button.getAwtComponent().setBackground( breadCrumbColor );
				selectChildButton.getAwtComponent().setBackground( breadCrumbColor );
			}
			this.setMaximumSizeClampedToPreferredSize( true );
		}

		@Override
		protected LayoutManager createLayoutManager( JPanel jPanel ) {
			return new BreadcrumbLayout();
		}
	}

	private static class InternalPanel<T> extends LineAxisPanel {
		private final Color breadCrumbColor;

		public InternalPanel( Color breadCrumbColor ) {
			this.breadCrumbColor = breadCrumbColor;
			this.setBackgroundColor( null );
		}

		@Override
		protected void internalRefresh() {
			this.internalRemoveAllComponents();
			//todo
			TreePathViewController<T> owner = (TreePathViewController<T>)this.getParent();

			TreeModel<T> treeModel = owner.getModel().getTreeModel();
			TreePath treePath = owner.treeSelectionModel.getSelectionPath();
			if( treePath != null ) {
				final int N = treePath.getPathCount();
				for( int i = 0; i < N; i++ ) {
					//todo: remove when look and feel magic is performed   
					if( i > 0 ) {
						this.internalAddComponent( BoxUtilities.createHorizontalSliver( 4 ) );
						this.internalAddComponent( Separator.createInstanceSeparatingLeftFromRight() );
						this.internalAddComponent( BoxUtilities.createHorizontalSliver( 4 ) );
					}
					T treeNode = (T)treePath.getPathComponent( i );
					if( treeModel.isLeaf( treeNode ) ) {
						//pass
					} else {
						SelectDirectoryPanel<T> selectDirectoryPanel = new SelectDirectoryPanel( owner.getModel(), treeNode, this.breadCrumbColor );
						this.internalAddComponent( selectDirectoryPanel );
					}
				}
			}
		}
	}

	private TreeSelectionModel treeSelectionModel;
	private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
		@Override
		public void valueChanged( TreeSelectionEvent e ) {
			TreePathViewController.this.getInternalPanel().refreshLater();
		}
	};

	public TreePathViewController( SingleSelectTreeState<T> model, Color breadCrumbColor ) {
		super( model, new InternalPanel<T>( breadCrumbColor ) );
		this.setBackgroundColor( null );
		this.setSwingTreeSelectionModel( model.getSwingModel().getTreeSelectionModel() );
	}

	private void setSwingTreeSelectionModel( TreeSelectionModel treeSelectionModel ) {
		if( this.treeSelectionModel != null ) {
			this.treeSelectionModel.removeTreeSelectionListener( this.treeSelectionListener );
		}
		this.treeSelectionModel = treeSelectionModel;
		this.getInternalPanel().refreshLater();
		if( this.treeSelectionModel != null ) {
			this.treeSelectionModel.addTreeSelectionListener( this.treeSelectionListener );
		}
	}
}
