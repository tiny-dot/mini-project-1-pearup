package SSF.mini_project_1.Models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import SSF.mini_project_1.Models.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

public class Group {
    private String groupID;

    @NotNull (message = "please enter a theme")
    @Size(min=2, max=50)
    private String theme;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @FutureOrPresent
    private String eventDate;
    
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
    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
    
    @Override
    public String toString() {
        return "Group [groupID=" + groupID + ", theme=" + theme + ", eventDate=" + eventDate + ", members=" + members
                + "]";
    }
    public List<Member> getMembers() {
        return members;
    }
    public void setMembers(List<Member> members) {
        this.members = members;
    }

    
    
}
