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
package org.alice.stageide.gallerybrowser.uri.merge.edits;

/**
 * @author Dennis Cosgrove
 */
public class ImportTypeEdit extends org.lgna.croquet.edits.Edit {
	private final java.net.URI uriForDescriptionPurposesOnly;
	private final org.lgna.project.ast.NamedUserType existingType;
	private final java.util.List<org.lgna.project.ast.UserMethod> methodsToCreate;
	private final java.util.List<org.lgna.project.ast.UserField> fieldsToCreate;
	private final java.util.List<edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserMethod, String, String>> methodsToRename;
	private final java.util.List<edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserField, String, String>> fieldsToRename;

	public ImportTypeEdit( org.lgna.croquet.history.CompletionStep completionStep, java.net.URI uriForDescriptionPurposesOnly, org.lgna.project.ast.NamedUserType existingType, java.util.List<org.lgna.project.ast.UserMethod> methodsToCreate, java.util.List<org.lgna.project.ast.UserField> fieldsToCreate, java.util.List<edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserMethod, String, String>> methodsToRename, java.util.List<edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserField, String, String>> fieldsToRename ) {
		super( completionStep );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.existingType = existingType;
		this.methodsToCreate = methodsToCreate;
		this.fieldsToCreate = fieldsToCreate;
		this.methodsToRename = methodsToRename;
		this.fieldsToRename = fieldsToRename;
	}

	public ImportTypeEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "decode", this );
		this.uriForDescriptionPurposesOnly = null;
		this.existingType = null;
		this.methodsToCreate = null;
		this.fieldsToCreate = null;
		this.methodsToRename = null;
		this.fieldsToRename = null;
	}

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "encode", this );
	}

	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		for( org.lgna.project.ast.UserMethod method : this.methodsToCreate ) {
			this.existingType.methods.add( method );
		}
		for( org.lgna.project.ast.UserField field : this.fieldsToCreate ) {
			this.existingType.fields.add( field );
		}

		for( edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserMethod, String, String> tuple : this.methodsToRename ) {
			tuple.setC( tuple.getA().getName() );
			tuple.getA().setName( tuple.getB() );
		}

		for( edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserField, String, String> tuple : this.fieldsToRename ) {
			tuple.setC( tuple.getA().getName() );
			tuple.getA().setName( tuple.getB() );
		}
	}

	@Override
	protected final void undoInternal() {
		for( org.lgna.project.ast.UserMethod method : this.methodsToCreate ) {
			this.existingType.methods.remove( this.existingType.methods.indexOf( method ) );
		}
		for( org.lgna.project.ast.UserField field : this.fieldsToCreate ) {
			this.existingType.fields.remove( this.existingType.methods.indexOf( field ) );
		}
		for( edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserMethod, String, String> tuple : this.methodsToRename ) {
			tuple.getA().setName( tuple.getC() );
		}

		for( edu.cmu.cs.dennisc.pattern.Tuple3<org.lgna.project.ast.UserField, String, String> tuple : this.fieldsToRename ) {
			tuple.getA().setName( tuple.getC() );
		}
	}

	@Override
	protected void appendDescription( StringBuilder rv, DescriptionStyle descriptionStyle ) {
		rv.append( "import " );
		rv.append( this.uriForDescriptionPurposesOnly );
	}
}
