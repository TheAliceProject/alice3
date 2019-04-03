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
package edu.cmu.cs.dennisc.javax.sound;

import edu.cmu.cs.dennisc.java.io.InputStreamUtilities;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Dennis Cosgrove
 */
public class SoundUtilities {
  public static Clip createOpenedClip(InputStream inputStream) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
    Clip clip = AudioSystem.getClip();
    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(inputStream);
    clip.open(audioInputStream);
    return clip;
  }

  public static Clip createOpenedClip(byte[] array) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
    return createOpenedClip(new ByteArrayInputStream(array));
  }

  public static Clip createOpenedClip(Class<?> cls, String resourceName) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
    InputStream is = cls.getResourceAsStream(resourceName);
    byte[] buffer = InputStreamUtilities.getBytes(is);
    return createOpenedClip(buffer);
  }

  public static Clip createOpenedClip(File file) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
    return createOpenedClip(new FileInputStream(file));
  }

  public static Clip createOpenedClip(String path) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
    return createOpenedClip(new File(path));
  }

  public static void playAndDrainClip(Clip clip) {
    clip.setFramePosition(0);
    clip.start();
    clip.drain();
  }
}
