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
package org.alice.ide.members.components;

import org.alice.ide.recyclebin.RecycleBin;

/**
 * @author Dennis Cosgrove
 */
public class MembersView extends org.lgna.croquet.views.BorderPanel {
	private static edu.cmu.cs.dennisc.map.MapToMap<Class<?>, org.lgna.project.ast.AbstractType<?, ?, ?>, org.alice.ide.common.TypeComponent> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	public static final byte PROTOTYPE = 0;

	public static org.alice.ide.common.TypeComponent getComponentFor( Class<?> cls, org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		return mapToMap.getInitializingIfAbsent( cls, type, new edu.cmu.cs.dennisc.map.MapToMap.Initializer<Class<?>, org.lgna.project.ast.AbstractType<?, ?, ?>, org.alice.ide.common.TypeComponent>() {
			@Override
			public org.alice.ide.common.TypeComponent initialize( Class<?> cls, org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
				return org.alice.ide.common.TypeComponent.createInstance( type );
			}
		} );
	}

	private static final int SIZE = 32;

	private static enum DragReceptorState {
		IDLE( null, null, null, 0 ),
		STARTED( java.awt.Color.YELLOW, new java.awt.Color( 191, 191, 191, 0 ), null, SIZE ),
		ENTERED( java.awt.Color.YELLOW, new java.awt.Color( 127, 127, 127, 191 ), new org.alice.ide.recyclebin.icons.ClosedTrashCanSymbolicStyleIcon( 128, 128, java.awt.Color.LIGHT_GRAY ), SIZE ),
		ENTERED_FAR_ENOUGH( new java.awt.Color( 0xCCFF99 ), new java.awt.Color( 127, 127, 127, 191 ), new org.alice.ide.recyclebin.icons.OpenTrashCanSymbolicStyleIcon( 128, 128, java.awt.Color.WHITE ), SIZE );
		private final java.awt.Color colorA;
		private final java.awt.Color colorB;
		private final javax.swing.Icon icon;
		private final int gradientAmount;

		private DragReceptorState( java.awt.Color colorA, java.awt.Color colorB, javax.swing.Icon icon, int gradientAmount ) {
			this.colorA = colorA;
			this.colorB = colorB;
			this.icon = icon;
			this.gradientAmount = gradientAmount;
		}

		public java.awt.Color getColorA() {
			return this.colorA;
		}

		public java.awt.Color getColorB() {
			return this.colorB;
		}

		public int getGradientAmount() {
			return this.gradientAmount;
		}

		public javax.swing.Icon getIcon() {
			return this.icon;
		}
	};

	private class RecycleBinDropReceptor extends org.lgna.croquet.AbstractDropReceptor {
		private DragReceptorState dragReceptorState = DragReceptorState.IDLE;

		@Override
		public boolean isPotentiallyAcceptingOf( org.lgna.croquet.DragModel dragModel ) {
			return dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel;
		}

		private void setDragReceptorState( DragReceptorState dragReceptorState ) {
			this.dragReceptorState = dragReceptorState;
			MembersView.this.repaint();
		}

		@Override
		public void dragStarted( org.lgna.croquet.history.DragStep step ) {
			this.setDragReceptorState( DragReceptorState.STARTED );
		}

		@Override
		public void dragEntered( org.lgna.croquet.history.DragStep step ) {
			this.setDragReceptorState( DragReceptorState.ENTERED );
		}

		@Override
		public org.lgna.croquet.DropSite dragUpdated( org.lgna.croquet.history.DragStep step ) {
			java.awt.event.MouseEvent e = step.getLatestMouseEvent();
			java.awt.Point p = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.convertPoint( e.getComponent(), e.getPoint(), MembersView.this.getAwtComponent() );

			final int FAR_ENOUGH = 64;
			if( p.x < ( MembersView.this.getWidth() - FAR_ENOUGH ) ) {
				this.setDragReceptorState( DragReceptorState.ENTERED_FAR_ENOUGH );
				return MembersView.this.dropSite;
			} else {
				this.setDragReceptorState( DragReceptorState.ENTERED );
				return null;
			}
		}

