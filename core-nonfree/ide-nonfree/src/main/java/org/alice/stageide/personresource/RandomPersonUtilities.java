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

package org.alice.stageide.personresource;

import org.lgna.common.RandomUtilities;
import org.lgna.story.Color;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseFace;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.EyeColor;
import org.lgna.story.resources.sims2.Face;
import org.lgna.story.resources.sims2.FullBodyOutfitManager;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.Hair;
import org.lgna.story.resources.sims2.HairManager;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.Outfit;
import org.lgna.story.resources.sims2.PersonResource;

/**
 * @author Dennis Cosgrove
 */
public class RandomPersonUtilities {
  private static final String ELDER_HAIR_COLOR = "GREY";

  private RandomPersonUtilities() {
    throw new AssertionError();
  }

  public static Hair getRandomHair(LifeStage lifeStage, Gender gender) {
    while (true) {
      Class<? extends Hair> hairCls = HairManager.getSingleton().getRandomClass(lifeStage, gender);
      if (hairCls.isEnum()) {
        Hair[] hairs = hairCls.getEnumConstants();
        if (lifeStage == LifeStage.ELDER) {
          for (Hair hair : hairs) {
            Enum<? extends Hair> hairEnum = (Enum<? extends Hair>) hair;
            if (ELDER_HAIR_COLOR.equals(hairEnum.name())) {
              return hair;
            }
          }
        } else {
          Enum hairEnum = edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant((Class) hairCls);
          if (ELDER_HAIR_COLOR.equals(hairEnum.name())) {
            //pass
          } else {
            return (Hair) hairEnum;
          }
        }
      }
    }
  }

  public static PersonResource createRandomResource(LifeStage lifeStage) {
    if (lifeStage == null) {
      LifeStage[] potentialLifeStages = {LifeStage.ELDER, LifeStage.ADULT, LifeStage.TEEN, LifeStage.CHILD, LifeStage.TODDLER};
      lifeStage = RandomUtilities.getRandomValueFrom(potentialLifeStages);
    }
    Gender gender = Gender.getRandom();
    BaseSkinTone skinTone = BaseSkinTone.getRandom();
    Color skinColor = Color.createInstance(skinTone.getColor());
    EyeColor eyeColor = BaseEyeColor.getRandom();
    Outfit outfit = FullBodyOutfitManager.getSingleton().getRandomEnumConstant(lifeStage, gender);
    Hair hair = getRandomHair(lifeStage, gender);
    Face face = BaseFace.getRandom();
    double obesityLevel = RandomUtilities.nextDouble();
    return lifeStage.createResource(gender, skinColor, eyeColor, hair, obesityLevel, outfit, face);
  }
}
