package org.alice.interact;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

public class SnapLine extends Transformable {
	private InfiniteLineVisualization sgAxis = null;

	public SnapLine(Vector3 line) {
		sgAxis = new InfiniteLineVisualization(line);
		sgAxis.setParent( this );
	}
	
	public void setLine(Vector3 line)
	{
		sgAxis.setLine(line);
	}
	
	public void setColor(Color4f color)
	{
		sgAxis.setColor(color);
	}
}
