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

import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.UnitQuaternion;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.SimpleAppearance;
import edu.cmu.cs.dennisc.scenegraph.SkeletonVisual;
import edu.cmu.cs.dennisc.scenegraph.TexturedAppearance;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointArrayId;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;

import java.util.Map;

/**
 * @author Dennis Cosgrove
 */
public class JointImplementationAndVisualDataFactory implements JointedModelImp.JointImplementationAndVisualDataFactory {
	private static final Map<JointedModelResource, JointImplementationAndVisualDataFactory> map = Maps.newHashMap();

	private static class VisualData implements JointedModelImp.VisualData {
		private TexturedAppearance[] texturedAppearances;
		private SkeletonVisual sgSkeletonVisual;

		VisualData( JointedModelResource resource ) {
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
		public SimpleAppearance[] getSgAppearances() {
			return new SimpleAppearance[] { (SimpleAppearance)this.sgSkeletonVisual.frontFacingAppearance.getValue() };
		}

		@Override
		public Visual[] getSgVisuals() {
			return new Visual[] { this.sgSkeletonVisual };
		}

		@Override
		public double getBoundingSphereRadius() {
			return 1.0;
		}

		@Override
		public void setSGParent( Composite parent ) {
			sgSkeletonVisual.setParent( parent );
		}

		@Override
		public Composite getSGParent() {
			return this.sgSkeletonVisual.getParent();
		}
	}

	public static JointImplementationAndVisualDataFactory getInstance( JointedModelResource resource ) {
		synchronized( map ) {
			JointImplementationAndVisualDataFactory rv = map.get( resource );
			if ( rv == null ) {
				rv = new JointImplementationAndVisualDataFactory( resource );
				map.put( resource, rv );
			}
			return rv;
		}
	}

	private final JointedModelResource resource;

	private JointImplementationAndVisualDataFactory( JointedModelResource resource ) {
		this.resource = resource;
	}

	@Override
	public JointedModelResource getResource() {
		return this.resource;
	}

	@Override
	public JointImp createJointImplementation( JointedModelImp jointedModelImplementation, JointId jointId ) {
		SkeletonVisual sgSkeletonVisual = ( (VisualData)jointedModelImplementation.getVisualData() ).sgSkeletonVisual;
		if( sgSkeletonVisual != null ) {
			String key = jointId.toString();
			Joint sgSkeletonRoot = sgSkeletonVisual.skeleton.getValue();
			Joint sgJoint = sgSkeletonRoot.getJoint( key );
			if( sgJoint != null ) {
				sgJoint.setName( key );
				return new JointImplementation( jointedModelImplementation, jointId, sgJoint );
			} else {
				JointedModelResource resource = jointedModelImplementation.getResource();
				Logger.severe( jointId, "not found for", resource.getClass(), resource );
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public boolean hasJointImplementation( JointedModelImp jointedModelImplementation, JointId jointId ) {
		SkeletonVisual sgSkeletonVisual = ( (VisualData)jointedModelImplementation.getVisualData() ).sgSkeletonVisual;
		if( sgSkeletonVisual != null ) {
			String key = jointId.toString();
			Joint sgSkeletonRoot = sgSkeletonVisual.skeleton.getValue();
			Joint sgJoint = sgSkeletonRoot.getJoint( key );
			if( sgJoint != null ) {
				return true;
			} else {
				JointedModelResource resource = jointedModelImplementation.getResource();
				Logger.severe( jointId, "not found for", resource.getClass(), resource );
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public JointId[] getJointArrayIds( JointedModelImp jointedModelImplementation, JointArrayId jointArrayId ) {
		SkeletonVisual sgSkeletonVisual = ( (VisualData)jointedModelImplementation.getVisualData() ).sgSkeletonVisual;
		if( sgSkeletonVisual != null ) {
			String key = jointArrayId.getElementNamePattern();
			Joint sgSkeletonRoot = sgSkeletonVisual.skeleton.getValue();
			Joint[] sgJoints = sgSkeletonRoot.getJoints( key );
			if( sgJoints != null ) {
				JointId[] jointIds = new JointId[ sgJoints.length ];
				for( int i = 0; i < sgJoints.length; i++ ) {
					final String jointName = sgJoints[ i ].jointID.getValue();
					sgJoints[ i ].setName( jointName );
					JointId parentJointId;
					if( i == 0 ) {
						parentJointId = jointArrayId.getRoot();
					} else {
						parentJointId = jointIds[ i - 1 ];
					}
					//We're making the joint ids for an implicit array structure
					//(meaning this is an array that is declared on the class but is ultimately implemented by an individual resource)
					//Given this fact, these joint ids need to be tied to the resource rather than the class
					//Maybe pass parent in as null if the parent joint is the root joint? this seems like a bad idea though...
					//Update 8/3/2018: Working on removing containing class dependency. Goal is to make joints just know about their id and their parent.
					JointId jointId = new JointId( parentJointId, jointArrayId.getContainingClass()) {
						@Override
						public String toString( ) {
							return jointName;
						}
					};

					jointIds[ i ] = jointId;
				}
				return jointIds;
			} else {
				JointedModelResource resource = jointedModelImplementation.getResource();
				Logger.severe( jointArrayId, " array not found for ", resource.getClass(), resource );
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	public UnitQuaternion getOriginalJointOrientation( JointId jointId ) {
		return AliceResourceUtilties.getOriginalJointOrientation( this.resource, jointId );
	}

	@Override
	public AffineMatrix4x4 getOriginalJointTransformation( JointId jointId ) {
		return AliceResourceUtilties.getOriginalJointTransformation( this.resource, jointId );
	}

	@Override
	public JointedModelImp.VisualData createVisualData() {
		return new VisualData( this.resource );
	}
}
