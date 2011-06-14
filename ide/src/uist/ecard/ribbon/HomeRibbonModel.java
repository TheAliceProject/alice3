package uist.ecard.ribbon;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;

public class HomeRibbonModel extends org.lgna.croquet.PredeterminedTab {
	private static class SingletonHolder {
		private static HomeRibbonModel instance = new HomeRibbonModel();
	}
	public static HomeRibbonModel getInstance() {
		return SingletonHolder.instance;
	}
	private HomeRibbonModel() {
		super( java.util.UUID.fromString( "808f41b6-c1e1-4c79-8d8f-de43900ae17b" ) );
	}
	@Override
	protected org.lgna.croquet.components.JComponent< ? > createMainComponent() {
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.weightx = 0.0;
		org.lgna.croquet.components.GridBagPanel rv = new org.lgna.croquet.components.GridBagPanel();
		rv.setBackgroundColor(java.awt.SystemColor.control);
		rv.setBorder( BorderFactory.createEmptyBorder( 4,2,4,2 ) );

		org.lgna.croquet.components.Button button;
		javax.swing.JButton jButton;

//		LineAxisPanel panel = new LineAxisPanel();
//		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		rv.addComponent(panel);

		jButton = new javax.swing.JButton("Paste");
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-paste.png") ));
		rv.addComponent(new org.lgna.croquet.components.SwingAdapter(jButton), gbc);
		
		org.lgna.croquet.components.GridPanel panel2 = org.lgna.croquet.components.GridPanel.createSingleColumnGridPane();
		rv.addComponent(panel2, gbc);
		
		jButton = new JButton("Copy");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-copy.png") ));
		jButton.setHorizontalAlignment( SwingConstants.LEADING );
		
		panel2.addComponent(new org.lgna.croquet.components.SwingAdapter(jButton));
		
		jButton = new JButton("Cut");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-cut.png") ));
		jButton.setHorizontalAlignment( SwingConstants.LEADING );
		panel2.addComponent(new org.lgna.croquet.components.SwingAdapter(jButton));
		
		rv.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 6 ), gbc );

//		panel = new LineAxisPanel();
//		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
//		rv.addComponent(panel);
		
		button = uist.ecard.DeletePictureModel.getInstance().createButton();
		jButton = button.getAwtComponent();
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		rv.addComponent(button, gbc);
		
		panel2 = org.lgna.croquet.components.GridPanel.createSingleColumnGridPane();
		rv.addComponent(panel2, gbc);
		
		jButton = new JButton("Select All");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-select-all.png") ));
		jButton.setHorizontalAlignment( SwingConstants.LEADING );
		panel2.addComponent(new org.lgna.croquet.components.SwingAdapter(jButton));
		
		jButton = new JButton("Find");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-find.png") ));
		jButton.setHorizontalAlignment( SwingConstants.LEADING );
		panel2.addComponent(new org.lgna.croquet.components.SwingAdapter(jButton));
		
		gbc.weightx = 1.0;
		rv.addComponent( org.lgna.croquet.components.BoxUtilities.createHorizontalGlue(), gbc  );
		return rv;
	}
}