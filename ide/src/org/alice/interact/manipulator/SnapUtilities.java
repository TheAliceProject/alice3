package org.alice.interact.manipulator;

import org.alice.interact.GlobalDragAdapter;
import org.alice.interact.SnapLine;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Component;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.OrthographicCamera;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.util.TransformationUtilities;

public class SnapUtilities 
{
	public static final double SNAP_TO_GROUND_DISTANCE = .05d;
	public static final double SNAP_TO_GRID_DISTANCE = .1d;
	public static final double DEFAULT_GRID_SPACING = .5d;
	public static final double MIN_SNAP_RETURN_VALUE = .1d;
	
	private static final SnapLine X_AXIS_LINE = new SnapLine(Vector3.accessPositiveXAxis());
	private static final SnapLine Y_AXIS_LINE = new SnapLine(Vector3.accessPositiveYAxis());
	private static final SnapLine Z_AXIS_LINE = new SnapLine(Vector3.accessPositiveZAxis());
	private static final SnapLine ARBITRARY_AXIS_LINE = new SnapLine(Vector3.accessPositiveXAxis());
	
	public static void showXAxis(Point3 position, Composite parent)
	{
		X_AXIS_LINE.setParent(parent);
		X_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
	}
	
	public static void hideXAxis()
	{
		X_AXIS_LINE.setParent(null);
	}
	
	public static void showYAxis(Point3 position, Composite parent)
	{
		Y_AXIS_LINE.setParent(parent);
		Y_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
	}
	
	public static void hideYAxis()
	{
		Y_AXIS_LINE.setParent(null);
	}
	
	public static void showZAxis(Point3 position, Composite parent)
	{
		Z_AXIS_LINE.setParent(parent);
		Z_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
	}
	
	public static void hideZAxis()
	{
		Z_AXIS_LINE.setParent(null);
	}
	
	public static void showArbitraryAxis(Point3 position, Vector3 direction, Composite parent)
	{
		ARBITRARY_AXIS_LINE.setLine(direction);
		ARBITRARY_AXIS_LINE.setParent(parent);
		ARBITRARY_AXIS_LINE.setTranslationOnly(position, AsSeenBy.SCENE);
	}
	
	public static void hideArbitraryAxis()
	{
		ARBITRARY_AXIS_LINE.setParent(null);
	}
	
	public static void hideSnapLines()
	{
		hideZAxis();
		hideYAxis();
		hideXAxis();
		hideArbitraryAxis();
	}
	
	public static Visual getSGVisualForTransformable( Transformable t )
	{
		if (t == null)
		{
			return null;
		}
		for (int i=0; i<t.getComponentCount(); i++)
		{
			Component c = t.getComponentAt( i );
			if (c instanceof Visual)
			{
				return (Visual)c;
			}
		}
		return null;
	}
	
	public static edu.cmu.cs.dennisc.math.Matrix3x3 getTransformableScale( Transformable t )
	{
		edu.cmu.cs.dennisc.math.Matrix3x3 returnScale;
		Visual objectVisual = getSGVisualForTransformable( t );
		if (objectVisual != null)
		{
			returnScale = new edu.cmu.cs.dennisc.math.Matrix3x3();
			returnScale.setValue( objectVisual.scale.getValue() );
		}
		else
		{
			returnScale = edu.cmu.cs.dennisc.math.ScaleUtilities.newScaleMatrix3d( 1.0d, 1.0d, 1.0d );
		}
		return returnScale;
		
	}
	
	public static AxisAlignedBox getBoundingBox(Transformable t)
	{
		AxisAlignedBox boundingBox = null;
		if (t != null)
		{
			Object bbox = t.getBonusDataFor( GlobalDragAdapter.BOUNDING_BOX_KEY );
			if (bbox instanceof edu.cmu.cs.dennisc.math.AxisAlignedBox)
			{
				boundingBox = new AxisAlignedBox((edu.cmu.cs.dennisc.math.AxisAlignedBox)bbox);
				if (boundingBox.isNaN())
				{
					boundingBox = null;
				}
			}
		}
		if (boundingBox == null)
		{
			boundingBox = new AxisAlignedBox(new Point3(-1, 0, -1), new Point3(1, 1, 1));
		}
		if (boundingBox != null)
		{
			boundingBox.scale( getTransformableScale( t ) );
		}
		Point3 boxMin = boundingBox.getMinimum();
		Point3 boxMax = boundingBox.getMaximum();
		TransformationUtilities.transformToAbsolute(boxMin, boxMin, t);
		TransformationUtilities.transformToAbsolute(boxMax, boxMax, t);
		boundingBox.setMaximum(boxMax);
		boundingBox.setMinimum(boxMin);
		return boundingBox;
	}
	
	public static Point3 snapObjectToGround(Transformable toSnap, Point3 newPosition)
	{

		Point3 returnSnapPosition = new Point3(newPosition);
		
		boolean didSnap = false;
		Vector3 movementDelta = Vector3.createSubtraction( newPosition, toSnap.getAbsoluteTransformation().translation);
		if (movementDelta.y != 0)
		{
			AxisAlignedBox bbox = getBoundingBox(toSnap);
			bbox.translate(movementDelta); //move the bounding box to where the newPosition would place it
			double boxBottom = bbox.getYMinimum();
			if (Math.abs(boxBottom) <= SNAP_TO_GROUND_DISTANCE )
			{
				didSnap = true;
				returnSnapPosition.y -= boxBottom; //translate the object point the amount the bottom of the bounding box is awau from y
			}
//			if (Math.abs(rv.y) <= SNAP_TO_GROUND_DISTANCE)
//			{
//				rv.y = 0.0d;
//			}
		}
		return returnSnapPosition;
		
	}
	
