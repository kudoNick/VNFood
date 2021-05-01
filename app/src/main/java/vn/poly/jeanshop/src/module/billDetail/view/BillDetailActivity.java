package vn.poly.jeanshop.src.module.billDetail.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.Images;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.module.bill.view.BillAdapter;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.ErrorUtils;

public class BillDetailActivity extends AppCompatActivity {
    Gson gson;
    private RecyclerView rcvBillDeil;
    private RecyclerView rcvBill;
    private BillDetailAdapter billDetailAdapter;
    IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);
    private OrderDetails orderDetails;
    public static String current;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        Toolbar toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());

        rcvBillDeil = findViewById(R.id.rcvBillDetail);
        rcvBill = findViewById(R.id.rcvBill);
        listOrderDetail();
        gson = new Gson();
    }
    //branch: tu get all list order detail - idorder

    //branch: tu get all list order detail - idorder

    public void listOrderDetail() {

        Intent intent = getIntent();
        String a = intent.getStringExtra("orderString");
        Log.d("TAG" + a, "");


        Log.d("LONgKUTE", "listProductForCate: " + a);
        Call<BaseResponse<List<OrderDetails>>> call = apiService.getListOederDetail(a);
        call.enqueue(new Callback<BaseResponse<List<OrderDetails>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<OrderDetails>>> call, Response<BaseResponse<List<OrderDetails>>> response) {
                if (response.isSuccessful()) {
                    List<OrderDetails> list = response.body().getData();
                    for (int i = 0; i < list.size(); i++) {
                        String ab = list.get(i).getProductId();
                        current = ab;
                    }

                    Log.d("TAG" + current, "");

                    billDetailAdapter = new BillDetailAdapter(getApplicationContext(), list);
                    rcvBillDeil.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rcvBillDeil.setAdapter(billDetailAdapter);

                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<OrderDetails>>> call, Throwable t) {

            }
        });
    }
}