package SSF.mini_project_1.Models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class Group {
    private String groupID;

    @NotNull (message = "Please enter a theme")
    private String theme;

    @NotNull (message="Please enter a date")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @FutureOrPresent (message="Date should be in the future")
    private Date eventDate;
    
    private List<Member> members;

    public String getGroupID() {
        return groupID;
    }
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    public String getTheme() {
        return theme;
    }
    public void setTheme(String theme) {
        this.theme = theme;
    }
    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }
    public Date getEventDate() {
        return eventDate;
    }
    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }
    @Override
    public String toString() {
        return "Group [groupID=" + groupID + ", theme=" + theme + ", eventDate=" + eventDate + ", members=" + members
                + "]";
    }
   

    
    
}
