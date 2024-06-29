package step.learning.getratemobile.orm;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyCurrency {
    private String R030;
    private String Txt;
    private double Rate;
    private String Cc;
    private Date ExchangeDate;
    private static final SimpleDateFormat apiDataFormat = new SimpleDateFormat(
            "yyyy-MM-dd", Locale.UK
    );

    public static MyCurrency fromJson (JSONObject jsonObject ) throws IllegalArgumentException {
        try {
            String r030 = jsonObject.getString( "r030" );
            String txt = jsonObject.getString( "txt" );
            double rate = jsonObject.getDouble( "rate");
            String cc = jsonObject.getString( "cc" );
            Date exchangedate = apiDataFormat.parse(
                jsonObject.getString( "exchangedate" )
            );

            MyCurrency myCurrency = new MyCurrency();
            myCurrency.setR030( r030 );
            myCurrency.setTxt( txt );
            myCurrency.setRate( rate );
            myCurrency.setCc( cc );
            myCurrency.setExchangeDate( exchangedate );

            return myCurrency;
        }
        catch ( Exception ex ) {
            throw new IllegalArgumentException( ex.getMessage() );
        }
    }
    public String getR030() {
        return R030;
    }

    public void setR030(String r030) {
        R030 = r030;
    }

    public String getTxt() {
        return Txt;
    }

    public void setTxt(String txt) {
        Txt = txt;
    }

    public double getRate() {
        return Rate;
    }

    public void setRate(double rate) {
        Rate = rate;
    }

    public String getCc() {
        return Cc;
    }

    public void setCc(String cc) {
        Cc = cc;
    }

    public Date getExchangeDate() {
        return ExchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        ExchangeDate = exchangeDate;
    }
}
