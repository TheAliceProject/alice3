package org.lgna.ik.core.enforcer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.lgna.ik.core.IkConstants;
import org.lgna.ik.core.solver.Bone;
import org.lgna.ik.core.solver.Chain;
import org.lgna.ik.core.solver.Bone.Axis;
import org.lgna.story.implementation.AsSeenBy;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.resources.JointId;

import Jama.Matrix;
import edu.cmu.cs.dennisc.math.AxisRotation;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;

public class TightPositionalIkEnforcer extends IkEnforcer {

	//TODO need to update the desired pos/ori for constraints

	//TODO handle reversals correctly
	//Bone.Axis has the corrected one
	//JacobianAxis does not have the corrected one
	//my angledeltas have to have values that can be applied to the uncorrected original axes (since shared)
	//how about my jacobians? they should have contributions that are corrected. I believe they do, because of Bone.Axis
	//there seems to be a contradiction between the last two statements...

	//TODO should do my best to get rid of map lookups (mostly with JointAxis). make it 

	class Limit {

	}

	class SingleAxisLimit extends Limit {
		private final double minAngle;
		private final double maxAngle;

		public SingleAxisLimit( double minAngle, double maxAngle ) {
			this.minAngle = minAngle;
			this.maxAngle = maxAngle;
		}

		public double getMinAngle() {
			return minAngle;
		}

		public double getMaxAngle() {
			return maxAngle;
		}
	}

	class IndependentBallJointLimit extends Limit {

		//if null, no limit
		SingleAxisLimit aroundFirstBoneLimit;
		SingleAxisLimit aroundSecondBoneLimit;
		SingleAxisLimit betweenBonesLimit;

		//how should I define this?
		//angle between axis of first and axis of second

		//(1) rotation around the first bone
		//(2) rotation around the second bone
		//(3) angle between the two bones

		//how do you tell the difference between the first (1) and the second (2)?
		//completely depends on what the elbow axis is.
		//you can find (3) easily
		//1st: the angle that the rotation axis makes with the initial rotation axis in the first bone's frame 
		//2nd: the angle that the rotation axis makes with the initial rotation axis in the second bone's frame
	}

	class SvdInfo {
		boolean isTransposed;
		Matrix u;
		Matrix sForDamped;
		Matrix sForRegular;
		Matrix v;
		boolean isDampedReady;
		boolean isRegularReady;

		Matrix dampedPseudoInverse;
		Matrix regularPseudoInverse;

		public SvdInfo( Matrix mj ) {
			int m = mj.getRowDimension();
			int n = mj.getColumnDimension();
			if( m < n ) {
				isTransposed = true;
				mj = mj.transpose();
			} else {
				isTransposed = false;
			}

			Jama.SingularValueDecomposition svd = new Jama.SingularValueDecomposition( mj );

			u = svd.getU();
			Matrix s = svd.getS();
			v = svd.getV();

			sForRegular = s.copy();
			sForDamped = s;
		}

		Matrix createDampedInverse() {
			if( isDampedReady ) {
				return dampedPseudoInverse;
			}

			reduceAndInvertSofSvdByDamping( sForDamped, IkConstants.SVD_DAMPING_CONSTANT );

			Matrix pseudoInverseForMotion = v.times( sForDamped ).times( u.transpose() );

			if( isTransposed ) { //TODO perhaps record the fact that matrices are transposed and act accordingly. 
				pseudoInverseForMotion = pseudoInverseForMotion.transpose();
			}

			dampedPseudoInverse = pseudoInverseForMotion;
			isDampedReady = true;

			return dampedPseudoInverse;
		}

		private void reduceAndInvertSofSvdByDamping( Matrix s, double svdDampingConstant ) {
			assert ( s.getRowDimension() == s.getColumnDimension() );
			for( int i = 0; i < s.getRowDimension(); ++i ) {
				double d = s.get( i, i );
				s.set( i, i, d / ( ( d * d ) + ( svdDampingConstant * svdDampingConstant ) ) );
			}
		}

		Matrix createRegularInverse() {
			if( isRegularReady ) {
				return regularPseudoInverse;
			}

			reduceAndInvertSofSvdBasically( sForRegular );

			Jama.Matrix pseudoInverseForNullspace = v.times( sForRegular ).times( u.transpose() );

			if( isTransposed ) { //TODO perhaps record the fact that matrices are transposed and act accordingly. 
				pseudoInverseForNullspace = pseudoInverseForNullspace.transpose();
			}

			regularPseudoInverse = pseudoInverseForNullspace;
			isRegularReady = true;

			return regularPseudoInverse;
		}

		private void reduceAndInvertSofSvdBasically( Matrix s ) {
			assert ( s.getRowDimension() == s.getColumnDimension() );
			for( int i = 0; i < s.getRowDimension(); ++i ) {
				s.set( i, i, 1.0 / s.get( i, i ) );
			}
		}

	}

	public class Jacobian {
		//WHAT axis -> displacement
		//really displacement or a matrix?
		//it's got to be a matrix at some point so that I can invert it. 
		//so it makes sense to keep it as a matrix? yeah. 
		//only a handful of axes (ones that are in the collection of constraints)

		//whenever you have axis->... kind of thing, you can use integers from indexToAxis (axisToIndex gets this number)

		//TODO should start using http://code.google.com/p/efficient-java-matrix-library/
		//because http://code.google.com/p/java-matrix-benchmark/

		Matrix matrix;
		//		int[] axisForColumn; //this is used->global
		JacobianAxis[] columnIndexToJacobianColumn; //this is indexed by the one that I use. 

