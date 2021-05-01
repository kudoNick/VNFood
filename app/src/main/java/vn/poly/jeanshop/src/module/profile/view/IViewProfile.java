package vn.poly.jeanshop.src.module.profile.view;

import vn.poly.jeanshop.src.model.User;

public interface IViewProfile {
    void onSuccess(User user);
    void onFailed(String msg);

    void onChangePasswordSuccess(String msg);
    void onChangePasswordFail(String msg);
}
