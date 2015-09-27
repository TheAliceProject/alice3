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

package org.lgna.story.implementation.alice;

import edu.cmu.cs.dennisc.scenegraph.Composite;

/**
 * @author Dennis Cosgrove
 */
public class JointImplementationAndVisualDataFactory implements org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory {
	private static java.util.Map<org.lgna.story.resources.JointedModelResource, JointImplementationAndVisualDataFactory> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static class VisualData implements org.lgna.story.implementation.JointedModelImp.VisualData {
		private edu.cmu.cs.dennisc.scenegraph.TexturedAppearance[] texturedAppearances;
		private edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual;

		public VisualData( org.lgna.story.resources.JointedModelResource resource ) {
			assert resource != null;
			this.texturedAppearances = AliceResourceUtilties.getTexturedAppearances( resource );
			//Get the copy of the original geometry (this makes a new skeleton, appearance and whatnot, and keeps references to static data like the meshes)
			this.sgSkeletonVisual = AliceResourceUtilties.getVisualCopy( resource );
			//Set the texture data to be the texture info specified by the resource
			this.sgSkeletonVisual.textures.setValue( this.texturedAppearances );
			if( this.sgSkeletonVisual.skeleton.getValue() != null ) {
				this.sgSkeletonVisual.skeleton.getValue().setParentVisual( this.sgSkeletonVisual );
			}
		}

		@Override
		public edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] getSgAppearances() {
			return new edu.cmu.cs.dennisc.scenegraph.SimpleAppearance[] { (edu.cmu.cs.dennisc.scenegraph.SimpleAppearance)this.sgSkeletonVisual.frontFacingAppearance.getValue() };
		}

		@Override
		public edu.cmu.cs.dennisc.scenegraph.Visual[] getSgVisuals() {
			return new edu.cmu.cs.dennisc.scenegraph.Visual[] { this.sgSkeletonVisual };
		}

		@Override
		public double getBoundingSphereRadius() {
			return 1.0;
		}

		@Override
		public void setSGParent( edu.cmu.cs.dennisc.scenegraph.Composite parent ) {
			sgSkeletonVisual.setParent( parent );
			for( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual : this.getSgVisuals() ) {
				sgVisual.setParent( parent );
			}
		}

		//		public void setResource(org.lgna.story.resources.JointedModelResource resource) {
		//			assert resource != null;
		//			this.texturedAppearances = AliceResourceUtilties.getTexturedAppearances( resource );
		//			this.sgSkeletonVisual = AliceResourceUtilties.createReplaceVisualElements( this.sgSkeletonVisual, resource );
		//			this.sgSkeletonVisual.textures.setValue(this.texturedAppearances);
		//		}
		@Override
		public Composite getSGParent() {
			return this.sgSkeletonVisual.getParent();
		}
	}

	public static JointImplementationAndVisualDataFactory getInstance( org.lgna.story.resources.JointedModelResource resource ) {
		synchronized( map ) {
			JointImplementationAndVisualDataFactory rv = map.get( resource );
			if( rv != null ) {
				//pass
			} else {
				rv = new JointImplementationAndVisualDataFactory( resource );
				map.put( resource, rv );
			}
			return rv;
		}
	}

	private final org.lgna.story.resources.JointedModelResource resource;

	private JointImplementationAndVisualDataFactory( org.lgna.story.resources.JointedModelResource resource ) {
		this.resource = resource;
	}

	@Override
	public org.lgna.story.implementation.JointedModelImp.JointImplementationAndVisualDataFactory getFactoryForResource( org.lgna.story.resources.JointedModelResource resource ) {
		return JointImplementationAndVisualDataFactory.getInstance( resource );
	}

	@Override
	public org.lgna.story.resources.JointedModelResource getResource() {
		return this.resource;
	}

	@Override
	public org.lgna.story.implementation.JointImp createJointImplementation( org.lgna.story.implementation.JointedModelImp jointedModelImplementation, org.lgna.story.resources.JointId jointId ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual = ( (VisualData)jointedModelImplementation.getVisualData() ).sgSkeletonVisual;
		if( sgSkeletonVisual != null ) {
			String key = jointId.toString();
			edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgSkeletonVisual.skeleton.getValue();
			edu.cmu.cs.dennisc.scenegraph.Joint sgJoint = sgSkeletonRoot.getJoint( key );
			if( sgJoint != null ) {
				sgJoint.setName( key );
				return new org.lgna.story.implementation.alice.JointImplementation( jointedModelImplementation, jointId, sgJoint );
			} else {
				org.lgna.story.resources.JointedModelResource resource = jointedModelImplementation.getResource();
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( jointId, "not found for", resource.getClass(), resource );
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public org.lgna.story.implementation.JointImp[] createJointArrayImplementation( org.lgna.story.implementation.JointedModelImp jointedModelImplementation, org.lgna.story.resources.JointArrayId jointArrayId ) {
		edu.cmu.cs.dennisc.scenegraph.SkeletonVisual sgSkeletonVisual = ( (VisualData)jointedModelImplementation.getVisualData() ).sgSkeletonVisual;
		if( sgSkeletonVisual != null ) {
			String key = jointArrayId.getElementNamePattern();
			edu.cmu.cs.dennisc.scenegraph.Joint sgSkeletonRoot = sgSkeletonVisual.skeleton.getValue();
			edu.cmu.cs.dennisc.scenegraph.Joint[] sgJoints = sgSkeletonRoot.getJoints( key );
			if( sgJoints != null ) {
				org.lgna.story.implementation.JointImp[] jointImps = new org.lgna.story.implementation.JointImp[ sgJoints.length ];
				for( int i = 0; i < sgJoints.length; i++ ) {
					final String jointName = sgJoints[ i ].jointID.getValue();
					sgJoints[ i ].setName( jointName );
					org.lgna.story.resources.JointId parentJointId;
					if( i == 0 ) {
						parentJointId = jointArrayId.getRoot();
					}
					else {
						parentJointId = jointImps[ i - 1 ].getJointId();
					}
					if( sgSkeletonRoot.getJoint( parentJointId.toString() ) != sgJoints[ i ].getParent() )
					{
						org.lgna.story.resources.JointedModelResource resource = jointedModelImplementation.getResource();
						edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "Invalid implicit array structure:", jointName, "'s parent is not set to", parentJointId.toString(), "on", resource.getClass(), resource );
						return null;
					}

					org.lgna.story.resources.JointId jointId = new org.lgna.story.resources.JointId( parentJointId, jointArrayId.getContainingClass() ) {
						@Override
						public String toString() {
							return jointName;
						}
					};

					jointImps[ i ] = new org.lgna.story.implementation.alice.JointImplementation( jointedModelImplementation, jointId, sgJoints[ i ] );
				}
				return jointImps;
			} else {
				org.lgna.story.resources.JointedModelResource resource = jointedModelImplementation.getResource();
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( jointArrayId, " array not found for ", resource.getClass(), resource );
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public edu.cmu.cs.dennisc.math.UnitQuaternion getOriginalJointOrientation( org.lgna.story.resources.JointId jointId ) {
		return AliceResourceUtilties.getOriginalJointOrientation( this.resource, jointId );
	}

	@Override
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getOriginalJointTransformation( org.lgna.story.resources.JointId jointId ) {
		return AliceResourceUtilties.getOriginalJointTransformation( this.resource, jointId );
	}

	@Override
	public org.lgna.story.implementation.JointedModelImp.VisualData createVisualData() {
		return new VisualData( this.resource );
	}
}
