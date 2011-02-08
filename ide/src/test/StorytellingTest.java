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

import org.lookingglassandalice.storytelling.*;

abstract class CustomPerson extends Person {
	public CustomPerson( PersonResource resource ) {
		super( resource );
	}
}

class CustomAdult extends CustomPerson {
	public CustomAdult( AdultPersonResource resource ) {
		super( resource );
	}
}

class SnowScene extends Scene {
	private final Sun sun = new Sun();
	private final Ground snow = new Ground();
	private final Camera camera;
	private final CustomAdult susan;
	public SnowScene( Camera camera, CustomAdult susan ) {
		this.camera = camera;
		this.susan = susan;
		this.addEntity( this.sun );
		this.addEntity( this.snow );
		this.addEntity( this.camera );
		this.addEntity( this.susan );
	}
	
	private void performGeneratedSetup() {
		this.snow.setAppearance( Ground.Appearance.SNOW );
	}
	private void performCustomSetup() {
	}
	
	@Override
	protected void handleActivation( int count ) {
		if( count == 1 ) {
			this.performGeneratedSetup();
			this.performCustomSetup();
		} else {
			this.restorePointsOfView();
		}
	}
	@Override
	protected void handleDeactivation( int count ) {
		this.preservePointsOfView();
	}
}

class DesertScene extends Scene {
	private final Sun sun = new Sun();
	private final Ground desert = new Ground();
	private final Sphere sphere = new Sphere();
	private final Camera camera;
	public DesertScene( Camera camera ) {
		this.camera = camera;
		this.addEntity( this.sun );
		this.addEntity( this.desert );
		this.addEntity( this.sphere );
		this.addEntity( this.camera );
	}
	private void performGeneratedSetup() {
		this.desert.setAppearance( Ground.Appearance.SAND );
		this.sphere.setRadius( 0.1 );
		this.sphere.setColor( Color.RED );
	}
	private void performCustomSetup() {
	}
	@Override
	protected void handleActivation( int count ) {
		if( count == 1 ) {
			this.performGeneratedSetup();
			this.performCustomSetup();
		} else {
			this.restorePointsOfView();
		}
	}
	@Override
	protected void handleDeactivation( int count ) {
		this.preservePointsOfView();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class StorytellingTest {
	public static void main( String[] args ) {
		Camera camera = new Camera();
		camera.move( MoveDirection.FORWARD, 10.0 );
		camera.turn( TurnDirection.LEFT, 0.5 );

		Sphere sphere = new Sphere();
		sphere.setRadius( 0.1 );
		sphere.setColor( Color.RED );

//		//best practice
//		sphere.move( MoveDirection.LEFT, 0.5, new MoveDetails.Builder().asSeenBy( camera ).duration( 0.5 ).build() );
//		//serves our purpose
//		sphere.turn( TurnDirection.LEFT, 0.5, new TurnDetails().asSeenBy( camera ).duration( 0.5 ) );
//		//less syntax, more magic?
		sphere.roll( RollDirection.LEFT, 0.5, RollDetailsFactory.asSeenBy( camera ).duration( 0.5 ) );
		
		CustomAdult susan = new CustomAdult( new org.lookingglassandalice.storytelling.sims2.AdultPersonResource() );

		SnowScene snowScene = new SnowScene( camera, susan );
		//DesertScene desertScene = new DesertScene( camera );

		LookingGlass lookingGlass = new LookingGlass();
		//lookingGlass.setActiveScene( desertScene );
		lookingGlass.setActiveScene( snowScene );
		lookingGlass.setVisible( true );
	}
}
