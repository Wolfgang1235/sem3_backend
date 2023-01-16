package utils;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Set;
import com.google.gson.*;
import dtos.UserDTO;

import java.io.UnsupportedEncodingException;

public class Utility {
    private static final Gson gson = new GsonBuilder().create();
    
    public static void printAllProperties() {
            Properties prop = System.getProperties();
            Set<Object> keySet = prop.keySet();
            for (Object obj : keySet) {
                    System.out.println("System Property: {" 
                                    + obj.toString() + "," 
                                    + System.getProperty(obj.toString()) + "}");
            }
    }
    
    public static UserDTO json2DTO(String json) throws UnsupportedEncodingException{
        return gson.fromJson(new String(json.getBytes(StandardCharsets.UTF_8)), UserDTO.class);
    }
    
    public static String DTO2json(UserDTO userDTO){
        return gson.toJson(userDTO, UserDTO.class);
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        String str2 = "{'id':1, 'username':'owais', 'password':'1234', 'age':26}";
        UserDTO userDTO = json2DTO(str2);
        System.out.println(userDTO);
        
        String backAgain = DTO2json(userDTO);
        System.out.println(backAgain);
    }

}
