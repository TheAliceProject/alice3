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
package org.alice.stageide.icons;

import org.lgna.croquet.icon.EmptyIconFactory;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.croquet.icon.ImageIconFactory;
import org.lgna.croquet.icon.TrimmedImageIconFactory;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resources.sims2.PersonResource;
import org.lgna.story.resourceutilities.SimsThumbnailMaker;

import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class SimsIconFactoryManager {

  public static IconFactory createIconFactory(ModelResource instance) {
    if (instance instanceof PersonResource) {
      PersonResource personResource = (PersonResource) instance;
      try {
        SimsThumbnailMaker thumbnailMaker = SimsThumbnailMaker.getInstance();
        BufferedImage image = thumbnailMaker.createThumbnailFromPersonResource(personResource);
        int width = thumbnailMaker.getWidth();
        int height = thumbnailMaker.getHeight();

        //Used for saving out gallery thumbnails for the sims lifestages
        //          java.io.File outputFile = new java.io.File( "C:/Users/dculyba/Documents/Alice/simThumbs/thumb_" + personResource.getGender().toString() + "_" + personResource.getLifeStage().toString() + "_" + Integer.toString( personResource.hashCode() ) + ".png" );
        //          edu.cmu.cs.dennisc.image.ImageUtilities.write( outputFile, org.lgna.story.resourceutilities.AliceThumbnailMaker.getInstance( 240, 180 ).createGalleryThumbnailFromPersonResource( personResource ) );

        if ((width == image.getWidth()) && (height == image.getHeight())) {
          return new ImageIconFactory(image);
        } else {
          return new TrimmedImageIconFactory(image, width, height);
        }
      } catch (Throwable t) {
        System.err.println("Person thumbnail creation failed so it will be blank.");
        t.printStackTrace();
        return EmptyIconFactory.getInstance();
      }
    } else {
      return null;
    }
  }
}
