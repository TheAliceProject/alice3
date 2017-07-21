/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

package org.lgna.story.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.lgna.ik.core.solver.Bone.Direction;
import org.lgna.story.SJoint;
import org.lgna.story.SJointedModel;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointedModelImp<A extends org.lgna.story.SJointedModel, R extends org.lgna.story.resources.JointedModelResource> extends ModelImp {
	public static interface VisualData<R extends org.lgna.story.resources.JointedModelResource> {
		public edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals();

		public edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgAppearances();

		public double getBoundingSphereRadius();

		public void setSGParent( edu.cmu.cs.dennisc.scenegraph.Composite parent );

		public edu.cmu.cs.dennisc.scenegraph.Composite getSGParent();
	}

	public static interface JointImplementationAndVisualDataFactory<R extends org.lgna.story.resources.JointedModelResource> {
		public R getResource();

		public JointImp createJointImplementation( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImplementation, org.lgna.story.resources.JointId jointId );

		public boolean hasJointImplementation( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImplementation, org.lgna.story.resources.JointId jointId );

		public JointImp[] createJointArrayImplementation( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImplementation, org.lgna.story.resources.JointArrayId jointArrayId );

		public VisualData createVisualData();

		public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId );

		public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalJointTransformation( org.lgna.story.resources.JointId jointId );

		public JointImplementationAndVisualDataFactory<R> getFactoryForResource( R resource );
	}

	private static class JointImpWrapper extends JointImp {
		public JointImpWrapper( JointedModelImp<?, ?> jointedModelImp, JointImp joint ) {
			super( jointedModelImp );
			this.internalJointImp = joint;

		}

		@Override
		public final void setAbstraction( SJoint abstraction ) {
			super.setAbstraction( abstraction );
			if( this.internalJointImp != null ) {
				this.internalJointImp.setAbstraction( abstraction );
			}
		}

		@Override
		public org.lgna.story.implementation.SceneImp getScene() {
			return this.internalJointImp.getScene();
		}

		@Override
		public JointId getJointId() {
			return internalJointImp.getJointId();
		}

		@Override
		public boolean isFreeInX() {
			return internalJointImp.isFreeInX();
		}

		@Override
		public boolean isFreeInY() {
			return internalJointImp.isFreeInY();
		}

		@Override
		public boolean isFreeInZ() {
			return internalJointImp.isFreeInZ();
		}

		public void replaceWithJoint( JointImp newJoint, edu.cmu.cs.dennisc.math.UnitQuaternion originalRotation ) {
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 currentRotation = this.getLocalOrientation();
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 invertedOriginal = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			invertedOriginal.setValue( originalRotation );
			invertedOriginal.invert();
			edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 dif = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
			dif.setToMultiplication( invertedOriginal, currentRotation );
			if( !dif.isWithinReasonableEpsilonOfIdentity() ) {
				edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3 newRotation = new edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3();
				newRotation.setToMultiplication( newJoint.getLocalOrientation(), dif );
				newJoint.setLocalOrientation( newRotation );
			}
			if( this.getAbstraction() != null ) {
				newJoint.setAbstraction( this.getAbstraction() );
			}
			for( edu.cmu.cs.dennisc.scenegraph.Component child : this.internalJointImp.getSgComposite().getComponents() ) {
				if( !( child instanceof edu.cmu.cs.dennisc.scenegraph.ModelJoint ) ) {
					child.setParent( newJoint.getSgComposite() );

				}
			}
			this.internalJointImp = newJoint;
		}

		@Override
		public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
			return internalJointImp.getAxisAlignedMinimumBoundingBox( asSeenBy );
		}

		@Override
		public AbstractTransformable getSgComposite() {
			return internalJointImp.getSgComposite();
		}

		@Override
		protected CumulativeBound updateCumulativeBound( CumulativeBound rv, AffineMatrix4x4 trans ) {
			return internalJointImp.updateCumulativeBound( rv, trans );
		}

		@Override
		public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalOrientation() {
			return internalJointImp.getOriginalOrientation();
		}

		@Override
		public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalTransformation() {
			return internalJointImp.getOriginalTransformation();
		}

		@Override
		public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getLocalTransformation() {
			return internalJointImp.getLocalTransformation();
		}

		@Override
		public void setLocalTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 transformation ) {
			internalJointImp.setLocalTransformation( transformation );
		}

		@Override
		protected void postCheckSetVehicle( org.lgna.story.implementation.EntityImp vehicle ) {
			//todo?
			internalJointImp.postCheckSetVehicle( vehicle );
		}

		@Override
		public boolean isFacing( EntityImp other ) {
			return this.internalJointImp.isFacing( other );
		}

		@Override
		public void applyTranslation( double x, double y, double z, ReferenceFrame asSeenBy ) {
			this.internalJointImp.applyTranslation( x, y, z, asSeenBy );
		}

		@Override
		public void applyRotationInRadians( edu.cmu.cs.dennisc.math.Vector3 axis, double angleInRadians, ReferenceFrame asSeenBy ) {
			this.internalJointImp.applyRotationInRadians( axis, angleInRadians, asSeenBy );
		}

		@Override
		public boolean isPivotVisible() {
			return internalJointImp.isPivotVisible();
		}

		@Override
		public void setPivotVisible( boolean isPivotVisible ) {
			internalJointImp.setPivotVisible( isPivotVisible );
		}

		private JointImp internalJointImp;
	}

	public JointedModelImp( A abstraction, JointImplementationAndVisualDataFactory<R> factory ) {
		this.abstraction = abstraction;
		this.factory = factory;
		this.visualData = this.factory.createVisualData();

		//Handle joint arrays
		//This makes sure we have JointImps or JointWrappers for the given jointIds and adds them to mapIdToJoint for future retrieval
		for( org.lgna.story.resources.JointArrayId arrayId : this.getJointArrayIds() ) {
			this.createJointImpsAsNeededForJointArrayIds( arrayId, this.mapIdToJoint, true );
		}

		org.lgna.story.resources.JointId[] rootIds = this.getRootJointIds();
		edu.cmu.cs.dennisc.scenegraph.Composite sgComposite;
		if( rootIds.length == 0 ) {
			this.sgScalable = null;
			sgComposite = this.getSgComposite();
		} else {
			final boolean isScalableDesired = false;
			if( isScalableDesired ) {
				this.sgScalable = new edu.cmu.cs.dennisc.scenegraph.Scalable();
				this.sgScalable.setParent( this.getSgComposite() );
				this.sgScalable.putBonusDataFor( ENTITY_IMP_KEY, this );
				sgComposite = this.sgScalable;
			} else {
				this.sgScalable = null;
				sgComposite = this.getSgComposite();
			}
			for( org.lgna.story.resources.JointId root : rootIds ) {
				this.createWrapperJointTree( root, this, this.mapIdToJoint );
			}
		}

		List<JointId> missingJoints = this.getMissingJoints();
		if( !missingJoints.isEmpty() ) {
			StringBuilder sb = new StringBuilder();
			String resourceName = this.getResource().getClass().getSimpleName();
			sb.append( resourceName + " missing joints: " );
			boolean first = true;
			for( JointId id : missingJoints ) {
				sb.append( ( !first ? ", " : "" ) + id.toString() );
				first = false;
			}
			throw new RuntimeException( sb.toString() );
		}

		this.visualData.setSGParent( sgComposite );

		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.visualData.getSgVisuals() ) {
			putInstance( sgVisual );
		}
		for( edu.cmu.cs.dennisc.scenegraph.SimpleAppearance sgAppearance : this.visualData.getSgAppearances() ) {
			putInstance( sgAppearance );
		}
	}

	//JointArrayId support
	//This is for implicit array support. It iterates through declared joints and puts matching ones into an joint array
	private void buildJointArrayMapHelper( org.lgna.story.resources.JointId currentJointId, org.lgna.story.resources.JointArrayId jointArrayId, List<org.lgna.story.resources.JointId> arrayList ) {
		if( jointArrayId.isMemberOf( currentJointId ) ) {
			arrayList.add( currentJointId );
		}
		//If the currentJointId is null, then use the root joints as a starting point for searching for array elements
		if( currentJointId == null ) {
			for( org.lgna.story.resources.JointId childId : this.getRootJointIds() ) {
				buildJointArrayMapHelper( childId, jointArrayId, arrayList );
			}
		} else {
			for( org.lgna.story.resources.JointId childId : currentJointId.getChildren( this.factory.getResource() ) ) {
				buildJointArrayMapHelper( childId, jointArrayId, arrayList );
			}
		}
	}

	//Makes sure we have JointImps or JointWrappers for the given jointIds and adds them to mapIdToJoint for future retrieval
	private <J extends JointImp> void createJointImpsAsNeededForJointArrayIds( org.lgna.story.resources.JointArrayId jointArrayId, java.util.Map<org.lgna.story.resources.JointId, J> jointMap, boolean makeWrappers ) {
		List<org.lgna.story.resources.JointId> jointIdList = edu.cmu.cs.dennisc.java.util.Lists.newArrayList();
		//Look for declared joints for
		this.buildJointArrayMapHelper( jointArrayId.getRoot(), jointArrayId, jointIdList );
		//If there aren't any declared joints, look for hidden joints
		if( jointIdList.isEmpty() ) {
			JointImp[] jointImpArray = this.factory.createJointArrayImplementation( this, jointArrayId );
			for( JointImp jointImp : jointImpArray ) {
				if( makeWrappers ) {
					jointMap.put( jointImp.getJointId(), (J)( new JointImpWrapper( this, jointImp ) ) );
				} else {
					jointMap.put( jointImp.getJointId(), (J)( jointImp ) );
				}
			}
		} else {
			//If the jointIdList has ids in it, then it means the array is already made up of existing JointImps, so skip making new ones
		}
		//Add a map to the jointArrayId for later lookup (see method getJointIdArray)
		this.mapArrayIdToJointIdArray.put( jointArrayId, jointIdList.toArray( new org.lgna.story.resources.JointId[ jointIdList.size() ] ) );
	}

	public org.lgna.story.resources.JointId[] getJointIdArray( org.lgna.story.resources.JointArrayId jointArrayId ) {
		return this.mapArrayIdToJointIdArray.get( jointArrayId );
	}

	//TODO: Do we need this? Why do we need this.getJointArrayIds()?
	public org.lgna.story.resources.JointArrayId[] getJointArrayIds() {
		if( this.jointArrayIds == null ) {
			java.util.List<org.lgna.story.resources.JointArrayId> jointArrayIdsList = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicStaticFinalInstances( this.getResource().getClass(), org.lgna.story.resources.JointArrayId.class );
			this.jointArrayIds = jointArrayIdsList.toArray( new org.lgna.story.resources.JointArrayId[ jointArrayIdsList.size() ] );
		}
		return this.jointArrayIds;
	}

	private List<org.lgna.story.resources.JointId> getMissingJoints() {
		List<JointId> missingJoints = new ArrayList<JointId>();
		List<JointId> jointsToCheck = new ArrayList<JointId>();
		org.lgna.story.resources.JointId[] rootIds = this.getRootJointIds();
		for( JointId id : rootIds ) {
			jointsToCheck.add( id );
		}
		while( !jointsToCheck.isEmpty() ) {
			JointId joint = jointsToCheck.remove( 0 );
			if( !this.hasJointImplementation( joint ) ) {
				missingJoints.add( joint );
			}
			for( JointId child : joint.getChildren( this.getResource() ) ) {
				jointsToCheck.add( child );
			}
		}
		return missingJoints;
	}

	public void setNewResource( JointedModelResource resource ) {
		if( resource != this.getResource() ) {
			java.util.Map<org.lgna.story.resources.JointId, edu.cmu.cs.dennisc.math.UnitQuaternion> mapIdToOriginalRotation = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			for( java.util.Map.Entry<org.lgna.story.resources.JointId, JointImpWrapper> jointEntry : this.mapIdToJoint.entrySet() ) {
				mapIdToOriginalRotation.put( jointEntry.getKey(), jointEntry.getValue().getOriginalOrientation() );
			}
			edu.cmu.cs.dennisc.scenegraph.Composite originalParent = this.visualData.getSGParent();
			VisualData<?> oldVisualData = this.visualData;
			edu.cmu.cs.dennisc.math.Dimension3 oldScale = this.getScale();
			edu.cmu.cs.dennisc.property.InstanceProperty[] oldScaleProperties = this.getScaleProperties();
			this.factory = (JointImplementationAndVisualDataFactory<R>)resource.getImplementationAndVisualFactory();
			float originalOpacity = this.opacity.getValue();
			org.lgna.story.Paint originalPaint = this.paint.getValue();
			this.visualData = this.factory.createVisualData();
			org.lgna.story.resources.JointId[] rootIds = this.getRootJointIds();
			java.util.Map<org.lgna.story.resources.JointId, JointImp> newJoints = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
			if( rootIds.length == 0 ) {
				//pass
			} else {
				for( org.lgna.story.resources.JointId root : rootIds ) {
					this.createRegularJointTree( root, this, newJoints );
				}
			}

			this.mapArrayIdToJointIdArray.clear();
			for( org.lgna.story.resources.JointArrayId arrayId : this.getJointArrayIds() ) {
				this.createJointImpsAsNeededForJointArrayIds( arrayId, newJoints, false );
			}

			matchNewDataToExistingJoints( mapIdToOriginalRotation, newJoints );

			this.visualData.setSGParent( originalParent );
			oldVisualData.setSGParent( null );
			this.opacity.setValue( originalOpacity );
			this.paint.setValue( originalPaint );

			edu.cmu.cs.dennisc.property.InstanceProperty<?>[] newScaleProperties = this.getScaleProperties();
			for( int i = 0; i < oldScaleProperties.length; i++ ) {
				edu.cmu.cs.dennisc.property.InstanceProperty<?> oldProp = oldScaleProperties[ i ];
				assert oldProp != null : i;
				for( edu.cmu.cs.dennisc.property.event.PropertyListener propListener : oldProp.getPropertyListeners() ) {
					newScaleProperties[ i ].addPropertyListener( propListener );
				}
			}
			this.setScale( oldScale );
		}
	}

	public org.lgna.story.resources.JointedModelResource getVisualResource() {
		return this.factory.getResource();
	}

	public Iterable<JointImp> getJoints() {
		final java.util.List<JointImp> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.treeWalk( new TreeWalkObserver() {
			@Override
			public void pushJoint( org.lgna.story.implementation.JointImp joint ) {
				//todo: remove null check?
				if( joint != null ) {
					rv.add( joint );
				}
			}

			@Override
			public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
			}

			@Override
			public void popJoint( org.lgna.story.implementation.JointImp joint ) {
			}
		} );
		return rv;
	}

	private boolean findJoint( org.lgna.story.resources.JointId jointId, org.lgna.story.resources.JointId toFind ) {
		if( jointId.toString().equals( toFind.toString() ) ) {
			return true;
		}
		for( org.lgna.story.resources.JointId childId : jointId.getChildren( this.factory.getResource() ) ) {
			if( findJoint( childId, toFind ) ) {
				return true;
			}
		}
		return false;
	}

	private void matchNewDataToExistingJoints( java.util.Map<org.lgna.story.resources.JointId, edu.cmu.cs.dennisc.math.UnitQuaternion> mapIdToOriginalRotation, java.util.Map<org.lgna.story.resources.JointId, JointImp> newJoints ) {
		java.util.List<org.lgna.story.resources.JointId> toRemove = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( java.util.Map.Entry<org.lgna.story.resources.JointId, JointImpWrapper> jointEntry : this.mapIdToJoint.entrySet() ) {
			if( newJoints.containsKey( jointEntry.getKey() ) ) {
				JointImp newJoint = newJoints.get( jointEntry.getKey() );
				jointEntry.getValue().replaceWithJoint( newJoint, mapIdToOriginalRotation.get( jointEntry.getKey() ) );
			} else {
				toRemove.add( jointEntry.getKey() );
			}
		}
		for( org.lgna.story.resources.JointId id : toRemove ) {
			this.mapIdToJoint.remove( id );
		}
	}

	private JointImp createRegularJointTree( org.lgna.story.resources.JointId jointId, EntityImp parent, java.util.Map<org.lgna.story.resources.JointId, JointImp> jointMap ) {
		JointImp joint = this.createAndParentJointImp( jointId, parent );
		jointMap.put( jointId, joint );
		for( org.lgna.story.resources.JointId childId : jointId.getChildren( this.factory.getResource() ) ) {
			JointImp childTree = createRegularJointTree( childId, joint, jointMap );
		}
		return joint;
	}

	private JointImp createAndParentJointImp( org.lgna.story.resources.JointId jointId, EntityImp parent ) {
		JointImp joint = this.createJointImplementation( jointId );
		if( joint.getSgVehicle() == null ) {
			joint.setVehicle( parent );
		}
		return joint;
	}

	private JointImpWrapper createJointWrapper( org.lgna.story.resources.JointId jointId, EntityImp parent ) {
		return new JointImpWrapper( this, this.createAndParentJointImp( jointId, parent ) );
	}

	private JointImp createWrapperJointTree( org.lgna.story.resources.JointId jointId, EntityImp parent, java.util.Map<org.lgna.story.resources.JointId, JointImpWrapper> jointMap ) {
		JointImpWrapper joint = createJointWrapper( jointId, parent );
		jointMap.put( jointId, joint );
		for( org.lgna.story.resources.JointId childId : jointId.getChildren( this.factory.getResource() ) ) {
			createWrapperJointTree( childId, joint, jointMap );
		}
		return joint;
	}

	public void setAllJointPivotsVisibile( boolean isPivotVisible ) {
		for( java.util.Map.Entry<org.lgna.story.resources.JointId, JointImpWrapper> jointEntry : this.mapIdToJoint.entrySet() ) {
			jointEntry.getValue().setPivotVisible( isPivotVisible );
		}
	}

	@Override
	public A getAbstraction() {
		return this.abstraction;
	}

	public R getResource() {
		return this.factory.getResource();
	}

	public VisualData getVisualData() {
		return this.visualData;
	}

	@Override
	public final edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
		return this.visualData.getSgVisuals();
	}

	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgPaintAppearances() {
		return this.visualData.getSgAppearances();
	}

	@Override
	protected final edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgOpacityAppearances() {
		return this.getSgPaintAppearances();
	}

	public org.lgna.story.implementation.JointImp getJointImplementation( org.lgna.story.resources.JointId jointId ) {
		JointImpWrapper wrapper = this.mapIdToJoint.get( jointId );
		if( wrapper != null ) {
			return wrapper;
		}
		return null;
	}

	protected edu.cmu.cs.dennisc.math.Vector4 getFrontOffsetForJoint( org.lgna.story.implementation.JointImp jointImp ) {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = jointImp.getAxisAlignedMinimumBoundingBox( this );
		edu.cmu.cs.dennisc.math.Point3 point = bbox.getCenterOfFrontFace();
		offsetAsSeenBySubject.x = point.x;
		offsetAsSeenBySubject.y = point.y;
		offsetAsSeenBySubject.z = point.z;
		offsetAsSeenBySubject.w = 1;
		return offsetAsSeenBySubject;
	}

	protected edu.cmu.cs.dennisc.math.Vector4 getTopOffsetForJoint( org.lgna.story.implementation.JointImp jointImp ) {
		edu.cmu.cs.dennisc.math.Vector4 offsetAsSeenBySubject = new edu.cmu.cs.dennisc.math.Vector4();
		edu.cmu.cs.dennisc.math.AxisAlignedBox bbox = jointImp.getAxisAlignedMinimumBoundingBox( this );
		edu.cmu.cs.dennisc.math.Point3 point = bbox.getCenterOfTopFace();
		offsetAsSeenBySubject.x = point.x;
		offsetAsSeenBySubject.y = point.y;
		offsetAsSeenBySubject.z = point.z;
		offsetAsSeenBySubject.w = 1;
		return offsetAsSeenBySubject;
	}

	public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.getOriginalJointOrientation( jointId );
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalJointTransformation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.getOriginalJointTransformation( jointId );
	}

	public abstract org.lgna.story.resources.JointId[] getRootJointIds();

	public edu.cmu.cs.dennisc.scenegraph.SkeletonVisual getSgSkeletonVisual() {
		if( this.getSgVisuals()[ 0 ] instanceof edu.cmu.cs.dennisc.scenegraph.SkeletonVisual ) {
			return (edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)this.getSgVisuals()[ 0 ];
		}
		return null;
	}

	@Override
	protected edu.cmu.cs.dennisc.property.InstanceProperty[] getScaleProperties() {
		if( this.sgScalable != null ) {
			return new edu.cmu.cs.dennisc.property.InstanceProperty[] { this.sgScalable.scale };
		} else {
			return new edu.cmu.cs.dennisc.property.InstanceProperty[] { this.visualData.getSgVisuals()[ 0 ].scale };
		}
	}

	@Override
	public edu.cmu.cs.dennisc.math.Dimension3 getScale() {
		if( this.sgScalable != null ) {
			return this.sgScalable.scale.getValue();
		} else {
			edu.cmu.cs.dennisc.math.Matrix3x3 scale = this.visualData.getSgVisuals()[ 0 ].scale.getValue();
			return new edu.cmu.cs.dennisc.math.Dimension3( scale.right.x, scale.up.y, scale.backward.z );
		}
	}

	@Override
	public void setScale( edu.cmu.cs.dennisc.math.Dimension3 scale ) {
		if( this.sgScalable != null ) {
			this.sgScalable.scale.setValue( new edu.cmu.cs.dennisc.math.Dimension3( scale ) );
		} else {
			edu.cmu.cs.dennisc.math.Matrix3x3 m = edu.cmu.cs.dennisc.math.Matrix3x3.createZero();
			m.right.x = scale.x;
			m.up.y = scale.y;
			m.backward.z = scale.z;

			for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.visualData.getSgVisuals() ) {
				sgVisual.scale.setValue( m );
			}
			for( JointImp jointImp : this.mapIdToJoint.values() ) {
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 lt = jointImp.getLocalTransformation();
				lt.translation.setToMultiplication( jointImp.getOriginalTransformation().translation, scale );
				jointImp.setLocalTransformation( lt );
			}
		}
	}

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, boolean ignoreJointOrientations ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans = this.getTransformation( asSeenBy );
		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
		this.updateCumulativeBound( cumulativeBound, trans, ignoreJointOrientations );
		return cumulativeBound.getBoundingBox();
	}

	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( boolean ignoreJointOrientations ) {
		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF, ignoreJointOrientations );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
		return getAxisAlignedMinimumBoundingBox( asSeenBy, true );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF, true );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getDynamicAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
		return getAxisAlignedMinimumBoundingBox( asSeenBy, false );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getDynamicAxisAlignedMinimumBoundingBox() {
		return getDynamicAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
	}

	@Override
	public edu.cmu.cs.dennisc.math.Dimension3 getSize() {
		return getAxisAlignedMinimumBoundingBox().getSize();
	}

	public edu.cmu.cs.dennisc.math.Dimension3 getSize( boolean ignoreJointOrientations ) {
		return getAxisAlignedMinimumBoundingBox( ignoreJointOrientations ).getSize();
	}

	@Override
	public void setSize( edu.cmu.cs.dennisc.math.Dimension3 size ) {
		setScale( getScaleForSize( size ) );
	}

	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans, boolean ignoreJointOrientations ) {
		for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.getSgVisuals() ) {
			if( sgVisual instanceof edu.cmu.cs.dennisc.scenegraph.SkeletonVisual ) {
				rv.addSkeletonVisual( (edu.cmu.cs.dennisc.scenegraph.SkeletonVisual)sgVisual, trans, ignoreJointOrientations );
			} else {
				rv.add( sgVisual, trans );
			}
		}
		return rv;
	}

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans ) {
		return updateCumulativeBound( rv, trans, true );
	}

	protected final org.lgna.story.implementation.JointImp createJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.createJointImplementation( this, jointId );
	}

	protected final boolean hasJointImplementation( org.lgna.story.resources.JointId jointId ) {
		return this.factory.hasJointImplementation( this, jointId );
	}

	private org.lgna.story.implementation.visualization.JointedModelVisualization visualization;

	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Leaf getVisualization() {
		if( this.visualization != null ) {
			//pass
		} else {
			this.visualization = new org.lgna.story.implementation.visualization.JointedModelVisualization( this );
		}
		return this.visualization;
	}

	@Override
	public void showVisualization() {
		this.getVisualization().setParent( this.getSgComposite() );
	}

	@Override
	public void hideVisualization() {
		if( this.visualization != null ) {
			this.visualization.setParent( null );
		}
	}

	public static interface TreeWalkObserver {
		public void pushJoint( JointImp joint );

		public void handleBone( JointImp parent, JointImp child );

		public void popJoint( JointImp joint );
	}

	private void treeWalk( org.lgna.story.resources.JointId parentId, TreeWalkObserver observer ) {
		JointImp parentImp = this.getJointImplementation( parentId );
		if( parentImp != null ) {
			observer.pushJoint( parentImp );
			R resource = this.getResource();
			for( org.lgna.story.resources.JointId childId : parentId.getChildren( resource ) ) {
				JointImp childImp = this.getJointImplementation( childId );
				if( childImp != null ) {
					observer.handleBone( parentImp, childImp );
				}
			}
			observer.popJoint( parentImp );
			for( org.lgna.story.resources.JointId childId : parentId.getChildren( resource ) ) {
				treeWalk( childId, observer );
			}
		}
	}

	public void treeWalk( TreeWalkObserver observer ) {
		for( org.lgna.story.resources.JointId root : this.getRootJointIds() ) {
			this.treeWalk( root, observer );
		}
	}

	private static enum AddOp {
		PREPEND {
			@Override
			public java.util.List<JointImp> add( java.util.List<JointImp> rv, JointImp joint, List<org.lgna.ik.core.solver.Bone.Direction> directions, org.lgna.ik.core.solver.Bone.Direction direction ) {
				rv.add( 0, joint );
				if( directions != null ) {
					directions.add( 0, direction );
				}
				return rv;
			}
		},
		APPEND {
			@Override
			public java.util.List<JointImp> add( java.util.List<JointImp> rv, JointImp joint, List<org.lgna.ik.core.solver.Bone.Direction> directions, org.lgna.ik.core.solver.Bone.Direction direction ) {
				rv.add( joint );
				if( directions != null ) {
					directions.add( direction );
				}
				return rv;
			}
		};
		public abstract java.util.List<JointImp> add( java.util.List<JointImp> rv, JointImp joint, List<org.lgna.ik.core.solver.Bone.Direction> directions, org.lgna.ik.core.solver.Bone.Direction direction );
	}

	private java.util.List<JointImp> updateJointsBetween( java.util.List<JointImp> rv, List<org.lgna.ik.core.solver.Bone.Direction> directions, JointImp joint, EntityImp ancestorToReach, AddOp addOp ) {
		if( joint == ancestorToReach ) {
			//pass
		} else {
			org.lgna.story.resources.JointId parentId = joint.getJointId().getParent();
			if( parentId != null ) {
				JointImp parent = this.getJointImplementation( parentId );
				this.updateJointsBetween( rv, directions, parent, ancestorToReach, addOp );
			}
		}
		org.lgna.ik.core.solver.Bone.Direction direction;
		if( addOp == AddOp.APPEND ) {
			direction = org.lgna.ik.core.solver.Bone.Direction.DOWNSTREAM;
		} else {
			direction = org.lgna.ik.core.solver.Bone.Direction.UPSTREAM;
		}
		addOp.add( rv, joint, directions, direction );
		return rv;
	}

	public java.util.List<JointImp> getInclusiveListOfJointsBetween( JointImp jointA, JointImp jointB, java.util.List<org.lgna.ik.core.solver.Bone.Direction> directions ) {
		assert jointA != null : this;
		assert jointB != null : this;
		java.util.List<JointImp> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		if( jointA == jointB ) {
			//?
			rv.add( jointA );
			directions.add( org.lgna.ik.core.solver.Bone.Direction.DOWNSTREAM );
			//			throw new RuntimeException( "To Gazi: Please ensure that direction is correct in this case." );
		} else {
			if( jointA.isDescendantOf( jointB ) ) {
				this.updateJointsBetween( rv, directions, jointA, jointB, AddOp.PREPEND );
			} else if( jointB.isDescendantOf( jointA ) ) {
				this.updateJointsBetween( rv, directions, jointB, jointA, AddOp.APPEND );
			} else {
				//It shouldn't even use the joint on which direction is changed (the common ancestor)
				//that's what the below call does
				this.updateJointsUpToAndExcludingCommonAncestor( rv, directions, jointA, jointB );
			}
		}
		return rv;
	}

	private void updateJointsUpToAndExcludingCommonAncestor( List<JointImp> rvPath, List<Direction> rvDirections, JointImp jointA, JointImp jointB ) {
		java.util.List<JointImp> pathA = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		java.util.List<JointImp> pathB = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		List<Direction> directionsA = new java.util.ArrayList<Direction>();
		List<Direction> directionsB = new java.util.ArrayList<Direction>();

		this.updateJointsBetween( pathA, directionsA, jointA, this, AddOp.PREPEND );
		this.updateJointsBetween( pathB, directionsB, jointB, this, AddOp.APPEND );

		JointImp commonAncestor = null;

		for( JointImp jointInA : pathA ) {
			if( pathB.contains( jointInA ) ) {
				commonAncestor = jointInA;
				break;
			}
		}

		if( commonAncestor == null ) {
			throw new RuntimeException( "Probably not connected with a chain." );
		}

		ListIterator<JointImp> pathAIterator = pathA.listIterator( pathA.size() );
		ListIterator<Direction> directionsAIterator = directionsA.listIterator( directionsA.size() );
		for( ; pathAIterator.hasPrevious(); ) {
			JointImp jointImp = pathAIterator.previous();
			directionsAIterator.previous();

			pathAIterator.remove();
			directionsAIterator.remove();

			if( jointImp == commonAncestor ) {
				break;
			}
		}

		ListIterator<JointImp> pathBIterator = pathB.listIterator();
		ListIterator<Direction> directionsBIterator = directionsB.listIterator();
		for( ; pathBIterator.hasNext(); ) {
			JointImp jointImp = (JointImp)pathBIterator.next();
			directionsBIterator.next();

			pathBIterator.remove();
			directionsBIterator.remove();

			if( jointImp == commonAncestor ) {
				break;
			}
		}

		rvPath.addAll( pathA );
		rvPath.addAll( pathB );
		rvDirections.addAll( directionsA );
		rvDirections.addAll( directionsB );
	}

	public java.util.List<JointImp> getInclusiveListOfJointsBetween( org.lgna.story.resources.JointId idA, org.lgna.story.resources.JointId idB, java.util.List<org.lgna.ik.core.solver.Bone.Direction> directions ) {
		return this.getInclusiveListOfJointsBetween( this.getJointImplementation( idA ), this.getJointImplementation( idB ), directions );
	}

	private static class JointData {
		private final JointImp jointImp;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q0;
		private final edu.cmu.cs.dennisc.math.UnitQuaternion q1;

		public JointData( JointImp jointImp ) {
			this.jointImp = jointImp;
			this.q0 = this.jointImp.getLocalOrientation().createUnitQuaternion();
			edu.cmu.cs.dennisc.math.UnitQuaternion q = this.jointImp.getOriginalOrientation();
			if( q != null ) {
				if( this.q0.isWithinReasonableEpsilonOrIsNegativeWithinReasonableEpsilon( q ) ) {
					this.q1 = null;
				} else {
					this.q1 = q;
				}
			} else {
				this.q1 = null;
			}
		}

		//		public JointImp getJointImp() {
		//			return this.jointImp;
		//		}
		//		public edu.cmu.cs.dennisc.math.UnitQuaternion getQ0() {
		//			return this.q0;
		//		}
		//		public edu.cmu.cs.dennisc.math.UnitQuaternion getQ1() {
		//			return this.q1;
		//		}
		public void setPortion( double portion ) {
			if( this.q1 != null ) {
				this.jointImp.setLocalOrientationOnly( edu.cmu.cs.dennisc.math.UnitQuaternion.createInterpolation( this.q0, this.q1, portion ).createOrthogonalMatrix3x3() );
			} else {
				//System.err.println( "skipping: " + this.jointImp );
			}
		}

		public void epilogue() {
			if( this.q1 != null ) {
				this.jointImp.setLocalOrientationOnly( this.q1.createOrthogonalMatrix3x3() );
			} else {
				//System.err.println( "skipping: " + this.jointImp );
			}
		}
	}

	private static class StraightenTreeWalkObserver implements TreeWalkObserver {
		private java.util.List<JointData> list = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		@Override
		public void pushJoint( JointImp jointImp ) {
			if( jointImp != null ) {
				list.add( new JointData( jointImp ) );
			}
		}

		@Override
		public void handleBone( org.lgna.story.implementation.JointImp parent, org.lgna.story.implementation.JointImp child ) {
		}

		@Override
		public void popJoint( JointImp joint ) {
		}
	};

	public void straightenOutJoints() {
		StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
		this.treeWalk( treeWalkObserver );
		for( JointData jointData : treeWalkObserver.list ) {
			jointData.epilogue();
		}
	}

	public void animateStraightenOutJoints( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			this.straightenOutJoints();
		} else {
			final StraightenTreeWalkObserver treeWalkObserver = new StraightenTreeWalkObserver();
			this.treeWalk( treeWalkObserver );
			class StraightenOutJointsAnimation extends edu.cmu.cs.dennisc.animation.DurationBasedAnimation {
				public StraightenOutJointsAnimation( double duration, edu.cmu.cs.dennisc.animation.Style style ) {
					super( duration, style );
				}

				@Override
				protected void prologue() {
				}

				@Override
				protected void setPortion( double portion ) {
					for( JointData jointData : treeWalkObserver.list ) {
						jointData.setPortion( portion );
					}
				}

				@Override
				protected void epilogue() {
					for( JointData jointData : treeWalkObserver.list ) {
						jointData.epilogue();
					}
				}
			}
			perform( new StraightenOutJointsAnimation( duration, style ) );
		}
	}

	public void animateStraightenOutJoints( double duration ) {
		this.animateStraightenOutJoints( duration, DEFAULT_STYLE );
	}

	public void animateStraightenOutJoints() {
		this.animateStraightenOutJoints( DEFAULT_DURATION );
	}

	public void say( String text, double duration, java.awt.Font font, edu.cmu.cs.dennisc.color.Color4f textColor, edu.cmu.cs.dennisc.color.Color4f fillColor, edu.cmu.cs.dennisc.color.Color4f outlineColor, edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.PositionPreference positionPreference ) {
		duration = adjustDurationIfNecessary( duration );
		org.lgna.story.implementation.overlay.BubbleImp bubbleImp = new org.lgna.story.implementation.overlay.SpeechBubbleImp( this, this.getSpeechBubbleOriginator(), text, font, textColor, fillColor, outlineColor, positionPreference );
		this.displayBubble( bubbleImp, duration );
	}

	public void think( String text, double duration, java.awt.Font font, edu.cmu.cs.dennisc.color.Color4f textColor, edu.cmu.cs.dennisc.color.Color4f fillColor, edu.cmu.cs.dennisc.color.Color4f outlineColor, edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.PositionPreference positionPreference ) {
		duration = adjustDurationIfNecessary( duration );
		org.lgna.story.implementation.overlay.BubbleImp bubbleImp = new org.lgna.story.implementation.overlay.ThoughtBubbleImp( this, this.getSpeechBubbleOriginator(), text, font, textColor, fillColor, outlineColor, positionPreference );
		this.displayBubble( bubbleImp, duration );
	}

	public void strikePose( org.lgna.story.Pose<? extends SJointedModel> pose, double duration, edu.cmu.cs.dennisc.animation.Style style ) {
		this.getProgram().perform( new PoseAnimation( duration, style, this, pose ), null );
	}

	private final A abstraction;
	private final edu.cmu.cs.dennisc.scenegraph.Scalable sgScalable;
	private final java.util.Map<org.lgna.story.resources.JointId, JointImpWrapper> mapIdToJoint = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private final java.util.Map<org.lgna.story.resources.JointArrayId, org.lgna.story.resources.JointId[]> mapArrayIdToJointIdArray = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private org.lgna.story.resources.JointArrayId[] jointArrayIds = null;
	private JointImplementationAndVisualDataFactory<R> factory;
	private VisualData visualData;
}
