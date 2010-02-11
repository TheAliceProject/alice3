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
package org.alice.stageide.personeditor;

import org.alice.apis.stage.*;

/**
 * @author Dennis Cosgrove
 */
public class PersonViewer extends org.alice.stageide.modelviewer.ModelViewer {
	static PersonViewer singleton = null;

	public static PersonViewer getSingleton() {
		if( PersonViewer.singleton != null ) {
			//pass
		} else {
			PersonViewer.singleton = new PersonViewer();
		}
		return PersonViewer.singleton;
	}

	private IngredientsPane ingredientsPane;
	private org.alice.interact.CreateASimDragAdapter dragAdapter = new org.alice.interact.CreateASimDragAdapter();
	private edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Person > mapToMap = new edu.cmu.cs.dennisc.map.MapToMap< LifeStage, Gender, Person >();
	private LifeStage lifeStage = null;
	private Gender gender = null;
	private BaseSkinTone baseSkinTone = null;
	private BaseEyeColor baseEyeColor = null;
	private FullBodyOutfit fullBodyOutfit = null;
	private Hair hair = null;
	private Double fitnessLevel = 0.5;

	private PersonViewer() {
		Adult femaleAdult = new Adult();
		femaleAdult.setGender( Gender.FEMALE );
		Adult maleAdult = new Adult();
		maleAdult.setGender( Gender.MALE );
		this.mapToMap.put( LifeStage.ADULT, Gender.FEMALE, femaleAdult );
		this.mapToMap.put( LifeStage.ADULT, Gender.MALE, maleAdult );
		this.mapToMap.get( LifeStage.ADULT, Gender.FEMALE ).getSGTransformable().putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );
		this.mapToMap.get( LifeStage.ADULT, Gender.MALE ).getSGTransformable().putBonusDataFor( org.alice.interact.PickHint.PICK_HINT_KEY, org.alice.interact.PickHint.MOVEABLE_OBJECTS );

