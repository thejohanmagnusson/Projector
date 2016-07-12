package android.johanmagnusson.se.projector.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class User {

    private String name;
    public String getName() {
        return name;
    }

    public User(){
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name){
        this.name = name;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);

        return result;
    }
}