		//TODO go over these constructors and ensure all of them do what the caller expect
		public Jacobian( int rowDimension, JacobianAxis[] inputColumns ) {
			matrix = new Matrix( rowDimension, inputColumns.length );
			columnIndexToJacobianColumn = Arrays.copyOf( inputColumns, inputColumns.length );
		}

		public Jacobian( Jacobian[] jacobians ) {
			//augment them

			//create resultantJacobianAxisList
			//create a resultantJacobian with enough size. one by one, copy their columns to the correct places

			Set<JacobianAxis> resultantJacobianAxisSet = new HashSet<JacobianAxis>();
			int resultantRowCount = 0;

			for( Jacobian jacobian : jacobians ) {
				resultantRowCount += jacobian.getRowCount();
				for( JacobianAxis ja : jacobian.columnIndexToJacobianColumn ) {
					resultantJacobianAxisSet.add( ja );
				}
			}

			JacobianAxis[] resultantJacobianAxes = resultantJacobianAxisSet.toArray( new JacobianAxis[ resultantJacobianAxisSet.size() ] );
			columnIndexToJacobianColumn = resultantJacobianAxes;

			int resultantColumnCount = columnIndexToJacobianColumn.length;
			matrix = new Matrix( resultantRowCount, resultantColumnCount );

			for( int rowi = 0; rowi < resultantRowCount; ++rowi ) {
				for( int coli = 0; coli < resultantRowCount; ++coli ) {
					matrix.set( rowi, coli, 0.0 );
				}
			}

			int resultantRowOffset = 0;

			//now do the copying
			for( Jacobian jacobian : jacobians ) {
				//how many rows does this jacobian have?
				int rowCount = jacobian.getRowCount();

				for( int columnIndex = 0; columnIndex < resultantColumnCount; ++columnIndex ) {

					JacobianAxis jacobianAxis = columnIndexToJacobianColumn[ columnIndex ];

					boolean found = false;

					//now get the data for this axis from this jacobian
					for( int jci = 0; jci < jacobian.columnIndexToJacobianColumn.length; ++jci ) {
						if( jacobian.columnIndexToJacobianColumn[ jci ] == jacobianAxis ) {
							found = true;

							for( int rowi = 0; rowi < rowCount; ++rowi ) {
								matrix.set( resultantRowOffset + rowi, columnIndex, jacobian.matrix.get( rowi, jci ) );
							}
						}
					}

					assert found;

					//if the jacobian has this column, copy it from it
					//else leave it zero 
				}

				resultantRowOffset += rowCount;
			}

			//at this point the jacobian should be augmented
		}

		public Jacobian( Matrix mj, JacobianAxis[] jacobianAxes ) {
			matrix = mj;
			columnIndexToJacobianColumn = jacobianAxes;
		}

		public Displacement multiplyWithAngleDeltas( AngleDeltas angleDeltas ) {
			double[] d = new double[ matrix.getRowDimension() ]; //TODO perhaps not create a new one every time and reuse?

			for( int rowi = 0; rowi < matrix.getRowDimension(); ++rowi ) { //vectorize?
				d[ rowi ] = 0.0;
				for( int coli = 0; coli < matrix.getColumnDimension(); ++coli ) {
					JacobianAxis jacobianAxis = columnIndexToJacobianColumn[ coli ];
					int globalIndex = getGlobalIndexForAxis( jacobianAxis );
					d[ rowi ] += angleDeltas.getByGlobalIndex( globalIndex ) * matrix.get( rowi, coli );
				}
			}

			return new Displacement( d );
		}

		public AngleDeltas multiplyDisplacementWithInverseForMoving( Displacement displacement ) {

			//damped inverse, dimensions are like transpose
			Matrix inverseForMoving = createInverseMatrixForMoving();

			//first get it as an array
			double[] compactResult = new double[ matrix.getColumnDimension() ];

			for( int rowi = 0; rowi < inverseForMoving.getRowDimension(); ++rowi ) {
				double val = 0;
				for( int coli = 0; coli < inverseForMoving.getColumnDimension(); ++coli ) {
					val += inverseForMoving.get( rowi, coli ) * displacement.storage[ coli ];
				}
				compactResult[ rowi ] = val;
			}

			//then stretch it to make it into a globally-indexed angledeltas 
			AngleDeltas result = new AngleDeltas( indexToAxis.size() );

			//zero them out 
			for( int i = 0; i < result.storage.length; ++i ) {
				result.storage[ i ] = 0;
			}

			for( int ci = 0; ci < columnIndexToJacobianColumn.length; ++ci ) {
				result.storage[ getGlobalIndexForAxis( columnIndexToJacobianColumn[ ci ] ) ] = compactResult[ ci ];
			}

			return result;
		}

		private Matrix createInverseMatrixForMoving() {
			prepareSvdInfoIfNecessary();

			return svdInfo.createDampedInverse();
		}

		public InvertedJacobian createInverseForNullProjection() {
			prepareSvdInfoIfNecessary();

			Matrix regularInverse = svdInfo.createRegularInverse();

			return new InvertedJacobian( regularInverse, this );
		}

		SvdInfo svdInfo;

		private void prepareSvdInfoIfNecessary() {
			if( svdInfo == null ) {
				svdInfo = new SvdInfo( matrix );
			}
		}

		//TODO call this whenever you change the values of the matrix 
		private void matrixWasUpdated() {
			//clean the svd stuff
			svdInfo = null;
		}

		public int getRowCount() {
			return matrix.getRowDimension();
		}
	}

