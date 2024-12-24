package SSF.mini_project_1.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class conversions {
    //date -> long
    //to ephochmilliseconds
    public static Long dateToEphoch(Date date) {
        return date.getTime();
    }

    //obj -> date
    public static Date ephocToDate(Object ephoch) {
        Long fromObj= ((Long)ephoch);
        Date date = new Date(fromObj);
        return date;
    }
    //for html date
    public static Date stringToDateHtml(String string) {
        //date = 2024-12-20
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        
        try {
            Date date = df.parse(string);
            return date;
        } catch (Exception x) {
            System.out.println("html date parsing error");
        }
        return null;
    }
    
}
