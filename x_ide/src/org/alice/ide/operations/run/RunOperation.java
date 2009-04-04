/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.ide.operations.run;

class RunIcon implements javax.swing.Icon {
	public int getIconHeight() {
		return 18;
	}
	public int getIconWidth() {
		return 18;
	}
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		java.awt.Color prevColor = g.getColor();
		try {
			g.setColor( java.awt.Color.GREEN.darker() );
			int w = this.getIconWidth();
			int h = this.getIconHeight();
			g.fillOval( x, y, w, h );

			int offset = w / 5;
			int x0 = x + offset * 2;
			int x1 = x + w - offset;

			int y0 = y + offset;
			int y1 = y + h - offset;
			int yC = (y0 + y1) / 2;

			int[] xs = { x0, x1, x0 };
			int[] ys = { y0, yC, y1 };

			g.setColor( java.awt.Color.WHITE );
			g.fillPolygon( xs, ys, 3 );
		} finally {
			g.setColor( prevColor );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RunOperation extends org.alice.ide.operations.AbstractActionOperation {
	public RunOperation() {
		this.putValue( javax.swing.Action.NAME, "Run..." );
		this.putValue( javax.swing.Action.SMALL_ICON, new RunIcon() );
		this.putValue( javax.swing.Action.MNEMONIC_KEY, java.awt.event.KeyEvent.VK_R );
	}
	public void perform( zoot.ActionContext actionContext ) {
		this.getIDE().handleRun( actionContext );
		actionContext.commit();
	}
}
