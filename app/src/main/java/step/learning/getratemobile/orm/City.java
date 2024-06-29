package step.learning.getratemobile.orm;

import org.json.JSONObject;

public class City {
    private String Name;

    public static City fromJson(JSONObject jsonObject ) throws IllegalArgumentException {
        try{
            String name = jsonObject.getString( "name" );
            City city = new City();
            city.setName( name );

            return city;
        }
        catch ( Exception ex ) {
            throw new IllegalArgumentException( ex.getMessage() );
        }
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }
}
