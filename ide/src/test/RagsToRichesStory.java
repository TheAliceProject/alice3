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
package test;

import org.lgna.story.Biped;
import org.lgna.story.Camera;
import org.lgna.story.Color;
import org.lgna.story.Cone;
import org.lgna.story.Ground;
import org.lgna.story.MoveDirection;
import org.lgna.story.Program;
import org.lgna.story.RollDirection;
import org.lgna.story.Scene;
import org.lgna.story.Sphere;
import org.lgna.story.Sun;
import org.lgna.story.TurnDirection;
import org.lgna.story.VantagePointWithAsSeenByDetails;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.MonsterResource;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitAmbulanceDriver;
import org.lgna.story.resources.sims2.FemaleAdultHairBraids;
import org.lgna.story.resources.sims2.Gender;

class CustomBiped extends Biped {
	public CustomBiped( BipedResource resource ) {
		super( resource );
	}
}

class CustomMonster extends CustomBiped {
	public CustomMonster( MonsterResource resource ) {
		super( resource );
	}
}

class CustomOgre extends CustomMonster {
	public CustomOgre( org.lgna.story.resources.monster.Ogre resource ) {
		super( resource );
	}
}

class CustomOgreGreen extends CustomOgre {
	public CustomOgreGreen() {
		super( org.lgna.story.resources.monster.Ogre.BEAST_DIFFUSE );
	}
}


class DesertScene extends Scene {
	private final Sun sun = new Sun();
	private final Ground desert = new Ground();
	private final Sphere sphere = new Sphere();
//	private final CustomAdult fellowLaborer = new CustomAdult( org.lgna.story.resources.people.Ogre.BEAST_DIFFUSE );
	private final org.lgna.story.Billboard billboard = new org.lgna.story.Billboard();
	private final Camera camera;
	private final CustomMonster ogre;
	public DesertScene( Camera camera, CustomMonster ogre ) {
		this.camera = camera;
		this.ogre = ogre;
	}
	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
		this.desert.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		//this.sphere.setVehicle( this );
		//this.ogre.setVehicle( this );
//		this.fellowLaborer.setVehicle( this );
		
		this.billboard.setVehicle( this );
		this.billboard.setPaint( Color.RED );
		this.billboard.setBackPaint( Color.BLUE );

//		this.ogre.move( MoveDirection.LEFT, 1.0 );
//		this.fellowLaborer.move( MoveDirection.RIGHT, 1.0 );
		
		this.desert.setPaint( Ground.SurfaceAppearance.SAND );
		this.sphere.setRadius( 0.1 );
		this.sphere.setPaint( Color.RED );
		this.camera.moveAndOrientToAGoodVantagePointOf( this.sphere );
	}
	private void performCustomSetup() {
	}
	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
			} else {
				this.restoreVehiclesAndVantagePoints();
			}
		} else {
			this.preserveVehiclesAndVantagePoints();
		}
	}
		
	public void turnBigRocksIntoLittleRocks() {
		this.ogre.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
		this.ogre.getRightElbow().turn( TurnDirection.FORWARD, 0.25 );
	}
}

class SnowScene extends Scene {
	private final Sun sun = new Sun();
	private final Ground snow = new Ground();
	private final Cone redCone = new Cone(); 
	private final Cone greenCone = new Cone(); 
	private final Cone blueCone = new Cone();
	private final Camera camera;
	private final CustomMonster ogre;
	private final CustomBiped susan;
	public SnowScene( Camera camera, CustomMonster ogre, CustomBiped susan ) {
		this.camera = camera;
		this.susan = susan;
		this.ogre = ogre;
	}
	
	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
		this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.redCone.setVehicle( this );
		this.greenCone.setVehicle( this );
		this.blueCone.setVehicle( this );
		this.camera.setVehicle( this );
		this.susan.setVehicle( this );
		this.ogre.setVehicle( this );
		
		this.redCone.setPaint( Color.RED );
		this.greenCone.setPaint( Color.GREEN );
		this.blueCone.setPaint( Color.BLUE );
		this.redCone.setBaseRadius( 0.1 );
		this.greenCone.setBaseRadius( 0.1 );
		this.blueCone.setBaseRadius( 0.1 );
		this.redCone.setLength( 0.25 );
		this.greenCone.setLength( 0.25 );
		this.blueCone.setLength( 0.25 );

