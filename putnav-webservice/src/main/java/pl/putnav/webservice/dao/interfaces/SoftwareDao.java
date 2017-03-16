package pl.putnav.webservice.dao.interfaces;

import pl.putnav.webservice.entities.SoftwareEntity;
import pl.putnav.webservice.entities.containers.SoftwaresList;

/**
 *
 * @author Łukasz
 */
public interface SoftwareDao {
    
    SoftwareEntity findById(long id);
    
    SoftwareEntity findActive();
    
    SoftwareEntity save(SoftwareEntity entity);
    
    void delete(int id);
    
    SoftwaresList findAll();
}
