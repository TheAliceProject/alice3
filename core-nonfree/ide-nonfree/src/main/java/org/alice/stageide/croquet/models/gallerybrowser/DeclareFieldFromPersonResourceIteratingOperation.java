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

package org.alice.stageide.croquet.models.gallerybrowser;

import org.alice.stageide.ast.declaration.AddPersonResourceManagedFieldComposite;
import org.alice.stageide.personresource.PersonResourceComposite;
import org.lgna.croquet.Application;
import org.lgna.croquet.SingleThreadIteratingOperation;
import org.lgna.croquet.Triggerable;
import org.lgna.croquet.history.UserActivity;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.story.resources.sims2.LifeStage;

import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class DeclareFieldFromPersonResourceIteratingOperation extends SingleThreadIteratingOperation {
  private static class SingletonHolder {
    private static DeclareFieldFromPersonResourceIteratingOperation adultInstance = new DeclareFieldFromPersonResourceIteratingOperation(LifeStage.ADULT);
    private static DeclareFieldFromPersonResourceIteratingOperation childInstance = new DeclareFieldFromPersonResourceIteratingOperation(LifeStage.CHILD);
    private static DeclareFieldFromPersonResourceIteratingOperation toddlerInstance = new DeclareFieldFromPersonResourceIteratingOperation(LifeStage.TODDLER);
    private static DeclareFieldFromPersonResourceIteratingOperation teenInstance = new DeclareFieldFromPersonResourceIteratingOperation(LifeStage.TEEN);
    private static DeclareFieldFromPersonResourceIteratingOperation elderInstance = new DeclareFieldFromPersonResourceIteratingOperation(LifeStage.ELDER);
  }

  public static DeclareFieldFromPersonResourceIteratingOperation getInstanceForLifeStage(LifeStage lifeStage) {
    if (lifeStage == LifeStage.ELDER) {
      return SingletonHolder.elderInstance;
    } else if (lifeStage == LifeStage.ADULT) {
      return SingletonHolder.adultInstance;
    } else if (lifeStage == LifeStage.TEEN) {
      return SingletonHolder.teenInstance;
    } else if (lifeStage == LifeStage.CHILD) {
      return SingletonHolder.childInstance;
    } else {
      return SingletonHolder.toddlerInstance;
    }
  }

  private final LifeStage lifeStage;

  private DeclareFieldFromPersonResourceIteratingOperation(LifeStage lifeStage) {
    super(Application.PROJECT_GROUP, UUID.fromString("0ec73a7c-f272-4ff1-87eb-f5f25e480ace"));
    this.lifeStage = lifeStage;
  }

  public LifeStage getLifeStage() {
    return this.lifeStage;
  }

  @Override
  protected boolean hasNext(List<UserActivity> finishedSteps) {
    return finishedSteps.size() < 2;
  }

  @Override
  protected Triggerable getNext(List<UserActivity> finishedSteps) {
    switch (finishedSteps.size()) {
    case 0:
      return PersonResourceComposite.getInstance().getRandomPersonExpressionValueConverter(this.lifeStage);
    case 1:
      UserActivity prevSubStep = finishedSteps.get(0);
      if (prevSubStep.getProducedValue() != null) {
        InstanceCreation instanceCreation = (InstanceCreation) prevSubStep.getProducedValue();
        AddPersonResourceManagedFieldComposite addPersonResourceManagedFieldComposite = AddPersonResourceManagedFieldComposite.getInstance();
        addPersonResourceManagedFieldComposite.setInitialPersonResourceInstanceCreation(instanceCreation);
        return addPersonResourceManagedFieldComposite.getLaunchOperation();
      } else {
        return null;
      }
    default:
      return null;
    }
  }
}
