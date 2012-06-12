package org.lgna.ik.enforcer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lgna.ik.solver.Bone.Axis;
import org.lgna.ik.solver.Chain;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import Jama.Matrix;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;

public class TightPositionalIkEnforcer extends IkEnforcer {
	
	public class Jacobian {
		//WHAT axis -> displacement
			//really displacement or a matrix?
			//it's got to be a matrix at some point so that I can invert it. 
			//so it makes sense to keep it as a matrix? yeah. 
		//only a handful of axes (ones that are in the collection of constraints)
		
		Matrix matrix;
		
		public Jacobian() {
			//TODO probably accept a chain and go with it. it would make sense not to recreate this over and over. update it every frame. TODO need a function for that
			//the class Constraint is probably the only thing that calls this.
			throw new RuntimeException("Not implemented constructor"); // TODO Auto-generated constructor stub
		}
		
		public Displacement multiplyWithAngleDeltas(AngleDeltas angleDeltas) {
			//assume they all have arrays.
			double[] d = new double[matrix.getRowDimension()]; //TODO perhaps not create a new one every time and reuse?
			
			for(int rowi = 0; rowi < matrix.getRowDimension(); ++rowi) { //vectorize?
				d[rowi] = 0.0;
				for(int coli = 0; coli < matrix.getColumnDimension(); ++coli) {
					d[rowi] += angleDeltas.get(coli) * matrix.get(rowi, coli);
				}
			}
			
			return new Displacement(d);
		}
		//TODO temporary placeholder

