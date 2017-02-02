package pl.putnav.webservice.dto.software;

import pl.putnav.webservice.modelbase.SoftwareBase;

/**
 *
 * @author Łukasz
 */
public class SoftwareDTO extends SoftwareBase {
    
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
