/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Servlet;

import EJB.AdminBeanLocal;
import EJB.ComplaintBean;
import EJB.ComplaintBeanLocal;
import EJB.OfficerBeanLocal;
import EJB.QRCodeManager;
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
import static java.lang.System.out;
import java.util.List;

/**
 *
 * @author riya vesuwala
 *
 * /**
 *
 * @author krishnaiya
 */
@WebServlet(name = "Demo", urlPatterns = {"/Demo"})
public class Demo extends HttpServlet {

    @EJB
    private AdminBeanLocal adminBean;
    @EJB
    private ComplaintBeanLocal complaintBean;
    @EJB
    private OfficerBeanLocal officerBean;
    @EJB
    private UserBeanLocal userBean;

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

//        adminBean.createZone("Udhna Zone","Active",1);
//        adminBean.updateZone(1, "Udhna Zone Updated", "Active", 1);
//        adminBean.deleteZone(3);


//        adminBean.createDepartment("Water Department","Handles water complaints","Active");
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


//        adminBean.createSociety(2,"Green Park Society","Udhna Surat","Active");
//        adminBean.updateSociety(4,
//        "Green Park Residency",
//        "Udhna Main Road",
//        "Active",
//        2);
//        adminBean.deleteSociety(4);
//        
//        adminBean.addCategory(category);
//        adminBean.createCategory("Water Leakage",1);
//        adminBean.updateCategory(1, "Water Pipe Leakage", 1);
//        adminBean.deleteCategory(3);
//
//  
//        adminBean.createOfficer(
//                    5,          // existing user_id
//                    1,          // department_id
//                    3,          // zone_id
//                    2,          // ward_id
//                    "Junior Officer"
//        );
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
//        user.setMobile("7383181453");
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
//       
//          processRequest(request, response);
//          System.out.print(adminBean);
//          adminBean.createWard(2,"Udhna", "Active");
//          adminBean.updateWard(1, 1, "ABC", "inactive");
//          adminBean.deleteWard(1);
//          adminBean.createOfficer(1, 1, 2,1, "ZONE");
//          adminBean.createOfficer(1, 1, 2,1, "WARD");
//          adminBean.updateOfficer(1, 0, 0, 0, 0, "JJ");
//          adminBean.deleteOfficer(1);
//        complaintBean.createComplaint(2, 2, 2, 3, 1, "Garbage remove", "Garbage remove", "ACTIVE","High");
//        complaintBean.createComplaint(
//                6,   // citizen user
//                1,   // category
//                2,   // society
//                2,   // ward
//                2,   // zone
//                "Garbage remove",
//                "Garbage remove",
//                "ACTIVE");
//        List<Object[]> complaints = complaintBean.getComplaintByUserId(2);
//
//        for (Object[] row : complaints) {
//            String title = (String) row[0];
//            String designation = (String) row[1];
//            String status = (String) row[2];
//
//            out.println("Title: " + title);
//            out.println("Designation: " + designation);
//            out.println("status: " + status);
//        }

//        List<Complaint> complaints=officerBean.getAssignedComplaint(3);
//        for(Complaint c:complaints){
//            out.println(c.getStatus());
//            out.println(c.getTitle());
//        }
//        officerBean.updateComplaintStatus(4, "Processing");
       
//        adminBean.addSlaRules(2,2);
//        adminBean.updateSlaRule(1,1);
//        adminBean.deleteSlaRule(1);

//          officerBean.updateComplaintStatus(9, "RESOLVED",2);
//          complaintBean.createComplaintReply(9, 1, "Escalated to Corporation");
//            Users user=userBean.getUserById(2);
//            System.out.println(user.getEmail());
//            System.out.println(user);
//            Officers officer=officerBean.getOfficerProfile(3);
//            Users user=officer.getUserId();
//            
//            System.out.println("Email "+user.getEmail());
//            
//            if(officer.getDepartmentId()!=null){
//                System.out.println("Department "+officer.getDepartmentId().getDepartmentName());
//            }
//            if(officer.getWardId()!=null){
//                System.out.println("Ward "+officer.getWardId().getWardName());
//            }
//            if(officer.getZoneId()!=null){
//                System.out.println("Zone "+officer.getZoneId().getZoneName());
//            }

              // Display Designation wise Complaint based on the officer ID
//              List<Complaint> complaints=officerBean.getComplaintByOfficer(3);
//              for(Complaint c: complaints){
////                  System.out.println(c.getTitle() + c.getDescription()); 
//                    System.out.println(c.getComplaintId());            
//
//              }

        QRCodeManager qr=new QRCodeManager();
        qr.qrcode();
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
