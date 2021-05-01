package vn.poly.jeanshop.src.module.myorder;

import java.util.List;

import vn.poly.jeanshop.src.model.Gift;
import vn.poly.jeanshop.src.model.Order;
import vn.poly.jeanshop.src.model.OrderDetails;

public interface IOrder {
    interface IPresenterOrder{
        void addCartToServer(Order order,  List<OrderDetails> orderDetails);
        void resultAddCart(boolean result, String msg);


        void checkGift(String codeGift);
        void resultCheckGift(boolean result, Gift gift, String msg);


    }
    interface IViewOrder{
        void onSuccess(String msg);
        void onFailed(String msg);

        void onCheckGiftSuccess(Gift gift);
        void onCheckGiftFailed(String msg);
    }
}
