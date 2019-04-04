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
package org.alice.ide.javacode.croquet.views;

import de.java2html.converter.JavaSource2HTMLConverter;
import de.java2html.javasource.JavaSource;
import de.java2html.javasource.JavaSourceParser;
import de.java2html.javasource.JavaSourceType;
import de.java2html.options.JavaSourceConversionOptions;
import de.java2html.options.JavaSourceStyleEntry;
import de.java2html.options.JavaSourceStyleTable;
import de.java2html.util.RGB;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import edu.cmu.cs.dennisc.javax.swing.text.IgnoreAdjustVisibilityCaret;
import org.alice.ide.IDE;
import org.alice.ide.ProjectDocument;
import org.lgna.croquet.undo.UndoHistory;
import org.lgna.croquet.undo.event.HistoryClearEvent;
import org.lgna.croquet.undo.event.HistoryInsertionIndexEvent;
import org.lgna.croquet.undo.event.HistoryListener;
import org.lgna.croquet.undo.event.HistoryPushEvent;
import org.lgna.croquet.views.HtmlView;
import org.lgna.project.ast.AbstractDeclaration;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.ast.NamedUserType;
import org.lgna.project.ast.UserConstructor;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.code.CodeFormatter;
import org.lgna.story.ast.JavaCodeUtilities;

