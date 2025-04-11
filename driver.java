import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

class driver {

    public static void main(String[] args) {

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();
         
        try (FileReader reader = new FileReader("records.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
 
            // Parse the object to a JSONArray for easier use
            JSONArray recordsList = (JSONArray) obj;

            // Open the main GUI and pass in the JSONArray data to use
            new MainFrame(recordsList);
 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}