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

import java.awt.Component;

import org.lgna.story.Color;
import org.lgna.story.ImplementationAccessor;
import org.lgna.story.Move;
import org.lgna.story.MoveDirection;
import org.lgna.story.RollDirection;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.SCone;
import org.lgna.story.SGround;
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.lgna.story.SSphere;
import org.lgna.story.SSun;
import org.lgna.story.TurnDirection;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.CollisionEndListener;
import org.lgna.story.event.CollisionStartListener;
import org.lgna.story.event.ComesIntoViewEvent;
import org.lgna.story.event.EndCollisionEvent;
import org.lgna.story.event.EndOcclusionEvent;
import org.lgna.story.event.EnterProximityEvent;
import org.lgna.story.event.ExitProximityEvent;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.LeavesViewEvent;
import org.lgna.story.event.MouseClickOnObjectEvent;
import org.lgna.story.event.MouseClickOnObjectListener;
import org.lgna.story.event.MouseClickOnScreenListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;
import org.lgna.story.event.OcclusionEndListener;
import org.lgna.story.event.OcclusionStartListener;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;
import org.lgna.story.event.ProximityEnterListener;
import org.lgna.story.event.ProximityExitListener;
import org.lgna.story.event.SceneActivationEvent;
import org.lgna.story.event.SceneActivationListener;
import org.lgna.story.event.StartCollisionEvent;
import org.lgna.story.event.StartOcclusionEvent;
import org.lgna.story.event.TimeEvent;
import org.lgna.story.event.TimeListener;
import org.lgna.story.event.ViewEnterListener;
import org.lgna.story.event.ViewExitListener;
import org.lgna.story.event.WhileCollisionEvent;
import org.lgna.story.event.WhileCollisionListener;
import org.lgna.story.event.WhileInViewEvent;
import org.lgna.story.event.WhileInViewListener;
import org.lgna.story.event.WhileOccludingEvent;
import org.lgna.story.event.WhileOcclusionListener;
import org.lgna.story.event.WhileProximityEvent;
import org.lgna.story.event.WhileProximityListener;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitAmbulanceDriver;
import org.lgna.story.resources.sims2.FemaleAdultHairBraids;
import org.lgna.story.resources.sims2.Gender;

import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

class MyBiped extends SBiped {
	public MyBiped( BipedResource resource ) {
		super( resource );
	}
}

class MyOgre extends MyBiped {
	public MyOgre( org.lgna.story.resources.biped.OgreResource resource ) {
		super( resource );
	}

	public void doOgreyThing() {
		this.say( "I am an ogre!!!" );
		this.getHead().turn( TurnDirection.RIGHT, 1 );
	}
}

//class MyArmoire extends Prop {
//	public MyArmoire() {
//		super( org.lgna.story.resources.prop.Helicopter.VEHICLE_HELICOPTER );
//	}
//	public Joint getLeftDoor() {
//		return this.getJoint( org.lgna.story.resources.ArmoireResource.LEFT_DOOR );
//	}
//	public Joint getRightDoor() {
//		return this.getJoint( org.lgna.story.resources.ArmoireResource.RIGHT_DOOR );
//	}
//}

class DesertScene extends SScene {
	private final SSun sun = new SSun();
	private final SGround desert = new SGround();
	private final SSphere sphere = new SSphere();
	private final MyBiped fellowLaborer = new MyBiped( org.lgna.story.resources.biped.OgreResource.BROWN );
	private final org.lgna.story.SBillboard billboard = new org.lgna.story.SBillboard();
	private final SCamera camera;
	private final MyOgre ogre;

	public DesertScene( SCamera camera, MyOgre ogre ) {
		this.camera = camera;
		this.ogre = ogre;
	}
	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
		this.desert.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.sphere.setVehicle( this );
		this.ogre.setVehicle( this );
		this.fellowLaborer.setVehicle( this );

		this.billboard.setVehicle( this );
		this.billboard.setPaint( Color.RED );
		this.billboard.setBackPaint( Color.BLUE );

		this.ogre.move( MoveDirection.LEFT, 1.0 );
		this.fellowLaborer.move( MoveDirection.RIGHT, 1.0 );

		this.desert.setPaint( SGround.SurfaceAppearance.SAND );
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
				this.restoreStateAndEventListeners();
			}
		} else {
			this.preserveStateAndEventListeners();
		}
	}

	public void turnBigRocksIntoLittleRocks() {
		this.ogre.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
		this.ogre.getRightElbow().turn( TurnDirection.FORWARD, 0.25 );
	}
}

