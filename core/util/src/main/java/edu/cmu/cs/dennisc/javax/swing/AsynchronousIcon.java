/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.javax.swing;

/**
 * @author Dennis Cosgrove
 */
public abstract class AsynchronousIcon implements javax.swing.Icon {
	private class IconWorker extends edu.cmu.cs.dennisc.worker.Worker<javax.swing.Icon> {
		@Override
		protected javax.swing.Icon do_onBackgroundThread() throws java.lang.Exception {
			return AsynchronousIcon.this.do_onBackgroundThread();
		}

		@Override
		protected void handleDone_onEventDispatchThread( javax.swing.Icon value ) {
			AsynchronousIcon.this.handleDone_onEventDispatchThread( value );
		}
	}

	public AsynchronousIcon( int width, int height ) {
		this.width = width;
		this.height = height;
	}

	public int getIconWidth() {
		if( this.icon != null ) {
			if( this.icon.getIconWidth() == this.width ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "width:", this.icon.getIconWidth(), this.width );
			}
		}
		return this.width;
	}

	public int getIconHeight() {
		if( this.icon != null ) {
			if( this.icon.getIconHeight() == this.height ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "height:", this.icon.getIconHeight(), this.height );
			}
		}
		return this.height;
	}

	private static java.awt.Component getComponentToRepaint( java.awt.Component c ) {
		java.awt.Container parent = c.getParent();
		if( parent instanceof javax.swing.CellRendererPane ) {
			java.awt.Container grandparent = parent.getParent();
			return grandparent;
		} else {
			return c;
		}
	}

	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		if( this.worker.isDone() ) {
			if( this.icon != null ) {
				this.icon.paintIcon( c, g, x, y );
			} else {
				//g.setColor( java.awt.Color.RED );
				//g.fillRect( x, y, 100, 100 );
			}
		} else {

			java.awt.Component componentToRepaint = getComponentToRepaint( c );
			if( this.componentsToRepaint != null ) {
				if( this.componentsToRepaint.contains( componentToRepaint ) ) {
					//pass
				} else {
					this.componentsToRepaint.add( componentToRepaint );
				}
			} else {
				this.componentsToRepaint = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList( componentToRepaint );
				this.worker.execute();
			}
		}
	}

	protected abstract javax.swing.Icon do_onBackgroundThread() throws java.lang.Exception;

	private void handleDone_onEventDispatchThread( javax.swing.Icon value ) {
		this.icon = value;
		if( this.componentsToRepaint != null ) {
			for( java.awt.Component component : this.componentsToRepaint ) {
				component.repaint();
			}
			this.componentsToRepaint = null;
		}
	}

	private final int width;
	private final int height;
	private final IconWorker worker = new IconWorker();
	private javax.swing.Icon icon;
	private java.util.List<java.awt.Component> componentsToRepaint;
}
