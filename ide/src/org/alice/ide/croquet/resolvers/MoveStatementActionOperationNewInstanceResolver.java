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

package org.alice.ide.croquet.resolvers;

/**
 * @author Dennis Cosgrove
 */
public class MoveStatementActionOperationNewInstanceResolver extends org.lgna.croquet.resolvers.NewInstanceKeyedResolver< org.alice.ide.croquet.models.ast.MoveStatementActionOperation > {
	public MoveStatementActionOperationNewInstanceResolver( org.alice.ide.croquet.models.ast.MoveStatementActionOperation instance ) {
		super( instance, org.alice.ide.croquet.models.ast.MoveStatementActionOperation.CONSTRUCTOR_PARAMETER_TYPES, instance.getArguments() );
	}
	public MoveStatementActionOperationNewInstanceResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}

	@Override
	protected Object[] decodeArguments( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		java.util.UUID fromBlockStatementId = binaryDecoder.decodeId();
		int fromIndex = binaryDecoder.decodeInt();
		java.util.UUID statementId = binaryDecoder.decodeId();
		java.util.UUID toBlockStatementId = binaryDecoder.decodeId();
		int toIndex = binaryDecoder.decodeInt();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
		org.lgna.project.ast.BlockStatement fromBlockStatement = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), fromBlockStatementId );
		org.lgna.project.ast.Statement statement = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), statementId );
		org.lgna.project.ast.BlockStatement toBlockStatement = org.lgna.project.ProgramTypeUtilities.lookupNode( ide.getProject(), toBlockStatementId );
		return new Object[] { fromBlockStatement, fromIndex, statement, toBlockStatement, toIndex };
	}
	@Override
	protected void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Object[] arguments ) {
		org.lgna.project.ast.BlockStatement fromBlockStatement = (org.lgna.project.ast.BlockStatement)arguments[ 0 ];
		int fromIndex = (Integer)arguments[ 1 ];
		org.lgna.project.ast.Statement statement = (org.lgna.project.ast.Statement)arguments[ 2 ];
		org.lgna.project.ast.BlockStatement toBlockStatement = (org.lgna.project.ast.BlockStatement)arguments[ 3 ];
		int toIndex = (Integer)arguments[ 4 ];

		binaryEncoder.encode( fromBlockStatement.getId() );
		binaryEncoder.encode( fromIndex );
		binaryEncoder.encode( statement.getId() );
		binaryEncoder.encode( toBlockStatement.getId() );
		binaryEncoder.encode( toIndex );
	}
}
