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
package org.lgna.story.resources.sims2;

/**
 * @author Dennis Cosgrove
 */
public enum LifeStage {
	TODDLER {
		@Override
		public PersonResource createResource( Gender gender, org.lgna.story.Color skinColor, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit, Face face ) {
			return new ToddlerPersonResource( gender, skinColor, eyeColor, hair, obseityLevel, outfit, face );
		}
	},
	CHILD {
		@Override
		public PersonResource createResource( Gender gender, org.lgna.story.Color skinColor, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit, Face face ) {
			return new ChildPersonResource( gender, skinColor, eyeColor, hair, obseityLevel, outfit, face );
		}
	},
	TEEN {
		@Override
		public PersonResource createResource( Gender gender, org.lgna.story.Color skinColor, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit, Face face ) {
			return new TeenPersonResource( gender, skinColor, eyeColor, hair, obseityLevel, outfit, face );
		}
	},
	ADULT {
		@Override
		public PersonResource createResource( Gender gender, org.lgna.story.Color skinColor, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit, Face face ) {
			return new AdultPersonResource( gender, skinColor, eyeColor, hair, obseityLevel, outfit, face );
		}
	},
	ELDER {
		@Override
		public PersonResource createResource( Gender gender, org.lgna.story.Color skinColor, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit, Face face ) {
			return new ElderPersonResource( gender, skinColor, eyeColor, hair, obseityLevel, outfit, face );
		}
	};

	public static LifeStage getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( LifeStage.class );
	}

	private static final edu.cmu.cs.dennisc.map.MapToMap<LifeStage, Gender, Class<? extends FullBodyOutfit>> mapLifeStageAndGenderToFullBodyOutfit = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static final edu.cmu.cs.dennisc.map.MapToMap<LifeStage, Gender, Class<? extends TopPiece>> mapLifeStageAndGenderToTopPiece = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static final edu.cmu.cs.dennisc.map.MapToMap<LifeStage, Gender, Class<? extends BottomPiece>> mapLifeStageAndGenderToBottomPiece = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	private static final edu.cmu.cs.dennisc.map.MapToMap<LifeStage, Gender, Class<? extends Hair>> mapLifeStageAndGenderToHair = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	static {
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.ELDER, Gender.MALE, MaleElderFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.ELDER, Gender.FEMALE, FemaleElderFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.ADULT, Gender.MALE, MaleAdultFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.TEEN, Gender.MALE, MaleTeenFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.TEEN, Gender.FEMALE, FemaleTeenFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.CHILD, Gender.MALE, MaleChildFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.CHILD, Gender.FEMALE, FemaleChildFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.TODDLER, Gender.MALE, MaleToddlerFullBodyOutfit.class );
		mapLifeStageAndGenderToFullBodyOutfit.put( LifeStage.TODDLER, Gender.FEMALE, FemaleToddlerFullBodyOutfit.class );

		mapLifeStageAndGenderToTopPiece.put( LifeStage.ELDER, Gender.MALE, MaleElderTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.ELDER, Gender.FEMALE, FemaleElderTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.ADULT, Gender.MALE, MaleAdultTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.TEEN, Gender.MALE, MaleTeenTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.TEEN, Gender.FEMALE, FemaleTeenTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.CHILD, Gender.MALE, MaleChildTopPiece.class );
		mapLifeStageAndGenderToTopPiece.put( LifeStage.CHILD, Gender.FEMALE, FemaleChildTopPiece.class );

		mapLifeStageAndGenderToBottomPiece.put( LifeStage.ELDER, Gender.MALE, MaleElderBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.ELDER, Gender.FEMALE, FemaleElderBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.ADULT, Gender.MALE, MaleAdultBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.TEEN, Gender.MALE, MaleTeenBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.TEEN, Gender.FEMALE, FemaleTeenBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.CHILD, Gender.MALE, MaleChildBottomPiece.class );
		mapLifeStageAndGenderToBottomPiece.put( LifeStage.CHILD, Gender.FEMALE, FemaleChildBottomPiece.class );

		mapLifeStageAndGenderToHair.put( LifeStage.ELDER, Gender.MALE, MaleElderHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.ELDER, Gender.FEMALE, FemaleElderHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.ADULT, Gender.MALE, MaleAdultHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.TEEN, Gender.MALE, MaleTeenHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.TEEN, Gender.FEMALE, FemaleTeenHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.CHILD, Gender.MALE, MaleChildHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.CHILD, Gender.FEMALE, FemaleChildHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.TODDLER, Gender.MALE, MaleToddlerHair.class );
		mapLifeStageAndGenderToHair.put( LifeStage.TODDLER, Gender.FEMALE, FemaleToddlerHair.class );
	}

	public Class<? extends FullBodyOutfit> getGenderedFullBodyOutfitInterfaceClass( Gender gender ) {
		return mapLifeStageAndGenderToFullBodyOutfit.get( this, gender );
	}

	public Class<? extends TopPiece> getGenderedTopPieceInterfaceClass( Gender gender ) {
		return mapLifeStageAndGenderToTopPiece.get( this, gender );
	}

	public Class<? extends BottomPiece> getGenderedBottomPieceInterfaceClass( Gender gender ) {
		return mapLifeStageAndGenderToBottomPiece.get( this, gender );
	}

	public Class<? extends Hair> getGenderedHairInterfaceClass( Gender gender ) {
		return mapLifeStageAndGenderToHair.get( this, gender );
	}

	public abstract PersonResource createResource( Gender gender, org.lgna.story.Color skinColor, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit, Face face );
}