		public AngleDeltas multiplyDisplacementWithInverseForMoving(Displacement displacement) {
			//TODO get the jacobian inversion code here
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		public InvertedJacobian createInverseForNullProjection() {
			//TODO get the jacobian inversion code here
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
	}
	
	public class InvertedJacobian {
		//TODO temporary placeholder
		//WHAT axis -> displacement
	}
	
	public static class Displacement {
		//WHAT can be either position or orientation change
		//TODO temporary placeholder
		double[] storage;
		
		public Displacement(double[] d) {
			storage = d;
		}

		public Displacement createThisMinusOther(Displacement other) {
			if(storage.length != other.storage.length) {
				throw new RuntimeException("Displacements have different lengths.");
			}
			double[] result = new double[storage.length];
			for(int i = 0; i < storage.length; ++i) { //vectorize?
				result[i] = storage[i] - other.storage[i];
			}
			
			return new Displacement(result);
		}

		public int size() {
			return storage.length;
		}
	}
	
	public class PriorityLevel {
		public int levelIndex;
		
		//has a bunch of constraints
		public List<Constraint> constraints = new ArrayList<Constraint>();

		private Jacobian currentJacobian;

		//TODO has one concatenated jacobian at a time (not thread-safe, but who needs it anyway?)
		public Jacobian computeAugmentedJacobian() {
			Jacobian[] jacobians = new Jacobian[constraints.size()];
			int i = 0;
			for(Constraint constraint: constraints) {
				Jacobian jacobian = constraint.computeJacobian();
				jacobians[i] = jacobian;
				++i;
			}
			
			currentJacobian = augmentJacobians(jacobians);
			return currentJacobian;
		}
		
		public Jacobian getCurrentJacobian() {
			return currentJacobian;
		}
		
		private Jacobian augmentJacobians(Jacobian[] jacobians) {
			//TODO could I devise a clever plan to get this done? maybe initially keep everything as subarrays of this?
			throw new RuntimeException("Not implemented method");
		}
		
		private Displacement currentAugmentedDesiredDisplacement;
		
		public Displacement computeAugmentedDesiredDisplacement() {
			Displacement[] displacements = new Displacement[constraints.size()];
			int size = 0;
			int i = 0; 
			for(Constraint constraint: constraints) {
				Displacement displacement = constraint.computeDesiredDisplacement();
				displacements[i] = displacement;
				++i;
				size += displacement.storage.length;
			}
			
			currentAugmentedDesiredDisplacement = augmentDisplacements(displacements, size);
			return currentAugmentedDesiredDisplacement;
		}
		
		public Displacement getCurrentAugmentedDesiredDisplacement() {
			return currentAugmentedDesiredDisplacement;
		}
		
		private Displacement augmentDisplacements(Displacement[] displacements, int size) {
			//TODO a clever plan maybe? like augmented jacobians?
			//vectorize?
			//reuse storage?
			double[] d = new double[size];
			int largeIndex = 0;
			for(Displacement displacement: displacements) {
				for(int i = 0; i < displacement.size(); ++i) {
					d[largeIndex + i] = displacement.storage[i];
				}
				largeIndex += displacement.size();
			}
			return new Displacement(d);
		}

		public boolean areConstraintsMet() {
			for(Constraint constraint: constraints) {
				if(!constraint.isMet()) {
					return false;
				}
			}
			return true;
		}
	}
	
	public abstract class Constraint {
		Chain chain;
		
//		//TODO feel free to change the joint type. 
//		// something that identifies the joint. perhaps not joint id but its implementation or whatever.
//		private JointId base;
//		private JointId ee;
//		
//		public void setBase(JointId base) {
//			this.base = base;
//		}
//		
//		public void setEe(JointId ee) {
//			this.ee = ee;
//		}
		
		public abstract boolean isMet();
		
		public abstract Displacement computeDesiredDisplacement();

		public abstract Jacobian computeJacobian();
	}
	
	public class PositionConstraint extends Constraint {
		private Point3 eeDesiredPosition;

		@Override
		public boolean isMet() {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
		
		public void setEeDesiredPosition(Point3 position) {
			this.eeDesiredPosition = position;
		}

		@Override
		public Displacement computeDesiredDisplacement() {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		@Override
		public Jacobian computeJacobian() {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
	}
	
	public class OrientationConstraint extends Constraint {
		private OrthogonalMatrix3x3 eeDesiredOrientation;

		@Override
		public boolean isMet() {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
		
		public void setEeDesiredOrientation(OrthogonalMatrix3x3 orientation) {
			this.eeDesiredOrientation = orientation;
		}

		@Override
		public Displacement computeDesiredDisplacement() {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		@Override
		public Jacobian computeJacobian() {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
	}
	
//	private List<Constraint> allConstraints = new ArrayList<Constraint>();
	
	//TODO keep this ordered
	private List<PriorityLevel> priorityLevels = new ArrayList<PriorityLevel>();
	
	public TightPositionalIkEnforcer(org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp) {
		super(jointedModelImp); 
		
		initializeListOfAxes();
	}

	//this is the mr representative of an axis in the actual model. there is one for each. it's not in jacobians. 
	class JacobianAxis { //FIXME my current jacobian does not make use of this. should it? where is this used?
		private final JointId jointId;
		private final int axisIndex;
		private boolean isFree = true;

		public JacobianAxis(JointId jointId, int axisIndex) {
			this.jointId = jointId;
			this.axisIndex = axisIndex;
		}
		
		public boolean isFree() {
			return isFree;
		}
		
		public void setFree(boolean isFree) {
			this.isFree = isFree;
		}

		public void applyCorrespondingDelta(AngleDeltas angleDeltas) {
			//TODO make the change to the actual joint
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		public boolean wentOverLimit() {
			// did the angle cross the joint limit?
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		public double setToLimitAndReturnTheDifference() {
			//TODO make the change to the actual joint
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
	}
	
	//are these all the joints? yes. 
	//TODO make sure that it's the assumption everywhere
	List<JacobianAxis> indexToAxis;
	Map<JacobianAxis, Integer> axisToIndex;
	
	class NullspaceProjector {
		//all the joints that are in indexToAxis is the size
		double[][] matrix;
		private final int numAxes;
		
		public NullspaceProjector(int numAxes) {
			this.numAxes = numAxes;
			matrix = new double[numAxes][numAxes];
		}

		public void initializeToIdentity() {
			for(int row = 0; row < numAxes; ++row) {
				for(int col = 0; col < numAxes; ++col) {
					matrix[row][col] = 0.0;
				}
				matrix[row][row] = 1.0;
			}
		}

		public Jacobian createProjected(Jacobian currentJacobian) {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		public void subtractInverseTimesJacobian(InvertedJacobian inverse, Jacobian jacobian) {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		public void setIndexToLocked(int index) {
			//TODO should effectively zero the row and col of this index, and one the intersection
			// if it's going to be more efficient, maybe do it lazily.
			// it probably wouldn't be if I figure out a way of doing it with vectors and parallel programming
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
		
	}
	
	NullspaceProjector nullspaceProjector;
	
	private void initializeListOfAxes() {
		//in a list, I'll hold the list of axes (or angles).
		// what type do they have? Axis? no, that's an IK thing. 
		// joint, number pair?
		
		indexToAxis = new ArrayList<TightPositionalIkEnforcer.JacobianAxis>();
		axisToIndex = new HashMap<JacobianAxis, Integer>();
		
		Iterable<JointImp> jointImps = jointedModelImp.getJoints();
		
		for(JointImp jointImp: jointImps) {
			for(int index = 0; index < 3; ++index) {
				JacobianAxis jacobianAxis = new JacobianAxis(jointImp.getJointId(), index);
				indexToAxis.add(jacobianAxis);
				axisToIndex.put(jacobianAxis, index);
			}
		}
		
		nullspaceProjector = new NullspaceProjector(indexToAxis.size());
	}

	public void enforceConstraints() {
		//convergence loop (constraints or max iter)
		//clamping loop (joint limits, clamping)
		//priority loop (apply constraints in current null space)
		
		convergenceLoop();
	}

	//1. COMPUTE JACOBIANS
	//a jacobian is a list of velocity contributions and a list of angle indices
	//those angle indices are predictable, from 0 to num angles, and are indices to the large nullspace projector
	
	//first of all, you need to know that this enforcer is for a specific character
		//how many angles does that character have? this will be the size of my nullspace projector
			//alternatively, I can count the angles in active chains and make the nullspace projector's size be that 
	//to compute a jacobian, you need to 
		//know the chain
			//base and ee bones, axes and angles in between, position of the end effector point wrt end effector frame 
		//know the current positions and directions of axes
	
	//a chain contains one goal: either a position or an orientation goal. to enforce both, you need two chains.   
	
	//jacobians are per-priority-level. there can be multiple chains in a priority level. you vertically concatenate their contribution vectors.
	
	
	private void convergenceLoop() {
		int numIterations = 0;
		int maxNumIterations = 10;
		
		angleDeltas = new AngleDeltas(indexToAxis.size());
		
		while(numIterations < maxNumIterations && !areConstraintsMet()) {
			// compute current jacobians for all priority levels.  
				// I had some reservations, maybe calculating jacobians should be inside clamping loop? it seems it's too much of an optimization because it will try one more time for convergence.
			
			for(PriorityLevel priorityLevel: priorityLevels) {
				priorityLevel.computeAugmentedJacobian();
				priorityLevel.computeAugmentedDesiredDisplacement();
			}
			
			nullspaceProjector.initializeToIdentity();
			
			// if I would have to do anything about locking state and joint limits, I would initialize them here. 
			clampingLoop();
			++numIterations;
		}
	}
	
	private boolean areConstraintsMet() {
//		for(Constraint constraint: allConstraints) {
//			if(!constraint.isMet()) {
//				return false;
//			}
//		}
		
		for(PriorityLevel priorityLevel: priorityLevels) {
			if(!priorityLevel.areConstraintsMet()) {
				return false;
			}
		}
		
		return true;
	}
	
	private void clampingLoop() {
		// ensure that priority levels are appropriately initialized (ui changes, whatever)
		// 
		boolean isComputedWithClamping = false;
		
		while(!isComputedWithClamping) {
			isComputedWithClamping = false; //make it true when you clamp
			
			priorityLoop();
			
			makeCloserToNaturalPose();
			
			isComputedWithClamping = applyAngleChangesAndClampingIfNecessary();
		}
	}

	//this is the cumulative angledeltas. just like nullspaceprojector.
	class AngleDeltas {
		//WHAT axis->value. not really, only a large number of angles, most not used. 
		//could have a huge array with unused axes being unused. or, could keep the actual axis->value for only the axes that are used. 
		
		double[] storage;
		
		public AngleDeltas(int numAllPossibleAxes) {
			storage = new double[numAllPossibleAxes];
		}

		public void add(AngleDeltas other) {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}
		//TODO placeholder
		//this is applied to actual angles at every clamping test and convergence test
		// I mean, how else would you test for convergence? gets pretty hard. 

		public double get(int coli) {
			throw new RuntimeException("Not implemented method"); // TODO Auto-generated method stub
		}

		public void correctDeltaForAxis(int index, double correction) {
			//subtract correction from the angle delta that corresponds to the axis (index)
			storage[index] -= correction; //angle continuity? deltas should never get close to 2pi, so, no.
		}
	}
	
	AngleDeltas angleDeltas;
	
	private void priorityLoop() {
		//loop through the priority levels and compute the angle delta vector incrementally with each
		int lastPriorityLevel = 0;
		boolean first = true;
		for(PriorityLevel priorityLevel: priorityLevels) {
			// verify the order (temporarily)
			if(!first) {
				if(lastPriorityLevel > priorityLevel.levelIndex) {
					throw new RuntimeException("priorities are not ordered " + lastPriorityLevel + " " + priorityLevel.levelIndex);
				}
			} else {
				first = false;
			}
			lastPriorityLevel = priorityLevel.levelIndex;
			// /verify the order (temporarily)
			
			priorityLoopIteration(priorityLevel);
		}
	}
	
	private void priorityLoopIteration(PriorityLevel priorityLevel) {
		// desired displacement (augmented) (delta x_i)
		Displacement desiredDisplacement = priorityLevel.getCurrentAugmentedDesiredDisplacement();
		// current angle deltas (delta theta)
			//this I need to keep during one iteration of clamping loop
			//angleDeltas
		// jacobian of this level (J_i)
			//priorityLevel.getCurrentJacobian();
		// current nullspace projector (P_N)
			//nullspaceProjector
		// 
		
		// calculate the displacement that the current angle deltas would cause and subtract it from desired displacement
		// delta ^x_i
		Jacobian currentJacobian = priorityLevel.getCurrentJacobian();
		Displacement remainingDesiredDisplacement = createRemainingDesiredDisplacement(desiredDisplacement, currentJacobian, angleDeltas);
		
		// project jacobian on the nullspace
		Jacobian projectedJacobian = nullspaceProjector.createProjected(currentJacobian); 
		
		// add to angle delta, compute nullspace projectors, etc
		addRequiredMotionToCurrentAngleDeltas(angleDeltas, projectedJacobian, remainingDesiredDisplacement);
		
		updateNullspaceProjector(nullspaceProjector, projectedJacobian);
	}

	private Displacement createRemainingDesiredDisplacement(
			Displacement desiredDisplacement, Jacobian currentJacobian,
			AngleDeltas currentAngleDeltas) {
		// multiply jacobian and currentAngleDeltas 
		Displacement alreadyMoved = currentJacobian.multiplyWithAngleDeltas(currentAngleDeltas);
		// create the difference between desiredDisplacement and the multiplication above 
		return desiredDisplacement.createThisMinusOther(alreadyMoved);
	}
	
	private void addRequiredMotionToCurrentAngleDeltas(
			AngleDeltas currentAngleDeltas, Jacobian projectedJacobian,
			Displacement remainingDesiredDisplacement) {
		AngleDeltas angleDeltasToMove = projectedJacobian.multiplyDisplacementWithInverseForMoving(remainingDesiredDisplacement);
		//update currentAngleDeltas
		currentAngleDeltas.add(angleDeltasToMove);
	}

	private void updateNullspaceProjector(NullspaceProjector currentNullspaceProjector, Jacobian jacobian) {
		InvertedJacobian inverse = jacobian.createInverseForNullProjection();
		
		currentNullspaceProjector.subtractInverseTimesJacobian(inverse, jacobian);
	}
	
	private void makeCloserToNaturalPose() {
		// TODO use the current nullspace projector to find an additional angle delta 
		// that would move us closer to a natural pose
		// TODO for now I'm ignoring this. TBD
		System.out.println("implement this at some point");
	}

	private boolean applyAngleChangesAndClampingIfNecessary() {
		// everything in the box that starts with clamping detected=no
		boolean clamped = false;

		// actually apply the current angle deltas to the joints
		// clamp the ones that violate constraints. 
		// applyCorrespondingDelta probably would do it, but maybe I should filter out ones that I don't use. 
		for(int index = 0; index < indexToAxis.size(); ++index) {
			JacobianAxis axis = indexToAxis.get(index);
			if(axis.isFree()) {
				axis.applyCorrespondingDelta(angleDeltas);
				
				if(axis.wentOverLimit()) {
					axis.setFree(false);
					double correction = axis.setToLimitAndReturnTheDifference();
					angleDeltas.correctDeltaForAxis(index, correction);
					nullspaceProjector.setIndexToLocked(index);
					clamped = true;
				}
			}
		}
		
		//return true if clamped
		return clamped;
	}
	
}



//this was in convergeLoop()
// need to know how many possible angles are there
// initialize the nullspace projector to identity
	//what is this? a square matrix with angle dimensions - good. 
		//my chains have different sets of angles. 
	//CAN ENLARGE: If I compute it with a small number of axes, I can always insert a zero rowcol pair with a 1 in the diagonal
	//SHRINK: I should obviously be able to. however, should I keep things that I didn't use for further iterations? sure!
	//therefore, I'll either
		//1. keep an incremental matrix that you can submatrix depending on the angles you use
		//2. keep a huge matrix, enlarge your jacobians and do the huge matrix multiplications everytime 
			//keep the huge matrix, code your multiplications so that you use subparts of it in your multiplications.
				//for the jacobian, for every column, keep the index of the angle among all angles. 
				//This will help you address subparts of the nullspace projector correctly. 
			//instead of keeping indices, you can keep angle objects. you use them for addressing only. 
				//well, addressing with integers in arrays is much faster. I'm going for speed. do integer addresses. 
	//I think the first one makes sense. 
