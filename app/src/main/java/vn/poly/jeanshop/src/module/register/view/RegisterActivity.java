package vn.poly.jeanshop.src.module.register.view;

import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.module.register.adapter.CitySpinnerAdapter;
import vn.poly.jeanshop.src.module.register.adapter.DistrictSpinnerAdapter;
import vn.poly.jeanshop.src.module.register.adapter.WardSpinnerAdapter;
import vn.poly.jeanshop.src.module.register.model.DataCity;
import vn.poly.jeanshop.src.module.register.model.DataDistrict;
import vn.poly.jeanshop.src.module.register.model.DataWard;
import vn.poly.jeanshop.src.module.register.presenter.PresenterRegister;
import vn.poly.jeanshop.src.utils.DialogLoading;

public class RegisterActivity extends AppCompatActivity implements IViewRegister {

    private WardSpinnerAdapter wardSpinnerAdapter;
    private CitySpinnerAdapter spinnerAdapter;
    private DistrictSpinnerAdapter districtSpinnerAdapter;

    private DataCity dataCity;
    private DataDistrict dataDistrict;
    private DataWard dataWard;

    private List<DataWard> dataWardList;
    private List<DataCity> cityList = new ArrayList<>();
    private List<DataDistrict> districtList;
    private String idAddress;

    private  String city,district,ward;

    private LinearLayout lineLoading;

    private TextInputEditText edtEmailRegister, edtUserNameRegister, edtPhoneRegister,
            edtPasswordRegister,edtAddressRegister;
    private Button btnRegister;
    private PresenterRegister presenterRegister;
    private GoogleProgressBar progress_register;
    private AppCompatSpinner spiCity,spiDistrict, spiWard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        btnRegister.setOnClickListener(v -> {
            DialogLoading.LoadingGoogle(true, progress_register);
            registerUser();

        });
        spinnerCity();
        chooseSpinnerCity();
        chooseSpinnerWard();
    }

    private void registerUser() {
        String diaChi = edtAddressRegister.getText().toString().trim() + ", " + ward + ", " + district + ", " + city;
        if (validation()) {
            User user = new User();
            user.setEmail(edtEmailRegister.getText().toString().trim());
            user.setUsername(edtUserNameRegister.getText().toString().trim());
            user.setPassword(edtPasswordRegister.getText().toString().trim());
            user.setPhone(edtPhoneRegister.getText().toString().trim());
            //tuan anh
            user.setAddress(diaChi);

            presenterRegister.handlerRegister(user);
        }

    }

    private void initView() {
        androidx.appcompat.widget.Toolbar toolbarRegister = findViewById(R.id.toolbarRegister);
        progress_register = findViewById(R.id.progress_register);
        toolbarRegister.setNavigationOnClickListener(v -> finish());
        edtEmailRegister = findViewById(R.id.edtEmailRegister);
        edtPasswordRegister = findViewById(R.id.edtPasswordRegister);
        edtPhoneRegister = findViewById(R.id.edtPhoneRegister);
        edtUserNameRegister = findViewById(R.id.edtUserNameRegister);
        btnRegister = findViewById(R.id.btnRegister);
        //tuan anh
        edtAddressRegister = findViewById(R.id.edtAddressRegister);
        presenterRegister = new PresenterRegister(this);
        spiCity = findViewById(R.id.spiCity);
        spiDistrict = findViewById(R.id.spiDistrict);
        spiWard = findViewById(R.id.spiWard);
        lineLoading = findViewById(R.id.lineLoading);


    }


    @Override
    public void onSuccess() {
        DialogLoading.LoadingGoogle(false, progress_register);
        Toasty.success(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT, true).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toasty.warning(RegisterActivity.this, "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT, true).show();
                finish();
            }
        }, 3000);

    }

    @Override
    public void onFailed(String msg) {
        DialogLoading.LoadingGoogle(false, progress_register);
        Toasty.error(RegisterActivity.this, msg, Toast.LENGTH_SHORT, true).show();
    }

    private boolean validation() {
        String email = edtEmailRegister.getText().toString().trim();
        String password = edtPasswordRegister.getText().toString().trim();
        String phone = edtPhoneRegister.getText().toString().trim();
        String username = edtUserNameRegister.getText().toString().trim();

        //Tuấn Anh
        String address = edtAddressRegister.getText().toString().trim();


         if (email.length() == 0) {
            edtEmailRegister.setError("Email Invalid");
            edtEmailRegister.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailRegister.setError("Email Invalid");
            edtEmailRegister.requestFocus();
            return false;

        } else if (username.length() == 0) {
            edtUserNameRegister.setError("Username Invalid");
            edtUserNameRegister.requestFocus();
            return false;

        } else if (phone.length() == 0) {
            edtPhoneRegister.setError("Phone Invalid");
            edtPhoneRegister.requestFocus();
            return false;


        } else if (phone.length() != 10) {
            edtPhoneRegister.setError("Phone 10");
            edtPhoneRegister.requestFocus();
            return false;


        } else if (password.length() == 0) {
            edtPasswordRegister.setError("Password Invalid");
            edtPasswordRegister.requestFocus();
            return false;


        } else if (password.length() < 6) {
            edtPasswordRegister.setError("Password < 6");
            edtPasswordRegister.requestFocus();
            return false;

        }else if (address.length() == 0){
            edtAddressRegister.setError("address Invalid");
            edtAddressRegister.requestFocus();
            return false;
        }

        return true;
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
                        spinnerAdapter = new CitySpinnerAdapter(cityList,RegisterActivity.this);
                        spiCity.setAdapter(spinnerAdapter);
                    }
                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(RegisterActivity.this, anError.toString(), Toast.LENGTH_SHORT).show();
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
                                districtSpinnerAdapter = new DistrictSpinnerAdapter(districtList,RegisterActivity.this);
                                spiDistrict.setAdapter(districtSpinnerAdapter);
                            }
                            @Override
                            public void onError(ANError anError) {

                            }
                        });

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(RegisterActivity.this, "ko chon gi", Toast.LENGTH_SHORT).show();
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
                                wardSpinnerAdapter = new WardSpinnerAdapter(dataWardList,RegisterActivity.this);
                                spiWard.setAdapter(wardSpinnerAdapter);
                                lineLoading.setVisibility(View.GONE);
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
