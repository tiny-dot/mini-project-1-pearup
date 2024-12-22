package SSF.mini_project_1.Controllers;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



import SSF.mini_project_1.Models.Group;
import SSF.mini_project_1.Models.Member;
import SSF.mini_project_1.Services.GroupsService;


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

    //do smth about thissss
    @PostMapping("/createGroup")
    public ResponseEntity<String> newGroup(@ModelAttribute Group group) {
        // Create the group and get the groupID
        String groupID = grpSvc.createGroup(group);

        // Prepare the response message with the groupID
        String message = "Group successfully created. Your group ID is: " + groupID;

        // Return a ResponseEntity with a success message and the groupID
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(message);  
    }

    
    @GetMapping("/{groupID}")
    public String leadersPage(@PathVariable String groupID, Model model) {
        // Get the group by ID
        Group group = grpSvc.getGroupById(groupID);
        
        if (group == null) {
            model.addAttribute("error", "Group not found");
            return "error"; // Handle the case when group is not found
        }

        model.addAttribute("group", group);  // Add group details to the model
        return "leadersPage";  // Return the leader's group page view
    }

    //shuffle and assign pairs
    @PostMapping("/{groupID}/shuffle")
    public String shuffleMembers(@RequestParam @PathVariable String groupID, Model model){
        Group group = grpSvc.getGroupById(groupID);
        if(group == null){
            model.addAttribute("not-found", "not-found");
            return "not-found";
        }

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
                santa.setSecretsanta(receiver.getName());
            }
        }

        grpSvc.updateGroup(group);

        //model.addAttribute("success", "secret santa pairs have been assigned");
        model.addAttribute("group", group);
        return "leadersPage";
        

    }


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

            //check if group exists
            Group group = grpSvc.getGroupById(groupID);
            if (group == null) {
                model.addAttribute("error", "Group not found");
                return "error"; // Or redirect to an error page if group doesn't exist
            }

            //check if member exists
            Member member = null;
            for (Member m : group.getMembers()) {
                if (m.getName().equals(name)) {
                    member = m;
                    break; // Stop searching once the member is found
                }
            }

            if(member==null){
                //add member to group
                Member me = new Member();
                me.setName(name);
                grpSvc.addMembersToGroup(groupID, me);
                return "redirect:/" + groupID + "/profile/" + name;
            } else{
                return "redirect:/" + groupID + "/profile/" + name;
            }
        }catch (Exception e){
            e.printStackTrace();
            return "joinGroup";
        }
    }


    //member profile page - dont need request param/request body??
    @GetMapping("/{groupID}/profile/{name}")
    public String profile(@PathVariable(name="groupID") String groupID, @PathVariable(name="name") String name, Model model){
        //get the group by the given ID
        Group group = grpSvc.getGroupById(groupID);
        
        if(group==null){
            model.addAttribute("not-found", "error");
            return "not-found";
        }

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

    @PostMapping("/{groupID}/profile/{name}/update")
    public String updateProfile(@PathVariable String groupID, @PathVariable String name,
                                @RequestParam String wishlist, Model model) {

        // Get the group
        Group group = grpSvc.getGroupById(groupID);
        if (group == null) {
            model.addAttribute("error", "Group not found");
            return "not-found"; // Handle the case when group is not found
        }

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
            member.setWishlist(wishlist);  // Set the wishlist for the member
            grpSvc.updateGroup(group);  // Save updated group
            model.addAttribute("group", group);
            model.addAttribute("member", member);
        } else {
            model.addAttribute("error", "Member not found");
            return "member-not-found";  // If member is not found
        }
        return "profile";
}
}


