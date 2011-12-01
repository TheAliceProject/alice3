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

/**
 * @author Dennis Cosgrove
 */
import java.awt.event.MouseEvent;

import org.lgna.story.*;
import org.lgna.story.resources.sims2.AdultPersonResource;
import org.lgna.story.resources.sims2.BaseEyeColor;
import org.lgna.story.resources.sims2.BaseSkinTone;
import org.lgna.story.resources.sims2.FemaleAdultFullBodyOutfitAmbulanceDriver;
import org.lgna.story.resources.sims2.FemaleAdultHairBraids;
import org.lgna.story.resources.sims2.Gender;

class TestScene extends Scene {
	private final Sun sun = new Sun();
	private final Ground snow = new Ground();
	private final Camera camera;
	private final Biped susan;
	private final ObjectMarker marker;
	public TestScene( Camera camera, MyBiped susan, ObjectMarker marker ) {
		this.camera = camera;
		this.susan = susan;
		this.marker = marker;
	}
	
	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
		//this.snow.setVehicle( this );
		this.sun.setVehicle( this );
		this.camera.setVehicle( this );
		this.susan.setVehicle( this );
		this.marker.setVehicle( this );
		this.susan.turn( TurnDirection.LEFT, 0.25 );
		this.snow.setPaint( Ground.SurfaceAppearance.SNOW );
		this.camera.moveAndOrientToAGoodVantagePointOf( this.susan );
		//this.camera.move( MoveDirection.UP, .8);
		this.camera.move( MoveDirection.FORWARD, 2 );
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

	public void test() {
		org.lgna.story.implementation.JointedModelImp imp = ImplementationAccessor.getImplementation( this.susan );
		imp.showVisualization();
		while( true ) {
			this.susan.getRightShoulder().roll( RollDirection.LEFT, 0.25 );
//			this.susan.getLeftKnee().turn( TurnDirection.BACKWARD, 0.25 );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
class TestJointedModel extends Program {
	private final Camera camera = new Camera();
	private final MyBiped susan = new MyBiped( 
			new AdultPersonResource(
					Gender.FEMALE,
					BaseSkinTone.getRandom(),
					BaseEyeColor.getRandom(),
					FemaleAdultHairBraids.BLACK,
					0.5,
					FemaleAdultFullBodyOutfitAmbulanceDriver.BLUE
	) );
	private final ObjectMarker marker = new ObjectMarker();
	private final TestScene testScene = new TestScene( camera, susan, marker );
	
	private int previousX = -1;
	private int previousY = -1;
	
	public void test() {
		final edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass lg = ImplementationAccessor.getImplementation( this ).getOnscreenLookingGlass();
		
		lg.getAWTComponent().addMouseListener(new java.awt.event.MouseListener() {
			public void mousePressed(MouseEvent e) {
				previousX = e.getX();
				previousY = e.getY();
			}
			public void mouseClicked(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {
				previousX = -1;
				previousY = -1;
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			
		});
		lg.getAWTComponent().addMouseMotionListener( new java.awt.event.MouseMotionListener() {
			
			public void mouseMoved(java.awt.event.MouseEvent e) {
				edu.cmu.cs.dennisc.lookingglass.PickResult pickResult = lg.getPicker().pickFrontMost( e.getX(), e.getY(), edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED );
				org.lgna.story.implementation.EntityImp entityImp = org.lgna.story.implementation.EntityImp.getInstance( pickResult.getVisual() );
				//System.out.println( pickResult.getSubElement() + " " + entityImp );
			}
			
			
			public void mouseDragged(java.awt.event.MouseEvent e) {
				if (previousX != -1 && previousY != -1) {
					int xDif = e.getX() - previousX;
					int yDif = e.getY() - previousY;
					if (xDif != 0) {
						camera.turn(TurnDirection.RIGHT, xDif*.002, Turn.duration(0), Turn.asSeenBy(testScene));
					}
					if (yDif != 0) {
						marker.turnToFace(camera, TurnToFace.duration(0));;
						camera.turn(TurnDirection.BACKWARD, yDif*.002, Turn.duration(0), Turn.asSeenBy(marker));
					}
				}
				previousX = e.getX();
				previousY = e.getY();
			}
		} );
		this.setActiveScene( this.testScene );
		this.testScene.test();
	}
	public static void main( String[] args ) {
		TestJointedModel test = new TestJointedModel();
		test.initializeInFrame( args );
		test.test();
	}
}
