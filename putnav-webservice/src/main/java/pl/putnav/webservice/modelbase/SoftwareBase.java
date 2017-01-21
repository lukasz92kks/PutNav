package pl.putnav.webservice.modelbase;

/**
 *
 * @author Łukasz
 */
public class SoftwareBase {
    
    private String version;
    private String description;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
