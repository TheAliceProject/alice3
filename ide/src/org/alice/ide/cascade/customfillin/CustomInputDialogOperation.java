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
package org.alice.ide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public class CustomInputDialogOperation<E extends edu.cmu.cs.dennisc.alice.ast.Expression> extends org.alice.ide.operations.InputDialogWithPreviewOperation<CustomInputPane< E >> {
	@Deprecated
	public interface EPIC_HACK_Validator {
		public String getExplanationIfOkButtonShouldBeDisabled( org.alice.ide.choosers.ValueChooser< ? > valueChooser );
	}
	private EPIC_HACK_Validator validator = null;

	private CustomInputPane< E > customInputPane;
	public CustomInputDialogOperation( CustomInputPane< E > customInputPane ) {
		super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "0e69d792-3e5b-4a17-b670-465885ade615" ) );
		this.customInputPane = customInputPane;
	}
	public EPIC_HACK_Validator getValidator() {
		return this.validator;
	}
	public void setValidator( EPIC_HACK_Validator validator ) {
		this.validator = validator;
	}
	@Override
	protected String getExplanationIfOkButtonShouldBeDisabled(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<CustomInputPane< E >> context) {
		String rv = super.getExplanationIfOkButtonShouldBeDisabled( context );
		if( this.validator != null ) {
			String explanation = this.validator.getExplanationIfOkButtonShouldBeDisabled( this.customInputPane.getValueChooser() );
			if( explanation != null ) {
				rv = explanation;
			}
		}
		return rv;
	}
	
	@Override
	protected CustomInputPane< E > prologue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<CustomInputPane< E >> context) {
		return this.customInputPane;
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext<CustomInputPane< E >> context, boolean isOk) {
		if( isOk ) {
			context.finish();
		} else {
			context.cancel();
		}
	}
}
