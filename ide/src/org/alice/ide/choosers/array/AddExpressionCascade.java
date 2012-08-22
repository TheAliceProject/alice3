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

package org.alice.ide.choosers.array;

/**
 * @author Dennis Cosgrove
 */
public class AddExpressionCascade extends org.lgna.croquet.Cascade< org.lgna.project.ast.Expression > {
	private static class InternalBlank extends org.alice.ide.croquet.models.cascade.ExpressionBlank {
		private static class SingletonHolder {
			private static InternalBlank instance = new InternalBlank();
		}
		public static InternalBlank getInstance() {
			return SingletonHolder.instance;
		}
		private InternalBlank() {
			super( java.util.UUID.fromString( "309a7961-22d1-4c64-b9bc-23c5aba8f0ff" ), Double.class );
		}
	}
	private static class SingletonHolder {
		private static AddExpressionCascade instance = new AddExpressionCascade();
	}
	public static AddExpressionCascade getInstance() {
		return SingletonHolder.instance;
	}
	private AddExpressionCascade() {
		super( 
				org.lgna.croquet.Application.INHERIT_GROUP, 
				java.util.UUID.fromString( "031117ba-2e27-4323-beff-e3c1e6d83a6c" ), 
				org.lgna.project.ast.Expression.class, 
				InternalBlank.getInstance()
		);
	}
	@Override
	protected org.lgna.croquet.edits.Edit< ? extends org.lgna.croquet.Cascade< org.lgna.project.ast.Expression >> createEdit( org.lgna.croquet.history.CompletionStep< org.lgna.croquet.Cascade< org.lgna.project.ast.Expression > > completionStep, org.lgna.project.ast.Expression[] values ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( values[ 0 ] );
		ExpressionListSelectionState.getInstance().addItem( values[ 0 ] );
		return null;
	}
}
