/*
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.lgna.story.implementation.overlay;

// from ModelImp
//  public void sayText( String textToSay, org.alice.flite.VoiceType voice, edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble ) {
//    final boolean showSpeechBubble = bubble != null;
//    final org.lgna.common.resources.TextToSpeechResource tts = org.lgna.common.resources.TextToSpeechResource.valueOf( textToSay, voice.getVoiceString() );
//    final org.lgna.story.implementation.overlay.AudioLimitedBubbleAnimation bubbleAnimation = ( showSpeechBubble ) ? new org.lgna.story.implementation.overlay.AudioLimitedBubbleAnimation( this, .3, .3, bubble ) : null;
//
//    Runnable textToSpeech = new Runnable() {
//      public void run() {
//        if( !tts.isLoaded() )
//        {
//          tts.loadResource();
//        }
//        edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
//        edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( tts, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_VOLUME, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_START_TIME, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_STOP_TIME );
//        if( showSpeechBubble )
//        {
//          bubbleAnimation.setDuration( tts.getDuration() + .2 );
//        }
//        perform( new edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation( player ) );
//      }
//    };
//    if( showSpeechBubble )
//    {
//      Runnable[] runnables = new Runnable[ 2 ];
//      runnables[ 0 ] = new Runnable() {
//        public void run() {
//          perform( bubbleAnimation );
//        }
//      };
//      runnables[ 1 ] = textToSpeech;
//      org.lgna.common.ThreadUtilities.doTogether( runnables );
//    }
//    else
//    {
//      textToSpeech.run();
//    }
//
//  }

/**
 * @author dculyba
 *
 */
public class AudioLimitedBubbleAnimation extends BubbleAnimation implements org.lgna.common.resources.TextToSpeechResource.ResourceLoadedObserver {

  private long startTime;

  /**
   * @param entity
   * @param openingDuration
   * @param updatingDuration
   * @param closingDuration
   * @param bubble
   */
  public AudioLimitedBubbleAnimation(org.lgna.story.implementation.EntityImp entityImp, double openingDuration, double closingDuration, edu.cmu.cs.dennisc.scenegraph.graphics.Bubble bubble) {
    super(entityImp, openingDuration, 100.0, closingDuration, bubble);
    this.startTime = 0;
  }

  @Override
  protected void prologue() {
    super.prologue();
    this.startTime = System.currentTimeMillis();
  }

  @Override
  protected void epilogue() {
    super.epilogue();
    this.startTime = 0;
  }

  public void setDuration(double duration) {
    double elapsedTime = 0;
    if (this.startTime != 0) {
      long currentTime = System.currentTimeMillis();
      elapsedTime = (currentTime - this.startTime) * 0.001;
    }
    if (elapsedTime > this.m_openingDuration) {
      this.m_updatingDuration = (duration + elapsedTime) - this.m_openingDuration;
    } else {
      this.m_updatingDuration = duration;
    }
  }

  public void ResourceLoaded(org.lgna.common.resources.TextToSpeechResource resource) {
    this.m_updatingDuration = resource.getDuration();
  }

}
