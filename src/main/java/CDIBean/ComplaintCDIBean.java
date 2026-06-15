/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import EJB.AdminBeanLocal;
import Entity.ComplaintCategory;
import Entity.Society;
//import Resources.RestClient;
import jakarta.ejb.EJB;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Named(value = "complaintCDIBean")
@ViewScoped
public class ComplaintCDIBean implements Serializable{

    Integer ward_id;
    String title;
    String description;
    List<Society> socities;
    List<ComplaintCategory> categories;
    String priority;
    Integer categoryId;
    Integer societyId;
    
    @EJB
    AdminBeanLocal adminService;
    
//    RestClient client=new RestClient();
//    Response rs;
    
    // Getting Scoiety of that Ward
    public void loadSociety(){
//        rs=client.decodeQRCode(Response.class, String.valueOf(ward_id));
//        socities=rs.readEntity(new GenericType<List<Society>>(){});  
          socities=adminService.getAllSocities(ward_id);
    }
    
//    public void submitComplaint(){
//        client.createComplaint(String.valueOf(1), String.valueOf(categoryId), String.valueOf(societyId),String.valueOf(ward_id), title, description, "ACTIVE", priority);
//    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public List<ComplaintCategory> getCategories() {
        return adminService.getAllCategory();
    }

    public void setCategories(List<ComplaintCategory> categories) {
        this.categories = categories;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

//    public RestClient getClient() {
//        return client;
//    }
//
//    public void setClient(RestClient client) {
//        this.client = client;
//    }
    
   
    public ComplaintCDIBean() {
    }

    public Integer getWard_id() {
        return ward_id;
    }

    public void setWard_id(Integer ward_id) {
        this.ward_id = ward_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Society> getSocities() {
        return socities;
    }

    public void setSocities(List<Society> socities) {
        this.socities = socities;
    }
    
}
