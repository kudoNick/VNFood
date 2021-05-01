package vn.poly.jeanshop.src.module.register.model;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.ErrorResponse;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.module.register.presenter.PresenterRegister;
import vn.poly.jeanshop.src.network.APIVnFood;
import vn.poly.jeanshop.src.network.IApiVnFood;
import vn.poly.jeanshop.src.utils.ErrorUtils;

public class ModelRegister {
    public void register(User user, final PresenterRegister presenterRegister) {
        IApiVnFood apiService = APIVnFood.getAPIVnFood().create(IApiVnFood.class);
        Call<BaseResponse<User>> call = apiService.handlerRegister(user.getEmail(), user.getPassword(), user.getPhone(), user.getAddress(), user.getUsername());
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    presenterRegister.resultRegister(true, "Đăng ký thành công");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterRegister.resultRegister(false, err.getStatusCode() + " - " + err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                presenterRegister.resultRegister(false, t.getMessage());

            }
        });
    }
}

