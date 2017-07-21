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
package org.alice.ide.issue;

import edu.cmu.cs.dennisc.issue.Attachment;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsPropertiesAttachment implements Attachment {
	private static org.w3c.dom.Element createXmlElement( org.w3c.dom.Document xmlDocument, String name, String value ) {
		org.w3c.dom.Element rv = xmlDocument.createElement( name );
		rv.appendChild( xmlDocument.createTextNode( value ) );
		return rv;
	}

	private static java.io.ByteArrayOutputStream getPropertiesAsXMLByteArrayOutputStream() {
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		org.w3c.dom.Document xmlDocument = edu.cmu.cs.dennisc.xml.XMLUtilities.createDocument();
		org.w3c.dom.Element xmlRootElement = xmlDocument.createElement( "graphicsProperties" );
		xmlDocument.appendChild( xmlRootElement );
		edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SharedDetails sharedDetails = edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SINGLETON.getSharedDetails();
		edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SynchronousPickDetails synchronousPickDetails = edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SINGLETON.getSynchronousPickDetails();
		if( sharedDetails != null ) {
			org.w3c.dom.Element xmlSharedElement = xmlDocument.createElement( "sharedProperties" );
			xmlRootElement.appendChild( xmlSharedElement );

			xmlSharedElement.appendChild( createXmlElement( xmlDocument, "renderer", sharedDetails.getRenderer() ) );
			xmlSharedElement.appendChild( createXmlElement( xmlDocument, "vendor", sharedDetails.getVendor() ) );
			xmlSharedElement.appendChild( createXmlElement( xmlDocument, "version", sharedDetails.getVersion() ) );
			xmlSharedElement.appendChild( createXmlElement( xmlDocument, "extensions", java.util.Arrays.toString( sharedDetails.getExtensions() ) ) );
		}
		if( synchronousPickDetails != null ) {
			org.w3c.dom.Element xmlPickElement = xmlDocument.createElement( "pickProperties" );
			xmlRootElement.appendChild( xmlPickElement );

			xmlPickElement.appendChild( createXmlElement( xmlDocument, "isPickFunctioningCorrectly", Boolean.toString( synchronousPickDetails.isPickFunctioningCorrectly() ) ) );
			xmlPickElement.appendChild( createXmlElement( xmlDocument, "isReportingPickCanBeHardwareAccelerated", Boolean.toString( synchronousPickDetails.isReportingPickCanBeHardwareAccelerated() ) ) );
			xmlPickElement.appendChild( createXmlElement( xmlDocument, "isPickActuallyHardwareAccelerated", Boolean.toString( synchronousPickDetails.isPickActuallyHardwareAccelerated() ) ) );
		}
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, baos );
		try {
			baos.flush();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
		return baos;
	}

	@Override
	public byte[] getBytes() {
		return getPropertiesAsXMLByteArrayOutputStream().toByteArray();
	}

	@Override
	public String getMIMEType() {
		return "application/xml";
	}

	@Override
	public String getFileName() {
		return "graphicsProperties.xml";
	}

	public static void main( String[] args ) {
		System.out.println( new String( new GraphicsPropertiesAttachment().getBytes() ) );
	}
}
