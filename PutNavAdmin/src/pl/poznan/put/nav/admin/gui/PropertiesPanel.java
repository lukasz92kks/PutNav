package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.MapPointTypes;
import pl.poznan.put.nav.admin.managers.AppFactory;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -128116218890100985L;
	
	private MapPoint activeMapPoint = null;
	private JTextField xTextField;
	private JTextField yTextField;
	private JComboBox<String> mapPointTypesComboBox;
	private JComboBox<String> mapsComboBox;
	private ArrayList<Map> maps;

	public ArrayList<Map> getMaps() {
		return maps;
	}

	public JComboBox<String> getMapsComboBox() {
		return mapsComboBox;
	}

	public void setMapsComboBox(JComboBox<String> mapsComboBox) {
		this.mapsComboBox = mapsComboBox;
	}

	public void setMaps(ArrayList<Map> maps) {
		this.maps = maps;
	}

	public PropertiesPanel() {
		this.setPreferredSize(new Dimension(230, 100));
		
		JLabel mapLabel = new JLabel("Mapa: ");
		mapsComboBox = new JComboBox<String>();
		mapsComboBox.setPreferredSize(new Dimension(160, 25));
		mapsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MapPanel panel = AppFactory.getMapPanel();
				JComboBox comboBox = (JComboBox) event.getSource();
				for(Map map : maps) {
					if(((String)comboBox.getSelectedItem()).equals(map.getMapFile().getName()))
					panel.setMap(map);
				}
			}
		});
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.LINE_AXIS));
		mapsPanel.add(mapLabel);
		mapsPanel.add(mapsComboBox);
		
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
		
		this.add(mapsPanel);
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
	
	public void setMapsComboBoxList(ArrayList<String> list) {
		mapsComboBox.removeAllItems();
		for(String item : list) {
			mapsComboBox.addItem(item);
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
