package android.johanmagnusson.se.projector.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Project {

    private String name;
    public String getName() {
        return name;
    }

    private String number;
    public String getNumber() {
        return number;
    }

    public Project(){
        // Default constructor required for calls to DataSnapshot.getValue(Project.class)
    }

    public Project(String name, String number){
        this.name = name;
        this.number = number;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("number", number);

        return result;
    }
}
