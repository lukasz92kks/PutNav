package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import pl.poznan.put.nav.admin.entities.Photo;
import pl.poznan.put.nav.admin.entities.Room;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.EntitiesManager;

public class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -128116218890100985L;
	
	private MapPoint activeMapPoint = null;
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
	private EntitiesManager em;

	public PropertiesPanel() {
		System.out.println("PropertiesPanel");
		this.setPreferredSize(new Dimension(230, 100));
		
		
		this.add(createMapBox());
		this.add(createBuildingBox());
		this.add(createPointBox());
		this.add(createRoomBox());
	}
	
	public void loadData() {
		em = AppFactory.getEntitiesManager();
		buildings = em.getBuildings();
		departments = em.getDepartments();
		maps = em.getMaps();
	}
	
	private JPanel createMapBox() {
		JButton campusButton = new JButton("Widok kampusu");
		campusButton.setPreferredSize(new Dimension(200, 25));
		campusButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(maps != null)
					for(Map map : maps) {
						if(map.isCampus()) {
							em.setActiveMap(map);
							em.setActiveBuilding(null);
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
						em.setActiveBuilding(b);
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
				JComboBox<?> comboBox = (JComboBox<?>) event.getSource();
				if(maps != null)
				for(Map map : maps) {
					if(((String)comboBox.getSelectedItem()).equals(map.getMapFile())) {
						em.setActiveMap(map);
						
						floorTextField.setText(Integer.toString(map.getFloor()));
					
						if(!map.isCampus()) {
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
		
		JButton departmentsButton = new JButton("Wydzialy...");
		departmentsButton.setPreferredSize(new Dimension(200, 25));
		departmentsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(em == null)
					loadData();
				if(em.getActiveMap() != null) {
					Building building = null;
					if(em.getActiveBuilding() != null) {
						building = em.getActiveBuilding();
					} else {
						building = new Building();
						em.setActiveBuilding(building);
					}
					BuildingDepartmentsPanel panel = new BuildingDepartmentsPanel();
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
				if(em == null)
					loadData();
				if(em.getActiveMap() != null) {
					Building building = null;
					if(em.getActiveBuilding() != null) {
						building = em.getActiveBuilding();
					} else {
						building = new Building();
						em.setActiveBuilding(building);
					}
					BuildingPhotosPanel panel = new BuildingPhotosPanel();
					int result = JOptionPane.showConfirmDialog(null, panel, 
				       "Zdjecia", JOptionPane.OK_CANCEL_OPTION, 1, new ImageIcon("images/building.png"));
					
					if(result == JOptionPane.YES_OPTION) {
						copyPhotosToTemp(panel.getPhotos());
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
				if(em == null)
					loadData();
				if(em.getActiveMap() != null) {
					Building building = null;
					if(em.getActiveBuilding() != null) {
						building = em.getActiveBuilding();
					} else {
						building = new Building();
						em.setActiveBuilding(building);
					}
					BuildingMapsPanel panel = new BuildingMapsPanel();
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
	
	private void copyPhotosToTemp(ArrayList<Photo> photos) {
		for(Photo photo : photos) {
			File temp = new File("temp");
			File source = new File(photo.getFile());
			File target = new File(temp + File.separator + "images" + File.separator + new File(photo.getFile()).getName());
			try {
				Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String newFile = target.getPath().substring(temp.getPath().length()+1);
			photo.setFile(newFile);
		}
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
			System.out.println(mapPoint.getId());
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
