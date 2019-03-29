/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser.anchors.views;

import edu.cmu.cs.dennisc.math.GoldenRatio;
import org.lgna.croquet.Application;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.Frame;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.ik.poser.anchors.Anchors;
import org.lgna.ik.poser.anchors.events.AnchorEvent;
import org.lgna.ik.poser.anchors.events.AnchorListener;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

/**
 * @author Dennis Cosgrove
 */
public class AnchorsView extends SwingComponentView<JComponent> {
	private static class Node {
		private static final Shape SHAPE = new Ellipse2D.Float( -8, -8, 16, 16 );
		private final double theta;
		private final double length;
		private boolean isSelected;

		public Node( double theta, double length ) {
			this.theta = theta;
			this.length = length;
		}

		public boolean isSelected() {
			return this.isSelected;
		}

		public void setSelected( boolean isSelected ) {
			this.isSelected = isSelected;
		}

		public void paint( Graphics2D g2 ) {
			g2.rotate( this.theta );
			g2.setPaint( Color.BLACK );
			g2.drawLine( 0, 0, 0, (int)this.length );

			if( this.isSelected() ) {
				g2.setPaint( Color.GREEN );
			} else {
				g2.setPaint( Color.GRAY );
			}
			g2.fill( SHAPE );

			g2.setPaint( Color.BLACK );
			g2.draw( SHAPE );
			g2.translate( 0, this.length );
		}
	}

	private abstract static class Chain {
		private final double tx;
		private final double ty;
		private final Node[] nodes;

		public Chain( double tx, double ty, Node... nodes ) {
			this.tx = tx;
			this.ty = ty;
			this.nodes = nodes;
			this.nodes[ 0 ].setSelected( true );
		}

		public void paint( Graphics2D g2 ) {
			AffineTransform prevTransform = g2.getTransform();
			g2.translate( this.tx, this.ty );
			for( Node node : this.nodes ) {
				node.paint( g2 );
			}
			g2.setTransform( prevTransform );
		}
	}

	private static class ArmChain extends Chain {
		public ArmChain( double tx, double ty, double direction ) {
			super( tx, ty, new Node( ( Math.PI / 8 ) * direction, 80 ), new Node( ( -Math.PI / 6 ) * direction, 60 ), new Node( 0, 24 ) );
		}
	}

	private static class LegChain extends Chain {
		public LegChain( double tx, double ty, double direction ) {
			super( tx, ty, new Node( ( Math.PI / 24 ) * direction, 100 ), new Node( ( -Math.PI / 24 ) * direction, 80 ), new Node( 0, 16 ) );
		}
	}

	private final class JAnchorsView extends JComponent {
		private static final double LEFT__SIDE_DIRECTION = 1;
		private static final double RIGHT_SIDE_DIRECTION = -1;
		private static final double ARM_Y = 40;
		private static final double LEG_Y = 180;
		private final ArmChain leftArmChain = new ArmChain( 60, ARM_Y, LEFT__SIDE_DIRECTION );
		private final ArmChain rightArmChain = new ArmChain( 160, ARM_Y, RIGHT_SIDE_DIRECTION );

		private final LegChain leftLegChain = new LegChain( 80, LEG_Y, LEFT__SIDE_DIRECTION );
		private final LegChain rightLegChain = new LegChain( 140, LEG_Y, RIGHT_SIDE_DIRECTION );

		@Override
		protected void paintComponent( Graphics g ) {
			super.paintComponent( g );
			Graphics2D g2 = (Graphics2D)g;
			this.leftArmChain.paint( g2 );
			this.rightArmChain.paint( g2 );
			this.leftLegChain.paint( g2 );
			this.rightLegChain.paint( g2 );
		}

		@Override
		public Dimension getPreferredSize() {
			return GoldenRatio.createTallerSizeFromWidth( 240 );
		}
	}

	private final Anchors anchors;

	private final AnchorListener anchorListener = new AnchorListener() {
		@Override
		public void leftArmChanged( AnchorEvent e ) {
			getAwtComponent().repaint();
		}

		@Override
		public void rightArmChanged( AnchorEvent e ) {
			getAwtComponent().repaint();
		}

		@Override
		public void leftLegChanged( AnchorEvent e ) {
			getAwtComponent().repaint();
		}

		@Override
		public void rightLegChanged( AnchorEvent e ) {
			getAwtComponent().repaint();
		}
	};

	public AnchorsView( Anchors anchors ) {
		this.anchors = anchors;
	}

	@Override
	protected JComponent createAwtComponent() {
		return new JAnchorsView();
	}

	public static void main( String[] args ) {
		Application app = new SimpleApplication();
		app.getDocumentFrame().getFrame().getContentPane().addCenterComponent( new AnchorsView( null ) );
		app.getDocumentFrame().getFrame().setDefaultCloseOperation( Frame.DefaultCloseOperation.EXIT );
		app.getDocumentFrame().getFrame().pack();
		app.getDocumentFrame().getFrame().setVisible( true );
	}
}
