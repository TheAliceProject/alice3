/*******************************************************************************
 * Copyright (c) 2020 Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.io;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import org.alice.tweedle.file.ModelManifest;
import org.alice.tweedle.file.StructureReference;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.virtualmachine.InstanceCreatingVirtualMachine;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resourceutilities.ModelResourceInfo;
import org.lgna.story.resourceutilities.UtilitySkeletonVisualAdapter;

import java.util.HashSet;
import java.util.Set;

public class JsonPersonIo extends JsonModelIo {

  protected JsonPersonIo(Set<InstanceCreation> resourceCreations, ExportFormat exportFormat) {
    super(exportFormat);
    initializeFromPersonResources(resourceCreations);
  }

  private void initializeFromPersonResources(Set<InstanceCreation> resourceCreations) {
    InstanceCreatingVirtualMachine vm = new InstanceCreatingVirtualMachine();
    modelResources = new HashSet<>();
    for (InstanceCreation creation : resourceCreations) {
      final Object instance = vm.createInstance(creation);
      if (instance instanceof JointedModelResource) {
        modelResources.add((JointedModelResource) instance);
      }
    }
    ModelResourceInfo modelInfo = personModelInfo("PersonResource", "");
    for (JointedModelResource resource: modelResources) {
      modelInfo.addSubResource(personModelInfo(resource.toString(), resource.toString()));
    }
    modelManifest = modelInfo.createModelManifest();
    modelManifest.parentClass = "BipedResource";
  }

  private ModelResourceInfo personModelInfo(String resourceName, String textureName) {
    return new ModelResourceInfo(
        null, resourceName, "EA", 2004, null,
        new String[0], new String[0], new String[0],
        "Person", textureName, false, true);
  }

  @Override
  protected SkeletonVisual getSkeletonVisual(JointedModelImp.VisualData<JointedModelResource> v, JointedModelResource modelResource) {
    SkeletonVisual sv = super.getSkeletonVisual(v, modelResource);
    UtilitySkeletonVisualAdapter skeletonVisualAdapter = new UtilitySkeletonVisualAdapter();
    skeletonVisualAdapter.initialize(sv);
    skeletonVisualAdapter.processWeightedMesh();
    return sv;
  }

  @Override
  protected void finishStructureReference(StructureReference structureReference, SkeletonVisual sv) {
    if (structureReference.boundingBox != null) {
      return;
    }
    structureReference.boundingBox = new ModelManifest.BoundingBox();
    AxisAlignedBox svBounds = sv.getAxisAlignedMinimumBoundingBox();
    structureReference.boundingBox.max = svBounds.getMaximum().getAsFloatList();
    structureReference.boundingBox.min = svBounds.getMinimum().getAsFloatList();
  }
}
