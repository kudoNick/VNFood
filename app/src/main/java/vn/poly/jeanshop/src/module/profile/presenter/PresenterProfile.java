package vn.poly.jeanshop.src.module.profile.presenter;

import okhttp3.MultipartBody;
import vn.poly.jeanshop.src.model.User;
import vn.poly.jeanshop.src.module.profile.model.ModelProfile;
import vn.poly.jeanshop.src.module.profile.view.IViewProfile;

public class PresenterProfile implements IPresenterProfile{
    private IViewProfile iViewProfile;
    private ModelProfile modelProfile;

    public PresenterProfile(IViewProfile iViewProfile) {
        this.iViewProfile = iViewProfile;
        modelProfile = new ModelProfile();
    }

    @Override
    public void getProfile() {
        modelProfile.profile(this);
    }

    @Override
    public void resultGetProfile(boolean success, User user,String msg) {
        if(success){
            iViewProfile.onSuccess(user);
        }else{
            iViewProfile.onFailed(msg);
        }
    }

    @Override
    public void uploadImage(MultipartBody.Part value) {
        modelProfile.uploadImageToServer(value);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        modelProfile.changePassword(oldPassword,newPassword,this);
    }


    public void changeProfile(String userId,String phone, String address,String username) {
        modelProfile.changeProfile(userId,phone,address,username);
    }

    @Override
    public void resultChangePassword(boolean success, String msg) {
        if(success){
            iViewProfile.onChangePasswordSuccess(msg);
        }else {
            iViewProfile.onChangePasswordFail(msg);
        }
    }


}
