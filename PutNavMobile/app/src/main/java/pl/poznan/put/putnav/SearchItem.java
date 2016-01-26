package pl.poznan.put.putnav;


import android.widget.TextView;

public class SearchItem {

    private int id;
    private String pointType;
    private String pointName;

    SearchItem(int id, String pointName, String pointType) {
        this.id = id;
        this.pointName = pointName;
        this.pointType = pointType;
    }

    public String getPointName() {
        return this.pointName;
    }

    public String getPointType() {
        return this.pointType;
    }

    public int getId() {
        return this.id;
    }

}
