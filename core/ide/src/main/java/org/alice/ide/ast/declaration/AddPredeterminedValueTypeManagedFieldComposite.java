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
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddPredeterminedValueTypeManagedFieldComposite extends AddManagedFieldComposite {
	private final org.lgna.project.ast.JavaType javaValueType;
	private org.lgna.project.ast.AbstractType<?, ?, ?> type;

	public AddPredeterminedValueTypeManagedFieldComposite( java.util.UUID migrationId, org.lgna.project.ast.JavaType javaValueType ) {
		super( migrationId, new FieldDetailsBuilder()
				.valueComponentType( ApplicabilityStatus.DISPLAYED, null )
				.valueIsArrayType( ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, false )
				.initializer( ApplicabilityStatus.DISPLAYED, null )
				.build() );
		this.javaValueType = javaValueType;
		org.lgna.croquet.icon.IconFactory iconFactory = org.alice.stageide.icons.IconFactoryManager.getIconFactoryForType( this.javaValueType );
		if( ( iconFactory != null ) && ( iconFactory != org.lgna.croquet.icon.EmptyIconFactory.getInstance() ) ) {
			this.getLaunchOperation().setButtonIcon( iconFactory.getIcon( org.lgna.croquet.icon.IconSize.SMALL.getSize() ) );
		}
	}

	public AddPredeterminedValueTypeManagedFieldComposite( java.util.UUID migrationId, Class<?> valueCls ) {
		this( migrationId, org.lgna.project.ast.JavaType.getInstance( valueCls ) );
	}

	protected boolean isUserTypeDesired() {
		return true;
	}

	@Override
	protected void handlePreShowDialog( org.lgna.croquet.history.CompletionStep<?> step ) {
		if( this.isUserTypeDesired() ) {
			this.type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromSuperType( this.javaValueType );
		} else {
			this.type = this.javaValueType;
		}
		super.handlePreShowDialog( step );
	}

	@Override
	protected void handlePostHideDialog( org.lgna.croquet.history.CompletionStep<?> completionStep ) {
		super.handlePostHideDialog( completionStep );
		this.type = null;
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getValueComponentTypeInitialValue() {
		return this.type;
	}

	@Override
	protected org.lgna.project.ast.Expression getInitializerInitialValue() {
		return org.lgna.project.ast.AstUtilities.createInstanceCreation( this.type );
	}
}
