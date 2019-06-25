/*******************************************************************************
 * Copyright (c) 2018 Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.projecturi;

import edu.cmu.cs.dennisc.javax.swing.option.Dialogs;
import org.alice.ide.ProjectApplication;
import org.alice.ide.icons.Icons;
import org.alice.stageide.StageIDE;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.io.ModelResourceCrawler;
import org.lgna.story.resources.JointedModelResource;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ExportProjectOperation extends AbstractSaveProjectOperation {
  public ExportProjectOperation() {
    super(UUID.fromString("44ffba8a-ff13-4cb5-9736-55cd93c48e9d"));
  }
  @Override
  protected String getExtension() {
    return IoUtilities.EXPORT_EXTENSION;
  }

  @Override
  protected boolean isPromptNecessary(File file) {
    return true;
  }

  @Override
  protected void localize() {
    super.localize();
    this.setButtonIcon(Icons.SAVE_DOCUMENT_SMALL);
  }

  @Override
  public boolean isToolBarTextClobbered() {
    return true;
  }

  @Override
  protected void perform(UserActivity activity) {
    Set<String> problemModels = getProblemModels();
    if (problemModels.isEmpty() || userWantsToGoAhead(problemModels)) {
      super.perform(activity);
    }
  }

  private boolean userWantsToGoAhead(Set<String> problemModels) {
    StringBuilder sb = new StringBuilder();
    sb.append("The following model types will not export cleanly and may cause errors. Do you wish to continue with the export?");
    for (String s : problemModels) {
      sb.append("\n    ").append(s);
    }
    return Dialogs.confirmWithWarning("Problem models", sb.toString());
  }

  private Set<String> getProblemModels() {
    StageIDE application = StageIDE.getActiveInstance();
    ModelResourceCrawler modelResourceFieldAccessCrawler = new ModelResourceCrawler();
    application.getProgramType().crawl(modelResourceFieldAccessCrawler, CrawlPolicy.COMPLETE);
    Map<String, Set<JointedModelResource>> modelResources = modelResourceFieldAccessCrawler.modelResources;

    Set<String> problemModels = new HashSet<>();
    for (Map.Entry<String, Set<JointedModelResource>> entry : modelResources.entrySet()) {
      final Set<JointedModelResource> resources = entry.getValue();
      if (resources.isEmpty() || resources.iterator().next().getImplementationAndVisualFactory().isSims()) {
        problemModels.add(entry.getKey());
      }
    }
    return problemModels;
  }

  @Override
  protected void save(ProjectApplication application, File file) throws IOException {
    application.exportProjectTo(file);
  }
}
