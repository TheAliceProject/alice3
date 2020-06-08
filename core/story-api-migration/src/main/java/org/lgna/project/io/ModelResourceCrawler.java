/*******************************************************************************
 * Copyright (c) 2019 Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.pattern.Crawlable;
import edu.cmu.cs.dennisc.pattern.Crawler;
import org.lgna.project.ast.*;
import org.lgna.story.resources.DynamicResource;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.ModelResource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModelResourceCrawler implements Crawler {
  Map<String, Set<JointedModelResource>> modelResources = new HashMap<>();
  Set<InstanceCreation> personCreations = new HashSet<>();
  Set<DynamicResource> dynamicResources = new HashSet<>();
  Set<NamedUserType> activeUserTypes = new HashSet<>();

  @Override
  public void visit(Crawlable crawlable) {
    if (crawlable == null) {
      return;
    }
    if (FieldAccess.class.isAssignableFrom(crawlable.getClass())) {
      addIfResourceEnum((FieldAccess) crawlable);
    }
    if (InstanceCreation.class.isAssignableFrom(crawlable.getClass())) {
      addNonEnumResourceCreations((InstanceCreation) crawlable);
    }
    if (NamedUserType.class.isAssignableFrom(crawlable.getClass())) {
      activeUserTypes.add((NamedUserType) crawlable);
    }
  }

  private void addIfResourceEnum(FieldAccess fieldAccess) {
    AbstractType<?, ?, ?> type = fieldAccess.getType();
    if (type != null && type.isAssignableTo(JointedModelResource.class)) {
      JavaField field = (JavaField) fieldAccess.field.getValue();
      try {
        JointedModelResource modelResource = (JointedModelResource) field.getFieldReflectionProxy().getReification().get(null);
        final String resourceName = modelResource.getClass().getSimpleName();
        final String modelName = modelNameForResource(resourceName);
        Set<JointedModelResource> resources = modelResources.computeIfAbsent(modelName, k -> new HashSet<>());
        resources.add(modelResource);
      } catch (IllegalAccessException e) {
        e.printStackTrace(); //TODO: Log this
      }
    }
  }

  private String modelNameForResource(String resourceName) {
    return resourceName.endsWith("Resource")
        ? resourceName.substring(0, resourceName.length() - 8)
        : resourceName;
  }

  private void addNonEnumResourceCreations(InstanceCreation resourceCreation) {
    AbstractType<?, ?, ?> resourceType = resourceCreation.constructor.getValue().getDeclaringType();
    final Class<?> resourceClass = resourceType.getFirstEncounteredJavaType().getClassReflectionProxy().getReification();
    if (ModelResource.class.isAssignableFrom(resourceClass) && resourceClass.getSimpleName().contains("PersonResource")) {
      personCreations.add(resourceCreation);
    }
    if (DynamicResource.class.isAssignableFrom(resourceClass)) {
      Object dynamicInstance = resourceCreation.instantiateDynamicResource();
      if (dynamicInstance instanceof DynamicResource) {
        dynamicResources.add((DynamicResource) dynamicInstance);
      }
    }
  }
}
