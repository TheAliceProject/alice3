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
package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class InsertStatementActionOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation implements org.alice.ide.croquet.models.BeholdenModel {
	private java.util.UUID instanceId;
	private edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement;
	private int index;
	private edu.cmu.cs.dennisc.alice.ast.Statement statement;
	
	private static java.util.Map< java.util.UUID, InsertStatementActionOperation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized InsertStatementActionOperation getInstance( java.util.UUID instanceId, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		InsertStatementActionOperation rv = map.get( instanceId );
		if( rv != null ) {
			//pass
		} else {
			rv = new InsertStatementActionOperation( instanceId, blockStatement, index, statement );
			map.put( instanceId, rv );
		}
		return rv;
	}
	private InsertStatementActionOperation( java.util.UUID instanceId, edu.cmu.cs.dennisc.alice.ast.BlockStatement blockStatement, int index, edu.cmu.cs.dennisc.alice.ast.Statement statement ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP, java.util.UUID.fromString( "a6aa2cea-f205-434a-8ec8-c068c9fb3b83" ) );
		this.instanceId = instanceId;
		this.blockStatement = blockStatement;
		this.index = index;
		this.statement = statement;
	}
	
	public java.util.UUID getInstanceId() {
		return this.instanceId;
	}
	public edu.cmu.cs.dennisc.alice.ast.BlockStatement getBlockStatement() {
		return this.blockStatement;
	}
	public int getIndex() {
		return this.index;
	}
	public edu.cmu.cs.dennisc.alice.ast.Statement getStatement() {
		return this.statement;
	}

	public void doOrRedoInternal( boolean isDo ) {
		this.blockStatement.statements.add( this.index, this.statement );
	}

	public void undoInternal() {
		if( this.blockStatement.statements.get( this.index ) == this.statement ) {
			this.blockStatement.statements.remove( this.index );
		} else {
			throw new javax.swing.undo.CannotUndoException();
		}
	}
	
	public void retarget( edu.cmu.cs.dennisc.croquet.Retargeter retargeter ) {
		this.blockStatement = retargeter.retarget( this.blockStatement );
		this.statement = retargeter.retarget( this.statement );
	}
	
	
	public StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
		//super.updatePresentation( rv, locale );
		rv.append( "drop: " );
		edu.cmu.cs.dennisc.alice.ast.NodeUtilities.safeAppendRepr( rv, this.statement, locale );
		return rv;
	}
	public boolean isReplacementAcceptable( edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		return edit instanceof org.alice.ide.croquet.edits.DependentEdit;
	}
	public void addKeyValuePairs( edu.cmu.cs.dennisc.croquet.Retargeter retargeter, edu.cmu.cs.dennisc.croquet.Edit< ? > edit ) {
		org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation> replacementEdit = (org.alice.ide.croquet.edits.DependentEdit<InsertStatementActionOperation>)edit;
		InsertStatementActionOperation replacementModel = replacementEdit.getModel();
		retargeter.addKeyValuePair( this, replacementModel );
		retargeter.addKeyValuePair( this.instanceId, replacementModel.instanceId );
		retargeter.addKeyValuePair( this.blockStatement, replacementModel.blockStatement );
		retargeter.addKeyValuePair( this.statement, replacementModel.statement );
		System.err.println( "TODO: recursive retarget" );
		if( this.statement instanceof edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody ) {
			retargeter.addKeyValuePair( ((edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)this.statement).body.getValue(), ((edu.cmu.cs.dennisc.alice.ast.AbstractStatementWithBody)replacementModel.statement).body.getValue() );
		}
	}
	
	@Override
	protected edu.cmu.cs.dennisc.croquet.CodableResolver< InsertStatementActionOperation > createCodableResolver() {
		return new org.alice.ide.croquet.resolvers.InsertStatementResolver( this );
	}
	@Override
	protected void perform( edu.cmu.cs.dennisc.croquet.ActionOperationContext context ) {
		//context.commitAndInvokeDo( new org.alice.ide.croquet.edits.ast.InsertStatementEdit( this.blockStatement, this.index, this.statement ) );
		context.commitAndInvokeDo( new org.alice.ide.croquet.edits.DependentEdit< InsertStatementActionOperation >() );
	}
}
