package vn.poly.jeanshop.src.module.profile.view;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.model.StoreKey;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.module.bill.view.BillActivity;
import vn.poly.jeanshop.src.module.login.view.LoginActivity;
import vn.poly.jeanshop.src.module.myorder.view.MyOrderActivity;
import vn.poly.jeanshop.src.module.profile.presenter.PresenterProfile;
import vn.poly.jeanshop.src.module.profileDetail.ProfileActivity;
import vn.poly.jeanshop.src.module.register.adapter.CitySpinnerAdapter;
import vn.poly.jeanshop.src.module.register.adapter.DistrictSpinnerAdapter;
import vn.poly.jeanshop.src.module.register.adapter.WardSpinnerAdapter;
import vn.poly.jeanshop.src.module.register.model.DataCity;
import vn.poly.jeanshop.src.module.register.model.DataDistrict;
import vn.poly.jeanshop.src.module.register.model.DataWard;
import vn.poly.jeanshop.src.module.register.view.RegisterActivity;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.DialogLoading;
import vn.poly.jeanshop.src.utils.ErrorUtils;
import vn.poly.jeanshop.src.utils.SplashScreenActivity;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements IViewProfile, View.OnClickListener {

    private PresenterProfile presenterProfile;
    private TextView txtNameProfile;
    private GoogleProgressBar progressBarProfile;
    private ImageView imgProfile, imageProfileUpload;
    private LinearLayout layoutChangePassword,layoutBill,lineLogout,layoutChangeProfile;
    private String realPathImages;
    private Dialog dialogChanePass,dialog;
    private TextView tvProfileDetail;
    private IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);
    public static String current;

    //spinner
    private WardSpinnerAdapter wardSpinnerAdapter;
    private CitySpinnerAdapter spinnerAdapter;
    private DistrictSpinnerAdapter districtSpinnerAdapter;

    private DataCity dataCity;
    private DataDistrict dataDistrict;
    private DataWard dataWard;

    private List<DataWard> dataWardList;
    private List<DataCity> cityList = new ArrayList<>();
    private List<DataDistrict> districtList;

    private AppCompatSpinner spiCity,spiDistrict, spiWard;

    private String idAddress;

    private  String city,district,ward;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(v);
        DialogLoading.LoadingGoogle(true, progressBarProfile);
        presenterProfile.getProfile();

        return v;

    }

    private void initView(View v) {

        spinnerCity();
        chooseSpinnerCity();
        chooseSpinnerWard();

        spiCity = v.findViewById(R.id.spiCity);
        spiDistrict = v.findViewById(R.id.spiDistrict);
        spiWard = v.findViewById(R.id.spiWard);

        presenterProfile = new PresenterProfile(this);
        txtNameProfile = v.findViewById(R.id.txtNameProfile);
        progressBarProfile = v.findViewById(R.id.progressBarProfile);
        imgProfile = v.findViewById(R.id.imageProfile);
        layoutChangePassword = v.findViewById(R.id.layoutChangePassword);
        layoutChangeProfile = v.findViewById(R.id.layoutChangeProfile);
        layoutBill = v.findViewById(R.id.layoutBill);
        tvProfileDetail = v.findViewById(R.id.txtProfile);
        lineLogout = v.findViewById(R.id.lineLogout);

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPhotoImages();
            }
        });

        lineLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LoginActivity.class);

                SharedPreferences pref = getContext().getSharedPreferences("User", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("token" , null);
                editor.apply();
                StoreKey.setToken(null);

                startActivity(intent);
                Toast.makeText(getContext(), "Đăng xuất thành công",Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        layoutChangePassword.setOnClickListener(this);
        layoutChangeProfile.setOnClickListener(this);

        layoutBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getContext(), BillActivity.class);
               startActivity(intent);

            }
        });
        
        
        
        tvProfileDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        Bitmap bitmap = new ImageSaver(getContext()).
                setFileName("myImage.png").
                setDirectoryName("images").
                load();
        if (bitmap != null) {
            imgProfile.setImageBitmap(bitmap);
        }


    }
    @Override
    public void onSuccess(User user) {
        if (user != null) {
            DialogLoading.LoadingGoogle(false, progressBarProfile);
            txtNameProfile.setText(user.getUsername());
        }
    }

    @Override
    public void onFailed(String msg) {
        DialogLoading.LoadingGoogle(false, progressBarProfile);
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onChangePasswordSuccess(String msg) {

        DialogLoading.LoadingGoogle(false, progressBarProfile);
        dialogChanePass.dismiss();
        dialog.dismiss();
        Toasty.success(Objects.requireNonNull(getActivity()), "Đổi mật khẩu thành công", Toasty.LENGTH_LONG).show();

        Toasty.warning(Objects.requireNonNull(getActivity()), "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT, true).show();

        SharedPreferences pref = getActivity().getSharedPreferences("User", MODE_PRIVATE);

        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", null);
        editor.apply();

        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();

//        new Handler().postDelayed(() -> {
//            startActivity(new Intent(getActivity(), LoginActivity.class));
//            getActivity().finish();
//        }, 1000);
    }

    @Override
    public void onChangePasswordFail(String msg) {
        DialogLoading.LoadingGoogle(false, progressBarProfile);
        dialogChanePass.dismiss();
        dialog.dismiss();
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageProfile:
                openPhotoImages();
                break;
            case R.id.layoutChangePassword:
                handlerChangePassword();
                break;
            case R.id.layoutChangeProfile:
                changeProfile();
                break;
        }

    }

    private void handlerChangePassword() {
        dialogChanePass = new Dialog(Objects.requireNonNull(getActivity()));
        dialogChanePass.setContentView(R.layout.custom_dialog_change_password);
        TextInputEditText edtOldPassword, edtNewPassword, edtNewPasswordAgain;

        TextView txtCloseDialogChangePassword;
        Button btnChangePassword = dialogChanePass.findViewById(R.id.btnChangePassword);
        edtOldPassword = dialogChanePass.findViewById(R.id.edtOldPassword);
        edtNewPassword = dialogChanePass.findViewById(R.id.edtNewPassword);
        edtNewPasswordAgain = dialogChanePass.findViewById(R.id.edtNewPasswordAgain);
        txtCloseDialogChangePassword = dialogChanePass.findViewById(R.id.txtCloseDialogChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.loading);

            String oldPassword = edtOldPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String newPasswordAgain = edtNewPasswordAgain.getText().toString().trim();

            if (edtOldPassword.getText().toString().isEmpty()) {
                edtOldPassword.setError("Empty");

            } else if (newPassword.isEmpty()) {
                edtNewPassword.setError("Empty");
            } else if (newPasswordAgain.isEmpty()) {
                edtNewPasswordAgain.setError("Empty");
            } else if (!newPasswordAgain.equals(newPassword)) {
                edtNewPasswordAgain.setError("New Password Different");
            } else {
                DialogLoading.LoadingGoogle(true, progressBarProfile);
                presenterProfile.changePassword(oldPassword, newPassword);
                dialog.show();
            }
        });
        txtCloseDialogChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogChanePass.dismiss();
            }
        });

        dialogChanePass.show();


    }


    private void changeProfile() {

        Call<BaseResponse<User>> callProfile = apiService.getProfile();
        callProfile.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String data = response.body().getData().getUserId();
                    current = data;
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                }
                Log.d("ABC"+current,"");


                dialogChanePass = new Dialog(Objects.requireNonNull(getActivity()));
                dialogChanePass.setContentView(R.layout.custom_dialog_change_profile);
                TextInputEditText edtphone, edtaddress, edtusername;

                Button btnChangeProfile = dialogChanePass.findViewById(R.id.btnChangeProfile);
                edtphone = dialogChanePass.findViewById(R.id.edtphone);
                edtaddress = dialogChanePass.findViewById(R.id.edtaddress);
                edtusername = dialogChanePass.findViewById(R.id.edtusername);

                btnChangeProfile.setOnClickListener(v -> {
                    dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.loading);

                    String phone = edtphone.getText().toString().trim();
                    String address = edtaddress.getText().toString().trim()+ ", " + ward + ", " + district + ", " + city;
                    String username = edtusername.getText().toString().trim();


                    DialogLoading.LoadingGoogle(true, progressBarProfile);
                    presenterProfile.changeProfile(current,phone,address,username);
                });


                dialogChanePass.show();

            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {

            }
        });

    }

    private void openPhotoImages() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 456);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 456) {
            getActivity();
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri uri = data.getData();
                realPathImages = getRealPathFromURI(uri);
                try {
                    Bitmap mBitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(data.getData()));
                    showDialogAccept(mBitmap);
                    new ImageSaver(getContext()).
                            setFileName("myImage.png").
                            setDirectoryName("images").
                            save(mBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showDialogAccept(Bitmap mBitmap) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_profile);
        ImageView imageProfileUpload = dialog.findViewById(R.id.imageProfileUpload);
        Button btnAcceptUploadImage, btnCancelUploadImage;
        btnAcceptUploadImage = dialog.findViewById(R.id.btnAcceptUploadImage);
        btnCancelUploadImage = dialog.findViewById(R.id.btnCancelUploadImage);

        btnCancelUploadImage.setOnClickListener(v -> dialog.dismiss());
        imageProfileUpload.setImageBitmap(mBitmap);
        btnAcceptUploadImage.setOnClickListener(v -> {
            File file = new File(realPathImages);
            String filePath = file.getAbsolutePath();
            String[] mangtenFile = filePath.split("\\.");
            filePath = mangtenFile[0] + System.currentTimeMillis() + "." + mangtenFile[1];


            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), file);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/from-data"), filePath);
            MultipartBody.Part body =  MultipartBody.Part.createFormData("file", filePath, requestBody);
            presenterProfile.uploadImage(body);
            imgProfile.setImageBitmap(mBitmap);
            dialog.dismiss();
        });

        dialog.show();

    }

    private String getRealPathFromURI(Uri contentUri) {
        String path = null;
        String[] proj = {MediaStore.MediaColumns.DATA};
        Cursor cursor = Objects.requireNonNull(getActivity()).getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }

    public void spinnerCity(){
        AndroidNetworking.get("https://thongtindoanhnghiep.co/api/city")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray city = response.getJSONArray("LtsItem");
                            for (int i = 0; i < city.length(); i++) {
                                dataCity = new DataCity(city.getJSONObject(i));
                                cityList.add(dataCity);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        spinnerAdapter = new CitySpinnerAdapter(cityList, getActivity());
                        spiCity.setAdapter(spinnerAdapter);
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getActivity(), anError.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void chooseSpinnerCity(){
        spiCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                dataCity = cityList.get(i);
                idAddress = dataCity.getId();
                city = dataCity.getTitle();
                //choose Spinner District
                districtList = new ArrayList<>();
                AndroidNetworking.get("https://thongtindoanhnghiep.co/api/city/" + idAddress +"/district")
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int j = 0; j < response.length(); j++) {
                                    try {
                                        dataDistrict = new DataDistrict(response.getJSONObject(j));
                                        districtList.add(dataDistrict);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                districtSpinnerAdapter = new DistrictSpinnerAdapter(districtList,getActivity());
                                spiDistrict.setAdapter(districtSpinnerAdapter);
                            }
                            @Override
                            public void onError(ANError anError) {

                            }
                        });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getActivity(), "ko chon gi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void chooseSpinnerWard(){
        spiDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dataDistrict = districtList.get(position);
                idAddress = dataDistrict.getId();
                district = dataDistrict.getTitle();
                //choose ward
                dataWardList = new ArrayList<>();
                AndroidNetworking.get("https://thongtindoanhnghiep.co/api/district/" + idAddress + "/ward")
                        .build()
                        .getAsJSONArray(new JSONArrayRequestListener() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        dataWard = new DataWard(response.getJSONObject(i));
                                        dataWardList.add(dataWard);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                wardSpinnerAdapter = new WardSpinnerAdapter(dataWardList,getActivity());
                                spiWard.setAdapter(wardSpinnerAdapter);
                            }
                            @Override
                            public void onError(ANError anError) {

                            }
                        });

                spiWard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        dataWard = dataWardList.get(position);
                        ward = dataWard.getTitle();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
