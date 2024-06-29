package step.learning.getratemobile.orm;

import org.json.JSONObject;

public class Cargo {
    private String CustomCode;
    private String Name;

    public static Cargo fromJson(JSONObject jsonObject ) throws IllegalArgumentException {
        try{
            String custom_code = jsonObject.getString( "custom_code" );
            String name = jsonObject.getString( "name" );

            Cargo cargo = new Cargo();
            cargo.setCustomCode( custom_code );
            cargo.setName( name );

            return cargo;
        }
        catch ( Exception ex ) {
            throw new IllegalArgumentException( ex.getMessage() );
        }
    }
    public String getCustomCode() {
        return CustomCode;
    }

    public void setCustomCode(String customCode) {
        CustomCode = customCode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
