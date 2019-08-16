/*******************************************************************************
 * Copyright (c) 2006, 2015, 2019 Carnegie Mellon University. All rights reserved.
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
package org.alice.ide.croquet.models.print;

import edu.cmu.cs.dennisc.java.awt.print.PageFormatUtilities;
import org.alice.ide.IDE;
import org.alice.ide.operations.InconsequentialActionOperation;
import org.alice.ide.sceneeditor.AbstractSceneEditor;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.UUID;

public class PrintSceneEditorOperation extends InconsequentialActionOperation {
 public PrintSceneEditorOperation() {
    super(UUID.fromString("b38997ea-e970-416e-86db-58623d1c3352"));
  }

  @Override
  protected final void performInternal() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(getPrintable());
    PrintRequestAttributeSet printOptions = new HashPrintRequestAttributeSet();
    printOptions.add(DialogTypeSelection.NATIVE);
    if (job.printDialog(printOptions)) {
      try {
        job.print(printOptions);
      } catch (PrinterException pe) {
        pe.printStackTrace();
      }
    }
  }

  private Printable getPrintable() {
    return (g, pageFormat, pageIndex) -> {
      if (pageIndex <= 0) {
        AbstractSceneEditor sceneEditor = IDE.getActiveInstance().getSceneEditor();
        if (sceneEditor != null) {
          Graphics2D g2 = (Graphics2D) g;
          int width = sceneEditor.getWidth();
          int height = sceneEditor.getHeight();
          double scale = PageFormatUtilities.calculateScale(pageFormat, width, height);
          g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
          if (scale > 0.0) {
            g2.scale(1.0 / scale, 1.0 / scale);
          }
          sceneEditor.getAwtComponent().paintAll(g2);
          return Printable.PAGE_EXISTS;
        }
      }
      return Printable.NO_SUCH_PAGE;
    };
  }
}
