package SSF.mini_project_1.Models;

public class Events {
    private String name;
    private String type;
    private String eventURL;
   
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getEventURL() {
        return eventURL;
    }
    public void setEventURL(String eventURL) {
        this.eventURL = eventURL;
    }
    @Override
    public String toString() {
        return "events [name=" + name + ", type=" + type + ", eventURL=" + eventURL + "]";
    }

    



    
}
