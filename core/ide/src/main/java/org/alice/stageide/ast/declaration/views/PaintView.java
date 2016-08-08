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

package org.alice.stageide.ast.declaration.views;

/**
 * @author Dennis Cosgrove
 */
public class PaintView extends org.lgna.croquet.views.ViewController<javax.swing.JComponent, org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression>> {
	private org.lgna.croquet.event.ValueListener<org.lgna.project.ast.Expression> valueListener = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.Expression>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.Expression> e ) {
			PaintView.this.repaint();
		}
	};

	public PaintView( org.lgna.croquet.CustomItemState<org.lgna.project.ast.Expression> model ) {
		super( model );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().addNewSchoolValueListener( this.valueListener );
	}

	@Override
	protected void handleUndisplayable() {
		this.getModel().removeNewSchoolValueListener( this.valueListener );
		super.handleUndisplayable();
	}

	@Override
	protected javax.swing.JComponent createAwtComponent() {
		javax.swing.JComponent rv = new javax.swing.JComponent() {
			@Override
			public java.awt.Dimension getMinimumSize() {
				return this.getPreferredSize();
			}

			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				org.lgna.project.ast.Expression expression = PaintView.this.getModel().getValue();
				if( expression != null ) {
					org.lgna.project.virtualmachine.VirtualMachine vm = org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getVirtualMachine();

					Object[] values = vm.ENTRY_POINT_evaluate( null, new org.lgna.project.ast.Expression[] { expression } );
					assert values.length == 1;
					if( values[ 0 ] instanceof org.lgna.story.Paint ) {
						org.lgna.story.Paint paint = (org.lgna.story.Paint)values[ 0 ];

						edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.EmployeesOnly.getColor4f( paint, null );
						edu.cmu.cs.dennisc.texture.Texture texture = org.lgna.story.EmployeesOnly.getTexture( paint, null );

						if( color != null ) {
							g.setColor( edu.cmu.cs.dennisc.java.awt.ColorUtilities.toAwtColor( color ) );
							g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
						}
						if( texture instanceof edu.cmu.cs.dennisc.texture.BufferedImageTexture ) {
							edu.cmu.cs.dennisc.texture.BufferedImageTexture bufferedImageTexture = (edu.cmu.cs.dennisc.texture.BufferedImageTexture)texture;
							edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredScaledToFitImage( g, bufferedImageTexture.getBufferedImage(), this );
						}
					}
				}
			}
		};
		rv.setPreferredSize( new java.awt.Dimension( 128, 128 ) );
		return rv;
	}
}
