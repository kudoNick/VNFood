package vn.poly.jeanshop.src.module.billDetail.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.module.explore.view.PhotoDetailActivity;
import vn.poly.jeanshop.src.network.EndPoint;

public class BillDetailAdapter extends RecyclerView.Adapter<BillDetailAdapter.BillDetailHolder> {

    Context context;
    List<OrderDetails> orderDetailsList;

    public BillDetailAdapter(Context context, List<OrderDetails> orderDetails) {
        this.context = context;
        this.orderDetailsList = orderDetails;
    }

    @NonNull
    @Override
    public BillDetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill_detail, null);
        BillDetailHolder billDetailHolder = new BillDetailHolder(view);
        return billDetailHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BillDetailHolder holder, int position) {
        OrderDetails orderDetails = orderDetailsList.get(position);


        Locale localeVN = new Locale("vi", " VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        String str1 = vn.format(orderDetails.getPrice());
        holder.tvPriceProduct.setText(str1 + " VNĐ");
        holder.tvAmountProduct.setText(String.valueOf(orderDetails.getAmount()));
        holder.size.setText(String.valueOf(orderDetails.getSize()));
        holder.tvNameProduct.setText("Sản phẩm: "+orderDetails.getProducts());

        Glide.with(context)
                .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_cart_loading))
                .load(EndPoint.BASE_URL_PUBLIC + orderDetails.getImage())
                .error(R.drawable.ic_error_outline_white_24dp).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                intent.putExtra("photo",orderDetails.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }

    public class BillDetailHolder extends RecyclerView.ViewHolder {
        private  TextView tvNameProduct,tvPriceProduct, tvAmountProduct,size;
        private ImageView image;

        public BillDetailHolder(@NonNull View itemView) {
            super(itemView);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvAmountProduct = itemView.findViewById(R.id.tvAmountProduct);
            size = itemView.findViewById(R.id.size);
            image = itemView.findViewById(R.id.image);
        }
    }
}
