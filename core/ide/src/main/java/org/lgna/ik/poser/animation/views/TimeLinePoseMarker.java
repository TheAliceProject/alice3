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
import java.awt.event.ItemEvent;
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

	@Override
	public void paint( Graphics g, JComponent c ) {
		//note: do not invoke super
		Graphics2D g2 = (Graphics2D)g;
		AbstractButton button = (AbstractButton)c;
		ButtonModel buttonModel = button.getModel();
		Paint circlePaint;
		if( buttonModel.isSelected() ) {
			circlePaint = Color.YELLOW;
		} else {
			if( buttonModel.isRollover() ) {
				circlePaint = Color.GREEN;
			} else {
				circlePaint = Color.BLUE;
			}
		}
		g2.setPaint( Color.BLACK );
		g2.fillRect( button.getWidth() / 4, button.getWidth() / 4, button.getWidth() / 2, button.getHeight() - ( button.getWidth() / 4 ) );
		g2.setPaint( circlePaint );
		g2.fillOval( 0, 0, button.getWidth(), button.getWidth() );
	}
}

class JTimeLinePoseMarker extends JToggleButton {
	public static Dimension SIZE = new Dimension( 32, 64 );
	private KeyFrameData keyFrameData;
	private boolean isSliding = false;
	private final JTimeLineView parent;
	private double prevEventTime;

	public JTimeLinePoseMarker( KeyFrameData data, JTimeLineView jView ) {
		this.keyFrameData = data;
		this.parent = jView;
		this.setOpaque( false );
		this.setRolloverEnabled( true );
		this.addMouseListener( listener );
		this.addMouseMotionListener( motionListener );
	}

	private MouseListener listener = new MouseListener() {

		public void mouseReleased( MouseEvent e ) {
			if( isSliding ) {
				org.lgna.croquet.history.TransactionHistory history = Application.getActiveInstance().getApplicationOrDocumentTransactionHistory().getActiveTransactionHistory();
				org.lgna.croquet.history.Transaction transaction = history.acquireActiveTransaction();
				org.lgna.croquet.history.CompletionStep<?> step = transaction.createAndSetCompletionStep( null, NullTrigger.createUserInstance() );

				double newTime = keyFrameData.getEventTime();
				if( Math.abs( newTime - prevEventTime ) > 0 ) {
					step.commitAndInvokeDo( new ModifyTimeOfExistingKeyFrameInTimeLineEdit( step, parent.getComposite().getTimeLine(), keyFrameData, newTime, prevEventTime ) );
					JTimeLinePoseMarker.this.setSelected( true );
				} else {
					parent.getComposite().getTimeLine().moveExistingKeyFrameData( keyFrameData,
							prevEventTime );
				}
			}
			isSliding = false;
		}

		public void mousePressed( MouseEvent e ) {
			if( JTimeLinePoseMarker.this.isSelected() ) {
				prevEventTime = keyFrameData.getEventTime();
				isSliding = true;
			}

			//			System.out.println( keyFrameData.getPose().getFakeLeftHandPosition() + " \t " + keyFrameData.getPose().getFakeRightHandPosition() );
		}

		public void mouseExited( MouseEvent e ) {
		}

		public void mouseEntered( MouseEvent e ) {
		}

		public void mouseClicked( MouseEvent e ) {
		}
	};
	private MouseMotionListener motionListener = new MouseMotionListener() {

		public void mouseMoved( MouseEvent e ) {
		}

		public void mouseDragged( MouseEvent e ) {
			if( isSliding ) {
				parent.getComposite().getTimeLine().moveExistingKeyFrameData( keyFrameData,
						parent.getTime( e ) );
				revalidate();
			}

		}
	};

	@Override
	public Dimension getPreferredSize() {
		return SIZE;
	}

	@Override
	public boolean contains( int x, int y ) {
		return super.contains( x, y );
	}

	@Override
	public void updateUI() {
		this.setUI( new TimeLinePoseMarkerUI() );
	}

	@Override
	protected void fireItemStateChanged( ItemEvent event ) {
		super.fireItemStateChanged( event );
	}

	public KeyFrameData getKeyFrameData() {
		return keyFrameData;
	}

}
