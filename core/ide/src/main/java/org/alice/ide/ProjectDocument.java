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

package org.alice.ide;

import org.alice.ide.type.TypeCache;
import org.lgna.croquet.Document;
import org.lgna.croquet.Group;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.undo.UndoHistory;
import org.lgna.project.Project;

/**
 * @author Dennis Cosgrove
 */
public class ProjectDocument implements Document {

  private final Project project;
  private final UserActivity userActivity;
  private final ProjectHistoryManager projectHistoryManager;

  private final TypeCache typeCache;

  public ProjectDocument(Project project, UserActivity rootActivity) {
    this.project = project;
    userActivity = rootActivity;
    this.projectHistoryManager = new ProjectHistoryManager(this);

    // TODO: Store user activity as part of the profile file
    //this.putValueFor( UserActivity.INTERACTION_HISTORY_PROPERTY_KEY, this.userActivity );

    this.typeCache = new TypeCache(this.project);
  }

  public Project getProject() {
    return this.project;
  }

  public TypeCache getTypeCache() {
    return this.typeCache;
  }

  @Override
  public UserActivity getUserActivity() {
    return userActivity;
  }

  @Override
  public UndoHistory getUndoHistory(Group group) {
    return this.projectHistoryManager.getGroupHistory(group);
  }
}
