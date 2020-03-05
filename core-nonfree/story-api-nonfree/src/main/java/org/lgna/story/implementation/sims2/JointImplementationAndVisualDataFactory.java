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

package org.lgna.story.implementation.sims2;

import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.nebulous.Model;
import edu.cmu.cs.dennisc.nebulous.NebulousJoint;
import edu.cmu.cs.dennisc.nebulous.Thing;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointArrayId;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.sims2.PersonResource;

import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class JointImplementationAndVisualDataFactory<R extends JointedModelResource> implements JointedModelImp.JointImplementationAndVisualDataFactory {
  private static final Map<JointedModelResource, JointImplementationAndVisualDataFactory> map = Maps.newHashMap();

  public static <R extends JointedModelResource> JointImplementationAndVisualDataFactory<R> getInstance(R resource) {
    synchronized (map) {
      JointImplementationAndVisualDataFactory<R> rv = map.get(resource);
      if (rv == null) {
        rv = new JointImplementationAndVisualDataFactory<>(resource);
        map.put(resource, rv);
      }
      return rv;
    }
  }

  private final JointedModelResource resource;

  private JointImplementationAndVisualDataFactory(JointedModelResource resource) {
    this.resource = resource;
  }

  @Override
  public JointedModelResource getResource() {
    return this.resource;
  }

  @Override
  public JointImp createJointImplementation(JointedModelImp jointedModelImplementation, JointId jointId) {
    assert jointedModelImplementation.getVisualData() instanceof NebulousVisualData;
    Model nebModel = ((NebulousVisualData<Model>) jointedModelImplementation.getVisualData()).getNebModel();
    return new JointImplementation(jointedModelImplementation, new NebulousJoint(nebModel, jointId));
  }

  @Override
  public boolean hasJointImplementation(JointedModelImp jointedModelImplementation, JointId jointId) {
    assert jointedModelImplementation.getVisualData() instanceof NebulousVisualData;
    Model nebModel = ((NebulousVisualData<Model>) jointedModelImplementation.getVisualData()).getNebModel();
    return nebModel.hasJoint(jointId);
  }

  //Not supported on nebulous models
  @Override
  public JointId[] getJointArrayIds(JointedModelImp jointedModelImplementation, JointArrayId jointArrayId) {
    return new JointId[0];
  }

  @Override
  public JointedModelImp.VisualData createVisualData() {
    try {
      if (this.resource instanceof PersonResource) {
        PersonResource personResource = (PersonResource) this.resource;
        return NebulousPersonVisualData.createInstance(personResource);
      } else {
        return new NebulousVisualData<Model>(new Thing(this.resource, this.resource));
      }
    } catch (LicenseRejectedException lre) {
      throw new RuntimeException(lre);
    }
  }

  @Override
  public UnitQuaternion getOriginalJointOrientation(JointId jointId) {
    return this.getOriginalJointTransformation(jointId).orientation.createUnitQuaternion();
  }

  @Override
  public AffineMatrix4x4 getOriginalJointTransformation(JointId jointId) {
    Logger.severe("getOriginalJointTransformation not supported from nebulous factory");
    return AffineMatrix4x4.createIdentity();
  }

  @Override
  public boolean isSims() {
    return true;
  }

}