		this.setState( PersonViewer.generateRandomState() );
	}

	public static edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> generateRandomState() {
		LifeStage lifeStage = LifeStage.ADULT;
		Gender gender = Gender.getRandom();
		return new edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double>( 
				lifeStage,
				gender,
				BaseSkinTone.getRandom(),
				BaseEyeColor.getRandom(),
				FullBodyOutfitUtilities.getSingleton().getRandomEnumConstant( lifeStage, gender ),
				HairUtilities.getSingleton().getRandomEnumConstant( lifeStage, gender ),
				Math.random()
		);
	}
	public edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> getState() {
		return new edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double>( this.lifeStage, this.gender, this.baseSkinTone, this.baseEyeColor, this.fullBodyOutfit, this.hair, this.fitnessLevel );
	}
	public void setState( edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> state ) {
		boolean isUpdateDesired = this.lifeStage != null;
		//this.lifeStage = LifeStage.getRandom();
		this.lifeStage = state.getA();
		this.gender = state.getB();
		this.baseSkinTone = state.getC();
		this.baseEyeColor = state.getD();
		this.fullBodyOutfit = state.getE();
		this.hair = state.getF();
		this.fitnessLevel = state.getG();
		if( isUpdateDesired ) {
			this.updatePerson();
		}
	}

	public void initializeValues( Person person ) {
		if( person != null ) {
			this.lifeStage = person.getLifeStage();
			this.gender = person.getGender();
			this.baseSkinTone = (BaseSkinTone)person.getSkinTone();
			this.baseEyeColor = (BaseEyeColor)person.getEyeColor();
			this.fullBodyOutfit = (FullBodyOutfit)person.getOutfit();
			this.hair = person.getHair();
			this.fitnessLevel = person.getFitnessLevel();
			this.updatePerson();
		}
	}

	public void handleTabSelection( int index ) {
		Person person = this.mapToMap.get( this.lifeStage, this.gender );
		double height = person.getHeight();
		double amount = 3.0;
		double duration = 0.5;
		org.alice.apis.moveandturn.StandIn target = this.getScene().createOffsetStandIn( 0, height*1.0, 0 );
		if( index == 0 ) {
			this.getCamera().moveAwayFrom( amount, target, duration );
		} else {
			this.getCamera().moveToward( amount, target, duration );
		}
	}
	
	public IngredientsPane getIngredientsPane() {
		return this.ingredientsPane;
	}
	public void setIngredientsPane( IngredientsPane ingredientsPane ) {
		this.ingredientsPane = ingredientsPane;
		if( this.ingredientsPane != null ) {
			this.ingredientsPane.refresh();
		}
	}
	
	public org.alice.apis.stage.Person getPerson() {
		if( this.lifeStage != null && this.gender != null ) {
			return this.mapToMap.get( this.lifeStage, this.gender );
		} else {
			return null;
		}
	}

	private void updatePerson() {
		if( this.lifeStage != null && this.gender != null ) {
			Person person = this.mapToMap.get( this.lifeStage, this.gender );
			if( person != null ) {
				this.dragAdapter.setSelectedObject( person.getSGTransformable() );
				if( this.baseSkinTone != null ) {
					person.setSkinTone( this.baseSkinTone );
					if( this.fitnessLevel != null ) {
						person.setFitnessLevel( this.fitnessLevel, org.alice.apis.stage.Person.RIGHT_NOW );
						if( this.fullBodyOutfit != null && FullBodyOutfitUtilities.getSingleton().isApplicable( this.fullBodyOutfit, this.lifeStage, this.gender ) ) {
							//pass
						} else {
							Outfit outfit = person.getOutfit();
							if( outfit instanceof FullBodyOutfit ) {
								this.fullBodyOutfit = ( FullBodyOutfit )outfit;
							} else {
								this.fullBodyOutfit = FullBodyOutfitUtilities.getSingleton().getRandomEnumConstant( this.lifeStage, this.gender );
							}
						}
						person.setOutfit( this.fullBodyOutfit );
					}
				}
				if( this.baseEyeColor != null ) {
					person.setEyeColor( this.baseEyeColor );
				}
				if( this.hair != null && HairUtilities.getSingleton().isApplicable( this.hair, this.lifeStage, this.gender ) ) {
					//pass
				} else {
					Hair hair = person.getHair();
					if( hair != null ) {
						this.hair = hair;
					} else {
						this.hair = HairUtilities.getSingleton().getRandomEnumConstant( this.lifeStage, this.gender );
					}
				}
				person.setHair( this.hair );
				this.setModel( person );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "updatePerson", this.lifeStage, this.gender );
			}
			if( this.ingredientsPane != null ) {
				this.ingredientsPane.refresh();
			}
		}
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.updatePerson();
		if( this.lifeStage != null && this.gender != null ) {
			Person person = this.mapToMap.get( this.lifeStage, this.gender );
			double height = person.getHeight();
			this.getCamera().moveTo( this.getScene().createOffsetStandIn( -1, height*0.667, -height*2.333 ), 0.0 );
			this.getCamera().pointAt( this.getScene().createOffsetStandIn( 0, height*0.5, 0 ), 0.0 );
		}
		//this._sunLight.turn( apis.moveandturn.TurnDirection.FORWARD, org.alice.apis.moveandturn.AngleInRevolutions( 0.125 ) );
		this.dragAdapter.setOnscreenLookingGlass( this.getOnscreenLookingGlass() );
	}
	
	public LifeStage getLifeStage() {
		return this.lifeStage;
	}
	public void setLifeStage( LifeStage lifeStage ) {
		if( this.lifeStage == lifeStage ) {
			//pass
		} else {
			this.lifeStage = lifeStage;
			this.updatePerson();
		}
	}

	public Gender getGender() {
		return this.gender;
	}
	public void setGender( Gender gender ) {
		if( this.gender == gender ) {
			//pass
		} else {
			this.gender = gender;
			this.updatePerson();
		}
	}
	public BaseEyeColor getBaseEyeColor() {
		return this.baseEyeColor;
	}
	public void setBaseEyeColor( BaseEyeColor baseEyeColor ) {
		if( this.baseEyeColor == baseEyeColor ) {
			//pass
		} else {
			this.baseEyeColor = baseEyeColor;
			this.updatePerson();
		}
	}
	public BaseSkinTone getBaseSkinTone() {
		return this.baseSkinTone;
	}
	public void setBaseSkinTone( BaseSkinTone baseSkinTone ) {
		if( this.baseSkinTone == baseSkinTone ) {
			//pass
		} else {
			this.baseSkinTone = baseSkinTone;
			this.updatePerson();
		}
	}
	public FullBodyOutfit getFullBodyOutfit() {
		return this.fullBodyOutfit;
	}
	public void setFullBodyOutfit( FullBodyOutfit fullBodyOutfit ) {
		if( this.fullBodyOutfit == fullBodyOutfit ) {
			//pass
		} else {
			this.fullBodyOutfit = fullBodyOutfit;
			this.updatePerson();
		}
	}
	
	
	
	public Hair getHair() {
		return this.hair;
	}
	public void setHair( Hair hair ) {
		if( this.hair == hair ) {
			//pass
		} else {
			this.hair = hair;
			this.updatePerson();
		}
	}
	
	public String getHairColor() {
		return this.hair.toString();
	}
	public void setHairColor( String hairColor ) {
		if( this.hair != null ) {
			Class< ? extends Enum > cls = (Class<? extends Enum>)this.hair.getClass();
			for( Enum e : cls.getEnumConstants() ) {
				if( e.name().equals( hairColor ) ) {
					this.setHair( (Hair)e );
					break;
				}
			}
		}
	}
	
	public Double getFitnessLevel() {
		return this.fitnessLevel;
	}
	public void setFitnessLevel( Double fitnessLevel ) {
		if( this.fitnessLevel == fitnessLevel ) {
			//pass
		} else {
			this.fitnessLevel = fitnessLevel;
			this.updatePerson();
		}
	}

	public static void main( String[] args ) {
		PersonEditor.main( args );
	}
}
