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

package org.alice.ide.statementfactory;

/**
 * @author dennisc
 */
public class LocalStatementCascade extends org.lgna.croquet.CascadeWithInternalBlank< org.lgna.project.ast.Statement > {
	private static edu.cmu.cs.dennisc.map.MapToMap< org.lgna.project.ast.UserLocal, org.alice.ide.ast.draganddrop.BlockStatementIndexPair, LocalStatementCascade > map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	public static synchronized LocalStatementCascade getInstance( org.lgna.project.ast.UserLocal local, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		LocalStatementCascade rv = map.get( local, blockStatementIndexPair );
		if( rv != null ) {
			//pass
		} else {
			rv = new LocalStatementCascade( local, blockStatementIndexPair );
			map.put( local, blockStatementIndexPair, rv );
		}
		return rv;
	}
	private final org.lgna.project.ast.UserLocal local;
	private final org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair;
	private LocalStatementCascade( org.lgna.project.ast.UserLocal local, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "0c4a6b56-3935-4e4b-a4e0-7828338d0f8c" ), org.lgna.project.ast.Statement.class );
		this.local = local;
		this.blockStatementIndexPair = blockStatementIndexPair;
	}
	@Override
	protected org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<org.lgna.project.ast.Statement>> createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.Statement>> completionStep, org.lgna.project.ast.Statement[] values ) {
		return new org.alice.ide.croquet.edits.ast.InsertStatementEdit( completionStep, this.blockStatementIndexPair, values[ 0 ] );
	}
	@Override
	protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.Statement> blankNode ) {
		rv.add( LocalAssignmentStatementFillIn.getInstance( this.local ) );
		org.lgna.project.ast.AbstractType<?,?,?> type = this.local.getValueType();
		if( type.isArray() ) {
			rv.add( LocalArrayAtIndexAssignmentStatementFillIn.getInstance( this.local ) );
		}
		return rv;
	}
}
