package vn.poly.jeanshop.src.module.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.model.Review;
import vn.poly.jeanshop.src.module.explore.IOnClickProduct;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.EndPoint;
import vn.poly.jeanshop.src.network.IApiVnFood;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> implements Filterable {

    IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);

    Context context;
    List<Product> productList;
    List<Product> getProductList;
    Filter filter;

    private IOnClickProduct iOnClickProduct;
    List<Review> reviewList;
    public SearchAdapter(Context context, List<Product> productList, IOnClickProduct iOnClickProduct) {
        this.context = context;
        this.productList = productList;
        this.getProductList = productList;
        this.iOnClickProduct = iOnClickProduct;
    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_product, null);
        SearchHolder searchHolder = new SearchHolder(view);

        return searchHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        Product product = productList.get(position);
//        Product product1 = getProductList.get(position);

        holder.txtNameFood.setText(product.getName());

        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_cart_loading))
                .load(EndPoint.BASE_URL_PUBLIC + product.getImage())
                .error(R.drawable.ic_error_outline_white_24dp).into(holder.imgFood);
        holder.tvPriceProduct.setText(product.getPrice());

        holder.layout_item_new_food.setOnClickListener(v -> {
            iOnClickProduct.OnClickProductDetails(product);
        });

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

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class SearchHolder extends RecyclerView.ViewHolder {

        TextView txtNameFood, txtRate, txtComment,txtTitleMar,tvPriceProduct;
        RoundedImageView imgFood;
        CardView layout_item_new_food;
        public SearchHolder(@NonNull View itemView) {
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

    public void resetData(){
        productList = getProductList;
    }
    @Override
    public Filter getFilter() {
        if (filter == null){
            Toast.makeText(context,"moi nhap",Toast.LENGTH_SHORT).show();
        }
        filter = new CustomFilter();
        return filter;
    }

    private class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = getProductList;
                results.count = getProductList.size();
            } else {
                List<Product> dataArrayList = new ArrayList<Product>();
                for (Product p : productList) {
                    if (p.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()))
                        dataArrayList.add(p);
                }
                results.values = dataArrayList;
                results.count = dataArrayList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count == 0)
//                notifyDataSetInvalidated();
                notifyDataSetChanged();

            else {
                productList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        }
    }


}
