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

package org.alice.ide.recyclebin;

/**
 * @author Dennis Cosgrove
 */
public class RecycleBin extends org.lgna.croquet.components.JComponent< javax.swing.JComponent > {
	private static final java.awt.Image OPEN_IMAGE;
	private static final java.awt.Image CLOSED_IMAGE;
	static {
		OPEN_IMAGE = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( RecycleBin.class.getResource( "images/open.png" ) ).getImage();
		CLOSED_IMAGE = edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( RecycleBin.class.getResource( "images/closed.png" ) ).getImage();
	}
	@Override
	protected javax.swing.JComponent createAwtComponent() {
		return new javax.swing.JComponent() {
			@Override
			protected void paintComponent( java.awt.Graphics g ) {
				super.paintComponent( g );
				g.drawImage( CLOSED_IMAGE, 0, 0, this );
			}
			@Override
			public java.awt.Dimension getPreferredSize() {
				return new java.awt.Dimension( 24, 40 );
			}
		};
	}
	
	public static void main( String[] args ) {
		org.lgna.croquet.Application application = new org.lgna.croquet.Application() {
			@Override
			protected org.lgna.croquet.components.Component< ? > createContentPane() {
				return new RecycleBin();
			}
			@Override
			public org.lgna.croquet.DropReceptor getDropReceptor( org.lgna.croquet.DropSite dropSite ) {
				return null;
			}
			@Override
			protected org.lgna.croquet.Operation< ? > getAboutOperation() {
				return null;
			}
			@Override
			protected org.lgna.croquet.Operation< ? > getPreferencesOperation() {
				return null;
			}
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleOpenFile( org.lgna.croquet.triggers.Trigger trigger ) {
			}
			@Override
			protected void handleQuit( org.lgna.croquet.triggers.Trigger trigger ) {
				System.exit( 0 );
			}
		};
		application.initialize( args );
		application.getFrame().pack();
		application.getFrame().setVisible( true );
	}
}
