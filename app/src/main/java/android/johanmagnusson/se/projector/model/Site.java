package android.johanmagnusson.se.projector.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;

@IgnoreExtraProperties
public class Site {

    private String name;
    public String getName() {
        return name;
    }

    private String owner;
    public String getOwner() {
        return owner;
    }

    public Site(){
        // Default constructor required for calls to DataSnapshot.getValue(Site.class)
    }

    public Site(String name, String owner){
        this.name = name;
        this.owner = owner;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("owner", owner);

        return result;
    }

}
