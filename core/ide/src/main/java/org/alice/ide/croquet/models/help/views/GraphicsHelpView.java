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
package org.alice.ide.croquet.models.help.views;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsHelpView extends org.lgna.croquet.views.MigPanel {
	private static String getSystemInformation() {
		StringBuilder sb = new StringBuilder();
		sb.append( "System information: " );
		sb.append( System.getProperty( "os.name" ) );
		sb.append( " " );
		sb.append( System.getProperty( "sun.arch.data.model" ) );
		sb.append( "-bit" );
		return sb.toString();
	}

	private static String getGraphicsInformation() {
		StringBuilder sb = new StringBuilder();
		sb.append( "Graphics information: " );
		edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SharedDetails sharedDetails = edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SINGLETON.getSharedDetails();
		if( sharedDetails != null ) {
			sb.append( sharedDetails.getRenderer() );
		} else {
			sb.append( "<unknown>" );
		}
		return sb.toString();
	}

	public GraphicsHelpView() {
		edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SynchronousPickDetails synchronousPickDetails = edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults.SINGLETON.getSynchronousPickDetails();
		StringBuilder sb = new StringBuilder();
		sb.append( "FYI: " );
		if( synchronousPickDetails != null ) {
			if( synchronousPickDetails.isPickFunctioningCorrectly() ) {
				sb.append( "Clicking into the scene appears to be functioning correctly" );
				if( synchronousPickDetails.isPickActuallyHardwareAccelerated() ) {
					sb.append( " in hardware" );
				} else {
					sb.append( " in software (updating your video drivers might help)" );
					if( synchronousPickDetails.isReportingPickCanBeHardwareAccelerated() ) {
						sb.append( "(video card reports hardware support but fails)" );
					}
				}
			} else {
				sb.append( "Clicking into the scene appears to be suboptimal (updating your video drivers might help)" );
			}
		} else {
			sb.append( "There is no information on clicking into the scene" );
		}
		sb.append( "." );
		final int LEVEL_1 = 16;
		final int LEVEL_2 = 32;

		this.addComponent( new org.lgna.croquet.views.Label( "The most common way to fix graphics problems is to update your video driver.", 1.2f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ), "wrap" );
		this.addComponent( new org.lgna.croquet.views.Label( "Where to go for help:" ), "wrap, gapleft " + LEVEL_1 );
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
			this.addComponent( org.alice.ide.system.croquet.WindowsSystemAssessmentToolComposite.getInstance().getLaunchOperation().createButton(), "wrap, gapleft " + LEVEL_2 );
			this.addComponent( org.alice.ide.system.croquet.StartPerformanceInformationAndToolsOperation.getInstance().createButton(), "wrap, gapleft " + LEVEL_2 );
		}
		this.addComponent( org.alice.ide.croquet.models.help.SearchForGraphicsDriversOperation.getInstance().createHyperlink(), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( org.alice.ide.issue.croquet.GraphicsDriverHelpOperation.getInstance().createHyperlink(), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( new org.lgna.croquet.views.Label( "About your computer:" ), "wrap, gaptop 16, gapleft " + LEVEL_1 );
		this.addComponent( new org.lgna.croquet.views.Label( getGraphicsInformation() ), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( new org.lgna.croquet.views.Label( getSystemInformation() ), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( new org.lgna.croquet.views.Label( sb.toString() ), "wrap, gaptop 24" );
	}
}
