package lab2.response;

import java.util.HashMap;
import java.util.Map;

public class Response {
    public Map<String, String> getResponse(String key, String value){
        Map<String, String> response = new HashMap<>();
        response.put(key,value);
        return response;
    }
}
