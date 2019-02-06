package galleryutils;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import edu.cmu.cs.dennisc.java.awt.datatransfer.ClipboardUtilities;
import sun.print.BackgroundLookupListener;

public class GalleryDiffDialog extends JDialog {

	private static final long serialVersionUID = 22870535223061392L;
	
	private JLabel toMatchLabel;
	private JTextField customOption;
	private JComboBox existingOptions;
	private JButton okButton;
	private JButton cancelButton;
	private JCheckBox useCustom;
	
	private String selectedOption = null;
	
	public GalleryDiffDialog() {
		super();
		this.setTitle("Gallery Diff");
		this.setSize(500, 200);
		this.getContentPane().setLayout(new BorderLayout());
		this.setModal(true);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
		JLabel label = new JLabel("Symbol to match:");
		labelPanel.add(label);
		labelPanel.add(Box.createHorizontalGlue());
		contentPane.add(labelPanel);
		
		JPanel labelPanel2 = new JPanel();
		labelPanel2.setLayout(new BoxLayout(labelPanel2, BoxLayout.LINE_AXIS));
		labelPanel2.add(Box.createHorizontalGlue());
		toMatchLabel = new JLabel("<NO SYMBOL>");
		labelPanel2.add(toMatchLabel);
		labelPanel2.add(Box.createHorizontalGlue());
		contentPane.add(labelPanel2);
		
		this.existingOptions = new JComboBox();
		ComboBoxModel model = new DefaultComboBoxModel(new String[]{"TEST1", "TEST2"});
		this.existingOptions.setModel(model);
		this.existingOptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedOption = existingOptions.getSelectedItem().toString();
				useCustom.setSelected(false);
			}
		});
		
		contentPane.add(Box.createVerticalGlue());
		
		contentPane.add(this.existingOptions);
		
		this.customOption = new JTextField(40);
		this.customOption.setMaximumSize( this.customOption.getPreferredSize() );
		this.customOption.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				useCustom.setSelected(true);
			}
		});
		this.customOption.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void focusGained(FocusEvent e) {
				useCustom.setSelected(true);
			}
		});
		contentPane.add(Box.createVerticalGlue());

		JPanel labelPanel3 = new JPanel();
		labelPanel3.setLayout(new BoxLayout(labelPanel3, BoxLayout.LINE_AXIS));
		JLabel label3 = new JLabel("Custom option:");
		labelPanel3.add(label3);
		labelPanel3.add(Box.createHorizontalGlue());
		contentPane.add(labelPanel3);
		
		JPanel customPane = new JPanel();
		customPane.setLayout(new BoxLayout(customPane, BoxLayout.LINE_AXIS));
		this.useCustom = new JCheckBox();
		customPane.add(Box.createHorizontalGlue());
		customPane.add(this.useCustom);
		customPane.add(this.customOption);
		customPane.add(Box.createHorizontalGlue());
		
		contentPane.add(customPane);
		contentPane.add(Box.createVerticalGlue());
		
		this.okButton = new JButton("OKAY");
		this.okButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String customString = customOption.getText();
				if (useCustom.isSelected() && selectedOption == null && customString != null && customString.length() > 0) {
					selectedOption = customString;
				}
				else {
					selectedOption = existingOptions.getSelectedItem().toString();
				}
				GalleryDiffDialog.this.setVisible(false);
			}
		});
		
		this.cancelButton = new JButton("CANCEL");
		this.cancelButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				selectedOption = null;
				GalleryDiffDialog.this.setVisible(false);
			}
		});
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(okButton);
		
		this.getContentPane().add(contentPane, BorderLayout.CENTER);
		this.getContentPane().add(buttonPane, BorderLayout.PAGE_END);
		
	}
	
	public void setOptionsAndShow(String toMatch, String[] options) {
		this.toMatchLabel.setText(toMatch);
		this.existingOptions.setModel( new DefaultComboBoxModel(options));
		this.customOption.setText(toMatch);
		this.useCustom.setSelected(false);
		this.setVisible(true);
	}
	
	public void setOptionsAndShow(String toMatch, List<String> options) {
		String[] sOptions = new String[options.size()];
		for (int i=0; i<options.size(); i++) {
			sOptions[i] = options.get(i);
		}
		setOptionsAndShow(toMatch, sOptions);
	}
	
	public String getSelectedOption() {
		return this.selectedOption;
	}
	
}
