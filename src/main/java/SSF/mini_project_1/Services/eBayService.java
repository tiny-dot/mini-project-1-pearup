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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import SSF.mini_project_1.Models.Items;
import SSF.mini_project_1.Models.itemSearch;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class eBayService {
    public static final String ITEM_URL="https://svcs.ebay.com/services/search/FindingService/v1";

    @Value("${ebay.api.key}")
    private String apikey;

    public List<Items> getItemlist(itemSearch search){
        String url = UriComponentsBuilder.fromUriString(ITEM_URL)
                                        .queryParam("security-appname", apikey)
                                        .queryParam("keywords", search.keyword())
                                        .queryParam("category", search.category())
                                        .toUriString();

        System.out.println("Constructed URL: " + url);

        RequestEntity<Void> req = RequestEntity.get(url)
                                                .accept(MediaType.APPLICATION_JSON)
                                                .build();
        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp;
        List<Items> itemList = new LinkedList<>();

        try{
            resp=template.exchange(req,String.class);

            String payload = resp.getBody();
            JsonReader reader = Json.createReader(new StringReader(payload));
            JsonObject result = reader.readObject();
            JsonObject jObject = result.getJsonObject("searchResult");
            JsonArray jArray = jObject.getJsonArray("item");

            String title = "";
            String categoryName="";
            String price="";
            String galleryURL="";
            String itemURL="";
            

            //create 1 object
            for(int i=0; i<jArray.size();i++){
                title = jArray.getJsonObject(i).getString("title");
                categoryName = jArray.getJsonObject(i).getJsonObject("primaryCategory").getString("categoryName");
                price=jArray.getJsonObject(i).getJsonObject("buyItNowPrice").getString("#text"); 
                galleryURL = jArray.getJsonObject(i).getString("galleryURL");
                itemURL = jArray.getJsonObject(i).getString("viewItemURL");



                Items items = new Items();
                items.setTitle(title);
                items.setCategoryName(categoryName);
                items.setPrice(price);
                items.setGalleryURL(galleryURL);
                items.setItemURL(itemURL);

                itemList.add(items);
                System.out.println(itemList);
            }
            

        } catch (Exception e ){
            e.printStackTrace();
        }

        return itemList;

    }

    
}
