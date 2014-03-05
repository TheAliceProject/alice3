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

package org.lgna.ik.core.solver;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lgna.ik.core.IkConstants;
import org.lgna.ik.core.enforcer.Weights;
import org.lgna.ik.core.solver.Bone.Axis;
import org.lgna.story.implementation.JointImp;

import Jama.Matrix;
import edu.cmu.cs.dennisc.math.AxisRotation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Vector3;

/**
 * @author Dennis Cosgrove
 */
public class Solver {

	private class Constraint {
		private final Map<Bone, Map<Axis, Vector3>> contributions;
		private final DesiredVelocity desiredValue;

		public Constraint( Map<Bone, Map<Axis, Vector3>> map, DesiredVelocity desiredValue ) {
			this.contributions = map;
			this.desiredValue = desiredValue;
		}
	}

	public class JacobianAndInverse {
		private final Matrix jacobian;
		private final Matrix pseudoInverseJacobianForMotion;
		private final Matrix pseudoInverseJacobianForNullspace;

		public JacobianAndInverse( Matrix jacobian, Matrix pseudoInverseJacobianForMotion, Matrix pseudoInverseJacobianForNullspace ) {
			this.jacobian = jacobian;
			this.pseudoInverseJacobianForMotion = pseudoInverseJacobianForMotion;
			this.pseudoInverseJacobianForNullspace = pseudoInverseJacobianForNullspace;
		}

		public Matrix getJacobian() {
			return jacobian;
		}

		public Matrix getPseudoInverseJacobianForMotion() {
			return pseudoInverseJacobianForMotion;
		}

		public Matrix getPseudoInverseJacobianForNullspace() {
			return pseudoInverseJacobianForNullspace;
		}
	}

	private final java.util.List<Chain> chains = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<Constraint> constraints = new java.util.ArrayList<Solver.Constraint>();

	private Map<Bone, Map<Axis, Vector3[]>> jacobianColumns;

	class DesiredVelocity {
		public final Vector3 velocity;
		public final boolean isLinear;

		DesiredVelocity( Vector3 velocity, boolean isLinear ) {
			this.velocity = velocity;
			this.isLinear = isLinear;
		}
	}

	private DesiredVelocity[] desiredVelocities;

	public void addChain( Chain chain ) {
		this.chains.add( chain );
	}

	public void removeChain( Chain chain ) {
		this.chains.remove( chain );
	}

	public double calculatePseudoInverseErrorForTime( JacobianAndInverse jacobianAndInverse, double dt ) {
		//will calculate I-(JJ^-1)

		Matrix jj11 = jacobianAndInverse.getJacobian().times( jacobianAndInverse.getPseudoInverseJacobianForMotion() ).times( -1 );

		int n = jj11.getRowDimension();

		for( int i = 0; i < n; ++i ) {
			jj11.set( i, i, 1 + jj11.get( i, i ) );
		}

		double dtn = dt;

		for( int i = 1; i < n; ++i ) {
			dtn *= dt;
		}

		double error = jj11.det() * dtn;

		return error;
	}

	public JacobianAndInverse prepareAndCalculateJacobianAndInverse() {
		prepareConstraints();
		if( constraints.size() == 0 ) {
			System.out.println( "no constraints!" );
			return null;
		}
		constructJacobianEntries();

		Matrix jacobian = createJacobian();

		JacobianAndInverse jacobianAndInverse = invertJacobian( jacobian );

		return jacobianAndInverse;
	}

	/**
	 * This is called just by itself to solve everything. Use it as an example
	 * when you are doing adaptive.
	 * 
	 * @return The angle speeds
	 */
	public Map<Bone, Map<Axis, Double>> solve() {
		JacobianAndInverse jacobianAndInverse = prepareAndCalculateJacobianAndInverse();

		return calculateAngleSpeeds( jacobianAndInverse );
	}

