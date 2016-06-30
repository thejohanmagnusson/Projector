package android.johanmagnusson.se.projector.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Site {

    private String name;
    private String owner;

    public Site(){
        // Default constructor required for calls to DataSnapshot.getValue(Site.class)
    }

    public Site(String name, String owner){
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public String getOwner() {
        return owner;
    }

}
