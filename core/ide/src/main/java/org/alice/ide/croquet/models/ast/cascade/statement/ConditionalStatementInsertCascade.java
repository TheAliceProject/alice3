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

package org.alice.ide.croquet.models.ast.cascade.statement;

import edu.cmu.cs.dennisc.java.util.Maps;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.Statement;

import java.util.Map;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class ConditionalStatementInsertCascade extends PotentiallyEnvelopingStatementInsertCascade {
  private static Map<BlockStatementIndexPair, ConditionalStatementInsertCascade> mapEnveloping = Maps.newHashMap();
  private static Map<BlockStatementIndexPair, ConditionalStatementInsertCascade> mapInsert = Maps.newHashMap();

  public static synchronized ConditionalStatementInsertCascade getInstance(BlockStatementIndexPair blockStatementIndexPair, boolean isEnveloping) {
    Map<BlockStatementIndexPair, ConditionalStatementInsertCascade> map = isEnveloping ? mapEnveloping : mapInsert;
    assert blockStatementIndexPair != null;
    ConditionalStatementInsertCascade rv = map.get(blockStatementIndexPair);
    if (rv == null) {
      rv = new ConditionalStatementInsertCascade(blockStatementIndexPair, isEnveloping);
      map.put(blockStatementIndexPair, rv);
    }
    return rv;
  }

  private ConditionalStatementInsertCascade(BlockStatementIndexPair blockStatementIndexPair, boolean isEnveloping) {
    super(UUID.fromString("52743dfb-d19c-455a-a723-0bd3d59b2326"), blockStatementIndexPair, isEnveloping, ConditionBlank.getInstance());
  }

  @Override
  protected final Statement createStatement(Expression... expressions) {
    return AstUtilities.createConditionalStatement(expressions[0]);
  }
}
