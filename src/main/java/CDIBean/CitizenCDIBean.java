package CDIBean;

import Client.RestClient;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("citizenBean")
@SessionScoped
public class CitizenCDIBean implements Serializable {

    private Long totalComplaints = 0L;
    private Long assignedComplaints = 0L;
    private Long resolvedComplaints = 0L;
    private Long rejectedComplaints = 0L;

    private List<Object[]> recentComplaints = new ArrayList<>();

    private RestClient rl = new RestClient();

    @Inject
    private LoginBean loginBean;

    public void loadDashboard() {

        try {

            if (loginBean == null
                    || loginBean.getLoggedInUser() == null) {
                return;
            }

            Integer userId =
                    loginBean.getLoggedInUser().getUserId();

            List<Object[]> complaints =
                    rl.getComplaintByUser(
                            List.class,
                            String.valueOf(userId));

            if (complaints == null) {
                return;
            }

            totalComplaints = (long) complaints.size();

            assignedComplaints = 0L;
            resolvedComplaints = 0L;
            rejectedComplaints = 0L;

            for (Object[] c : complaints) {

                String status =
                        c[2] == null
                                ? ""
                                : c[2].toString();

                if ("ASSIGNED".equalsIgnoreCase(status)
                        || "IN_PROGRESS".equalsIgnoreCase(status)) {

                    assignedComplaints++;
                }

                if ("RESOLVED".equalsIgnoreCase(status)) {

                    resolvedComplaints++;
                }

                if ("REJECTED".equalsIgnoreCase(status)) {

                    rejectedComplaints++;
                }
            }

            recentComplaints = complaints;

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Long getTotalComplaints() {

        loadDashboard();
        return totalComplaints;
    }

    public Long getAssignedComplaints() {

        loadDashboard();
        return assignedComplaints;
    }

    public Long getResolvedComplaints() {

        loadDashboard();
        return resolvedComplaints;
    }

    public Long getRejectedComplaints() {

        loadDashboard();
        return rejectedComplaints;
    }

    public List<Object[]> getRecentComplaints() {

        loadDashboard();
        return recentComplaints;
    }
}