package vn.poly.jeanshop.src.module.explore.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jsibbold.zoomage.ZoomageView;

import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.network.EndPoint;

public class PhotoDetailActivity extends AppCompatActivity {

    private ImageView imgProduct;
    private ZoomageView img1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        imgProduct = findViewById(R.id.imgProduct);
        img1 = findViewById(R.id.img1);

        Intent intent = getIntent();
        String photo = intent.getStringExtra("photo");

        Glide.with(this)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_cart_loading))
                .load(EndPoint.BASE_URL_PUBLIC + photo)
                .error(R.drawable.ic_error_outline_white_24dp).into(img1);

    }

    public void back(View view) {
        finish();
    }
}