package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Photos")
public class Photo {

    @DatabaseField(id = true, columnName = "Id")
    private int id;
    @DatabaseField(columnName = "FileName")
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
    @DatabaseField(foreign = true, columnName = "BUILDING_Id", foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private Building building;
}