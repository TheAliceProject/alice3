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

import edu.cmu.cs.dennisc.java.awt.ComponentUtilities;
import edu.cmu.cs.dennisc.java.awt.RectangleUtilities;
import org.lgna.croquet.SingleSelectListState;
import org.lgna.croquet.SingleSelectListStateComboBoxPrepModel;
import org.lgna.croquet.history.TransactionManager;
import org.lgna.croquet.triggers.PopupMenuEventTrigger;

import javax.accessibility.Accessible;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.ItemListener;

/**
 * @author Dennis Cosgrove
 */
public class ComboBox<E> extends ViewController<JComboBox, SingleSelectListStateComboBoxPrepModel<E, ?>> {
	public ComboBox( SingleSelectListStateComboBoxPrepModel<E, ?> model ) {
		super( model );
		this.setSwingComboBoxModel( model.getListSelectionState().getImp().getSwingModel().getComboBoxModel() );
	}

	private final ListSelectionListener listSelectionListener = new ListSelectionListener() {
		@Override
		public void valueChanged( ListSelectionEvent e ) {
			repaint();
			//			if( e.getValueIsAdjusting() ) {
			//				//pass
			//			} else {
			//				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "valueChanged:", e );
			//			}
		}
	};

	//	@Override
	//	public void appendPrepStepsIfNecessary( org.lgna.croquet.history.Transaction transaction ) {
	//		super.appendPrepStepsIfNecessary( transaction );
	//		org.lgna.croquet.history.CompletionStep< ? > completionStep = transaction.getCompletionStep();
	//		org.lgna.croquet.CompletionModel completionModel = completionStep.getModel();
	//		assert completionModel == this.getModel();
	//		org.lgna.croquet.ListSelectionState.InternalPrepModel< E > prepModel = this.getModel().getPrepModel();
	//		if( transaction.getPrepStepCount() == 1 ) {
	//			org.lgna.croquet.history.PrepStep< ? > prepStep = transaction.getPrepStepAt( 0 );
	//			if( prepStep.getModel() == prepModel ) {
	//				return;
	//			}
	//		}
	//		transaction.removeAllPrepSteps();
	//		org.lgna.croquet.history.ListSelectionStatePrepStep.createAndAddToTransaction( transaction, prepModel, new org.lgna.croquet.triggers.SimulatedTrigger() );		
	//	}

