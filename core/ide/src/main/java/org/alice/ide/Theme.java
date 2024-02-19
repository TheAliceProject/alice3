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

import org.lgna.project.ast.Code;
import org.lgna.project.ast.Node;
import org.lgna.project.ast.Statement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;

/**
 * @author Dennis Cosgrove
 */
public interface Theme {
  public static final Dimension EXTRA_SMALL_RECT_ICON_SIZE = new Dimension(24, 18);
  public static final Dimension SMALL_RECT_ICON_SIZE = new Dimension(32, 24);
  public static final Dimension MEDIUM_RECT_ICON_SIZE = new Dimension(40, 30);
  public static final Dimension LARGE_RECT_ICON_SIZE = new Dimension(120, 90);

  public static final Dimension EXTRA_SMALL_SQUARE_ICON_SIZE = new Dimension(16, 16);
  public static final Dimension SMALL_SQUARE_ICON_SIZE = new Dimension(22, 22);
  public static final Dimension MEDIUM_SQUARE_ICON_SIZE = new Dimension(32, 32);
  public static final Dimension LARGE_SQUARE_ICON_SIZE = new Dimension(90, 90);

  public Color getTypeColor();

  public Color getMutedTypeColor();

  public Color getProcedureColor();

  public Color getFunctionColor();

  public Color getConstructorColor();

  public Color getFieldColor();

  public Color getLocalColor();

  public Color getParameterColor();

  public Color getEventColor();

  public Color getEventBodyColor();

  public Paint getPaintFor(Class<? extends Statement> cls, int x, int y, int width, int height);

  public Color getColorFor(Class<? extends Node> cls);

  public Color getColorFor(Node node);

  public Color getCommentForegroundColor();

  public Color getCodeColor(Code code);

  public Color getSelectedColor();

  public Color getUnselectedColor();

  public Color getPrimaryBackgroundColor();

  public Color getSecondaryBackgroundColor();
}
