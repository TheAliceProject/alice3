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

package org.lgna.story.implementation;

import edu.cmu.cs.dennisc.animation.DurationBasedAnimation;
import edu.cmu.cs.dennisc.animation.Style;
import edu.cmu.cs.dennisc.animation.TraditionalStyle;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.lang.DoubleUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.media.Player;
import edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation;
import edu.cmu.cs.dennisc.media.jmf.MediaFactory;
import edu.cmu.cs.dennisc.property.PropertyUtilities;
import edu.cmu.cs.dennisc.render.OnscreenRenderTarget;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Element;
import edu.cmu.cs.dennisc.scenegraph.Joint;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;
import edu.cmu.cs.dennisc.scenegraph.qa.Mender;
import edu.cmu.cs.dennisc.scenegraph.qa.QualityAssuranceUtilities;
import org.lgna.common.LgnaIllegalArgumentException;
import org.lgna.common.ProgramClosedException;
import org.lgna.story.AudioSource;
import org.lgna.story.SThing;
import org.lgna.story.implementation.eventhandling.AabbCollisionDetector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;

/**
 * @author Dennis Cosgrove
 */
public abstract class EntityImp extends PropertyOwnerImp implements ReferenceFrame {
  protected static final Element.Key<EntityImp> ENTITY_IMP_KEY = Element.Key.createInstance("ENTITY_IMP_KEY");

  public static EntityImp getInstance(Element sgElement) {
    return sgElement != null ? sgElement.getBonusDataFor(ENTITY_IMP_KEY) : null;
  }

  protected void putInstance(Element sgElement) {
    sgElement.putBonusDataFor(ENTITY_IMP_KEY, this);
  }

  public static <T extends EntityImp> T getInstance(Element sgElement, Class<T> cls) {
    return ClassUtilities.getInstance(getInstance(sgElement), cls);
  }

  public static SThing getAbstractionFromSgElement(Element sgElement) {
    EntityImp imp = getInstance(sgElement);
    if (imp != null) {
      return imp.getAbstraction();
    } else {
      return null;
    }
  }

  public static <T extends SThing> T getAbstractionFromSgElement(Element sgElement, Class<T> cls) {
    return ClassUtilities.getInstance(getAbstractionFromSgElement(sgElement), cls);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
    this.getSgComposite().setName(name + ".sgComposite");
  }

  public Property<?> getPropertyForAbstractionGetter(Method getterMthd) {
    String propertyName = PropertyUtilities.getPropertyNameForGetter(getterMthd);
    Field fld = ReflectionUtilities.getField(this.getClass(), propertyName);
    return (Property<?>) ReflectionUtilities.get(fld, this);
  }

  protected abstract void updateCumulativeBound(CumulativeBound rv, AffineMatrix4x4 trans);

  public AxisAlignedBox getAxisAlignedMinimumBoundingBox(ReferenceFrame asSeenBy) {
    AffineMatrix4x4 trans = this.getTransformation(asSeenBy);
    CumulativeBound cumulativeBound = new CumulativeBound();
    this.updateCumulativeBound(cumulativeBound, trans);
    return cumulativeBound.getBoundingBox();
  }

