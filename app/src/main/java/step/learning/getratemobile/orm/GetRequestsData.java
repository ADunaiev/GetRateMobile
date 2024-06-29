package step.learning.getratemobile.orm;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class GetRequestsData {
    private String Message;
    private String Avatar;
    private String UserName;
    private List<City> Cities;
    private List<Cargo> Cargoes;
    private List<MyCurrency> Currencies;

    public static GetRequestsData fromJsonString( JSONObject root ) {
        try {
            String message = root.getString( "message" );
            String avatar = root.getString( "avatar" );
            String userName = root.getString( "name" );

            JSONArray arrCities = root.getJSONArray( "cities" );
            List<City> cities = new ArrayList<>();
            for (int i = 0; i < arrCities.length(); i++) {
                cities.add( City.fromJson( arrCities.getJSONObject( i )));
            }

            JSONArray arrCargoes = root.getJSONArray( "cargoes" );
            List<Cargo> cargoes = new ArrayList<>();
            for (int i = 0; i < arrCargoes.length(); i++) {
                cargoes.add( Cargo.fromJson( arrCargoes.getJSONObject( i )));
            }

            JSONArray arrCurrencies = root.getJSONArray( "currencies" );
            List<MyCurrency> currencies = new ArrayList<>();
            for (int i = 0; i < arrCurrencies.length(); i++) {
                currencies.add( MyCurrency.fromJson(
                        arrCurrencies.getJSONObject( i ) ) );
            }

            GetRequestsData data = new GetRequestsData();
            data.setMessage( message );
            data.setAvatar( avatar );
            data.setUserName( userName );
            data.setCities( cities );
            data.setCargoes( cargoes );
            data.setCurrencies( currencies );

            return data;
        }
        catch ( Exception ex ) {
            throw new IllegalArgumentException( ex.getMessage() );
        }
    }
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public List<City> getCities() {
        return Cities;
    }

    public void setCities(List<City> cities) {
        Cities = cities;
    }

    public List<Cargo> getCargoes() {
        return Cargoes;
    }

    public void setCargoes(List<Cargo> cargoes) {
        Cargoes = cargoes;
    }

    public List<MyCurrency> getCurrencies() {
        return Currencies;
    }

    public void setCurrencies(List<MyCurrency> currencies) {
        Currencies = currencies;
    }
}
