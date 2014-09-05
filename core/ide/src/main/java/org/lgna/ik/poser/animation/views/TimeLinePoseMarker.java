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
package org.lgna.ik.poser.animation.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.plaf.basic.BasicToggleButtonUI;

import org.lgna.croquet.Application;
import org.lgna.croquet.triggers.NullTrigger;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.edits.ModifyTimeOfExistingKeyFrameInTimeLineEdit;

class TimeLinePoseMarkerUI extends BasicToggleButtonUI {

	private static Paint FOCUS_PAINT = new Color( 191, 191, 255, 127 );
	private static Paint ROLLOVER_PAINT = new Color( 191, 191, 191, 127 );
	private static Paint NORMAL_PAINT = new Color( 221, 221, 221, 127 );

	private static java.awt.Shape createShape( JComponent c ) {
		int w = c.getWidth() - 1;
		int h = c.getHeight() - 1;

		double x0 = w * 0.25;
		double x1 = w - x0;
		double xCenter = ( x0 + x1 ) * 0.5;

		double yA = x0;
		double yC = h;
		double yB = yA + ( ( yC - yA ) * 0.8 );

		java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
		path.moveTo( x0, yA );
		path.lineTo( x1, yA );
		path.lineTo( x1, yB );
		path.lineTo( xCenter, yC );
		path.lineTo( x0, yB );
		path.closePath();
		//java.awt.Shape cap = new java.awt.geom.Ellipse2D.Float( 0, 0, w, w );
		java.awt.Shape cap = new java.awt.geom.RoundRectangle2D.Float( 0, 0, w, w / 2, 8, 8 );
		return edu.cmu.cs.dennisc.java.awt.geom.AreaUtilities.createUnion( path, cap );
	}

	@Override
	public void paint( Graphics g, JComponent c ) {
		//note: do not invoke super
		Graphics2D g2 = (Graphics2D)g;
		edu.cmu.cs.dennisc.java.awt.GraphicsContext gc = new edu.cmu.cs.dennisc.java.awt.GraphicsContext();
		gc.pushAll( g2 );
		gc.pushPaint();
		gc.pushAndSetAntialiasing( true );
		AbstractButton button = (AbstractButton)c;
		ButtonModel buttonModel = button.getModel();
		Paint circlePaint;
		if( buttonModel.isRollover() ) {
			circlePaint = ROLLOVER_PAINT;
		} else {
			if( button.isFocusOwner() || buttonModel.isSelected() ) {
				circlePaint = FOCUS_PAINT;
			} else {
				circlePaint = NORMAL_PAINT;
			}
		}
		java.awt.Shape shape = createShape( c );
		g2.setPaint( circlePaint );
		g2.fill( shape );

		Paint drawPaint = button.isSelected() ? Color.BLACK : Color.GRAY;
		g2.setPaint( drawPaint );
		g2.draw( shape );

		//todo: base on component size
		int w = c.getWidth();
		for( int xLine = 3; xLine < ( w - 3 ); xLine += 3 ) {
			g2.drawLine( xLine, 4, xLine, 12 );
		}

		gc.popAll();
	}

	@Override
	public boolean contains( javax.swing.JComponent c, int x, int y ) {
		return createShape( c ).contains( x, y );
	}
}

class JTimeLinePoseMarker extends JToggleButton {
	public static final Dimension SIZE = new Dimension( 32, 48 );

	public JTimeLinePoseMarker( KeyFrameData data, JTimeLineView jView ) {
		this.keyFrameData = data;
		this.parent = jView;
		this.setOpaque( false );
		this.setRolloverEnabled( true );
		this.addMouseListener( listener );
		this.addMouseMotionListener( motionListener );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		if( data.equals( jView.getComposite().getSelectedKeyFrame() ) ) {
			setSelected( true );
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return SIZE;
	}

	@Override
	public void updateUI() {
		this.setUI( new TimeLinePoseMarkerUI() );
	}

	public KeyFrameData getKeyFrameData() {
		return keyFrameData;
	}

	private final JTimeLineView parent;
	private final KeyFrameData keyFrameData;
	private final MouseListener listener = new MouseListener() {

		@Override
		public void mousePressed( MouseEvent e ) {
			if( JTimeLinePoseMarker.this.isSelected() ) {
				tPressed = keyFrameData.getEventTime();
				isSliding = true;
			}

			//			System.out.println( keyFrameData.getPose().getFakeLeftHandPosition() + " \t " + keyFrameData.getPose().getFakeRightHandPosition() );
		}

		@Override
		public void mouseReleased( MouseEvent e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e );
			if( isSliding ) {
				org.lgna.croquet.history.TransactionHistory history = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
				org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( null, NullTrigger.createUserInstance() );

				double tCurrent = keyFrameData.getEventTime();
				final double THRESHOLD = 0.0001;
				if( Math.abs( tCurrent - tPressed ) > THRESHOLD ) {
					step.commitAndInvokeDo( new ModifyTimeOfExistingKeyFrameInTimeLineEdit( step, parent.getComposite().getTimeLine(), keyFrameData, tCurrent, tPressed ) );
					JTimeLinePoseMarker.this.setSelected( true );
				} else {
					parent.getComposite().getTimeLine().moveExistingKeyFrameData( keyFrameData, tPressed );
				}
			}
			isSliding = false;
		}

		@Override
		public void mouseClicked( MouseEvent e ) {
		}

		@Override
		public void mouseEntered( MouseEvent e ) {
		}

		@Override
		public void mouseExited( MouseEvent e ) {
		}

	};
	private final MouseMotionListener motionListener = new MouseMotionListener() {

		@Override
		public void mouseMoved( MouseEvent e ) {
		}

		@Override
		public void mouseDragged( MouseEvent e ) {
			if( isSliding ) {
				parent.getComposite().getTimeLine().moveExistingKeyFrameData( keyFrameData, parent.getTime( e ) );
				revalidate();
			}

		}
	};
	private boolean isSliding;
	private double tPressed;
}
