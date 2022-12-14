import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class APITest {

    private JsonObject json;

    @BeforeAll
    public void setup(){

        URL url;
        HttpURLConnection con;
        BufferedReader in;
        String inputLine;
        StringBuilder builder = new StringBuilder();
        try{
            //Retrieving information from the endpoint
            url = new URL("https://api.tmsandbox.co.nz/v1/Categories/6328/Details.json?catalogue=false");
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setInstanceFollowRedirects(false);

            //Reading in data and storing it as a JSON object
            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            in.close();
            json = (JsonObject) JsonParser.parseString(builder.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_name_is_badges_is_true(){
        String name = String.valueOf(json.get("Name"));
        name = name.replaceAll("[^A-Za-z0-9]", "");
        assertEquals("Badges", name);
    }

    @Test
    public void test_CanRelist_is_set_to_true(){
        boolean canRelist = Boolean.parseBoolean(String.valueOf(json.get("CanRelist")));
        assertTrue(canRelist);
    }

    @Test
    public void test_promotion_name_is_Feature_and_description_contains_better_position_in_category_true(){
        JsonArray promotions = (JsonArray) json.get("Promotions");

        /*
        Loop through all  promotions and check which ones have the name 'Feature and their
        description contains 'Better position in category' and return (pass)
        */
        for(Object obj: promotions){
            JsonObject jsonObject = (JsonObject)obj;

            String promotionName = String.valueOf(jsonObject.get("Name")).replaceAll("[^A-Za-z0-9]", "");
            String description = String.valueOf(jsonObject.get("Description"));

            if(promotionName.equals("Feature") && description.contains("Better position in category")){
                return;
            }
        }
        fail("No promotion was found with the name 'Feature' and description containing 'Better position in category'");
    }

    @Test
    public void test_all_acceptance_criteria(){

        //Checking that the name is Badges and CanRelist is true
        String name = String.valueOf(json.get("Name"));
        name = name.replaceAll("[^A-Za-z0-9]", "");
        boolean canRelist = Boolean.parseBoolean(String.valueOf(json.get("CanRelist")));
        assertEquals("Badges", name);
        assertTrue(canRelist);

        //Checking there is a promotion with the name 'Feature', and with a description containing 'Better position in category'
        JsonArray promotions = (JsonArray) json.get("Promotions");

        /*
        Loop through all  promotions and check which ones have the name 'Feature and their
        description contains 'Better position in category' and return (pass)
        */
        for(Object obj: promotions){
            JsonObject jsonObject = (JsonObject)obj;

            String promotionName = String.valueOf(jsonObject.get("Name")).replaceAll("[^A-Za-z0-9]", "");
            String description = String.valueOf(jsonObject.get("Description"));

            if(promotionName.equals("Feature") && description.contains("Better position in category")){
                return;
            }
        }
        fail("No promotion was found with the name 'Feature' and description containing 'Better position in category'");
    }


}
