package pl.putnav.webservice.entities;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import pl.putnav.webservice.modelbase.SoftwareBase;

/**
 *
 * @author Łukasz
 */
@Entity
@Table(name = "Softwares")
public class SoftwareEntity extends SoftwareBase implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
