package vn.poly.jeanshop.src.module.myorder.presenter;

import java.util.List;

import vn.poly.jeanshop.src.model.Gift;
import vn.poly.jeanshop.src.model.Order;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.module.myorder.IOrder;
import vn.poly.jeanshop.src.module.myorder.model.ModelOrder;

public class PresenterOrder implements IOrder.IPresenterOrder {
    IOrder.IViewOrder iViewOrder;
    ModelOrder modelOrder;

    public PresenterOrder(IOrder.IViewOrder iViewOrder) {
        this.iViewOrder = iViewOrder;
        modelOrder = new ModelOrder();
    }


    @Override
    public void addCartToServer(Order order, List<OrderDetails> orderDetails) {
        modelOrder.addToCart(order,orderDetails,this);
    }

    @Override
    public void resultAddCart(boolean result, String msg) {
        if(result){
            iViewOrder.onSuccess(msg);
        }else {
            iViewOrder.onFailed(msg);
        }
    }

    @Override
    public void checkGift(String codeGift) {
        modelOrder.checkGift(codeGift,this);
    }

    @Override
    public void resultCheckGift(boolean result, Gift gift, String msg) {
        if(result){
            iViewOrder.onCheckGiftSuccess(gift);
        }else{
            iViewOrder.onCheckGiftFailed(msg);
        }
    }




}
