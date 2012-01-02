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

package org.lgna.ik;

/**
 * @author Dennis Cosgrove
 */
public class Solver {
	
	private class Constraint {
		private final java.util.Map<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3> contributions;
		private final edu.cmu.cs.dennisc.math.Vector3 desiredValue;

		public Constraint(java.util.Map<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3> contributions, edu.cmu.cs.dennisc.math.Vector3 desiredValue) {
			this.contributions = contributions;
			this.desiredValue = desiredValue;
		}
		
	}
	
	private final java.util.List< Chain > chains = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	private final java.util.List< Constraint > constraints = new java.util.ArrayList<Solver.Constraint>();
	private java.util.Map<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]> jacobianColumns;
	private edu.cmu.cs.dennisc.math.Vector3[] desiredVelocities;
	
	public void addChain( Chain chain ) {
		this.chains.add( chain );
	}
	public void removeChain( Chain chain ) {
		this.chains.remove( chain );
	}
	
	public java.util.Map<org.lgna.ik.Bone.Axis, Double> solve() {
		prepareConstraints();
		if(constraints.size() == 0) {
			System.out.println("no constraints!");
			return null;
		}
		constructJacobianEntries();
		
		return calculateAngleSpeeds(invertJacobian(0.1));
		//TODO apply them? maybe someone else should do it. 
	}
	
	private java.util.Map<org.lgna.ik.Bone.Axis, Double> calculateAngleSpeeds(Jama.Matrix ji) {
		// TODO Auto-generated method stub
		Jama.Matrix pDot = createDesiredVelocitiesColumn(desiredVelocities);
		Jama.Matrix mThetadot = ji.times(pDot);

		java.util.Map<org.lgna.ik.Bone.Axis, Double> axisSpeeds = new java.util.HashMap<Bone.Axis, Double>();
		
		int row = 0;
		//trusting that this will give me the same order of axes as in createJacobianMatrix(). No reason to doubt this.  
		for(java.util.Map.Entry<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]> e: jacobianColumns.entrySet()) {
			org.lgna.ik.Bone.Axis axis = e.getKey();
			axisSpeeds.put(axis, mThetadot.get(row, 0));
			
			++row;
		}
		
		return axisSpeeds;
	}
	private void prepareConstraints() {
		constraints.clear();
		for(Chain chain: chains) {
			//calculate contributions on the last joint location
			if(chain.isLinearVelocityEnabled() || chain.isAngularVelocityEnabled()) {
				chain.computeVelocityContributions();
			}
			
			if(chain.isLinearVelocityEnabled()) {
				//give this to a constraint set, together with the desired velocity (chain has it). 
				constraints.add(new Constraint(chain.getLinearVelocityContributions(), chain.getDesiredEndEffectorLinearVelocity()));
				System.out.println("lin");
			} else {
				System.out.println("nolin");
			}
			if(chain.isAngularVelocityEnabled()) {
				//give this to a constraint set, together with the desired velocity.
				constraints.add(new Constraint(chain.getAngularVelocityContributions(), chain.getDesiredEndEffectorAngularVelocity()));
				System.out.println("ang");
			} else {
				System.out.println("noang");
			}
		}
	}
	private void constructJacobianEntries() {
		//need to align axes in different constraints 
		//need to make it into a matrix
		//does making a map with lists make sense?
		//or all lists?
		//I think it's time to get orderly. all lists. 
		
		//pass one, create arrays
		jacobianColumns = new java.util.HashMap<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]>();
		desiredVelocities = new edu.cmu.cs.dennisc.math.Vector3[constraints.size()];
		for(Constraint constraint: constraints) {
			for(java.util.Map.Entry<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3> e: constraint.contributions.entrySet()) {
				org.lgna.ik.Bone.Axis axis = e.getKey();
				
				if(!jacobianColumns.containsKey(axis)) {
					jacobianColumns.put(axis, new edu.cmu.cs.dennisc.math.Vector3[constraints.size()]);
				}
			}
		}
		
		//pass two, fill arrays
		for(java.util.Map.Entry<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]> e: jacobianColumns.entrySet()) {
			org.lgna.ik.Bone.Axis axis = e.getKey();
			edu.cmu.cs.dennisc.math.Vector3[] contributions = e.getValue();
			
			int row = 0;
			for(Constraint constraint: constraints) {
				if(constraint.contributions.containsKey(axis)) {
					edu.cmu.cs.dennisc.math.Vector3 contribution = constraint.contributions.get(axis);
					contributions[row] = contribution;
				} else {
					contributions[row] = edu.cmu.cs.dennisc.math.Vector3.accessOrigin();
				}
				desiredVelocities[row] = constraint.desiredValue;
				++row;
			}
		}
	}
	private Jama.Matrix invertJacobian(double sThreshold) {
		Jama.Matrix j = createJacobianMatrix(this.jacobianColumns);
		System.out.println("jacobian " + j.getRowDimension() + "x" + j.getColumnDimension());
		Jama.Matrix ji = svdInvert(j, sThreshold);

		// TODO also do any optimizations (don't move too fast, etc)
		
		return ji;
	}
	
	
	
	
	private Jama.Matrix createDesiredVelocitiesColumn(edu.cmu.cs.dennisc.math.Vector3[] desiredVelocities) {
		Jama.Matrix rv = new Jama.Matrix(desiredVelocities.length * 3, 1);

		int row = 0;
		for(edu.cmu.cs.dennisc.math.Vector3 desiredVelocity: desiredVelocities) {
			rv.set(row, 0, desiredVelocity.x);
			rv.set(row + 1, 0, desiredVelocity.y);
			rv.set(row + 2, 0, desiredVelocity.z);
			row += 3;
		}
		
		return rv;
	}
	private Jama.Matrix createJacobianMatrix(java.util.Map<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]> jacobianColumns) {
		if(jacobianColumns.size() == 0) {
			throw new RuntimeException("Jacobian matrix should have columns.");
		}
		int numConstraints = jacobianColumns.values().iterator().next().length;
		
		Jama.Matrix j = new Jama.Matrix(numConstraints * 3, jacobianColumns.size());
		
		int column = 0;
		for(java.util.Map.Entry<org.lgna.ik.Bone.Axis, edu.cmu.cs.dennisc.math.Vector3[]> e: jacobianColumns.entrySet()) {
			edu.cmu.cs.dennisc.math.Vector3[] contributions = e.getValue();
			
			int row = 0;
			for(edu.cmu.cs.dennisc.math.Vector3 contribution: contributions) {
				j.set(row, column, contribution.x);
				j.set(row + 1, column, contribution.y);
				j.set(row + 2, column, contribution.z);
				
				row += 3;
			}
			
			++column;
		}
		return j;
	}
	
	private Jama.Matrix svdInvert(Jama.Matrix mj, double threshold) {
		
		boolean transposed = false;
		int m = mj.getRowDimension();
		int n = mj.getColumnDimension();
		if(m < n) {
			transposed = true;
		} 

		if(transposed) {
			mj = mj.transpose();
		}
		
		Jama.SingularValueDecomposition svd = new Jama.SingularValueDecomposition(mj);
		
		Jama.Matrix u = svd.getU();
		
		Jama.Matrix s = svd.getS();
		
		Jama.Matrix v = svd.getV();
		
		//reduce s
		assert(s.getRowDimension() == s.getColumnDimension());
		for(int i = 0; i < s.getRowDimension(); ++i) {
			if(s.get(i, i) < threshold) {
				System.out.printf("A value in S was lower than threshold %f < %f. Correcting.\n", s.get(i, i), threshold);
				s.set(i, i, 0.0);
				assert threshold > 0.0;
			} else {
				s.set(i, i, 1.0 / s.get(i, i));
			}
		}
		
		Jama.Matrix result = v.times(s).times(u.transpose());
		
		if(transposed) {
			result = result.transpose();
		}
		
		return result;
	}

}
