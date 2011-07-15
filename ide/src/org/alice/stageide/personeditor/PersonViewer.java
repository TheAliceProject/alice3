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

import org.lookingglassandalice.storytelling.*;
import org.alice.interact.AbstractDragAdapter.CameraView;

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

//	private IngredientsPane ingredientsPane;
	private org.alice.interact.CreateASimDragAdapter dragAdapter = new org.alice.interact.CreateASimDragAdapter();
	private PersonViewer() {
//		this.setState( PersonViewer.generateRandomState( LifeStage.ADULT, Gender.getRandom() ) );
	}

//	public static edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> generateRandomState( LifeStage lifeStage, Gender gender ) {
//		return new edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double>( 
//				lifeStage,
//				gender,
//				BaseSkinTone.getRandom(),
//				BaseEyeColor.getRandom(),
//				FullBodyOutfitManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
//				HairManager.getSingleton().getRandomEnumConstant( lifeStage, gender ),
//				Math.random()
//		);
//	}
//	public edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> getState() {
//		return new edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double>( this.lifeStage, this.gender, this.baseSkinTone, this.baseEyeColor, this.fullBodyOutfit, this.hair, this.fitnessLevel );
//	}
//	public void setState( edu.cmu.cs.dennisc.pattern.Tuple7<LifeStage, Gender, BaseSkinTone, BaseEyeColor, FullBodyOutfit, Hair, Double> state ) {
//		boolean isUpdateDesired = this.lifeStage != null && this.gender != null;
//		//this.lifeStage = LifeStage.getRandom();
//		this.lifeStage = state.getA();
//		this.gender = state.getB();
//		this.baseSkinTone = state.getC();
//		this.baseEyeColor = state.getD();
//		this.fullBodyOutfit = state.getE();
//		this.hair = state.getF();
//		this.fitnessLevel = state.getG();
//		if( isUpdateDesired ) {
//			this.updatePerson();
//		}
//	}

//	public void initializeValues( Person person ) {
//		if( person != null ) {
//			this.lifeStage = person.getLifeStage();
//			this.gender = person.getGender();
//			this.baseSkinTone = (BaseSkinTone)person.getSkinTone();
//			this.baseEyeColor = (BaseEyeColor)person.getEyeColor();
//			this.fullBodyOutfit = (FullBodyOutfit)person.getOutfit();
//			this.hair = person.getHair();
//			this.fitnessLevel = person.getFitnessLevel();
//			this.updatePerson();
//		}
//	}
//
//	public void handleTabSelection( int index, double duration ) {
//		Person person = this.mapLifeStageGenderToPerson.get( this.lifeStage, this.gender );
//		this.positionAndOrientCamera( person.getHeight(), index, duration );
//	}
	
//	public IngredientsPane getIngredientsPane() {
//		return this.ingredientsPane;
//	}
//	public void setIngredientsPane( IngredientsPane ingredientsPane ) {
//		this.ingredientsPane = ingredientsPane;
//		if( this.ingredientsPane != null ) {
//			this.ingredientsPane.refresh();
//		}
//	}
	
//	public org.alice.apis.stage.Person getPerson() {
//		if( this.lifeStage != null && this.gender != null ) {
//			return this.mapLifeStageGenderToPerson.get( this.lifeStage, this.gender );
//		} else {
//			return null;
//		}
//	}

	private void positionAndOrientCamera( double height, int index, double duration ) {
		double xzFactor;
		if( index == 0 ) {
			xzFactor = 2.333;
		} else {
			xzFactor = 0.5;
		}
		double yFactor;
		if( index == 0 ) {
			yFactor = 0.5;
		} else {
			yFactor = 0.9;
		}
		if( this.getScene() != null ) {
			org.lookingglassandalice.storytelling.VantagePoint prevPOV = this.getCamera().getLocalPointOfView();
			this.getCamera().moveTo( this.getScene().createOffsetStandIn( -0.3*xzFactor, height*yFactor, -height*xzFactor ), 0.0 );
			this.getCamera().pointAt( this.getScene().createOffsetStandIn( 0, height*yFactor, 0 ), 0.0 );
			edu.cmu.cs.dennisc.animation.Animator animator = this.getAnimator();
			if( duration > 0.0 && animator != null ) {
				org.lookingglassandalice.storytelling.VantagePoint nextPOV = this.getCamera().getLocalPointOfView();
				this.getCamera().setLocalPointOfView( prevPOV );

				edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation povAnimation = new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( this.getCamera().getSGAbstractTransformable(), edu.cmu.cs.dennisc.scenegraph.AsSeenBy.PARENT, null, nextPOV.getInternal() );
				povAnimation.setDuration( duration );

				animator.completeAll( null );
				animator.invokeLater( povAnimation, null );
			}
		}
	}

	public Person getPerson() { 
		return (Person)this.getModel();
	}
	public void setPerson( Person person ) {
		assert person != null;
		this.setModel( person );
		this.dragAdapter.setSelectedObject( person.getSGTransformable() );
		double height = person.getHeight();
		this.positionAndOrientCamera( height, 0, 0.0 );
	}
	@Override
	protected void initialize() {
		super.initialize();
		this.dragAdapter.setOnscreenLookingGlass( this.getOnscreenLookingGlass() );
		this.dragAdapter.addCameraView( CameraView.MAIN, this.getCamera().getSGSymmetricPerspectiveCamera(), null );
		this.dragAdapter.makeCameraActive( this.getCamera().getSGSymmetricPerspectiveCamera() );
	}
}
