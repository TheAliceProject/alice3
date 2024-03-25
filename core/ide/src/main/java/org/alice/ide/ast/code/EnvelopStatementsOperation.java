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
package org.alice.ide.ast.code;

import edu.cmu.cs.dennisc.map.MapToMap;
import org.alice.ide.ast.code.edits.EnvelopStatementsEdit;
import org.alice.ide.ast.draganddrop.BlockStatementIndexPair;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.history.UserActivity;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class EnvelopStatementsOperation extends ActionOperation {
  private static MapToMap<BlockStatementIndexPair, BlockStatementIndexPair, EnvelopStatementsOperation> map = MapToMap.newInstance();

  public static synchronized EnvelopStatementsOperation getInstance(BlockStatementIndexPair fromLocation, BlockStatementIndexPair toLocation) {
    EnvelopStatementsOperation rv = map.get(fromLocation, toLocation);
    if (rv == null) {
      rv = new EnvelopStatementsOperation(fromLocation, toLocation);
      map.put(fromLocation, toLocation, rv);
    }
    return rv;
  }

  private final BlockStatementIndexPair fromLocation;
  private final BlockStatementIndexPair toLocation;

  private EnvelopStatementsOperation(BlockStatementIndexPair fromLocation, BlockStatementIndexPair toLocation) {
    super(Application.PROJECT_GROUP, UUID.fromString("170a3707-f31d-4c59-89a8-844fab44a7c4"));
    this.fromLocation = fromLocation;
    this.toLocation = toLocation;
  }

  public BlockStatementIndexPair getFromLocation() {
    return this.fromLocation;
  }

  public BlockStatementIndexPair getToLocation() {
    return this.toLocation;
  }

  @Override
  protected void perform(UserActivity activity) {
    activity.commitAndInvokeDo(new EnvelopStatementsEdit(activity, this.fromLocation, this.toLocation));
  }

}
