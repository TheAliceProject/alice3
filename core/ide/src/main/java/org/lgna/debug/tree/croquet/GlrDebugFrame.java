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
package org.lgna.debug.tree.croquet;

import edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComposite;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrLeaf;
import edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene;
import edu.cmu.cs.dennisc.scenegraph.Scene;
import org.alice.stageide.sceneeditor.StorytellingSceneEditor;
import org.lgna.debug.tree.core.ZTreeNode;
import org.lgna.project.virtualmachine.UserInstance;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SScene;
import org.lgna.story.implementation.SceneImp;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class GlrDebugFrame extends DebugFrame<GlrComponent<?>> {
  public static ZTreeNode.Builder<GlrComponent<?>> createBuilder(GlrComponent<?> glrComponent) {
    ZTreeNode.Builder<GlrComponent<?>> rv = new ZTreeNode.Builder<GlrComponent<?>>(glrComponent, glrComponent instanceof GlrLeaf<?>);
    if (glrComponent instanceof GlrComposite<?>) {
      GlrComposite<?> glrComposite = (GlrComposite<?>) glrComponent;
      for (GlrComponent<?> glrChild : glrComposite.accessChildren()) {
        rv.addChildBuilder(createBuilder(glrChild));
      }
    }
    return rv;
  }

  public GlrDebugFrame() {
    super(UUID.fromString("502e2f7e-cd4a-4ae5-a06d-20890d8e1eb6"));
  }

  @Override
  protected ZTreeNode.Builder<GlrComponent<?>> capture() {
    UserInstance sceneUserInstance = StorytellingSceneEditor.getInstance().getActiveSceneInstance();
    SScene scene = sceneUserInstance.getJavaInstance(SScene.class);
    SceneImp sceneImp = EmployeesOnly.getImplementation(scene);
    Scene sgScene = sceneImp.getSgComposite();
    GlrScene glrScene = AdapterFactory.getAdapterFor(sgScene);
    return createBuilder(glrScene);
  }
}
