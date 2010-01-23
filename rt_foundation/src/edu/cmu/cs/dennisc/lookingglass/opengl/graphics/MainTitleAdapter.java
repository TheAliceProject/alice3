package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public class MainTitleAdapter extends TitleAdapter< edu.cmu.cs.dennisc.scenegraph.graphics.MainTitle > {
	@Override
	protected java.awt.geom.Rectangle2D.Float getFillBounds( java.awt.geom.Rectangle2D.Float rv, java.awt.Rectangle actualViewport, java.awt.geom.Dimension2D multilineTextSize ) {
		rv.setFrame( actualViewport );
		return rv;
	}
}
