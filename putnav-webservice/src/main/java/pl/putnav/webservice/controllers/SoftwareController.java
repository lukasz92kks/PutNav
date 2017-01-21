package pl.putnav.webservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Łukasz
 */
@RestController
@RequestMapping("/api/softwares")
public class SoftwareController {
    
    @GetMapping(value = "{id}")
    public String findById(@PathVariable String id) {
        return id;
    }
}
