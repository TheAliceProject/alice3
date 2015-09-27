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
public abstract class PrintOperation extends org.alice.ide.operations.InconsequentialActionOperation {
	public PrintOperation( java.util.UUID individualId ) {
		super( individualId );
	}

	protected abstract java.awt.print.Printable getPrintable();

	@Override
	protected final void performInternal( org.lgna.croquet.history.CompletionStep<?> step ) {
		java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
		java.awt.print.Printable printable = this.getPrintable();
		if( printable != null ) {
			job.setPrintable( printable );

			int mm = javax.print.attribute.Size2DSyntax.MM;
			javax.print.attribute.standard.MediaSize mediaSize = javax.print.attribute.standard.MediaSize.ISO.A4;

			float mediaWidth = mediaSize.getX( mm );
			float mediaHeight = mediaSize.getY( mm );
			float margin = mediaWidth / 50.0f;

			javax.print.attribute.PrintRequestAttributeSet set = new javax.print.attribute.HashPrintRequestAttributeSet();
			set.add( mediaSize.getMediaSizeName() );
			set.add( new javax.print.attribute.standard.MediaPrintableArea( margin, margin, mediaWidth - margin - margin, mediaHeight - margin - margin, mm ) );
			if( job.printDialog( set ) ) {
				try {
					job.print( set );
				} catch( java.awt.print.PrinterException pe ) {
					//todo
					pe.printStackTrace();
				}
			}
		} else {
			//todo
		}
	}
}
