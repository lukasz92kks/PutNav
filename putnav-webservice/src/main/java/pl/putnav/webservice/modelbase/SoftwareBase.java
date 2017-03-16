package pl.putnav.webservice.modelbase;

import javax.persistence.MappedSuperclass;

/**
 *
 * @author Łukasz
 */
@MappedSuperclass
public class SoftwareBase {
    
    private String version;
    private String description;
    private Boolean active;

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

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
