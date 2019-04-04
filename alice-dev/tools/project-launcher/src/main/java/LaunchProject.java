/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */

/**
 * @author Dennis Cosgrove
 */
public class LaunchProject {
  private static class LaunchProgramAction extends javax.swing.AbstractAction {
    public LaunchProgramAction(org.lgna.project.ast.NamedUserType programType, int defaultCloseOperation) {
      this.putValue(NAME, "launch");
      this.programType = programType;
      this.defaultCloseOperation = defaultCloseOperation;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
      org.alice.stageide.program.RunProgramContext runProgramContext = new org.alice.stageide.program.RunProgramContext(programType);

      javax.swing.JFrame frame = new javax.swing.JFrame();
      frame.setDefaultCloseOperation(this.defaultCloseOperation);
      runProgramContext.initializeInContainer(frame.getContentPane(), 640, 360);
      frame.pack();
      frame.setVisible(true);

      runProgramContext.setActiveScene();
    }

    private final org.lgna.project.ast.NamedUserType programType;
    private final int defaultCloseOperation;
  }

  public static void main(String[] args) throws Exception {
    org.lgna.project.ast.NamedUserType programType;
    if (args.length > 0) {
      String path = args[0];
      java.io.File file = new java.io.File(path);
      assert file.exists() : path;
      org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject(file);
      programType = project.getProgramType();
    } else {
      org.alice.stageide.openprojectpane.models.TemplateUriState.Template template = org.alice.stageide.openprojectpane.models.TemplateUriState.Template.GRASS;
      programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType(template.getSurfaceAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor());
    }

    final boolean IS_LAUNCH_BUTTON_DESIRED = true;

    LaunchProgramAction action = new LaunchProgramAction(programType, IS_LAUNCH_BUTTON_DESIRED ? javax.swing.JFrame.DISPOSE_ON_CLOSE : javax.swing.JFrame.EXIT_ON_CLOSE);

    if (IS_LAUNCH_BUTTON_DESIRED) {
      javax.swing.JFrame frame = new javax.swing.JFrame();
      frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(new javax.swing.JButton(action));
      frame.pack();
      frame.setVisible(true);
    } else {
      action.actionPerformed(null);
    }
  }
}
