package step.learning.getratemobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.viewmodel.CreationExtras;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import step.learning.getratemobile.model.User;
import step.learning.getratemobile.retrofit.AuthService;
import step.learning.getratemobile.retrofit.MyAuthApi;

public class RegistrationActivity extends AppCompatActivity {

    private static final String SIGNUP_URL = "https:/dunaiev.com/";
    private EditText emailEditText;
    private EditText nameEditText;
    private EditText passwordEditText;
    private EditText repeatEditText;
    private ImageView avatarImageView;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    private boolean isAvatarChoosed;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String avatarUri;
    private MyAuthApi myAuthApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        findViewById( R.id.registration_menu_btn_send ).setOnClickListener( this::onRegistrationSendClick);
        findViewById( R.id.registration_btn_image ).setOnClickListener( this::onImageBtnClick);
        emailEditText = findViewById( R.id.registration_menu_et_email );
        nameEditText = findViewById( R.id.registration_menu_et_name );
        passwordEditText = findViewById( R.id.registration_menu_et_password );
        repeatEditText = findViewById( R.id.registration_menu_et_repeat );
        avatarImageView = findViewById( R.id.registration_iv_avatar );
        isAvatarChoosed = false;
        avatarUri = null;


        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if ( uri != null ) {
                Log.d("PhotoPicker", "Selected URI: " + uri );
                avatarImageView.setImageURI( uri );
                isAvatarChoosed = true;
                avatarUri = getRealPathFromURI( this, uri );
            } else {
                Log.d("PhotoPicker", "No media selected");
                isAvatarChoosed = false;
                avatarUri = null;
            }
        });
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void onImageBtnClick( View view ) {
        // Launch the photo picker and let the user choose only images.
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    private void onRegistrationSendClick( View view ) {
        String email = emailEditText.getText().toString();
        if( email.isEmpty() ) {
            Toast.makeText( this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = nameEditText.getText().toString();
        if( name.isEmpty() ) {
            Toast.makeText( this, "Please enter name", Toast.LENGTH_SHORT).show();
            return;
        }
        String passord = passwordEditText.getText().toString();
        if( passord.isEmpty() ) {
            Toast.makeText( this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        String repeat = repeatEditText.getText().toString();
        if( repeat.compareTo( passord ) != 0 ) {
            Toast.makeText( this, "Passwords don't match!", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User( name, email, passord, avatarUri );

        CompletableFuture
                .runAsync( () -> sendRegistrationData( user ), executorService );

    }
    private void showMessage( String message ) {
        runOnUiThread( () -> {
            Toast.makeText( this, message, Toast.LENGTH_SHORT ).show();
        });
    }
    public void sendRegistrationData( User user ) {

        Retrofit retrofit = MyAuthApi.getRetrofitClient( this );

        AuthService authService = retrofit.create( AuthService.class );

        File avatar = new File( avatarUri );
        RequestBody fileBody = RequestBody.create(MediaType.parse( "image/*" ), avatar );
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData( "user-avatar", avatar.getName(), fileBody );

        RequestBody nameBody = RequestBody.create( MediaType.parse("text/plain" ), user.name );
        RequestBody emailBody = RequestBody.create( MediaType.parse("text/plain" ), user.email );
        RequestBody passwordBody = RequestBody.create( MediaType.parse("text/plain" ), user.password );

        Call<ResponseBody> call = authService.signUpUser( imagePart, nameBody, emailBody, passwordBody );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call call, Response response) {

                showMessage( "Registration is successful!");

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                showMessage( t.getMessage() == null ?
                        t.getClass().getName() : t.getMessage() );
            }
        });
    }


}

