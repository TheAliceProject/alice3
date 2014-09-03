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
package org.lgna.ik.core.pose;

import java.util.ArrayList;
import java.util.List;

import org.lgna.ik.core.pose.builder.BipedPoseBuilder;
import org.lgna.ik.core.pose.builder.FlyerPoseBuilder;
import org.lgna.ik.core.pose.builder.PoseBuilder;
import org.lgna.ik.core.pose.builder.QuadrupedPoseBuilder;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.SQuadruped;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */
public abstract class Pose<S extends SJointedModel> {

	private final Class<S> cls;
	private final JointKey[] jointQPairs;
	private final Class<? extends JointedModelResource> defaultResource;

	public Pose( Class<S> cls, Class<? extends JointedModelResource> resource, JointKey... pairs ) {
		this.jointQPairs = pairs;
		this.cls = cls;
		this.defaultResource = resource;
	}

	public JointKey[] getJointKeys() {
		return this.jointQPairs;
	}

	public JointId[] getDefaultJoints() {
		ArrayList<JointId> rv = Lists.newArrayList();
		JointId[] roots = getJointIdRoots();
		for( JointId id : roots ) {
			rv.addAll( tunnel( id ) );
		}
		return rv.toArray( new JointId[ 0 ] );
	}

	protected abstract JointId[] getJointIdRoots();

	protected static JointId getIDFor( SJoint sJoint ) {
		return ( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).getJointId();
	}

	protected static AffineMatrix4x4 getOrientation( SJoint sJoint ) {
		return ( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).getLocalTransformation();
	}

	protected static UnitQuaternion getUnitQuaternion( SJoint sJoint ) {
		return new UnitQuaternion( ( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).getLocalTransformation().orientation );
	}

	protected static void setOrientationOnly( SJoint sJoint, OrthogonalMatrix3x3 matrix ) {
		( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).setLocalOrientation( matrix );
	}

	protected static void setOrientationOnly( SJoint sJoint, UnitQuaternion unitQuaternion ) {
		( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).setLocalOrientation( new OrthogonalMatrix3x3( unitQuaternion ) );
	}

	protected static edu.cmu.cs.dennisc.math.UnitQuaternion getQuaternion( org.lgna.story.Orientation orientation ) {
		//todo: org.lgna.story.Orientation should store the UnitQuaternion?
		return org.lgna.story.EmployeesOnly.getOrthogonalMatrix3x3( orientation ).createUnitQuaternion();
	}

	public abstract PoseBuilder<S, Pose<S>> getBuilder();

	public void applyToJointedModel( S model ) {
		for( JointKey key : jointQPairs ) {
			setOrientationOnly( model.getJoint( key.getJointId() ), new UnitQuaternion( key.getOrientation().createOrthogonalMatrix3x3() ) );
		}
	}

	protected ArrayList<JointId> tunnel( JointId id ) {
		ArrayList<JointId> rv = Lists.newArrayList( id );
		for( JointId child : id.getChildren( defaultResource ) ) {
			rv.addAll( tunnel( child ) );
		}
		return rv;
	}

	public Class<S> getPosingType() {
		return cls;
	}

	public static <T extends SJointedModel> Pose<T> createPoseFromT( T model ) {
		if( model instanceof SBiped ) {
			return createPoseFromT( model, new BipedPose().getDefaultJoints() );
		} else if( model instanceof SQuadruped ) {
			return createPoseFromT( model, new QuadrupedPose().getDefaultJoints() );
		} else if( model instanceof SFlyer ) {
			return createPoseFromT( model, new FlyerPose().getDefaultJoints() );
		}
		return null;
	}

	public static <T extends SJointedModel> Pose<T> createPoseFromT( T model, JointId[] arr ) {

		PoseBuilder<?, ?> builder;
		if( model instanceof SBiped ) {
			builder = new BipedPoseBuilder();
		} else if( model instanceof SQuadruped ) {
			builder = new QuadrupedPoseBuilder();
		} else if( model instanceof SFlyer ) {
			builder = new FlyerPoseBuilder();
		} else {
			return null;
		}
		List<JointKey> list = Lists.newArrayList();
		for( JointId id : arr ) {
			JointImp implementation = EmployeesOnly.getImplementation( model.getJoint( id ) );
			list.add( new JointKey( implementation.getLocalOrientation(), id ) );
		}
		for( JointKey key : list ) {
			builder.addJointKey( key );
		}
		return (Pose<T>)builder.build();
	}
}
