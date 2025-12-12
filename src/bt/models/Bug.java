package bt.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class Bug extends BaseEntity {



    private static int counter = 1;

    private String name;
    private String type;
    private String priority;
    private String level;
    private String projectName;
    private String date;
    private String status;
    private String assignedTo;
    private String reportedBy;
    private String screenshotPath;

    public Bug(String name, String type, String priority, String level, String projectName, String reportedBy, String screenshotPath) {

        this.id = counter++;
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.level = level;
        this.projectName = projectName;
        this.reportedBy = reportedBy;
        this.screenshotPath = screenshotPath;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.status = "Open";
        this.assignedTo = "";
    }


    @Override
    public String getDisplayInfo() {
        return "BUG #" + this.id + ": " + this.name + " [" + this.status + "] - Assigned to: " + this.assignedTo;
    }




    public String getName() { return name; }
    public String getType() { return type; }
    public String getPriority() { return priority; }
    public String getLevel() { return level; }
    public String getProjectName() { return projectName; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getAssignedTo() { return assignedTo; }
    public String getReportedBy() { return reportedBy; }
    public String getScreenshotPath() { return screenshotPath; }

    public void setStatus(String status) { this.status = status; }
    public void setAssignedTo(String assignedTo) { this.assignedTo = assignedTo; }

    public static void setCounter(int c) { counter = c; }
}