	@Override
	protected JComboBox createAwtComponent() {
		JComboBox rv = new JComboBox() {
			@Override
			public Dimension getPreferredSize() {
				return constrainPreferredSizeIfNecessary( super.getPreferredSize() );
			}

			@Override
			public Dimension getMaximumSize() {
				Dimension rv = super.getMaximumSize();
				if( ComboBox.this.isMaximumSizeClampedToPreferredSize() ) {
					rv.setSize( this.getPreferredSize() );
				} else {
					rv.height = this.getPreferredSize().height;
				}
				return rv;
			}
		};
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "comboBoxUI:", rv.getUI() );
		return rv;
	}

	private PopupMenuListener popupMenuListener = new PopupMenuListener() {
		@Override
		public void popupMenuWillBecomeVisible( PopupMenuEvent e ) {
			TransactionManager.addListSelectionPrepStep( ComboBox.this.getModel(), PopupMenuEventTrigger.createUserInstance( ComboBox.this, e ) );
		}

		@Override
		public void popupMenuWillBecomeInvisible( PopupMenuEvent e ) {
		}

		@Override
		public void popupMenuCanceled( PopupMenuEvent e ) {
			TransactionManager.addCancelCompletionStep( ComboBox.this.getModel().getListSelectionState(), PopupMenuEventTrigger.createUserInstance( ComboBox.this, e ) );
		}
	};

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getAwtComponent().addPopupMenuListener( this.popupMenuListener );
		this.getModel().getListSelectionState().getImp().getSwingModel().getListSelectionModel().addListSelectionListener( this.listSelectionListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.getModel().getListSelectionState().getImp().getSwingModel().getListSelectionModel().removeListSelectionListener( this.listSelectionListener );
		this.getAwtComponent().removePopupMenuListener( this.popupMenuListener );
		super.handleUndisplayable();
	}

	public ListCellRenderer getRenderer() {
		return this.getAwtComponent().getRenderer();
	}

	public void setRenderer( ListCellRenderer listCellRenderer ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setRenderer( listCellRenderer );
	}

	public int getMaximumRowCount() {
		return this.getAwtComponent().getMaximumRowCount();
	}

	public void setMaximumRowCount( int maximumRowCount ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setMaximumRowCount( maximumRowCount );
	}

	private class ItemInPopupTrackableShape implements TrackableShape {
		private final E item;

		public ItemInPopupTrackableShape( E item ) {
			this.item = item;
		}

		private Component getView() {
			JComboBox jComboBox = ComboBox.this.getAwtComponent();
			if( jComboBox.isPopupVisible() ) {
				Accessible accessible = jComboBox.getUI().getAccessibleChild( jComboBox, 0 );
				if( accessible instanceof JPopupMenu ) {
					JPopupMenu jPopupMenu = (JPopupMenu)accessible;
					Component component = jPopupMenu.getComponent( 0 );
					if( component instanceof JScrollPane ) {
						JScrollPane scrollPane = (JScrollPane)component;
						JViewport viewport = scrollPane.getViewport();
						return viewport.getView();
					}
				}
			}
			return null;
		}

		@Override
		public ScrollPane getScrollPaneAncestor() {
			//todo
			return null;
		}

		@Override
		public Shape getShape( ScreenElement asSeenBy, Insets insets ) {
			Component view = this.getView();
			if( view != null ) {
				Rectangle rv = ComponentUtilities.convertRectangle( view.getParent(), view.getBounds(), asSeenBy.getAwtComponent() );
				SingleSelectListState<E, ?> listSelectionState = ComboBox.this.getModel().getListSelectionState();
				final int N = listSelectionState.getItemCount();
				int index = listSelectionState.indexOf( item );
				if( index != -1 ) {
					int offsetY = 0;
					int height = rv.height;
					//									if( view instanceof javax.swing.JComponent ) {
					//										javax.swing.JComponent jView = (javax.swing.JComponent)view;
					//										javax.swing.border.Border border = scrollPane.getBorder();
					//										java.awt.Insets viewInsets = border.getBorderInsets( scrollPane );
					//										java.awt.Insets viewInsets = scrollPane.getInsets();
					//									
					//										if( viewInsets != null ) {
					//											offsetY = viewInsets.top;
					//											height = rv.height - viewInsets.top - viewInsets.bottom;
					//										}
					//									}
					//									javax.swing.ListCellRenderer listCellRenderer = ComboBox.this.getAwtComponent().getRenderer();
					//									if( listCellRenderer instanceof javax.swing.JComponent ) {
					//										edu.cmu.cs.dennisc.print.PrintUtilities.println( ((javax.swing.JComponent)listCellRenderer).getInsets() );
					//									}
					double heightPerCell = height / (double)N;
					rv.y += offsetY;
					rv.y += (int)( heightPerCell * index );
					rv.height = (int)heightPerCell;

					//todo
					rv.y += 3;
					rv.height -= 6;

				}
				RectangleUtilities.inset( rv, insets );
				return rv;
			}
			return null;
		}

		@Override
		public Shape getVisibleShape( ScreenElement asSeenBy, Insets insets ) {
			return getShape( asSeenBy, insets );
		}

		@Override
		public boolean isInView() {
			Component view = this.getView();
			if( view != null ) {
				return view.isShowing();
			} else {
				return false;
			}
		}

		@Override
		public void addComponentListener( ComponentListener listener ) {
		}

		@Override
		public void removeComponentListener( ComponentListener listener ) {
		}

		@Override
		public void addHierarchyBoundsListener( HierarchyBoundsListener listener ) {
		}

		@Override
		public void removeHierarchyBoundsListener( HierarchyBoundsListener listener ) {
		}
	}

	public TrackableShape getTrackableShapeFor( E item ) {
		return new ItemInPopupTrackableShape( item );
	}

	/* package-private */void setSwingComboBoxModel( ComboBoxModel model ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setModel( model );
	}

	/* package-private */void addItemListener( ItemListener itemListener ) {
		this.getAwtComponent().addItemListener( itemListener );
	}

	/* package-private */void removeItemListener( ItemListener itemListener ) {
		this.getAwtComponent().removeItemListener( itemListener );
	}
}
