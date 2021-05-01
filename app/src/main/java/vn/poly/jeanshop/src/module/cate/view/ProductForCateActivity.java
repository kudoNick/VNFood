package vn.poly.jeanshop.src.module.cate.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.List;

import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.module.cate.ICate;
import vn.poly.jeanshop.src.module.cate.presenter.PresenterCate;
import vn.poly.jeanshop.src.module.explore.IOnClickProduct;
import vn.poly.jeanshop.src.module.explore.adapter.NewFoodAdapter;
import vn.poly.jeanshop.src.module.explore.view.ProductDetailsActivity;
import vn.poly.jeanshop.src.utils.DialogLoading;
import vn.poly.jeanshop.src.utils.ItemOffsetDecoration;

public class ProductForCateActivity extends AppCompatActivity implements ICate.IViewProductForCate, IOnClickProduct {

    private PresenterCate presenterCate;
    private RecyclerView recyclerViewFoodCate;
    private TextView txtCateName;
    private GoogleProgressBar progress_productForCate;
    Toolbar toolbar_MyOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prodduct_for_cate);
        initView();



    }

    private void initView() {
        toolbar_MyOrder = findViewById(R.id.toolbar_MyOrder);
        toolbar_MyOrder.setNavigationOnClickListener(v -> finish());

        recyclerViewFoodCate = findViewById(R.id.recyclerViewFoodCate);
        progress_productForCate = findViewById(R.id.progress_productForCate);
        txtCateName = findViewById(R.id.txtCateName);


        DialogLoading.LoadingGoogle(true,progress_productForCate);
        presenterCate = new PresenterCate(null,this);
        Intent intent = getIntent();
        if(intent != null) {
            presenterCate.getProductForCate(intent.getStringExtra("cateId"));
            txtCateName.setText(intent.getStringExtra("cateName"));
            toolbar_MyOrder.setTitle(intent.getStringExtra("cateName"));
        }
    }

    @Override
    public void onGetListProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false,progress_productForCate);

        NewFoodAdapter foodAdapter = new NewFoodAdapter(ProductForCateActivity.this, R.layout.custom_layout_new_food, productList, this, "");
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin10);

        recyclerViewFoodCate.setLayoutManager(new GridLayoutManager(ProductForCateActivity.this, 2));
        recyclerViewFoodCate.addItemDecoration(new ItemOffsetDecoration(spacingInPixels));
        recyclerViewFoodCate.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();

    }

    @Override
    public void onGetListProductFailed(String msg) {
        DialogLoading.LoadingGoogle(false,progress_productForCate);

        new AlertDialog.Builder(ProductForCateActivity.this)
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {
                    finish();
                })
                .show();
    }

    @Override
    public void OnClickProductDetails(Product product) {
        Intent intent = new Intent(ProductForCateActivity.this, ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    @Override
    public void OnClickBadge() {

    }
}
