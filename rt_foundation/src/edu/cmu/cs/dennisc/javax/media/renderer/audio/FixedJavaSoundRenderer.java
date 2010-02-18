/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.javax.media.renderer.audio;

/**
 * @author Dennis Cosgrove
 */
public class FixedJavaSoundRenderer extends com.sun.media.renderer.audio.JavaSoundRenderer {
	public static void usurpControlFromJavaSoundRenderer() {
		final String OFFENDING_RENDERER_PLUGIN_NAME = com.sun.media.renderer.audio.JavaSoundRenderer.class.getName();
		javax.media.Format[] rendererInputFormats = javax.media.PlugInManager.getSupportedInputFormats( OFFENDING_RENDERER_PLUGIN_NAME, javax.media.PlugInManager.RENDERER );
		javax.media.Format[] rendererOutputFormats = javax.media.PlugInManager.getSupportedOutputFormats( OFFENDING_RENDERER_PLUGIN_NAME, javax.media.PlugInManager.RENDERER );
		//should be only rendererInputFormats
		if( rendererInputFormats != null || rendererOutputFormats != null ) {
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
				g = Math.max( g, this.gc.getMinimum() );
				g = Math.min( g, this.gc.getMaximum() );
				super.setGain( g );
			}
		};
	}
}
