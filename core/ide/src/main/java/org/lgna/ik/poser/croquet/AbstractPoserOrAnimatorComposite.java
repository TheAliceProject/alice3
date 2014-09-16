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
package org.lgna.ik.poser.croquet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.lgna.croquet.SplitComposite;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.SplitPane;
import org.lgna.ik.poser.FieldFinder;
import org.lgna.ik.poser.animation.composites.AbstractPoserControlComposite;
import org.lgna.ik.poser.controllers.PoserControllerAdapter;
import org.lgna.ik.poser.jselection.JointSelectionSphere;
import org.lgna.ik.poser.scene.AbstractPoserScene;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserParameter;
import org.lgna.story.Pose;
import org.lgna.story.SJointedModel;
import org.lgna.story.SProgram;
import org.lgna.story.StraightenOutJoints;
import org.lgna.story.event.PointOfViewChangeListener;
import org.lgna.story.event.PointOfViewEvent;
import org.lgna.story.implementation.PoseUtilities;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

import test.ik.croquet.SceneComposite;
import edu.cmu.cs.dennisc.java.util.Lists;

/**
 * @author Matt May
 */
public abstract class AbstractPoserOrAnimatorComposite<T extends AbstractPoserControlComposite, M extends SJointedModel> extends org.lgna.croquet.SimpleComposite<org.lgna.croquet.views.Panel> {

	private final SProgram storyProgram;

	private final AbstractPoserScene scene;
	private final T controlComposite;
	private final List<JointId> usedJoints = Lists.newArrayList();
	private M model;
	private NamedUserType declaringType;

	private final SplitComposite splitComposite;
	private final SceneComposite sceneComposite;

	private boolean isInitialized = false;
	private final java.util.List<StatusUpdateListener> statusUpdateListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();

	protected AbstractPoserOrAnimatorComposite( UUID migrationId, NamedUserType userType ) {
		super( migrationId );
		this.setType( userType );
		this.scene = initScene();
		this.storyProgram = new SProgram();
		controlComposite = this.createControlComposite();
		sceneComposite = new SceneComposite();
		sceneComposite.getView().setMinimumPreferredHeight( 400 );
		sceneComposite.getView().setMinimumPreferredWidth( ( 400 * 16 ) / 9 );
		this.splitComposite = createHorizontalSplitComposite( controlComposite, sceneComposite, 0.25 );
	}

	//note: this relies on very specific ordering at the moment
	protected abstract T createControlComposite();

	private PointOfViewChangeListener transformationListener = new PointOfViewChangeListener() {

		@Override
		public void pointOfViewChanged( PointOfViewEvent e ) {
			JointId jointId = ( (JointSelectionSphere)e.getEntity() ).getJoint().getJointId();
			if( !usedJoints.contains( jointId ) ) {
				usedJoints.add( jointId );
			}
			for( StatusUpdateListener listener : statusUpdateListeners ) {
				listener.refreshStatus();
			}
		}

	};

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		sceneComposite.getView().initializeInAwtContainer( storyProgram );
		initializeScene();
		//these must be added after scene gets initialized!!! (mmay)
		scene.addPointOfViewChangeListener( transformationListener, (JointSelectionSphere[])scene.getJointSelectionSpheres().toArray( new JointSelectionSphere[ 0 ] ) );
	}

	private M createInstanceFromType( NamedUserType type ) {
		org.lgna.project.virtualmachine.ReleaseVirtualMachine vm = new org.lgna.project.virtualmachine.ReleaseVirtualMachine();

		org.lgna.project.ast.NamedUserConstructor userConstructor = type.constructors.get( 0 );
		final int N = userConstructor.requiredParameters.size();
		Object[] arguments = new Object[ N ];
		switch( N ) {
		case 0:
			break;
		case 1:
			UserParameter constructorParameter0 = userConstructor.requiredParameters.get( 0 );
			AbstractType<?, ?, ?> parameter0Type = constructorParameter0.getValueType();
			ArrayList<JointedModelResource> resourceList = FieldFinder.getInstance().getResourcesForType( type );
			if( parameter0Type instanceof org.lgna.project.ast.JavaType ) {
				org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)parameter0Type;
				Class<?> cls = javaType.getClassReflectionProxy().getReification();
				if( cls.isEnum() ) {
					arguments[ 0 ] = cls.getEnumConstants()[ 0 ];
				} else {
					arguments[ 0 ] = resourceList.get( 0 );
				}
			} else {
				arguments[ 0 ] = resourceList.get( 0 );
			}
			break;
		case 2:
			assert false : N;
		}
		org.lgna.project.virtualmachine.UserInstance userInstance = vm.ENTRY_POINT_createInstance( type, arguments );
		return userInstance.getJavaInstance( getModelClass() );
	}

	protected abstract AbstractPoserScene initScene();

	public abstract Class<M> getModelClass();

	private void initializeScene() {
		if( !isInitialized ) {
			this.storyProgram.setActiveScene( this.scene );
			this.scene.pointCamera();
			isInitialized = true;
			scene.addPointOfViewChangeListener( transformationListener, (JointSelectionSphere[])scene.getJointSelectionSpheres().toArray( new JointSelectionSphere[ 0 ] ) );
		} else {
			this.scene.pointCamera();
		}
	}

	public Collection<JointSelectionSphere> getJointSelectionSheres() {
		return scene.getJointSelectionSpheres();
	}

	public void setAdapter( PoserControllerAdapter adapter ) {
		scene.setAdapter( adapter );
	}

	public Pose getPose() {
		return PoseUtilities.createPoseFromT( getModel(), usedJoints.toArray( new JointId[ 0 ] ) );
	}

	public void strikePose( Pose pose ) {
		if( pose != null ) {
			PoseUtilities.applyToJointedModel( pose, getModel() );
		} else {
			getModel().straightenOutJoints( StraightenOutJoints.duration( 0 ) );
		}
	}

	public NamedUserType getDeclaringType() {
		return declaringType;
	}

	public SProgram getProgram() {
		return this.storyProgram;
	}

	@Override
	protected Panel createView() {
		org.lgna.croquet.views.BorderPanel rv = new org.lgna.croquet.views.BorderPanel();
		SplitPane view = splitComposite.getView();
		rv.addCenterComponent( view );
		return rv;
	}

	public T getControlComposite() {
		return this.controlComposite;
	}

	public M getModel() {
		return this.model;
	}

	public AbstractPoserScene getScene() {
		return scene;
	}

	public List<JointId> getUsedJoints() {
		return this.usedJoints;
	}

	public void setType( NamedUserType type ) {
		this.declaringType = type;
		this.model = createInstanceFromType( type );
		assert model != null : type;
		if( this.scene != null ) {
			scene.setNewModel( model );
		}
	}

	public void addStatusListener( StatusUpdateListener statusUpdateListener ) {
		this.statusUpdateListeners.add( statusUpdateListener );
	}

	public void removeStatusListener( StatusUpdateListener statusUpdateListener ) {
		this.statusUpdateListeners.remove( statusUpdateListener );
	}
}
