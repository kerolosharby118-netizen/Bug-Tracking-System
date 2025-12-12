package bt.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bug implements Serializable, IDable {
    private static final long serialVersionUID = 1L;

    private static int counter = 1;
    private int id;
    private String name;
    private String type;
    private String priority;
    private String level;
    private String projectName;
    private String date; // stored as string for simplicity
    private String status; // Open, In Progress, Closed
    private String assignedTo; // developer username
    private String reportedBy; // tester username
    private String screenshotPath; // path to file

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

    // getters / setters

    //
    @Override
    public int getId() { return id; }

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

    // used after loading saved list to restore counter
    public static void setCounter(int c) { counter = c; }
}