package SSF.mini_project_1.Services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SSF.mini_project_1.Models.Group;
import SSF.mini_project_1.Models.Member;
import SSF.mini_project_1.Repositories.GroupRepository;

@Service
public class GroupsService {
    @Autowired
    private GroupRepository grpRepo;

    //save new group into redis
    public String createGroup(Group group){
        String groupID = UUID.randomUUID().toString().substring(0,8);
        group.setGroupID(groupID);
        System.out.println(group.toString());

        grpRepo.saveGroup(group);

        return groupID;
    }

    //get a group by id
    public Group getGroupById(String groupID){
        return grpRepo.getGroupByID(groupID);
    }

    //add members to indiv group
    public void addMembersToGroup(String groupID, Member member){
        try{
            //get the group
            Group group = getGroupById(groupID);
            
            //get the members by the name and add to member list
            if (group.getMembers() == null) {
                group.setMembers(new LinkedList<>());
            }
            group.getMembers().add(member);

            //save to redis
            grpRepo.saveGroup(group);

        } catch(Exception e){
            e.printStackTrace();
        } 
    }

    //update new group
    public void updateGroup(Group group){
        grpRepo.updateGroup(group);
    }

    //members update wishlist
    public void updateInterest(String groupID, String name, String interest){
        try{
            //get group
            Group group = grpRepo.getGroupByID(groupID);

            
            for (Member m:group.getMembers()){
                if(m.getName().equals(name)){
                    //allow updating
                    m.setInterests(interest);
                    //save to redis
                    grpRepo.saveGroup(group);
                    break;
                }
            }

        } catch (Exception e){e.printStackTrace();}
    }

   
    //update secret santa
    public void updateSecretsanta(String groupID, String name, String secretsanta) {
        Group group = grpRepo.getGroupByID(groupID);
        
        Member member = null;
        for (Member m : group.getMembers()) {
            if (m.getName().equals(name)) {
                member=m;
                // m.setSecretsanta(secretsanta);
                break;
            }
        }
        grpRepo.saveGroup(group);
    }

    //groupID dont exist
    public boolean checkID(String groupID){
        if(grpRepo.checkID(groupID)){
            return true;
        } return false;
    }
}




