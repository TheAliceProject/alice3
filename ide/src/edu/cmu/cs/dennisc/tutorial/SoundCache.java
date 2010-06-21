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
package edu.cmu.cs.dennisc.tutorial;

/**
 * @author Dennis Cosgrove
 */
public enum SoundCache {
	//SUCCESS( javax.swing.plaf.metal.MetalLookAndFeel.class, "sounds/OptionPaneInformation.wav", false ),
	SUCCESS( null, null, false ),
	FAILURE( javax.swing.plaf.metal.MetalLookAndFeel.class, "sounds/OptionPaneError.wav", true );
	private Class<?> cls;
	private String resourceName;
	private boolean isAttempted = false;
	private javax.sound.sampled.Clip clip;
	private boolean isBeepDesiredAsFallback;
	private static int ignoreCount = 0;
	SoundCache( Class<?> cls, String resourceName, boolean isBeepDesiredAsFallback ) {
		this.cls = cls;
		this.resourceName = resourceName;
		this.isBeepDesiredAsFallback = isBeepDesiredAsFallback;
	}
	public static void pushIgnore() {
		ignoreCount ++;
	}
	public static void popIgnore() {
		ignoreCount --;
	}
	public synchronized void startIfNotAlreadyActive() {
		if( ignoreCount == 0 ) {
			if( this.cls != null && this.resourceName != null ) {
				if( this.isAttempted ) {
					//pass
				} else {
					this.isAttempted = true;
					try {
						this.clip = edu.cmu.cs.dennisc.javax.sound.SoundUtilities.createOpenedClip( this.cls, this.resourceName );
					} catch( Exception e ) {
						e.printStackTrace();
					}
				}
				if( this.clip != null ) {
					if( this.clip.isActive() ) {
						//pass
					} else {
						this.clip.setFramePosition( 0 );
						this.clip.start();
					}
				} else {
					if( this.isBeepDesiredAsFallback ) {
						java.awt.Toolkit.getDefaultToolkit().beep();
					}
				}
			}
		}
	}
}
