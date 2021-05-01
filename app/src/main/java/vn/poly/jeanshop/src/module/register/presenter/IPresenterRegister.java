package vn.poly.jeanshop.src.module.register.presenter;


import vn.poly.jeanshop.src.model.User;

public interface IPresenterRegister {
    void handlerRegister(User user);
    void resultRegister(boolean success, String msg);
}
