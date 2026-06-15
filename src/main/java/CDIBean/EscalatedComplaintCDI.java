package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Complaint;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EscalatedComplaintCDI implements Serializable {

    @EJB
    AdminBeanLocal adminBean;

    private List<Complaint> allEscalatedComplaints;
    private List<Complaint> zoneEscalatedComplaints;
    private List<Complaint> corporateEscalatedComplaints;

    private Complaint selectedComplaint;

    @PostConstruct
    public void init() {
//
//        allEscalatedComplaints =
//                adminBean.getAllEscalatedComplaints();
//
//        zoneEscalatedComplaints =
//                adminBean.getZoneEscalatedComplaints();
//
//        corporateEscalatedComplaints =
//                adminBean.getCorporateEscalatedComplaints();
    }

//    public void showDetails(Complaint complaint) {
//
//        selectedComplaint =
//                adminBean.getComplaintDetails(
//                        complaint.getComplaintId());
//    }

    public List<Complaint> getAllEscalatedComplaints() {
        return allEscalatedComplaints;
    }

    public List<Complaint> getZoneEscalatedComplaints() {
        return zoneEscalatedComplaints;
    }

    public List<Complaint> getCorporateEscalatedComplaints() {
        return corporateEscalatedComplaints;
    }

    public Complaint getSelectedComplaint() {
        return selectedComplaint;
    }

    public void setSelectedComplaint(Complaint selectedComplaint) {
        this.selectedComplaint = selectedComplaint;
    }
    
}