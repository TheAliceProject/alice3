/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.cmu.cs.dennisc.croquet;

public abstract class Widget extends ViewController<javax.swing.JPanel,Model> {
	protected abstract int getInsetTop();
	protected abstract int getInsetLeft();
	protected abstract int getInsetBottom();
	protected abstract int getInsetRight();

	protected abstract void fillBounds( java.awt.Graphics2D g2, int x, int y, int width, int height );
	protected abstract void paintPrologue( java.awt.Graphics2D g2, int x, int y, int width, int height );
	protected abstract void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height );

	private boolean isBorderInitialized = false;
	private static boolean isWarningAlreadyPrinted = false;
	public Widget() {
		super( null );
		if( isWarningAlreadyPrinted ) {
			//pass
		} else {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: Widget", this.getClass() );
			isWarningAlreadyPrinted = true;
		}
	}
	
	private void updateBorderIfNecessary() {
		if( this.isBorderInitialized ) {
			//pass
		} else {
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( this.getInsetTop(), this.getInsetLeft(), this.getInsetBottom(), this.getInsetRight() ) );
		}
	}
	
	protected java.awt.Paint getForegroundPaint( int x, int y, int width, int height ) {
		return this.getForegroundColor();
	}
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		return this.getBackgroundColor();
	}

	public void addComponent(Component<?> component) {
		this.internalAddComponent(component);
	}
	public void addComponent(Component<?> component, Object constraints) {
		this.internalAddComponent(component, constraints);
	}

	protected abstract java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel );
	@Override
	protected javax.swing.JPanel createAwtComponent() {
		javax.swing.JPanel rv = new javax.swing.JPanel() {
			@Override
			public void addNotify() {
				super.addNotify();
				Widget.this.addNotify();
			}
			@Override
			public void removeNotify() {
				Widget.this.removeNotify();
				super.removeNotify();
			}
			@Override
			public void doLayout() {
				Widget.this.doLayout();
				super.doLayout();
			}
			@Override
			public boolean contains(int x, int y) {
				return Widget.this.contains(x, y, super.contains(x, y) );
			}
			@Override
			public javax.swing.JToolTip createToolTip() {
				return Widget.this.createToolTip( super.createToolTip() );
			}
			@Override
			public java.awt.Dimension getMaximumSize() {
				return this.getPreferredSize();
			}
			@Override
			public void paint(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				int x = 0;
				int y = 0;
				int width = getWidth();
				int height = getHeight();

				java.awt.Paint prevPaint;
				prevPaint = g2.getPaint();
				try {
					g2.setPaint( Widget.this.getBackgroundPaint( x, y, width, height ) );
					Widget.this.paintPrologue( g2, x, y, width, height );
				} finally {
					g2.setPaint( prevPaint );
				}
				Widget.this.paintEpilogue(g2, x, y, width, height);
				super.paint(g);
				prevPaint = g2.getPaint();
				g2.setPaint( Widget.this.getForegroundPaint( x, y, width, height ) );
				try {
					Widget.this.paintEpilogue( g2, x, y, width, height );
				} finally {
					g2.setPaint( prevPaint );
				}
			}
		};
		java.awt.LayoutManager layoutManager = this.createLayoutManager( rv );
		rv.setLayout( layoutManager );
		
		//rv.setOpaque( false );
		rv.setBackground( null );
		rv.setOpaque( false );
		
		//rv.setDoubleBuffered( false );
		rv.setAlignmentX( java.awt.Component.LEFT_ALIGNMENT );
		rv.setAlignmentY( java.awt.Component.CENTER_ALIGNMENT );
		
//		rv.setBackground( edu.cmu.cs.dennisc.java.awt.ColorUtilities.GARISH_COLOR );
//		rv.setOpaque( true );
		return rv;
	}
	
	protected boolean contains( int x, int y, boolean jContains ) {
		return jContains;
	}

	protected void doLayout() {
		this.updateBorderIfNecessary();
		//super.doLayout();
	}
	
	protected void addNotify() {
		this.updateBorderIfNecessary();
		//super.addNotify();
	}
	protected void removeNotify() {
		//super.removeNotify();
	}
	
	protected javax.swing.JToolTip createToolTip(javax.swing.JToolTip jToolTip) {
		return jToolTip;
	}
	
}
