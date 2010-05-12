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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class TabbedPane extends Component<javax.swing.JComponent> {
	private BorderPanel panel;

	private static class HeaderPane extends Component<javax.swing.JPanel> {
		private static final int NORTH_AREA_PAD = 8;
		private static final int EAST_TAB_PAD = 48;
		// private static java.awt.Stroke SELECTED_STROKE = new
		// java.awt.BasicStroke( 3.0f );
		private static java.awt.Stroke NORMAL_STROKE = new java.awt.BasicStroke(1.0f);
		private static java.awt.Color BORDER_COLOR = java.awt.Color.WHITE;

		@Override
		protected javax.swing.JPanel createJComponent() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				private java.awt.geom.GeneralPath addToPath(java.awt.geom.GeneralPath rv, float x, float y, float width, float height, float a, boolean isContinuation) {
					float x0 = x + width - EAST_TAB_PAD / 2;
					float x1 = x + width + EAST_TAB_PAD;
					// x0 += EAST_TAB_PAD;
					float cx0 = x0 + EAST_TAB_PAD * 0.75f;
					float cx1 = x0;

					float y0 = y + NORTH_AREA_PAD;
					float y1 = y + height;// + this.contentBorderInsets.top;
					float cy0 = y0;
					float cy1 = y1;

					float xA = x + a;
					float yA = y + a;

					if (isContinuation) {
						rv.lineTo(x1, y1);
					} else {
						rv.moveTo(x1, y1);
					}
					rv.curveTo(cx1, cy1, cx0, cy0, x0, y0);
					rv.lineTo(xA, y0);
					rv.quadTo(x, y0, x, yA);
					rv.lineTo(x, y1);

					return rv;
				}

				private void paintTabBorder(java.awt.Graphics g, javax.swing.AbstractButton button) {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
					java.awt.Color prev = g.getColor();
					int x = button.getX();
					int y = button.getY();
					int width = button.getWidth();
					int height = button.getHeight();
					try {
						// if( isSelected ) {
						// g.setColor( this.lightHighlight );
						// g2.setStroke( this.selectedStroke );
						// g2.setClip( x, y, width + EAST_TAB_PAD, height +
						// this.contentBorderInsets.top );
						// } else {
						g.setColor(BORDER_COLOR);
						g2.setStroke(NORMAL_STROKE);
						// }
						java.awt.geom.GeneralPath path = this.addToPath(new java.awt.geom.GeneralPath(), x, y, width, height, height * 0.4f, false);
						g2.draw(path);
					} finally {
						g.setColor(prev);
					}
				}

				private void paintTabBackground(java.awt.Graphics g, javax.swing.AbstractButton button) {
					java.awt.Color prev = g.getColor();
					try {
						int x = button.getX();
						int y = button.getY();
						int width = button.getWidth();
						int height = button.getHeight();
						boolean isSelected = button.isSelected();
						boolean isRollover = button.getModel().isArmed();
						java.awt.Color color = button.getBackground();
						if (isSelected) {
							// pass
						} else {
							color = color.darker();
							if (isRollover) {
								// pass
							} else {
								color = color.darker();
							}
						}
						g.setColor(color);
						java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
						java.awt.geom.GeneralPath path = addToPath(new java.awt.geom.GeneralPath(), x, y, width, height, height * 0.4f, false);
						g2.fill(path);
					} finally {
						g.setColor(prev);
					}
				}

				@Override
				protected void paintChildren(java.awt.Graphics g) {
					for (java.awt.Component component : this.getComponents()) {
						if (component instanceof javax.swing.AbstractButton) {
							javax.swing.AbstractButton button = (javax.swing.AbstractButton) component;
							if (button.isSelected()) {
								// pass
							} else {
								this.paintTabBackground(g, button);
							}
						}
					}
					for (java.awt.Component component : this.getComponents()) {
						if (component instanceof javax.swing.AbstractButton) {
							javax.swing.AbstractButton button = (javax.swing.AbstractButton) component;
							if (button.isSelected()) {
								this.paintTabBackground(g, button);
								this.paintTabBorder(g, button);
								int y = this.getHeight() - 1;
								g.setColor(BORDER_COLOR);
								g.drawLine(0, y, button.getX(), y);

								int pad = EAST_TAB_PAD / 2;
								g.drawLine(button.getX() + button.getWidth() + pad, y, this.getWidth(), y);
							}
						}
					}
					super.paintChildren(g);
				}
			};
			rv.setLayout(new javax.swing.BoxLayout(rv, javax.swing.BoxLayout.LINE_AXIS));
			rv.setBackground(new java.awt.Color(63, 63, 81));
			return rv;
		}

		public void addComponent(Component<?> component) {
			this.internalAddComponent(component);
		}

		public void removeComponent(Component<?> component) {
			this.internalRemoveComponent(component);
		}
	}

	private CardPanel cardPanel = new CardPanel();
	private HeaderPane headerPane = new HeaderPane();
	private javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