class SnowScene extends SScene {
	private final SSun sun = new SSun();
	private final SGround snow = new SGround();
	private final SCone redCone = new SCone();
	private final SCone greenCone = new SCone();
	private final SCone blueCone = new SCone();
	//	private final MyArmoire armoire = new MyArmoire();
	private final SCamera camera;
	private final MyOgre ogre;
	private final MyBiped susan;
	private SCone mattsObjectThingy;

	public SnowScene( SCamera camera, MyOgre ogre, MyBiped susan ) {
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
		//		this.armoire.setVehicle( this );
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

		//		this.armoire.move( MoveDirection.BACKWARD, 2.0 );

		this.ogre.move( MoveDirection.LEFT, 2.0 );
		this.susan.turn( TurnDirection.LEFT, 0.25 );
		this.snow.setPaint( SGround.SurfaceAppearance.SNOW );
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
				this.performInitializeEvents();
			} else {
				this.restoreStateAndEventListeners();
			}
		} else {
			this.preserveStateAndEventListeners();
		}
	}

	Boolean[] arr = new Boolean[ 20 ];

	private void testEvents() {
		boolean allOK = true;
		for( int i = 0; i != arr.length; ++i ) {
			if( !arr[ i ] ) {
//				System.out.println( getErrorMessage( i ) );
				allOK = false;
			}
		}
		if( allOK ) {
			ogre.say( "all of the events have fired" );
		}
	}

	private String getErrorMessage( int i ) {
		switch( i ) {
		case 0:
			return "ArrowKeyPressListener has not fired";
		case 1:
			return "NumberKeyPressListener has not fired";
		case 2:
			return "KeyPressListener has not fired";
		case 3:
			return "MouseClickOnObjectListener has not fired";
		case 4:
			return "MouseClickOnScreenListener has not fired";
		case 5:
			return "TimeListener has not fired";
		case 6:
			return "SceneActivationListener has not fired";
		case 7:
			return "StartCollisionEvent has not fired";
		case 8:
			return "CollisionEndListener has not fired";
		case 9:
			return "ProximityEnterListener has not fired";
		case 10:
			return "ProximityExitListener has not fired";
		case 11:
			return "OcclusionStartListener has not fired";
		case 12:
			return "OcclusionEndListener has not fired";
		case 13:
			return "ViewEnterListener has not fired";
		case 14:
			return "ViewExitListener has not fired";
		case 15:
			return "PointOfViewChangeListener has not fired";
		case 16:
			return "WhileCollisionListener has not fired";
		case 17:
			return "WhileInViewListener has not fired";
		case 18:
			return "WhileOcclusionListener has not fired";
		case 19:
			return "WhileProximityListener has not fired";
		default:
			return "no recognized error";
		}
	}

	private void performInitializeEvents() {

		for( int i = 0; i != arr.length; ++i ) {
			arr[ i ] = false;
		}
		//keyboard
		this.addArrowKeyPressListener( new ArrowKeyPressListener() {

			public void arrowKeyPressed( ArrowKeyEvent e ) {
				arr[ 0 ] = true;
				testEvents();
			}
		} );
		this.addNumberKeyPressListener( new NumberKeyPressListener() {

			public void numberKeyPressed( NumberKeyEvent e ) {
				arr[ 1 ] = true;
			}
		} );
		this.addKeyPressListener( new KeyPressListener() {

			public void keyPressed( KeyEvent e ) {
				arr[ 2 ] = true;
			}
		} );
		//^^^^keyboard
		//mouse
		this.addMouseClickOnObjectListener( new MouseClickOnObjectListener<MyBiped>() {

			public void mouseClicked( MouseClickOnObjectEvent<MyBiped> e ) {
				arr[ 3 ] = true;
			}
		}, MyBiped.class );
		this.addMouseClickOnScreenListener( new MouseClickOnScreenListener() {

			public void mouseClicked() {
				arr[ 4 ] = true;
				testEvents();
			}
		} );
		//^^^^mouse
		//time
		this.addTimeListener( new TimeListener() {

			public void timeElapsed( TimeEvent e ) {
				arr[ 5 ] = true;
			}
		} );
		this.addSceneActivationListener( new SceneActivationListener() {
			public void sceneActivated( SceneActivationEvent e ) {
				arr[ 6 ] = true;
			}
		} );
		//^^^^time
		//transformationEvents
		this.addCollisionStartListener( new CollisionStartListener<MyOgre,MyBiped>() {

			public void collisionStarted( StartCollisionEvent<MyOgre,MyBiped> e ) {
				arr[ 7 ] = true;
			}
		}, MyOgre.class, MyBiped.class );
		this.addCollisionEndListener( new CollisionEndListener<MyOgre,MyBiped>() {

			public void collisionEnded( EndCollisionEvent<MyOgre,MyBiped> e ) {
				arr[ 8 ] = true;
			}
		}, MyOgre.class, MyBiped.class );
		this.addProximityEnterListener( new ProximityEnterListener<MyOgre,MyBiped>() {

			public void proximityEntered( EnterProximityEvent<MyOgre,MyBiped> e ) {
				arr[ 9 ] = true;
			}
		}, MyOgre.class, MyBiped.class, 1.0 );
		addProximityExitListener( new ProximityExitListener<MyOgre,MyBiped>() {

			public void proximityExited( ExitProximityEvent<MyOgre,MyBiped> e ) {
				arr[ 10 ] = true;
			}
		}, MyOgre.class, MyBiped.class, 1.0 );
		addOcclusionStartListener( new OcclusionStartListener<MyOgre,MyBiped>() {

			public void occlusionStarted( StartOcclusionEvent<MyOgre,MyBiped> e ) {
				arr[ 11 ] = true;
			}
		}, MyOgre.class, MyBiped.class );
		addOcclusionEndListener( new OcclusionEndListener<MyOgre,MyBiped>() {

			public void occlusionEnded( EndOcclusionEvent<MyOgre,MyBiped> e ) {
				arr[ 12 ] = true;
			}
		}, MyOgre.class, MyBiped.class );
		addViewEnterListener( new ViewEnterListener<MyOgre>() {

			public void viewEntered( ComesIntoViewEvent<MyOgre> e ) {
				arr[ 13 ] = true;
			}
		}, MyOgre.class );
		addViewExitListener( new ViewExitListener<MyOgre>() {

			public void leftView( LeavesViewEvent<MyOgre> e ) {
				arr[ 14 ] = true;
			}
		}, MyOgre.class );
		addPointOfViewChangeListener( new PointOfViewChangeListener<MyOgre>() {

			public void pointOfViewChanged( PointOfViewEvent<MyOgre> e ) {
				arr[ 15 ] = true;
			}
		}, MyOgre.class );
		//^^transformationEvents
		//whileEventsS
		addWhileCollisionListener( new WhileCollisionListener<MyOgre, MyBiped>() {
			public void whileColliding( WhileCollisionEvent<MyOgre,MyBiped> event ) {
//				System.out.println("whileColliding");
				arr[ 16 ] = true;
			}
		}, MyOgre.class, MyBiped.class );
		addWhileInViewListener( new WhileInViewListener<MyOgre>() {
			public void whileInView( WhileInViewEvent<MyOgre> e ) {
//				System.out.println("whileInView");
				arr[ 17 ] = true;
			}
		}, MyOgre.class );
		addWhileOcclusionListener( new WhileOcclusionListener<MyOgre, MyBiped>() {

			public void whileOccluding( WhileOccludingEvent<MyOgre,MyBiped> e ) {
				arr[ 18 ] = true;
			}
		}, MyOgre.class, MyBiped.class );
		addWhileProximityListener( new WhileProximityListener<MyOgre,MyBiped>() {
			public void whileClose( WhileProximityEvent<MyOgre,MyBiped> e ) {
//				System.out.println("whileProximity");
				arr[ 19 ] = true;
			}
		}, MyOgre.class, MyBiped.class, 1.0 );
		//^^^^WhileEvents
		//		//		addDefaultModelManipulation();
		//		this.addSceneActivationListener( new SceneActivationListener() {
		//			public void sceneActivated( SceneActivationEvent e ) {
		//			}
		//		} );
		//		//		this.addWhileCollisionListener(new WhileCollisionListener() {
		//		//			public void timeElapsed(TimerEvent e) {
		//		//				susan.move( MoveDirection.UP, 1.0 );
		//		//				susan.move( MoveDirection.DOWN, 1.0 );
		//		//			}
		//		//		}, colListOne, colListTwo, AddTimerEventListener.timerFrequency(0), MultipleEventPolicy.IGNORE );
		//		//		this.addObjectMoverFor( ogre );
		//		this.addArrowKeyPressListener( new ArrowKeyPressListener() {
		//			public void arrowKeyPressed( ArrowKeyEvent e ) {
		//				if( e.isKey( Key.LEFT ) || e.isKey( Key.RIGHT ) || e.isKey( Key.UP ) || e.isKey( Key.DOWN ) ) {
		//					ogre.move( e.getMoveDirection( MoveDirectionPlane.FORWARD_BACKWARD_LEFT_RIGHT ), 1 );
		//				} else {
		//					ogre.turn( e.getTurnDirection(), .125 );
		//				}
		//			}
		//		}, org.lgna.story.MultipleEventPolicy.COMBINE );
		//		this.addMouseClickOnObjectListener( new MouseClickOnObjectListener<MyOgre>() {
		//
		//			public void mouseClicked( MouseClickOnObjectEvent<MyOgre> e ) {
		//				e.getObjectAtMouseLocation().move( MoveDirection.UP, 1 );
		//				e.getObjectAtMouseLocation().move( MoveDirection.DOWN, 1 );
		//			}
		//		}, MyOgre.class );
		//		this.addCollisionEndListener( new CollisionEndListener<MyOgre,SModel>() {
		//
		//			public void collisionEnded( EndCollisionEvent<MyOgre,SModel> e ) {
		//				e.getCollidingFromGroupA().doOgreyThing();
		//				e.getCollidingFromGroupB().turn( TurnDirection.FORWARD, 1 );
		//			}
		//		}, MyOgre.class, SModel.class );
		//		this.addProximityEnterListener( new ProximityEnterListener<MyOgre,MyBiped>() {
		//
		//			public void proximityEntered( EnterProximityEvent<MyOgre,MyBiped> e ) {
		//			}
		//		}, MyOgre.class, MyBiped.class, new Double( 1 ) );
		//		this.mattsObjectThingy = new SCone();
		//		mattsObjectThingy.setVehicle( susan );
		//		mattsObjectThingy.setBaseRadius( 1 );
		//		mattsObjectThingy.setPaint( Color.PINK );
		//		mattsObjectThingy.move( MoveDirection.LEFT, 5 );
		//		susan.setVehicle( ImplementationAccessor.getImplementation( susan ).getScene().getAbstraction() );
	}
	public void chillInSkiChalet() {
		ogre.move( MoveDirection.LEFT, 10 );
		ogre.move( MoveDirection.RIGHT, 15 );
		ogre.move( MoveDirection.LEFT, 15 );
//		while( true ) {
//			this.susan.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
//			this.susan.getLeftKnee().turn( TurnDirection.BACKWARD, 0.25 );
//			this.ogre.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
//			this.redCone.turn( TurnDirection.FORWARD, 1.0 );
//			this.blueCone.roll( RollDirection.LEFT, 1.0 );
//			this.ogre.getRightShoulder().turn( TurnDirection.LEFT, 0.25 );
//			org.lgna.common.ThreadUtilities.doTogether( new Runnable() {
//				public void run() {
//					SnowScene.this.redCone.move( MoveDirection.UP, 1.0 );
//				}
//			}, new Runnable() {
//				public void run() {
//					SnowScene.this.greenCone.move( MoveDirection.UP, 1.0 );
//				}
//			}, new Runnable() {
//				public void run() {
//					SnowScene.this.blueCone.move( MoveDirection.UP, 1.0 );
//				}
//			} );
//			this.redCone.move( MoveDirection.DOWN, 1.0, Move.duration( 0.333 ) );
//			this.greenCone.move( MoveDirection.DOWN, 1.0, Move.duration( 0.333 ) );
//			this.blueCone.move( MoveDirection.DOWN, 1.0, Move.duration( 0.333 ) );
//		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class RagsToRichesStory extends SProgram {

	private final SCamera camera = new SCamera();
	private final MyBiped susan = new MyBiped( new AdultPersonResource( Gender.FEMALE, BaseSkinTone.getRandom(),

	BaseEyeColor.getRandom(), FemaleAdultHairBraids.BLACK, 0.5, FemaleAdultFullBodyOutfitAmbulanceDriver.BLUE ) );
	private final MyOgre ogre = new MyOgre( org.lgna.story.resources.biped.OgreResource.GREEN );
	private final DesertScene desertScene = new DesertScene( camera, ogre );
	private final SnowScene snowScene = new SnowScene( camera, ogre, susan );

	public void playOutStory() {
		//		this.setActiveScene( this.desertScene );
		//		this.desertScene.turnBigRocksIntoLittleRocks();
		this.setActiveScene( this.snowScene );
		this.snowScene.chillInSkiChalet();
	}
	public static void main( final String[] args ) {
		final RagsToRichesStory ragsToRichesStory = new RagsToRichesStory();
		ragsToRichesStory.initializeInFrame( args );
		new Thread() {
			@Override
			public void run() {
				ragsToRichesStory.playOutStory();
			}
		}.start();
		Logger.todo( "remove this EPIC HACK" );
		new Thread() {
			@Override
			public void run() {
				ThreadUtilities.sleep( 250 );
				Component awtComponent = ImplementationAccessor.getImplementation( ragsToRichesStory ).getOnscreenLookingGlass().getAWTComponent();
				edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getRootFrame( awtComponent ).setSize( 1024, 768 );
			}
		}.start();

	}
}
