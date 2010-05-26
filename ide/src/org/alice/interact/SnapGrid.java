package org.alice.interact;

import java.util.LinkedList;
import java.util.List;

import org.alice.interact.manipulator.SnapUtilities;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.LineArray;
import edu.cmu.cs.dennisc.scenegraph.ShadingStyle;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;

public class SnapGrid extends Transformable implements PropertyListener{
	private static final double LINE_HALF_DISTANCE = 20.0d;
	private static final double MIDPOINT = LINE_HALF_DISTANCE / 2;
	private static final int SEGMENTS_PER_LINE = 3;
	private static final int VERTICES_PER_LINE = SEGMENTS_PER_LINE*2;
	
	private SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	private LineArray sgXLines;
	private LineArray sgZLines;
	private Visual sgGridVisual;
	private double gridSpacing = .5d;
	
	private float opacity = .5f;
	
	private List<AbstractCamera> camerasToTrack;
	private AbstractCamera currentCamera;
	
	public SnapGrid()
	{
		super();
		this.setName("Snap Grid");
		this.sgFrontFacingAppearance.setShadingStyle(ShadingStyle.NONE);
		this.sgGridVisual = new Visual();
		this.sgGridVisual.frontFacingAppearance.setValue(this.sgFrontFacingAppearance);
		
		this.sgXLines = new LineArray();
		this.sgZLines = new LineArray();
		this.sgGridVisual.geometries.setValue( new Geometry[] { this.sgXLines, this.sgZLines } );
		this.sgGridVisual.setParent(this);
		
		this.camerasToTrack = new LinkedList<AbstractCamera>();
		
		this.setOpacity(.3f);
		this.setGridLines(.5);
		this.setColor(Color4f.PINK);
		
		
	}
	
	public void stopTrackingCameras()
	{
		for (AbstractCamera camera : this.camerasToTrack)
		{
			if (camera.getParent() instanceof Transformable)
			{
				Transformable cameraParent = (Transformable)camera.getParent();
				cameraParent.localTransformation.removePropertyListener(this);
			}
		}
		this.camerasToTrack.clear();
	}
	
	public void setCurrentCamera( AbstractCamera camera )
	{
		this.currentCamera = camera;
		setSnapGridBasedOnCameraPosition();
		
	}
	
	public void addCamera(AbstractCamera camera)
	{
		if (!this.camerasToTrack.contains(camera))
		{
			if (camera.getParent() instanceof Transformable)
			{
				this.camerasToTrack.add(camera);
				Transformable cameraParent = (Transformable)camera.getParent();
				cameraParent.localTransformation.addPropertyListener(this);
			}
		}
	}
	
	private int getLineCount(double spacing)
	{
		return (int)((LINE_HALF_DISTANCE*2) / spacing);
	}
	
