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

import org.alice.apis.stage.FemaleAdultFullBodyOutfit;
import org.alice.apis.stage.FemaleAdultHair;
import org.alice.apis.stage.FullBodyOutfit;
import org.alice.apis.stage.Hair;
import org.alice.apis.stage.MaleAdultFullBodyOutfit;
import org.alice.apis.stage.MaleAdultHair;

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
	
	private static edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends FullBodyOutfit> > s_mapFullBodyOutfit;
	private static edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends Hair> > s_mapHair;
	static {
		s_mapFullBodyOutfit = new edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends FullBodyOutfit> >();
		s_mapFullBodyOutfit.put( LifeStage.ADULT, Gender.MALE, MaleAdultFullBodyOutfit.class );
		s_mapFullBodyOutfit.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultFullBodyOutfit.class );

		s_mapHair = new edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Class<? extends Hair> >();
		s_mapHair.put( LifeStage.ADULT, Gender.MALE, MaleAdultHair.class );
		s_mapHair.put( LifeStage.ADULT, Gender.FEMALE, FemaleAdultHair.class );
	}
	public Class<? extends FullBodyOutfit> getFullBodyOutfitInterface( Gender gender ) {	
		return s_mapFullBodyOutfit.get( this, gender );
	}
	public Class<? extends Hair> getHairInterface( Gender gender ) {	
		return s_mapHair.get( this, gender );
	}
};
