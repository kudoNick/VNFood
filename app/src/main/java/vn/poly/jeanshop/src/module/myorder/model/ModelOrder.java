package vn.poly.jeanshop.src.module.myorder.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.Gift;
import vn.poly.jeanshop.src.model.Order;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.module.myorder.presenter.PresenterOrder;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.ErrorUtils;

public class ModelOrder {
    IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);

    public void addToCart(Order order, List<OrderDetails> orderDetails, PresenterOrder presenterOrder) {
        Gson gson = new GsonBuilder().create();

        Call<BaseResponse<String>> callOrder = apiService.addOrder(gson.toJson(orderDetails), gson.toJson(order));

        callOrder.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) {
                    presenterOrder.resultAddCart(true, response.body().getData());
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterOrder.resultAddCart(false, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                presenterOrder.resultAddCart(false, t.getMessage());

            }
        });
    }

    public void checkGift(String codeGift, PresenterOrder presenterOrder) {
        Call<BaseResponse<Gift>> callGift = apiService.checkGift(codeGift);
        callGift.enqueue(new Callback<BaseResponse<Gift>>() {
            @Override
            public void onResponse(Call<BaseResponse<Gift>> call, Response<BaseResponse<Gift>> response) {
                if (response.isSuccessful()) {
                    presenterOrder.resultCheckGift(true, response.body().getData(), "");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterOrder.resultCheckGift(false, null, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Gift>> call, Throwable t) {
                presenterOrder.resultCheckGift(false, null, t.getMessage());

            }
        });
    }
}
