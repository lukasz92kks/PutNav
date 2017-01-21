package pl.putnav.webservice.entities.containers;

import java.util.ArrayList;
import java.util.Collection;
import pl.putnav.webservice.entities.SoftwareEntity;

/**
 *
 * @author Łukasz
 */
public class SoftwaresList extends ArrayList<SoftwareEntity> {
    
    public SoftwaresList() {
        
    }
    
    public SoftwaresList(Collection<? extends SoftwareEntity> list) {
        super(list);
    }
}
