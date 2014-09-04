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
package org.lgna.ik.poser.croquet;

/**
 * @author Matt May
 */
public class AddUnmanagedPoseFieldComposite extends org.alice.ide.ast.declaration.AddFieldComposite {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.NamedUserType, AddUnmanagedPoseFieldComposite> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static AddUnmanagedPoseFieldComposite getInstance( org.lgna.project.ast.NamedUserType declaringType ) {
		return map.getInitializingIfAbsent( declaringType, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.NamedUserType, AddUnmanagedPoseFieldComposite>() {
			@Override
			public AddUnmanagedPoseFieldComposite initialize( org.lgna.project.ast.NamedUserType declaringType ) {
				return new AddUnmanagedPoseFieldComposite( declaringType );
			}
		} );
	}

	private AddUnmanagedPoseFieldComposite( org.lgna.project.ast.NamedUserType declaringType ) {
		super(
				java.util.UUID.fromString( "882dc293-d176-48c6-9b42-abc15c734779" ),
				new FieldDetailsBuilder()
						.isFinal( ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, true )
						.valueComponentType( ApplicabilityStatus.DISPLAYED, org.lgna.project.ast.JavaType.getInstance( org.lgna.story.Pose.class ) )
						.valueIsArrayType( ApplicabilityStatus.APPLICABLE_BUT_NOT_DISPLAYED, false )
						.initializer( ApplicabilityStatus.EDITABLE, null )
						.build() );
		this.declaringType = declaringType;
	}

	@Override
	protected boolean isNullAllowedForInitializer() {
		return org.alice.ide.croquet.models.ui.preferences.IsNullAllowedForFieldInitializers.getInstance().getValue();
	}

	@Override
	public org.lgna.project.ast.NamedUserType getDeclaringType() {
		return this.declaringType;
	}

	@Override
	protected org.lgna.project.ast.Expression getInitializerInitialValue() {
		return this.initializerInitialValue;
	}

	public void setInitializerInitialValue( org.lgna.project.ast.Expression initializerInitialValue ) {
		this.initializerInitialValue = initializerInitialValue;
	}

	@Override
	protected boolean isFieldFinal() {
		return this.getIsFinalState().getValue();
	}

	@Override
	protected org.lgna.project.ast.ManagementLevel getManagementLevel() {
		return org.lgna.project.ast.ManagementLevel.NONE;
	}

	@Override
	protected org.alice.ide.croquet.edits.ast.DeclareFieldEdit createEdit( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field ) {
		return new org.alice.ide.croquet.edits.ast.DeclareNonGalleryFieldEdit( step, declaringType, field );
	}

	@Override
	protected org.alice.ide.ast.declaration.views.AddFieldView createView() {
		return new org.alice.ide.ast.declaration.views.AddFieldView( this );
	}

	private final org.lgna.project.ast.NamedUserType declaringType;
	private org.lgna.project.ast.Expression initializerInitialValue;
}
