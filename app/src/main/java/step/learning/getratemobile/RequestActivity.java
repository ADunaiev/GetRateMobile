package step.learning.getratemobile;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import step.learning.getratemobile.orm.Cargo;
import step.learning.getratemobile.orm.City;
import step.learning.getratemobile.orm.GetRequestsData;
import step.learning.getratemobile.orm.GetRequestsResponse;
import step.learning.getratemobile.orm.MyCurrency;

public class RequestActivity extends AppCompatActivity {

    private SmartMaterialSpinner<String> polSpinner;
    private SmartMaterialSpinner<String> podSpinner;
    private SmartMaterialSpinner<String> cargoSpinner;
    private SmartMaterialSpinner<String> currencySpinner;
    private SmartMaterialSpinner<String> sortBySpinner;
    private List<String> cities = new ArrayList<>();
    private List<String> cargoes = new ArrayList<>();
    private List<String> currencies = new ArrayList<>();
    private List<String> sortBy = Arrays.asList(
        "Price", "Time"
    );
    private static final String REQUEST_GET = "https://dunaiev.com/requests";
    private final byte[] buffer = new byte[8096];
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadData();
        polSpinner = findViewById( R.id.request_sp_pol );
        podSpinner = findViewById( R.id.request_sp_pod );
        cargoSpinner = findViewById( R.id.request_sp_cargoes );
        currencySpinner = findViewById( R.id.request_sp_currency );
        sortBySpinner = findViewById( R.id.request_sp_sort );

        polSpinner.setItem( cities );
        podSpinner.setItem( cities );
        cargoSpinner.setItem( cargoes );
        currencySpinner.setItem( currencies );
        sortBySpinner.setItem( sortBy );

        polSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        podSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cargoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText( RequestActivity.this, cargoes.get( position ), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        currencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadData() {
        if( executorService.isShutdown() ) return;

        CompletableFuture
                .supplyAsync( this::getDataFromDb, executorService )
                .thenApplyAsync( this::processRequestResponse );
    }
    private String getDataFromDb() {
        try( InputStream dataStream = new URL( REQUEST_GET ).openStream() ) {
            String response = readString( dataStream );
            return response;
        }
        catch( Exception ex ) {
            Log.e( "RequestActivity: getDataFromDb()", ex.getMessage() == null ?
                    ex.getClass().getName() : ex.getMessage() );
        }
        return null;
    }
    private boolean processRequestResponse( String response ) {
        boolean isCitiesLoaded = false;
        boolean isCargoesLoaded = false;
        boolean isCurrenciesLoaded = false;
        boolean isDataLoadedSuccessfully = false;

        try {
            GetRequestsResponse res = GetRequestsResponse.fromJsonString(response);

            GetRequestsData data = res.getData();

            for( City city : data.getCities() ) {
                this.cities.add( city.getName() );
                if( !isCitiesLoaded ) isCitiesLoaded = true;
            }

            for( Cargo cargo : data.getCargoes() ) {
                this.cargoes.add( cargo.getCustomCode() + " " + cargo.getName() );
                if( !isCargoesLoaded) isCargoesLoaded = true;
            }

            for( MyCurrency currency: data.getCurrencies() ) {
                this.currencies.add( currency.getCc() );
                if( !isCurrenciesLoaded) isCurrenciesLoaded = true;
            }
            isDataLoadedSuccessfully = isCitiesLoaded && isCargoesLoaded && isCurrenciesLoaded;

        }
        catch ( IllegalArgumentException ex ) {
            Log.e( "RequestActivity: processRequestResponse()", ex.getMessage() == null ?
                    ex.getClass().getName() : ex.getMessage() );
        }
        return false;
    }

    private String readString(InputStream stream) throws IOException {
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        int len;
        while( ( len = stream.read( buffer) ) != -1 ) {
            byteBuilder.write( buffer, 0, len );
        }
        String result = byteBuilder.toString();
        byteBuilder.close();
        return result;
    }


}