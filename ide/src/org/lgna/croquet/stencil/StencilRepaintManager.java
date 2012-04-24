package org.lgna.croquet.stencil;

public final class StencilRepaintManager extends javax.swing.RepaintManager {

	private org.lgna.croquet.components.Stencil stencil;

	public StencilRepaintManager( org.lgna.croquet.components.Stencil stencil ) {
		this.stencil = stencil;
	}

	@Override
	public void addDirtyRegion(javax.swing.JComponent c, int x, int y, int w, int h) {
		super.addDirtyRegion(c, x, y, w, h);
		final javax.swing.JComponent stencil = this.stencil.getAwtComponent();
		if( stencil == c || stencil.isAncestorOf( c ) ) {
			//pass
		} else {
			java.awt.Component srcRoot = javax.swing.SwingUtilities.getRoot( c );
			java.awt.Component dstRoot = javax.swing.SwingUtilities.getRoot( stencil );

			if( srcRoot != null && srcRoot == dstRoot ) {
				java.awt.Rectangle rect = new java.awt.Rectangle(x,y,w,h);
				java.awt.Rectangle visibleRect = rect.intersection( c.getVisibleRect() );
				if( visibleRect.width != 0 && visibleRect.height != 0 ) {
					final java.awt.Rectangle rectAsSeenByStencil = edu.cmu.cs.dennisc.java.awt.ComponentUtilities.convertRectangle( c, visibleRect, stencil );
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							StencilRepaintManager.super.addDirtyRegion( stencil, rectAsSeenByStencil.x, rectAsSeenByStencil.y, rectAsSeenByStencil.width, rectAsSeenByStencil.height);
						}
					} );
				}
			}
		}
	}
}
