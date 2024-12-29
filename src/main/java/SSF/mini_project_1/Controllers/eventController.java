package SSF.mini_project_1.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import SSF.mini_project_1.Services.eventService;
import SSF.mini_project_1.Models.*;

@Controller
@RequestMapping
public class eventController {

    @Autowired 
    private eventService eventSvc;

    @GetMapping("/searchForm")
    public String search(){
        return "searchForm";
    }


    @PostMapping("/searchForm")
    public ModelAndView getSearch(@RequestParam MultiValueMap<String,String> search){
        
        ModelAndView mav = new ModelAndView();
        String keyword= search.getFirst("classificationName");
        String locale= search.getFirst("locale");

        eventSearch es = new eventSearch(keyword, locale);

        List<Events> eventList = eventSvc.getEvents(es);

        mav.setViewName("search");
        mav.addObject("keyword", keyword);
        mav.addObject("locale", locale);
        mav.addObject("events", eventList);

        return mav;
        

    }

    
}
