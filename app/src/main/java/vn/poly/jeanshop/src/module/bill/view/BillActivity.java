package vn.poly.jeanshop.src.module.bill.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.Order;
import vn.poly.jeanshop.src.model.OrderBill;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.ErrorUtils;

public class BillActivity extends AppCompatActivity {

    private RecyclerView rcvBill;
    private BillAdapter billAdapter;
    private Order order;
    private List<Order> list;
    private SwipeRefreshLayout SwipeRefreshLayout;
    IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);
    public static String current = "";
    public static String idOrder = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        Toolbar toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());

        SwipeRefreshLayout = findViewById(R.id.SwipeRefreshLayout);
        rcvBill = findViewById(R.id.rcvBill);
        Refresh();
        profileDetail();

    }

    //branch: tu get user id

    public void profileDetail() {
        Call<BaseResponse<User>> callProfile = apiService.getProfile();
        callProfile.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String name = response.body().getData().getUserId();
                    current = name;

                    Call<BaseResponse<List<OrderBill>>> callProduct = apiService.getListOrderUser(current);
                    Log.d("TAG" + callProduct, "");
                    callProduct.enqueue(new Callback<BaseResponse<List<OrderBill>>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<List<OrderBill>>> call, Response<BaseResponse<List<OrderBill>>> response) {
                            if (response.isSuccessful()) {
                                Log.d("LONgKUTE", "onResponse: " + response.body().getData().size());
                                List<OrderBill> data = response.body().getData();

                                for (int i = 0; i < data.size() ; i++) {
                                    String a = data.get(i).getOrderId();
                                    idOrder = a;
                                }
                                billAdapter = new BillAdapter(getApplicationContext(), data);
                                rcvBill.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                rcvBill.setAdapter(billAdapter);


                            } else {
                                ErrorResponse err = ErrorUtils.parseError(response);
//                    presenterCate.resultGetProductForCate(false, null, err.getErr());
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<List<OrderBill>>> call, Throwable t) {
//                presenterCate.resultGetProductForCate(false, null, t.getMessage());
                        }
                    });

                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
            }
        });
    }


    //branch: tu get reload

    private void Refresh(){
        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                profileDetail();
                SwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}