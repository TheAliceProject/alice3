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
package org.alice.ide.i18n;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dennis Cosgrove
 */
public class Line {
  private static final String PREFIX = "</";
  private static final String POSTFIX = "/>";
  private static final Pattern TAG_PATTERN = Pattern.compile(PREFIX + "[A-Za-z_0-9()]*" + POSTFIX);

  private int indentCount;
  private Chunk[] array;
  private boolean isLoop;

  public Line(String s) {
    this.isLoop = "\t loop".equals(s);
    List<Chunk> chunks = new LinkedList<Chunk>();
    this.indentCount = 0;
    for (byte b : s.getBytes()) {
      if (b == '\t') {
        this.indentCount++;
      } else {
        break;
      }
    }
    Matcher matcher = TAG_PATTERN.matcher(s);
    int iEnd = this.indentCount;
    while (matcher.find()) {
      int iStart = matcher.start();
      if (iStart != iEnd) {
        chunks.add(new TextChunk(s.substring(iEnd, iStart)));
      }
      //      edu.cmu.cs.dennisc.print.PrintUtilities.println( s );
      //      edu.cmu.cs.dennisc.print.PrintUtilities.println( iStart );
      //      edu.cmu.cs.dennisc.print.PrintUtilities.println( iEnd );
      iEnd = matcher.end();
      String sub = s.substring(iStart + PREFIX.length(), iEnd - POSTFIX.length());
      if (sub.startsWith("_gets_toward_")) {
        boolean isTowardLeading = sub.equals("_gets_toward_leading_");
        chunks.add(new GetsChunk(isTowardLeading));
      } else {
        if (sub.endsWith("()")) {
          chunks.add(new MethodInvocationChunk(sub));
        } else {
          chunks.add(new PropertyChunk(sub));
        }
      }
    }
    if (iEnd < s.length()) {
      chunks.add(new TextChunk(s.substring(iEnd)));
    }
    this.array = new Chunk[chunks.size()];
    chunks.toArray(this.array);
  }

  public int getIndentCount() {
    return this.indentCount;
  }

  public Chunk[] getChunks() {
    return this.array;
  }

  @Deprecated
  public boolean isLoop() {
    return this.isLoop;
  }
}