	public class InvertedJacobian {
		//TODO temporary placeholder
		//WHAT axis -> displacement

		public InvertedJacobian( Matrix inverse, Jacobian jacobian ) {
			matrix = inverse;
			straight = jacobian;
		}

		Matrix matrix;
		Jacobian straight;
	}

	public static class Displacement {
		//WHAT can be either position or orientation change
		double[] storage;

		public Displacement( double[] d ) {
			storage = d;
		}

		public Displacement createThisMinusOther( Displacement other ) {
			if( storage.length != other.storage.length ) {
				throw new RuntimeException( "Displacements have different lengths." );
			}
			double[] result = new double[ storage.length ];
			for( int i = 0; i < storage.length; ++i ) {
				result[ i ] = storage[ i ] - other.storage[ i ];
			}

			return new Displacement( result );
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

		//has one augmented jacobian at a time (not thread-safe, but who needs it anyway?)
		public Jacobian computeAugmentedJacobian() {
			Jacobian[] jacobians = new Jacobian[ constraints.size() ];
			int i = 0;
			for( Constraint constraint : constraints ) {
				Jacobian jacobian = constraint.computeJacobian();
				jacobians[ i ] = jacobian;
				++i;
			}

			currentJacobian = augmentJacobians( jacobians );
			return currentJacobian;
		}

		public Jacobian getCurrentJacobian() {
			return currentJacobian;
		}

		private Jacobian augmentJacobians( Jacobian[] jacobians ) {
			//could I devise a clever plan to get this done? maybe initially keep everything as subarrays of this?
			return new Jacobian( jacobians );
		}

		private Displacement currentAugmentedDesiredDisplacement;

		public Displacement computeAugmentedDesiredDisplacement() {
			Displacement[] displacements = new Displacement[ constraints.size() ];
			int size = 0;
			int i = 0;
			for( Constraint constraint : constraints ) {
				Displacement displacement = constraint.computeDesiredDisplacement();
				displacements[ i ] = displacement;
				++i;
				size += displacement.storage.length;
			}

			currentAugmentedDesiredDisplacement = augmentDisplacements( displacements, size );
			return currentAugmentedDesiredDisplacement;
		}

		public Displacement getCurrentAugmentedDesiredDisplacement() {
			return currentAugmentedDesiredDisplacement;
		}

		private Displacement augmentDisplacements( Displacement[] displacements, int size ) {
			//TODO a clever plan maybe? like augmented jacobians?
			//vectorize?
			//reuse storage?
			double[] d = new double[ size ];
			int largeIndex = 0;
			for( Displacement displacement : displacements ) {
				for( int i = 0; i < displacement.size(); ++i ) {
					d[ largeIndex + i ] = displacement.storage[ i ];
				}
				largeIndex += displacement.size();
			}
			return new Displacement( d );
		}

		public boolean areConstraintsMet() {
			for( Constraint constraint : constraints ) {
				if( !constraint.isMet() ) {
					return false;
				}
			}
			return true;
		}

		public void addConstraint( Constraint constraint ) {
			constraints.add( constraint );
		}
	}

	public abstract class Constraint {
		Chain chain;
		Jacobian jacobian; //TODO would be nice if I didn't have to store this separately but fill in the augmented jacobian directly
		Axis[] axes;

		public Constraint( Chain chain ) {
			this.chain = chain;
		}

		public abstract boolean isMet();

		public abstract Displacement computeDesiredDisplacement();

		public abstract Jacobian computeJacobian();

		protected void updateJacobianUsingVelocityContributions( Map<Bone, Map<Axis, Vector3>> velocityContributions ) {
			//if jacobian is not already created, create it. otherwise update it. 

			boolean isJacobianInitialized = jacobian != null;

			if( !isJacobianInitialized ) {
				//need to make these contributions be JacobianAxis -> Vector3
				List<JacobianAxis> jacobianAxisList = new ArrayList<JacobianAxis>();
				List<Vector3> contributionsList = new ArrayList<Vector3>();
				List<Axis> axisList = new ArrayList<Axis>();

				//don't need to worry about reversals at all
				//even order reversals?? - order doesn't matter here anyway...

				for( int i = 0; i < indexToAxis.size(); ++i ) {
					JacobianAxis jacobianAxis = indexToAxis.get( i );

					for( Entry<Bone, Map<Axis, Vector3>> e : velocityContributions.entrySet() ) {
						Bone bone = e.getKey();
						Map<Axis, Vector3> axisMap = e.getValue();

						if( bone.getA() == jacobianAxis.jointImp ) {
							//find the axis
							for( Entry<Axis, Vector3> ea : axisMap.entrySet() ) {
								Axis axis = ea.getKey();
								Vector3 contribution = ea.getValue();

								if( axis.getOriginalIndexInJoint() == jacobianAxis.axisInBoneIndex ) {
									//found
									assert !jacobianAxisList.contains( jacobianAxis );

									jacobianAxisList.add( jacobianAxis );
									contributionsList.add( contribution );
									axisList.add( axis );
								}
							}
						}
					}
				}

				JacobianAxis[] jacobianAxes = jacobianAxisList.toArray( new JacobianAxis[ jacobianAxisList.size() ] );

				int ji = 0;
				Matrix mj = new Matrix( 3, jacobianAxes.length );

				for( Vector3 contribution : contributionsList ) {
					mj.set( 0, ji, contribution.x );
					mj.set( 1, ji, contribution.y );
					mj.set( 2, ji, contribution.z );

					++ji;
				}

				jacobian = new Jacobian( mj, jacobianAxes );

				axes = axisList.toArray( new Axis[ jacobianAxes.length ] );
			} else {
				//use the axes in the jacobian to pull data from here

				assert axes.length == jacobian.columnIndexToJacobianColumn.length;

				for( int ji = 0; ji < jacobian.columnIndexToJacobianColumn.length; ++ji ) {
					JacobianAxis jacobianAxis = jacobian.columnIndexToJacobianColumn[ ji ];

					Axis axis = axes[ ji ];

					Vector3 contribution = velocityContributions.get( axis.getBone() ).get( axis );

					jacobian.matrix.set( 0, ji, contribution.x );
					jacobian.matrix.set( 1, ji, contribution.y );
					jacobian.matrix.set( 2, ji, contribution.z );
				}

				jacobian.matrixWasUpdated();
			}
		}
	}

