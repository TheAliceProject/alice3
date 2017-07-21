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
package org.lgna.story.implementation;

import java.util.ArrayList;
import java.util.List;

import org.lgna.story.BipedPose;
import org.lgna.story.BipedPoseBuilder;
import org.lgna.story.EmployeesOnly;
import org.lgna.story.FlyerPose;
import org.lgna.story.FlyerPoseBuilder;
import org.lgna.story.Pose;
import org.lgna.story.PoseBuilder;
import org.lgna.story.QuadrupedPose;
import org.lgna.story.QuadrupedPoseBuilder;
import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.SQuadruped;
import org.lgna.story.resources.BipedResource;
import org.lgna.story.resources.FlyerResource;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.QuadrupedResource;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.UnitQuaternion;

/**
 * @author Matt May
 */
public class PoseUtilities {

	public static <M extends SJointedModel, P extends Pose<M>, B extends PoseBuilder<M, P>> B createBuilderForPoseClass( Class<P> cls ) {
		Class<B> poseBuilderCls = getBuilderClassForPoseClass( cls );
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( poseBuilderCls );
	}

	public static <M extends SJointedModel, P extends Pose<M>> Class<P> getPoseClassForModelClass( Class<M> cls ) {
		if( SBiped.class.isAssignableFrom( cls ) ) {
			return (Class<P>)BipedPose.class;
		} else if( SQuadruped.class.isAssignableFrom( cls ) ) {
			return (Class<P>)QuadrupedPose.class;
		} else if( SFlyer.class.isAssignableFrom( cls ) ) {
			return (Class<P>)FlyerPose.class;
		} else {
			throw new RuntimeException( "Unhandled Model Type: " + cls );
		}
	}

	public static <M extends SJointedModel> Class<? extends PoseBuilder<?, ?>> getBuilderClassForModelClass( Class<M> cls ) {
		if( SBiped.class.isAssignableFrom( cls ) ) {
			return BipedPoseBuilder.class;
		} else if( SQuadruped.class.isAssignableFrom( cls ) ) {
			return QuadrupedPoseBuilder.class;
		} else if( SFlyer.class.isAssignableFrom( cls ) ) {
			return FlyerPoseBuilder.class;
		} else {
			throw new RuntimeException( "Unhandled Model Type: " + cls );
		}
	}

	public static <M extends SJointedModel, P extends Pose<M>, B extends PoseBuilder<M, P>> Class<B> getBuilderClassForPoseClass( Class<P> cls ) {
		if( BipedPose.class.isAssignableFrom( cls ) ) {
			return (Class<B>)BipedPoseBuilder.class;
		} else if( QuadrupedPose.class.isAssignableFrom( cls ) ) {
			return (Class<B>)QuadrupedPoseBuilder.class;
		} else if( FlyerPose.class.isAssignableFrom( cls ) ) {
			return (Class<B>)FlyerPoseBuilder.class;
		} else {
			throw new RuntimeException( "Unhandled Pose Type: " + cls );
		}
	}

	public static <M extends SJointedModel> void applyToJointedModel( Pose<M> pose, M model ) {
		for( JointIdTransformationPair jtPair : EmployeesOnly.getJointIdTransformationPairs( pose ) ) {
			if( jtPair.orientationOnly() ) {
				setOrientationOnly( model.getJoint( jtPair.getJointId() ), jtPair.getTransformation().orientation );
			} else {
				setTransformationOnJoint( model.getJoint( jtPair.getJointId() ), jtPair.getTransformation() );
			}
		}
	}

	public static void setTransformationOnJoint( SJoint sJoint, AffineMatrix4x4 transformation ) {
		( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).setLocalTransformation( transformation );
	}

	public static void setOrientationOnly( SJoint sJoint, UnitQuaternion unitQuaternion ) {
		setOrientationOnly( sJoint, new OrthogonalMatrix3x3( unitQuaternion ) );
	}

	public static void setOrientationOnly( SJoint sJoint, OrthogonalMatrix3x3 orientation ) {
		( (JointImp)org.lgna.story.EmployeesOnly.getImplementation( sJoint ) ).setLocalOrientation( orientation );
	}

	public static <R extends JointedModelResource> Class<R> getResourceClassFromModelClass( Class<? extends SJointedModel> modelCls ) {
		if( SBiped.class.isAssignableFrom( modelCls ) ) {
			return (Class<R>)BipedResource.class;
		} else if( SQuadruped.class.isAssignableFrom( modelCls ) ) {
			return (Class<R>)QuadrupedResource.class;
		} else if( SFlyer.class.isAssignableFrom( modelCls ) ) {
			return (Class<R>)FlyerResource.class;
		} else {
			throw new RuntimeException( "Unhandled Model Type: " + modelCls );
		}
	}

