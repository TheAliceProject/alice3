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
package edu.cmu.cs.dennisc.texture;

/**
 * @author Dennis Cosgrove
 */
public final class TextureFactory {
	private static java.util.Map<org.lgna.common.resources.ImageResource, BufferedImageTexture> resourceToTextureMap = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private static org.lgna.common.event.ResourceContentListener resourceContentListener = new org.lgna.common.event.ResourceContentListener() {
		public void contentChanging( org.lgna.common.event.ResourceContentEvent e ) {
		}

		public void contentChanged( org.lgna.common.event.ResourceContentEvent e ) {
			org.lgna.common.Resource resource = e.getTypedSource();
			if( resource instanceof org.lgna.common.resources.ImageResource ) {
				org.lgna.common.resources.ImageResource imageResource = (org.lgna.common.resources.ImageResource)resource;
				java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( imageResource );
				if( bufferedImage != null ) {
					Texture texture = TextureFactory.resourceToTextureMap.get( e.getTypedSource() );
					if( texture instanceof BufferedImageTexture ) {
						BufferedImageTexture bufferedImageTexture = (BufferedImageTexture)texture;
						TextureFactory.updateBufferedImageTexture( bufferedImageTexture, bufferedImage );
					} else {
						//todo
					}
				} else {
					//todo
				}
			} else {
				//todo
			}
		}
	};

	private TextureFactory() {
	}

	private static void updateBufferedImageTexture( BufferedImageTexture bufferedImageTexture, java.awt.image.BufferedImage bufferedImage ) {
		bufferedImageTexture.setBufferedImage( bufferedImage );

		//todo: handle java.awt.image.BufferedImage.BITMASK? 
		boolean isPotenentiallyAlphaBlended = bufferedImage.getTransparency() == java.awt.image.BufferedImage.TRANSLUCENT;
		bufferedImageTexture.setPotentiallyAlphaBlended( isPotenentiallyAlphaBlended );
	}

	public static BufferedImageTexture getTexture( org.lgna.common.resources.ImageResource imageResource, boolean isMipMappingDesired ) {
		assert imageResource != null;
		BufferedImageTexture rv = TextureFactory.resourceToTextureMap.get( imageResource );
		if( rv != null ) {
			//pass
		} else {
			java.awt.image.BufferedImage bufferedImage = edu.cmu.cs.dennisc.image.ImageFactory.getBufferedImage( imageResource );
			if( bufferedImage != null ) {
				BufferedImageTexture bufferedImageTexture = new BufferedImageTexture();
				bufferedImageTexture.setMipMappingDesired( isMipMappingDesired );
				TextureFactory.updateBufferedImageTexture( bufferedImageTexture, bufferedImage );
				rv = bufferedImageTexture;

				//todo: address order dependency w/ ImageFactory 
				imageResource.addContentListener( TextureFactory.resourceContentListener );

				TextureFactory.resourceToTextureMap.put( imageResource, rv );
			} else {
				//todo: warning texture
				rv = null;
			}
		}
		return rv;
	}
}