	public class PositionConstraint extends Constraint {
		private Point3 eeDesiredPosition; //global

		public PositionConstraint( Chain chain, Point3 eeDesiredPosition ) {
			super( chain );
			this.eeDesiredPosition = eeDesiredPosition;
		}

		@Override
		public boolean isMet() {
			Vector3 desiredVector = computeDesiredVector();
			return desiredVector.calculateMagnitudeSquared() < MIN_DISTANCE_SQUARED_BEFORE_CONSTRAINT_IS_MET;
		}

		public void setEeDesiredPosition( Point3 position ) {
			this.eeDesiredPosition = position;
		}

		@Override
		public Displacement computeDesiredDisplacement() {
			//know the desired position. know the ee local position. should ask for ee position and compute. 
			Vector3 dispPoint = computeDesiredVector();
			return new Displacement( new double[] { dispPoint.x, dispPoint.y, dispPoint.z } );
		}

		private Vector3 computeDesiredVector() {
			return Vector3.createSubtraction( eeDesiredPosition, chain.getEndEffectorPosition() );
		}

		@Override
		public Jacobian computeJacobian() {
			//I have a chain, that should help. 
			//chain will give me all the axes
			//I also need the distance of the ee's tip to the position of the joint
			//for that I need ee local position. who has it? chain?
			//chain does have it, yes. go with it for now. I think I should have this code somewhere. I do, 

			chain.updateStateFromJoints(); //TODO may be updating things unnecessarily when chains intersect too much

			Map<Bone, Map<Axis, Vector3>> velocityContributions = chain.computeLinearVelocityContributions();

			updateJacobianUsingVelocityContributions( velocityContributions );

			return jacobian;
		}
	}

	private static double MIN_ANGLE_IN_RADIANS_BEFORE_CONSTRAINT_IS_MET = Math.PI * .0001; //TODO may need to adjust if it's doing extra cycles 
	private static double MIN_DISTANCE_BEFORE_CONSTRAINT_IS_MET = .0001; //TODO may need to adjust if it's doing extra cycles 
	private static double MIN_DISTANCE_SQUARED_BEFORE_CONSTRAINT_IS_MET = MIN_DISTANCE_BEFORE_CONSTRAINT_IS_MET * MIN_DISTANCE_BEFORE_CONSTRAINT_IS_MET;

	public class OrientationConstraint extends Constraint {

		public OrientationConstraint( Chain chain ) {
			super( chain );
		}

		private OrthogonalMatrix3x3 eeDesiredOrientation;

		@Override
		public boolean isMet() {
			AxisRotation axisRotation = computeDesiredAxisRotation();

			return axisRotation.angle.getAsRadians() < MIN_ANGLE_IN_RADIANS_BEFORE_CONSTRAINT_IS_MET;
		}

		public void setEeDesiredOrientation( OrthogonalMatrix3x3 orientation ) {
			this.eeDesiredOrientation = orientation;
		}

		@Override
		public Displacement computeDesiredDisplacement() {
			AxisRotation axisRotation = computeDesiredAxisRotation();

			Vector3 displacementVector = Vector3.createMultiplication( axisRotation.axis, axisRotation.angle.getAsRadians() );

			return new Displacement( new double[] { displacementVector.x, displacementVector.y, displacementVector.z } );
		}

		private AxisRotation computeDesiredAxisRotation() {
			OrthogonalMatrix3x3 endEffectorOrientation = chain.getEndEffectorOrientation();

			OrthogonalMatrix3x3 inv = new OrthogonalMatrix3x3( endEffectorOrientation );
			inv.invert();

			//how do you multiply? it's change X current = desired, because the change is around global axes.

			OrthogonalMatrix3x3 desiredRotation = new OrthogonalMatrix3x3( eeDesiredOrientation );
			desiredRotation.applyMultiplication( inv );

			AxisRotation axisRotation = desiredRotation.createAxisRotation();
			return axisRotation;
		}

		@Override
		public Jacobian computeJacobian() {
			chain.updateStateFromJoints(); //TODO may be updating things unnecessarily when chains intersect too much

			Map<Bone, Map<Axis, Vector3>> velocityContributions = chain.computeAngularVelocityContributions();

			updateJacobianUsingVelocityContributions( velocityContributions );

			return jacobian;
		}
	}

	//TODO keep this ordered by priority
	private List<PriorityLevel> priorityLevels = new ArrayList<PriorityLevel>();
	//but also have pointers to them separately like this for easy access
	private List<PositionConstraint> activePositionConstraints = new ArrayList<PositionConstraint>();
	private List<OrientationConstraint> activeOrientationConstraints = new ArrayList<OrientationConstraint>();

	public TightPositionalIkEnforcer( org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp ) {
		super( jointedModelImp );

		initializeListOfAxes();
	}

