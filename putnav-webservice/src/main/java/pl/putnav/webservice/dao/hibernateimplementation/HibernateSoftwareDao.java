package pl.putnav.webservice.dao.hibernateimplementation;

import org.apache.tomcat.jni.SSLContext;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import pl.putnav.webservice.dao.interfaces.SoftwareDao;
import pl.putnav.webservice.entities.SoftwareEntity;
import pl.putnav.webservice.entities.containers.SoftwaresList;

/**
 *
 * @author Łukasz
 */
@Repository
public class HibernateSoftwareDao extends AbstractDao<Long, SoftwareEntity> implements SoftwareDao {

    @Override
    public SoftwareEntity findById(long id) {
        SoftwareEntity software = getByKey(id);
        return software;
    }
    
    @Override
    public SoftwareEntity findActive() {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("active", true));
        SoftwareEntity software = (SoftwareEntity) criteria.uniqueResult();
        return software;
    }

    @Override
    public SoftwareEntity save(SoftwareEntity entity) {
        persist(entity);
        return entity;
    }

    @Override
    public void delete(int id) {
        Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("id", id));
        SoftwareEntity software = (SoftwareEntity) criteria.uniqueResult();
        delete(software);
    }

    @Override
    public SoftwaresList findAll() {
        Criteria criteria = createEntityCriteria().addOrder(Order.asc("id"));
        SoftwaresList list = new SoftwaresList(criteria.list());
        return list;
    }
}
