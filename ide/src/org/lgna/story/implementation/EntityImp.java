/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.lgna.story.implementation;

import org.lgna.story.AudioSource;

import edu.cmu.cs.dennisc.matt.AabbCollisionDetector;

/**
 * @author Dennis Cosgrove
 */
public abstract class EntityImp implements ReferenceFrame {
	protected static final edu.cmu.cs.dennisc.scenegraph.Element.Key<EntityImp> ENTITY_IMP_KEY = edu.cmu.cs.dennisc.scenegraph.Element.Key.createInstance( "ENTITY_IMP_KEY" );

	public static EntityImp getInstance( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		return sgElement != null ? sgElement.getBonusDataFor( ENTITY_IMP_KEY ) : null;
	}
	protected void putInstance( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		sgElement.putBonusDataFor( ENTITY_IMP_KEY, this );
	}
	public static <T extends EntityImp> T getInstance( edu.cmu.cs.dennisc.scenegraph.Element sgElement, Class<T> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getInstance( sgElement ), cls );
	}
	public static org.lgna.story.Entity getAbstractionFromSgElement( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		EntityImp imp = getInstance( sgElement );
		if( imp != null ) {
			return imp.getAbstraction();
		} else {
			return null;
		}
	}
	public static <T extends org.lgna.story.Entity> T getAbstractionFromSgElement( edu.cmu.cs.dennisc.scenegraph.Element sgElement, Class<T> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getAbstractionFromSgElement( sgElement ), cls );
	}

	private String name;

	public String getName() {
		return this.name;
	}
	public void setName( String name ) {
		this.name = name;
		this.getSgComposite().setName( name + ".sgComposite" );
	}
	public Property<?> getPropertyForAbstractionGetter( java.lang.reflect.Method getterMthd ) {
		String propertyName = edu.cmu.cs.dennisc.property.PropertyUtilities.getPropertyNameForGetter( getterMthd );
		java.lang.reflect.Field fld = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getField( this.getClass(), propertyName );
		return (Property<?>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( fld, this );
	}

	protected abstract edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound updateCumulativeBound( edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv, edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans );
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 trans = this.getTransformation( asSeenBy );
		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
		this.updateCumulativeBound( cumulativeBound, trans );
		return cumulativeBound.getBoundingBox();
	}
	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
	}

	//	private edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound createCumulativeBound( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
	//		java.util.List< Transformable > transformables = new java.util.LinkedList< Transformable >();
	//		updateHowMuch( transformables, howMuch.isThisACandidate(), howMuch.isChildACandidate(), howMuch.isGrandchildAndBeyondACandidate() );
	//		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound rv = new edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound();
	//		ReferenceFrame actualAsSeenBy = asSeenBy.getActualReferenceFrame( this );
	//
	//		for( Transformable transformable : transformables ) {
	//			edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = transformable.getTransformation( actualAsSeenBy );
	//			assert m.isNaN() == false;
	//			transformable.updateCumulativeBound( rv, m, originPolicy.isOriginIncluded() );
	//		}
	//		return rv;
	//	}
	//	
	//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch, OriginInclusionPolicy originPolicy ) {
	//		edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound cumulativeBound = createCumulativeBound( asSeenBy, howMuch, originPolicy );
	//		return cumulativeBound.getBoundingBox();
	//	}
	//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy, HowMuch howMuch ) {
	//		return getAxisAlignedMinimumBoundingBox( asSeenBy, howMuch, DEFAULT_ORIGIN_INCLUSION_POLICY );
	//	}
	//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox( ReferenceFrame asSeenBy ) {
	//		return getAxisAlignedMinimumBoundingBox( asSeenBy, DEFAULT_HOW_MUCH );
	//	}
	//	public edu.cmu.cs.dennisc.math.AxisAlignedBox getAxisAlignedMinimumBoundingBox() {
	//		return getAxisAlignedMinimumBoundingBox( AsSeenBy.SELF );
	//	}

	public abstract org.lgna.story.Entity getAbstraction();
	public abstract edu.cmu.cs.dennisc.scenegraph.Composite getSgComposite();
	public edu.cmu.cs.dennisc.scenegraph.ReferenceFrame getSgReferenceFrame() {
		return this.getSgComposite();
	}
	public EntityImp getActualEntityImplementation( EntityImp ths ) {
		return this;
	}

	protected edu.cmu.cs.dennisc.scenegraph.Composite getSgVehicle() {
		return this.getSgComposite().getParent();
	}
	protected void setSgVehicle( edu.cmu.cs.dennisc.scenegraph.Composite sgVehicle ) {
		this.getSgComposite().setParent( sgVehicle );
	}

	//HACK
	private EntityImp getEntityImpForSgObject( edu.cmu.cs.dennisc.scenegraph.Composite sgObject ) {
		EntityImp rv = getInstance( sgObject );
		if( rv != null ) {
			return rv;
		} else if( sgObject.getParent() != null ) {
			return getEntityImpForSgObject( sgObject.getParent() );
		}
		return null;
	}

	public final EntityImp getVehicle() {
		edu.cmu.cs.dennisc.scenegraph.Composite sgVehicle = this.getSgVehicle();
		if( sgVehicle != null ) {
			EntityImp rv = getInstance( sgVehicle );
			if( rv != null ) {
				//pass
			} else {
				rv = getEntityImpForSgObject( sgVehicle );
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "No instance found for sgVehicle " + sgVehicle + ". Searched parent and got " + rv );
				if( rv != null ) {
					//pass
				} else {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( this, sgVehicle );
				}
			}
			return rv;
		} else {
			return null;
		}
	}
	public void setVehicle( EntityImp vehicle ) {
		assert vehicle != this;
		this.setSgVehicle( vehicle != null ? vehicle.getSgComposite() : null );
	}

	public boolean isDescendantOf( EntityImp candidateAncestor ) {
		assert candidateAncestor != null : this;
		EntityImp vehicle = this.getVehicle();
		if( vehicle != null ) {
			if( vehicle == candidateAncestor ) {
				return true;
			} else {
				return vehicle.isDescendantOf( candidateAncestor );
			}
		} else {
			return false;
		}
	}

	public SceneImp getScene() {
		EntityImp vehicle = this.getVehicle();
		return vehicle != null ? vehicle.getScene() : null;
	}
	protected ProgramImp getProgram() {
		SceneImp scene = this.getScene();
		return scene != null ? scene.getProgram() : null;
	}
	protected edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass getOnscreenLookingGlass() {
		ProgramImp program = this.getProgram();
		return program != null ? program.getOnscreenLookingGlass() : null;
	}

	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getAbsoluteTransformation() {
		return this.getSgComposite().getAbsoluteTransformation();
	}
	public edu.cmu.cs.dennisc.math.AffineMatrix4x4 getTransformation( ReferenceFrame asSeenBy ) {
		return this.getSgComposite().getTransformation( asSeenBy.getSgReferenceFrame() );
	}

	public StandInImp createStandIn() {
		StandInImp rv = new StandInImp();
		rv.setVehicle( this );
		return rv;
	}
	public StandInImp createOffsetStandIn( double x, double y, double z ) {
		StandInImp rv = this.createStandIn();
		edu.cmu.cs.dennisc.math.AffineMatrix4x4 m = edu.cmu.cs.dennisc.math.AffineMatrix4x4.createIdentity();
		m.translation.set( x, y, z );
		rv.setLocalTransformation( m );
		return rv;
	}

	public java.awt.Point transformToAwt( edu.cmu.cs.dennisc.math.Point3 xyz, CameraImp<?> camera ) {
		return this.getSgComposite().transformToAWT_New( xyz, this.getOnscreenLookingGlass(), camera.getSgCamera() );
	}

	protected static final double RIGHT_NOW = 0.0;
	protected static final double DEFAULT_DURATION = 1.0;
	protected static final edu.cmu.cs.dennisc.animation.Style DEFAULT_STYLE = edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY;

	//	public static final Style DEFAULT_SPEED_STYLE = org.alice.apis.moveandturn.TraditionalStyle.BEGIN_AND_END_ABRUPTLY;
	//	public static final HowMuch DEFAULT_HOW_MUCH = HowMuch.THIS_AND_DESCENDANT_PARTS;

	private double getSimulationSpeedFactor() {
		ProgramImp programImplementation = this.getProgram();
		if( programImplementation != null ) {
			return programImplementation.getSimulationSpeedFactor();
		} else {
			return Double.NaN;
		}
	}
	protected double adjustDurationIfNecessary( double duration ) {
		if( duration == RIGHT_NOW ) {
			//pass
		} else {
			double simulationSpeedFactor = this.getSimulationSpeedFactor();
			if( Double.isNaN( simulationSpeedFactor ) ) {
				duration = RIGHT_NOW;
			} else if( Double.isInfinite( simulationSpeedFactor ) ) {
				duration = RIGHT_NOW;
			} else {
				duration = duration / simulationSpeedFactor;
			}
		}
		return duration;
	}
	public void alreadyAdjustedDelay( double duration ) {
		if( duration == RIGHT_NOW ) {
			//pass;
		} else {
			perform( new edu.cmu.cs.dennisc.animation.DurationBasedAnimation( duration ) {
				@Override
				protected void prologue() {
				}
				@Override
				protected void setPortion( double portion ) {
				}
				@Override
				protected void epilogue() {
				}
			} );
		}
	}
	public void delay( double duration ) {
		this.alreadyAdjustedDelay( this.adjustDurationIfNecessary( duration ) );
	}

	public void playAudio( AudioSource audioSource ) {
		edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
		edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime() );
		this.perform( new edu.cmu.cs.dennisc.media.animation.MediaPlayerAnimation( player ) );
	}

	protected void perform( edu.cmu.cs.dennisc.animation.Animation animation, edu.cmu.cs.dennisc.animation.AnimationObserver animationObserver ) {
		ProgramImp programImplementation = this.getProgram();
		if( programImplementation != null ) {
			programImplementation.perform( animation, animationObserver );
		} else {
			animation.complete( animationObserver );
		}
	}
	protected final void perform( edu.cmu.cs.dennisc.animation.Animation animation ) {
		this.perform( animation, null );
	}

	private java.awt.Component getParentComponent() {
		SceneImp scene = this.getScene();
		if( scene != null ) {
			edu.cmu.cs.dennisc.lookingglass.OnscreenLookingGlass onscreenLookingGlass = this.getOnscreenLookingGlass();
			if( onscreenLookingGlass != null ) {
				return onscreenLookingGlass.getAWTComponent();
			}
		}
		return null;
	}

	private static abstract class NumberModel<N extends Number> {
		private final String message;
		private final javax.swing.text.Document document = new javax.swing.text.PlainDocument();
		private final NumeralAction[] numeralActions = new NumeralAction[ 10 ];
		private final NegateAction negateAction = new NegateAction( this );
		private final BackspaceAction backspaceAction = new BackspaceAction( this );
		public NumberModel( String message ) {
			this.message = message;
			for( int i=0; i<numeralActions.length; i++ ) {
				numeralActions[ i ] = new NumeralAction( this, (short)i );
			}
		}
		protected abstract DecimalPointAction getDecimalPointAction();
		private void append( String s ) {
			try {
				this.document.insertString( this.document.getLength(), s, null );
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		}
		public void appendDigit( short numeral ) {
			this.append( Short.toString( numeral ) );
		}
		public void appendDecimalPoint() {
			javax.swing.Action action = this.getDecimalPointAction();
			if( action != null ) {
				this.append( (String)action.getValue( javax.swing.Action.NAME ) );
			}
		}
		public void negate() {
			final int N = this.document.getLength();
			try {
				boolean isNegative;
				if( N > 0 ) {
					String s0 = this.document.getText( 0, 1 );
					isNegative = s0.charAt( 0 ) == '-';
				} else {
					isNegative = false;
				}
				if( isNegative ) {
					this.document.remove( 0, 1 );
				} else {
					this.document.insertString( 0, "-", null );
				}
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		}
		public void deleteLastCharacter() {
			final int N = this.document.getLength();
			if( this.document.getLength() > 0 ) {
				try {
					this.document.remove( N-1, 1 );
				} catch( javax.swing.text.BadLocationException ble ) {
					throw new RuntimeException( ble );
				}
			}
		}
		
		protected abstract N getValue( String text );
		public N getValue() {
			try {
				final int N = this.document.getLength();
				String text = this.document.getText( 0, N );
				try {
					N rv = this.getValue( text );
					return rv;
				} catch( NumberFormatException nfe ) {
					return null;
				}
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		}
		
		public javax.swing.JComponent createComponent() {
			javax.swing.JPanel gridBagPanel = new javax.swing.JPanel();
			gridBagPanel.setLayout( new java.awt.GridBagLayout() );
			java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
			gbc.fill = java.awt.GridBagConstraints.BOTH;
			gbc.weightx = 1.0;
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 7 ] ), gbc );
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 8 ] ), gbc );
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 9 ] ), gbc );

			gbc.weightx = 0.0;
			gbc.gridwidth = 1;
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 4 ] ), gbc );
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 5 ] ), gbc );
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 6 ] ), gbc );

			gbc.gridwidth = 1;
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 1 ] ), gbc );
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 2 ] ), gbc );
			gbc.gridwidth = java.awt.GridBagConstraints.REMAINDER;
			gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 3 ] ), gbc );


			DecimalPointAction decimalPointAction = this.getDecimalPointAction();
			if( decimalPointAction != null ) {
				gbc.gridwidth = 1;
				gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 0 ] ), gbc );
				gridBagPanel.add( new javax.swing.JButton( decimalPointAction ), gbc );
			} else {
				gbc.gridwidth = 2;
				gridBagPanel.add( new javax.swing.JButton( this.numeralActions[ 0 ] ), gbc );
			}
			gridBagPanel.add( new javax.swing.JButton( this.negateAction ), gbc );

			javax.swing.JPanel lineAxisPanel = new javax.swing.JPanel();
			lineAxisPanel.setLayout( new javax.swing.BoxLayout( lineAxisPanel, javax.swing.BoxLayout.LINE_AXIS ) );
			lineAxisPanel.add( new javax.swing.JTextField( this.document, "", 0 ) );
			lineAxisPanel.add( new javax.swing.JButton( this.backspaceAction ) );

			javax.swing.JLabel messageLabel = new javax.swing.JLabel( this.message );
			//messageLabel.setHorizontalAlignment( javax.swing.SwingConstants.LEADING );
			messageLabel.setAlignmentX( 0.0f );
			lineAxisPanel.setAlignmentX( 0.0f );
			gridBagPanel.setAlignmentX( 0.0f );

			javax.swing.JPanel rv = new javax.swing.JPanel();
			rv.setLayout( new javax.swing.BoxLayout( rv, javax.swing.BoxLayout.PAGE_AXIS ) );
			rv.add( messageLabel );
			rv.add( lineAxisPanel );
			rv.add( gridBagPanel );
			return rv;
		}
	}
	private static class DoubleNumberModel extends NumberModel<Double> {
		private final DecimalPointAction decimalPointAction = new DecimalPointAction( this );
		public DoubleNumberModel( String message ) {
			super( message );
		}
		@Override
		protected Double getValue( String text ) {
			double d = edu.cmu.cs.dennisc.java.lang.DoubleUtilities.parseDoubleInCurrentDefaultLocale( text );
			if( Double.isNaN( d ) ) {
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
		public IntegerNumberModel( String message ) {
			super( message );
		}
		@Override
		protected Integer getValue( String text ) {
			return Integer.valueOf( text );
		}
		@Override
		public DecimalPointAction getDecimalPointAction() {
			return null;
		}
	}
	private static class BackspaceAction extends javax.swing.AbstractAction {
		private final NumberModel<?> numberModel;
		public BackspaceAction( NumberModel<?> numberModel ) {
			this.numberModel = numberModel;
			this.putValue( NAME, "\u2190" );
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			this.numberModel.deleteLastCharacter();
		}
	}

	private static class NumeralAction extends javax.swing.AbstractAction {
		private final NumberModel<?> numberModel;
		private final short digit;
		public NumeralAction( NumberModel<?> numberModel, short digit ) {
			this.numberModel = numberModel;
			this.digit = digit;
			this.putValue( NAME, Short.toString( this.digit ) );
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			this.numberModel.appendDigit( digit );
		}
	}
	private static class NegateAction extends javax.swing.AbstractAction {
		private final NumberModel<?> numberModel;
		public NegateAction( NumberModel<?> numberModel ) {
			this.numberModel = numberModel;
			this.putValue( NAME, "\u00B1" );
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			this.numberModel.negate();
		}
	}
	private static class DecimalPointAction extends javax.swing.AbstractAction {
		private final NumberModel<?> numberModel;
		public DecimalPointAction( NumberModel<?> numberModel ) {
			this.numberModel = numberModel;
			java.text.DecimalFormatSymbols decimalFormatSymbols = new java.text.DecimalFormatSymbols();
			this.putValue( NAME, "" + decimalFormatSymbols.getDecimalSeparator() );
		}
		public void actionPerformed( java.awt.event.ActionEvent e ) {
			this.numberModel.appendDecimalPoint();
		}
	}
	
	public double getDoubleFromUser( String message ) {
		String title = null;
		DoubleNumberModel model = new DoubleNumberModel( message );
		while( true ) {
			javax.swing.JOptionPane.showMessageDialog( this.getParentComponent(), model.createComponent(), title, javax.swing.JOptionPane.QUESTION_MESSAGE );
			Double value = model.getValue();
			if( value != null ) {
				return value;
			}
		}
	}
	public int getIntegerFromUser( String message ) {
		String title = null;
		IntegerNumberModel model = new IntegerNumberModel( message );
		while( true ) {
			javax.swing.JOptionPane.showMessageDialog( this.getParentComponent(), model.createComponent(), title, javax.swing.JOptionPane.QUESTION_MESSAGE );
			Integer value = model.getValue();
			if( value != null ) {
				return value;
			}
		}
	}
	public boolean getBooleanFromUser( String message ) {
		java.awt.Component parentComponent = this.getParentComponent();
		String title = null;
		Object[] selectionValues = { "True", "False" };
		javax.swing.Icon icon = null;
		Object initialSelectionValue = null;
		while( true ) {
			int option = javax.swing.JOptionPane.showOptionDialog( parentComponent, message, title, javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, icon, selectionValues, initialSelectionValue );
			switch( option ) {
			case javax.swing.JOptionPane.YES_OPTION:
				return true;
			case javax.swing.JOptionPane.NO_OPTION:
				return false;
			}
		}
	}
	public String getStringFromUser( String message ) {
		java.awt.Component parentComponent = this.getParentComponent();
		String title = null;
		
		javax.swing.JOptionPane optionPane = new javax.swing.JOptionPane( message, javax.swing.JOptionPane.QUESTION_MESSAGE, javax.swing.JOptionPane.DEFAULT_OPTION );
		optionPane.setWantsInput( true );
		
		javax.swing.JDialog dialog = optionPane.createDialog( parentComponent, title );

//		dialog.setResizable( true );

//		if( javax.swing.JDialog.isDefaultLookAndFeelDecorated() ) {
//			if( javax.swing.UIManager.getLookAndFeel().getSupportsWindowDecorations() ) {
//				dialog.setUndecorated( true );
//				dialog.getRootPane().setWindowDecorationStyle( javax.swing.JRootPane.QUESTION_DIALOG );
//			}
//		}
		
		while( true ) {
			dialog.setVisible( true );
			Object value = optionPane.getInputValue();
			if( javax.swing.JOptionPane.UNINITIALIZED_VALUE.equals( value ) ) {
				//pass
			} else {
				dialog.dispose();
				return (String)value;
			}
		}
	}
	
	protected void appendRepr( StringBuilder sb ) {
	}
	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append( this.getClass().getSimpleName() );
		sb.append( "[" );
		this.appendRepr( sb );
		sb.append( "]" );
		return sb.toString();
	}
	public boolean isCollidingWith( org.lgna.story.Entity other ) {
		return AabbCollisionDetector.doTheseCollide( this.getAbstraction(), other );
	}
	
	public static void main( String[] args ) {
		org.lgna.story.Entity entity = new org.lgna.story.Cone();
		System.err.println( entity.getStringFromUser( "who are you?" ) );
		System.err.println( entity.getIntegerFromUser( "four score and seven years ago is how many days?" ) );
		System.err.println( entity.getDoubleFromUser( "how much?" ) );
		System.err.println( entity.getBooleanFromUser( "to be or not to be?" ) );
	}
}