//	private javax.swing.SingleSelectionModel singleSelectionModel = new javax.swing.DefaultSingleSelectionModel();
//	private javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
//		public void stateChanged(javax.swing.event.ChangeEvent e) {
//			edu.cmu.cs.dennisc.print.PrintUtilities.println( "ChangeListener", e );
//		}
//	};

	@Override
	protected javax.swing.JComponent createJComponent() {
		this.headerPane.setBackgroundColor(new java.awt.Color(63, 63, 81));

		this.panel = new BorderPanel();
		this.panel.addComponent(this.headerPane, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.NORTH);
		this.panel.addComponent(this.cardPanel, edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER);
		return panel.getJComponent();
	}

	/* package-private */class Key {
		private AbstractButton<?> headerComponent;
		private TabStateOperation tabStateOperation;
		private edu.cmu.cs.dennisc.croquet.CardPanel.Key mainComponentKey;

		private Key( AbstractButton<?> headerComponent, Component<?> mainComponent, TabStateOperation tabStateOperation ) {
			this.headerComponent = headerComponent;
			this.mainComponentKey = TabbedPane.this.cardPanel.createKey( mainComponent, tabStateOperation.getIndividualUUID().toString());
			this.tabStateOperation = tabStateOperation;
		}
		public TabStateOperation getTabStateOperation() {
			return this.tabStateOperation;
		}
	}
	private final java.util.Map<javax.swing.ButtonModel, Key> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();

	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if( e.getStateChange() == java.awt.event.ItemEvent.SELECTED ) {
				java.awt.ItemSelectable itemSelectable = e.getItemSelectable();
				Key key;
				if( itemSelectable instanceof javax.swing.ButtonModel ) {
					key = map.get( (javax.swing.ButtonModel)itemSelectable );
					assert key != null;
				} else {
					assert false;
					key = null;
				}
				TabbedPane.this.selectTab( key );
			} else {
				//pass
			}
		}
	};

	/* package-private */Key createKey(AbstractButton<?> header, Component<?> mainComponent, TabStateOperation tabStateOperation) {
		Key rv = new Key(header, mainComponent, tabStateOperation);
		map.put( header.getJComponent().getModel(), rv);
		return rv;
	}

	/* package-private */void addTab(Key key) {
		this.headerPane.addComponent(key.headerComponent);
		this.buttonGroup.add(key.headerComponent.getJComponent());
		this.cardPanel.addComponent(key.mainComponentKey);
		key.headerComponent.getJComponent().getModel().addItemListener( this.itemListener );
		this.revalidateAndRepaint();
	}

	/* package-private */void removeTab(Key key) {
		key.headerComponent.getJComponent().getModel().removeItemListener( this.itemListener );
		this.cardPanel.removeComponent(key.mainComponentKey);
		this.buttonGroup.remove(key.headerComponent.getJComponent());
		this.headerPane.removeComponent(key.headerComponent);
		this.revalidateAndRepaint();
	}

	/* package-private */ void selectTab(Key key) {
		if( key != null ) {
			this.cardPanel.show(key.mainComponentKey);
		} else {
			this.cardPanel.show( null );
		}
		
	}
	
	/* package-private */Key getSelectedKey() {
		javax.swing.ButtonModel model = this.buttonGroup.getSelection();
		if( model != null ) {
			return this.map.get( model );
		} else {
			return null;
		}
	}
}
