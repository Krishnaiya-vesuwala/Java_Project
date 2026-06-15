package EJB;

import Entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class MasterDataService {

    @PersistenceContext(unitName="jpu1")
    private EntityManager em;

   public List<Zone> getZones(Integer corpId){

    return em.createQuery(
        "SELECT z FROM Zone z " +
        "WHERE z.corporationId.corporationId=:corpId",
        Zone.class)
        .setParameter("corpId", corpId)
        .getResultList();
}

  public List<Ward> getWards(Integer corpId){

    return em.createQuery(
        "SELECT w FROM Ward w " +
        "WHERE w.zoneId.corporationId.corporationId=:corpId",
        Ward.class)
        .setParameter("corpId", corpId)
        .getResultList();
}

    public List<ComplaintCategory> getCategories() {
        return em.createQuery(
                "SELECT c FROM ComplaintCategory c",
                ComplaintCategory.class)
                .getResultList();
    }
    public List<Departments> getAllDepartments(){
        return em.createQuery(
                "SELECT d FROM Departments d",
                Departments.class)
                .getResultList();
    }
}