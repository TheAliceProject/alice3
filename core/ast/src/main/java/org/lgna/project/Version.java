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
package org.lgna.project;

/**
 * @author Dennis Cosgrove
 */
public final class Version implements Comparable<Version> {
  private int[] subNumbers;
  private String prerelease;
  private String metadata;

  /**
   * Alice 3's semantic versioning effectively shifts Major and Minor over one place.
   * 3.Major.Minor.Patch{-prerelease}{+metadata}
   *
   * Following the 3 is the semantic version - https://semver.org/
   * This means prerelease and metadata are both optional.
   *
   * Some older versions of Alice have additional subpatch numbers
   *
   * @param text formatted to follow Alice's semantic versioning
   */
  public Version(String text) {
    String remainder = extractMetadata(text);
    remainder = extractPrerelease(remainder);
    extractSubNumbers(remainder);
  }

  private String extractPrerelease(String text) {
    String[] chunks = text.split("-");
    final int chunkCount = chunks.length;
    if (chunkCount > 1) {
      prerelease = chunks[1];
    }
    return chunks[0];
  }

  private String extractMetadata(String text) {
    String[] chunks = text.split("\\+");
    final int chunkCount = chunks.length;
    if (chunkCount > 1) {
      metadata = chunks[1];
    }
    return chunks[0];
  }

  private void extractSubNumbers(String text) {
    String[] subTexts = text.split("\\.");
    final int N = subTexts.length;
    this.subNumbers = new int[N];
    for (int i = 0; i < N; i++) {
      try {
        this.subNumbers[i] = Integer.parseInt(subTexts[i]);
      } catch (NumberFormatException nfe) {
        this.subNumbers[i] = -1;
      }
    }
  }

  public boolean isValid() {
    for (int i : this.subNumbers) {
      if (i < 0) {
        return false;
      }
    }
    return true;
  }

  // This will remain 3. It is as much branding as version.
  public int getAliceIdentifier() {
    return this.subNumbers[0];
  }

  public int getMajor() {
    return this.subNumbers[1];
  }

  public int getMinor() {
    return this.subNumbers[2];
  }

  public String getSimpleVersion() {
    String ver = getAliceIdentifier() + "." + getMajor();
    if (getMinor() != 0) {
      ver = ver + "." + getMinor();
    }
    if (hasPrerelease()) {
      ver = ver + '-' + prerelease;
    }
    return ver;
  }

  public int getPatch() {
    return this.subNumbers[3];
  }

  public boolean hasMetadata() {
    return metadata != null && metadata.length() > 0;
  }

  public boolean hasPrerelease() {
    return prerelease != null && prerelease.length() > 0;
  }

  private static int[] growIfNecessary(int[] source, int[] other) {
    if (source.length < other.length) {
      int[] rv = new int[other.length];
      System.arraycopy(source, 0, rv, 0, source.length);
      return rv;
    } else {
      return source;
    }
  }

  @Override
  public int compareTo(Version other) {
    int[] thisSubNumbers = growIfNecessary(this.subNumbers, other.subNumbers);
    int[] otherSubNumbers = growIfNecessary(other.subNumbers, this.subNumbers);
    for (int i = 0; i < thisSubNumbers.length; i++) {
      int result = Integer.signum(thisSubNumbers[i] - otherSubNumbers[i]);
      if (result != 0) {
        return result;
      }
    }
    // Version numbers match. Check prerelease. The one without is behind.
    if (this.hasPrerelease() && !other.hasPrerelease()) {
      return -1;
    }
    if (!this.hasPrerelease() && other.hasPrerelease()) {
      return 1;
    }
    // The metadata is not considered in ordering
    return 0;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    String separator = "";
    for (int subNumber : this.subNumbers) {
      sb.append(separator);
      sb.append(subNumber);
      separator = ".";
    }
    if (hasPrerelease()) {
      sb.append('-').append(prerelease);
    }
    if (hasMetadata()) {
      sb.append('+').append(metadata);
    }
    return sb.toString();
  }
}
