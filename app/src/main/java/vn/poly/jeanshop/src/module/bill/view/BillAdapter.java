package vn.poly.jeanshop.src.module.bill.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.Order;
import vn.poly.jeanshop.src.model.OrderBill;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.module.billDetail.view.BillDetailActivity;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillHolder> {
    Gson gson = new Gson();
    Context context;
    List<OrderBill> orderList;
    public BillAdapter(Context context, List<OrderBill> orders) {
        this.context = context;
        this.orderList = orders;
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bill, null);
        BillHolder billHolder = new BillHolder(view);
        return billHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, int position) {
        OrderBill order = orderList.get(position);

        holder.tvIdOder.setText(String.valueOf(order.getOrderId()));

        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        String str1 = vn.format(order.getTotalPrice());
        holder.tvTotalPrice.setText(str1 +"VNĐ");

        holder.tvTotalAmount.setText(String.valueOf(order.getTotalAmount()));
        holder.status.setText(String.valueOf(order.getStatus()));
        holder.tvDayBuy.setText(order.getCreated_at());

        holder.tvdateOff.setText(order.getUpdate_at());

        String a = order.getStatus();
        if (a.equals("Chờ xác nhận")){
            holder.goneNofidy.setVisibility(View.VISIBLE);
        }
        if (a.equals("Đang giao hàng")){
            holder.goneDateOff.setVisibility(View.VISIBLE);
            holder.goneNofidy.setVisibility(View.VISIBLE);
            holder.goneNofidy.setText("Sản phẩm sẽ được giao trong 3 ngày tới bạn chú ý điện thoại nhé!");

        }if (a.equals("Giao hàng thành công")){
            holder.goneDateOff.setVisibility(View.VISIBLE);
            holder.goneNofidy.setVisibility(View.VISIBLE);
            holder.goneNofidy.setText("Bạn ơi! bạn đã nhận hàng rồi ạ? nếu có điều gì khiến bạn cảm thấy chưa hài lòng hãy cho shop biết để shop hỗ trợ bạn nhé. nếu bạn hài lòng đừng tiếc tặng shop 5* kèm feedback nha. cảm ơn khách yêu nhiều!");
        }


//        holder.layoutCateItem.setOnClickListener(v -> iOnClick.onClickGetProductForCate(cate.getCateId(),cate.getName()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orderString = order.getOrderId();

                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("orderString", orderString);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class BillHolder extends RecyclerView.ViewHolder {
        public TextView tvIdOder,tvTotalAmount, tvTotalPrice,tvDayShip,status,tvDayBuy, tvdateOff,goneNofidy;
        public LinearLayout goneDateOff;
        CardView layoutCateItem;
        public BillHolder(@NonNull View itemView) {
            super(itemView);
            layoutCateItem = itemView.findViewById(R.id.layoutCateItem);
            tvIdOder = itemView.findViewById(R.id.tvIdOder);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            status = itemView.findViewById(R.id.status);
            tvDayBuy = itemView.findViewById(R.id.tvDayBuy);
//            tvDayShip = itemView.findViewById(R.id.tvDayShip);
            tvdateOff = itemView.findViewById(R.id.tvdateOff);
            goneDateOff = itemView.findViewById(R.id.goneDateOff);
            goneNofidy = itemView.findViewById(R.id.goneNofidy);

        }
    }
}
