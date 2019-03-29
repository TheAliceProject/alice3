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

package test.ik.croquet.views;

import edu.cmu.cs.dennisc.java.awt.font.TextFamily;
import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.List;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.croquet.views.Separator;
import org.lgna.croquet.views.TextArea;
import org.lgna.ik.core.solver.Bone;
import test.ik.croquet.AnchorJointIdState;
import test.ik.croquet.BonesState;
import test.ik.croquet.ControlsComposite;
import test.ik.croquet.EndJointIdState;
import test.ik.croquet.InfoState;
import test.ik.croquet.IsAngularEnabledState;
import test.ik.croquet.IsLinearEnabledState;

/**
 * @author Dennis Cosgrove
 */
public class ControlsPanel extends PageAxisPanel {
	public ControlsPanel( ControlsComposite composite ) {
		super( composite );

		this.addComponent( IsLinearEnabledState.getInstance().createCheckBox() );
		this.addComponent( IsAngularEnabledState.getInstance().createCheckBox() );

		this.addComponent( new LineAxisPanel(
				new Label( "anchor:", TextPosture.OBLIQUE ),
				new JointIdDropDown( AnchorJointIdState.getInstance() )
				) );
		this.addComponent( new LineAxisPanel(
				new Label( "end:", TextPosture.OBLIQUE ),
				new JointIdDropDown( EndJointIdState.getInstance() )
				) );

		this.addComponent( BoxUtilities.createVerticalSliver( 4 ) );
		this.addComponent( Separator.createInstanceSeparatingTopFromBottom() );
		this.addComponent( BoxUtilities.createVerticalSliver( 4 ) );

		this.addComponent( new Label( "chain:", TextPosture.OBLIQUE ) );

		List<Bone> list = BonesState.getInstance().createList();
		list.setAlignmentX( 0.0f );
		list.setBackgroundColor( null );
		this.addComponent( list );

		this.addComponent( BoxUtilities.createVerticalSliver( 4 ) );
		this.addComponent( Separator.createInstanceSeparatingTopFromBottom() );
		this.addComponent( BoxUtilities.createVerticalSliver( 4 ) );

		this.addComponent( new Label( "info:", TextPosture.OBLIQUE ) );

		TextArea textArea = InfoState.getInstance().createTextArea();
		textArea.getAwtComponent().setEditable( false );
		textArea.setBorder( null );
		textArea.changeFont( TextFamily.MONOSPACED );
		ScrollPane scrollPane = new ScrollPane( textArea );
		scrollPane.setAlignmentX( 0.0f );
		this.addComponent( scrollPane );
		this.setMinimumPreferredWidth( 200 );
	}
}
