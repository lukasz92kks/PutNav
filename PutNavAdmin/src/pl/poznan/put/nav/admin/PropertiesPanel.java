package pl.poznan.put.nav.admin;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -128116218890100985L;

	public PropertiesPanel() {
		this.setLayout(new GridLayout());
		
		JButton saveButton = new JButton("Ok");
		JButton cancelButton = new JButton("Cancel");
		
		this.add(saveButton);
		this.add(cancelButton);
	}
}
