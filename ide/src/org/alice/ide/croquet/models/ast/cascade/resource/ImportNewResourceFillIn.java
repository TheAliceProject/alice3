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

package org.alice.ide.croquet.models.ast.cascade.resource;

/**
 * @author Dennis Cosgrove
 */
public abstract class ImportNewResourceFillIn< E extends org.alice.virtualmachine.Resource > extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithoutBlanks< edu.cmu.cs.dennisc.alice.ast.ResourceExpression > {
	protected abstract Class< E > getResourceClass();
	protected abstract org.alice.ide.resource.prompter.ResourcePrompter<E> getResourcePrompter();
	protected abstract String getMenuText();
	public ImportNewResourceFillIn( java.util.UUID id ) {
		super( id );
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ResourceExpression createValue(org.lgna.croquet.history.CascadeFillInPrepStep<edu.cmu.cs.dennisc.alice.ast.ResourceExpression,Void> context) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		try {
			E resource = getResourcePrompter().promptUserForResource( ide.getFrame() );
			if( resource != null ) {
				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
				if( project != null ) {
					project.addResource( resource );
				}
				return new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( getResourceClass(), resource );
			} else {
				throw new org.lgna.croquet.CancelException();
			}
		} catch( java.io.IOException ioe ) {
			//todo
			throw new org.lgna.croquet.CancelException();
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.ResourceExpression getTransientValue( org.lgna.croquet.history.CascadeFillInPrepStep< edu.cmu.cs.dennisc.alice.ast.ResourceExpression, Void > context ) {
		return null;
	}
//	@Override
//	protected void addChildren() {
//	}
//	@Override
//	protected javax.swing.JComponent createMenuProxy() {
//		return edu.cmu.cs.dennisc.javax.swing.LabelUtilities.createLabel( this.getMenuText() );
//	}
}