	public static boolean isEdgeOn(AbstractCamera camera)
	{
		return isEdgeOn(camera, 0);
	}
	
	public static boolean isEdgeOn(AbstractCamera camera, double epsilon)
	{
		double dotProd = Vector3.calculateDotProduct(camera.getAbsoluteTransformation().orientation.up, Vector3.accessPositiveYAxis());
		return (1 - Math.abs(dotProd)) <= epsilon;
	}
	
	public static void showHorizontalSnap(AbstractCamera camera, Point3 currentPosition, Point3 snapPosition)
	{
		Vector3 snapVector = Vector3.createSubtraction(currentPosition, snapPosition);
		Point3 linePosition = new Point3(snapPosition);
		//If we're looking edge on, use a vertical line to draw the snap
		if (isEdgeOn(camera))
		{
			if (snapVector.x != 0 || snapVector.z != 0)
			{
				SnapUtilities.showYAxis(linePosition, camera.getRoot());
			}
			else
			{
				SnapUtilities.hideYAxis();
			}
		}
		else
		{
			linePosition.y = .05d;
			if (snapVector.x != 0)
			{
				SnapUtilities.showZAxis(linePosition, camera.getRoot());
			}
			else
			{
				SnapUtilities.hideZAxis();
			}
			if (snapVector.z != 0)
			{
				SnapUtilities.showXAxis(linePosition, camera.getRoot());
			}
			else
			{
				SnapUtilities.hideXAxis();
			}
		}
	}
	
	public static void showVerticalSnap(AbstractCamera camera, Point3 currentPosition, Point3 snapPosition)
	{
		if (isEdgeOn(camera))
		{
			Vector3 snapVector = Vector3.createSubtraction(currentPosition, snapPosition);
			if (snapVector.y != 0)
			{
				SnapUtilities.showArbitraryAxis(snapPosition, camera.getAbsoluteTransformation().orientation.right, camera.getRoot());
			}
			else
			{
				SnapUtilities.hideArbitraryAxis();
			}
		}
	}
	
	public static void showSnapLines(AbstractCamera camera, Point3 currentPosition, Point3 snapPosition)
	{
		showHorizontalSnap(camera, currentPosition, snapPosition);
		showVerticalSnap(camera, currentPosition, snapPosition);
	}
	
	public static Point3 snapObjectToAbsoluteGrid(Transformable toSnap, Point3 newPosition)
	{
		return snapObjectToAbsoluteGrid(toSnap, newPosition, DEFAULT_GRID_SPACING);
	}
	
	public static Point3 snapObjectToAbsoluteGrid(Transformable toSnap, Point3 newPosition, double gridSpacing)
	{
		Point3 returnSnapPosition = new Point3(newPosition);
		boolean didSnap = false;
		Vector3 movementDelta = Vector3.createSubtraction( newPosition, toSnap.getAbsoluteTransformation().translation);
		if (movementDelta.x != 0)
		{
			double currentPos = newPosition.x;
			
			int lowerMultiplier = (int)(currentPos / gridSpacing);
			int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
			double lowerSnap = gridSpacing * lowerMultiplier;
			double upperSnap = gridSpacing * upperMultiplier;
			if (Math.abs(lowerSnap - currentPos) <= SNAP_TO_GRID_DISTANCE)
			{
				didSnap = true;
				currentPos = gridSpacing * lowerMultiplier;
			}
			else if (Math.abs(upperSnap - currentPos) <= SNAP_TO_GRID_DISTANCE)
			{
				didSnap = true;
				currentPos = gridSpacing * upperMultiplier;
			}
			
			returnSnapPosition.x = currentPos;
		}
		if (movementDelta.y != 0)
		{
			double currentPos = newPosition.y;
			
			int lowerMultiplier = (int)(currentPos / gridSpacing);
			int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
			double lowerSnap = gridSpacing * lowerMultiplier;
			double upperSnap = gridSpacing * upperMultiplier;
			if (Math.abs(lowerSnap - currentPos) <= SNAP_TO_GRID_DISTANCE)
			{
				didSnap = true;
				currentPos = gridSpacing * lowerMultiplier;
			}
			else if (Math.abs(upperSnap - currentPos) <= SNAP_TO_GRID_DISTANCE)
			{
				didSnap = true;
				currentPos = gridSpacing * upperMultiplier;
			}
			
			returnSnapPosition.y = currentPos;
		}
		if (movementDelta.z != 0)
		{
			double currentPos = newPosition.z;
			
			int lowerMultiplier = (int)(currentPos / gridSpacing);
			int upperMultiplier = ( currentPos < 0 ) ? lowerMultiplier - 1 : lowerMultiplier + 1;
			double lowerSnap = gridSpacing * lowerMultiplier;
			double upperSnap = gridSpacing * upperMultiplier;
			if (Math.abs(lowerSnap - currentPos) <= SNAP_TO_GRID_DISTANCE)
			{
				didSnap = true;
				currentPos = gridSpacing * lowerMultiplier;
			}
			else if (Math.abs(upperSnap - currentPos) <= SNAP_TO_GRID_DISTANCE)
			{
				didSnap = true;
				currentPos = gridSpacing * upperMultiplier;
			}
			
			returnSnapPosition.z = currentPos;
		}
		return returnSnapPosition;
		
	}

}
