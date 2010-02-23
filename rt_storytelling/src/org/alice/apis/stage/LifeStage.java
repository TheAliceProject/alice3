/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.stage;

/**
 * @author Dennis Cosgrove
 */
public enum LifeStage {
	TODDLER, 
	CHILD, 
	TEEN, 
	ADULT, 
	ELDER;
	public static LifeStage getRandom() {
		return edu.cmu.cs.dennisc.random.RandomUtilities.getRandomEnumConstant( LifeStage.class );
	}
	
	private static java.util.Map< LifeStage, Class<? extends FullBodyOutfit> > s_mapLifeStageUnisexFullBodyOutfit;
	private static java.util.Map< LifeStage, Class<? extends Hair> > s_mapLifeStageUnisexHair;
	private static edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends FullBodyOutfit> > s_mapLifeStageGenderFullBodyOutfit;
	private static edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends Hair> > s_mapLifeStageGenderHair;
	static {
		s_mapLifeStageUnisexFullBodyOutfit = new java.util.HashMap< LifeStage, Class<? extends FullBodyOutfit> >();
		s_mapLifeStageUnisexFullBodyOutfit.put( LifeStage.ADULT, AdultFullBodyOutfit.class );
		s_mapLifeStageUnisexFullBodyOutfit.put( LifeStage.CHILD, ChildFullBodyOutfit.class );

		s_mapLifeStageUnisexHair = new java.util.HashMap< LifeStage, Class<? extends Hair> >();
		s_mapLifeStageUnisexHair.put( LifeStage.ADULT, AdultHair.class );
		s_mapLifeStageUnisexHair.put( LifeStage.CHILD, ChildHair.class );
		
		
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
};
