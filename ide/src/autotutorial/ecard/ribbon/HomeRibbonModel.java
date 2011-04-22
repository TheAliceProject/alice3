package autotutorial.ecard.ribbon;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import edu.cmu.cs.dennisc.croquet.LineAxisPanel;
import edu.cmu.cs.dennisc.croquet.PageAxisPanel;

public class HomeRibbonModel extends edu.cmu.cs.dennisc.croquet.PredeterminedTab {
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
	protected edu.cmu.cs.dennisc.croquet.JComponent< ? > createMainComponent() {
		edu.cmu.cs.dennisc.croquet.LineAxisPanel rv = new edu.cmu.cs.dennisc.croquet.LineAxisPanel();
		rv.setBackgroundColor(java.awt.SystemColor.control);

		edu.cmu.cs.dennisc.croquet.Button button;
		javax.swing.JButton jButton;

		LineAxisPanel panel = new LineAxisPanel();
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rv.addComponent(panel);

		jButton = new javax.swing.JButton("Paste");
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-paste.png") ));
		panel.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(jButton));
		
		edu.cmu.cs.dennisc.croquet.PageAxisPanel panel2 = new PageAxisPanel();
		panel.addComponent(panel2);
		
		jButton = new JButton("Copy");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-copy.png") ));
		panel2.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(jButton));
		
		jButton = new JButton("Cut");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-cut.png") ));
		panel2.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(jButton));
		
		rv.addComponent( edu.cmu.cs.dennisc.croquet.BoxUtilities.createHorizontalSliver( 6 ) );
		panel = new LineAxisPanel();
		panel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		rv.addComponent(panel);
		
		button = autotutorial.ecard.DeletePictureModel.getInstance().createButton();
		jButton = button.getAwtComponent();
		jButton.setHorizontalTextPosition(SwingConstants.CENTER);
		jButton.setVerticalTextPosition(SwingConstants.BOTTOM);
		panel.addComponent(button);
		
		panel2 = new PageAxisPanel();
		panel.addComponent(panel2);
		
		jButton = new JButton("Select All");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-select-all.png") ));
		panel2.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(jButton));
		
		jButton = new JButton("Find");
		jButton.setIcon(new ImageIcon(getClass().getResource("/autotutorial/ecard/resources/ribbon/edit-find.png") ));
		panel2.addComponent(new edu.cmu.cs.dennisc.croquet.SwingAdapter(jButton));
		
		
		return rv;
	}
}