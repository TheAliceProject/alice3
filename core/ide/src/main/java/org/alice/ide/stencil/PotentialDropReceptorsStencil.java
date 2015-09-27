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
package org.alice.ide.stencil;

/**
 * @author Dennis Cosgrove
 * 
 */
public class PotentialDropReceptorsStencil extends org.lgna.croquet.views.LayerStencil {

	private static final java.awt.Stroke THIN_STROKE = new java.awt.BasicStroke( 1.0f );
	private static final java.awt.Stroke THICK_STROKE = new java.awt.BasicStroke( 3.0f );

	private java.util.List<org.lgna.croquet.DropReceptor> holes = null;
	private org.lgna.croquet.views.DragComponent<?> potentialDragSource;
	private org.lgna.croquet.views.AwtComponentView<?> currentDropReceptorComponent;

	public PotentialDropReceptorsStencil( org.lgna.croquet.views.AbstractWindow<?> window ) {
		super( window, javax.swing.JLayeredPane.POPUP_LAYER + 1 );
	}

	public void handleDragStarted( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.potentialDragSource = null;
		if( this.holes != null ) {
			this.repaint();
		}
	}

	public void handleDragEnteredDropReceptor( org.lgna.croquet.history.DragStep dragAndDropContext ) {
	}

	public void handleDragExitedDropReceptor( org.lgna.croquet.history.DragStep dragAndDropContext ) {
		this.currentDropReceptorComponent = null;
		if( this.holes != null ) {
			this.repaint();
		}
	}

	public void handleDragStopped( org.lgna.croquet.history.DragStep dragAndDropContext ) {
	}

	private boolean isFauxStencilDesired() {
		return org.alice.ide.IDE.getActiveInstance().isDragInProgress();
	}

	public void setDragInProgress( boolean isDragInProgress ) {
		this.currentDropReceptorComponent = null;
	}

	public void showStencilOver( org.lgna.croquet.views.DragComponent<?> potentialDragSource, final org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.perspectives.ProjectPerspective idePerspective = (org.alice.ide.perspectives.ProjectPerspective)ide.getPerspective();
		java.util.List<org.lgna.croquet.DropReceptor> dropReceptors = idePerspective.createListOfPotentialDropReceptors( (org.alice.ide.croquet.models.IdeDragModel)potentialDragSource.getModel() );
		if( dropReceptors.size() > 0 ) {
			this.holes = dropReceptors;
			this.potentialDragSource = potentialDragSource;
			this.setStencilShowing( true );
		}
	}

	public void hideStencil() {
		this.setStencilShowing( false );
		this.holes = null;
		this.potentialDragSource = null;
	}

	@Override
	protected boolean contains( int x, int y, boolean superContains ) {
		return superContains;
	}

	@Override
	protected void paintComponentPrologue( java.awt.Graphics2D g2 ) {
		if( this.holes != null ) {
			synchronized( this.holes ) {
				java.awt.geom.Area area = new java.awt.geom.Area( new java.awt.Rectangle( 0, 0, getWidth(), getHeight() ) );
				if( this.currentDropReceptorComponent != null ) {
					g2.setPaint( new java.awt.Color( 0, 0, 127, 95 ) );
				} else {
					g2.setPaint( new java.awt.Color( 0, 0, 127, 127 ) );
				}

				java.awt.Rectangle potentialDragSourceBounds;
				if( this.potentialDragSource != null ) {
					potentialDragSourceBounds = javax.swing.SwingUtilities.convertRectangle( this.potentialDragSource.getParent().getAwtComponent(), this.potentialDragSource.getBounds(), this.getAwtComponent() );
				} else {
					potentialDragSourceBounds = null;
				}

				if( isFauxStencilDesired() ) {
					for( org.lgna.croquet.DropReceptor dropReceptor : this.holes ) {
						org.lgna.croquet.views.AwtComponentView<?> component = (org.lgna.croquet.views.AwtComponentView<?>)dropReceptor;
						java.awt.Rectangle holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent().getAwtComponent(), component.getBounds(), this.getAwtComponent() );
						area.subtract( new java.awt.geom.Area( holeBounds ) );
					}

					if( potentialDragSourceBounds != null ) {
						area.subtract( new java.awt.geom.Area( potentialDragSourceBounds ) );
					}
					g2.fill( area );
				}

				g2.setStroke( THICK_STROKE );
				final int BUFFER = 6;
				for( org.lgna.croquet.DropReceptor dropReceptor : this.holes ) {
					org.lgna.croquet.views.AwtComponentView<?> component = (org.lgna.croquet.views.AwtComponentView<?>)dropReceptor;
					java.awt.Rectangle holeBounds = javax.swing.SwingUtilities.convertRectangle( component.getParent().getAwtComponent(), component.getBounds(), this.getAwtComponent() );
					holeBounds.x -= BUFFER;
					holeBounds.y -= BUFFER;
					holeBounds.width += 2 * BUFFER;
					holeBounds.height += 2 * BUFFER;

					g2.setColor( new java.awt.Color( 0, 0, 0 ) );
					g2.draw( holeBounds );
					if( this.currentDropReceptorComponent == component ) {
						g2.setColor( new java.awt.Color( 0, 255, 0 ) );
						g2.setStroke( THIN_STROKE );
						g2.draw( holeBounds );
						if( this.currentDropReceptorComponent == component ) {
							g2.setColor( new java.awt.Color( 0, 255, 0 ) );
							g2.setStroke( THIN_STROKE );
							g2.draw( holeBounds );
							g2.setStroke( THICK_STROKE );
							g2.setColor( new java.awt.Color( 191, 255, 191, 63 ) );
							g2.fill( holeBounds );
						}
					}
				}
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this );
		}
	}

	@Override
	protected void paintComponentEpilogue( java.awt.Graphics2D g2 ) {
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2 ) {
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return null;
	}
}