		this.redCone.move( MoveDirection.LEFT, 0.5 );
		this.greenCone.move( MoveDirection.LEFT, 1.0 );
		this.blueCone.move( MoveDirection.LEFT, 1.5 );
		
		this.ogre.move( MoveDirection.LEFT, 1.5 );
		this.susan.turn( TurnDirection.LEFT, 0.25 );
		this.snow.setPaint( Ground.SurfaceAppearance.SNOW );
		this.camera.moveAndOrientToAGoodVantagePointOf( this.ogre );
	}
	private void performCustomSetup() {
	}
	
	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
		if( isActive ) {
			if( activeCount == 1 ) {
				this.performGeneratedSetup();
				this.performCustomSetup();
			} else {
				this.restoreVehiclesAndVantagePoints();
			}
		} else {
			this.preserveVehiclesAndVantagePoints();
		}
	}

	public void chillInSkiChalet() {
		while( true ) {
			this.susan.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
			this.susan.getLeftKnee().turn( TurnDirection.BACKWARD, 0.25 );
			this.ogre.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
			this.redCone.turn( TurnDirection.FORWARD, 1.0 );
			this.blueCone.roll( RollDirection.LEFT, 1.0 );
			this.ogre.getRightShoulder().turn( TurnDirection.LEFT, 0.25 );
			org.alice.virtualmachine.DoTogether.invokeAndWait( new Runnable() {
				public void run() {
					SnowScene.this.redCone.move( MoveDirection.UP, 1.0 );
				}
			}, new Runnable() {
				public void run() {
					SnowScene.this.greenCone.move( MoveDirection.UP, 1.0 );
				}
			}, new Runnable() {
				public void run() {
					SnowScene.this.blueCone.move( MoveDirection.UP, 1.0 );
				}
			} );
			this.redCone.move( MoveDirection.DOWN, 1.0, VantagePointWithAsSeenByDetails.duration( 0.333 ) );
			this.greenCone.move( MoveDirection.DOWN, 1.0, VantagePointWithAsSeenByDetails.duration( 0.333 ) );
			this.blueCone.move( MoveDirection.DOWN, 1.0, VantagePointWithAsSeenByDetails.duration( 0.333 ) );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class RagsToRichesStory extends Program {
	private final Camera camera = new Camera();
	private final CustomBiped susan = new CustomBiped( 
			new AdultPersonResource(
					Gender.FEMALE,
					BaseSkinTone.getRandom(),
					BaseEyeColor.getRandom(),
					FemaleAdultHairBraids.BLACK,
					0.5,
					FemaleAdultFullBodyOutfitAmbulanceDriver.BLUE
	) );
	private final CustomMonster ogre = new CustomMonster( org.lgna.story.resources.monster.Ogre.BEAST_DIFFUSE );
//	private final CustomAdult ogre = new CustomAdult( 
//			new AdultPersonResource(
//					Gender.MALE,
//					BaseSkinTone.getRandom(),
//					BaseEyeColor.getRandom(),
//					MaleAdultHairBald.BARE,
//					0.5,
//					MaleAdultFullBodyOutfitAmbulanceDriver.BLUE
//	) );
	private final DesertScene desertScene = new DesertScene( camera, ogre );
	private final SnowScene snowScene = new SnowScene( camera, ogre, susan );
	public void playOutStory() {
		this.setActiveScene( this.desertScene );
	}
	public static void main( String[] args ) {
		RagsToRichesStory ragsToRichesStory = new RagsToRichesStory();
		ragsToRichesStory.initializeInFrame( args );
		ragsToRichesStory.playOutStory();
	}
}

////best practice
//sphere.move( MoveDirection.LEFT, 0.5, new MoveDetails.Builder().asSeenBy( camera ).duration( 0.5 ).build() );
////serves our purpose
//sphere.turn( TurnDirection.LEFT, 0.5, new TurnDetails().asSeenBy( camera ).duration( 0.5 ) );
////less syntax, more magic?
//sphere.roll( RollDirection.LEFT, 0.5, RollDetailsFactory.asSeenBy( camera ).duration( 0.5 ) );
