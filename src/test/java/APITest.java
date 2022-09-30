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
        JsonParser parser = new JsonParser();
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
            json = (JsonObject) parser.parse(builder.toString());

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

}
