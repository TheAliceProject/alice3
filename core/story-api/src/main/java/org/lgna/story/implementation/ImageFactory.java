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
package org.lgna.story.implementation;

import org.lgna.common.event.ResourceContentEvent;
import org.lgna.common.event.ResourceContentListener;
import org.lgna.common.resources.ImageResource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public final class ImageFactory {
	private static Map<ImageResource, BufferedImage> resourceToBufferedImageMap = new HashMap<ImageResource, BufferedImage>();

	private static ResourceContentListener resourceContentListener = new ResourceContentListener() {
		@Override
		public void contentChanging( ResourceContentEvent e ) {
		}

		@Override
		public void contentChanged( ResourceContentEvent e ) {
			ImageFactory.forget( (ImageResource)e.getTypedSource() );
		}
	};

	private ImageFactory() {
	}

	public static void forget( ImageResource imageResource ) {
		ImageFactory.resourceToBufferedImageMap.remove( imageResource );
		imageResource.removeContentListener( ImageFactory.resourceContentListener );
	}

	public static BufferedImage getBufferedImage( ImageResource imageResource ) {
		assert imageResource != null;
		BufferedImage rv = ImageFactory.resourceToBufferedImageMap.get( imageResource );
		if( rv != null ) {
			//pass
		} else {
			try {
				rv = ImageIO.read( new ByteArrayInputStream( imageResource.getData() ) );
				//				if( imageResource.getWidth() < 0 || imageResource.getHeight() < 0 ) {
				imageResource.setWidth( rv.getWidth() );
				imageResource.setHeight( rv.getHeight() );
				//				}

				imageResource.addContentListener( ImageFactory.resourceContentListener );
				ImageFactory.resourceToBufferedImageMap.put( imageResource, rv );
			} catch( IOException ioe ) {
				//todo: return warning texture
			}
		}
		return rv;
	}

	public static ImageResource createImageResource( File file ) throws IOException {
		String contentType = ImageResource.getContentType( file );
		if( contentType != null ) {
			ImageResource rv = new ImageResource( file, contentType );

			//update width and height details
			getBufferedImage( rv );

			return rv;
		} else {
			throw new RuntimeException( "content type not found for " + file );
		}
	}

}
