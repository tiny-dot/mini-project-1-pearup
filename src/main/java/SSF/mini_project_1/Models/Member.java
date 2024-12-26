package SSF.mini_project_1.Models;


public class Member {
    
    private String name;
    private String interests;
    private String eventPlanner;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getInterests() {
        return interests;
    }
    public void setInterests(String interests) {
        this.interests = interests;
    }
   
    
    public String getEventPlanner() {
        return eventPlanner;
    }
    public void setEventPlanner(String eventPlanner) {
        this.eventPlanner = eventPlanner;
    }
    @Override
    public String toString() {
        return "Member [name=" + name + ", interests=" + interests + ", eventPlanner=" + eventPlanner + "]";
    }
   
}
