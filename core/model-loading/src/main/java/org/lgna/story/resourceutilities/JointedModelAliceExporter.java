/*******************************************************************************
 * Copyright (c) 2021 Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.resourceutilities;

import edu.cmu.cs.dennisc.java.util.zip.DataSource;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import org.alice.tweedle.file.AliceTextureReference;
import org.alice.tweedle.file.ModelManifest;
import org.lgna.project.io.JointedModelExporter;
import org.lgna.story.implementation.alice.AliceResourceUtilities;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import static org.lgna.story.implementation.alice.AliceResourceUtilities.MODEL_RESOURCE_EXTENSION;
import static org.lgna.story.implementation.alice.AliceResourceUtilities.TEXTURE_RESOURCE_EXTENSION;

public class JointedModelAliceExporter implements JointedModelExporter {
  private final SkeletonVisual visual;
  private final ModelManifest.ModelVariant modelVariant;
  private final String resourcePath;
  private final TexturedAppearance[] texturedAppearancesToSave;

  public JointedModelAliceExporter(SkeletonVisual sv, ModelManifest.ModelVariant variant, String resourcePath) {
    this.visual = sv;
    this.modelVariant = variant;
    this.resourcePath = resourcePath;
    texturedAppearancesToSave = visual.textures.getValue();
    // Null out the appearance since we save the textures separately
    visual.textures.setValue(new TexturedAppearance[0]);
  }

  @Override
  public DataSource createStructureDataSource() {
    String fileName = resourcePath + "/" + AliceResourceUtilities.getVisualResourceFileNameFromModelName(modelVariant.structure);
    return new DataSource() {
      @Override
      public String getName() {
        return fileName;
      }

      @Override
      public void write(OutputStream os) throws IOException {
        AliceResourceUtilities.encodeVisual(visual, os);
      }
    };
  }

  @Override
  public String getStructureFileName(DataSource structureDataSource) {
    return new File(structureDataSource.getName()).getName();
  }

  @Override
  public String getStructureExtension() {
    return MODEL_RESOURCE_EXTENSION;
  }

  @Override
  public void addImageDataSources(List<DataSource> dataSources, ModelManifest modelManifest, Map<Integer, String> resourceMap) {
    String textureName = AliceResourceUtilities.getTextureResourceFileName(modelVariant.structure, modelVariant.textureSet);
    DataSource textureDataSource = createAliceTextureDataSource(resourcePath + "/" + textureName, texturedAppearancesToSave);

    if (!dataSources.contains(textureDataSource)) {
      dataSources.add(textureDataSource);
      AliceTextureReference aliceTextureReference = new AliceTextureReference();
      aliceTextureReference.name = modelVariant.textureSet + "-textureReference";
      aliceTextureReference.file = new File(textureDataSource.getName()).getName();
      aliceTextureReference.format = TEXTURE_RESOURCE_EXTENSION;
      modelManifest.resources.add(aliceTextureReference);

      resourceMap.put(0, aliceTextureReference.name);
    }
  }


  private static DataSource createAliceTextureDataSource(final String fileName, final TexturedAppearance[] textures) {
    return new DataSource() {
      @Override
      public String getName() {
        return fileName;
      }

      @Override
      public void write(OutputStream os) throws IOException {
        AliceResourceUtilities.encodeTexture(textures, os);
      }
    };
  }
}
