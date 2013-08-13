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
package org.alice.stageide.gallerybrowser.uri.merge.views;

/**
 * @author Dennis Cosgrove
 */
public class MergeTypeView extends org.lgna.croquet.components.MigPanel {
	private static final edu.cmu.cs.dennisc.java.awt.font.TextAttribute[] HEADER_TEXT_ATTRIBUTES = { edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD };
	private static final String HEADER_CONSTRAINT = "gap 16, wrap";
	private static final String DECLARATION_CHECK_BOX_CONSTRAINT = "gap 32, wrap";

	public MergeTypeView( org.alice.stageide.gallerybrowser.uri.merge.ImportTypeComposite composite ) {
		super( composite );
		this.addComponent( new org.lgna.croquet.components.Label( org.alice.ide.common.TypeIcon.getInstance( composite.getDstType() ) ), "wrap" );
		java.util.List<org.alice.stageide.gallerybrowser.uri.merge.IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> isProcedureImportDesiredStates = composite.getIsProcedureImportDesiredStates();
		if( isProcedureImportDesiredStates.size() > 0 ) {
			this.addComponent( composite.getProceduresHeader().createLabel( HEADER_TEXT_ATTRIBUTES ), HEADER_CONSTRAINT );
			for( org.alice.stageide.gallerybrowser.uri.merge.IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod> isProcedureImportDesiredState : isProcedureImportDesiredStates ) {
				this.addComponent( isProcedureImportDesiredState.createCheckBox(), DECLARATION_CHECK_BOX_CONSTRAINT );
			}
		}
		java.util.List<org.alice.stageide.gallerybrowser.uri.merge.IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod>> isFunctionImportDesiredStates = composite.getIsFunctionImportDesiredStates();
		if( isFunctionImportDesiredStates.size() > 0 ) {
			this.addComponent( composite.getFunctionsHeader().createLabel( HEADER_TEXT_ATTRIBUTES ), HEADER_CONSTRAINT );
			for( org.alice.stageide.gallerybrowser.uri.merge.IsDeclarationImportDesiredState<org.lgna.project.ast.UserMethod> isFunctionImportDesiredState : isFunctionImportDesiredStates ) {
				this.addComponent( isFunctionImportDesiredState.createCheckBox(), DECLARATION_CHECK_BOX_CONSTRAINT );
			}
		}
		java.util.List<org.alice.stageide.gallerybrowser.uri.merge.IsDeclarationImportDesiredState<org.lgna.project.ast.UserField>> isFieldImportDesiredStates = composite.getIsFieldImportDesiredStates();
		if( isFunctionImportDesiredStates.size() > 0 ) {
			this.addComponent( composite.getFieldsHeader().createLabel( HEADER_TEXT_ATTRIBUTES ), HEADER_CONSTRAINT );
			for( org.alice.stageide.gallerybrowser.uri.merge.IsDeclarationImportDesiredState<org.lgna.project.ast.UserField> isFieldImportDesiredState : isFieldImportDesiredStates ) {
				this.addComponent( isFieldImportDesiredState.createCheckBox(), DECLARATION_CHECK_BOX_CONSTRAINT );
			}
		}
	}
}
