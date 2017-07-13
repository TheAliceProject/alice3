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

package edu.cmu.cs.dennisc.nebulous.javabased;

import java.io.File;

import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.Color;
import org.lgna.story.SCone;
import org.lgna.story.SGround;
import org.lgna.story.SModel;
import org.lgna.story.MoveDirection;
import org.lgna.story.SProgram;
import org.lgna.story.RollDirection;
import org.lgna.story.SScene;
import org.lgna.story.SSphere;
import org.lgna.story.SSwimmer;
import org.lgna.story.TurnDirection;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.SwimmerResource;

import edu.cmu.cs.dennisc.scenegraph.Geometry;


abstract class CustomPerson extends SBiped {
	public CustomPerson( BipedResource resource ) {
		super( resource );
	}
}

class CustomAdult extends CustomPerson {
	public CustomAdult( BipedResource resource ) {
		super( resource );
	}
}

class CustomFish extends SSwimmer {
	public CustomFish( SwimmerResource resource ) {
		super( resource );
	}
}

class SceneTest extends SScene {
	private final SGround desert = new SGround();
//	private final CustomAdult fellowLaborer = new CustomAdult( org.lgna.story.resources.people.Ogre.BEAST_DIFFUSE );
	private final CustomAdult fellowLaborer = null;
	private final SCamera camera;
	private final SModel ogre;
	public SceneTest( SCamera camera, SModel ogre ) {
		this.camera = camera;
		this.ogre = ogre;
	}
	private void performGeneratedSetup() {
		// this code is automatically generated
		// edit performCustomSetup instead
//		this.desert.setVehicle( this );
		this.camera.setVehicle( this );
		this.ogre.setVehicle( this );
//		this.fellowLaborer.setVehicle( this );

//		this.ogre.move( MoveDirection.LEFT, 1.0 );
//		this.fellowLaborer.move( MoveDirection.RIGHT, 1.0 );
		
//		this.desert.setAppearance( SGround.SurfaceAppearance.SAND );
		this.camera.moveTo(ogre);
		this.camera.move(MoveDirection.BACKWARD, 3);
	}
	private void performCustomSetup() {
	}
	@Override
	protected void handleActiveChanged( Boolean isActive, Integer activeCount ) {
//		if( isActive ) {
//			if( activeCount == 1 ) {
//				this.performGeneratedSetup();
//				this.performCustomSetup();
//			} else {
//				this.restoreVehiclesAndVantagePoints();
//			}
//		} else {
//			this.preserveVehiclesAndVantagePoints();
//		}
	}
		
}

/**
 * @author Dennis Cosgrove
 */
class TestScene extends SProgram {
	private final SCamera camera = new SCamera();
	private final CustomFish ogre = null;
	private final SceneTest testScene = new SceneTest( camera, ogre );
	public void playOutStory() {
		this.setActiveScene( this.testScene );
	}
	public static void main( String[] args ) {
		TestScene testProgram = new TestScene();
		testProgram.initializeInFrame( args );
		testProgram.playOutStory();
	}
}