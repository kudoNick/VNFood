package vn.poly.jeanshop.src.module.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.Images;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.module.explore.IOnClickProduct;
import vn.poly.jeanshop.src.module.explore.presenter.IProduct;
import vn.poly.jeanshop.src.module.explore.presenter.PresenterProduct;
import vn.poly.jeanshop.src.module.explore.view.ProductDetailsActivity;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.ErrorUtils;

public class SearchActivity extends AppCompatActivity implements IOnClickProduct {
    IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);
    private EditText edtSearch;
    private RecyclerView rcvSearch;

    private List<Product> productList;
    private SearchAdapter searchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
               thamChieu();
               listProduct();

               edtSearch.addTextChangedListener(new TextWatcher() {
                   @Override
                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                   }

                   @Override
                   public void onTextChanged(CharSequence s, int start, int before, int count) {
                   }

                   @Override
                   public void afterTextChanged(Editable s) {
                       searchAdapter.getFilter().filter(s);
                   }
               });


    }

    private void thamChieu() {
        Toolbar toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());
        rcvSearch = findViewById(R.id.rcvShearch);
        edtSearch = findViewById(R.id.edtSearch);

    }

    private void loadData(){
        if (!productList.isEmpty()) {
            searchAdapter = new SearchAdapter(this, productList, SearchActivity.this);
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
            rcvSearch.setLayoutManager(layoutManager);
            rcvSearch.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
        }

    }

    public void listProduct() {
        productList = new ArrayList<>();
        Call<BaseResponse<List<Product>>> callProduct = apiService.getListProduct();
        callProduct.enqueue(new Callback<BaseResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Product>>> call, Response<BaseResponse<List<Product>>> response) {
                if (response.isSuccessful()) {
                    productList = response.body().getData();
//                    for (int i = 0; i <productList.size() ; i++) {
//                        productList.get(i).getName();
//                    }
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                }

                loadData();
            }
            @Override
            public void onFailure(Call<BaseResponse<List<Product>>> call, Throwable t) {

            }
        });


    }

    @Override
    public void OnClickProductDetails(Product product) {
        Intent intent = new Intent(this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void OnClickBadge() {

    }
}