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
package org.alice.ide.ast.type.merge.help.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class PotentialNameChangerHelpComposite<V extends org.lgna.croquet.components.View, M extends org.lgna.project.ast.Member, N extends org.alice.ide.ast.type.merge.croquet.PotentialNameChanger> extends org.lgna.croquet.OperationInputDialogCoreComposite<V> {
	private final org.lgna.croquet.PlainStringValue header = this.createStringValue( this.createKey( "header" ) );
	private final org.lgna.croquet.PlainStringValue importNameText = this.createStringValue( this.createKey( "importNameText" ) );
	private final org.lgna.croquet.PlainStringValue projectNameText = this.createStringValue( this.createKey( "projectNameText" ) );
	private final N potentialNameChanger;

	private final edu.cmu.cs.dennisc.javax.swing.ColorCustomizer foregroundCustomizer = new edu.cmu.cs.dennisc.javax.swing.ColorCustomizer() {
		public java.awt.Color changeColorIfAppropriate( java.awt.Color defaultColor ) {
			return isRenameRequired() ? org.alice.ide.ast.type.merge.croquet.views.MemberViewUtilities.ACTION_MUST_BE_TAKEN_COLOR : defaultColor;
		}
	};

	private boolean isImportDesiredPre;
	private boolean isProjectDesiredPre;
	private String importNamePre;
	private String projectNamePre;

	public PotentialNameChangerHelpComposite( java.util.UUID migrationId, N potentialNameChanger ) {
		super( migrationId, org.lgna.croquet.Application.INHERIT_GROUP );
		this.potentialNameChanger = potentialNameChanger;
	}

	@Override
	protected String modifyLocalizedText( org.lgna.croquet.Element element, String localizedText ) {
		String rv = super.modifyLocalizedText( element, localizedText );
		if( rv != null ) {
			if( element == this.importNameText ) {
				rv = org.alice.ide.ast.type.merge.croquet.AddMembersPage.modifyFilenameLocalizedText( rv, this.potentialNameChanger.getUriForDescriptionPurposesOnly() );
			}
		}
		return rv;
	}

	public N getPotentialNameChanger() {
		return this.potentialNameChanger;
	}

	public edu.cmu.cs.dennisc.javax.swing.ColorCustomizer getForegroundCustomizer() {
		return this.foregroundCustomizer;
	}

	public org.lgna.croquet.PlainStringValue getHeader() {
		return this.header;
	}

	public org.lgna.croquet.PlainStringValue getImportNameText() {
		return this.importNameText;
	}

	public org.lgna.croquet.PlainStringValue getProjectNameText() {
		return this.projectNameText;
	}

	protected final boolean isRenameRequired() {
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
	protected void cancel( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		this.potentialNameChanger.getImportHub().getIsDesiredState().setValueTransactionlessly( this.isImportDesiredPre );
		this.potentialNameChanger.getProjectHub().getIsDesiredState().setValueTransactionlessly( this.isProjectDesiredPre );
		this.potentialNameChanger.getImportHub().getNameState().setValueTransactionlessly( this.importNamePre );
		this.potentialNameChanger.getProjectHub().getNameState().setValueTransactionlessly( this.projectNamePre );
		super.cancel( completionStep );
	}

	@Override
	protected final org.lgna.croquet.edits.Edit createEdit( org.lgna.croquet.history.CompletionStep completionStep ) {
		return null;
	}

}