	private Vertex[] buildLines(double spacing, int lineCount, boolean isXAxis)
	{
		Vertex[] lines = new Vertex[(lineCount+1)*(VERTICES_PER_LINE)];
		
		int trueLineCount = lines.length / (VERTICES_PER_LINE); //There are n vertices for each line
		double lineStart = spacing*(trueLineCount / 2);
		double y = SnapUtilities.SNAP_LINE_VISUAL_HEIGHT;
		float r = 0;
		float g = 0;
		float b = 0;
		
		for (int index = 0; index<lines.length; index += VERTICES_PER_LINE)
		{
			if (Math.abs(lineStart) <= .000001)
			{
				lineStart = 0;
			}
			double currentPos = lineStart;
			float alpha = this.opacity;
			if (Math.abs(currentPos) > MIDPOINT)
			{
				alpha = (float)(1.0f - (Math.abs(currentPos) - MIDPOINT) / MIDPOINT) * this.opacity;
			}
			if (isXAxis)
			{
				lines[index] = Vertex.createXYZRGBA(currentPos, y, LINE_HALF_DISTANCE, r, g, b, 0);
				lines[index+1] = Vertex.createXYZRGBA(currentPos, y, MIDPOINT, r, g, b, alpha);
				lines[index+2] = Vertex.createXYZRGBA(currentPos, y, MIDPOINT, r, g, b, alpha);
				lines[index+3] = Vertex.createXYZRGBA(currentPos, y, -MIDPOINT, r, g, b, alpha);
				lines[index+4] = Vertex.createXYZRGBA(currentPos, y, -MIDPOINT, r, g, b, alpha);
				lines[index+5] = Vertex.createXYZRGBA(currentPos, y, -LINE_HALF_DISTANCE, r, g, b, 0);
			}
			else //It's the Z axis
			{
				lines[index] = Vertex.createXYZRGBA(LINE_HALF_DISTANCE, y, currentPos, r, g, b, 0);
				lines[index+1] = Vertex.createXYZRGBA(MIDPOINT, y, currentPos, r, g, b, alpha);
				lines[index+2] = Vertex.createXYZRGBA(MIDPOINT, y, currentPos, r, g, b, alpha);
				lines[index+3] = Vertex.createXYZRGBA(-MIDPOINT, y, currentPos, r, g, b, alpha);
				lines[index+4] = Vertex.createXYZRGBA(-MIDPOINT, y, currentPos, r, g, b, alpha);
				lines[index+5] = Vertex.createXYZRGBA(-LINE_HALF_DISTANCE, y, currentPos, r, g, b, 0);
			}
			lineStart -= spacing;
		}
		
		for (int i=0; i<lines.length; i++)
		{
			if (lines[i] == null)
			{
				System.out.println("BOOM!");
			}
		}
		
		return lines;
	}
	
	private void setGridLines(double spacing)
	{
		this.gridSpacing = spacing;
		int lineCount = getLineCount(this.gridSpacing);
		
		Vertex[] xLines = buildLines(spacing, lineCount, true);
		Vertex[] zLines = buildLines(spacing, lineCount, false);

		this.sgXLines.vertices.setValue(xLines);
		this.sgZLines.vertices.setValue(zLines);
	}
	
	public void setColor(Color4f color)
	{
		this.sgFrontFacingAppearance.setDiffuseColor(color);
	}
	
	public void setOpacity(float opacity)
	{
		this.opacity = opacity;
//		this.sgFrontFacingAppearance.setOpacity(opacity);
	}
	
	public void setShowing( boolean isShowing )
	{
		this.sgGridVisual.isShowing.setValue(isShowing);
	}
	
	public boolean getShowing()
	{
		return this.sgGridVisual.isShowing.getValue();
	}
	
	private void setSnapGridBasedOnCameraPosition()
	{
		AffineMatrix4x4 currentCameraPosition = currentCamera.getAbsoluteTransformation();
		this.setTranslationOnly(getClosestSnapLocation(currentCameraPosition.translation), AsSeenBy.SCENE);
	}
	
	public void setSpacing(double spacing)
	{
		if (spacing != this.gridSpacing)
		{
			setGridLines(spacing);
			setSnapGridBasedOnCameraPosition();
		}
	}
	
	private Point3 getClosestSnapLocation( Point3 point )
	{
		int xMultiplier = (int)(point.x / this.gridSpacing);
		int zMultiplier = (int)(point.z / this.gridSpacing);
		double x = xMultiplier * this.gridSpacing;
		double z = zMultiplier * this.gridSpacing;
		return new Point3(x, 0, z);
	}

	public void propertyChanged(PropertyEvent e) {
		if (e.getValue() instanceof AffineMatrix4x4)
		{
			setSnapGridBasedOnCameraPosition();
//			AffineMatrix4x4 newCameraTransform = (AffineMatrix4x4)e.getValue();
//			this.setTranslationOnly(getClosestSnapLocation(newCameraTransform.translation), AsSeenBy.SCENE);
		}
		
	}

	public void propertyChanging(PropertyEvent e) {
		// TODO Auto-generated method stub
		
	}

	

}
