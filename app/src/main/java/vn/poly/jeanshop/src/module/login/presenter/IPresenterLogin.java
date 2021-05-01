package vn.poly.jeanshop.src.module.login.presenter;


public interface IPresenterLogin {
    void handlerLogin(String email,String password);
    void resultLogin(boolean success, String msg);

    void logout(String token);
}
