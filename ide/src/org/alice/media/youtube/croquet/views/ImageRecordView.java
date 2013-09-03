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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.alice.media.youtube.croquet.ImageRecordComposite;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.List;
import org.lgna.croquet.components.ToggleButton;

import edu.cmu.cs.dennisc.matt.EventScript.EventWithTime;

/**
 * @author Matt May
 */
public class ImageRecordView extends org.lgna.croquet.components.MigPanel {
	private final org.lgna.croquet.components.BorderPanel lookingGlassContainer = new org.lgna.croquet.components.BorderPanel();
	private final Label timerLabel;
	private ToggleButton playPauseButton;

	public ImageRecordView( org.alice.media.youtube.croquet.ImageRecordComposite recordComposite ) {
		super( recordComposite, "insets 0", "" );
		org.lgna.croquet.components.Panel panel = new org.lgna.croquet.components.FixedCenterPanel( this.lookingGlassContainer );

		List<EventWithTime> list = recordComposite.getEventList().createList();
		list.setMinimumPreferredWidth( 400 );
		list.setOpaque( false );

		playPauseButton = recordComposite.getIsRecordingState().createToggleButton();
		timerLabel = new Label( String.valueOf( recordComposite.getTimerInSeconds() ) );

		this.addComponent( panel, "span 5" );
		list.setCellRenderer( recordComposite.getCellRenderer() );
		this.addComponent( list, "wrap, grow, spany 2" );

		this.addComponent( playPauseButton, "push" );
		this.addComponent( recordComposite.getFrameRateState().getSidekickLabel().createImmutableTextField() );
		this.addComponent( recordComposite.getFrameRateState().createSpinner() );
		this.addComponent( timerLabel, "push" );
		this.addComponent( recordComposite.getRestartOperation().createButton(), "align right, wrap" );
	}

	public org.lgna.croquet.components.BorderPanel getLookingGlassContainer() {
		return this.lookingGlassContainer;
	}

	public void updateTime() {
		double timeInSeconds = ( (ImageRecordComposite)this.getComposite() ).getTimerInSeconds() * 1000;
		Date date = new Date( (long)timeInSeconds );
		String formattedDate = new SimpleDateFormat( "mm:ss.SS" ).format( date );
		timerLabel.setText( formattedDate );
	}

	public ToggleButton getPlayPauseButton() {
		return this.playPauseButton;
	}
}
