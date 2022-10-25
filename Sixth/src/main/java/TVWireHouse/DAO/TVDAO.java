package TVWireHouse.DAO;

import TVWireHouse.Entities.TV;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class TVDAO {
    private SessionFactory session;

    public void add(TV tv) {
        Transaction addTransaction = null;
        try (Session addSession = this.session.openSession()) {
            addTransaction = addSession.beginTransaction();
            addSession.save(tv);
            addTransaction.commit();
            addSession.close();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            if (addTransaction != null) {
                addTransaction.rollback();
            }
        }
    }

    public void update(TV tv){
        Transaction updateTransaction = null;
        try (Session updateSession = this.session.openSession()) {
            updateTransaction = updateSession.beginTransaction();
            updateSession.update(tv);
            updateTransaction.commit();
            updateSession.close();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            if (updateTransaction != null) {
                updateTransaction.rollback();
            }
        }
    }

    public boolean delete(int id){
        Transaction deleteTransaction = null;
        TV tv;
        try (Session deleteSession = this.session.openSession()) {
            deleteTransaction = deleteSession.beginTransaction();
            tv = this.findById(id);
            if(tv == null){
                deleteTransaction.commit();
                deleteSession.close();
                return false;
            }
            deleteSession.delete(tv);
            deleteTransaction.commit();
            deleteSession.close();
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            if (deleteTransaction != null) {
                deleteTransaction.rollback();
            }
        }
        return true;
    }

    public TV findById(int id) {
        Transaction finByIdTransaction = null;
        TV tv;
        try (Session findByIdSession = this.session.openSession()) {
            finByIdTransaction = findByIdSession.beginTransaction();
            tv = findByIdSession.get(TV.class, id);
            if(tv == null){
                finByIdTransaction.commit();
                findByIdSession.close();
                return null;
            }
            finByIdTransaction.commit();
            findByIdSession.close();
            return tv;
        } catch (HibernateException hibernateException) {
            hibernateException.printStackTrace();
            if (finByIdTransaction != null) {
                finByIdTransaction.rollback();
            }
            return null;
        }
    }

    public List<TV> allTVs(){
        try (Session selectSession = this.session.openSession()){
            CriteriaBuilder criteriaBuilder = selectSession.getCriteriaBuilder();
            CriteriaQuery<TV> criteriaQuery = criteriaBuilder.createQuery(TV.class);
            Root<TV> tvRoot = criteriaQuery.from(TV.class);
            criteriaQuery.select(tvRoot).where();

            Query selectQuery = selectSession.createQuery(criteriaQuery);
            return (List<TV>) selectQuery.getResultList();
        }catch (HibernateException exception){
            exception.printStackTrace();
            return null;
        }
    }

    public SessionFactory getSession() {
        return session;
    }

    @Autowired
    public void setSession(SessionFactory session) {
        this.session = session;
    }
}
