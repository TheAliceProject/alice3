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

import edu.cmu.cs.dennisc.eula.LicenseRejectedException;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.nebulous.Person;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.story.SThing;
import org.lgna.story.implementation.SingleVisualModelImp;
import org.lgna.story.resources.sims2.EyeColor;
import org.lgna.story.resources.sims2.Face;
import org.lgna.story.resources.sims2.FullBodyOutfit;
import org.lgna.story.resources.sims2.Gender;
import org.lgna.story.resources.sims2.Hair;
import org.lgna.story.resources.sims2.LifeStage;
import org.lgna.story.resources.sims2.Outfit;
import org.lgna.story.resources.sims2.PersonResource;
import org.lgna.story.resources.sims2.TopAndBottomOutfit;

import java.awt.Color;
import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class PersonImp extends SingleVisualModelImp {
  public PersonImp() {
    super(new Visual());
  }

  @Override
  public SThing getAbstraction() {
    return null;
  }

  private final Map<LifeStage, Person> mapLifeStageToNebPerson = Maps.newHashMap();

  public void unload() {
    for (Map.Entry<LifeStage, Person> entry : this.mapLifeStageToNebPerson.entrySet()) {
      entry.getValue().synchronizedUnload();
    }
    this.mapLifeStageToNebPerson.clear();
    this.setSgGeometry(null);
  }

  private void setSgGeometry(Geometry sgGeometry) {
    Visual sgVisual = this.getSgVisuals()[0];
    sgVisual.setGeometry(sgGeometry);
  }

  void updateNebPerson() {
    IngredientsComposite composite = PersonResourceComposite.getInstance().getIngredientsComposite();
    LifeStage lifeStage = composite.getLifeStageState().getValue();
    if (lifeStage == null) {
      Logger.severe("NO PERSON LIFESTAGE: not attempting to update");
      return;
    }
    Person nebPerson = this.mapLifeStageToNebPerson.get(lifeStage);
    if (nebPerson == null) {
      try {
        nebPerson = new Person(lifeStage);
        this.mapLifeStageToNebPerson.put(lifeStage, nebPerson);
      } catch (LicenseRejectedException lre) {
        //todo
        throw new RuntimeException(lre);
      }
    }
    PersonResource personResource = composite.createResourceFromStates();
    if (personResource == null) {
      Logger.severe("NOT SETTING ATTRIBUTES ON PERSON: null resource.");
      return;
    }
    Gender gender = personResource.getGender();
    Color awtSkinColor = new Color(personResource.getSkinColor().getRed().floatValue(), personResource.getSkinColor().getGreen().floatValue(), personResource.getSkinColor().getBlue().floatValue());
    EyeColor eyeColor = personResource.getEyeColor();
    double obesityLevel = personResource.getObesityLevel();
    Hair hair = personResource.getHair();
    Outfit outfit = personResource.getOutfit();
    Face face = personResource.getFace();

    if (gender == null || outfit == null || eyeColor == null || hair == null || face == null) {
      Logger.severe("NOT SETTING ATTRIBUTES ON PERSON: gender=" + gender + ", outfit=" + outfit + ", skinColor" + awtSkinColor + ", eyeColor=" + eyeColor + ", obesityLevel=" + obesityLevel + ", hair=" + hair + ", face=" + face);
      return;
    } else {
      if (lifeStage.getGenderedHairInterfaceClass(gender).isAssignableFrom(hair.getClass())) {
        if ((outfit instanceof FullBodyOutfit && lifeStage.getGenderedFullBodyOutfitInterfaceClass(gender).isAssignableFrom(outfit.getClass()))
            || outfit instanceof TopAndBottomOutfit<?, ?>
              && lifeStage.getGenderedTopPieceInterfaceClass(gender).isAssignableFrom(((TopAndBottomOutfit<?, ?>) outfit).getTopPiece().getClass())
              && lifeStage.getGenderedBottomPieceInterfaceClass(gender).isAssignableFrom(((TopAndBottomOutfit<?, ?>) outfit).getBottomPiece().getClass())) {
          nebPerson.synchronizedSetAll(gender, outfit, awtSkinColor.getRGB(), obesityLevel, eyeColor, hair, face);
        } else {
          Logger.severe(outfit, lifeStage, gender);
        }
      } else {
        Logger.severe(hair, lifeStage, gender);
      }
    }
    setSgGeometry(nebPerson);
  }

  @Override
  public void setSize(Dimension3 size) {
    this.setScale(getScaleForSize(size));
  }
}