	public Map<Bone, Map<Axis, Double>> projectToNullSpace( JacobianAndInverse jacobianAndInverse, Map<Bone, Map<Axis, Double>> desiredJointSpeeds ) {
		//make sure you use the correct order, the order that you use to go from jama matrix to maps

		Matrix j1j = jacobianAndInverse.getPseudoInverseJacobianForNullspace().times( jacobianAndInverse.getJacobian() );

		Matrix projector = Matrix.identity( j1j.getRowDimension(), j1j.getColumnDimension() ).minus( j1j );

		//put the map into the column vector
		Matrix desiredJointSpeedsVector = createThetadotFromBoneSpeeds( desiredJointSpeeds, j1j.getColumnDimension() );

		//multiply
		Matrix projectedJointSpeedsVector = projector.times( desiredJointSpeedsVector );

		return createBoneSpeedsFromThetadot( projectedJointSpeedsVector );
	}

	public Map<Bone, Map<Axis, Double>> calculateAngleSpeeds( JacobianAndInverse jacobianAndInverse ) {
		Map<Bone, Map<Axis, Double>> angleSpeeds = calculateAngleSpeeds( jacobianAndInverse.getPseudoInverseJacobianForMotion() );

		return angleSpeeds;
	}

	private Map<Bone, Map<Axis, Double>> calculateAngleSpeeds( Jama.Matrix ji ) {
		Jama.Matrix pDot = createDesiredVelocitiesColumn( desiredVelocities );
		Jama.Matrix mThetadot = ji.times( pDot );

		return createBoneSpeedsFromThetadot( mThetadot );
	}

	public void addAngleSpeedsTowardsDefaultPoseInNullSpace( Map<JointImp, OrthogonalMatrix3x3> defaultPose, Map<Bone, Map<Axis, Double>> angleSpeeds, JacobianAndInverse jacobianAndInverse ) {
		Map<Bone, Map<Axis, Double>> additionalSpeeds = new HashMap<Bone, Map<Axis, Double>>();

		for( Entry<Bone, Map<Axis, Double>> ae : angleSpeeds.entrySet() ) {
			Bone bone = ae.getKey();
			Map<Axis, Double> axisToSpeed = ae.getValue();

			Map<Axis, Double> additionalSpeedsForBone = new HashMap<Axis, Double>();
			additionalSpeeds.put( bone, additionalSpeedsForBone );

			OrthogonalMatrix3x3 desiredOrientation = defaultPose.get( bone.getA() );
			if( desiredOrientation == null ) {
				continue;
			}

			OrthogonalMatrix3x3 currentOrientation = bone.getCurrentOrientationFromJoint();

			OrthogonalMatrix3x3 m = new OrthogonalMatrix3x3( currentOrientation );
			m.invert();
			//tried the other way around, didn't matter. FIXME but it should be desired X current's inverse according to maths 
			m.setToMultiplication( m, desiredOrientation );
			OrthogonalMatrix3x3 rotation = m;

			AxisRotation axisRotation = rotation.createAxisRotation();

			double scale = IkConstants.NULLSPACE_DEFAULT_POSE_MOTION_SCALE;

			double amount = axisRotation.angle.getAsRadians() * scale;

			Vector3 rotationAxis = axisRotation.axis;

			//project rotation axis on the three axes. then scale with amount and add to them. 
			//I could have projected the scaled vector but it's the same thing. I guess that requires less computation so I'll do that instead.

			Vector3 rotationVector = Vector3.createMultiplication( rotationAxis, amount );

			//project this on the axes, later project the whole thing on the nullspace and then add the result to angleSpeeds.
			for( Entry<Axis, Double> e : axisToSpeed.entrySet() ) {
				Axis axis = e.getKey();

				Vector3 localAxis = axis.getLocalAxis();

				assert !additionalSpeeds.containsKey( axis );

				double value = Vector3.calculateDotProduct( rotationVector, localAxis );

				additionalSpeedsForBone.put( axis, value );
			}
		}

		//we know that the rest from here work. it's what's above that messes things up. 
		//project the whole thing on the nullspace and then add the result to angleSpeeds.
		Map<Bone, Map<Axis, Double>> projectedExtraThetaDot;
		projectedExtraThetaDot = projectToNullSpace( jacobianAndInverse, additionalSpeeds );

		for( Entry<Bone, Map<Axis, Double>> ae : projectedExtraThetaDot.entrySet() ) {
			Bone bone = ae.getKey();
			Map<Axis, Double> axisToAdditionalSpeed = ae.getValue();

			Map<Axis, Double> axisToCurrentSpeed = angleSpeeds.get( bone );

			for( Entry<Axis, Double> e : axisToAdditionalSpeed.entrySet() ) {
				Axis axis = e.getKey();

				axisToCurrentSpeed.put( axis, axisToCurrentSpeed.get( axis ) + e.getValue() );
			}
		}
	}

