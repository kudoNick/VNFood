package vn.poly.jeanshop.src.module.explore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.model.Review;
import vn.poly.jeanshop.src.module.explore.IOnClickProduct;
import vn.poly.jeanshop.src.module.explore.presenter.IProductDetails;
import vn.poly.jeanshop.src.module.explore.presenter.PresenterProductDetails;
import vn.poly.jeanshop.src.module.explore.view.ProductDetailsActivity;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.EndPoint;
import vn.poly.jeanshop.src.network.IApiVnFood;

public class NewFoodAdapter extends RecyclerView.Adapter<NewFoodAdapter.ViewHolderNewFood> {
    private Context context;
    private int resource;
    private List<Product> productList;
    private IOnClickProduct iOnClickProduct;
    private String titleMar;

    IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);

    List<Review> reviewList;
    public NewFoodAdapter(Context context, int resource, List<Product> productList,IOnClickProduct iOnClickProduct,String titleMar){
        this.context = context;
        this.resource = resource;
        this.productList = productList;
        this.iOnClickProduct = iOnClickProduct;
        this.titleMar = titleMar;

    }

    @NonNull
    @Override
    public NewFoodAdapter.ViewHolderNewFood onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
//        ViewGroup.LayoutParams paramsBody = view.getLayoutParams();
//        paramsBody.width = (int) (StoreKey.getSize().x /1.1);
//        view.setLayoutParams(paramsBody);
        return new ViewHolderNewFood(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewFoodAdapter.ViewHolderNewFood holder, int position) {
        Product product = productList.get(position);
        holder.txtNameFood.setText(product.getName());
//        holder.txtRate.setText(String.valueOf(product.getRate()));
        holder.txtComment.setText("10");
        String[] quickAction = {
                "❤️",
                "😍️",
                "🤩",
                "☺️️",
                "😛",
                "😉",
                "👏",
                "🤘"};
        int randomQuick = new Random().nextInt(7);
        holder.txtTitleMar.setText(titleMar + " " +quickAction[randomQuick]);
        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_cart_loading))
                .load(EndPoint.BASE_URL_PUBLIC + product.getImage())
                .error(R.drawable.ic_error_outline_white_24dp).into(holder.imgFood);

        holder.layout_item_new_food.setOnClickListener(v -> {
            iOnClickProduct.OnClickProductDetails(product);
        });
        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        String str1 = vn.format(Double.parseDouble(product.getPrice()));
        holder.tvPriceProduct.setText(str1 + " VNĐ");
        //
        reviewList = new ArrayList<>();

        Call<BaseResponse<List<Review>>> callReview = apiService.getListReview(product.getProductId());
        callReview.enqueue(new Callback<BaseResponse<List<Review>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Review>>> call, Response<BaseResponse<List<Review>>> response) {
                if(response.isSuccessful()){

//                    presenterProductDetails.resultGetComment(true,response.body().getData());
                    reviewList = response.body().getData();

                }else {
//                    presenterProductDetails.resultGetComment(false,  null);
                }
                holder.txtComment.setText( "💬 " + String.valueOf(reviewList.size()) );
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Review>>> call, Throwable t) {

            }
        });

        holder.txtRate.setText( "❤️" + " 5");

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ViewHolderNewFood extends RecyclerView.ViewHolder {
        TextView txtNameFood, txtRate, txtComment,txtTitleMar,tvPriceProduct;
        RoundedImageView imgFood;
        CardView layout_item_new_food;

        ViewHolderNewFood(@NonNull View itemView) {
            super(itemView);
            layout_item_new_food = itemView.findViewById(R.id.layout_item_new_food);
            txtNameFood = itemView.findViewById(R.id.txtNameFoodNew);
            txtRate = itemView.findViewById(R.id.txtRateFoodNew);
            txtComment = itemView.findViewById(R.id.txtCommentFoodNew);
            txtTitleMar = itemView.findViewById(R.id.txtTitleMar);
            imgFood = itemView.findViewById(R.id.imgFoodNew);
            txtNameFood.setMaxLines(1);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
        }
    }

}
