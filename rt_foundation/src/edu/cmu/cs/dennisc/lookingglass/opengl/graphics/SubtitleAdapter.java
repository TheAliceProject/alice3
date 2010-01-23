package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public class SubtitleAdapter extends TitleAdapter< edu.cmu.cs.dennisc.scenegraph.graphics.Subtitle > {
	@Override
	protected java.awt.geom.Rectangle2D.Float getFillBounds( java.awt.geom.Rectangle2D.Float rv, java.awt.Rectangle actualViewport, java.awt.geom.Dimension2D multilineTextSize ) {
		rv.setFrame( actualViewport );
		rv.height = (float)multilineTextSize.getHeight() + VERTICAL_MARGIN_TIMES_2;
		rv.y = actualViewport.height - rv.height;
		return rv;
	}
}
