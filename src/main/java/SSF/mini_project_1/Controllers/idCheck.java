package SSF.mini_project_1.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import SSF.mini_project_1.Services.GroupsService;


@RestController
@RequestMapping
public class idCheck {
    @Autowired GroupsService grpSvc;

    //for groupID not available errors - return w error codes if available or not
    @GetMapping(path="/joinGroup/{groupID}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> checkgroupID(@RequestParam String groupID){
        

        if(grpSvc.checkID(groupID)){
            String existing = "exists";
            return ResponseEntity.ok(existing);
        } else {
            String noGroup = "groupID doesn't exist";
            return ResponseEntity.badRequest().body(noGroup);
        }
    }
}



