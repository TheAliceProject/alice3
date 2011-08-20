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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
public class PersonInfo {
	//public static final org.lgna.croquet.Group PERSON_GROUP = org.lgna.croquet.Group.getInstance( java.util.UUID.fromString( "2d7d725d-1806-40d1-ac2b-d9cd48cb0abb" ), "PERSON_GROUP" );

	///*package-private*/ static final java.awt.Color BACKGROUND_COLOR = new java.awt.Color( 220, 220, 255 );
	public static final java.awt.Color SELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( java.awt.Color.YELLOW, 1.0, 0.3, 1.0 );
	public static final java.awt.Color UNSELECTED_COLOR = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR, 1.0, 0.9, 0.8 );

	public static PersonInfo createFromPerson( org.lgna.story.implementation.sims2.SimsBipedImplementation person ) {
		return new PersonInfo( 
				person.getLifeStage(), 
				person.getGender(), 
				(org.lgna.story.resources.sims2.BaseSkinTone)person.getSkinTone(), 
				(org.lgna.story.resources.sims2.BaseEyeColor)person.getEyeColor(), 
				(org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit)person.getOutfit(), 
				person.getHair(), 
				person.getObesityLevel()
		);
	}
	public static PersonInfo createRandom() {
		org.lgna.story.resources.sims2.LifeStage[] potentialLifeStages = { org.lgna.story.resources.sims2.LifeStage.ADULT, org.lgna.story.resources.sims2.LifeStage.CHILD };
		org.lgna.story.resources.sims2.LifeStage lifeStage = edu.cmu.cs.dennisc.random.RandomUtilities.getRandomValueFrom( potentialLifeStages );
		org.lgna.story.resources.sims2.Gender gender = org.lgna.story.resources.sims2.Gender.getRandom();
		return new PersonInfo( 
				lifeStage, 
				gender,
				org.lgna.story.resources.sims2.BaseSkinTone.getRandom(),
				org.lgna.story.resources.sims2.BaseEyeColor.getRandom(),
				org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
				org.lookingglassandalice.storytelling.resources.sims2.HairManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
				edu.cmu.cs.dennisc.random.RandomUtilities.nextDouble()
		);
	}

	private org.lgna.story.resources.sims2.LifeStage lifeStage;
	private org.lgna.story.resources.sims2.Gender gender;
	private org.lgna.story.resources.sims2.BaseSkinTone baseSkinTone;
	private org.lgna.story.resources.sims2.BaseEyeColor baseEyeColor;
	private org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit fullBodyOutfit;
	private org.lookingglassandalice.storytelling.resources.sims2.Hair hair;
	private double fitnessLevel;

	private PersonInfo( org.lgna.story.resources.sims2.LifeStage lifeStage, org.lgna.story.resources.sims2.Gender gender, org.lgna.story.resources.sims2.BaseSkinTone baseSkinTone, org.lgna.story.resources.sims2.BaseEyeColor baseEyeColor, org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit fullBodyOutfit, org.lookingglassandalice.storytelling.resources.sims2.Hair hair, double fitnessLevel ) {
		this.lifeStage = lifeStage;
		this.gender = gender;
		this.baseSkinTone = baseSkinTone;
		this.baseEyeColor = baseEyeColor;
		this.fullBodyOutfit = fullBodyOutfit;
		this.hair = hair;
		this.fitnessLevel = fitnessLevel;
	}
	public org.lgna.story.resources.sims2.LifeStage getLifeStage() {
		return this.lifeStage;
	}
	public org.lgna.story.resources.sims2.Gender getGender() {
		return this.gender;
	}
	public org.lgna.story.resources.sims2.BaseSkinTone getBaseSkinTone() {
		return this.baseSkinTone;
	}
	public org.lgna.story.resources.sims2.BaseEyeColor getBaseEyeColor() {
		return this.baseEyeColor;
	}
	public org.lookingglassandalice.storytelling.resources.sims2.FullBodyOutfit getFullBodyOutfit() {
		return this.fullBodyOutfit;
	}
	public org.lookingglassandalice.storytelling.resources.sims2.Hair getHair() {
		return this.hair;
	}
	public double getFitnessLevel() {
		return this.fitnessLevel;
	}
	
	public org.lgna.story.resources.sims2.PersonResource createPersonResource() {
		org.lgna.story.resources.sims2.PersonResource rv;
		if( this.lifeStage != null ) {
			rv = this.lifeStage.createPersonResource( this.gender, this.baseSkinTone, this.baseEyeColor, this.hair, this.fitnessLevel, this.fullBodyOutfit );
		} else {
			rv = null;
		}
		return rv;
	}
}