	//this is the mr representative of an axis in the actual model. there is one for each. it's not in jacobians. 
	class JacobianAxis {
		//TODO need to know and access the actual model and joint. figure out how you were doing it in the old code
		//like in Bone.updateStateFromJoint()

		//axis
		//axisIndex
		//012, xyz
		//vector to multiply?
		//ee pos-ori
		//chain.getEndEffectorPosition 
		//	org.lgna.story.implementation.JointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE).setReturnValueToTransformed(new edu.cmu.cs.dennisc.math.Point3(), endEffectorLocalPosition);
		//I think this one below is for calculating the desired only.
		//chain.getEndEffectorOrientation
		//	org.lgna.story.implementation.JointImp.getTransformation(org.lgna.story.implementation.AsSeenBy.SCENE).orientation
		//anchor

		//this does not know its global index but parent class does 
		private final JointImp jointImp;
		private final int axisInBoneIndex;
		private boolean isFree = true;

		public JacobianAxis( JointImp jointImp, int axisInBoneIndex ) {
			this.jointImp = jointImp;
			this.axisInBoneIndex = axisInBoneIndex;
		}

		public boolean isFree() {
			return isFree;
		}

		public void setFree( boolean isFree ) {
			this.isFree = isFree;
		}

		//limit stuff, not used for now
		public void applyCorrespondingSingleDelta( double delta ) {

			//			jointImp.applyRotationInRadians(axis, angleInRadians);
			//jointimp would work
			//Do I need the Bone.Axis to continue? NO!
			//I'm afraid I may have to deal with the reversal myself. what if there are two usages of this axis and one is reversed?
			//bone.axis should know that. but JacobianAxis is agnostic to that... 

			//I should simply 
			//I concluded yesterday that this should be very simple
			//Bone.Axis is inverted appropriately. that's what I use for contributions.
			//so, contribution is, how it moves when I turn it in the original axis direction. 
			//therefore, the numbers I get out are directly applicable to the joints. no need to worry. 

			//how about the order? I don't reverse order when I reverse the chain. so, don't worry about it. 

			//0, 1, 2 is x, y, z

			//TODO it's better to turn the joint at once rather than around three axes. rethink this call? 
			//sounds ok on paper, however joint limits make this difficult...
			//if it was three separate axes, I could enforce joint limits really easily. 
			//if it is a ball joint with three moving axes, then you can't enforce shit...
			//or can I? something to ponder

			//good thing about locking is that you won't try that direction.
			//jacobian can't move it that way. the angles I give can't move it that way either. 
			//isn't this like nullspace in a way?? not really, this is less restrictive. 
			//actually, locking /is/ like setting a nullspace entry that way. If I understood nullspace well, maybe I could. 
			//zeroing the diagonal term makes joint unused... somehow I should be able to engineer a direction. 

			//TODO apply this delta to the actual joint
			throw new RuntimeException( "Not completed method" ); // TODO Auto-generated method stub
		}

		public boolean wentOverLimit() {
			// did the angle cross the joint limit?
			throw new RuntimeException( "Not implemented method" ); // TODO Auto-generated method stub
		}

		public double setToLimitAndReturnTheDifference() {
			//TODO make the change to the actual joint
			throw new RuntimeException( "Not implemented method" ); // TODO Auto-generated method stub
		}
	}

	//are these all the joints? yes. 
	//TODO make sure that it's the assumption everywhere
	List<JacobianAxis> indexToAxis;
	Map<JointImp, List<JacobianAxis>> axesByIndexInJoint;
	Map<JacobianAxis, Integer> axisToIndex; //TODO the contents of this is wrong!

	private int getGlobalIndexForAxis( JacobianAxis jacobianAxis ) {
		//TODO ideally, the axis should know this index
		Integer globalIndex = axisToIndex.get( jacobianAxis );
		assert globalIndex != null;
		return globalIndex;
	}

	class NullspaceProjector {
		//all the joints that are in indexToAxis is the size
		double[][] matrix;
		private final int numAxes;

		public NullspaceProjector( int numAxes ) {
			this.numAxes = numAxes;
			matrix = new double[ numAxes ][ numAxes ];
		}

		public void initializeToIdentity() {
			for( int row = 0; row < numAxes; ++row ) {
				for( int col = 0; col < numAxes; ++col ) {
					matrix[ row ][ col ] = 0.0;
				}
				matrix[ row ][ row ] = 1.0;
			}

			//TODO unlock ball joints as well
		}

		public Jacobian createProjected( Jacobian input ) {
			//create a new jacobian with the same columns

			Jacobian result = new Jacobian( input.matrix.getRowDimension(), input.columnIndexToJacobianColumn );

			//result will be nullProjector times input

			int[] localToGlobal = new int[ result.matrix.getColumnDimension() ];

			for( int ci = 0; ci < result.matrix.getColumnDimension(); ++ci ) {
				localToGlobal[ ci ] = getGlobalIndexForAxis( result.columnIndexToJacobianColumn[ ci ] );
			}

			for( int outRowi = 0; outRowi < result.matrix.getRowDimension(); ++outRowi ) {
				for( int outColi = 0; outColi < result.matrix.getColumnDimension(); ++outColi ) {

					//this is both column and row index for nsp
					int outColGlobali = localToGlobal[ outColi ];

					double val = 0;
					for( int ci = 0; ci < result.matrix.getColumnDimension(); ++ci ) {
						val += input.matrix.get( outRowi, ci ) * matrix[ localToGlobal[ ci ] ][ outColGlobali ];
					}
					result.matrix.set( outRowi, outColi, val );

				}
			}

			result.matrixWasUpdated();

			return result;
		}

