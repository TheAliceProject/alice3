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

import edu.cmu.cs.dennisc.java.awt.font.TextWeight;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.ResourceBundleUtilities;
import edu.cmu.cs.dennisc.system.graphics.ConformanceTestResults;
import org.alice.ide.croquet.models.help.GraphicsHelpComposite;
import org.alice.ide.croquet.models.help.SearchForGraphicsDriversOperation;
import org.alice.ide.issue.croquet.GraphicsDriverHelpOperation;
import org.alice.ide.system.croquet.StartPerformanceInformationAndToolsOperation;
import org.alice.ide.system.croquet.WindowsSystemAssessmentToolComposite;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsHelpView extends MigPanel {
	private static String getSystemInformation() {
		return String.format( getLocalizedStringByKey( "systemInfo" ),
						System.getProperty( "os.name" ),
						System.getProperty( "sun.arch.data.model" ) );
	}

	private static String getGraphicsInformation() {
		ConformanceTestResults.SharedDetails sharedDetails = ConformanceTestResults.SINGLETON.getSharedDetails();

		return String.format( getLocalizedStringByKey( "graphicsInfo" ),
						sharedDetails != null ? sharedDetails.getRenderer() : getLocalizedStringByKey( "unknownGraphics" ));
	}

	public GraphicsHelpView() {
		final int LEVEL_1 = 16;
		final int LEVEL_2 = 32;

		this.addComponent( new Label( getLocalizedStringByKey( "commonFix" ), 1.2f, TextWeight.BOLD ), "wrap" );
		this.addComponent( new Label( getLocalizedStringByKey( "helpHeader" ) ), "wrap, gapleft " + LEVEL_1 );
		if( SystemUtilities.isWindows() ) {
			this.addComponent( WindowsSystemAssessmentToolComposite.getInstance().getLaunchOperation().createButton(), "wrap, gapleft " + LEVEL_2 );
			this.addComponent( StartPerformanceInformationAndToolsOperation.getInstance().createButton(), "wrap, gapleft " + LEVEL_2 );
		}
		this.addComponent( SearchForGraphicsDriversOperation.getInstance().createHyperlink(), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( GraphicsDriverHelpOperation.getInstance().createHyperlink(), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( new Label( getLocalizedStringByKey( "aboutHeader" ) ), "wrap, gaptop 16, gapleft " + LEVEL_1 );
		this.addComponent( new Label( getGraphicsInformation() ), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( new Label( getSystemInformation() ), "wrap, gapleft " + LEVEL_2 );
		this.addComponent( new Label( getLocalizedStringByKey( getPickInfoKey() ) ), "wrap, gaptop 24" );
	}

	private String getPickInfoKey() {
		ConformanceTestResults.SynchronousPickDetails synchronousPickDetails = ConformanceTestResults.SINGLETON.getSynchronousPickDetails();
		if (synchronousPickDetails == null) {
			return "sceneClickNothing";
		}

		if( synchronousPickDetails.isPickFunctioningCorrectly() ) {
			if( synchronousPickDetails.isPickActuallyHardwareAccelerated() ) {
				return "sceneClickHardware";
			} else {
				return synchronousPickDetails.isReportingPickCanBeHardwareAccelerated() ?
								"sceneClickSoftwareAccel" :
								"sceneClickSoftware";
			}
		} else {
			return "sceneClickNoPick";
		}
	}

	private static String getLocalizedStringByKey( String key ) {
		return ResourceBundleUtilities.getStringForKey( key, GraphicsHelpComposite.class );
	}
}
