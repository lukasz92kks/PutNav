package pl.putnav.webservice.dao.hibernateimplementation;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Łukasz
 */
public abstract class AbstractDao<PK extends Serializable, T> {
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private final Class<T> persistentClass;
    
    public AbstractDao() {
        persistentClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }
    
    public T getByKey(PK key) {
        return (T) getSession().get(persistentClass, key);
    }
    
    public void persist(T entity) {
        getSession().persist(entity);
    }
    
    public void delete(T entity) {
        getSession().delete(entity);
    }
    
    protected Session getSession() {
        return sessionFactory.getCurrentSession();
    }
    
    protected Criteria createEntityCriteria() {
        return getSession().createCriteria(persistentClass);
    }
}
