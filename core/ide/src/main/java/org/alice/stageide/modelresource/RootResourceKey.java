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
package org.alice.stageide.modelresource;

import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.stageide.gallerybrowser.GalleryComposite;
import org.lgna.croquet.DropSite;
import org.lgna.croquet.SingleSelectTreeState;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.DragStep;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.NamedUserType;

import javax.swing.JComponent;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Dennis Cosgrove
 */
public class RootResourceKey extends ResourceKey {
  private final String keyText;
  private final String defaultDisplayText;

  RootResourceKey(String keyText, String defaultDisplayText) {
    this.keyText = keyText;
    this.defaultDisplayText = defaultDisplayText;
  }

  @Override
  public String getSearchText() {
    return null;
  }

  @Override
  public String getInternalText() {
    return this.defaultDisplayText;
  }

  @Override
  public String getLocalizedDisplayText() {
    Class cls = GalleryComposite.class;
    String bundleName = cls.getPackage().getName() + ".croquet";
    try {
      ResourceBundle resourceBundle = ResourceBundleUtilities.getUtf8Bundle(bundleName, JComponent.getDefaultLocale());
      return resourceBundle.getString(this.keyText);
    } catch (MissingResourceException mre) {
      Logger.severe(cls, this.keyText);
      return this.defaultDisplayText;
    }
  }

  @Override
  public IconFactory getIconFactory() {
    return null;
  }

  @Override
  public InstanceCreation createInstanceCreation(Set<NamedUserType> typeCache) {
    throw new Error();
  }

  @Override
  public String[] getTags() {
    return null;
  }

  @Override
  public String[] getGroupTags() {
    return null;
  }

  @Override
  public String[] getThemeTags() {
    return null;
  }

  @Override
  public boolean isLeaf() {
    return false;
  }

  @Override
  public boolean isInstanceCreator() {
    return false;
  }

  @Override
  public Triggerable getLeftClickOperation(ResourceNode node, SingleSelectTreeState<ResourceNode> controller) {
    return null;
  }

  @Override
  public Triggerable getDropOperation(ResourceNode node, DragStep step, DropSite dropSite) {
    return null;
  }

  @Override
  protected void appendRep(StringBuilder sb) {
  }
}
