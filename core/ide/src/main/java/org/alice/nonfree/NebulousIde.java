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
package org.alice.nonfree;

import java.util.List;
import java.util.Map;

import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.instancefactory.InstanceFactory;
import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.alice.stageide.StoryApiConfigurationManager;
import org.alice.stageide.ast.ExpressionCreator;
import org.alice.stageide.cascade.ExpressionCascadeManager;
import org.alice.stageide.gallerybrowser.uri.ResourceKeyUriIteratingOperation;
import org.alice.stageide.modelresource.InstanceCreatorKey;
import org.alice.stageide.modelresource.ResourceKey;
import org.alice.stageide.modelresource.ResourceNode;
import org.alice.stageide.openprojectpane.models.TemplateUriState;
import org.lgna.croquet.CascadeBlankChild;
import org.lgna.croquet.Operation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.icon.IconFactory;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaMethod;
import org.lgna.project.ast.NamedUserType;
import org.lgna.story.Paint;
import org.lgna.story.implementation.EntityImp;
import org.lgna.story.resources.ModelResource;

import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

/**
 * @author Kyle J. Harms
 */
public class NebulousIde {

  public static final NebulousIde nonfree;

  private static final String NONFREE_IDE_CLASSNAME = "org.alice.nonfree.IdeNonfree";

  static {
    NebulousIde nonfreeInstance = null;
    try {
      Class<?> nonfreeClass = ClassUtilities.forName(NONFREE_IDE_CLASSNAME);
      nonfreeInstance = (NebulousIde) ReflectionUtilities.newInstance(nonfreeClass);
    } catch (Throwable t) {
      Logger.warning("unable to initialize ide-nonfree library", t);
      nonfreeInstance = new NebulousIde();
    }
    nonfree = nonfreeInstance;
  }

  protected NebulousIde() {
  }

  public boolean isNonFreeEnabled() {
    return false;
  }

  public void promptForLicenseAgreements(String licenseKey) throws LicenseRejectedException {
  }

  public Operation newSimsArtEulaDialogLaunchOperation() {
    return null;
  }

  public NamedUserType createProgramType(TemplateUriState.Template template, boolean makeVrReady) {
    return null;
  }

  public AbstractPropertyAdapter<?, ?> getPropertyAdapterForGetter(JavaMethod setter, StandardExpressionState state, EntityImp entityImp) {
    return null;
  }

  public void addBipedResourceResourceNodes(List<ResourceNode> childNodes, List<ResourceNode> emptyList) {
  }

  public void unloadNebulousModelData() {
  }

  public void unloadPerson() {
  }

  public IconFactory createIconFactory(ModelResource instance) {
    return null;
  }

  public double setOneShotSortValues(Map<JavaMethod, Double> map, double value, final double INCREMENT) {
    return value;
  }

  public boolean isInstanceOfPersonResourceKey(ResourceKey resourceKey) {
    return false;
  }

  public Triggerable getPersonResourceDropOperation(ResourceKey resourceKey) {
    return null;
  }

  public void addRoomMethods(AbstractType<?, ?, ?> instanceFactoryValueType, List<JavaMethod> methods) {
  }

  public CascadeBlankChild<?> getRoomFillIns(JavaMethod method, InstanceFactory instanceFactory) {
    return null;
  }

  public ResourceKeyUriIteratingOperation getPersonResourceKeyUriIteratingOperation() {
    return null;
  }

  public ExpressionCascadeManager newExpressionCascadeManager() {
    return new ExpressionCascadeManager();
  }

  public StoryApiConfigurationManager newStoryApiConfigurationManager() {
    return new StoryApiConfigurationManager();
  }

  public boolean isAssignableToPersonResource(AbstractType<?, ?, ?> type) {
    return false;
  }

  public Paint getFloorApperanceRedwood() {
    return null;
  }

  public Paint getWallApperanceYellow() {
    return null;
  }

  public CascadeBlankChild<?> getGalleryPersonResourceFillInInstance(AbstractType<?, ?, ?> type) {
    return null;
  }

  public boolean isPersonResourceAssignableFrom(Class<?> cls) {
    return false;
  }

  public InstanceCreatorKey getPersonResourceKeyInstanceForResourceClass(Class<? extends ModelResource> resourceCls) {
    return null;
  }

  public ExpressionCreator newExpressionCreator() {
    return new ExpressionCreator();
  }

  public boolean isPersonResourceTypeAssingleFrom(AbstractType<?, ?, ?> type) {
    return false;
  }
}
