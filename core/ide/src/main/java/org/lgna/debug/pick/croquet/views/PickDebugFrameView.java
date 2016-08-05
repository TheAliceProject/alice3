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
package org.lgna.debug.pick.croquet.views;

import edu.cmu.cs.dennisc.render.PickResult;

/**
 * @author Dennis Cosgrove
 */
public class PickDebugFrameView extends org.lgna.croquet.views.BorderPanel {
	public PickDebugFrameView( org.lgna.debug.pick.croquet.PickDebugFrame composite ) {
		super( composite );
		this.addPageEndComponent( composite.getRefreshOperation().createButton() );
		this.getAwtComponent().add( this.pickView, java.awt.BorderLayout.CENTER );
		javax.swing.JList jList = new javax.swing.JList( this.listModel );
		jList.setCellRenderer( new edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer<edu.cmu.cs.dennisc.scenegraph.Visual>() {
			@Override
			protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, edu.cmu.cs.dennisc.scenegraph.Visual value, int index, boolean isSelected, boolean cellHasFocus ) {
				rv.setText( value != null ? value.getName() : null );
				rv.setIcon( mapSgVisualToColorIcon.get( value ) );
				return rv;
			}
		} );
		this.getAwtComponent().add( new javax.swing.JScrollPane( jList ), java.awt.BorderLayout.LINE_END );
	}

	public void setPickResults( edu.cmu.cs.dennisc.render.PickResult[][] pickResults ) {
		synchronized( pickResultsLock ) {
			this.mapSgVisualToColorIcon.clear();
			this.listModel.clear();
			this.pickResults = pickResults;
			if( this.pickResults != null ) {
				java.awt.Color[] colors = {
						java.awt.Color.RED,
						java.awt.Color.GREEN,
						java.awt.Color.BLUE,
						java.awt.Color.CYAN,
						java.awt.Color.MAGENTA,
						java.awt.Color.YELLOW,
						java.awt.Color.ORANGE,
						java.awt.Color.WHITE,

				};
				int colorIndex = 0;
				for( PickResult[] line : pickResults ) {
					for( PickResult pickResult : line ) {
						edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
						if( sgVisual != null ) {
							if( this.mapSgVisualToColorIcon.containsKey( sgVisual ) ) {
								//pass
							} else {
								this.listModel.addElement( sgVisual );
								this.mapSgVisualToColorIcon.put( sgVisual, new edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon( colors[ colorIndex ] ) );
								colorIndex += 1;
								colorIndex %= colors.length;
							}
						}
					}
				}
			}
		}
		this.repaint();
	}

	private Object pickResultsLock = "pickResultsLock";
	private edu.cmu.cs.dennisc.render.PickResult[][] pickResults;
	private final java.util.Map<edu.cmu.cs.dennisc.scenegraph.Visual, edu.cmu.cs.dennisc.javax.swing.icons.ColorIcon> mapSgVisualToColorIcon = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private final javax.swing.DefaultListModel listModel = new javax.swing.DefaultListModel();

	private final javax.swing.JComponent pickView = new javax.swing.JComponent() {
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			synchronized( pickResultsLock ) {
				if( pickResults != null ) {
					final int PIXELS_PER_PICK = 10;
					for( int y = 0; y < pickResults.length; y++ ) {
						int yPixel = y * PIXELS_PER_PICK;
						for( int x = 0; x < pickResults[ y ].length; x++ ) {
							int xPixel = x * PIXELS_PER_PICK;
							edu.cmu.cs.dennisc.render.PickResult pickResult = pickResults[ y ][ x ];
							edu.cmu.cs.dennisc.scenegraph.Visual sgVisual = pickResult.getVisual();
							if( sgVisual != null ) {
								g.setColor( mapSgVisualToColorIcon.get( sgVisual ).getFillColor() );
							} else {
								g.setColor( java.awt.Color.BLACK );
							}
							g.fillRect( xPixel, yPixel, PIXELS_PER_PICK, PIXELS_PER_PICK );
						}
					}
				}
			}
		}

		@Override
		public java.awt.Dimension getPreferredSize() {
			return new java.awt.Dimension( 800, 450 );
		}
	};
}
