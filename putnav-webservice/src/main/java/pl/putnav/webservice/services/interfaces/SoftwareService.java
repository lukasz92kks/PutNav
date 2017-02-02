package pl.putnav.webservice.services.interfaces;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pl.putnav.webservice.entities.SoftwareEntity;
import pl.putnav.webservice.entities.containers.SoftwaresList;
import pl.putnav.webservice.exceptions.EntityNotFoundException;

/**
 *
 * @author Łukasz
 */
public interface SoftwareService {
    
    SoftwareEntity findById(long id) throws EntityNotFoundException;
    
    SoftwaresList findAll(Pageable pageable);
    
    SoftwareEntity create(SoftwareEntity software);
    
    SoftwareEntity update(SoftwareEntity software) throws EntityNotFoundException;
    
    SoftwareEntity delete(long id) throws EntityNotFoundException;

    void saveFile(MultipartFile file) throws EntityNotFoundException;
}
