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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public class InternalCascadingItemOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
	private static edu.cmu.cs.dennisc.map.MapToMap< edu.cmu.cs.dennisc.croquet.Group, java.util.UUID, InternalCascadingItemOperation > mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static synchronized InternalCascadingItemOperation getInstance( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID id ) {
		InternalCascadingItemOperation rv = mapToMap.get( group, id );
		if( rv != null ) {
			//pass
		} else {
			rv = new InternalCascadingItemOperation( group, id );
			mapToMap.put( group, id, rv );
		}
		return rv;
	}

	private java.util.UUID id;
	private InternalCascadingItemOperation( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID id ) {
		super( group, java.util.UUID.fromString( "98e30a01-242f-4f3c-852c-d0b0a33d277f" ) );
		this.id = id;
	}
	public FillIn< ? > getFillIn() {
		return Node.lookup( this.id );
	}
	
	@Override
	public String getTutorialNoteText( edu.cmu.cs.dennisc.croquet.ModelContext< ? > context, edu.cmu.cs.dennisc.croquet.UserInformation userInformation ) {
		StringBuilder sb = new StringBuilder();
		FillIn< ? > fillIn = this.getFillIn();
		if( fillIn != null ) {
			fillIn.appendTutorialNoteText( sb, java.util.Locale.getDefault() );
			if( sb.length() > 0 ) {
				return sb.toString();
			} else {
				return fillIn.toString();
			}
		} else {
			return "unknown fill in: " + this;
		}
	}
	@Override
	protected org.alice.ide.croquet.resolvers.InternalCascadingItemOperationStaticGetInstanceKeyedResolver createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.InternalCascadingItemOperationStaticGetInstanceKeyedResolver( this );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Edit< ? > createTutorialCompletionEdit( edu.cmu.cs.dennisc.croquet.Edit< ? > originalEdit, edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		Blank rootBlank = this.getFillIn().getRootBlank();
		CascadingRoot cascadingRoot = rootBlank.getCascadingRoot();
		return cascadingRoot.createTutorialCompletionEdit( originalEdit, retargeter );
	}
	@Override
	protected final void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		this.getFillIn().handleActionOperationPerformed( context );
		Blank rootBlank = this.getFillIn().getRootBlank();
		CascadingRoot cascadingRoot = rootBlank.getCascadingRoot();
		try {
			Object value = rootBlank.getSelectedFillIn().getValue();
			edu.cmu.cs.dennisc.croquet.Edit< ? extends edu.cmu.cs.dennisc.croquet.ActionOperation > edit = cascadingRoot.createEdit( value, context );
			context.commitAndInvokeDo( edit );
		} catch( CancelException ce ) {
			context.cancel();
		}
	}
	@Override
	protected StringBuilder appendRepr( StringBuilder rv ) {
		super.appendRepr( rv );
		rv.append( "[" );
		rv.append( this.id );
		rv.append( "]" );
		return rv;
	}
}