import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.IOException;
import java.io.StringWriter;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeView extends HtmlView {
  private static final boolean IS_LAMBDA_TOGGLE_ENABLED = true;
  private static final boolean IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED = false;

  public JavaCodeView() {
    this(null);
  }

  public JavaCodeView(AbstractDeclaration declaration) {
    this.setDeclaration(declaration);
    this.setCaret(new IgnoreAdjustVisibilityCaret()); // avoid scrolling on setText
  }

  public AbstractDeclaration getDeclaration() {
    return this.declaration;
  }

  public void setDeclaration(AbstractDeclaration declaration) {
    this.declaration = declaration;
    this.updateHtml();
  }

  private static UndoHistory getProjectUndoHistory() {
    IDE ide = IDE.getActiveInstance();
    if (ide != null) {
      ProjectDocument document = ide.getDocumentFrame().getDocument();
      if (document != null) {
        return document.getUndoHistory(IDE.PROJECT_GROUP);
      }
    }
    return null;
  }

  @Override
  protected void handleDisplayable() {
    this.undoHistory = getProjectUndoHistory();
    if (this.undoHistory != null) {
      this.undoHistory.addHistoryListener(this.historyListener);
    }
    this.addKeyListener(this.keyListener);
    if (IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED) {
      this.addMouseWheelListener(this.mouseWheelListener);
    }
    this.updateHtml();
    super.handleDisplayable();
  }

  @Override
  protected void handleUndisplayable() {
    if (IS_MOUSE_WHEEL_FONT_ADJUSTMENT_DESIRED) {
      this.removeMouseWheelListener(this.mouseWheelListener);
    }
    this.removeKeyListener(this.keyListener);
    if (this.undoHistory != null) {
      this.undoHistory.removeHistoryListener(this.historyListener);
    }
    super.handleUndisplayable();
  }

  @Override
  protected boolean isRightToLeftComponentOrientationAllowed(boolean defaultValue) {
    return false;
  }

  private void updateHtml() {
    //edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "updateHtml", this.declaration );
    JavaCodeGenerator.Builder javaCodeGeneratorBuilder = JavaCodeUtilities.createJavaCodeGeneratorBuilder();
    javaCodeGeneratorBuilder.isLambdaSupported(this.isLambdaSupported);
    JavaCodeGenerator javaCodeGenerator = javaCodeGeneratorBuilder.build();
    //org.lgna.project.ast.JavaCodeGenerator javaCodeGenerator = org.lgna.story.ast.JavaCodeUtilities.createJavaCodeGenerator();
    String code;
    if (this.declaration instanceof UserMethod) {
      UserMethod method = (UserMethod) this.declaration;
      code = method.generateCode(javaCodeGenerator);
    } else if (this.declaration instanceof UserConstructor) {
      UserConstructor constructor = (UserConstructor) this.declaration;
      code = constructor.generateCode(javaCodeGenerator);
    } else if (this.declaration instanceof NamedUserType) {
      NamedUserType type = (NamedUserType) this.declaration;
      code = type.generateCode(javaCodeGenerator);
    } else {
      code = null;
    }
    if (code != null) {
      code = CodeFormatter.format(code);
      JavaSourceStyleTable javaSourceStyleTable = JavaSourceStyleTable.getDefault().getClone();

      RGB keywordRGB = new RGB(0, 0, 230);
      RGB commentRGB = new RGB(150, 150, 150);
      RGB stringRGB = new RGB(206, 123, 0);
      RGB blackRGB = new RGB(0, 0, 0);

      javaSourceStyleTable.put(JavaSourceType.KEYWORD, new JavaSourceStyleEntry(keywordRGB));
      javaSourceStyleTable.put(JavaSourceType.CODE_TYPE, new JavaSourceStyleEntry(keywordRGB));
      javaSourceStyleTable.put(JavaSourceType.STRING, new JavaSourceStyleEntry(stringRGB));
      javaSourceStyleTable.put(JavaSourceType.CHAR_CONSTANT, new JavaSourceStyleEntry(stringRGB));
      javaSourceStyleTable.put(JavaSourceType.COMMENT_LINE, new JavaSourceStyleEntry(commentRGB));
      javaSourceStyleTable.put(JavaSourceType.COMMENT_BLOCK, new JavaSourceStyleEntry(commentRGB));
      javaSourceStyleTable.put(JavaSourceType.NUM_CONSTANT, new JavaSourceStyleEntry(blackRGB));

      JavaSourceConversionOptions javaSourceConversionOptions = JavaSourceConversionOptions.getDefault().getClone();
      javaSourceConversionOptions.setStyleTable(javaSourceStyleTable);
      final boolean IS_TAB_SIZE_WORKING = false;
      if (IS_TAB_SIZE_WORKING) {
        javaSourceConversionOptions.setTabSize(4);
      } else {
        code = code.replaceAll("\t", "    ");
      }
      //javaSourceConversionOptions.setShowLineNumbers( true );
      JavaSource javaSource = new JavaSourceParser().parse(code);
      JavaSource2HTMLConverter javaSource2HTMLConverter = new JavaSource2HTMLConverter();
      StringWriter stringWriter = new StringWriter();
      stringWriter.write("<html><head><style type=\"text/css\">code { font-size:" + this.fontSize + "px; }</style></head><body>");
      try {
        javaSource2HTMLConverter.convert(javaSource, javaSourceConversionOptions, stringWriter);
        stringWriter.write("</body></html>");
        stringWriter.flush();
        code = stringWriter.toString();
      } catch (IOException ioe) {
        code = "";
      }
    } else {
      code = "";
    }
    this.setText(code);
  }

  private final HistoryListener historyListener = new HistoryListener() {
    @Override
    public void clearing(HistoryClearEvent e) {
    }

    @Override
    public void cleared(HistoryClearEvent e) {
    }

    @Override
    public void insertionIndexChanging(HistoryInsertionIndexEvent e) {
    }

    @Override
    public void insertionIndexChanged(HistoryInsertionIndexEvent e) {
      //check not necessary
      //if( e.getTypedSource().getGroup() == org.alice.ide.IDE.PROJECT_GROUP ) {
      updateHtml();
      //}
    }

    @Override
    public void operationPushed(HistoryPushEvent e) {
    }

    @Override
    public void operationPushing(HistoryPushEvent e) {
    }
  };

  private final MouseWheelListener mouseWheelListener = new MouseWheelListener() {
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
      Logger.outln(e);
      if (e.isControlDown()) {
        fontSize += e.getWheelRotation();
        updateHtml();
      } else {
        Component src = e.getComponent();
        Container parent = src.getParent();
        parent.dispatchEvent(SwingUtilities.convertMouseEvent(src, e, parent));
      }
    }
  };
  private final KeyListener keyListener = new KeyListener() {
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();
      switch (keyCode) {
      case KeyEvent.VK_EQUALS: //plus
        fontSize++;
        updateHtml();
        break;
      case KeyEvent.VK_MINUS: //minus
        fontSize--;
        updateHtml();
        break;
      case KeyEvent.VK_SPACE:
        if (IS_LAMBDA_TOGGLE_ENABLED) {
          if (e.isControlDown() && e.isShiftDown()) {
            isLambdaSupported = !isLambdaSupported;
            updateHtml();
          }
        }
        break;
      }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
  };

  private UndoHistory undoHistory;
  private AbstractDeclaration declaration;
  private int fontSize = UIManagerUtilities.getDefaultFontSize();

  private boolean isLambdaSupported = true;
}