	private Map<Bone, Map<Axis, Double>> createBoneSpeedsFromThetadot( Jama.Matrix mThetadot ) {
		Map<Bone, Map<Axis, Double>> boneSpeeds = new HashMap<Bone, Map<Axis, Double>>();

		//trusting that this will give me the same order of axes as in createJacobianMatrix(). No reason to doubt this.  

		int row = 0;
		for( Entry<Bone, Map<Axis, Vector3[]>> jce : jacobianColumns.entrySet() ) {
			Bone bone = jce.getKey();
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();

			HashMap<Bone.Axis, Double> axisSpeeds = new HashMap<Bone.Axis, Double>();
			boneSpeeds.put( bone, axisSpeeds );

			for( Entry<Axis, Vector3[]> e : columnsForBone.entrySet() ) {
				Axis axis = e.getKey();

				axisSpeeds.put( axis, mThetadot.get( row, 0 ) );

				++row;
			}
		}
		return boneSpeeds;
	}

	private Jama.Matrix createThetadotFromBoneSpeeds( Map<Bone, Map<Axis, Double>> boneSpeeds, int sizeHelper ) {
		Jama.Matrix mThetadot = new Matrix( sizeHelper, 1 );

		int row = 0;
		for( Entry<Bone, Map<Axis, Vector3[]>> jce : jacobianColumns.entrySet() ) {
			Bone bone = jce.getKey();
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();

			Map<Axis, Double> axisSpeeds = boneSpeeds.get( bone );

			for( Entry<Axis, Vector3[]> e : columnsForBone.entrySet() ) {
				Axis axis = e.getKey();

				mThetadot.set( row, 0, axisSpeeds.get( axis ) );

				++row;
			}
		}

		assert row == sizeHelper;

		return mThetadot;
	}

	private void prepareConstraints() {
		constraints.clear();
		for( Chain chain : chains ) {
			//calculate contributions on the last joint location
			Vector3 desiredLinearVelocity = desiredLinearVelocities.get( chain );
			Vector3 desiredAngularVelocity = desiredAngularVelocities.get( chain );

			if( ( desiredLinearVelocity != null ) || ( desiredAngularVelocity != null ) ) {
				chain.updateStateFromJoints();

				if( desiredLinearVelocity != null ) {
					chain.computeLinearVelocityContributions();
					//give this to a constraint set, together with the desired velocity (chain has it). 
					constraints.add( new Constraint( chain.getLinearVelocityContributions(), new DesiredVelocity( desiredLinearVelocity, true ) ) );
				}
				if( desiredAngularVelocity != null ) {
					chain.computeAngularVelocityContributions();
					//give this to a constraint set, together with the desired velocity.
					constraints.add( new Constraint( chain.getAngularVelocityContributions(), new DesiredVelocity( desiredAngularVelocity, false ) ) );
				}
			}
		}
		desiredLinearVelocities.clear();
		desiredAngularVelocities.clear();
	}

