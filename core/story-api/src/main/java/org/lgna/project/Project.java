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
package org.lgna.project;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Sets;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.pattern.IsInstanceCrawler;
import org.alice.tweedle.file.Manifest;
import org.alice.tweedle.file.ProjectManifest;
import org.lgna.common.Resource;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.CrawlPolicy;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.ResourceExpression;
import org.lgna.project.event.ResourceEvent;
import org.lgna.project.event.ResourceListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class Project {

  private final NamedUserType programType;
  private final SceneCameraType sceneCameraType;
  private final Set<Resource> resources = Sets.newCopyOnWriteArraySet();
  private final Set<NamedUserType> namedUserTypes = Sets.newCopyOnWriteArraySet();

  private final List<ResourceListener> resourceListeners = Lists.newCopyOnWriteArrayList();

  private final Object lock = new Object();

  public Project(ProjectManifest manifest, NamedUserType programType, Set<NamedUserType> namedUserTypes, Set<Resource> resources) {
    this.programType = programType;
    this.namedUserTypes.addAll(namedUserTypes);
    this.resources.addAll(resources);
    this.sceneCameraType = manifest == null ? SceneCameraType.WindowCamera : manifest.projectStructure.sceneCameraType;
  }

  public Project(NamedUserType programType, SceneCameraType sceneCameraType) {
    this.programType = programType;
    this.sceneCameraType = sceneCameraType;
  }

  public enum SceneCameraType {
    WindowCamera, VRHeadset
  }

  public Object getLock() {
    return this.lock;
  }

  public NamedUserType getProgramType() {
    return this.programType;
  }

  public ProjectManifest createSaveManifest() {
    final ProjectManifest manifest = createProjectManifest();
    manifest.metadata.fileType = "a3p";
    return manifest;
  }

  public ProjectManifest createExportManifest() {
    final ProjectManifest manifest = createProjectManifest();
    manifest.metadata.fileType = "a3w";
    manifest.prerequisites.add(standardLibrary());
    return manifest;
  }

  private ProjectManifest createProjectManifest() {
    final ProjectManifest manifest = new ProjectManifest();
    manifest.description.name = getProgramType().getName(); // probably "Program"
    manifest.provenance.aliceVersion = ProjectVersion.getCurrentVersion().toString();
    manifest.metadata.identifier.name = getProgramType().getId().toString();
    manifest.metadata.identifier.type = Manifest.ProjectType.World;
    manifest.projectStructure.sceneCameraType = sceneCameraType;
    return manifest;
  }

  private Manifest.ProjectIdentifier standardLibrary() {
    final Manifest.ProjectIdentifier libraryIdentifier = new Manifest.ProjectIdentifier();
    libraryIdentifier.type = Manifest.ProjectType.Library;
    libraryIdentifier.version = "0.16";
    libraryIdentifier.name = "SceneGraphLibrary";
    return libraryIdentifier;
  }

  public void addResourceListener(ResourceListener resourceListener) {
    this.resourceListeners.add(resourceListener);
  }

  public void removeResourceListener(ResourceListener resourceListener) {
    this.resourceListeners.remove(resourceListener);
  }

  public void addResource(Resource resource) {
    if (!this.resources.contains(resource)) {
      this.resources.add(resource);
      if (this.resourceListeners.size() > 0) {
        ResourceEvent e = new ResourceEvent(this, resource);
        for (ResourceListener resourceListener : this.resourceListeners) {
          resourceListener.resourceAdded(e);
        }
      }
    }
  }

  public void removeResource(Resource resource) {
    this.resources.remove(resource);
    if (this.resourceListeners.size() > 0) {
      ResourceEvent e = new ResourceEvent(this, resource);
      for (ResourceListener resourceListener : this.resourceListeners) {
        resourceListener.resourceRemoved(e);
      }
    }
  }

  public Set<Resource> getResources() {
    return this.resources;
  }

  public Set<Resource> getReferencedResources() {
    AbstractType<?, ?, ?> programType = getProgramType();
    IsInstanceCrawler<ResourceExpression> crawler = new IsInstanceCrawler<>(ResourceExpression.class) {
      @Override
      protected boolean isAcceptable(ResourceExpression resourceExpression) {
        return true;
      }
    };
    programType.crawl(crawler, CrawlPolicy.COMPLETE);

    Set<Resource> rv = new HashSet<>();
    for (ResourceExpression resourceExpression : crawler.getList()) {
      Resource resource = resourceExpression.resource.getValue();
      if (!resources.contains(resource)) {
        Logger.warning("adding missing resource", resource);
        resources.add(resource);
      }
      rv.add(resource);
    }
    return rv;
  }

  public Set<NamedUserType> getNamedUserTypes() {
    synchronized (this.getLock()) {
      this.namedUserTypes.addAll(AstUtilities.getNamedUserTypes(this.programType));
      return this.namedUserTypes;
    }
  }
}
