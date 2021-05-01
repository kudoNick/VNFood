package vn.poly.jeanshop.src.module.profileDetail;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.module.profile.view.ImageSaver;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.ErrorUtils;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName;
    private TextView tvPhone;
    private TextView tvEmail;
    private TextView tvAddress;
    private CircleImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profiledetail);
        imgAvatar = findViewById(R.id.imgAvatar);
        profileDetail();
        initView();
        Toolbar toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());

        Bitmap bitmap = new ImageSaver(this).
                setFileName("myImage.png").
                setDirectoryName("images").
                load();
        if (bitmap != null) {
            imgAvatar.setImageBitmap(bitmap);
        }
    }
    private void initView() {
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvAddress = findViewById(R.id.tvAddress);
    }

    private IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);

    public void profileDetail() {
        Call<BaseResponse<User>> callProfile = apiService.getProfile();
        callProfile.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String name = response.body().getData().getUsername();
                    tvName.setText(name);
                    String phone = response.body().getData().getPhone();
                    tvPhone.setText(phone);
                    String email = response.body().getData().getEmail();
                    tvEmail.setText(email);
                    String address = response.body().getData().getAddress();
                    tvAddress.setText(address);
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
                Toast.makeText(ProfileActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}