package pl.putnav.webservice.services.implementations;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.putnav.webservice.dao.interfaces.SoftwareDao;
import pl.putnav.webservice.entities.SoftwareEntity;
import pl.putnav.webservice.entities.containers.SoftwaresList;
import pl.putnav.webservice.exceptions.EntityNotFoundException;
import pl.putnav.webservice.services.interfaces.SoftwareService;

/**
 *
 * @author Łukasz
 */
@Service
public class SoftwareServiceImpl implements SoftwareService {
    
    private final SoftwareDao softwareDao;
    
    @Autowired
    public SoftwareServiceImpl(SoftwareDao softwareDao) {
        this.softwareDao = softwareDao;
    }

    @Override
    public SoftwareEntity findById(long id) throws EntityNotFoundException {
        SoftwareEntity software = softwareDao.findById(id);
        if (software == null) {
            throw new EntityNotFoundException("Software entity identify by id #" + id + " not found");
        }
        return software;
    }

    @Override
    public SoftwaresList findAll(Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SoftwareEntity create(SoftwareEntity software) {
        software = softwareDao.save(software);
        return software;
    }

    @Override
    public SoftwareEntity update(SoftwareEntity software) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SoftwareEntity delete(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveFile(MultipartFile file) throws EntityNotFoundException {
        SoftwareEntity software = softwareDao.findById(1);
        if (software == null) {
            throw new EntityNotFoundException("Software entity identify by id #" + 1 + " not found");
        }
        try {
            software.setFile(file.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(SoftwareServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        softwareDao.save(software);
    }
}