	public static <T extends SJointedModel> Pose<T> createPoseFromT( T model ) {
		return createPoseFromT( model, getDefaultJoints( getResourceClassFromModelClass( model.getClass() ) ) );
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
		List<JointIdTransformationPair> list = Lists.newArrayList();
		for( JointId id : arr ) {
			JointImp implementation = EmployeesOnly.getImplementation( model.getJoint( id ) );
			list.add( new JointIdTransformationPair( id, implementation.getLocalOrientation().createUnitQuaternion() ) );
		}
		for( JointIdTransformationPair key : list ) {
			EmployeesOnly.addJointIdTransformationPair( builder, key );
		}
		return (Pose<T>)builder.build();
	}

	public static JointId[] getDefaultJoints( Class<? extends JointedModelResource> resourceClass ) {
		ArrayList<JointId> rv = Lists.newArrayList();
		JointId[] roots = getJointIdRoots( resourceClass );
		for( JointId id : roots ) {
			rv.addAll( tunnel( id, resourceClass ) );
		}
		return rv.toArray( new JointId[ 0 ] );
	}

	protected static JointId[] getJointIdRoots( Class<? extends JointedModelResource> resourceClass ) {
		if( BipedResource.class.isAssignableFrom( resourceClass ) ) {
			return BipedResource.JOINT_ID_ROOTS;
		} else if( QuadrupedResource.class.isAssignableFrom( resourceClass ) ) {
			return QuadrupedResource.JOINT_ID_ROOTS;
		} else if( FlyerResource.class.isAssignableFrom( resourceClass ) ) {
			return FlyerResource.JOINT_ID_ROOTS;
		} else {
			throw new RuntimeException( "Unhandled Resoruce Type: " + resourceClass );
		}
	}

	protected static ArrayList<JointId> tunnel( JointId id, Class<? extends JointedModelResource> resource ) {
		ArrayList<JointId> rv = Lists.newArrayList( id );
		for( JointId child : id.getChildren( resource ) ) {
			rv.addAll( tunnel( child, resource ) );
		}
		return rv;
	}

	public static Class<? extends PoseBuilder> getBuilderClassForModelResourceClass( Class<? extends JointedModelResource> resourceCls ) {
		if( BipedResource.class.isAssignableFrom( resourceCls ) ) {
			return BipedPoseBuilder.class;
		} else if( QuadrupedResource.class.isAssignableFrom( resourceCls ) ) {
			return QuadrupedPoseBuilder.class;
		} else if( FlyerResource.class.isAssignableFrom( resourceCls ) ) {
			return FlyerPoseBuilder.class;
		} else {
			return null;
		}
	}

	public static java.lang.reflect.Method getSpecificPoseBuilderMethod( Class<? extends PoseBuilder> poseBuilderCls, JointId jointId ) {
		java.lang.reflect.Field jField = jointId.getPublicStaticFinalFld();
		if( jField != null ) {
			String fieldName = jField.getName();
			String camelCaseName = org.lgna.story.implementation.alice.AliceResourceUtilties.enumToCamelCase( fieldName, true );
			try {
				return poseBuilderCls.getMethod( camelCaseName, org.lgna.story.Orientation.class );
			} catch( NoSuchMethodException nsme ) {
				//not a problem
			}
		}
		return null;
	}

	public static java.lang.reflect.Method getCatchAllPoseBuilderMethod( Class<? extends PoseBuilder> poseBuilderCls ) {
		java.lang.reflect.Method rv = null;
		for( java.lang.reflect.Method jMethod : poseBuilderCls.getMethods() ) {
			if( jMethod.getReturnType().equals( poseBuilderCls ) ) {
				Class<?>[] jParameterTypes = jMethod.getParameterTypes();
				if( jParameterTypes.length == 2 ) {
					if( jParameterTypes[ 0 ].equals( JointId.class ) && jParameterTypes[ 1 ].equals( org.lgna.story.Orientation.class ) ) {
						if( rv != null ) {
							throw new RuntimeException( "unexpected methods found when only one should have catch all signature: " + rv.getName() + " " + jMethod.getName() );
						} else {
							rv = jMethod;
						}
					}
				}
			}
		}
		return rv;
	}
}
