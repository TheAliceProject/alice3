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

package rose;

class Layer {
}

class LayerListModel extends javax.swing.AbstractListModel {
	private java.util.List< Layer > layers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public Layer getElementAt( int index ) {
		return this.layers.get( index );
	}
	public int getSize() {
		return this.layers.size();
	}
}
class Canvas extends edu.cmu.cs.dennisc.croquet.JComponent< javax.swing.JComponent > { 
	@Override
	protected javax.swing.JComponent createAwtComponent() {
		final java.io.File file = new java.io.File( "C:/Users/dennisc/Pictures/paintingTheRoseBushes.png" );
		final javax.media.jai.PlanarImage planarImage = javax.media.jai.JAI.create( "fileload", file.getAbsolutePath() );
		
		return new javax.swing.JComponent() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				g.setColor( java.awt.Color.RED );
				g.fillRect( 0,0,640,480 );
				g.drawImage( planarImage.getAsBufferedImage(), 0, 0, this );
			}
		};
	}
}

/**
 * @author Dennis Cosgrove
 */
public class Rose extends edu.cmu.cs.dennisc.croquet.Application {
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component< ? > createContentPane() {
		edu.cmu.cs.dennisc.croquet.BorderPanel rv = new edu.cmu.cs.dennisc.croquet.BorderPanel();
		rv.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "toolbox" ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_START );
		rv.addComponent( new Canvas(), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.CENTER );
		rv.addComponent( new edu.cmu.cs.dennisc.croquet.Label( "layers" ), edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint.LINE_END );
		return rv;
	}
	@Override
	protected void handleAbout( java.util.EventObject e ) {
	}
	@Override
	protected void handlePreferences( java.util.EventObject e ) {
	}
	@Override
	protected void handleQuit( java.util.EventObject e ) {
		System.exit( 0 );
	}
	
	@Override
	protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
	}
	
	@Override
	public edu.cmu.cs.dennisc.croquet.DropReceptor getDropReceptor(edu.cmu.cs.dennisc.croquet.DropSite dropSite) {
		return null;
	}

	public static void main( String[] args ) {
		Rose rose = new Rose();
		rose.initialize( args );
		rose.getFrame().setSize( 640, 480 );
		rose.getFrame().setVisible( true );
	}
}
