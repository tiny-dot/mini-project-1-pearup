package SSF.mini_project_1.Services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import SSF.mini_project_1.Models.Events;
import SSF.mini_project_1.Models.eventSearch;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class eventService {
    public static final String EVENT_URL="https://app.ticketmaster.com/discovery/v2/events.json";

    @Value("${tm.api.key}")
    private String apikey;

    public List<Events> getEvents(eventSearch search){
        String url = UriComponentsBuilder.fromUriString(EVENT_URL)
                                        .queryParam("apikey", apikey)
                                        .queryParam("keywords", search.keyword())
                                        .queryParam("locale", search.locale())
                                        .toUriString();

        System.out.println("Constructed URL: " + url);

        RequestEntity<Void> req = RequestEntity.get(url)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .build();
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp;
        List<Events> eventList = new LinkedList<>();

        try{
            resp=template.exchange(req,String.class);

            String payload = resp.getBody();
            //System.out.println("API Response: " + payload);
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();
           
            JsonObject jObject = result.getJsonObject("_embedded");
            
            if (jObject == null) {
                System.out.println("Error: _embedded key is missing in the response.");
                return eventList;
            }
        
            JsonArray jArray = jObject.getJsonArray("events");
            

            //create 1 object
            for(int i=0; i<jArray.size();i++){
                String name = jArray.getJsonObject(i).getString("name");
                String type = jArray.getJsonObject(i).getString("type");
                String eventURL = jArray.getJsonObject(i).getString("url");

                Events events = new Events();
                events.setName(name);
                events.setType(type);
                events.setEventURL(eventURL);


                eventList.add(events);
                //System.out.println(eventList);
            }
            

        } catch (Exception e ){
            e.printStackTrace();
        }

        return eventList;

    }
    
}
