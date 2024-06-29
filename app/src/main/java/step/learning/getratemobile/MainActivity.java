package step.learning.getratemobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private static final String AUTH_URL = "https:/dunaiev.com/auth";
    private EditText etEmail;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etEmail = findViewById( R.id.start_menu_et_email ) ;
        etPassword = findViewById( R.id.start_menu_et_password ) ;

        findViewById( R.id.start_menu_btn_enter ).setOnClickListener( this::onEnterBtnClick );
        findViewById( R.id.start_menu_btn_registration ).setOnClickListener( this::onRegistrationBtnCLick );
    }

    private void sendCredentials ( String email, String password ) {

        // `/auth?email=${signinEmailInput.value}&password=${signinPasswordInput.value}`
        try{
            String body = String.format(
                    "email=%s&password=%s",
                    URLEncoder.encode( email, StandardCharsets.UTF_8.name() ),
                    URLEncoder.encode( password, StandardCharsets.UTF_8.name() )
            );
            // 1. Готуємо підключення. Та налаштовуваємо його.
            URL url = new URL( AUTH_URL + "?" + body);
            HttpURLConnection connection = ( HttpURLConnection) url.openConnection();
            connection.setChunkedStreamingMode( 0 );
            //connection.setDoOutput( true ); // запис у підключення - передача тіла
            connection.setDoInput( true ); // читання -- одержання тіла відповіді від сервера
            connection.setRequestMethod( "GET" );
            // заголовки у connection задаються через setRequestProperty
            connection.setRequestProperty( "Accept", "application/json" );
            //connection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded" );
            connection.setRequestProperty( "Connection", "close" );

            // 4. Одержуємо відповідь
            int statusCode= connection.getResponseCode();
            //Log.e( "statusCode", String.valueOf( statusCode ));
            // у разі успуху сервер передає 201 і не передає тіло
            // якщо помилка, то статус інший та є тіло з описом помилки
            if( statusCode != 200 ) {
                // хоча при помилці тіло таке ж, але воно вилучається через .getErrorStream()
                InputStream connectionInput = connection.getErrorStream();
                body = readString( connectionInput );
                connectionInput.close();
                Log.e( "sendCredentials", body );

                runOnUiThread( () -> {
                    Toast.makeText( this, "Wrong credentials!", Toast.LENGTH_SHORT).show();
                });
            }

            // 5. Закриваємо підключення
            connection.disconnect();

            if( statusCode == 200 ) {

                Intent intent = new Intent( this, RequestActivity.class);
                startActivity( intent );

                runOnUiThread( () -> {
                    Toast.makeText( this, "Login is successful!", Toast.LENGTH_SHORT).show();
                });
            }

        }
        catch ( Exception ex ) {
            Log.e( "sendCredentials", ex.getMessage() == null ?
                    ex.getClass().getName() : ex.getMessage() );
        }
    }
    private void onEnterBtnClick( View view ) {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if( email.isEmpty() ) {
            Toast.makeText( this, "Please enter email", Toast.LENGTH_SHORT ).show();
            return;
        }
        if( password.isEmpty() ) {
            Toast.makeText( this, "Please enter password", Toast.LENGTH_SHORT ).show();
            return;
        }

        CompletableFuture
                .runAsync( () -> sendCredentials( email, password ));
    }
    private void onRegistrationBtnCLick( View view ) {
        Intent intent = new Intent( this,  RegistrationActivity.class );
        startActivity( intent );
    }
    private String readString(InputStream stream) throws IOException {
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream();
        byte[] buffer = new byte[8096];
        int len;
        while( ( len = stream.read( buffer) ) != -1 ) {
            byteBuilder.write( buffer, 0, len );
        }
        String result = byteBuilder.toString();
        byteBuilder.close();
        return result;
    }
}