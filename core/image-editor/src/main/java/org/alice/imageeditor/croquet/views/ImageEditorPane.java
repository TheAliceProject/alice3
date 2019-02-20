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
package org.alice.imageeditor.croquet.views;

import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import edu.cmu.cs.dennisc.javax.swing.JShowLabel;
import org.alice.imageeditor.croquet.ImageEditorFrame;
import org.alice.imageeditor.croquet.Tool;
import org.alice.imageeditor.croquet.views.renderers.FilenameListCellRenderer;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AbstractWindow;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.croquet.views.Separator;

import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public class ImageEditorPane extends MigPanel {
	private final FocusListener comboBoxEditorFocusListener = new FocusListener() {
		@Override
		public void focusGained( FocusEvent e ) {
			SwingUtilities.invokeLater( new Runnable() {
				@Override
				public void run() {
					ComboBoxEditor editor = getComposite().getJComboBox().getEditor();
					Component editorComponent = editor.getEditorComponent();
					if( editorComponent instanceof JTextComponent ) {
						JTextComponent jTextComponent = (JTextComponent)editorComponent;
						String rootDirectoryPath = getComposite().getRootDirectoryState().getValue();
						String path = jTextComponent.getText();
						if( path.startsWith( rootDirectoryPath ) ) {
							int startIndex = rootDirectoryPath.length();
							if( path.length() > startIndex ) {
								char c = path.charAt( startIndex );
								if( ( c == '/' ) || ( c == '\\' ) ) {
									startIndex += 1;
								}
							}
							int endIndex = path.length();
							if( path.endsWith( ".png" ) ) {
								endIndex -= 4;
							}
							jTextComponent.select( startIndex, endIndex );
						} else {
							jTextComponent.selectAll();
						}
					} else {
						editor.selectAll();
					}
				}
			} );
		}

		@Override
		public void focusLost( FocusEvent e ) {
		}
	};

	private final ValueListener<Image> imageListener = new ValueListener<Image>() {
		@Override
		public void valueChanged( ValueEvent<Image> e ) {
			repaint();
		}
	};

	private final ValueListener<String> pathListener = new ValueListener<String>() {
		@Override
		public void valueChanged( ValueEvent<String> e ) {
			updatePathLabel( e.getNextValue() );
		}
	};

	private final ValueListener<Boolean> repaintImageViewListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			jImageView.repaint();
		}
	};

	private final ValueListener<Boolean> revalidateImageViewAndResizeWindowIfNecessaryListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			jImageView.revalidate();
			AbstractWindow<?> window = getRoot();
			if( window != null ) {
				Dimension size = window.getSize();
				Dimension preferredSize = window.getAwtComponent().getPreferredSize();
				if( ( size.width < preferredSize.width ) || ( size.height < preferredSize.height ) ) {
					window.setSize( Math.max( size.width, preferredSize.width ), Math.max( size.height, preferredSize.height ) );
				}
			}
		}
	};

	private class SelectToolActionListener implements ActionListener {
		private final Tool tool;

		public SelectToolActionListener( Tool tool ) {
			this.tool = tool;
		}

		@Override
		public void actionPerformed( ActionEvent e ) {
			getComposite().getToolState().setValueTransactionlessly( this.tool );
		}
	}

	private static final KeyStroke CROP_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_F12, 0 );
	private static final KeyStroke RECTANGLE_KEY_STROKE = KeyStroke.getKeyStroke( KeyEvent.VK_F11, 0 );
	private final ActionListener cropListener = new SelectToolActionListener( Tool.CROP_SELECT );
	private final ActionListener rectangleListener = new SelectToolActionListener( Tool.ADD_RECTANGLE );

	private final JImageEditorView jImageView;

	private final JShowLabel jPathLabel = new JShowLabel();

	private final Button saveButton;
	private final Button cropButton;

	public ImageEditorPane( ImageEditorFrame composite ) {
		super( composite, "fill", "[grow 0][grow 100]16[grow 0]", "[grow,shrink]16[grow 0, shrink 0][grow 0, shrink 0][grow 0, shrink 0]" );

		this.jImageView = new JImageEditorView( composite );
		this.saveButton = composite.getSaveOperation().createButton();

		JComboBox jComboBox = composite.getJComboBox();
		jComboBox.setRenderer( new FilenameListCellRenderer() );
		jComboBox.setMaximumRowCount( 24 );
		jComboBox.setMinimumSize( new Dimension( 0, 0 ) );

		this.getAwtComponent().add( this.jImageView, "align center, spanx 2" );

		MigPanel panel = new MigPanel( null, "insets 0, fill" );

		this.cropButton = composite.getCropOperation().createButton();
		panel.addComponent( composite.getToolState().createVerticalDefaultRadioButtons(), "growx, wrap" );
		panel.addComponent( this.cropButton, "growx, split 2" );
		panel.addComponent( composite.getUncropOperation().createButton(), "growx, wrap" );
		panel.addComponent( composite.getClearOperation().createButton(), "growx, wrap" );
		panel.addComponent( composite.getDropShadowState().createCheckBox(), "growx, wrap" );
		panel.addComponent( Separator.createInstanceSeparatingTopFromBottom(), "growx, gap bottom 16, wrap" );
		panel.addComponent( composite.getShowInScreenResolutionState().getSidekickLabel().createLabel(), "split 2" );
		panel.addComponent( composite.getShowInScreenResolutionState().createHorizontalToggleButtons(), "growx, wrap" );
		panel.addComponent( composite.getShowDashedBorderState().createCheckBox(), "growx, wrap" );
		panel.addComponent( composite.getCopyOperation().createButton(), "pushy, aligny bottom, growx" );
		this.addComponent( panel, "aligny top, grow, shrink, wrap" );

		this.addComponent( composite.getRootDirectoryState().getSidekickLabel().createLabel(), "align right" );
		this.addComponent( composite.getRootDirectoryState().createTextField(), "split 2, growx, shrinkx" );
		this.addComponent( composite.getBrowseOperation().createButton() );
		this.addComponent( this.saveButton, "spany 3, grow, wrap" );

		this.addComponent( new Label( "file:" ), "align right" );
		this.getAwtComponent().add( jComboBox, "growx, shrinkx, wrap" );
		this.getAwtComponent().add( this.jPathLabel, "skip 1" );

		this.jPathLabel.setForeground( Color.DARK_GRAY );

		this.updatePathLabel( composite.getPathHolder().getValue() );
	}

	private void updatePathLabel( String nextPath ) {
		if( FileUtilities.isValidPath( nextPath ) ) {
			//pass
		} else {
			Logger.outln( "INVALID PATH:", nextPath );
		}

		this.jPathLabel.setText( nextPath );
		if( nextPath != null ) {
			Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
			if( awtEditorComponent instanceof JTextField ) {
				JTextField jTextField = (JTextField)awtEditorComponent;
				boolean isEqual = jTextField.getText().contentEquals( nextPath );
				boolean isShowing = false == ( isEqual || ImageEditorFrame.INVALID_PATH_NOT_A_DIRECTORY.contentEquals( nextPath ) || ImageEditorFrame.INVALID_PATH_EMPTY_SUB_PATH.contentEquals( nextPath ) );
				this.jPathLabel.setShowing( isShowing );
			}
		}
	}

	@Override
	public ImageEditorFrame getComposite() {
		return (ImageEditorFrame)super.getComposite();
	}

	public Image render() {
		ImageEditorFrame composite = getComposite();
		Image image = composite.getImageHolder().getValue();
		if( image != null ) {
			int width;
			int height;
			Rectangle selection = composite.getCropSelectHolder().getValue();
			assert selection == null : selection;
			Rectangle crop = composite.getCropCommitHolder().getValue();
			if( crop != null ) {
				width = crop.width;
				height = crop.height;
			} else {
				width = image.getWidth( this.jImageView );
				height = image.getHeight( this.jImageView );
			}
			BufferedImage rv = new BufferedImage( width, height, BufferedImage.TYPE_INT_BGR );
			Graphics2D g2 = (Graphics2D)rv.getGraphics();
			this.jImageView.render( g2 );
			g2.dispose();
			return rv;
		} else {
			return null;
		}
	}

	public void setDefaultButtonToSave() {
		this.getRoot().setDefaultButton( this.saveButton );
	}

	public void setDefaultButtonToCrop() {
		this.getRoot().setDefaultButton( this.cropButton );
	}

	@Override
	public void handleCompositePreActivation() {
		this.getComposite().getShowInScreenResolutionState().addNewSchoolValueListener( this.revalidateImageViewAndResizeWindowIfNecessaryListener );
		this.getComposite().getShowDashedBorderState().addNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getDropShadowState().addNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getImageHolder().addAndInvokeValueListener( this.imageListener );
		this.getComposite().getPathHolder().addAndInvokeValueListener( this.pathListener );
		Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
		awtEditorComponent.addFocusListener( this.comboBoxEditorFocusListener );
		this.setDefaultButtonToSave();
		SwingUtilities.invokeLater( new Runnable() {
			@Override
			public void run() {
				getComposite().getJComboBox().getEditor().getEditorComponent().requestFocusInWindow();
			}
		} );
		this.registerKeyboardAction( this.cropListener, CROP_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( this.rectangleListener, RECTANGLE_KEY_STROKE, Condition.WHEN_IN_FOCUSED_WINDOW );
		super.handleCompositePreActivation();
	}

	@Override
	public void handleCompositePostDeactivation() {
		super.handleCompositePostDeactivation();
		this.unregisterKeyboardAction( RECTANGLE_KEY_STROKE );
		this.unregisterKeyboardAction( CROP_KEY_STROKE );
		Component awtEditorComponent = this.getComposite().getJComboBox().getEditor().getEditorComponent();
		awtEditorComponent.removeFocusListener( this.comboBoxEditorFocusListener );
		this.getComposite().getPathHolder().removeValueListener( this.pathListener );
		this.getComposite().getImageHolder().removeValueListener( this.imageListener );
		this.getComposite().getDropShadowState().removeNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getShowDashedBorderState().removeNewSchoolValueListener( this.repaintImageViewListener );
		this.getComposite().getShowInScreenResolutionState().removeNewSchoolValueListener( this.revalidateImageViewAndResizeWindowIfNecessaryListener );
	}
}
