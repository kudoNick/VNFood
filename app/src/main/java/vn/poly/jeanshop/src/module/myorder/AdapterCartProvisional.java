package vn.poly.jeanshop.src.module.myorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import vn.poly.jeanshop.R;
import vn.poly.jeanshop.src.model.OrderProvisional;
import vn.poly.jeanshop.src.network.EndPoint;

public class AdapterCartProvisional extends RecyclerView.Adapter<AdapterCartProvisional.ViewHolderCartProvisional> {
    private Context context;
    private List<OrderProvisional> orderProvisionals;
    private IOnClickCart iOnClickCart;

    public static String chooseSize = "";
    public static String chooseToTalAmon = "";

    public AdapterCartProvisional(Context context, List<OrderProvisional> orderProvisionals,IOnClickCart iOnClickCart) {
        this.context = context;
        this.orderProvisionals = orderProvisionals;
        this.iOnClickCart = iOnClickCart;
    }

    @NonNull
    @Override
    public ViewHolderCartProvisional onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_order, parent, false);
        return new ViewHolderCartProvisional(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCartProvisional holder, int position) {
        OrderProvisional orderProvisional = orderProvisionals.get(position);
        Glide.with(context).load(EndPoint.BASE_URL_PUBLIC + orderProvisional.getProduct().getImage()).into(holder.imgProductCart);
        holder.txtNameProductCart.setText(orderProvisional.getProduct().getName());


        Locale localeVN = new Locale("vi", "VN");
        NumberFormat vn = NumberFormat.getInstance(localeVN);
        String str1 = vn.format(Double.parseDouble(orderProvisional.getProduct().getPrice()));
        holder.txtPriceProductCart.setText(str1 + "VNĐ");

        holder.numberCart.setText(orderProvisional.getAmount() + "");

        holder.tvTotalAmount.setText("Kho: " + orderProvisional.getProduct().getAmount());
//        chooseToTalAmon = orderProvisional.getProduct().getAmount();


        holder.increase.setOnClickListener(v ->{
            iOnClickCart.increase(orderProvisional,holder.numberCart);
        });
        holder.reduce.setOnClickListener(v ->{
            iOnClickCart.reduce(orderProvisional,holder.numberCart);
        });

        String size = orderProvisional.getSize();
        String [] DsSize = size.split(",");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, android.R.layout.simple_spinner_item,DsSize);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        holder.spnSize.setAdapter(adapter);

        holder.spnSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                chooseSize = DsSize[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chooseSize = "";
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderProvisionals.size();
    }

    public class ViewHolderCartProvisional extends RecyclerView.ViewHolder {
        RoundedImageView imgProductCart;
        TextView txtNameProductCart,txtPriceProductCart,
                increase,reduce,tvTotalAmount;
        Spinner spnSize;
        TextView numberCart;
        public ViewHolderCartProvisional(@NonNull View itemView) {
            super(itemView);
            imgProductCart = itemView.findViewById(R.id.imgProductCart);
            txtNameProductCart = itemView.findViewById(R.id.txtNameProductCart);
            txtPriceProductCart = itemView.findViewById(R.id.txtPriceProductCart);
            increase = itemView.findViewById(R.id.increase);
            reduce = itemView.findViewById(R.id.reduce);
            numberCart = itemView.findViewById(R.id.numberCart);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            spnSize = itemView.findViewById(R.id.spnSize);
        }
    }
}
