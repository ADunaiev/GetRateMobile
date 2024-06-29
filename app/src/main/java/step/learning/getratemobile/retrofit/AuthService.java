package step.learning.getratemobile.retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import step.learning.getratemobile.model.User;

public interface AuthService {
    @Multipart
    @POST("auth")
    Call<ResponseBody> signUpUser(
            @Part MultipartBody.Part avatar,
            @Part( "user-name" ) RequestBody name,
            @Part( "user-email" ) RequestBody email,
            @Part( "user-password" ) RequestBody password
    );
}
