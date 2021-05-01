package vn.poly.jeanshop.src.module.explore.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.google.gson.Gson;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import vn.poly.jeanshop.NavigationActivity;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.module.explore.IOnClickProduct;
import vn.poly.jeanshop.src.module.explore.adapter.NewFoodAdapter;
import vn.poly.jeanshop.src.module.explore.presenter.IBanner;
import vn.poly.jeanshop.src.module.explore.presenter.IProduct;
import vn.poly.jeanshop.src.module.explore.presenter.PresenterBanner;
import vn.poly.jeanshop.src.module.explore.presenter.PresenterProduct;
import vn.poly.jeanshop.src.module.search.SearchActivity;
import vn.poly.jeanshop.src.utils.DialogLoading;
import vn.poly.jeanshop.src.utils.ItemOffsetDecoration;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements IProduct.IViewProduct, IBanner.IViewBanner, IOnClickProduct {
    private RecyclerView recyclerNewFood, recyclerAllProduct;
    private SlidingSplashView splashExplore;
    private List<Product> products, productsNew;
    private GoogleProgressBar progressBarExplore;
    private static NotificationBadge badge;
    private TextView edtSearch;
    private NestedScrollView nestedScrollMenu;
    private PresenterProduct presenterProduct;
    private androidx.appcompat.widget.Toolbar toolbarExplore;
    private Gson gson = new Gson();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        init(view);
        onScrollListener();

        return view;
    }

    @SuppressLint("WrongConstant")
    private void onScrollListener() {
        nestedScrollMenu.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0) {
                toolbarExplore.setVisibility(View.GONE);

            }
            if (scrollY > oldScrollY) {

                toolbarExplore.setVisibility(View.VISIBLE);

                toolbarExplore.setTitle("VN HiepJean");

                //hide
                NavigationActivity.navigation.setVisibility(View.GONE);

                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        NavigationActivity.navigation.getHeight(),  // fromYDelta
                        0);                // toYDelta
                animate.setDuration(200);
                animate.setFillAfter(true);
                NavigationActivity.navigation.startAnimation(animate);


            } else if (scrollY < oldScrollY) {
                toolbarExplore.setVisibility(View.VISIBLE);

                toolbarExplore.setTitle("VN HiepJean");

                //show
                NavigationActivity.navigation.setVisibility(View.VISIBLE);
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        NavigationActivity.navigation.getHeight(),  // fromYDelta
                        0);                // toYDelta
                animate.setDuration(200);
                animate.setFillAfter(true);
                NavigationActivity.navigation.startAnimation(animate);

            }
        });
    }


    private void init(View view) {
        nestedScrollMenu = view.findViewById(R.id.nestedScrollMenu);
        toolbarExplore = view.findViewById(R.id.toolbarExplore);
        recyclerNewFood = view.findViewById(R.id.recyclerNewFood);
        splashExplore = view.findViewById(R.id.splashExplore);
        edtSearch = view.findViewById(R.id.edtSearch);
        progressBarExplore = view.findViewById(R.id.progressExplore);
        recyclerAllProduct = view.findViewById(R.id.recyclerAllProduct);
        FrameLayout framBadge = view.findViewById(R.id.layout_Badge);
        badge = framBadge.findViewById(R.id.badge);
        Button btnViewAllProduct = view.findViewById(R.id.btnViewAllProduct);
        PresenterBanner presenterBanner = new PresenterBanner(this);

        products = new ArrayList<>();
        productsNew = new ArrayList<>();


        btnViewAllProduct.setOnClickListener(v -> viewAllProduct());

        if (products != null) {
            products.clear();
            badge.setNumber(NavigationActivity.numberBadge);
        }
        presenterProduct = new PresenterProduct(this);

        DialogLoading.LoadingGoogle(true, progressBarExplore);


        presenterProduct.getListProductMore(new Random().nextInt(5));

        presenterProduct.getNewListProduct();

        presenterBanner.getBanner();

        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                String s = gson.toJson(products);
                intent.putExtra("product", s);
                startActivity(intent);
            }
        });

    }




    private void viewAllProduct() {
        startActivity(new Intent(getActivity(), AllProductActivity.class));
    }

    private void loadData() {

        NewFoodAdapter newFoodAdapter = new NewFoodAdapter(getActivity(), R.layout.custom_layout_new_food, productsNew, this, "News");
        NewFoodAdapter allFoodAdapter = new NewFoodAdapter(getActivity(), R.layout.custom_layout_new_food, products, this, "All");

        recyclerNewFood.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerNewFood.setNestedScrollingEnabled(false);
        recyclerNewFood.setAdapter(newFoodAdapter);

        newFoodAdapter.notifyDataSetChanged();

        recyclerAllProduct.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerAllProduct.setAdapter(allFoodAdapter);
        allFoodAdapter.notifyDataSetChanged();


    }

    @Override
    public void onGetListProductSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false, progressBarExplore);
        products.addAll(productList);
        loadData();

    }

    @Override
    public void onGetListProductFailed(String msg) {
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onGetListNewFoodSuccess(List<Product> productList) {
        DialogLoading.LoadingGoogle(false, progressBarExplore);
        productsNew.addAll(productList);
        loadData();
    }

    @Override
    public void onGetListNewFoodFailed(String msg) {
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();

    }

    @Override
    public void OnClickProductDetails(Product product) {

        Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);


    }

    @Override
    public void OnClickBadge() {
        try {
            badge.setNumber(NavigationActivity.numberBadge);

        } catch (Exception e) {
            Log.d("LONgKUTE", "OnClickBadge: " + e.getMessage());
        }

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("LONgKUTE", "onDestroy: explore");
//    }


    @Override
    public void onSuccessGetBanner(String[] listBanner) {
        splashExplore.setImageResources(listBanner);
        splashExplore.setAutoPage();
    }

    @Override
    public void onFailGetBanner(String msg) {
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }
}
