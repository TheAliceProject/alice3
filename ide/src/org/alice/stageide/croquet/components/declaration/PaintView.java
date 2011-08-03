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

package org.alice.stageide.croquet.components.declaration;

/**
 * @author Dennis Cosgrove
 */
public class PaintView extends org.lgna.croquet.components.ViewController< javax.swing.JComponent, org.alice.stageide.croquet.models.declaration.PaintState > {
	private org.lgna.croquet.State.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Expression > valueObserver = new org.lgna.croquet.State.ValueObserver< edu.cmu.cs.dennisc.alice.ast.Expression >() {
		public void changing( org.lgna.croquet.State< edu.cmu.cs.dennisc.alice.ast.Expression > state, edu.cmu.cs.dennisc.alice.ast.Expression prevValue, edu.cmu.cs.dennisc.alice.ast.Expression nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< edu.cmu.cs.dennisc.alice.ast.Expression > state, edu.cmu.cs.dennisc.alice.ast.Expression prevValue, edu.cmu.cs.dennisc.alice.ast.Expression nextValue, boolean isAdjusting ) {
			PaintView.this.repaint();
		}
	};
	public PaintView( org.alice.stageide.croquet.models.declaration.PaintState model ) {
		super( model );
	}
	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.getModel().addValueObserver( this.valueObserver );
	}
	@Override
	protected void handleUndisplayable() {
		this.getModel().removeValueObserver( this.valueObserver );
		super.handleUndisplayable();
	}
	@Override
	protected javax.swing.JComponent createAwtComponent() {
		javax.swing.JComponent rv = new javax.swing.JComponent() {
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( 128, 128 );
			}
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				edu.cmu.cs.dennisc.alice.ast.Expression expression = PaintView.this.getModel().getValue();
				if( expression != null ) {
					edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = org.alice.stageide.StageIDE.getActiveInstance().getVirtualMachineForSceneEditor();
					
					Object[] values = vm.ENTRY_POINT_evaluate( null, new edu.cmu.cs.dennisc.alice.ast.Expression[] { expression } );
					assert values.length == 1;
					if( values[ 0 ] instanceof org.lgna.story.Paint ) {
						org.lgna.story.Paint paint = (org.lgna.story.Paint)values[ 0 ];
						
						edu.cmu.cs.dennisc.color.Color4f color = org.lgna.story.ImplementationAccessor.getColor4f( paint, null );
						edu.cmu.cs.dennisc.texture.BufferedImageTexture texture = org.lgna.story.ImplementationAccessor.getTexture( paint, null );

						if( color != null ) {
							g.setColor( color.getAsAWTColor() );
							g.fillRect( 0, 0, this.getWidth(), this.getHeight() );
						}
						if( texture != null ) {
							edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredScaledToFitImage( g, texture.getBufferedImage(), this );
						}
					}
				}
			}
		};
		return rv;
	}
}
