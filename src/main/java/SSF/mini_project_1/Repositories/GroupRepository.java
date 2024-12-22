package SSF.mini_project_1.Repositories;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import SSF.mini_project_1.Models.Group;
import SSF.mini_project_1.Models.Member;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@Repository
public class GroupRepository {
    @Autowired @Qualifier("redis-object")

    private RedisTemplate<String, Object> template;

    //add created group to redis
    public void saveGroup(Group group){
        HashOperations<String,String,Object> hashOps = template.opsForHash();
        ListOperations<String,Object> listOps = template.opsForList();
        Map<String,Object> values = new HashMap<>();
        values.put("id", group.getGroupID());
        values.put("theme", group.getTheme());
        values.put("eventDate", group.getEventDate());
        
        hashOps.putAll(group.getGroupID(), values);

        //handle members list(need new key)
        String membersKey=group.getGroupID()+"member";
        List<Member> members = group.getMembers();
        if(members==null){
            members=new LinkedList<>();
            group.setMembers(members);
        }
        for(Member member: members){
            //handle nulls
            JsonObjectBuilder jBuilder=Json.createObjectBuilder();
            if(member.getName()!=null){jBuilder.add("name", member.getName());}
            if(member.getWishlist()!=null){jBuilder.add("wishlist",member.getWishlist());}
            if(member.getSecretsanta()!=null){jBuilder.add("secretsanta", member.getSecretsanta());}
            //convert java object to json object
            JsonObject memberInJson = jBuilder.build();
            String memberAsString = memberInJson.toString();

            listOps.rightPush(membersKey, memberAsString);
        }
    }

    //retrieve group by id
    public Group getGroupByID(String id){
        HashOperations<String,String,Object> hashOps = template.opsForHash();
        ListOperations<String,Object> listOps = template.opsForList();

        //group
        Map<String,Object> group = hashOps.entries(id);
        
        if(group.isEmpty()) return null;

        Group grp = new Group();
        grp.setGroupID(id);
        grp.setTheme(group.get("theme").toString());
        grp.setEventDate(group.get("eventDate").toString());
        
        //get members list
        String membersKey = id + "member";
        List<Object> rawMembers = listOps.range(membersKey, 0, -1);

        // Convert members list to List<Member>
        List<Member> members = new ArrayList<>();
        if (rawMembers != null) {
            for (Object member : rawMembers) {

                //get java object from json object
                String jsonMember = member.toString();
                JsonReader reader = Json.createReader(new StringReader(jsonMember));
                JsonObject jobject = reader.readObject();

                Member javaMember = new Member();
                if (jobject.containsKey("name")) {
                    javaMember.setName(jobject.getString("name"));
                } else {
                    javaMember.setName(null); // Set to null if the key does not exist
                }
    
                // Check if 'wishlist' exists in JSON object and set it
                if (jobject.containsKey("wishlist")) {
                    javaMember.setWishlist(jobject.getString("wishlist"));
                } else {
                    javaMember.setWishlist(null); // Set to null if the key does not exist
                }
    
                // Check if 'secretsanta' exists in JSON object and set it
                if (jobject.containsKey("secretsanta")) {
                    javaMember.setSecretsanta(jobject.getString("secretsanta"));
                } else {
                    javaMember.setSecretsanta(null); // Set to null if the key does not exist
                }
    

                members.add(javaMember);
            }
        }
        grp.setMembers(members);

        return grp;
    }

    public void updateGroup(Group group) {
        HashOperations<String, String, Object> hashOps = template.opsForHash();
        ListOperations<String, Object> listOps = template.opsForList();
        
        // Check if group exists, update its details if it does
        if (group != null && group.getGroupID() != null) {
            Map<String, Object> values = new HashMap<>();
            values.put("theme", group.getTheme());
            values.put("eventDate", group.getEventDate());
            
            // Update group details in Redis hash
            hashOps.putAll(group.getGroupID(), values);

            // Update members list (handle new member addition or removal)
            String membersKey = group.getGroupID() + "member";
            List<Member> members = group.getMembers();
            
            if (members != null) {
                // Clear existing members in Redis
                listOps.getOperations().delete(membersKey);
                
                // Save updated members list
                for (Member member : members) {
                    JsonObjectBuilder jBuilder = Json.createObjectBuilder();
                    if (member.getName() != null) {
                        jBuilder.add("name", member.getName());
                    }
                    if (member.getWishlist() != null) {
                        jBuilder.add("wishlist", member.getWishlist());
                    }
                    if (member.getSecretsanta() != null) {
                        jBuilder.add("secretsanta", member.getSecretsanta());
                    }
                    
                    // Convert member to JSON string
                    JsonObject memberInJson = jBuilder.build();
                    String memberAsString = memberInJson.toString();
                    
                    // Push member JSON to the Redis list
                    listOps.rightPush(membersKey, memberAsString);
                }
            }
        }
    }

    // Optionally add a method to add a single member to the group
    public void addMemberToGroup(String groupID, String memberName) {
        String membersKey = groupID + "members";
        ListOperations<String, Object> listOps = template.opsForList();
        listOps.rightPush(groupID, memberName);  // Add member to the list
    }

    
}