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
import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.alice.ide.declarationseditor.type.components.FieldList;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.GridBagPanel;

public abstract class AbstractMarkerManagerPanel extends GridBagPanel {

	protected Button moveToMarkerButton;
	protected Button moveToObjectButton;
	protected FieldList fieldList;

	protected abstract Button createMovetoMarkerButton();

	protected abstract Button createMoveToObjectButton();

	protected abstract FieldList createFieldList( org.lgna.project.ast.UserType<?> type );

	protected String getTitleString()
	{
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( AbstractMarkerManagerPanel.class.getPackage().getName() + ".croquet" );
		return resourceBundle.getString( this.getClass().getSimpleName() + ".title" );
	}

	public void setType( org.lgna.project.ast.UserType<?> type )
	{
		this.removeAllComponents();
		this.fieldList = this.createFieldList( type );
		this.fieldList.setBackgroundColor( this.getBackgroundColor() );
		this.addComponent( new org.lgna.croquet.views.Label( getTitleString(), 1.0f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD ), new GridBagConstraints(
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 2, 2, 2, 2 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		if( this.moveToMarkerButton == null ) {
			this.moveToMarkerButton = createMovetoMarkerButton();
		}
		this.addComponent( this.moveToMarkerButton, new GridBagConstraints(
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		if( this.moveToObjectButton == null ) {
			this.moveToObjectButton = createMoveToObjectButton();
		}
		this.addComponent( this.moveToObjectButton, new GridBagConstraints(
				2, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
		);

		this.addComponent( this.fieldList, new GridBagConstraints(
				0, //gridX
				1, //gridY
				3, //gridWidth
				1, //gridHeight
				0.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTHWEST, //anchor 
				GridBagConstraints.VERTICAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints(
				3, //gridX
				0, //gridY
				1, //gridWidth
				3, //gridHeight
				1.0, //weightX
				1.0, //weightY
				GridBagConstraints.NORTH, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets
				0, //ipadX
				0 ) //ipadY
		);

		this.updateButtons();
	}

	public void updateButtons() {
		this.revalidateAndRepaint();
	}

	protected Button getMoveToMarkerButton() {
		return this.moveToMarkerButton;
	}

	protected Button getMoveToObjectButton() {
		return this.moveToObjectButton;
	}

	@Override
	public void setBackgroundColor( Color color )
	{
		super.setBackgroundColor( color );
		if( this.fieldList != null ) {
			this.fieldList.setBackgroundColor( color );
		}
		//        this.fieldList.setUnselectedBackgroundColor(color);
	}

	public void setSelectedItemBackgroundColor( Color color )
	{
		//        this.fieldList.setSelectedBackgroundColor(color);
	}

}
