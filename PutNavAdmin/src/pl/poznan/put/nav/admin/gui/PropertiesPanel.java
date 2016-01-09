package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.MapPointTypes;
import pl.poznan.put.nav.admin.entities.Room;
import pl.poznan.put.nav.admin.managers.AppFactory;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -128116218890100985L;
	
	private MapPoint activeMapPoint = null;
	private Map activeMap = null;
	private JTextField floorTextField;
	private JTextField xTextField;
	private JTextField yTextField;
	private JTextField buildingNameTextField;
	private JTextField addressTextField;
	private JTextField numOfFloorsTextField;
	private JTextField roomNameTextField;
	private JTextField functionTextField;
	private JComboBox<String> buildingsComboBox;
	private JComboBox<String> mapPointTypesComboBox;
	private JComboBox<String> mapsComboBox;
	private List<Building> buildings;
	private List<Department> departments;

	private List<Map> maps;

	public PropertiesPanel() {
		this.setPreferredSize(new Dimension(230, 100));
		
		this.add(createMapBox());
		this.add(createBuildingBox());
		this.add(createPointBox());
		this.add(createRoomBox());
	}
	
	private JPanel createMapBox() {
		JButton campusButton = new JButton("Widok kampusu");
		campusButton.setPreferredSize(new Dimension(200, 25));
		campusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(maps != null)
					for(Map map : maps) {
						if(map.getBuilding() == null) {
							ActionsPanel actionsPanel = AppFactory.getActionsPanel();
							actionsPanel.setActiveMap(map);
							MapPanel mapPanel = AppFactory.getMapPanel();
							mapPanel.setMap(map);
							clearBuildingProperties();
							setEmptyComboBoxes();
						}
				}
			}
		});
		JPanel campusPanel = new JPanel();
		campusPanel.setBorder(new EmptyBorder(5, 0, 2, 0));
		campusPanel.add(campusButton);
		
		JLabel buildingLabel = new JLabel("Budynek: ");
		buildingsComboBox = new JComboBox<String>();
		buildingsComboBox.setPreferredSize(new Dimension(150, 25));
		buildingsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JComboBox<?> comboBox = (JComboBox<?>) event.getSource();
				for(Building b : buildings) {
					if(comboBox.getSelectedItem().equals(b.getName())) {
						ArrayList<String> names = new ArrayList<String>();
						for(Map map : b.getMaps())
							names.add(map.getMapFile());
						setMapsComboBoxList(names);
					}
				}
			}
		});
		JPanel buildingsPanel = new JPanel();
		buildingsPanel.setLayout(new BoxLayout(buildingsPanel, BoxLayout.LINE_AXIS));
		buildingsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		buildingsPanel.add(buildingLabel);
		buildingsPanel.add(buildingsComboBox);
		
		JLabel mapLabel = new JLabel("Mapa: ");
		mapsComboBox = new JComboBox<String>();
		mapsComboBox.setPreferredSize(new Dimension(167, 25));
		mapsComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MapPanel mapPanel = AppFactory.getMapPanel();
				JComboBox<?> comboBox = (JComboBox<?>) event.getSource();
				for(Map map : maps) {
					if(((String)comboBox.getSelectedItem()).equals(map.getMapFile())) {
						ActionsPanel actionsPanel = AppFactory.getActionsPanel();
						actionsPanel.setActiveMap(map);
						mapPanel.setMap(map);
						
						floorTextField.setText(Integer.toString(map.getFloor()));
					
						if(map.getBuilding() != null) {
							loadBuildingProperties(map.getBuilding());
						} else {
							clearBuildingProperties();
						}
					}
				}
			}
		});
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout(new BoxLayout(mapsPanel, BoxLayout.LINE_AXIS));
		mapsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		mapsPanel.add(mapLabel);
		mapsPanel.add(mapsComboBox);
		
		JLabel floorLabel = new JLabel("Numer pietra: ");
		floorTextField = new JTextField(3);
		JPanel floorPanel = new JPanel();
		floorPanel.setLayout(new BoxLayout(floorPanel, BoxLayout.LINE_AXIS));
		floorPanel.setBorder(new EmptyBorder(2, 2, 5, 2));
		floorPanel.add(floorLabel);
		floorPanel.add(floorTextField);
		
		JPanel mapBox = new JPanel();
		mapBox.setPreferredSize(new Dimension(220, 150));
		mapBox.setLayout(new BoxLayout(mapBox, BoxLayout.Y_AXIS));
		mapBox.setBorder(BorderFactory.createTitledBorder("Wybor mapy"));
		mapBox.add(campusPanel);
		mapBox.add(buildingsPanel);
		mapBox.add(mapsPanel);
		mapBox.add(floorPanel);
		
		return mapBox;
	}
	
	public JPanel createBuildingBox() {
		JLabel nameLabel = new JLabel("Nazwa: ");
		buildingNameTextField = new JTextField(12);
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
		namePanel.setBorder(new EmptyBorder(5, 2, 2, 2));
		namePanel.add(nameLabel);
		namePanel.add(buildingNameTextField);
		
		JLabel addressLabel = new JLabel("Adres: ");
		addressTextField = new JTextField(12);
		JPanel addressPanel = new JPanel();
		addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.LINE_AXIS));
		addressPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		addressPanel.add(addressLabel);
		addressPanel.add(addressTextField);
		
		JLabel floorsLabel = new JLabel("Liczba pieter: ");
		numOfFloorsTextField = new JTextField(3);
		JPanel floorsPanel = new JPanel();
		floorsPanel.setLayout(new BoxLayout(floorsPanel, BoxLayout.LINE_AXIS));
		floorsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		floorsPanel.add(floorsLabel);
		floorsPanel.add(numOfFloorsTextField);
		
		JButton departmentsButton = new JButton("Wydzia³y...");
		departmentsButton.setPreferredSize(new Dimension(200, 25));
		departmentsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MapPanel mapPanel = AppFactory.getMapPanel();
				if(mapPanel.getMap() != null && mapPanel.getMap().getBuilding() != null) {
					Building building = mapPanel.getMap().getBuilding();
					BuildingDepartmentsPanel panel = new BuildingDepartmentsPanel(building, departments);
					int result = JOptionPane.showConfirmDialog(null, panel, 
				       "Wydzialy", JOptionPane.OK_CANCEL_OPTION, 1, new ImageIcon("images/building.png"));
					
					if(result == JOptionPane.YES_OPTION) {
						building.setDepartments(panel.getIncludedDepartments());
					}
				}
			}
		});
		JButton photosButton = new JButton("Zdjecia...");
		photosButton.setPreferredSize(new Dimension(200, 25));
		photosButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MapPanel mapPanel = AppFactory.getMapPanel();
				if(mapPanel.getMap() != null && mapPanel.getMap().getBuilding() != null) {
					Building building = mapPanel.getMap().getBuilding();
					BuildingPhotosPanel panel = new BuildingPhotosPanel(building);
					int result = JOptionPane.showConfirmDialog(null, panel, 
				       "Zdjecia", JOptionPane.OK_CANCEL_OPTION, 1, new ImageIcon("images/building.png"));
					
					if(result == JOptionPane.YES_OPTION) {
						building.setPhotos(panel.getPhotos());
					}
				}
			}
		});
		JButton mapsButton = new JButton("Mapy...");
		mapsButton.setPreferredSize(new Dimension(200, 25));
		mapsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				MapPanel mapPanel = AppFactory.getMapPanel();
				if(mapPanel.getMap() != null && mapPanel.getMap().getBuilding() != null) {
					Building building = mapPanel.getMap().getBuilding();
					BuildingMapsPanel panel = new BuildingMapsPanel(building, maps);
					int result = JOptionPane.showConfirmDialog(null, panel, 
				       "Mapy", JOptionPane.OK_CANCEL_OPTION, 1, new ImageIcon("images/building.png"));
					
					if(result == JOptionPane.YES_OPTION) {
						building.setMaps(panel.getIncludedMaps());
					}
				}
			}
		});
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setPreferredSize(new Dimension(220, 110));
		//buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
		buttonsPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		buttonsPanel.add(departmentsButton);
		buttonsPanel.add(photosButton);
		buttonsPanel.add(mapsButton);
		
		JPanel buildingBox = new JPanel();
		buildingBox.setPreferredSize(new Dimension(220, 205));
		buildingBox.setLayout(new BoxLayout(buildingBox, BoxLayout.Y_AXIS));
		buildingBox.setBorder(new CompoundBorder(new EmptyBorder(10,0,0,0), 
				BorderFactory.createTitledBorder("Parametry budynku")));
		buildingBox.add(namePanel);
		buildingBox.add(addressPanel);
		buildingBox.add(floorsPanel);
		buildingBox.add(buttonsPanel);
		
		return buildingBox;
	}
	
	public JPanel createRoomBox() {
		JLabel nameLabel = new JLabel("Nazwa: ");
		roomNameTextField = new JTextField(14);
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.LINE_AXIS));
		namePanel.setBorder(new EmptyBorder(5, 2, 2, 2));
		namePanel.add(nameLabel);
		namePanel.add(roomNameTextField);

		JLabel functionLabel = new JLabel("Funkcja: ");
		functionTextField = new JTextField(14);
		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.LINE_AXIS));
		functionPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
		functionPanel.add(functionLabel);
		functionPanel.add(functionTextField);
		
		JPanel roomBox = new JPanel();
		roomBox.setPreferredSize(new Dimension(220, 85));
		roomBox.setLayout(new BoxLayout(roomBox, BoxLayout.Y_AXIS));
		roomBox.setBorder(new CompoundBorder(new EmptyBorder(10,0,0,0), 
				BorderFactory.createTitledBorder("Parametry pomieszczenia")));
		roomBox.add(namePanel);
		roomBox.add(functionPanel);
		
		return roomBox;
	}

	private JPanel createPointBox() {
		JLabel xLabel = new JLabel("X: ");
		xTextField = new JTextField();
		xTextField.setPreferredSize(new Dimension(85,  25));
		JLabel yLabel = new JLabel("Y: ");
		yTextField = new JTextField();
		yTextField.setPreferredSize(new Dimension(85,  25));
		JPanel coordsPanel = new JPanel();
		coordsPanel.setLayout(new BoxLayout(coordsPanel, BoxLayout.LINE_AXIS));
		coordsPanel.setBorder(new EmptyBorder(5, 2, 2, 2));
		coordsPanel.add(xLabel);
		coordsPanel.add(xTextField);
		coordsPanel.add(yLabel);
		coordsPanel.add(yTextField);
		
		JLabel typeLabel = new JLabel("Typ punktu: ");
		mapPointTypesComboBox = new JComboBox<String>(MapPointTypes.nameList);
		mapPointTypesComboBox.setPreferredSize(new Dimension(130, 25));
		JPanel typePanel = new JPanel();
		typePanel.setLayout(new BoxLayout(typePanel, BoxLayout.LINE_AXIS));
		typePanel.setBorder(new EmptyBorder(2, 2, 5, 2));
		typePanel.add(typeLabel);
		typePanel.add(mapPointTypesComboBox);
		
		JPanel pointBox = new JPanel();
		pointBox.setPreferredSize(new Dimension(220, 95));
		pointBox.setLayout(new BoxLayout(pointBox, BoxLayout.Y_AXIS));
		pointBox.setBorder(new CompoundBorder(new EmptyBorder(10,0,0,0), 
				BorderFactory.createTitledBorder("Parametry punktu")));
		pointBox.add(coordsPanel);
		pointBox.add(typePanel);
		
		return pointBox;
	}
	
	public void setActiveMapPoint(MapPoint point) {
		if(point != null) {
			this.activeMapPoint = point;
			loadMapPointProperties(activeMapPoint);
			if(point.getType() == MapPointTypes.ROOM) {
				loadRoomProperties(point.getRoom());
			} else {
				clearRoomProperties();
			}
			
		} else {
			clearMapPointProperties();
		}
	}
	
	public void setBuildingsComboBoxList(ArrayList<String> list) {
		clearBuildingsComboBox();
		
		for(String item : list) {
			buildingsComboBox.addItem(item);
		}
		buildingsComboBox.addItem("");
	}
	
	public void setMapsComboBoxList(ArrayList<String> list) {
		clearMapsComboBox();
		
		for(String item : list) {
			mapsComboBox.addItem(item);
		}
		mapsComboBox.addItem("");
	}
	
	private void loadMapPointProperties(MapPoint mapPoint) {
		if(mapPoint != null) {
			xTextField.setText(String.valueOf(mapPoint.getX()));
			yTextField.setText(String.valueOf(mapPoint.getY()));
			mapPointTypesComboBox.setSelectedIndex(mapPoint.getType()-1);
		}
	}
	
	private void loadBuildingProperties(Building building) {
		if(building != null) {
			buildingNameTextField.setText(building.getName());
			addressTextField.setText(building.getAddress());
			numOfFloorsTextField.setText(Integer.toString(building.getNumberOfFloors()));
		}
	}
	
	private void loadRoomProperties(Room room) {
		if(room != null) {
			roomNameTextField.setText(room.getName());
			functionTextField.setText(room.getFunction());
		}
	}
	
	private void clearMapPointProperties() {
		xTextField.setText("");
		yTextField.setText("");
		mapPointTypesComboBox.setSelectedIndex(0);
	}
	
	private void clearBuildingProperties() {
		buildingNameTextField.setText("");
		addressTextField.setText("");
		numOfFloorsTextField.setText("");
	}
	
	private void clearRoomProperties() {
		roomNameTextField.setText("");
		functionTextField.setText("");
	}
	
	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	public List<Map> getMaps() {
		return maps;
	}
	
	public JComboBox<String> getBuildingsComboBox() {
		return buildingsComboBox;
	}

	public void setBuildingsComboBox(JComboBox<String> buildingsComboBox) {
		this.buildingsComboBox = buildingsComboBox;
	}
	
	private void clearBuildingsComboBox() {
		if(buildingsComboBox.getItemCount() > 0)
			buildingsComboBox.setModel(new DefaultComboBoxModel<String>());
	}

	public JComboBox<String> getMapsComboBox() {
		return mapsComboBox;
	}

	public void setMapsComboBox(JComboBox<String> mapsComboBox) {
		this.mapsComboBox = mapsComboBox;
	}
	
	private void clearMapsComboBox() {
		if(mapsComboBox.getItemCount() > 0)
			mapsComboBox.setModel(new DefaultComboBoxModel<String>());
	}
	
	private void setEmptyComboBoxes() {
		buildingsComboBox.setSelectedIndex(buildingsComboBox.getItemCount()-1);
		mapsComboBox.setSelectedIndex(mapsComboBox.getItemCount()-1);
	}

	public void setMaps(List<Map> maps) {
		this.maps = maps;
	}
	
	public JTextField getFloorTextField() {
		return floorTextField;
	}

	public JTextField getBuildingNameTextField() {
		return buildingNameTextField;
	}

	public JTextField getAddressTextField() {
		return addressTextField;
	}

	public JTextField getNumOfFloorsTextField() {
		return numOfFloorsTextField;
	}

	public JTextField getRoomNameTextField() {
		return roomNameTextField;
	}

	public JTextField getFunctionTextField() {
		return functionTextField;
	}
	
	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
}