		public void subtractInverseTimesJacobian( InvertedJacobian inverse, Jacobian jacobian ) {
			//multiply them and subtract from the corresponding index

			assert inverse.straight == jacobian;

			int[] localToGlobal = new int[ jacobian.matrix.getColumnDimension() ];

			for( int ci = 0; ci < jacobian.matrix.getColumnDimension(); ++ci ) {
				localToGlobal[ ci ] = getGlobalIndexForAxis( jacobian.columnIndexToJacobianColumn[ ci ] );
			}

			int localDimension = jacobian.matrix.getColumnDimension();

			for( int rowi = 0; rowi < localDimension; ++rowi ) {
				for( int coli = 0; coli < localDimension; ++coli ) {

					double val = 0;
					for( int ri = 0; ri < jacobian.matrix.getRowDimension(); ++ri ) {
						val += inverse.matrix.get( rowi, ri ) * jacobian.matrix.get( ri, coli );
					}

					matrix[ rowi ][ coli ] -= val;
				}
			}
		}

		public void setIndexToLocked( int index ) {
			// should effectively zero the row and col of this index, and one the intersection
			// if it's going to be more efficient, maybe do it lazily.
			// it probably wouldn't be if I figure out a way of doing it with vectors and parallel programming

			for( int i = 0; i < matrix.length; ++i ) {
				if( i == index ) {
					matrix[ i ][ i ] = 1;
				} else {
					matrix[ i ][ i ] = 0;
				}
			}

			//TODO do I need to remember in a boolean that this index is locked?
		}

	}

	NullspaceProjector nullspaceProjector;

	private void initializeListOfAxes() {
		//in a list, I'll hold the list of axes (or angles).
		// what type do they have? Axis? no, that's an IK thing. 
		// joint, number pair?

		indexToAxis = new ArrayList<TightPositionalIkEnforcer.JacobianAxis>();
		axesByIndexInJoint = new HashMap<JointImp, List<JacobianAxis>>();
		axisToIndex = new HashMap<JacobianAxis, Integer>();

		Iterable<JointImp> jointImps = jointedModelImp.getJoints();

		int globalIndex = 0;

		for( JointImp jointImp : jointImps ) {
			List<JacobianAxis> axesInJoint = new ArrayList<JacobianAxis>();
			axesByIndexInJoint.put( jointImp, axesInJoint );

			for( int indexInJoint = 0; indexInJoint < 3; ++indexInJoint ) {
				JacobianAxis jacobianAxis = new JacobianAxis( jointImp, indexInJoint );
				indexToAxis.add( jacobianAxis );
				axesInJoint.add( jacobianAxis );
				axisToIndex.put( jacobianAxis, globalIndex );
				++globalIndex;
			}
		}

		nullspaceProjector = new NullspaceProjector( indexToAxis.size() );
	}

	public void enforceConstraints() {
		//convergence loop (constraints or max iter)
		//clamping loop (joint limits, clamping)
		//priority loop (apply constraints in current null space)

		convergenceLoop();
	}

