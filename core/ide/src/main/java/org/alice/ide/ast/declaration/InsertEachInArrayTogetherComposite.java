/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.ast.declaration;

/**
 * @author Dennis Cosgrove
 */
public final class InsertEachInArrayTogetherComposite extends InsertEachInArrayComposite<org.lgna.project.ast.EachInArrayTogether> {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.alice.ide.ast.draganddrop.BlockStatementIndexPair, InsertEachInArrayTogetherComposite> mapEnveloping = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.alice.ide.ast.draganddrop.BlockStatementIndexPair, InsertEachInArrayTogetherComposite> mapInsert = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static synchronized InsertEachInArrayTogetherComposite getInstance( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair, final boolean isEnveloping ) {
		edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.alice.ide.ast.draganddrop.BlockStatementIndexPair, InsertEachInArrayTogetherComposite> map = isEnveloping ? mapEnveloping : mapInsert;
		return map.getInitializingIfAbsent( blockStatementIndexPair, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.alice.ide.ast.draganddrop.BlockStatementIndexPair, InsertEachInArrayTogetherComposite>() {
			@Override
			public InsertEachInArrayTogetherComposite initialize( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
				return new InsertEachInArrayTogetherComposite( blockStatementIndexPair, isEnveloping );
			}
		} );
	}

	private InsertEachInArrayTogetherComposite( org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair, boolean isEnveloping ) {
		super( java.util.UUID.fromString( "314ebbd9-b810-49aa-9832-39825d54082a" ), blockStatementIndexPair, isEnveloping );
	}

	@Override
	protected org.lgna.project.ast.EachInArrayTogether createStatement( org.lgna.project.ast.UserLocal item, org.lgna.project.ast.Expression initializer ) {
		return new org.lgna.project.ast.EachInArrayTogether(
				item,
				initializer,
				new org.lgna.project.ast.BlockStatement() );
	}
}
