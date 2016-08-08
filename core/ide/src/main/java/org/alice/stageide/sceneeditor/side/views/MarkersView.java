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
package org.alice.stageide.sceneeditor.side.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class MarkersView extends org.lgna.croquet.views.BorderPanel {

	public static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 173, 167, 208 );
	public static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	public static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( BACKGROUND_COLOR, 1.0, 0.9, 0.8 );

	private static class MarkerView extends org.lgna.croquet.views.BooleanStateButton<javax.swing.AbstractButton> {
		public MarkerView( org.lgna.croquet.BooleanState model ) {
			super( model );
		}

		@Override
		protected javax.swing.AbstractButton createAwtComponent() {
			javax.swing.JToggleButton rv = new javax.swing.JToggleButton() {
				@Override
				public java.awt.Color getBackground() {
					if( this.isSelected() ) {
						return SELECTED_COLOR;
					} else {
						return UNSELECTED_COLOR;
					}
				}
			};

			//rv.setLayout( new java.awt.BorderLayout() );
			//rv.add( new javax.swing.JLabel( "hello" ), java.awt.BorderLayout.LINE_END );
			return rv;
		}
	}

	private static class MarkerListView extends org.lgna.croquet.views.CustomRadioButtons<org.lgna.project.ast.UserField> {
		private class MarkerPopupButton extends org.lgna.croquet.views.PopupButton {
			private final org.lgna.project.ast.UserField field;

			public MarkerPopupButton( org.lgna.project.ast.UserField field ) {
				super( org.alice.ide.declarationseditor.type.FieldMenuModel.getInstance( field ).getPopupPrepModel() );
				this.field = field;
			}

			private boolean isFieldSelected() {
				return MarkerListView.this.getModel().getValue() == field;
			}

			@Override
			protected javax.swing.AbstractButton createSwingButton() {
				return new JPopupButton() {
					@Override
					public void paint( java.awt.Graphics g ) {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						boolean isAlphaDesired = isFieldSelected();
						java.awt.Composite prevComposite = g2.getComposite();
						if( isAlphaDesired ) {
							//pass
						} else {
							g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 0.2f ) );
						}
						try {
							super.paint( g );
						} finally {
							if( isAlphaDesired ) {
								//pass
							} else {
								g2.setComposite( prevComposite );
							}
						}

					}

					//					@Override
					//					public boolean contains( int x, int y ) {
					//						if( isFieldSelected() ) {
					//							return super.contains( x, y );
					//						} else {
					//							return false;
					//						}
					//					}
				};
			}
		}

		private final java.util.Map<org.lgna.project.ast.UserField, MarkerPopupButton> mapFieldToPopupButton = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

		private final org.lgna.croquet.event.ValueListener<org.lgna.project.ast.UserField> selectionListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.UserField>() {
			@Override
			public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.UserField> e ) {
				repaint();
			}
		};

		public MarkerListView( org.lgna.croquet.RefreshableDataSingleSelectListState<org.lgna.project.ast.UserField> model ) {
			super( model );
		}

		@Override
		protected void handleAddedTo( org.lgna.croquet.views.AwtComponentView<?> parent ) {
			super.handleAddedTo( parent );
			this.getModel().addNewSchoolValueListener( this.selectionListener );
		}

		@Override
		protected void handleRemovedFrom( org.lgna.croquet.views.AwtComponentView<?> parent ) {
			this.getModel().removeNewSchoolValueListener( this.selectionListener );
			super.handleRemovedFrom( parent );
		}

		@Override
		protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
			return new net.miginfocom.swing.MigLayout();
		}

		@Override
		protected void removeAllDetails() {
			this.internalRemoveAllComponents();
		}

		@Override
		protected void addPrologue( int count ) {
		}

		@Override
		protected void addItem( org.lgna.project.ast.UserField item, org.lgna.croquet.views.BooleanStateButton<?> button ) {
			this.internalAddComponent( this.mapFieldToPopupButton.get( item ) );
			this.internalAddComponent( button, "wrap, grow" );
		}

		@Override
		protected void addEpilogue() {
		}

		@Override
		protected org.lgna.croquet.views.BooleanStateButton<?> createButtonForItemSelectedState( final org.lgna.project.ast.UserField item, final org.lgna.croquet.BooleanState itemSelectedState ) {
			itemSelectedState.setIconForBothTrueAndFalse( org.alice.stageide.sceneeditor.viewmanager.MarkerUtilities.getIconForMarkerField( item ) );

			item.name.addPropertyListener( new edu.cmu.cs.dennisc.property.event.PropertyListener() {
				@Override
				public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
				}

				@Override
				public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
					itemSelectedState.setTextForBothTrueAndFalse( item.name.getValue() );
				}
			} );

			MarkerView rv = new MarkerView( itemSelectedState );
			rv.setHorizontalAlignment( org.lgna.croquet.views.HorizontalAlignment.LEADING );
			this.mapFieldToPopupButton.put( item, new MarkerPopupButton( item ) );
			return rv;
		}
	}

	private final java.awt.event.MouseListener mouseListener = new java.awt.event.MouseListener() {
		@Override
		public void mouseEntered( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseExited( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mousePressed( java.awt.event.MouseEvent e ) {
			unselectMarker();
		}

		@Override
		public void mouseReleased( java.awt.event.MouseEvent e ) {
		}

		@Override
		public void mouseClicked( java.awt.event.MouseEvent e ) {
		}
	};

	public MarkersView( org.alice.stageide.sceneeditor.side.MarkersToolPalette<?> composite ) {
		super( composite );
		this.addPageStartComponent( new org.lgna.croquet.views.FlowPanel( org.lgna.croquet.views.FlowPanel.Alignment.LEADING,
				composite.getMoveToMarkerOperation().createButton(),
				composite.getMoveMarkerToOperation().createButton() ) );
		this.addCenterComponent( new MarkerListView( composite.getMarkerListState() ) );
		this.addPageEndComponent( new org.lgna.croquet.views.FlowPanel( org.lgna.croquet.views.FlowPanel.Alignment.LEADING,
				composite.getAddOperation().createButton() ) );
		this.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getPrimaryBackgroundColor() );
	}

	private void unselectMarker() {
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "unselectMarker" );
		org.alice.stageide.sceneeditor.side.MarkersToolPalette<?> composite = (org.alice.stageide.sceneeditor.side.MarkersToolPalette<?>)this.getComposite();
		composite.getMarkerListState().clearSelection();
	}

	@Override
	protected void handleAddedTo( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		super.handleAddedTo( parent );
		this.addMouseListener( this.mouseListener );
	}

	@Override
	protected void handleRemovedFrom( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		this.removeMouseListener( this.mouseListener );
		super.handleRemovedFrom( parent );
	}
}
