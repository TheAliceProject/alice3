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
package org.lgna.story;

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.texture.BufferedImageTexture;
import edu.cmu.cs.dennisc.texture.Texture;
import org.alice.nonfree.NebulousStoryApi;
import org.lgna.common.resources.ImageResource;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.implementation.JointIdTransformationPair;
import org.lgna.story.implementation.ProgramImp;
import org.lgna.story.implementation.TextureFactory;
import org.lgna.story.resources.JointedModelResource;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class EmployeesOnly {
  private EmployeesOnly() {
    throw new AssertionError();
  }

  public static void invokeHandleActiveChanged(SScene scene, boolean isActive, int activationCount) {
    scene.handleActiveChanged(isActive, activationCount);
  }

  // Used by reflection in StageIDE
  public static void invokeSetJointedModelResource(SJointedModel jointedModel, JointedModelResource resource) {
    jointedModel.setJointedModelResource(resource);
  }

  public static <T extends EntityImp> T getImplementation(SThing entity) {
    return (T) entity.getImplementation();
  }

  public static ProgramImp getImplementation(SProgram program) {
    return program.getImplementation();
  }

  public static OrthogonalMatrix3x3 getOrthogonalMatrix3x3(Orientation orientation) {
    return orientation.getInternal();
  }

  public static Position createPosition(Point3 xyz) {
    return Position.createInstance(xyz);
  }

  public static Orientation createOrientation(OrthogonalMatrix3x3 m) {
    return Orientation.createInstance(m);
  }

  public static edu.cmu.cs.dennisc.animation.Style getInternal(AnimationStyle animationStyle) {
    return animationStyle.getInternal();
  }

  private static final Map<ImagePaint, BufferedImageTexture> mapImagePaintToTexture = Maps.newHashMap();

  public static Texture getTexture(Paint paint, Texture defaultValue) {
    if (paint instanceof ImageSource) {
      ImageSource imageSource = (ImageSource) paint;
      ImageResource imageResource = imageSource.getImageResource();
      if (imageResource != null) {
        return TextureFactory.getTexture(imageResource, true);
      } else {
        return null;
      }
    } else if (paint instanceof ImagePaint) {
      ImagePaint imagePaint = (ImagePaint) paint;
      BufferedImageTexture rv = mapImagePaintToTexture.get(imagePaint);
      if (rv == null) {
        rv = new BufferedImageTexture();
        try {
          rv.setBufferedImage(ImageIO.read(imagePaint.getResource()));
        } catch (IOException ioe) {
          throw new RuntimeException(ioe);
        }
        rv.setMipMappingDesired(true);
        mapImagePaintToTexture.put(imagePaint, rv);
      }
      return rv;
    } else if (paint instanceof NonfreeTexturePaint) {
      NonfreeTexturePaint nonfreeTexturePaint = (NonfreeTexturePaint) paint;
      if (nonfreeTexturePaint.isTextureValid()) {
        Texture texture = nonfreeTexturePaint.getTexture();

        NebulousStoryApi.nonfree.setMipMappingDesiredOnNebulousTexture(texture);
        return texture;
      } else {
        //todo?
        return defaultValue;
      }
    } else {
      return defaultValue;
    }
  }

  public static void addJointIdTransformationPair(PoseBuilder<?, ?> poseBuilder, JointIdTransformationPair jointIdQuaternionPair) {
    poseBuilder.addJointIdQuaternionPair(jointIdQuaternionPair);
  }

  public static JointIdTransformationPair[] getJointIdTransformationPairs(Pose<?> pose) {
    return pose.getJointIdTransformationPairs();
  }

}
