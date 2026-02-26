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

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Dimension;

/**
 * @author Dennis Cosgrove
 */
public interface Theme {
  Dimension EXTRA_SMALL_RECT_ICON_SIZE = new Dimension(24, 18);
  Dimension SMALL_RECT_ICON_SIZE = new Dimension(32, 24);
  Dimension MEDIUM_RECT_ICON_SIZE = new Dimension(40, 30);
  Dimension LARGE_RECT_ICON_SIZE = new Dimension(120, 90);

  Dimension EXTRA_SMALL_SQUARE_ICON_SIZE = new Dimension(16, 16);
  Dimension SMALL_SQUARE_ICON_SIZE = new Dimension(22, 22);
  Dimension MEDIUM_SQUARE_ICON_SIZE = new Dimension(32, 32);
  Dimension LARGE_SQUARE_ICON_SIZE = new Dimension(90, 90);

  int BLOCK_MARGINS_WIDTH = 2;
  int BLOCK_MARGINS_HEIGHT = 1;
  Border BLOCK_BORDER = BorderFactory.createEmptyBorder(BLOCK_MARGINS_HEIGHT, BLOCK_MARGINS_WIDTH, BLOCK_MARGINS_HEIGHT, BLOCK_MARGINS_WIDTH);

  Color getKnurlColorFor(Color backgroundColor);

  Color getColorFor(Class<? extends Node> cls);

  Color getColorFor(Node node);

  Color getCodeColor(Code code);

}
