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

    private int price;
    public int getPrice() { return price; }

    private int costInternalMaterial;
    public int getCostInternalMaterial() { return costInternalMaterial; }

    private int costExternalMaterial;
    public int getCostExternalMaterial() { return costExternalMaterial; }

    private int costOutsourced;
    public int getCostOutsourced() { return costOutsourced; }

    private int costOther;
    public int getCostOther() { return costOther; }

    private int hoursManager;
    public int getHoursManager() { return hoursManager; }

    private int hoursDesigner;
    public int getHoursDesigner() { return hoursDesigner; }

    private int hoursWorker;
    public int getHoursWorker() { return hoursWorker; }

    public Project(){
        // Default constructor required for calls to DataSnapshot.getValue(Project.class)
    }

    public Project(String name,
                   String number,
                   int price,
                   int costInternalMaterial,
                   int costExternalMaterial,
                   int costOutsourced,
                   int costOther,
                   int hoursManager,
                   int hoursDesigner,
                   int hoursWorker){

        this.name = name;
        this.number = number;
        this.price = price;
        this.costInternalMaterial = costInternalMaterial;
        this.costExternalMaterial = costExternalMaterial;
        this.costOutsourced = costOutsourced;
        this.costOther = costOther;
        this.hoursManager = hoursManager;
        this.hoursDesigner = hoursDesigner;
        this.hoursWorker = hoursWorker;
    }

    @Exclude
    public HashMap<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("name", name);
        result.put("number", number);
        result.put("price", price);
        result.put("costInternalMaterial", costInternalMaterial);
        result.put("costExternalMaterial", costExternalMaterial);
        result.put("costOutsourced", costOutsourced);
        result.put("costOther", costOther);
        result.put("hoursManager", hoursManager);
        result.put("hoursDesigner", hoursDesigner);
        result.put("hoursWorker", hoursWorker);

        return result;
    }
}
