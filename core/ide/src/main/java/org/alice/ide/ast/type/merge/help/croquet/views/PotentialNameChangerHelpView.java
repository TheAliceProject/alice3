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
package org.alice.ide.ast.type.merge.help.croquet.views;

import edu.cmu.cs.dennisc.javax.swing.ColorCustomizer;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import org.alice.ide.ast.type.merge.croquet.PotentialNameChanger;
import org.alice.ide.ast.type.merge.croquet.views.MemberPreviewPane;
import org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities;
import org.alice.ide.ast.type.merge.help.croquet.PotentialNameChangerHelpComposite;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.VerticalTextPosition;

import javax.swing.BorderFactory;
import java.awt.Color;

/**
 * @author Dennis Cosgrove
 */
public class PotentialNameChangerHelpView extends BorderPanel {
	private final MigPanel keepBothPanel = new MigPanel( null, "fill" );

	public PotentialNameChangerHelpView( PotentialNameChangerHelpComposite<?, ?, ?> composite ) {
		super( composite );
		AbstractLabel label = composite.getHeader().createLabel();
		label.setIcon( IconUtilities.getQuestionIcon() );
		label.setOpaque( true );
		label.setBackgroundColor( Color.WHITE );
		label.setVerticalTextPosition( VerticalTextPosition.TOP );
		label.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		label.getAwtComponent().setIconTextGap( 12 );
		this.addPageStartComponent( label );

		MigPanel previewPanel = new MigPanel();
		previewPanel.addComponent( composite.getImportNameText().createLabel(), "wrap" );
		previewPanel.addComponent( MemberPreviewPane.createView( composite.getPotentialNameChanger().getImportHub(), true ), "wrap" );
		previewPanel.addComponent( composite.getProjectNameText().createLabel(), "wrap" );
		previewPanel.addComponent( MemberPreviewPane.createView( composite.getPotentialNameChanger().getProjectHub(), true ), "wrap" );
		this.addCenterComponent( previewPanel );

		ColorCustomizer foregroundCustomizer = composite.getForegroundCustomizer();

		PotentialNameChanger<?> potentialNameChanger = composite.getPotentialNameChanger();
		this.keepBothPanel.addComponent( composite.getImportNameText().createLabel(), "align right" );
		this.keepBothPanel.addComponent( MemberViewUtilities.createTextField( potentialNameChanger.getImportHub().getNameState(), foregroundCustomizer ), "wrap" );
		this.keepBothPanel.addComponent( composite.getProjectNameText().createLabel(), "align right" );
		this.keepBothPanel.addComponent( MemberViewUtilities.createTextField( potentialNameChanger.getProjectHub().getNameState(), foregroundCustomizer ), "wrap" );
	}

	public MigPanel getKeepBothPanel() {
		return this.keepBothPanel;
	}
}
