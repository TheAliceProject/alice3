package org.alice.interact;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.LineArray;
import edu.cmu.cs.dennisc.scenegraph.ShadingStyle;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Vertex;
import edu.cmu.cs.dennisc.scenegraph.Visual;

public class InfiniteLineVisualization extends Visual {
	
	private static final double LINE_DISTANCE = 1000.0d;
	private static SingleAppearance s_sgFrontFacingAppearance = new SingleAppearance();
	static {
		s_sgFrontFacingAppearance.setShadingStyle( ShadingStyle.NONE );
	}
	
	public InfiniteLineVisualization( Vector3 line) {
		
	    Vertex[] vertices = new Vertex[ 2 ];
	    
	    Point3 lineEnd1 = Point3.createMultiplication(line, LINE_DISTANCE);
	    Point3 lineEnd2 = Point3.createMultiplication(line, -LINE_DISTANCE);
	    vertices[ 0 ] = Vertex.createXYZRGB( lineEnd1.x, lineEnd1.y, lineEnd1.z, 0,1,0 );
	    vertices[ 1 ] = Vertex.createXYZRGB( lineEnd2.x, lineEnd2.y, lineEnd2.z, 0,1,0 );
	    
	    LineArray sgLineArray = new LineArray();
	    sgLineArray.vertices.setValue( vertices );
	    geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { sgLineArray } );
		frontFacingAppearance.setValue( s_sgFrontFacingAppearance );
	}
	
	public void setLine(Vector3 line)
	{
		LineArray lines = (LineArray)(geometries.getValue()[0]);
		Vertex[] vertices = lines.vertices.getValue();
		Point3 lineEnd1 = Point3.createMultiplication(line, LINE_DISTANCE);
	    Point3 lineEnd2 = Point3.createMultiplication(line, -LINE_DISTANCE);
		vertices[0].position.x = lineEnd1.x;
		vertices[0].position.y = lineEnd1.y;
		vertices[0].position.z = lineEnd1.z;
		vertices[1].position.x = lineEnd2.x;
		vertices[1].position.y = lineEnd2.y;
		vertices[1].position.z = lineEnd2.z;
	}
	
	public void setColor(Color4f color)
	{
		s_sgFrontFacingAppearance.setDiffuseColor(color);
	}

	
}
