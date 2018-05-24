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
package org.alice.ide.ast.type.merge.help.croquet;

import edu.cmu.cs.dennisc.javax.swing.ColorCustomizer;
import org.alice.ide.ast.type.merge.croquet.AddMembersPage;
import org.alice.ide.ast.type.merge.croquet.PotentialNameChanger;
import org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities;
import org.lgna.croquet.Application;
import org.lgna.croquet.Element;
import org.lgna.croquet.HtmlStringValue;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.SimpleOperationInputDialogCoreComposite;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.croquet.views.CompositeView;
import org.lgna.project.ast.Member;

import java.awt.Color;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class PotentialNameChangerHelpComposite<V extends CompositeView<?, ?>, M extends Member, N extends PotentialNameChanger<M>> extends SimpleOperationInputDialogCoreComposite<V> {
	//private final org.lgna.croquet.PlainStringValue header = this.createStringValue( this.createKey( "header" ) );
	private final HtmlStringValue header = new HtmlStringValue( UUID.fromString( "77e35d67-a35a-4a89-875d-7fab232445d5" ) ) {
	};

	private final PlainStringValue importNameText = this.createStringValue( "importNameText" );
	private final PlainStringValue projectNameText = this.createStringValue( "projectNameText" );
	private final ErrorStatus nameChangeRequiredError = this.createErrorStatus( "nameChangeRequiredError" );
	private final N potentialNameChanger;

	private final ColorCustomizer foregroundCustomizer = new ColorCustomizer() {
		@Override
		public Color changeColorIfAppropriate( Color defaultColor ) {
			if( isRetainBothSelected() ) {
				return areNamesIdentical() ? MemberViewUtilities.ACTION_MUST_BE_TAKEN_COLOR : defaultColor;
			} else {
				return defaultColor;
			}
		}
	};

	private boolean isImportDesiredPre;
	private boolean isProjectDesiredPre;
	private String importNamePre;
	private String projectNamePre;

	public PotentialNameChangerHelpComposite( UUID migrationId, N potentialNameChanger ) {
		super( migrationId, Application.INHERIT_GROUP );
		this.potentialNameChanger = potentialNameChanger;
	}

	protected abstract boolean isRetainBothSelected();

	@Override
	protected Status getStatusPreRejectorCheck( CompletionStep step ) {
		//todo
		this.getView().repaint();
		Status rv = IS_GOOD_TO_GO_STATUS;
		if( this.isRetainBothSelected() ) {
			if( this.areNamesIdentical() ) {
				rv = this.nameChangeRequiredError;
			}
		}
		return rv;
	}

	@Override
	protected String modifyLocalizedText( Element element, String localizedText ) {
		String rv = super.modifyLocalizedText( element, localizedText );
		if( rv != null ) {
			if( element == this.importNameText ) {
				rv = AddMembersPage.modifyFilenameLocalizedText( rv, this.potentialNameChanger.getUriForDescriptionPurposesOnly() );
			}
		}
		return rv;
	}

	public N getPotentialNameChanger() {
		return this.potentialNameChanger;
	}

	public ColorCustomizer getForegroundCustomizer() {
		return this.foregroundCustomizer;
	}

	public ErrorStatus getNameChangeRequiredError() {
		return this.nameChangeRequiredError;
	}

	public StringValue getHeader() {
		return this.header;
	}

	public PlainStringValue getImportNameText() {
		return this.importNameText;
	}

	public PlainStringValue getProjectNameText() {
		return this.projectNameText;
	}

	private boolean areNamesIdentical() {
		//todo
		return this.potentialNameChanger.getImportHub().getNameState().getValue().contentEquals( this.potentialNameChanger.getProjectHub().getNameState().getValue() );
	}

	@Override
	public void handlePreActivation() {
		this.isImportDesiredPre = this.potentialNameChanger.getImportHub().getIsDesiredState().getValue();
		this.isProjectDesiredPre = this.potentialNameChanger.getProjectHub().getIsDesiredState().getValue();
		this.importNamePre = this.potentialNameChanger.getImportHub().getNameState().getValue();
		this.projectNamePre = this.potentialNameChanger.getProjectHub().getNameState().getValue();
		super.handlePreActivation();
	}

	@Override
	protected void cancel( CompletionStep<?> completionStep ) {
		this.potentialNameChanger.getImportHub().getIsDesiredState().setValueTransactionlessly( this.isImportDesiredPre );
		this.potentialNameChanger.getProjectHub().getIsDesiredState().setValueTransactionlessly( this.isProjectDesiredPre );
		this.potentialNameChanger.getImportHub().getNameState().setValueTransactionlessly( this.importNamePre );
		this.potentialNameChanger.getProjectHub().getNameState().setValueTransactionlessly( this.projectNamePre );
		super.cancel( completionStep );
	}

	@Override
	protected final Edit createEdit( CompletionStep completionStep ) {
		return null;
	}

}
