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
package org.alice.ide.video.preview;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.lang.ThreadUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities;
import edu.cmu.cs.dennisc.video.VideoPlayer;
import edu.cmu.cs.dennisc.video.vlcj.VlcjUtilities;
import edu.cmu.cs.dennisc.video.vlcj.VlcjVideoPlayer;
import org.alice.ide.video.preview.views.VideoView;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.DocumentFrame;
import org.lgna.croquet.Operation;
import org.lgna.croquet.SimpleComposite;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.simple.SimpleApplication;
import org.lgna.croquet.views.Frame;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class VideoComposite extends SimpleComposite<VideoView> {
  private final Operation togglePlayPauseOperation = this.createActionOperation("togglePlayPauseOperation", new Action() {
    @Override
    public Edit perform(UserActivity userActivity, InternalActionOperation source) throws CancelException {
      if (getView().isErrorFreeSinceLastPrepareMedia()) {
        VideoPlayer videoPlayer = getView().getVideoPlayer();
        if (videoPlayer.isPlaying()) {
          videoPlayer.pause();
        } else {
          videoPlayer.playResume();
        }
      } else {
        Logger.severe();
      }
      return null;
    }
  });

  public VideoComposite() {
    super(UUID.fromString("ffa047e2-9bce-4a46-8a16-70c19ced4d00"));
  }

  public Operation getTogglePlayPauseOperation() {
    return this.togglePlayPauseOperation;
  }

  @Override
  protected VideoView createView() {
    return new VideoView(this);
  }

  public static void main(String[] args) throws Exception {
    UIManagerUtilities.setLookAndFeel("Nimbus");
    final URL SECURE_URL = new URL("https://lookingglass.wustl.edu/videos/projects/483.webm?1382676625");
    final boolean IS_SIMPLE_TEST_DESIRED = false;
    if (IS_SIMPLE_TEST_DESIRED) {
      final boolean IS_APPLICATION_ROOT_DESIRED = true;
      VideoPlayer videoPlayer;
      if (IS_APPLICATION_ROOT_DESIRED) {
        videoPlayer = VlcjUtilities.createVideoPlayer();
      } else {
        final boolean IS_NATIVE_DISCOVERY_DESIRED = false;
        if (IS_NATIVE_DISCOVERY_DESIRED) {
          NativeDiscovery nativeDiscovery = new NativeDiscovery();
          if (nativeDiscovery.discover()) {
            //pass
          } else {
            System.err.println("vlcj native discovery failed");
          }
        } else {
          if (SystemUtilities.isWindows()) {
            if (SystemUtilities.is64Bit()) {
              NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), "C:/Program Files/VideoLAN/VLC");
            } else {
              throw new RuntimeException();
            }
          } else {
            throw new RuntimeException();
          }
        }
        Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(), LibVlc.class);
        videoPlayer = new VlcjVideoPlayer();
      }

      JFrame frame = new JFrame("vlcj test");
      frame.setLocation(100, 100);
      frame.setSize(1050, 600);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(videoPlayer.getVideoSurface(), BorderLayout.CENTER);
      frame.setVisible(true);

      videoPlayer.prepareMedia(SECURE_URL.toURI());
      videoPlayer.playResume();
    } else {
      final URI uriA;
      final URI uriB;
      if (args.length > 0) {
        uriA = new URI(args[0]);
        uriB = new URI(args[1]);
      } else {
        File directory = FileUtilities.getDefaultDirectory();
        if (SystemUtilities.isWindows()) {
          directory = directory.getParentFile();
        }
        File fileB = new File(directory, "Videos/b.webm");
        final boolean IS_SECURE_URL_TEST_DESIRED = true;
        if (IS_SECURE_URL_TEST_DESIRED) {
          uriA = SECURE_URL.toURI();
        } else {
          File fileA = new File(directory, "Videos/c.webm");
          uriA = fileA.toURI();
        }
        uriB = fileB.toURI();
      }
      SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
          SimpleApplication app = new SimpleApplication();
          DocumentFrame documentFrame = app.getDocumentFrame();
          Frame frame = documentFrame.getFrame();

          final VideoComposite videoComposite = new VideoComposite();
          videoComposite.getView().setUri(uriA);
          frame.setMainComposite(videoComposite);

          javax.swing.Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
              videoComposite.getView().setUri(uriB);
            }
          };
          action.putValue(javax.swing.Action.NAME, "set second video");
          frame.getMainComposite().getView().getAwtComponent().add(new JButton(action), BorderLayout.PAGE_START);

          frame.pack();
          frame.setVisible(true);

          final boolean IS_SNAPSHOT_TEST = false;
          if (IS_SNAPSHOT_TEST) {
            new Thread() {
              @Override
              public void run() {
                float tPrev = Float.NaN;
                while (true) {
                  float tCurr = videoComposite.getView().getVideoPlayer().getPosition();
                  if (tCurr != tPrev) {
                    Logger.outln(tCurr);
                    tPrev = tCurr;
                  }
                  ThreadUtilities.sleep(1);
                }
              }
            }.start();
          }
        }
      });
    }
  }
}
