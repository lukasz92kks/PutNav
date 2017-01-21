package pl.putnav.webservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.putnav.webservice.dao.interfaces.SoftwareDao;
import pl.putnav.webservice.entities.SoftwareEntity;

/**
 *
 * @author Łukasz
 */
@RestController
@RequestMapping("/api/softwares")
@Transactional
public class SoftwareController {
    
    @Autowired
    private SoftwareDao softwareDao;
    
    @GetMapping(value = "{id}")
    public String findById(@PathVariable String id) {
        SoftwareEntity software = new SoftwareEntity();
        software.setVersion("3.0");
        software.setDescription("test");
        software = softwareDao.save(software);
        return id;
    }
}
