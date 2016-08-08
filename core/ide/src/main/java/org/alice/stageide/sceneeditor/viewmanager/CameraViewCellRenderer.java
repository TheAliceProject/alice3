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
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Color;

import org.alice.stageide.sceneeditor.View;
import org.lgna.story.implementation.CameraMarkerImp;

import edu.cmu.cs.dennisc.javax.swing.renderers.ListCellRenderer;

/**
 * @author dculyba
 * 
 */
public class CameraViewCellRenderer extends ListCellRenderer<View> {

	private final javax.swing.border.Border separatorBelowBorder = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 8, 0 );
	private final javax.swing.border.Border emptyBorder = javax.swing.BorderFactory.createEmptyBorder( 2, 2, 2, 0 );

	private final CameraMarkerTracker cameraMarkerTracker;

	public CameraViewCellRenderer( CameraMarkerTracker cameraMarkerTracker ) {
		this.cameraMarkerTracker = cameraMarkerTracker;
	}

	@Override
	protected javax.swing.JLabel getListCellRendererComponent( javax.swing.JLabel rv, javax.swing.JList list, View view, int index, boolean isSelected, boolean cellHasFocus ) {
		CameraMarkerImp value = this.cameraMarkerTracker.getCameraMarker( view );
		rv.setText( MarkerUtilities.getNameForView( view ) );
		if( index == 0 ) {
			rv.setBorder( separatorBelowBorder );
		} else {
			rv.setBorder( emptyBorder );
		}
		if( isSelected )
		{
			rv.setOpaque( true );
			rv.setBackground( new Color( 57, 105, 138 ) );
			rv.setForeground( Color.WHITE );
			rv.setIcon( MarkerUtilities.getHighlightedIconForCameraMarkerImp( value ) );
		}
		else
		{
			rv.setOpaque( false );
			rv.setForeground( Color.BLACK );
			rv.setIcon( MarkerUtilities.getIconForCameraMarkerImp( value ) );
		}
		return rv;
	}

}