		@Override
		protected org.lgna.croquet.Model dragDroppedPostRejectorCheck( org.lgna.croquet.history.DragStep step ) {
			org.lgna.croquet.DropSite dropSite = step.getCurrentPotentialDropSite();
			if( dropSite != null ) {
				org.lgna.croquet.DragModel dragModel = step.getModel();
				if( dragModel instanceof org.alice.ide.ast.draganddrop.statement.StatementDragModel ) {
					org.alice.ide.ast.draganddrop.statement.StatementDragModel statementDragModel = (org.alice.ide.ast.draganddrop.statement.StatementDragModel)dragModel;
					org.lgna.project.ast.Statement statement = statementDragModel.getStatement();
					return org.alice.ide.ast.delete.DeleteStatementOperation.getInstance( statement );
				} else {
					return null;
				}
			} else {
				return null;
			}
		}

		@Override
		public void dragExited( org.lgna.croquet.history.DragStep step, boolean isDropRecipient ) {
			//			step.getDragSource().showDragProxy();
			this.setDragReceptorState( DragReceptorState.STARTED );
		}

		@Override
		public void dragStopped( org.lgna.croquet.history.DragStep step ) {
			this.setDragReceptorState( DragReceptorState.IDLE );
		}

		@Override
		public org.lgna.croquet.views.TrackableShape getTrackableShape( org.lgna.croquet.DropSite potentialDropSite ) {
			return MembersView.this;
		}

		@Override
		public org.lgna.croquet.views.SwingComponentView<?> getViewController() {
			return MembersView.this;
		}

	}

	//todo
	private static class RecycleBinDropSite implements org.lgna.croquet.DropSite {
		public RecycleBinDropSite() {
		}

		public RecycleBinDropSite( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
			//todo
		}

		@Override
		public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
			//todo
		}

		@Override
		public int hashCode() {
			return 0;
		}

		@Override
		public boolean equals( Object obj ) {
			return obj instanceof RecycleBinDropSite;
		}

		@Override
		public org.lgna.croquet.DropReceptor getOwningDropReceptor() {
			return RecycleBin.SINGLETON.getDropReceptor();
		}
	}

	private final RecycleBinDropSite dropSite = new RecycleBinDropSite();

	private final RecycleBinDropReceptor recycleBinDropReceptor = new RecycleBinDropReceptor();

	public MembersView( org.alice.ide.members.MembersComposite composite ) {
		super( composite );
		org.alice.ide.croquet.components.InstanceFactoryPopupButton instanceFactoryPopupButton = new org.alice.ide.croquet.components.InstanceFactoryPopupButton( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance() );
		//		org.lgna.croquet.components.LineAxisPanel instancePanel = new org.lgna.croquet.components.LineAxisPanel();
		//		instancePanel.addComponent( new org.alice.ide.croquet.components.InstanceFactoryPopupButton( org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance() ) );
		//		instancePanel.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		//		instancePanel.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 0, 4 ) );
		//
		//		this.addPageStartComponent( instancePanel );
		this.setBackgroundColor( org.lgna.croquet.views.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
		this.addPageStartComponent( instanceFactoryPopupButton );
		org.lgna.croquet.views.TabbedPane<?> tabbedPane;
		if( org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() ) {
			tabbedPane = composite.getTabState().createFolderTabbedPane();
		} else {
			tabbedPane = composite.getTabState().createToolPaletteTabbedPane();
		}
		this.addCenterComponent( tabbedPane );
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		javax.swing.JPanel rv = new DefaultJPanel() {
			@Override
			public void paint( java.awt.Graphics g ) {
				super.paint( g );
				if( recycleBinDropReceptor.dragReceptorState == DragReceptorState.IDLE ) {
					//pass
				} else {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					int width = this.getWidth();
					int height = this.getHeight();
					java.awt.Color colorA = recycleBinDropReceptor.dragReceptorState.getColorA();
					java.awt.Color colorB = recycleBinDropReceptor.dragReceptorState.getColorB();
					int gradientAmount = recycleBinDropReceptor.dragReceptorState.getGradientAmount();
					java.awt.Paint paint = new java.awt.GradientPaint( width, 0, colorA, width - gradientAmount, gradientAmount, colorB );
					g2.setPaint( paint );
					g2.fillRect( 0, 0, width, height );
					javax.swing.Icon icon = recycleBinDropReceptor.dragReceptorState.getIcon();
					if( icon != null ) {
						g2.setPaint( new java.awt.Color( 221, 221, 221 ) );
						int x = ( width - icon.getIconWidth() ) / 2;
						int y = ( height - icon.getIconHeight() ) / 2;
						y = Math.min( y, 100 );
						icon.paintIcon( this, g2, x, y );
					}
				}
			}
		};
		return rv;
	}

	public RecycleBinDropReceptor getRecycleBinDropReceptor() {
		return this.recycleBinDropReceptor;
	}
}
