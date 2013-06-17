/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.lgna.ik.poser;

import java.util.ArrayList;
import java.util.UUID;

import org.lgna.croquet.FrameComposite;
import org.lgna.croquet.SplitComposite;
import org.lgna.croquet.components.SplitPane;
import org.lgna.ik.walkandtouch.PoserScene;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserType;
import org.lgna.story.MoveDirection;
import org.lgna.story.SBiped;
import org.lgna.story.SCamera;
import org.lgna.story.TurnDirection;

import test.ik.croquet.SceneComposite;

/**
 * @author Matt May
 */
public abstract class AbstractPoserFrameComposite extends FrameComposite<SplitPane> {

	private IkPoser poser;

	private final SCamera camera = new SCamera();
	private final SBiped biped;
	private final PoserScene scene;
	private UserType<?> userType;

	private final SplitComposite splitComposite;

	//note: this relies on very specific ordering at the moment
	protected abstract AbstractPoserControlComposite<?> createControlComposite();

	protected AbstractPoserFrameComposite( NamedUserType userType, UUID uuid ) {
		super( uuid, null );
		this.biped = deriveBipedFromUserType( userType );
		this.scene = new PoserScene( camera, biped );
		this.userType = userType;
		this.poser = new IkPoser();
		this.splitComposite = createHorizontalSplitComposite( this.createControlComposite(), test.ik.croquet.SceneComposite.getInstance(), 0.5 );
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		SceneComposite.getInstance().getView().initializeInAwtContainer( poser );
		initializeTest();
		getBiped().move( MoveDirection.UP, 1 );
		getBiped().move( MoveDirection.DOWN, 1 );
	}

	//	public InternalMenuItemPrepModel getMenuItemPrepModel() {
	//		return composite.getBooleanState().getMenuItemPrepModel();
	//	}

	private SBiped deriveBipedFromUserType( NamedUserType type ) {

		org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();

		org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.OgreResource.BROWN;
		//org.lgna.story.resources.BipedResource bipedResource = org.lgna.story.resources.biped.AlienResource.DEFAULT;

		org.lgna.project.ast.NamedUserConstructor userConstructor = type.constructors.get( 0 );
		final int N = userConstructor.requiredParameters.size();
		Object[] arguments = new Object[ N ];
		switch( N ) {
		case 0:
			break;
		case 1:
			arguments[ 0 ] = bipedResource;
			break;
		case 2:
			assert false : N;
		}
		org.lgna.project.virtualmachine.UserInstance userInstance = vm.ENTRY_POINT_createInstance( type, arguments );
		return userInstance.getJavaInstance( SBiped.class );
	}

	public PoserScene getScene() {
		return this.scene;
	}

	public SBiped getBiped() {
		return this.biped;
	}

	public void initializeTest() {
		this.poser.setActiveScene( this.scene );
		this.camera.turn( TurnDirection.RIGHT, .5 );
		this.camera.move( MoveDirection.BACKWARD, 8 );
		this.camera.move( MoveDirection.UP, 1 );
	}

	public ArrayList<JointSelectionSphere> getJointSelectionSheres() {
		return scene.getJointSelectionSheres();
	}

	public void setAdapter( PoserControllerAdapter adapter ) {
		scene.setAdapter( adapter );
	}

	public Pose getPose() {
		return Pose.createPoseFromBiped( biped );
	}

	public UserType<?> getDeclaringType() {
		return userType;
	}

	public IkPoser getProgram() {
		return this.poser;
	}

	@Override
	protected SplitPane createView() {
		return this.splitComposite.getView();
	}
}
