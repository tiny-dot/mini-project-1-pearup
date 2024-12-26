package SSF.mini_project_1.Controllers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import SSF.mini_project_1.Models.Group;
import SSF.mini_project_1.Models.Member;
import SSF.mini_project_1.Services.GroupsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping
public class UserController {
    @Autowired
    private GroupsService grpSvc;

    //view 1: landing page
    @GetMapping()
    public String welcome(){
        return "welcome";
    }

    //view 2(leader) - create group
    @GetMapping("/createGroup")
    public String createGroup(Model model){
        model.addAttribute("group", new Group());
        return "createGroup";
    }


    @PostMapping("/createGroup")
    public String groupCreated(@Valid @ModelAttribute("group") Group group, BindingResult bindings, @RequestBody MultiValueMap<String, String> form, Model model){
       System.out.printf("---bindings: %b\n," , bindings.hasErrors());

        if(bindings.hasErrors()){
            FieldError nameError = new FieldError("group", "theme", "Please include a theme");
            FieldError dateError = new FieldError("group", "eventDate", "Date must be in the future");
        
            bindings.addError(nameError);
            bindings.addError(dateError);

            return "createGroup";
        }
                   

        System.out.println(group.toString());
        String groupID = grpSvc.createGroup(group);
        System.out.println(groupID);
        model.addAttribute("groupID", groupID);
        model.addAttribute("group", group);
       
        return "getID";
        }


    @GetMapping("/getgroup")
    public String getGroup(
        @RequestParam String groupid,
        Model model
    ){
        Group group = grpSvc.getGroupById(groupid);

        model.addAttribute("group", group);  
        return "leadersPage";  
    }
    
    // @GetMapping("/{groupID}")
    // public String leadersPage(@PathVariable String groupID, Model model) {
    //     // Get the group by ID
    //     Group group = grpSvc.getGroupById(groupID);

    //     model.addAttribute("group", group);  
    //     return "leadersPage";  
    // }

    //shuffle
    @PostMapping("/getgroup/{groupID}/shuffle")
    public String shuffleMembers(@PathVariable String groupID, Model model){
        Group group = grpSvc.getGroupById(groupID);

        List<Member> ogMembers = group.getMembers();
        Collections.shuffle(ogMembers);

        //to make sure they dont get themselves
        List<Member> shuffledMembers=new LinkedList<>(ogMembers); //create new list

        //assign pairs
        for(int i=0;i<ogMembers.size();i++){
            Member santa = ogMembers.get(i);
            Member receiver = shuffledMembers.get(i);

            if(santa.equals(receiver)){
                Collections.shuffle(shuffledMembers);  
                i=-1;
                continue;
        
            } else {
                //assign santa
                santa.setEventPlanner(receiver.getName());
            }
        }

        grpSvc.updateGroup(group);

        model.addAttribute("group", group);
        return "leadersPage";
    }

   
    //shuffle and assign pairs
    // @PostMapping("getgroup/{groupID}/shuffle")
    // public String shuffleMembers(@PathVariable String groupID, Model model){
    //     Group group = grpSvc.getGroupById(groupID);

    //     List<Member> ogMembers = group.getMembers();
    //     Collections.shuffle(ogMembers);

    //     //to make sure they dont get themselves
    //     List<Member> shuffledMembers=new LinkedList<>(ogMembers); //create new list

    //     //assign pairs
    //     for(int i=0;i<ogMembers.size();i++){
    //         Member santa = ogMembers.get(i);
    //         Member receiver = shuffledMembers.get(i);

    //         if(santa.equals(receiver)){
    //             Collections.shuffle(shuffledMembers);  
    //             i=-1;
    //             continue;
        
    //         } else {
    //             //assign santa
    //             santa.setSecretsanta(receiver.getName());
    //         }
    //     }

    //     grpSvc.updateGroup(group);

    //     model.addAttribute("group", group);
    //     return "leadersPage";
    // }


    /*--------------------------JOIN EXISTING GROUP------------------------------- */
    @GetMapping("/joinGroup")
    public String members(){
        return "joinGroup";
    }

    //member login
    @PostMapping("/joinGroup")
    public String joinGroup(@RequestParam MultiValueMap<String, String> form, Model model){
        try{
            String groupID = form.getFirst("groupID");
            String name = form.getFirst("name");

            //get group
            Group group = grpSvc.getGroupById(groupID);

            //find the member in the group
            Member member = null;
            for (Member m : group.getMembers()) {
                if (m.getName().equals(name)) {
                    member = m;
                    break; // Stop searching once the member is found
                }
            }
            //if no current member, create and add to 
            if(member==null){
                //add member to group
                member = new Member();
                member.setName(name);
                grpSvc.addMembersToGroup(groupID, member);
            } 
            model.addAttribute("groupID", groupID);
            model.addAttribute("member", member);
            return "profile";

        }catch (Exception e){
            e.printStackTrace();
            return "joinGroup";
        }
    }


    
    @GetMapping("/group/{groupID}/profile/{name}")
    public String profile(@PathVariable(name="groupID") String groupID, @PathVariable(name="name") String name, Model model){
        //get the group by the given ID
        Group group = grpSvc.getGroupById(groupID);
        

        //in this group, find the member
        List<Member> groupMembers = group.getMembers();
        Member member = null;

        if (groupMembers != null) {
            for (Member m : groupMembers) {
                if (m.getName().equals(name)) {
                    member = m;
                    break;
                }
            }
        }

        if(member == null){
            model.addAttribute("error", "Member not found");
            return "member-not-found";}
        //bind to model
        model.addAttribute("groupID", groupID);
        model.addAttribute("member", member);
        //model.addAttribute("secretsanta", member.getSecretsanta());
        
        return "profile";  
    }

    @PostMapping("/group/{groupID}/profile/{name}/update")
    public String updateProfile(@PathVariable String groupID, @PathVariable String name,
                                @RequestParam String interests, Model model) {

        // Get the group
        Group group = grpSvc.getGroupById(groupID);

        // Update member list
        List<Member> members = group.getMembers();
        Member member = null;
        for (Member m : members) {
            if (m.getName().equals(name)) {
                member = m;
                break;
            }
        }

        if (member != null) {
            member.setInterests(interests);  
            grpSvc.updateGroup(group);  // Save updated group
            model.addAttribute("group", group);
            model.addAttribute("member", member);
        } else {
            return "member not found";
        }
        return "profile";
}
}