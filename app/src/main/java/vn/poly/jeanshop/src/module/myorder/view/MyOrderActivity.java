package vn.poly.jeanshop.src.module.myorder.view;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.NavigationActivity;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.Gift;
import vn.poly.jeanshop.src.model.Order;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.model.OrderProvisional;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.module.explore.view.ExploreFragment;
import vn.poly.jeanshop.src.module.myorder.AdapterCartProvisional;
import vn.poly.jeanshop.src.module.myorder.IOnClickCart;
import vn.poly.jeanshop.src.module.myorder.IOrder;
import vn.poly.jeanshop.src.module.myorder.presenter.PresenterOrder;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.DialogLoading;
import vn.poly.jeanshop.src.utils.ErrorUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrderActivity extends AppCompatActivity implements IOnClickCart, IOrder.IViewOrder {

    private OrderDetails orderDetails;

    private IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);

    private RecyclerView recyclerProductCart;
    private TextView txtTotalPrice, txtTotalAmount,txtPhone;
    private LinearLayout layoutCartEmpty, layoutCart;
    private AdapterCartProvisional adapterCartProvisional;
    private Button btnCheckoutCart, btnCheckGift;
    private PresenterOrder presenterOrder;
    private TextView txtResultCheckGift,txtAddressOrderDetails;
    private TextInputEditText edtGift;
    private double totalPrice = 0;
    private GoogleProgressBar progressOrder;
    ExploreFragment exploreFragmentCallback = new ExploreFragment();

    private Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_order);
        initView();

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        recyclerProductCart = findViewById(R.id.recyclerProductCart);
        txtTotalAmount = findViewById(R.id.txtTotalAmountCart);
        txtTotalPrice = findViewById(R.id.txtTotalPriceCart);
        layoutCartEmpty = findViewById(R.id.layoutCartEmpty);
        layoutCart = findViewById(R.id.layoutCart);
        btnCheckoutCart = findViewById(R.id.btnCheckoutCart);
        txtResultCheckGift = findViewById(R.id.txtResultCheckGift);
        btnCheckGift = findViewById(R.id.btnCheckGift);
        edtGift = findViewById(R.id.edtGift);
        progressOrder = findViewById(R.id.progressOrder);
        txtAddressOrderDetails = findViewById(R.id.txtAddressOrderDetails);
        txtPhone = findViewById(R.id.txtPhone);


        //
        Call<BaseResponse<User>> callProfile = apiService.getProfile();
        callProfile.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String address = response.body().getData().getAddress();
                    String phone = response.body().getData().getPhone();
                    txtAddressOrderDetails.setText(address);
                    txtPhone.setText(phone);
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
                Toast.makeText(MyOrderActivity.this, ""+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        //
        presenterOrder = new PresenterOrder(this);

        btnCheckGift.setOnClickListener(v -> getCodeGift());

        Toolbar toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());

        checkVisibleCart();
    }

    private void getCodeGift() {
        String codeGift = edtGift.getText().toString();
        if (!codeGift.isEmpty()) {
            presenterOrder.checkGift(codeGift);
        } else {
            Toasty.error(MyOrderActivity.this, "Code Empty", Toasty.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void handlerGetDataCart() {
        adapterCartProvisional = new AdapterCartProvisional(MyOrderActivity.this, NavigationActivity.orderDetails, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyOrderActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerProductCart.setLayoutManager(layoutManager);
        recyclerProductCart.setAdapter(adapterCartProvisional);
        handlerTotalPrice();
        adapterCartProvisional.notifyDataSetChanged();

        btnCheckoutCart.setOnClickListener(v -> checkOut());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkOut() {
        DialogLoading.LoadingGoogle(true,progressOrder);
        String orderId = String.valueOf(new Random().nextInt(900000));
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderProvisional i : NavigationActivity.orderDetails) {
             orderDetails = new OrderDetails();

            orderDetails.setIdOrder(orderId);
            orderDetails.setIdOrderDetails(String.valueOf(new Date().getTime()));
            orderDetails.setAmount(i.getAmount());
            orderDetails.setPrice(Double.parseDouble(i.getProduct().getPrice()) * i.getAmount());
            orderDetails.setProductId(i.getProduct().getProductId());
            orderDetails.setProducts(i.getProducts());
            orderDetails.setImage(i.getImage());
            orderDetails.setSize(AdapterCartProvisional.chooseSize);

            //tuan anh
            orderDetails.setNameProduct(i.getProduct().getName());
            orderDetails.setImageProduct(i.getProduct().getImage());

            orderDetailsList.add(orderDetails);
        }

        Order order = new Order(orderId, NavigationActivity.numberBadge, totalPrice, "Chờ xác nhận");
        presenterOrder.addCartToServer(order, orderDetailsList);


        //Tuấn Anh
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm / dd-MM-yyyy", Locale.getDefault());
//        String currentDateandTime = sdf.format(new Date());
//
//        //lấy ngày mua hàng + thêm 1 ngày ra ngày ship
//        // hiện thị ngày ship lên hóa đơn
//        //cái này t mới lấy đc nhưng chưa kịp cho vào database
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//        Calendar c1 = dateFormat.getCalendar();
//        c1.roll(Calendar.DAY_OF_MONTH, 1);
//        Date date = c1.getTime();
//        Toast.makeText(this, date.toString(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void increase(OrderProvisional orderProvisional, TextView numberCart) {

        if (orderProvisional.getAmount() >= Integer.parseInt(orderProvisional.getProduct().getAmount())) {
            Toast.makeText(this, "Vượt quá số lượng trong kho!", Toast.LENGTH_SHORT).show();
        }else {
            orderProvisional.setAmount(orderProvisional.getAmount() + 1);
            numberCart.setText(String.valueOf(orderProvisional.getAmount()));
            handlerTotalPrice();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    public void reduce(OrderProvisional orderProvisional, TextView numberCart) {

        if (orderProvisional.getAmount() == 1) {
            new AlertDialog.Builder(MyOrderActivity.this)
                    .setTitle("Thông báo")
                    .setMessage("Bạn có muốn xoá sản phẩm " + orderProvisional.getProduct().getName())
                    .setPositiveButton("Có", (dialog, which) -> {
                        NavigationActivity.orderDetails.remove(orderProvisional);
                        NavigationActivity.numberBadge = NavigationActivity.numberBadge - 1;
                        exploreFragmentCallback.OnClickBadge();
                        adapterCartProvisional.notifyDataSetChanged();
                        handlerTotalPrice();
                        checkVisibleCart();
                    }).setNegativeButton("Không", (dialog, which) -> dialog.dismiss()).show();
        }else {
            orderProvisional.setAmount(orderProvisional.getAmount() - 1);
            numberCart.setText(orderProvisional.getAmount() + "");
            handlerTotalPrice();

        }


    }

    private void handlerTotalPrice() {
        totalPrice = 0;
        for (OrderProvisional i : NavigationActivity.orderDetails) {
            totalPrice += i.getAmount() * Double.parseDouble(i.getProduct().getPrice());
        }
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        String str1 = vn.format(totalPrice);
        txtTotalPrice.setText("Tổng tiền: " + str1 + " VNĐ");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkVisibleCart() {
        if (NavigationActivity.orderDetails.size() <= 0) {
            layoutCartEmpty.setVisibility(View.VISIBLE);
            layoutCart.setVisibility(View.GONE);
        } else {
            layoutCart.setVisibility(View.VISIBLE);
            layoutCartEmpty.setVisibility(View.GONE);
            handlerGetDataCart();


        }
    }

    @Override
    public void onSuccess(String msg) {
        DialogLoading.LoadingGoogle(false,progressOrder);
        //cart
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyOrderActivity.this);
        LayoutInflater layoutInflater = getLayoutInflater();
        @SuppressLint("InflateParams") View viewDialogPickUp = layoutInflater.inflate(R.layout.custom_dialog_success, null);
        builder.setView(viewDialogPickUp);


        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        NavigationActivity.orderDetails.clear();
        NavigationActivity.numberBadge = 0;
        exploreFragmentCallback.OnClickBadge();

        Intent intent = new Intent(this,NavigationActivity.class);
        new Handler().postDelayed(() -> startActivity(intent), 3000);
    }

    @Override
    public void onFailed(String msg) {
  DialogLoading.LoadingGoogle(false,progressOrder);
        Toasty.error(MyOrderActivity.this, msg, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onCheckGiftSuccess(Gift gift) {
        txtResultCheckGift.setVisibility(View.VISIBLE);
        txtResultCheckGift.setTextColor(Color.BLUE);
        txtResultCheckGift.setText("√ Giảm giá " + gift.getNumber() + " %");
        try {

            totalPrice = Double.parseDouble(String.valueOf(Double.parseDouble(String.valueOf(totalPrice)) * (1 - (Double.parseDouble(gift.getNumber()) / 100))));

            Locale localeVN = new Locale("vi", "VN");
            NumberFormat vn = NumberFormat.getInstance(localeVN);
            String str1 = vn.format(totalPrice);

            txtTotalPrice.setText("Tổng tiền: " + str1 + " VNĐ");

        } catch (Exception e) {
            Log.d("LONgKUTE", "onCheckGiftSuccess: Exception " + e.getMessage());
        }

    }

    @Override
    public void onCheckGiftFailed(String msg) {
        txtResultCheckGift.setVisibility(View.VISIBLE);
        txtResultCheckGift.setTextColor(Color.RED);
        txtResultCheckGift.setText(msg);
    }
}
