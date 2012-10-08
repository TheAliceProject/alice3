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
package org.alice.ide.croquet.models.help.views;

/**
 * @author Dennis Cosgrove
 */
public class GraphicsPropertiesView extends org.lgna.croquet.components.PageAxisPanel {
	public GraphicsPropertiesView( org.alice.ide.croquet.models.help.GraphicsPropertiesComposite composite ) {
		super( composite );
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );

		this.addComponent( new org.lgna.croquet.components.FormPanel() {
			private org.lgna.croquet.components.LabeledFormRow createRow( String a, String b ) {
				return org.lgna.croquet.components.LabeledFormRow.createFromLabel( new org.lgna.croquet.components.Label( a ), new org.lgna.croquet.components.Label( b ) );
			}

			@Override
			protected void appendRows( java.util.List<org.lgna.croquet.components.LabeledFormRow> rows ) {
				edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults.SharedDetails sharedDetails = edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults.SINGLETON.getSharedDetails();
				if( sharedDetails != null ) {
					rows.add( this.createRow( "renderer:", sharedDetails.getRenderer() ) );
					rows.add( this.createRow( "vendor:", sharedDetails.getVendor() ) );
					rows.add( this.createRow( "version:", sharedDetails.getVersion() ) );
				}
			}
		} );

		edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults.PickDetails pickDetails = edu.cmu.cs.dennisc.lookingglass.opengl.ConformanceTestResults.SINGLETON.getPickDetails();
		StringBuilder sb = new StringBuilder();
		if( pickDetails != null ) {
			if( pickDetails.isPickFunctioningCorrectly() ) {
				sb.append( "Clicking into the scene appears to be functioning correctly" );
				if( pickDetails.isPickActuallyHardwareAccelerated() ) {
					sb.append( " in hardware" );
				} else {
					sb.append( " in software (updating your video drivers might help)" );
					if( pickDetails.isReportingPickCanBeHardwareAccelerated() ) {
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
		this.addComponent( new org.lgna.croquet.components.Label( sb.toString() ) );
		this.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 8 ) );
		this.addComponent( new GraphicsProblemsView() );
	}
}
