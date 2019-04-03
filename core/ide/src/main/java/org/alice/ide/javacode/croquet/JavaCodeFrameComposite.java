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
package org.alice.ide.javacode.croquet;

import org.alice.ide.IDE;
import org.alice.ide.MetaDeclarationFauxState;
import org.alice.ide.javacode.croquet.views.JavaCodeView;
import org.lgna.croquet.Application;
import org.lgna.croquet.FrameCompositeWithInternalIsShowingState;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.Panel;
import org.lgna.croquet.views.ScrollPane;
import org.lgna.project.ast.AbstractDeclaration;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeFrameComposite extends FrameCompositeWithInternalIsShowingState<Panel> {
  private static class SingletonHolder {
    private static JavaCodeFrameComposite instance = new JavaCodeFrameComposite();
  }

  public static JavaCodeFrameComposite getInstance() {
    return SingletonHolder.instance;
  }

  private JavaCodeFrameComposite() {
    super(UUID.fromString("7015e117-22dd-49a1-a194-55d5fe17f821"), Application.DOCUMENT_UI_GROUP);
  }

  @Override
  protected ScrollPane createScrollPaneIfDesired() {
    return new ScrollPane();
  }

  @Override
  protected Panel createView() {
    return new BorderPanel.Builder()
        //.center( this.stringValue.createImmutableEditorPane( 1.4f, edu.cmu.cs.dennisc.java.awt.font.TextFamily.MONOSPACED ) )
        .center(this.javaCodeView).build();
  }

  @Override
  protected Integer getWiderGoldenRatioSizeFromHeight() {
    return 400;
  }

  @Override
  public void handlePreActivation() {
    IDE.getActiveInstance().getDocumentFrame().getMetaDeclarationFauxState().addAndInvokeValueListener(this.declarationListener);
    super.handlePreActivation();
  }

  @Override
  public void handlePostDeactivation() {
    super.handlePostDeactivation();
    IDE.getActiveInstance().getDocumentFrame().getMetaDeclarationFauxState().removeValueListener(this.declarationListener);
  }

  private final MetaDeclarationFauxState.ValueListener declarationListener = new MetaDeclarationFauxState.ValueListener() {
    @Override
    public void changed(AbstractDeclaration prevValue, AbstractDeclaration nextValue) {
      javaCodeView.setDeclaration(nextValue);
    }
  };

  private final JavaCodeView javaCodeView = new JavaCodeView();

}
