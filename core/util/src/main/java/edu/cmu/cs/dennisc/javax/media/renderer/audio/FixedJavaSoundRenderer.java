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
package edu.cmu.cs.dennisc.javax.media.renderer.audio;

/**
 * @author Dennis Cosgrove
 */
public class FixedJavaSoundRenderer extends com.sun.media.renderer.audio.JavaSoundRenderer {
	public static void usurpControlFromJavaSoundRenderer() {
		final String OFFENDING_RENDERER_PLUGIN_NAME = com.sun.media.renderer.audio.JavaSoundRenderer.class.getName();
		javax.media.Format[] rendererInputFormats = javax.media.PlugInManager.getSupportedInputFormats( OFFENDING_RENDERER_PLUGIN_NAME, javax.media.PlugInManager.RENDERER );
		javax.media.Format[] rendererOutputFormats = javax.media.PlugInManager.getSupportedOutputFormats( OFFENDING_RENDERER_PLUGIN_NAME, javax.media.PlugInManager.RENDERER );
		if( ( rendererInputFormats != null ) || ( rendererOutputFormats != null ) ) {
			final String REPLACEMENT_RENDERER_PLUGIN_NAME = FixedJavaSoundRenderer.class.getName();
			javax.media.PlugInManager.removePlugIn( OFFENDING_RENDERER_PLUGIN_NAME, javax.media.PlugInManager.RENDERER );
			javax.media.PlugInManager.addPlugIn( REPLACEMENT_RENDERER_PLUGIN_NAME, rendererInputFormats, rendererOutputFormats, javax.media.PlugInManager.RENDERER );
		}
	}

	@Override
	protected com.sun.media.renderer.audio.device.AudioOutput createDevice( javax.media.format.AudioFormat format ) {
		return new com.sun.media.renderer.audio.device.JavaSoundOutput() {
			@Override
			public void setGain( double g ) {
				if( this.gc != null ) {
					g = Math.max( g, this.gc.getMinimum() );
					g = Math.min( g, this.gc.getMaximum() );
				}
				super.setGain( g );
			}
		};
	}
}