	public PositionConstraint createPositionConstraint( int level, JointId anchorId, JointId endId ) {
		Chain chain = org.lgna.ik.core.solver.Chain.createInstance( jointedModelImp, anchorId, endId );

		Point3 endPosition = jointedModelImp.getJointImplementation( endId ).getTransformation( AsSeenBy.SCENE ).translation;

		PositionConstraint positionConstraint = new PositionConstraint( chain, Point3.createAddition( endPosition, new Point3( 0, 1, 0 ) ) );

		// call setEeDesiredPosition

		activePositionConstraints.add( positionConstraint );

		while( ( priorityLevels.size() - 1 ) < level ) {
			priorityLevels.add( new PriorityLevel() );
		}
		priorityLevels.get( level ).addConstraint( positionConstraint );

		return positionConstraint;
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

		angleDeltas = new AngleDeltas( indexToAxis.size() );

		while( ( numIterations < maxNumIterations ) && !areConstraintsMet() ) {
			// compute current jacobians for all priority levels.  
			// I had some reservations, maybe calculating jacobians should be inside clamping loop? it seems it's too much of an optimization because it will try one more time for convergence.

			for( PriorityLevel priorityLevel : priorityLevels ) {
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

		for( PriorityLevel priorityLevel : priorityLevels ) {
			if( !priorityLevel.areConstraintsMet() ) {
				return false;
			}
		}

		return true;
	}

	private void clampingLoop() {
		// ensure that priority levels are appropriately initialized (ui changes, whatever)
		// 
		boolean isComputedWithClamping = true;

		while( isComputedWithClamping ) {
			isComputedWithClamping = false; //make it true when you clamp

			priorityLoop();

			makeCloserToNaturalPose();

			isComputedWithClamping = applyAngleChangesAndClampingIfNecessary_NoClampingForNow();
		}
	}

	//this is the cumulative angledeltas. just like nullspaceprojector.
	class AngleDeltas {
		//WHAT globalIndex->value. 

		double[] storage;

		public AngleDeltas( int numAllPossibleAxes ) {
			storage = new double[ numAllPossibleAxes ];
		}

		public void add( AngleDeltas other ) {
			assert storage.length == other.storage.length;
			for( int i = 0; i < storage.length; ++i ) {
				storage[ i ] += other.storage[ i ];
			}
		}

		//TODO placeholder
		//this is applied to actual angles at every clamping test and convergence test
		// I mean, how else would you test for convergence? gets pretty hard. 

		public double getByGlobalIndex( int coli ) {
			return storage[ coli ];
		}

		public double getForAxis( JacobianAxis axis ) {
			int globalIndex = axisToIndex.get( axis );
			double delta = angleDeltas.getByGlobalIndex( globalIndex );
			return delta;
		}

		public void correctDeltaForAxis( int index, double correction ) {
			//subtract correction from the angle delta that corresponds to the axis (index)
			storage[ index ] -= correction; //angle continuity? deltas should never get close to 2pi, so, no.
		}
	}

	AngleDeltas angleDeltas;

	private void priorityLoop() {
		//loop through the priority levels and compute the angle delta vector incrementally with each
		int lastPriorityLevel = 0;
		boolean first = true;
		for( PriorityLevel priorityLevel : priorityLevels ) {
			// verify the order (temporarily)
			if( !first ) {
				if( lastPriorityLevel > priorityLevel.levelIndex ) {
					throw new RuntimeException( "priorities are not ordered " + lastPriorityLevel + " " + priorityLevel.levelIndex );
				}
			} else {
				first = false;
			}
			lastPriorityLevel = priorityLevel.levelIndex;
			// /verify the order (temporarily)

			priorityLoopIteration( priorityLevel );
		}
	}

	private void priorityLoopIteration( PriorityLevel priorityLevel ) {
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
		Displacement remainingDesiredDisplacement = createRemainingDesiredDisplacement( desiredDisplacement, currentJacobian, angleDeltas );

		// project jacobian on the nullspace
		Jacobian projectedJacobian = nullspaceProjector.createProjected( currentJacobian );

		// add to angle delta, compute nullspace projectors, etc
		addRequiredMotionToCurrentAngleDeltas( angleDeltas, projectedJacobian, remainingDesiredDisplacement );

		updateNullspaceProjector( nullspaceProjector, projectedJacobian );
	}

	private Displacement createRemainingDesiredDisplacement(
			Displacement desiredDisplacement, Jacobian currentJacobian,
			AngleDeltas currentAngleDeltas ) {
		// multiply jacobian and currentAngleDeltas 
		Displacement alreadyMoved = currentJacobian.multiplyWithAngleDeltas( currentAngleDeltas );
		// create the difference between desiredDisplacement and the multiplication above 
		return desiredDisplacement.createThisMinusOther( alreadyMoved );
	}

	private void addRequiredMotionToCurrentAngleDeltas(
			AngleDeltas currentAngleDeltas, Jacobian projectedJacobian,
			Displacement remainingDesiredDisplacement ) {
		AngleDeltas angleDeltasToMove = projectedJacobian.multiplyDisplacementWithInverseForMoving( remainingDesiredDisplacement );
		//update currentAngleDeltas
		currentAngleDeltas.add( angleDeltasToMove );
	}

	private void updateNullspaceProjector( NullspaceProjector currentNullspaceProjector, Jacobian jacobian ) {
		InvertedJacobian inverse = jacobian.createInverseForNullProjection();

		currentNullspaceProjector.subtractInverseTimesJacobian( inverse, jacobian );
	}

	private void makeCloserToNaturalPose() {
		// TODO use the current nullspace projector to find an additional angle delta 
		// that would move us closer to a natural pose
		// TODO for now I'm ignoring this. TBD
		//		System.out.println( "implement this at some point" );
	}

	//this is the version that never clamps
	private boolean applyAngleChangesAndClampingIfNecessary_NoClampingForNow() {
		// everything in the box that starts with clamping detected=no
		boolean clamped = false;

		//basically, just rotate the joints and that's it. 

		//I have axes for each joint
		for( Entry<JointImp, List<JacobianAxis>> e : axesByIndexInJoint.entrySet() ) {
			JointImp jointImp = e.getKey();
			List<JacobianAxis> axesForJoint = e.getValue();

			if( axesForJoint.size() == 3 ) {
				int axisIndexInJoint = 0;
				Vector3 combinedRotation = null;

				for( JacobianAxis axis : axesForJoint ) {
					assert axis.isFree();

					//need to get this axis so that I can merge
					//they only need to be local axes
					double delta = angleDeltas.getForAxis( axis );

					Vector3 rotationAroundThisAxis;

					switch( axisIndexInJoint ) {
					case 0:
						rotationAroundThisAxis = Vector3.createPositiveXAxis();
						break;
					case 1:
						rotationAroundThisAxis = Vector3.createPositiveYAxis();
						break;
					case 2:
						rotationAroundThisAxis = Vector3.createPositiveZAxis();
						break;
					default:
						assert false;
						rotationAroundThisAxis = Vector3.createPositiveXAxis(); //against compile-time error
					}

					rotationAroundThisAxis.multiply( delta );

					//this is local rotation

					if( combinedRotation == null ) {
						combinedRotation = rotationAroundThisAxis;
					} else {
						combinedRotation.add( rotationAroundThisAxis );
					}

					++axisIndexInJoint;
				}

				//apply 
				double angleInRadians = combinedRotation.calculateMagnitude();
				combinedRotation.divide( angleInRadians );
				Vector3 axis = combinedRotation;

				//local rotation
				if( !axis.isNaN() ) {
					jointImp.applyRotationInRadians( axis, angleInRadians );
				}
			} else {
				assert false : "Joint with other than three angles";
			}
		}

		//return true if clamped
		return clamped;
	}

	private boolean applyAngleChangesAndClampingIfNecessary_originalEffort() {
		// everything in the box that starts with clamping detected=no
		boolean clamped = false;

		// need to change this so that I apply angle changes joint by joint.
		// group axes by joint, apply them like that.
		// it's axesByIndexInJoint

		for( Entry<JointImp, List<JacobianAxis>> e : axesByIndexInJoint.entrySet() ) {
			JointImp jointImp = e.getKey();
			List<JacobianAxis> axesForJoint = e.getValue();

			//I want to apply the changes for these together.
			//if one of them is not free, don't include that (impossible when 3 and floating)

			//if there are three axes, 
			//assert that all are free
			//merge them, see if applying them would cause going over the limit. if so,
			//move till right when the limit is reached
			//(make sure nullspaceProjector was initialized to identity before going into this loop)  
			//change nullspaceProjector so that things only move orthogonal to the limit direction
			//if not, move all the way

			//if there are less than three axes, some may be not free
			//move the next free one
			//if moving this would move over the limit, 
			//move so far and make this locked (not free)
			//else, move all the way

			if( axesForJoint.size() == 3 ) {
				int axisIndexInJoint = 0;
				Vector3 combinedRotation = null;

				for( JacobianAxis axis : axesForJoint ) {
					assert axis.isFree();

					//need to get this axis so that I can merge
					//they only need to be local axes
					int globalIndex = axisToIndex.get( axis );
					double delta = angleDeltas.getByGlobalIndex( globalIndex );

					Vector3 rotationAroundThisAxis;

					switch( axisIndexInJoint ) {
					case 0:
						rotationAroundThisAxis = Vector3.createPositiveXAxis();
						break;
					case 1:
						rotationAroundThisAxis = Vector3.createPositiveYAxis();
						break;
					case 2:
						rotationAroundThisAxis = Vector3.createPositiveZAxis();
						break;
					default:
						assert false;
						rotationAroundThisAxis = Vector3.createPositiveXAxis(); //against compile-time error
					}

					rotationAroundThisAxis.multiply( delta );

					//this is local rotation

					if( combinedRotation == null ) {
						combinedRotation = rotationAroundThisAxis;
					} else {
						combinedRotation.add( rotationAroundThisAxis );
					}

					++axisIndexInJoint;
				}

				//apply 
				double angleInRadians = combinedRotation.calculateMagnitude();
				combinedRotation.divide( angleInRadians );
				Vector3 axis = combinedRotation;

				//local rotation
				OrthogonalMatrix3x3 initialOrientation = jointImp.getLocalOrientation();
				jointImp.applyRotationInRadians( axis, angleInRadians );

				//
				//while asking "if", I should also learn about the violation, so that I can use it for locking in nullspace matrix
				//if there is only one violated, next rotations have to be perp to that
				//if there are two violated, next rotations can only be perp to both (on one line)
				//if more, totally locked

				IndependentBallJointLimit violatedBallJointLimits = getViolatedBallJointLimits( jointImp );

				if( violatedBallJointLimits != null ) {
					turnJointBackToLimits( jointImp, initialOrientation, violatedBallJointLimits );
				}

				boolean isNewLocksFound = lockViolatedBallJointLimits( jointImp, violatedBallJointLimits );

				clamped = isNewLocksFound;

				//clamping detected means: are there any NEW joints that have been clamped?
				//so I need to remember whether this was clamped before. hard to do with ball joints?
				//why not convert them all to "around first, between, around second"?

				//keep the three axis for each ball joint and whether they are locked atm
				//clear those locks after every step (I guess when nullspaceprojector is cleared)
				//this way, I can incrementally lock them here

				//violated?
				//undo some
				//set constraint

			} else {
				for( JacobianAxis axis : axesForJoint ) {
					int globalIndex = axisToIndex.get( axis );
					axis.applyCorrespondingSingleDelta( angleDeltas.getByGlobalIndex( globalIndex ) );

					if( axis.wentOverLimit() ) {
						axis.setFree( false );
						double correction = axis.setToLimitAndReturnTheDifference();
						angleDeltas.correctDeltaForAxis( globalIndex, correction );
						nullspaceProjector.setIndexToLocked( globalIndex );
						clamped = true;
					}
				}
			}
		}

		//return true if clamped
		return clamped;
	}

	private boolean lockViolatedBallJointLimits( JointImp jointImp,
			IndependentBallJointLimit violatedBallJointLimits ) {
		//this happens in nullspaceprojector
		//you need to add the violatedBallJointLimits to existing ones
		//you need to re-lock existing ones in the projector as their axes probably have moved!
		//here or elsewhere, but somewhere! 
		//return true only if new axes got locked now 

		//this is how you lock in the projector:
		//if there is only one violated, next rotations have to be perp to that
		//if there are two violated, next rotations can only be perp to both (on one line)
		//if more, totally locked

		throw new RuntimeException( "Not implemented method" ); // TODO Auto-generated method stub
	}

	private void turnJointBackToLimits( JointImp jointImp,
			OrthogonalMatrix3x3 initialOrientation,
			IndependentBallJointLimit violatedBallJointLimits ) {
		throw new RuntimeException( "Not implemented method" ); // TODO Auto-generated method stub
	}

	private IndependentBallJointLimit getViolatedBallJointLimits( JointImp jointImp ) {
		//I've rotated the joint with existing limits but not new ones
		//I'd like to know whether I've ran over any extra limits

		//ignore breaches of existing limits, you'll silently undo them
		//identify breaches of nonexisting limits

		//need the two bones' frames...
		//what's a joint anyways?
		//it's vs identity. when I'm using local orientation, identity is the frame of upstream, isn't it?
		//no, not really. is it the initial state of the frame instead? most likely...

		throw new RuntimeException( "Not implemented method" ); // TODO Auto-generated method stub
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