	private void constructJacobianEntries() {
		//need to align axes in different constraints 
		//need to make it into a matrix
		//does making a map with lists make sense?
		//or all lists?
		//I think it's time to get orderly. all lists. 

		//pass one, create arrays
		jacobianColumns = new HashMap<Bone, Map<Axis, Vector3[]>>();
		desiredVelocities = new DesiredVelocity[ constraints.size() ];
		//		isDesiredVelocityLinear = new 
		for( Constraint constraint : constraints ) {
			for( Entry<Bone, Map<Axis, Vector3>> e : constraint.contributions.entrySet() ) {
				Bone bone = e.getKey();
				Map<Axis, Vector3> contributionsForBone = e.getValue();

				Map<Axis, Vector3[]> columnForBone = new HashMap<Bone.Axis, Vector3[]>();
				jacobianColumns.put( bone, columnForBone ); //FIXME if it doesn't already exist? (chains sharing bones)

				for( Entry<Axis, Vector3> eb : contributionsForBone.entrySet() ) {
					Axis axis = eb.getKey();

					if( columnForBone.containsKey( axis ) ) {
						throw new RuntimeException( "Axis already in column" );
					}

					columnForBone.put( axis, new edu.cmu.cs.dennisc.math.Vector3[ constraints.size() ] );
				}
			}
		}

		//pass two, fill arrays
		for( Entry<Bone, Map<Axis, Vector3[]>> jce : jacobianColumns.entrySet() ) {
			Bone bone = jce.getKey();
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();
			for( Entry<Axis, Vector3[]> e : columnsForBone.entrySet() ) {
				Axis axis = e.getKey();
				Vector3[] contributions = e.getValue();

				int row = 0;
				for( Constraint constraint : constraints ) {
					Map<Axis, Vector3> contributionsForBone = constraint.contributions.get( bone );

					boolean found = false;
					if( contributionsForBone != null ) {
						Vector3 contribution = contributionsForBone.get( axis );

						if( contribution != null ) {
							found = true;
							contributions[ row ] = contribution;
						}
					}
					if( !found ) {
						contributions[ row ] = Vector3.accessOrigin();
					}

					desiredVelocities[ row ] = constraint.desiredValue;
					++row;
				}
			}
		}
	}

	private Jama.Matrix createJacobian() {
		return createJacobianMatrix( this.jacobianColumns );
	}

	private JacobianAndInverse invertJacobian( Jama.Matrix jacobian ) {
		Jama.Matrix mj = jacobian;

		boolean transposed = false;
		int m = mj.getRowDimension();
		int n = mj.getColumnDimension();
		if( m < n ) {
			transposed = true;
		}

		if( transposed ) {
			mj = mj.transpose();
		}

		Jama.SingularValueDecomposition svd = new Jama.SingularValueDecomposition( mj );

		Jama.Matrix u = svd.getU();
		Jama.Matrix s = svd.getS();
		Jama.Matrix v = svd.getV();

		Jama.Matrix sForBasic = s.copy();

		reduceAndInvertSofSvdByDamping( s, IkConstants.SVD_DAMPING_CONSTANT );
		reduceAndInvertSofSvdBasically( sForBasic );

		Jama.Matrix pseudoInverseForMotion = v.times( s ).times( u.transpose() );
		Jama.Matrix pseudoInverseForNullspace = v.times( sForBasic ).times( u.transpose() );

		if( transposed ) { //TODO perhaps record the fact that matrices are transposed and act accordingly. 
			pseudoInverseForMotion = pseudoInverseForMotion.transpose();
			pseudoInverseForNullspace = pseudoInverseForNullspace.transpose();
		}

		return new JacobianAndInverse( jacobian, pseudoInverseForMotion, pseudoInverseForNullspace );
	}

	private Jama.Matrix createDesiredVelocitiesColumn( DesiredVelocity[] desiredVelocities2 ) {
		Jama.Matrix rv = new Jama.Matrix( desiredVelocities2.length * 3, 1 );

		int row = 0;
		for( DesiredVelocity desiredVelocity : desiredVelocities2 ) {
			rv.set( row, 0, desiredVelocity.velocity.x );
			rv.set( row + 1, 0, desiredVelocity.velocity.y );
			rv.set( row + 2, 0, desiredVelocity.velocity.z );
			row += 3;
		}

		return rv;
	}

