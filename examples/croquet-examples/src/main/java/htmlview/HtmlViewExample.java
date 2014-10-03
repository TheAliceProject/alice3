/**
 * Copyright (c) 2006-2013, Carnegie Mellon University. All rights reserved.
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
package htmlview;

/**
 * @author Dennis Cosgrove
 */
public class HtmlViewExample {
	public static void main( String[] args ) {
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				org.lgna.croquet.views.HtmlView topView = new org.lgna.croquet.views.HtmlView();
				String imageUrlSpec = "http://circleImage";

				java.net.URL imageFromResourceUrl = HtmlViewExample.class.getResource( "images/yashAndYak.png" );
				javax.swing.ImageIcon imageIconFromResource = new javax.swing.ImageIcon( imageFromResourceUrl );

				java.awt.Color backgroundColor = java.awt.Color.BLUE;
				java.awt.Color textColor = java.awt.Color.YELLOW;
				StringBuilder sb = new StringBuilder();
				sb.append( "<html>" );
				sb.append( "<body bgcolor=\"" );
				sb.append( edu.cmu.cs.dennisc.java.awt.ColorUtilities.toHashText( backgroundColor ) );
				sb.append( "\" text=\"" );
				sb.append( edu.cmu.cs.dennisc.java.awt.ColorUtilities.toHashText( textColor ) );
				sb.append( "\">" );
				sb.append( "<h2>drawn image</h2>" );
				sb.append( "<img src=\"" );
				sb.append( imageUrlSpec );
				sb.append( "\">" );
				sb.append( "<h2>local resource image</h2>" );
				sb.append( "<img src=\"" );
				sb.append( imageFromResourceUrl );
				sb.append( "\">" );
				sb.append( "<p>text" );
				sb.append( "</body>" );
				sb.append( "</html>" );

				java.awt.Image circleImage = new java.awt.image.BufferedImage( 64, 64, java.awt.image.BufferedImage.TYPE_3BYTE_BGR );
				java.awt.Graphics g = circleImage.getGraphics();
				g.setColor( java.awt.Color.RED );
				g.fillOval( 8, 8, 48, 48 );
				g.dispose();

				topView.addImageToCache( imageFromResourceUrl, imageIconFromResource.getImage() );
				topView.addImageToCache( edu.cmu.cs.dennisc.java.net.UrlUtilities.createUrl( imageUrlSpec ), circleImage );
				topView.setText( sb.toString() );

				org.lgna.croquet.views.HtmlView bottomView = new org.lgna.croquet.views.HtmlView();
				bottomView.setTextFromUrlLater( edu.cmu.cs.dennisc.java.net.UrlUtilities.createUrl( "http://www.cs.cmu.edu/~dennisc" ) );
				bottomView.getHtmlDocument().getStyleSheet().addRule( "A {color:" + edu.cmu.cs.dennisc.java.awt.ColorUtilities.toHashText( java.awt.Color.GREEN.darker() ) + "}" );

				org.lgna.croquet.simple.SimpleApplication app = new org.lgna.croquet.simple.SimpleApplication();
				app.getFrame().getContentPane().addPageStartComponent( topView );
				app.getFrame().getContentPane().addPageEndComponent( bottomView );
				app.getFrame().setSize( 1000, 800 );
				app.getFrame().setVisible( true );
			}
		} );
	}
}
