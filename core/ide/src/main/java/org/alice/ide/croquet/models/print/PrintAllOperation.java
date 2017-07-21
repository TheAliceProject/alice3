/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.croquet.models.print;

/**
 * @author Dennis Cosgrove
 */
public class PrintAllOperation extends PrintOperation {
	private static class SingletonHolder {
		private static PrintAllOperation instance = new PrintAllOperation();
	}

	public static PrintAllOperation getInstance() {
		return SingletonHolder.instance;
	}

	private PrintAllOperation() {
		super( java.util.UUID.fromString( "6a205070-e8e0-48a7-a059-fff15b7350a3" ) );
	}

	@Override
	protected java.awt.print.Printable getPrintable() {
		return new java.awt.print.Printable() {
			@Override
			public int print( java.awt.Graphics g, java.awt.print.PageFormat pageFormat, int pageIndex ) throws java.awt.print.PrinterException {
				if( pageIndex > 0 ) {
					return NO_SUCH_PAGE;
				} else {
					java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
					org.lgna.croquet.views.Frame frame = org.lgna.croquet.Application.getActiveInstance().getDocumentFrame().getFrame();
					int width = frame.getWidth();
					int height = frame.getHeight();
					double scale = edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities.calculateScale( pageFormat, width, height );
					g2.translate( pageFormat.getImageableX(), pageFormat.getImageableY() );
					if( scale > 1.0 ) {
						g2.scale( 1.0 / scale, 1.0 / scale );
					}
					frame.getAwtComponent().paintAll( g2 );
					return PAGE_EXISTS;
				}
			}
		};
	}
}