	private Jama.Matrix createJacobianMatrix( Map<Bone, Map<Axis, edu.cmu.cs.dennisc.math.Vector3[]>> jacobianColumns ) {
		boolean found = false;
		int numConstraints = 0;
		int numColumns = 0;
		//calculate sizes

		for( Entry<Bone, Map<Axis, Vector3[]>> jce : jacobianColumns.entrySet() ) {
			Map<Axis, Vector3[]> columnsForBone = jce.getValue();

			if( !found ) {
				for( Entry<Axis, Vector3[]> e : columnsForBone.entrySet() ) {
					numConstraints = e.getValue().length;
					found = true;
					break;
				}
			}

			numColumns += columnsForBone.size();
		}

		Jama.Matrix j = new Jama.Matrix( numConstraints * 3, numColumns );

		int column = 0;

		for( Entry<Bone, Map<Axis, Vector3[]>> jce : jacobianColumns.entrySet() ) {
			Bone bone = jce.getKey();
			double weight = weights.getEffectiveJointWeight( bone.getA().getJointId() );

			Map<Axis, Vector3[]> columnsForBone = jce.getValue();

			for( Entry<Axis, Vector3[]> e : columnsForBone.entrySet() ) {
				Vector3[] contributions = e.getValue();

				int row = 0;
				for( edu.cmu.cs.dennisc.math.Vector3 contribution : contributions ) {
					j.set( row, column, contribution.x * weight );
					j.set( row + 1, column, contribution.y * weight );
					j.set( row + 2, column, contribution.z * weight );

					row += 3;
				}

				++column;
			}
		}

		return j;
	}

	private void reduceAndInvertSofSvdBasically( Matrix s ) {
		assert ( s.getRowDimension() == s.getColumnDimension() );
		for( int i = 0; i < s.getRowDimension(); ++i ) {
			s.set( i, i, 1.0 / s.get( i, i ) );
		}
	}

	private void reduceAndInvertSofSvdByDamping( Matrix s, double svdDampingConstant ) {
		assert ( s.getRowDimension() == s.getColumnDimension() );
		for( int i = 0; i < s.getRowDimension(); ++i ) {
			double d = s.get( i, i );
			s.set( i, i, d / ( ( d * d ) + ( svdDampingConstant * svdDampingConstant ) ) );
		}
	}

	private void reduceAndInvertSofSvdByClampingSmallEntries( Jama.Matrix s, double threshold ) {
		assert ( s.getRowDimension() == s.getColumnDimension() );
		for( int i = 0; i < s.getRowDimension(); ++i ) {
			if( s.get( i, i ) < threshold ) {
				s.set( i, i, 0.0 );
				assert threshold > 0.0;
			} else {
				s.set( i, i, 1.0 / s.get( i, i ) );
			}
		}
	}

	java.util.Map<Chain, edu.cmu.cs.dennisc.math.Vector3> desiredLinearVelocities = new java.util.HashMap<Chain, edu.cmu.cs.dennisc.math.Vector3>();
	java.util.Map<Chain, edu.cmu.cs.dennisc.math.Vector3> desiredAngularVelocities = new java.util.HashMap<Chain, edu.cmu.cs.dennisc.math.Vector3>();

	public void setDesiredEndEffectorLinearVelocity( Chain chain, edu.cmu.cs.dennisc.math.Vector3 linVelToUse ) {
		//		chain.setDesiredEndEffectorLinearVelocity(linVelToUse);
		desiredLinearVelocities.put( chain, linVelToUse );
	}

	public void setDesiredEndEffectorAngularVelocity( Chain chain, edu.cmu.cs.dennisc.math.Vector3 angVelToUse ) {
		//		chain.setDesiredEndEffectorAngularVelocity(angVelToUse);
		desiredAngularVelocities.put( chain, angVelToUse );
	}

	private Weights weights;

	public void setJointWeights( Weights weights ) {
		this.weights = weights;
	}

}
