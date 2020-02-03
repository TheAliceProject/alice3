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

import edu.cmu.cs.dennisc.nebulous.Model;
import edu.cmu.cs.dennisc.nebulous.Person;
import edu.cmu.cs.dennisc.nebulous.Thing;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.story.implementation.JointedModelImp;

/**
 * @author Dennis Cosgrove
 */
public class NebulousVisualData<M extends Model> implements JointedModelImp.VisualData {
  private final M nebModel;
  private final Visual[] sgVisuals = new Visual[] {new Visual()};
  private final SimpleAppearance[] sgAppearances = new SimpleAppearance[] {new SimpleAppearance()};

  public NebulousVisualData(M nebModel) {
    this.nebModel = nebModel;
    this.nebModel.setVisual(sgVisuals[0]);
    this.getSgVisuals()[0].geometries.setValue(new Geometry[] {this.nebModel});
    this.getSgVisuals()[0].frontFacingAppearance.setValue(sgAppearances[0]);
  }

  @Override
  public SimpleAppearance[] getSgAppearances() {
    return this.sgAppearances;
  }

  @Override
  public Visual[] getSgVisuals() {
    return this.sgVisuals;
  }

  public M getNebModel() {
    return this.nebModel;
  }

  @Override
  public double getBoundingSphereRadius() {
    return 1.0;
  }

  @Override
  public void setSGParent(Composite parent) {
    nebModel.setSGParent(parent);
    for (Visual sgVisual : this.getSgVisuals()) {
      sgVisual.setParent(parent);
    }
  }

  @Override
  public Composite getSGParent() {
    return nebModel.getSGParent();
  }

  public void unload() {
    if (this.nebModel instanceof Person) {
      ((Person) this.nebModel).synchronizedUnload();
    } else if (this.nebModel instanceof Thing) {
      ((Thing) this.nebModel).synchronizedUnload();
    }
  }

}
