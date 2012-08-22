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

package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.GridBagPanel;

public class MarkerManagerPanel extends GridBagPanel
{
	private SceneCameraMarkerManagerPanel cameraMarkerManagerPanel = null;
	private SceneObjectMarkerManagerPanel objectMarkerManagerPanel = null;

	public MarkerManagerPanel( org.alice.stageide.croquet.models.sceneditor.MarkerPanelTab tab )
	{
		super( tab );
		this.cameraMarkerManagerPanel = new SceneCameraMarkerManagerPanel();
		this.objectMarkerManagerPanel = new SceneObjectMarkerManagerPanel();

		int index = 0;

		this.addComponent( this.cameraMarkerManagerPanel, new GridBagConstraints(
				0, // gridX
				index++, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTH, // anchor
				GridBagConstraints.BOTH, // fill
				new Insets( 2, 0, 2, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.addComponent( new org.lgna.croquet.components.HorizontalSeparator(), new GridBagConstraints(
				0, // gridX
				index++, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.CENTER, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 4, 0, 4, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.addComponent( this.objectMarkerManagerPanel, new GridBagConstraints(
				0, // gridX
				index++, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.NORTH, // anchor
				GridBagConstraints.BOTH, // fill
				new Insets( 2, 0, 2, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.addComponent( BoxUtilities.createVerticalGlue(), new GridBagConstraints(
				0, // gridX
				index++, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				1.0, // weightY
				GridBagConstraints.CENTER, // anchor
				GridBagConstraints.VERTICAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.setBackgroundColor( Color.RED );
	}

	@Override
	public void setBackgroundColor( Color color )
	{
		super.setBackgroundColor( color );
		this.cameraMarkerManagerPanel.setBackgroundColor( color );
		this.objectMarkerManagerPanel.setBackgroundColor( color );
	}

	public void setSelectedItemColor( Color color )
	{
		this.cameraMarkerManagerPanel.setSelectedItemBackgroundColor( color );
		this.objectMarkerManagerPanel.setSelectedItemBackgroundColor( color );
	}

	public SceneObjectMarkerManagerPanel getObjectMarkerPanel()
	{
		return this.objectMarkerManagerPanel;
	}

	public SceneCameraMarkerManagerPanel getCameraMarkerPanel()
	{
		return this.cameraMarkerManagerPanel;
	}
}
