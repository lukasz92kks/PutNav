package pl.putnav.webservice.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.putnav.webservice.entities.SoftwareEntity;
import pl.putnav.webservice.exceptions.EntityNotFoundException;
import pl.putnav.webservice.services.interfaces.SoftwareService;

/**
 *
 * @author Łukasz
 */
@Controller
@RequestMapping("/softwares")
@Transactional
public class SoftwareController {
    
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SoftwareService softwareService;
    
    @GetMapping()
    public String uploadForm(Model model) {
        return "uploadForm";
    }
    
    @PostMapping("/file/new")
    public String uploadFile(@RequestParam String description, @RequestParam String version, @RequestParam Boolean active, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) throws IOException, EntityNotFoundException {
        SoftwareEntity software = new SoftwareEntity();
        software.setDescription(description);
        software.setVersion(version);
        software.setActive(active);
        software.setFile(file.getBytes());
        softwareService.create(software);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");
        return "redirect:/softwares";
    }
    
    @GetMapping(value = "/file/{id}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable long id) throws SQLException, EntityNotFoundException {
        SoftwareEntity software = softwareService.findById(id);
        ResponseEntity<InputStreamResource> response = fileToResponseEntity(software.getFile());
        return response;
    }
    
    @GetMapping(value = "/file")
    public ResponseEntity<InputStreamResource> downloadActiveFile() throws SQLException, EntityNotFoundException {
        SoftwareEntity software = softwareService.findActive();
        ResponseEntity<InputStreamResource> response = fileToResponseEntity(software.getFile());
        return response;
    }
    
    private ResponseEntity<InputStreamResource> fileToResponseEntity(byte[] file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "putnav.pna");
        
        InputStreamResource isr = new InputStreamResource(new ByteArrayInputStream(file));
        ResponseEntity<InputStreamResource> response = new ResponseEntity<>(isr, headers, HttpStatus.OK);
        return response;
    }
}
