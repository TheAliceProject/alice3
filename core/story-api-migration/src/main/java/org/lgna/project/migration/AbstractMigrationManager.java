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
package org.lgna.project.migration;

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.project.Version;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.Node;
import org.lgna.story.resources.ModelResource;
import org.lgna.story.resourceutilities.ResourceTypeHelper;

import java.util.*;
import java.util.logging.Level;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractMigrationManager implements MigrationManager {
  private final Version currentVersion;
  private ResourceTypeHelper helper;
  private final Set<NamedUserType> typeCache = new HashSet<>();
  private final List<Runnable> finalizers = new ArrayList<>();

  AbstractMigrationManager(Version currentVersion) {
    this.currentVersion = currentVersion;
  }

  protected abstract TextMigration[] getTextMigrations();

  protected abstract AstMigration[] getAstMigrations();

  Version getCurrentVersion() {
    return this.currentVersion;
  }

  @Override
  public boolean hasTextMigrationsFor(Version version) {
    return Arrays.stream(getTextMigrations()).anyMatch(migration -> migration.isApplicable(version));
  }

  @Override
  public boolean hasAstMigrationsFor(Version version) {
    return Arrays.stream(getAstMigrations()).anyMatch(migration -> migration != null && migration.isApplicable(version));
  }

  @Override
  public String migrate(String source, Version version) {
    String rv = source;
    typeCache.clear();
    for (TextMigration textMigration : this.getTextMigrations()) {
      if (textMigration.isApplicable(version)) {
        if (Logger.getLevel().intValue() < Level.SEVERE.intValue()) {
          Logger.outln(version, textMigration);
        }
        rv = textMigration.migrate(rv);
        version = textMigration.getResultVersion();
      }
    }
    return rv;
  }

  @Override
  public void migrate(Node root, ResourceTypeHelper helper, Version version) {
    if (!hasAstMigrationsFor(version)) {
      return;
    }
    this.helper = helper;
    for (AstMigration astMigration : this.getAstMigrations()) {
      if (astMigration != null && astMigration.isApplicable(version)) {
        if (Logger.getLevel().intValue() < Level.SEVERE.intValue()) {
          Logger.outln(version, astMigration);
        }
        astMigration.migrate(root, this);
        version = astMigration.getResultVersion();
      }
    }
    applyFinalizers();
  }

  private void applyFinalizers() {
    for (Runnable finalizer : finalizers) {
      finalizer.run();
    }
  }

  @Override
  public void cacheType(NamedUserType type) {
    typeCache.add(type);
  }

  @Override
  public AbstractType<?, ?, ?> getCachedType(String className) {
    for (NamedUserType existingType : typeCache) {
      if (className.equals(existingType.getName())) {
        return existingType;
      }
    }
    return null;
  }

  @Override
  public InstanceCreation createInstanceCreation(ModelResource resourceClass) {
    return helper.createInstanceCreation(resourceClass, typeCache);
  }

  @Override
  public void addFinalization(Runnable finalizer) {
    finalizers.add(finalizer);
  }
}
