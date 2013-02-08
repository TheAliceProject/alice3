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
package org.lgna.ik.poser.view;

import org.lgna.croquet.components.BorderPanel;
import org.lgna.croquet.components.GridPanel;
import org.lgna.croquet.components.ItemDropDown;
import org.lgna.croquet.components.MigPanel;
import org.lgna.croquet.components.ScrollPane;
import org.lgna.ik.poser.JointSelectionSphere;
import org.lgna.ik.poser.JointSelectionSphereState;
import org.lgna.ik.poser.PoserControlComposite;

/**
 * @author Matt May
 */
public class PoserControlView extends MigPanel {

	public PoserControlView( PoserControlComposite poserControlComposite ) {
		super( poserControlComposite, "debug" );
		//		GridPanel panel = GridPanel.createGridPane( 2, 2 );
		GridPanel bottomPanel = GridPanel.createGridPane( 1, 2 );

		BorderPanel raPanel = new BorderPanel();
		raPanel.addPageStartComponent( poserControlComposite.getRightArmLabel().createLabel() );
		JointSelectionSphereState rightArmAnchor = poserControlComposite.getRightArmAnchor();
		ItemDropDown<JointSelectionSphere, JointSelectionSphereState> raDropDown = rightArmAnchor.createItemDropDown();
		raPanel.addCenterComponent( raDropDown );

		BorderPanel laPanel = new BorderPanel();
		laPanel.addPageStartComponent( poserControlComposite.getLeftArmLabel().createLabel() );
		JointSelectionSphereState leftArmAnchor = poserControlComposite.getLeftArmAnchor();
		ItemDropDown<JointSelectionSphere, JointSelectionSphereState> laDropDown = leftArmAnchor.createItemDropDown();
		laPanel.addCenterComponent( laDropDown );

		BorderPanel rlPanel = new BorderPanel();
		rlPanel.addPageStartComponent( poserControlComposite.getRightLegLabel().createLabel() );
		JointSelectionSphereState rightLegAnchor = poserControlComposite.getRightLegAnchor();
		ItemDropDown<JointSelectionSphere, JointSelectionSphereState> rlDropDown = rightLegAnchor.createItemDropDown();
		rlPanel.addCenterComponent( rlDropDown );

		BorderPanel llPanel = new BorderPanel();
		llPanel.addPageStartComponent( poserControlComposite.getLeftLegLabel().createLabel() );
		JointSelectionSphereState leftLegAnchor = poserControlComposite.getLeftLegAnchor();
		ItemDropDown<JointSelectionSphere, JointSelectionSphereState> llDropDown = leftLegAnchor.createItemDropDown();
		llPanel.addCenterComponent( llDropDown );

		//		panel.addComponent( raPanel );
		//		panel.addComponent( laPanel );
		//		panel.addComponent( rlPanel );
		//		panel.addComponent( llPanel );

		bottomPanel.addComponent( poserControlComposite.getSavePoseOperation().createButton() );
		bottomPanel.addComponent( poserControlComposite.getRunAnimationOperation().createButton() );

		BorderPanel listPanel = new BorderPanel();
		listPanel.addPageStartComponent( poserControlComposite.getDeselectPoseOperation().createButton() );
		listPanel.addPageEndComponent( GridPanel.createGridPane( 1, 2, poserControlComposite.getDeletePoseOperation().createButton(), poserControlComposite.getSaveUpdatedPoseOperation().createButton() ) );
		listPanel.addCenterComponent( new ScrollPane( poserControlComposite.getPosesList().createVerticalDefaultRadioButtons() ) );

		//		this.addPageStartComponent( panel );
		this.addComponent( raPanel );
		this.addComponent( laPanel, "wrap" );
		this.addComponent( rlPanel );
		this.addComponent( llPanel, "wrap" );

		this.addComponent( poserControlComposite.getDeselectPoseOperation().createButton(), "span 2, grow, wrap" );
		this.addComponent( new ScrollPane( poserControlComposite.getPosesList().createVerticalDefaultRadioButtons() ),
				"cell 0 3 2 6, growy" );
		//	"height 100:100:100, growy, span 2, wrap" );
		//		this.addComponent( listPanel, "span 2, wrap" );
		//		this.addComponent( new ScrollPane( poserControlComposite.getPosesList().createVerticalDefaultRadioButtons() ) );
		this.addComponent( bottomPanel, "span 2, dock south" );
		//		this.addCenterComponent( listPanel );
		//		this.addPageEndComponent( bottomPanel );
	}
}