  public AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
    return getAxisAlignedMinimumBoundingBox(AsSeenBy.SELF);
  }

  //Returns a bounding box that reflects any changes to the given entity. Namely any changes to the skeleton of jointed models
  public AxisAlignedBox getDynamicAxisAlignedMinimumBoundingBox(ReferenceFrame asSeenBy) {
    return this.getAxisAlignedMinimumBoundingBox(asSeenBy);
  }

  public AxisAlignedBox getDynamicAxisAlignedMinimumBoundingBox() {
    return getDynamicAxisAlignedMinimumBoundingBox(AsSeenBy.SELF);
  }

  //  private edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound createCumulativeBound( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
  //    java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
  //    updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
  //    edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
  //    ReferenceFrame actualAsSeenBy = asSeenBy.getActualReferenceFrame( this );
  //
  //    for( Transformable transformable : transformables ) {
  //      edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.getTransformation( actualAsSeenBy );
  //      assert m.isNaN() == false;
  //      transformable.updateCumulativeBound( rv, m, originPolicy.isOriginIncluded() );
  //    }
  //    return rv;
  //  }
  //
  //  public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
  //    edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = createCumulativeBound( asSeenBy, howMuch, originPolicy );
  //    return cumulativeBound.getBoundingBox();
  //  }
  //  public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch ) {
  //    return getAxisAlignedMinimumBoundingBox( asSeenBy, howMuch, DEFAULT_ORIGIN_INCLUSION_POLICY );
  //  }
  //  public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
  //    return getAxisAlignedMinimumBoundingBox( asSeenBy, DEFAULT_HOW_MUCH );
  //  }
  //  public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
  //    return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
  //  }

  public abstract SThing getAbstraction();

  public abstract Composite getSgComposite();

  @Override
  public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSgReferenceFrame() {
    return this.getSgComposite();
  }

  @Override
  public EntityImp getActualEntityImplementation(EntityImp ths) {
    return this;
  }

  protected Composite getSgVehicle() {
    return this.getSgComposite().getParent();
  }

  protected void setSgVehicle(Composite sgVehicle) {
    this.getSgComposite().setParentWithoutMoving(sgVehicle);
  }

  //HACK
  private EntityImp getEntityImpForSgObject(Composite sgObject) {
    EntityImp rv = getInstance(sgObject);
    if (rv != null) {
      return rv;
    } else if (sgObject.getParent() != null) {
      return getEntityImpForSgObject(sgObject.getParent());
    }
    return null;
  }

  public final EntityImp getVehicle() {
    Composite sgVehicle = this.getSgVehicle();
    if (sgVehicle != null) {
      EntityImp rv = getInstance(sgVehicle);
      if (rv != null) {
        //pass
      } else {
        //this does happen when the vehicle is the ROOT bone of a biped
        //ROOT apparently has no scenegraph counterpart.
        //ROOT won't be the child of any joint. however it could be the child of something else in the scene.
        //Therefore I realize that this is not a good fix in a generic EntityImp class.
        //However I don't know what is. Therefore this is what I do for now.
        rv = getEntityImpForSgObject(sgVehicle);
        Logger.severe("No instance found for sgVehicle " + sgVehicle + ". Searched parent and got " + rv);
        if (rv != null) {
          //pass
        } else {
          Logger.severe(this, sgVehicle);
        }
      }
      return rv;
    } else {
      return null;
    }
  }

  protected void postCheckSetVehicle(EntityImp vehicle) {
    this.setSgVehicle(vehicle != null ? vehicle.getSgComposite() : null);
  }

  public final void setVehicle(EntityImp vehicle) {
    //    if( vehicle == null ) {
    //      throw new org.lgna.common.LgnaIllegalArgumentException( "vehicle argument is null", 0, null );
    //    }
    if (vehicle != null) {
      if (vehicle == this) {
        throw new LgnaIllegalArgumentException("vehicle argument \"" + vehicle.getAbstraction() + "\" is the same as the instance \"" + this.getAbstraction() + "\"", 0, vehicle.getAbstraction());
      }
      if (vehicle.isDescendantOf(this)) {
        throw new LgnaIllegalArgumentException("vehicle argument \"" + vehicle.getAbstraction() + "\" is descendant of the instance \"" + this.getAbstraction() + "\" which would cause a cycle", 0, vehicle.getAbstraction());
      }
    }

    this.postCheckSetVehicle(vehicle);
  }

  public boolean isDescendantOf(EntityImp candidateAncestor) {
    assert candidateAncestor != null : this;
    EntityImp vehicle = this.getVehicle();
    if (vehicle != null) {
      if (vehicle == candidateAncestor) {
        return true;
      } else {
        return vehicle.isDescendantOf(candidateAncestor);
      }
    } else {
      return false;
    }
  }

  public SceneImp getScene() {
    EntityImp vehicle = this.getVehicle();
    return vehicle != null ? vehicle.getScene() : null;
  }

  @Override
  public ProgramImp getProgram() {
    SceneImp scene = this.getScene();
    return scene != null ? scene.getProgram() : null;
  }

  protected OnscreenRenderTarget<?> getOnscreenRenderTarget() {
    ProgramImp program = this.getProgram();
    return program != null ? program.getOnscreenRenderTarget() : null;
  }

  public AffineMatrix4x4 getAbsoluteTransformation() {
    return this.getSgComposite().getAbsoluteTransformation();
  }

  public AffineMatrix4x4 getTransformation(ReferenceFrame asSeenBy) {
    return this.getSgComposite().getTransformation(asSeenBy.getSgReferenceFrame());
  }

  public StandInImp createStandIn() {
    StandInImp rv = new StandInImp();
    rv.setVehicle(this);
    return rv;
  }

  public StandInImp createOffsetStandIn(double x, double y, double z) {
    StandInImp rv = this.createStandIn();
    AffineMatrix4x4 m = AffineMatrix4x4.createIdentity();
    m.translation.set(x, y, z);
    rv.setLocalTransformation(m);
    return rv;
  }

  public Point transformToAwt(Point3 xyz, CameraImp<?> camera) {
    return this.getSgComposite().transformToAWT_New(xyz, this.getOnscreenRenderTarget(), camera.getSgCamera());
  }

  protected static final double DEFAULT_DURATION = 1.0;
  protected static final Style DEFAULT_STYLE = TraditionalStyle.BEGIN_AND_END_GENTLY;

  //  public static final Style DEFAULT_SPEED_STYLE = org.alice.apis.moveandturn.TraditionalStyle.BEGIN_AND_END_ABRUPTLY;
  //  public static final HowMuch DEFAULT_HOW_MUCH = HowMuch.THIS_AND_DESCENDANT_PARTS;

  public void alreadyAdjustedDelay(double duration) {
    if (duration == RIGHT_NOW) {
      //pass;
    } else {
      perform(new DurationBasedAnimation(duration) {
        @Override
        protected void prologue() {
        }

        @Override
        protected void setPortion(double portion) {
        }

        @Override
        protected void epilogue() {
        }
      });
    }
  }

  public void delay(double duration) {
    this.alreadyAdjustedDelay(this.adjustDurationIfNecessary(duration));
  }

  public void playAudio(AudioSource audioSource) {
    edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = MediaFactory.getSingleton();
    Player player = mediaFactory.createPlayer(audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime());
    this.perform(new MediaPlayerAnimation(player));
  }

  private Component getParentComponent() {
    SceneImp scene = this.getScene();
    if (scene != null) {
      OnscreenRenderTarget<?> onscreenRenderTarget = this.getOnscreenRenderTarget();
      if (onscreenRenderTarget != null) {
        return onscreenRenderTarget.getAwtComponent();
      }
    }
    return null;
  }

  private static class JNumPad extends JPanel {

    //todo: select all on focus?

    private final AncestorListener ancestorListener = new AncestorListener() {
      @Override
      public void ancestorAdded(AncestorEvent event) {
        textField.requestFocusInWindow();
      }

      @Override
      public void ancestorMoved(AncestorEvent event) {
      }

      @Override
      public void ancestorRemoved(AncestorEvent event) {
      }
    };

    private final JTextField textField;

    public JNumPad(NumberModel<?> numberModel) {
      JPanel gridBagPanel = new JPanel();
      gridBagPanel.setLayout(new GridBagLayout());
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.fill = GridBagConstraints.BOTH;
      gbc.weightx = 1.0;
      gridBagPanel.add(new JButton(numberModel.numeralActions[7]), gbc);
      gridBagPanel.add(new JButton(numberModel.numeralActions[8]), gbc);
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gridBagPanel.add(new JButton(numberModel.numeralActions[9]), gbc);

      gbc.weightx = 0.0;
      gbc.gridwidth = 1;
      gridBagPanel.add(new JButton(numberModel.numeralActions[4]), gbc);
      gridBagPanel.add(new JButton(numberModel.numeralActions[5]), gbc);
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gridBagPanel.add(new JButton(numberModel.numeralActions[6]), gbc);

      gbc.gridwidth = 1;
      gridBagPanel.add(new JButton(numberModel.numeralActions[1]), gbc);
      gridBagPanel.add(new JButton(numberModel.numeralActions[2]), gbc);
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gridBagPanel.add(new JButton(numberModel.numeralActions[3]), gbc);

      DecimalPointAction decimalPointAction = numberModel.getDecimalPointAction();
      if (decimalPointAction != null) {
        gbc.gridwidth = 1;
        gridBagPanel.add(new JButton(numberModel.numeralActions[0]), gbc);
        gridBagPanel.add(new JButton(decimalPointAction), gbc);
      } else {
        gbc.gridwidth = 2;
        gridBagPanel.add(new JButton(numberModel.numeralActions[0]), gbc);
      }
      gridBagPanel.add(new JButton(numberModel.negateAction), gbc);

      this.textField = new JTextField(numberModel.document, "", 0);
      JPanel lineAxisPanel = new JPanel();
      lineAxisPanel.setLayout(new BoxLayout(lineAxisPanel, BoxLayout.LINE_AXIS));
      lineAxisPanel.add(this.textField);
      lineAxisPanel.add(new JButton(numberModel.backspaceAction));

      JLabel messageLabel = new JLabel(numberModel.message);
      //messageLabel.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
      messageLabel.setAlignmentX(0.0f);
      lineAxisPanel.setAlignmentX(0.0f);
      gridBagPanel.setAlignmentX(0.0f);

      this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      this.add(messageLabel);
      this.add(lineAxisPanel);
      this.add(gridBagPanel);
    }

    public void addListeners() {
      this.textField.addAncestorListener(this.ancestorListener);
    }

    public void removeListeners() {
      this.textField.removeAncestorListener(this.ancestorListener);
    }
  }

  private abstract static class NumberModel<N extends Number> {
    private final String message;
    private final Document document = new PlainDocument();
    private final NumeralAction[] numeralActions = new NumeralAction[10];
    private final NegateAction negateAction = new NegateAction(this);
    private final BackspaceAction backspaceAction = new BackspaceAction(this);

    public NumberModel(String message) {
      this.message = message;
      for (int i = 0; i < numeralActions.length; i++) {
        numeralActions[i] = new NumeralAction(this, (short) i);
      }
    }

    protected abstract DecimalPointAction getDecimalPointAction();

    private void append(String s) {
      try {
        this.document.insertString(this.document.getLength(), s, null);
      } catch (BadLocationException ble) {
        throw new RuntimeException(ble);
      }
    }

    public void appendDigit(short numeral) {
      this.append(Short.toString(numeral));
    }

    public void appendDecimalPoint() {
      Action action = this.getDecimalPointAction();
      if (action != null) {
        this.append((String) action.getValue(Action.NAME));
      }
    }

    public void negate() {
      final int N = this.document.getLength();
      try {
        boolean isNegative;
        if (N > 0) {
          String s0 = this.document.getText(0, 1);
          isNegative = s0.charAt(0) == '-';
        } else {
          isNegative = false;
        }
        if (isNegative) {
          this.document.remove(0, 1);
        } else {
          this.document.insertString(0, "-", null);
        }
      } catch (BadLocationException ble) {
        throw new RuntimeException(ble);
      }
    }

    public void deleteLastCharacter() {
      final int N = this.document.getLength();
      if (this.document.getLength() > 0) {
        try {
          this.document.remove(N - 1, 1);
        } catch (BadLocationException ble) {
          throw new RuntimeException(ble);
        }
      }
    }

    protected abstract N getValue(String text);

    public N getValue() {
      try {
        final int N = this.document.getLength();
        String text = this.document.getText(0, N);
        try {
          N rv = this.getValue(text);
          return rv;
        } catch (NumberFormatException nfe) {
          return null;
        }
      } catch (BadLocationException ble) {
        throw new RuntimeException(ble);
      }
    }

    public JNumPad createComponent() {
      return new JNumPad(this);
    }
  }

  private static class DoubleNumberModel extends NumberModel<Double> {
    private final DecimalPointAction decimalPointAction = new DecimalPointAction(this);

    public DoubleNumberModel(String message) {
      super(message);
    }

    @Override
    protected Double getValue(String text) {
      double d = DoubleUtilities.parseDoubleInCurrentDefaultLocale(text);
      if (Double.isNaN(d)) {
        return null;
      } else {
        return d;
      }
    }

    @Override
    public DecimalPointAction getDecimalPointAction() {
      return this.decimalPointAction;
    }
  }

  private static class IntegerNumberModel extends NumberModel<Integer> {
    public IntegerNumberModel(String message) {
      super(message);
    }

    @Override
    protected Integer getValue(String text) {
      return Integer.valueOf(text);
    }

    @Override
    public DecimalPointAction getDecimalPointAction() {
      return null;
    }
  }

  private static class BackspaceAction extends AbstractAction {
    private final NumberModel<?> numberModel;

    public BackspaceAction(NumberModel<?> numberModel) {
      this.numberModel = numberModel;
      this.putValue(NAME, "←");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.numberModel.deleteLastCharacter();
    }
  }

  private static class NumeralAction extends AbstractAction {
    private final NumberModel<?> numberModel;
    private final short digit;

    public NumeralAction(NumberModel<?> numberModel, short digit) {
      this.numberModel = numberModel;
      this.digit = digit;
      this.putValue(NAME, Short.toString(this.digit));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.numberModel.appendDigit(digit);
    }
  }

  private static class NegateAction extends AbstractAction {
    private final NumberModel<?> numberModel;

    public NegateAction(NumberModel<?> numberModel) {
      this.numberModel = numberModel;
      this.putValue(NAME, "±");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.numberModel.negate();
    }
  }

  private static class DecimalPointAction extends AbstractAction {
    private final NumberModel<?> numberModel;

    public DecimalPointAction(NumberModel<?> numberModel) {
      this.numberModel = numberModel;
      DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
      this.putValue(NAME, "" + decimalFormatSymbols.getDecimalSeparator());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      this.numberModel.appendDecimalPoint();
    }
  }

  private void promptUserWithOptionToCancel() {
    String optionA = "return to the previous dialog";
    String optionB = "exit the running program";
    StringBuilder sb = new StringBuilder();
    sb.append("Invalid dialog input.\n\nWould you like to:\n    ");
    sb.append(optionA);
    sb.append("\n        or\n    ");
    sb.append(optionB);
    sb.append("\n?");
    int returnResult;
    Object[] options;
    if (SystemUtilities.isWindows()) {
      returnResult = JOptionPane.YES_OPTION;
      options = new Object[] {optionA, optionB};
    } else {
      returnResult = JOptionPane.NO_OPTION;
      options = new Object[] {optionB, optionA};
    }
    int result = JOptionPane.showOptionDialog(this.getParentComponent(), sb.toString(), "Invalid Dialog Input", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, optionA);
    if (result == returnResult) {
      return;
    } else {
      //javax.swing.SwingUtilities.getRoot( this.getProgram().getOnscreenLookingGlass().getAWTComponent() ).setVisible( false );
      throw new ProgramClosedException("user dialog");
    }
  }

  public double getDoubleFromUser(String message) {
    String title = null;
    DoubleNumberModel model = new DoubleNumberModel(message);
    while (true) {
      JNumPad numPad = model.createComponent();
      numPad.addListeners();
      JOptionPane.showMessageDialog(this.getParentComponent(), numPad, title, JOptionPane.QUESTION_MESSAGE);
      numPad.removeListeners();
      Double value = model.getValue();
      if (value != null) {
        return value;
      } else {
        this.promptUserWithOptionToCancel();
      }
    }
  }

  public int getIntegerFromUser(String message) {
    String title = null;
    IntegerNumberModel model = new IntegerNumberModel(message);
    while (true) {
      JNumPad numPad = model.createComponent();
      numPad.addListeners();
      JOptionPane.showMessageDialog(this.getParentComponent(), numPad, title, JOptionPane.QUESTION_MESSAGE);
      numPad.removeListeners();
      Integer value = model.getValue();
      if (value != null) {
        return value;
      } else {
        this.promptUserWithOptionToCancel();
      }
    }
  }

  public boolean getBooleanFromUser(String message) {
    Component parentComponent = this.getParentComponent();
    String title = null;
    Object[] selectionValues = {"True", "False"};
    Icon icon = null;
    Object initialSelectionValue = null;
    while (true) {
      int option = JOptionPane.showOptionDialog(parentComponent, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, icon, selectionValues, initialSelectionValue);
      switch (option) {
      case JOptionPane.YES_OPTION:
        return true;
      case JOptionPane.NO_OPTION:
        return false;
      default:
        this.promptUserWithOptionToCancel();
      }
    }
  }

  public String getStringFromUser(String message) {
    Component parentComponent = this.getParentComponent();
    String title = null;

    JOptionPane optionPane = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION);
    optionPane.setWantsInput(true);

    JDialog dialog = optionPane.createDialog(parentComponent, title);

    //    dialog.setResizable( true );

    //    if( javax.swing.JDialog.isDefaultLookAndFeelDecorated() ) {
    //      if( javax.swing.UIManager.getLookAndFeel().getSupportsWindowDecorations() ) {
    //        dialog.setUndecorated( true );
    //        dialog.getRootPane().setWindowDecorationStyle( javax.swing.JRootPane.QUESTION_DIALOG );
    //      }
    //    }

    while (true) {
      dialog.setVisible(true);
      Object value = optionPane.getInputValue();
      if (JOptionPane.UNINITIALIZED_VALUE.equals(value)) {
        this.promptUserWithOptionToCancel();
      } else {
        dialog.dispose();
        return (String) value;
      }
    }
  }

  public boolean isCollidingWith(SThing other) {
    return AabbCollisionDetector.doTheseCollide(this.getAbstraction(), other);
  }

  public void mendSceneGraphIfNecessary() {
    QualityAssuranceUtilities.inspectAndMendIfNecessary(this.getSgComposite(), new Mender() {
      @Override
      public AffineMatrix4x4 getMendTransformationFor(Joint sgJoint) {
        EntityImp imp = EntityImp.getInstance(sgJoint);
        if (imp instanceof JointImp) {
          JointImp jointImp = (JointImp) imp;
          return jointImp.getScaledOriginalTransformation();
        } else {
          return AffineMatrix4x4.createIdentity();
        }
      }
    });
  }

  protected void appendRepr(StringBuilder sb) {
  }

  @Override
  public final String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName());
    sb.append("[");
    this.appendRepr(sb);
    sb.append("]");
    return sb.toString();
  }

  private String name;
}
