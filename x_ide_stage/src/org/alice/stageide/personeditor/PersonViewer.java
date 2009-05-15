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
	private FitnessLevel fitnessLevel = FitnessLevel.NORMAL;

	private PersonViewer() {
		this.mapToMap.put( LifeStage.ADULT, Gender.FEMALE, new FemaleAdult() );
		this.mapToMap.put( LifeStage.ADULT, Gender.MALE, new MaleAdult() );
		this.randomize();
		//		this.setLifeStage( LifeStage.ADULT );
		//		this.setGender( Gender.FEMALE );
		//		this.setBaseSkinTone( baseSkinTone.getRandom() );
		//		this.setFullBodyOutfit( FemaleAdultFullBodyOutfitAmbulanceDriver.BLUE );
		//		this.setHair( FemaleAdultHairGetFabulous.RED );
		//		this.setFitnessLevel( FitnessLevel.NORMAL );
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
						person.setFitnessLevel( this.fitnessLevel );
						if( this.fullBodyOutfit != null && IngredientUtilities.isApplicable( this.fullBodyOutfit, this.lifeStage, this.gender ) ) {
							//pass
						} else {
							Outfit outfit = person.getOutfit();
							if( outfit instanceof FullBodyOutfit ) {
								this.fullBodyOutfit = ( FullBodyOutfit )outfit;
							} else {
								this.fullBodyOutfit = IngredientUtilities.getRandomEnumConstant( this.lifeStage.getFullBodyOutfitInterface( this.gender ) );
							}
						}
						person.setOutfit( this.fullBodyOutfit );
					}
				}
				if( this.baseEyeColor != null ) {
					person.setEyeColor( this.baseEyeColor );
				}
				if( this.hair != null && IngredientUtilities.isApplicable( this.hair, this.lifeStage, this.gender ) ) {
					//pass
				} else {
					Hair hair = person.getHair();
					if( hair != null ) {
						this.hair = hair;
					} else {
						this.hair = IngredientUtilities.getRandomEnumConstant( this.lifeStage.getHairInterface( this.gender ) );
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
		this.getCamera().moveTo( this.getScene().createOffsetStandIn( -1, 2, -7 ), 0.0 );
		this.getCamera().pointAt( this.getScene().createOffsetStandIn( 0, 1.5, 0 ), 0.0 );
		//this._sunLight.turn( apis.moveandturn.TurnDirection.FORWARD, org.alice.apis.moveandturn.AngleInRevolutions( 0.125 ) );
		this.dragAdapter.setOnscreenLookingGlass( this.getOnscreenLookingGlass() );
		this.updatePerson();
	}

	public void randomize() {
		boolean isUpdateDesired = this.lifeStage != null;
		//this.lifeStage = LifeStage.getRandom();
		this.lifeStage = LifeStage.ADULT;
		this.gender = Gender.getRandom();
		this.baseSkinTone = BaseSkinTone.getRandom();
		this.baseEyeColor = BaseEyeColor.getRandom();
		this.fullBodyOutfit = IngredientUtilities.getRandomEnumConstant( this.lifeStage.getFullBodyOutfitInterface( this.gender ) );
		this.hair = IngredientUtilities.getRandomEnumConstant( this.lifeStage.getHairInterface( this.gender ) );
		//this.fitnessLevel = FitnessLevel.getRandom();
		this.fitnessLevel = FitnessLevel.NORMAL;
		if( isUpdateDesired ) {
			this.updatePerson();
		}
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
			Class<Enum> cls = (Class<Enum>)this.hair.getClass();
			for( Enum e : cls.getEnumConstants() ) {
				if( e.name().equals( hairColor ) ) {
					this.setHair( (Hair)e );
					break;
				}
			}
		}
	}
	
	public FitnessLevel getFitnessLevel() {
		return this.fitnessLevel;
	}
	public void setFitnessLevel( FitnessLevel fitnessLevel ) {
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
	//	def initialize( self ):
	//		ecc.dennisc.alice.ide.moveandturn.editors.ModelViewer.initialize( self )
	//		#self._scene.setAmbientLightBrightness( 1.0 )
	//		#self._dragAdapter.setSGCamera( self._camera.getSGCamera() )

	//	def _update( self ):
	//		if self._lifeStage and self._gender:
	//			person = self._mapToMap[ self._lifeStage ][ self._gender ]
	//			model = None
	//			if self._baseSkinTone:
	//				person.setSkinTone( self._baseSkinTone )
	//				if self._fitnessLevel:
	//					person.setFitnessLevel( self._fitnessLevel )
	//					if self._fullBodyOutfit:
	//						person.setOutfit( self._fullBodyOutfit )
	//						model = person
	//			if self._baseEyeColor:
	//				person.setEyeColor( self._baseEyeColor )
	//			if self._hair:
	//				person.setHair( self._hair )
	//			self.setModel( model )
	//
	//	def setLifeStage( self, lifeStage ):
	//		if self._lifeStage == lifeStage:
	//			pass
	//		else:
	//			self._lifeStage = lifeStage
	//			self._fullBodyOutfit = None
	//			self._hair = None
	//			self._update()
	//	def setGender( self, gender ):
	//		if self._gender == gender:
	//			pass
	//		else:
	//			self._gender = gender
	//			self._fullBodyOutfit = None
	//			self._hair = None
	//			self._update()
	//			person = self._mapToMap[ LifeStage.ADULT ][ gender ]
	//			if person:
	//				self._dragAdapter.setSelectedObject( person.getSGTransformable() )
	//	def setBaseSkinTone( self, baseSkinTone ):
	//		if self._baseSkinTone == baseSkinTone:
	//			pass
	//		else:
	//			self._baseSkinTone = baseSkinTone
	//			self._update()
	//	def setBaseEyeColor( self, baseEyeColor ):
	//		if self._baseEyeColor == baseEyeColor:
	//			pass
	//		else:
	//			self._baseEyeColor = baseEyeColor
	//			self._update()
	//	def setFullBodyOutfit( self, fullBodyOutfit ):
	//		if self._fullBodyOutfit == fullBodyOutfit:
	//			pass
	//		else:
	//			self._fullBodyOutfit = fullBodyOutfit
	//			self._update()
	//
	//	def setHair( self, hair ):
	//		if self._hair == hair:
	//			pass
	//		else:
	//			self._hair = hair
	//			self._update()
}
