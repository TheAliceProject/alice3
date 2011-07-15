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

import javax.swing.BorderFactory;


import org.lookingglassandalice.storytelling.MarkerWithIcon;
import org.alice.ide.IDE;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;
import edu.cmu.cs.dennisc.color.Color4f;

public class MarkerFieldTile extends LineAxisPanel
{
	private FieldDeclaredInAlice field;
	private Label iconLabel = new Label();
	private Label textLabel = new Label();
	
	public MarkerFieldTile()
	{
		super();
		this.setBackgroundColor(null);
		this.setBorder(BorderFactory.createEmptyBorder());

		this.textLabel.changeFont(edu.cmu.cs.dennisc.java.awt.font.TextPosture.REGULAR, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD);
		this.textLabel.scaleFont(1.4f);
		this.textLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
	}
	
	public MarkerFieldTile( FieldDeclaredInAlice field )
	{
		this();
		setField(field);
	}
	
	public void setSelected( boolean isSelected ) {
		MarkerWithIcon marker = ((MoveAndTurnSceneEditor)(IDE.getActiveInstance().getSceneEditor())).getMarkerForField(field);
		java.awt.Color foregroundColor = java.awt.Color.BLACK;
		if( marker != null ) {
			foregroundColor = marker.getMarkerColor().getAsAWTColor();
			if( isSelected ) {
				//pass
			} else {
				foregroundColor = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( foregroundColor, 1.0, 0.5, 0.5 );
			}
		}
		this.textLabel.setForegroundColor( foregroundColor );
	}
	
	public void setField( FieldDeclaredInAlice field )
	{
		this.removeAllComponents();
		this.field = field;
		
		this.textLabel.setText( this.field.getName() );
		
		MarkerWithIcon marker = ((MoveAndTurnSceneEditor)(IDE.getActiveInstance().getSceneEditor())).getMarkerForField(field);
		if (marker != null)
		{
			Color4f color = marker.getMarkerColor();
			this.textLabel.setForegroundColor(color.getAsAWTColor());
			this.iconLabel.setIcon(marker.getIcon());
		}
		this.addComponent( iconLabel );
		this.addComponent( this.textLabel );
		this.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 16 ) );
	}
}
