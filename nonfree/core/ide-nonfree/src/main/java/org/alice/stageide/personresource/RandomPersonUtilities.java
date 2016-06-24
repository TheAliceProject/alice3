/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package org.alice.stageide.personresource;

/**
 * @author Dennis Cosgrove
 */
public class RandomPersonUtilities {
	private static final String ELDER_HAIR_COLOR = "GREY";

	private RandomPersonUtilities() {
		throw new AssertionError();
	}

	public static org.lgna.story.resources.sims2.Hair getRandomHair( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender ) {
		while( true ) {
			Class<? extends org.lgna.story.resources.sims2.Hair> hairCls = org.lgna.story.resources.sims2.HairManager.getSingleton().getRandomClass( lifeStage, gender );
			if( hairCls.isEnum() ) {
				org.lgna.story.resources.sims2.Hair[] hairs = hairCls.getEnumConstants();
				if( lifeStage == org.lgna.story.resources.sims2.LifeStage.ELDER ) {
					for( org.lgna.story.resources.sims2.Hair hair : hairs ) {
						Enum<? extends org.lgna.story.resources.sims2.Hair> hairEnum = (Enum<? extends org.lgna.story.resources.sims2.Hair>)hair;
						if( ELDER_HAIR_COLOR.equals( hairEnum.name() ) ) {
							return hair;
						}
					}
				} else {
					Enum hairEnum = edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( (Class)hairCls );
					if( ELDER_HAIR_COLOR.equals( hairEnum.name() ) ) {
						//pass
					} else {
						return (org.lgna.story.resources.sims2.Hair)hairEnum;
					}
				}
			}
		}
	}

	public static org.lgna.story.resources.sims2.PersonResource createRandomResource( org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		if( lifeStage != null ) {
			//pass
		} else {
			org.lgna.story.resources.sims2.LifeStage[] potentialLifeStages = { org.lgna.story.resources.sims2.LifeStage.ELDER, org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.TEEN, org.lgna.story.resources.sims2.LifeStage.CHILD, org.lgna.story.resources.sims2.LifeStage.TODDLER };
			lifeStage = org.lgna.common.RandomUtilities.getRandomValueFrom( potentialLifeStages );
		}
		org.lgna.story.resources.sims2.Gender gender = org.lgna.story.resources.sims2.Gender.getRandom();
		org.lgna.story.resources.sims2.BaseSkinTone skinTone = org.lgna.story.resources.sims2.BaseSkinTone.getRandom();
		org.lgna.story.Color skinColor = org.lgna.story.EmployeesOnly.createColor( skinTone.getColor() );
		org.lgna.story.resources.sims2.EyeColor eyeColor = org.lgna.story.resources.sims2.BaseEyeColor.getRandom();
		org.lgna.story.resources.sims2.Outfit outfit = org.lgna.story.resources.sims2.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender );
		org.lgna.story.resources.sims2.Hair hair = getRandomHair( lifeStage, gender );
		org.lgna.story.resources.sims2.Face face = org.lgna.story.resources.sims2.BaseFace.getRandom();
		double obesityLevel = org.lgna.common.RandomUtilities.nextDouble();
		return lifeStage.createResource( gender, skinColor, eyeColor, hair, obesityLevel, outfit, face );
	}
}
