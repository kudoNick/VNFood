package vn.poly.jeanshop.src.module.explore.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chabbal.slidingdotsplash.SlidingSplashView;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import vn.poly.jeanshop.NavigationActivity;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.OrderProvisional;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.model.Review;
import vn.poly.jeanshop.src.module.explore.adapter.ReviewAdapter;
import vn.poly.jeanshop.src.module.explore.presenter.IProductDetails;
import vn.poly.jeanshop.src.module.explore.presenter.PresenterProductDetails;
import vn.poly.jeanshop.src.module.myorder.view.MyOrderActivity;
import vn.poly.jeanshop.src.network.EndPoint;
import vn.poly.jeanshop.src.utils.DialogLoading;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductDetailsActivity extends AppCompatActivity implements IProductDetails.IViewProductTDetails {


    public static int comment = 0;

    private SlidingSplashView introProductDetail;
    private LinearLayout bodyProductDetail, layoutWriteComment;
    private TextView txtRateFoodDetails, txtCommentFoodDetails, txtDescription, txtNameFoodDetails, txtNoComment, tvTotalamount, tvSize, tvPriceProduct;
    private TextInputEditText edtComment;
    private ImageView imgFavouriteFoodDetails;
    private Spinner SpinerSize;
    private RatingBar rateBar;
    private boolean checkedFavourite = false;
    private Product product;
    private SharedPreferences sharedPreferences;
    private PresenterProductDetails presenterProductDetails;
    private RecyclerView recyclerReviewAdapter;
    private GoogleProgressBar progress_review;
    ExploreFragment exploreFragmentCallback = new ExploreFragment();

    private ImageView imgProduct;

    private String ketqua = "";
    private String size;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_product_details);
        init();
