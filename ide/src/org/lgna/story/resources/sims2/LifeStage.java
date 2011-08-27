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

import org.lookingglassandalice.storytelling.resources.sims2.FemaleAdultFullBodyOutfit;
import org.lookingglassandalice.storytelling.resources.sims2.FemaleAdultHair;
import org.lookingglassandalice.storytelling.resources.sims2.FemaleChildFullBodyOutfit;
import org.lookingglassandalice.storytelling.resources.sims2.FemaleChildHair;
import org.lookingglassandalice.storytelling.resources.sims2.MaleAdultFullBodyOutfit;
import org.lookingglassandalice.storytelling.resources.sims2.MaleAdultHair;
import org.lookingglassandalice.storytelling.resources.sims2.MaleChildFullBodyOutfit;
import org.lookingglassandalice.storytelling.resources.sims2.MaleChildHair;
import org.lookingglassandalice.storytelling.resources.sims2.UnisexAdultFullBodyOutfit;
import org.lookingglassandalice.storytelling.resources.sims2.UnisexAdultHair;
import org.lookingglassandalice.storytelling.resources.sims2.UnisexChildFullBodyOutfit;
import org.lookingglassandalice.storytelling.resources.sims2.UnisexChildHair;

/**
 * @author Dennis Cosgrove
 */
public enum LifeStage {
	TODDLER {
		@Override
		public PersonResource createResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit ) {
			return null;
		} 
	},
	CHILD {
		@Override
		public PersonResource createResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit ) {
			return new ChildPersonResource( gender, skinTone, eyeColor, hair, obseityLevel, outfit );
		} 
	}, 
	TEEN {
		@Override
		public PersonResource createResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit ) {
			return null;
		} 
	}, 
	ADULT {
		@Override
		public PersonResource createResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit ) {
			return new AdultPersonResource( gender, skinTone, eyeColor, hair, obseityLevel, outfit );
		} 
	}, 
	ELDER {
		@Override
		public PersonResource createResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit ) {
			return null;
		} 
	};
	
	public static LifeStage getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( LifeStage.class );
	}
	
	private static java.util.Map< LifeStage, Class<? extends FullBodyOutfit> > s_mapLifeStageUnisexFullBodyOutfit;
	private static java.util.Map< LifeStage, Class<? extends Hair> > s_mapLifeStageUnisexHair;
	private static edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends FullBodyOutfit> > s_mapLifeStageGenderFullBodyOutfit;
	private static edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends Hair> > s_mapLifeStageGenderHair;
	static {
		s_mapLifeStageUnisexFullBodyOutfit = new java.util.HashMap< LifeStage, Class<? extends FullBodyOutfit> >();
		s_mapLifeStageUnisexFullBodyOutfit.put( LifeStage.ADULT, UnisexAdultFullBodyOutfit.class );
		s_mapLifeStageUnisexFullBodyOutfit.put( LifeStage.CHILD, UnisexChildFullBodyOutfit.class );

		s_mapLifeStageUnisexHair = new java.util.HashMap< LifeStage, Class<? extends Hair> >();
		s_mapLifeStageUnisexHair.put( LifeStage.ADULT, UnisexAdultHair.class );
		s_mapLifeStageUnisexHair.put( LifeStage.CHILD, UnisexChildHair.class );
		
		
		s_mapLifeStageGenderFullBodyOutfit = new edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends FullBodyOutfit> >();
		s_mapLifeStageGenderFullBodyOutfit.put( LifeStage.ADULT, Gender.MALE, MaleAdultFullBodyOutfit.class );
		s_mapLifeStageGenderFullBodyOutfit.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultFullBodyOutfit.class );
		s_mapLifeStageGenderFullBodyOutfit.put( LifeStage.CHILD, Gender.MALE, MaleChildFullBodyOutfit.class );
		s_mapLifeStageGenderFullBodyOutfit.put( LifeStage.CHILD, Gender.FEMALE, FemaleChildFullBodyOutfit.class );

		s_mapLifeStageGenderHair = new edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends Hair> >();
		s_mapLifeStageGenderHair.put( LifeStage.ADULT, Gender.MALE, MaleAdultHair.class );
		s_mapLifeStageGenderHair.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultHair.class );
		s_mapLifeStageGenderHair.put( LifeStage.CHILD, Gender.MALE, MaleChildHair.class );
		s_mapLifeStageGenderHair.put( LifeStage.CHILD, Gender.FEMALE, FemaleChildHair.class );
	}
	public Class<? extends FullBodyOutfit> getGenderedFullBodyOutfitInterfaceClass( Gender gender ) {	
		return s_mapLifeStageGenderFullBodyOutfit.get( this, gender );
	}
	public Class<? extends Hair> getGenderedHairInterfaceClass( Gender gender ) {	
		return s_mapLifeStageGenderHair.get( this, gender );
	}
	public Class<? extends FullBodyOutfit> getUnisexFullBodyOutfitInterfaceClass() {	
		return s_mapLifeStageUnisexFullBodyOutfit.get( this );
	}
	public Class<? extends Hair> getUnisexHairInterfaceClass() {	
		return s_mapLifeStageUnisexHair.get( this );
	}
	
	public abstract PersonResource createResource( Gender gender, SkinTone skinTone, EyeColor eyeColor, Hair hair, Number obseityLevel, Outfit outfit );
}
