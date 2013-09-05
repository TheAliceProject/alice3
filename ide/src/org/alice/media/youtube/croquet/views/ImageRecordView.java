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
package org.alice.media.youtube.croquet.views;

import org.alice.media.youtube.croquet.ImageRecordComposite;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.ToggleButton;

import edu.cmu.cs.dennisc.matt.eventscript.EventScript.EventWithTime;

/**
 * @author Matt May
 */
public class ImageRecordView extends org.lgna.croquet.components.MigPanel {
	private final org.lgna.croquet.components.BorderPanel lookingGlassContainer = new org.lgna.croquet.components.BorderPanel();
	private final Label timerLabel;
	private final ToggleButton playPauseButton;

	private static final java.text.SimpleDateFormat MINUTE_SECOND_FORMAT = new java.text.SimpleDateFormat( "mm:ss." );
	private static final java.text.NumberFormat CENTISECOND_FORMAT = new java.text.DecimalFormat( "00" );

	public ImageRecordView( org.alice.media.youtube.croquet.ImageRecordComposite recordComposite ) {
		super( recordComposite, "fill, insets 0", "[grow,shrink][grow 0,shrink]", "[grow 0,shrink][grow, shrink][grow 0,shrink]" );

		org.lgna.croquet.components.List<EventWithTime> list = recordComposite.getEventList().createList();
		list.setCellRenderer( recordComposite.getCellRenderer() );
		list.setBackgroundColor( this.getBackgroundColor() );

		playPauseButton = recordComposite.getIsRecordingState().createToggleButton();
		this.timerLabel = new Label();
		this.timerLabel.setHorizontalAlignment( org.lgna.croquet.components.HorizontalAlignment.TRAILING );
		this.timerLabel.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED );
		this.updateTime();

		this.addComponent( recordComposite.getRestartOperation().createButton(), "align right" );
		this.addComponent( list, "grow, shrink, spany 3, wrap" );

		this.addComponent( this.lookingGlassContainer, "grow, wrap" );

		this.addComponent( playPauseButton, "split 2" );
		this.addComponent( timerLabel, "grow, align right, wrap" );

		//this.addComponent( recordComposite.getFrameRateState().getSidekickLabel().createLabel(), "push" );
		//this.addComponent( recordComposite.getFrameRateState().createSpinner() );
	}

	public org.lgna.croquet.components.BorderPanel getLookingGlassContainer() {
		return this.lookingGlassContainer;
	}

	public void updateTime() {
		double timeInSeconds = ( (ImageRecordComposite)this.getComposite() ).getTimerInSeconds();
		long msec = (long)( timeInSeconds * 1000 );
		java.util.Date date = new java.util.Date( msec );
		StringBuilder sb = new StringBuilder();
		sb.append( MINUTE_SECOND_FORMAT.format( date ) );
		sb.append( CENTISECOND_FORMAT.format( ( msec % 1000 ) / 10 ) );
		this.timerLabel.setText( sb.toString() );
	}

	public ToggleButton getPlayPauseButton() {
		return this.playPauseButton;
	}
}