//        getSizeDevice();
        getDataProduct();
        handlerFavourite(product.getProductId());
        presenterProductDetails.getComment(product.getProductId());


        size = product.getSize();
        String[] DsSize = size.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, DsSize);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        SpinerSize.setAdapter(adapter);

        SpinerSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                ketqua = DsSize[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ketqua = "";
            }
        });


    }

    private void getDataProduct() {
        Intent intent = getIntent();
        if (intent != null) {
            product = intent.getParcelableExtra("product");
            assert product != null;

            txtNameFoodDetails.setText(product.getName());
            txtDescription.setText(product.getDescription());
            tvTotalamount.setText(product.getAmount());
            txtRateFoodDetails.setText("❤️" + " 4");
            txtCommentFoodDetails.setText("💬" + " 4");

            presenterProductDetails.getImages(product.getProductId());

            tvSize.setText(product.getSize());

            Glide.with(this)
                    .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_cart_loading))
                    .load(EndPoint.BASE_URL_PUBLIC + product.getImage())
                    .error(R.drawable.ic_error_outline_white_24dp).into(imgProduct);

            Locale localeVN = new Locale("vi", "VN");
            NumberFormat vn = NumberFormat.getInstance(localeVN);
            String str1 = vn.format(Double.parseDouble(product.getPrice()));
            tvPriceProduct.setText(str1 + " VNĐ");

            imgProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(ProductDetailsActivity.this, PhotoDetailActivity.class);
                    intent1.putExtra("photo", product.getImage());
                    startActivity(intent1);
                }
            });

        }
    }

    private boolean handlerFavourite(String productId) {
        sharedPreferences = getSharedPreferences("favourite", MODE_PRIVATE);
        checkedFavourite = sharedPreferences.getBoolean(productId, false);
        if (checkedFavourite) {
            imgFavouriteFoodDetails.setImageResource(R.drawable.ic_heart_checked);
        } else {
            imgFavouriteFoodDetails.setImageResource(R.drawable.ic_heart);
        }
        return checkedFavourite;
    }


    @SuppressLint("CutPasteId")
    private void init() {
        tvSize = findViewById(R.id.tvSize);

        imgProduct = findViewById(R.id.imgProduct);
        tvPriceProduct = findViewById(R.id.tvPriceProduct);
        introProductDetail = findViewById(R.id.introProductDetail);
        bodyProductDetail = findViewById(R.id.bodyProductDetail);
        TextView txtWriteComment = findViewById(R.id.txtWriteComment);
        layoutWriteComment = findViewById(R.id.layoutWriteComment);
        txtDescription = findViewById(R.id.txtDescription);
        tvTotalamount = findViewById(R.id.tvTotalamount);
        SpinerSize = findViewById(R.id.size);
        txtNameFoodDetails = findViewById(R.id.txtNameFoodDetails);
        txtRateFoodDetails = findViewById(R.id.txtRateFoodDetails);
        txtCommentFoodDetails = findViewById(R.id.txtCommentFoodDetails);
        ImageView imgBackFoodDetails = findViewById(R.id.imgBackFoodDetails);
        imgFavouriteFoodDetails = findViewById(R.id.imgFavouriteFoodDetails);
        Button btnOrderNowDetails = findViewById(R.id.btnOrderNowDetails);
        edtComment = findViewById(R.id.edtComment);
        Button btnSubmitComment = findViewById(R.id.btnSubmitComment);
        rateBar = findViewById(R.id.rateBar);
        recyclerReviewAdapter = findViewById(R.id.rcvComment);
        progress_review = findViewById(R.id.progress_review);
        txtNoComment = findViewById(R.id.txtNoComment);

        DialogLoading.LoadingGoogle(true, progress_review);

        presenterProductDetails = new PresenterProductDetails(this);
        txtWriteComment.setOnClickListener(v -> layoutWriteComment.setVisibility(View.VISIBLE));

        imgBackFoodDetails.setOnClickListener(v -> finish());

//        btnOrderNowDetails.setOnClickListener(v -> orderProduct());

        btnOrderNowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validForm()){
                    return;
                }
                orderProduct();
            }
        });

        imgFavouriteFoodDetails.setOnClickListener(v -> {
            checkedFavourite = handlerFavourite(product.getProductId());
            Editor editor = sharedPreferences.edit();
            if (!checkedFavourite) {
                editor.putBoolean(product.getProductId(), true);
                editor.apply();
                imgFavouriteFoodDetails.setImageResource(R.drawable.ic_heart_checked);
            } else {
                editor.putBoolean(product.getProductId(), false);
                editor.apply();
                imgFavouriteFoodDetails.setImageResource(R.drawable.ic_heart);
            }
        });

        btnSubmitComment.setOnClickListener(v -> handlerComment());

    }

    private boolean validForm() {


        Intent intent = getIntent();
        product = intent.getParcelableExtra("product");
        assert product != null;

        int amount = Integer.parseInt(product.getAmount());

        if (amount == 0) {
            Toast.makeText(this, "Sản phẩm đã hết vui lòng mua sản phẩm khác", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void orderProduct() {

        boolean exits = false;
        if (NavigationActivity.orderDetails.size() > 0) {
            for (OrderProvisional i : NavigationActivity.orderDetails) {
                if (!(product.getProductId().equalsIgnoreCase(i.getProduct().getProductId()))) {
                    exits = false;
                    continue;
                } else {
                    exits = true;
                    break;
                }
            }

            if (exits) {

                Toasty.warning(Objects.requireNonNull(ProductDetailsActivity.this), "Sản phẩm đã có trong giỏ hàng", Toasty.LENGTH_LONG).show();

            } else {

                Intent intent = getIntent();
                product = intent.getParcelableExtra("product");
                assert product != null;
                String name = product.getName();
                String image = product.getImage();
                Log.d("TAG"+name,"");

                OrderProvisional order = new OrderProvisional(product, 1, size, name,image);
                NavigationActivity.orderDetails.add(order);
                NavigationActivity.numberBadge = NavigationActivity.numberBadge + 1;


                Toasty.success(Objects.requireNonNull(ProductDetailsActivity.this), "Sản phẩm đã được thêm vào giỏ hàng", Toasty.LENGTH_LONG).show();
                Intent i = new Intent(this, MyOrderActivity.class);
                startActivity(i);


                exploreFragmentCallback.OnClickBadge();

            }
        } else {

            Intent intent = getIntent();
            product = intent.getParcelableExtra("product");
            assert product != null;
            String name = product.getName();
            String image = product.getImage();

            Log.d("TAG"+image,"");

            OrderProvisional order = new OrderProvisional(product, 1, size, name,image);
            NavigationActivity.orderDetails.add(order);
            NavigationActivity.numberBadge = NavigationActivity.numberBadge + 1;
            Toasty.success(Objects.requireNonNull(ProductDetailsActivity.this), "Sản phẩm đã được thêm vào giỏ hàng", Toasty.LENGTH_LONG).show();

            Intent i = new Intent(this, MyOrderActivity.class);
            startActivity(i);

            exploreFragmentCallback.OnClickBadge();
        }


    }

    @SuppressLint("CheckResult")
    private void handlerComment() {
        String comment = Objects.requireNonNull(edtComment.getText()).toString().trim();
        rateBar.setStepSize((float) 1.0);
        int rate = (int) rateBar.getRating();
        if (comment.isEmpty()) {
            edtComment.setError("");
        }
        if (rate == 0) {
            Toasty.warning(Objects.requireNonNull(ProductDetailsActivity.this), "Rate?", Toasty.LENGTH_LONG);
        }
        DialogLoading.LoadingGoogle(true, progress_review);

        presenterProductDetails.addCommentToServer(comment, product.getProductId(), rate);

    }

    @Override
    public void onSuccessAddComment(String msg) {
        txtNoComment.setVisibility(View.GONE);
        edtComment.setText("");
        rateBar.setRating(0);
        DialogLoading.LoadingGoogle(false, progress_review);
        Toasty.success(Objects.requireNonNull(ProductDetailsActivity.this), "Bình luận thành công", Toasty.LENGTH_LONG).show();
        layoutWriteComment.setVisibility(View.GONE);
        presenterProductDetails.getComment(product.getProductId());

    }

    @Override
    public void onFailedAddComment(String msg) {

        DialogLoading.LoadingGoogle(false, progress_review);
        Toasty.warning(Objects.requireNonNull(ProductDetailsActivity.this), "Có lỗi xảy ra, vui lòng thử lại!", Toasty.LENGTH_LONG).show();

    }

    @Override
    public void onSuccessGetComment(List<Review> reviews) {
        DialogLoading.LoadingGoogle(false, progress_review);

        if (!reviews.isEmpty()) {
            txtNoComment.setVisibility(View.GONE);
            showReview(reviews);
        } else {
            txtNoComment.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailedGetComment(String msg) {
        Toasty.error(getApplicationContext(), msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onSuccessGetImages(String[] images) {
        if (images != null) {
            introProductDetail.setImageResources(images);
            introProductDetail.setAutoPage();
        } else {
            introProductDetail.setImageResources(new String[]{EndPoint.BASE_URL_PUBLIC + product.getImage()});
        }
    }

    @Override
    public void onFailGetImages(String msg) {

    }

    private void showReview(List<Review> reviews) {
        ReviewAdapter reviewAdapter = new ReviewAdapter(ProductDetailsActivity.this, reviews);
        recyclerReviewAdapter.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.VERTICAL, true));
        recyclerReviewAdapter.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();

        comment = reviews.size();
    }

}
