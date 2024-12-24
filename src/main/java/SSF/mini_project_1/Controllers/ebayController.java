package SSF.mini_project_1.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import SSF.mini_project_1.Services.eBayService;
import SSF.mini_project_1.Models.*;

@Controller
@RequestMapping
public class ebayController {
    @Autowired 
    private eBayService ebaySvc;

    @GetMapping("/search")
    public ModelAndView getSearch(@RequestParam MultiValueMap<String,String> search){
        ModelAndView mav = new ModelAndView();
        String keyword= search.getFirst("keyword");
        String category= search.getFirst("category");

        itemSearch is = new itemSearch(keyword, category);

        List<Items> itemsList = ebaySvc.getItemlist(is);

        mav.setViewName("search");
        mav.addObject("keyword", keyword);
        mav.addObject("items", itemsList);

        return mav;
        

    }

    
}
