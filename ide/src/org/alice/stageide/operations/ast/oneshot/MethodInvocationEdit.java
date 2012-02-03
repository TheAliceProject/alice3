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
package org.alice.stageide.operations.ast.oneshot;

/**
 * @author Dennis Cosgrove
 */
public abstract class MethodInvocationEdit extends org.lgna.croquet.edits.Edit {
	private final org.alice.ide.instancefactory.InstanceFactory instanceFactory;
	private final org.lgna.project.ast.AbstractMethod method;
	private final org.lgna.project.ast.Expression[] argumentExpressions;
	public MethodInvocationEdit( org.lgna.croquet.history.CompletionStep completionStep, org.alice.ide.instancefactory.InstanceFactory instanceFactory, org.lgna.project.ast.AbstractMethod method, org.lgna.project.ast.Expression[] argumentExpressions ) {
		super( completionStep );
		this.instanceFactory = instanceFactory;
		this.method = method;
		this.argumentExpressions = argumentExpressions;
	}
	public MethodInvocationEdit( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Object step ) {
		super( binaryDecoder, step );
		org.lgna.project.Project project = org.alice.ide.IDE.getActiveInstance().getProject();
		//todo: support retarget
		org.lgna.croquet.resolvers.Resolver< org.alice.ide.instancefactory.InstanceFactory > resolver = binaryDecoder.decodeBinaryEncodableAndDecodable();
		this.instanceFactory = resolver.getResolved();
		this.method = org.lgna.project.io.IoUtilities.decodeNode( project, binaryDecoder );
		this.argumentExpressions = null;
		assert false : this.argumentExpressions;
	}
	@Override
	public final void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		super.encode( binaryEncoder );
		binaryEncoder.encode( this.instanceFactory.getResolver() );
		org.lgna.project.io.IoUtilities.encodeNode( binaryEncoder, this.method );
		assert false : this.argumentExpressions;
	}
	protected abstract void preserveUndoInfo( Object instance, boolean isDo );
	@Override
	protected final void doOrRedoInternal( boolean isDo ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.alice.ide.sceneeditor.AbstractSceneEditor sceneEditor = ide.getSceneEditor();
		Object instance = sceneEditor.getInstanceInJavaVMForExpression( this.instanceFactory.createExpression() );
		this.preserveUndoInfo( instance, isDo );
		org.lgna.project.ast.Expression methodInvocation = org.lgna.project.ast.AstUtilities.createMethodInvocation( 
				this.instanceFactory.createExpression(), 
				method, 
				argumentExpressions 
		);
		org.lgna.project.ast.ExpressionStatement statement = new org.lgna.project.ast.ExpressionStatement( methodInvocation );
		sceneEditor.executeStatements( statement );
	}
	@Override
	protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		rv.append( "one shot " );
		rv.append( this.method );
		return rv;
	}
}
