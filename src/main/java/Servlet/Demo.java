/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.UserBeanLocal;
import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.Corporation;
import Entity.Departments;
import Entity.Officers;
import Entity.Society;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;
import jakarta.ejb.EJB;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author riya vesuwala

/**
 *
 * @author krishnaiya
 */
@WebServlet(name = "Demo", urlPatterns = {"/Demo"})
public class Demo extends HttpServlet {

    @EJB 
//    AdminBeanLocal adminBean;
    ComplaintBeanLocal complaintBean;
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @EJB
    private AdminBeanLocal adminBean;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Demo</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Demo at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        System.out.println(adminBean);
//        System.out.println(userBean);

//        Zone zone = new Zone();
//        zone.setZoneName("Udhna");
//        zone.setStatus("Active");
//
//        Corporation corp = new Corporation();
//        corp.setCorporationId(1);
//        zone.setCorporationId(corp);
//
//        adminBean.createZone(zone);
//        adminBean.updateZone(1, "Udhna Zone Updated", "Active", 1);
//        adminBean.deleteZone(3);  
        
//        Departments dept = new Departments();
//        dept.setDepartmentName("Water Department");
//        dept.setDescription("Handles water supply complaints");
//        dept.setStatus("Active");
//
//        adminBean.createDepartment(dept);
//        adminBean.updateDepartment(1,
//        "Water Supply Dept",
//        "Handles water leakage & supply",
//        "Active");
//        adminBean.deleteDepartment(5);
//        
//        Society society = new Society();
//        society.setSocietyName("Green Park Society");
//        society.setAddress("Udhna Surat");
//        society.setStatus("Active");
//
//        Ward ward = new Ward();
//        ward.setWardId(2);
//
//        society.setWardId(ward);
//        
//        adminBean.createSociety(society);
//        adminBean.updateSociety(4,
//        "Green Park Residency",
//        "Udhna Main Road",
//        "Active",
//        2);
//        adminBean.deleteSociety(4);
//        
//        ComplaintCategory category = new ComplaintCategory();
//        category.setCategoryName("Water Leakage");
//
//        Departments deptRef = new Departments();
//        deptRef.setDepartmentId(1);
//
//        category.setDepartmentId(deptRef);
//
//        adminBean.addCategory(category);
//        adminBean.updateCategory(1, "Water Pipe Leakage", 1);
//        adminBean.deleteCategory(3);
//
//        Officers officer = new Officers();
//
//        Users user = new Users();
//        user.setUserId(5);   // existing user id
//        officer.setUserId(user);
//
//        Departments deptRef2 = new Departments();
//        deptRef2.setDepartmentId(1);
//        officer.setDepartmentId(deptRef2);
//
//        Ward wardRef = new Ward();
//        wardRef.setWardId(2);
//        officer.setWardId(wardRef);
//
//        Zone zoneRef = new Zone();
//        zoneRef.setZoneId(3);
//        officer.setZoneId(zoneRef);
//
//        officer.setDesignation("Junior Officer");
//
//        adminBean.createOfficer(officer);
//        adminBean.updateOfficer(
//        1,  // officer id
//        5,  // user id
//        1,  // department id
//        2,  // ward id
//        1,  // zone id
//        "Senior Officer"
//        );
//        adminBean.deleteOfficer(6);
        
//        Users user=new Users();
//        
//        user.setFullName("ABC");
//        user.setUsername("ABC123");
//        user.setEmail("abc@gmail.com");
//        user.setMobile("9999999999");
//        user.setPassword("abc123");
//        user.setRole("Citizen");
//        
//        userBean.registerUser(user);

//        Users user=userBean.login("ABC123", "abc123");
//        Users user=userBean.login("admin", "admin123");
//        Users user=userBean.login("officer1", "officer123");
//        
//        if(user!=null)
//        {
//            if(user.getRole().equals("Admin"))
//            {
//                System.out.println("Admin");
//            }
//            else if(user.getRole().equals("Officer"))
//            {
//                System.out.println("Officer");
//            }
//            else if(user.getRole().equals("Citizen"))
//            {
//                System.out.println("Citizen");
//            }
//        }
//        else
//        {
//            System.out.println("Null");
//        }

//        Complaint complaint=new Complaint();
//        complaint.setDescription("Water Pipe Leakage");
//        complaint.setStatus("Pending");
//        
//        Users user=new Users();
//        user.setUserId(1);
//        complaint.setCitizenId(user);
//        
//        complaintBean.registerComplaint(complaint);

//        List<Complaint> list=complaintBean.getCitizenComplaints(1);
//        
//        if(list!=null)
//        {   
//            for(Complaint c:list)
//            {
//                System.out.println("Complaint Id : "+c.getComplaintId()+"<br/>");
//                System.out.println("Description : "+c.getDescription()+"<br/>");
//                System.out.println("Status : "+c.getStatus()+"<br/>");
//            }
//        }
//        else
//        {
//            System.out.println("Null");
//        }

//        Complaint latest=complaintBean.trackComplaint(1);
//        
//        if(latest!=null)
//        {
//            System.out.println("Latest Complaint Id : "+latest.getComplaintId()+"<br/>");
//            System.out.println("Description : "+latest.getDescription()+"<br/>");
//            System.out.println("Status : "+latest.getStatus()+"<br/>");
//        }
//        else
//        {
//            System.out.println("Null");
//        }
//        processRequest(request, response);
//          System.out.print(adminBean);
//          adminBean.createWard(2,"Udhna", "Active");
//          adminBean.updateWard(1, 1, "ABC", "inactive");
//          adminBean.deleteWard(1);
//          adminBean.createOfficer(1, 1, 1,1, "ABC");
//          adminBean.updateOfficer(1, 0, 0, 0, 0, "JJ");
            adminBean.deleteOfficer(1);
            

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
//        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
