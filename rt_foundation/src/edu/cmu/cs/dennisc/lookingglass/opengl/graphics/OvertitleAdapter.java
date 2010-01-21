package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public class OvertitleAdapter extends TitleAdapter< edu.cmu.cs.dennisc.scenegraph.graphics.Subtitle > {
	@Override
	protected java.awt.geom.Rectangle2D.Float getFillBounds( java.awt.geom.Rectangle2D.Float rv, java.awt.Rectangle actualViewport, java.awt.geom.Rectangle2D textBounds ) {
		rv.setFrame( actualViewport );
		rv.height = (float)textBounds.getHeight() + VERTICAL_MARGIN_TIMES_2;		
		return rv;
	}
}
