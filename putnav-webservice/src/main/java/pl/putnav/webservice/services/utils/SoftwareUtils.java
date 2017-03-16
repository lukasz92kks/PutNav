package pl.putnav.webservice.services.utils;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.putnav.webservice.dao.interfaces.SoftwareDao;
import pl.putnav.webservice.entities.SoftwareEntity;

/**
 *
 * @author Łukasz
 */
@Component
public class SoftwareUtils {

    @Autowired
    private SoftwareDao dao;
    private static SoftwareDao softwareDao;
    
    @PostConstruct
    public void initDao() {
        softwareDao = dao;
    }

    public static void switchActiveSoftware(SoftwareEntity software) {
        for (SoftwareEntity s : softwareDao.findAll()) {
            if (s.equals(software)) {
                s.setActive(Boolean.TRUE);
            } else {
                s.setActive(Boolean.FALSE);
            }
            softwareDao.save(s);
        }
    }
}
