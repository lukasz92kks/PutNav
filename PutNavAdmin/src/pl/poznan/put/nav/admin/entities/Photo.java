package pl.poznan.put.nav.admin.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name = "Photos")
@Table(name = "Photos")
@TableGenerator(name="generator", initialValue=100, allocationSize=1)
public class Photo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@Column(name = "Id", columnDefinition="INTEGER")
	private int id;
	@Column(name = "FileName")
	private String file;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}
	private Building building;
}
