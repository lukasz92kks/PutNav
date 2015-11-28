package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.MapPointTypes;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -128116218890100985L;
	
	private MapPoint activeMapPoint = null;
	private JTextField xTextField;
	private JTextField yTextField;
	private JComboBox<String> mapPointTypesComboBox;

	public PropertiesPanel() {
		this.setPreferredSize(new Dimension(230, 100));
		
		JLabel xLabel = new JLabel("X: ");
		xTextField = new JTextField();
		xTextField.setPreferredSize(new Dimension(85,  25));
		JPanel xPanel = new JPanel();
		xPanel.setLayout(new BoxLayout(xPanel, BoxLayout.LINE_AXIS));
		xPanel.add(xLabel);
		xPanel.add(xTextField);
		
		JLabel yLabel = new JLabel("Y: ");
		yTextField = new JTextField();
		yTextField.setPreferredSize(new Dimension(85,  25));
		JPanel yPanel = new JPanel();
		yPanel.setLayout(new BoxLayout(yPanel, BoxLayout.LINE_AXIS));
		yPanel.add(yLabel);
		yPanel.add(yTextField);
		
		JLabel typeLabel = new JLabel("Typ punktu: ");
		mapPointTypesComboBox = new JComboBox<String>(MapPointTypes.nameList);
		mapPointTypesComboBox.setPreferredSize(new Dimension(130, 25));
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.LINE_AXIS));
		typePanel.add(typeLabel);
		typePanel.add(mapPointTypesComboBox);
		
		JButton saveButton = new JButton("Ok");
		saveButton.setPreferredSize(new Dimension(100, 25));
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setPreferredSize(new Dimension(100, 25));
		
		this.add(xPanel);
		this.add(yPanel);
		this.add(typePanel);
		this.add(saveButton);
		this.add(cancelButton);
	}
	
	public void setActiveMapPoint(MapPoint point) {
		if(point != null) {
			this.activeMapPoint = point;
			loadMapPointProperties(activeMapPoint);
		} else {
			clearMapPointProperties();
		}
	}
	
	private void loadMapPointProperties(MapPoint mapPoint) {
		xTextField.setText(String.valueOf(mapPoint.getX()));
		yTextField.setText(String.valueOf(mapPoint.getY()));
		mapPointTypesComboBox.setSelectedIndex(mapPoint.getType()-1);
	}
	
	private void clearMapPointProperties() {
		xTextField.setText("");
		yTextField.setText("");
		mapPointTypesComboBox.setSelectedIndex(0);
	}
}
