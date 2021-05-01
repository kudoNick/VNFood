package vn.poly.jeanshop.src.network;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vn.poly.jeanshop.src.model.BaseResponse;
import vn.poly.jeanshop.src.model.Cate;
import vn.poly.jeanshop.src.model.Gift;
import vn.poly.jeanshop.src.model.Images;
import vn.poly.jeanshop.src.model.OrderBill;
import vn.poly.jeanshop.src.model.OrderDetails;
import vn.poly.jeanshop.src.model.Product;
import vn.poly.jeanshop.src.model.Review;
import vn.poly.jeanshop.src.model.User;


public interface IApiVnFood {

    //user
    @FormUrlEncoded
    @POST("users/login")
    Call<BaseResponse<User>> handlerLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("users/register")
    Call<BaseResponse<User>> handlerRegister(
                                @Field("email") String email,
                               @Field("password") String password,
                               @Field("phone") String phone,
                               @Field("address") String address,
                               @Field("username") String userName);

    @FormUrlEncoded
    @POST("users/change-password")
    Call<BaseResponse<String>> handlerChangePassword(@Field("oldPassword") String password, @Field("newPassword") String newPassword);



    @GET("users/profile")
    Call<BaseResponse<User>> getProfile();

    @Multipart
    @POST("upload/photo")
    Call<String> uploadImages(@Part MultipartBody.Part photo);

    //product
    @GET("products/list")
    Call<BaseResponse<List<Product>>> getListProduct();

    @GET("products/new_list")
    Call<BaseResponse<List<Product>>> getNewListProduct();

    @GET("products/list_paging")
    Call<BaseResponse<List<Product>>> getListProductPaging(@Query("page") int page);

    @GET("products/list/{id}")
    Call<BaseResponse<List<Product>>> getListProductForCate(@Path("id") String cateId);

    @GET("products/images/{id}")
    Call<BaseResponse<List<Images>>> getImagesProduct(@Path("id") String productId);

    @GET("products/search")
    Call<BaseResponse<List<Product>>> getSearchProduct(@Query("search") String query);

    //branch: tu
    @GET("orders/list/{id}")
    Call<BaseResponse<List<OrderBill>>> getListOrderUser(@Path("id") String cateId);

    //branch: tu
    @GET("orders/listbill/{id}")
    Call<BaseResponse<List<OrderDetails>>> getListOederDetail(@Path("id") String idOrder);
    //tuta

    @FormUrlEncoded
    @POST("users/change-profile")
    Call<BaseResponse<String>> changeProfilee(@Field("userId") String userId, @Field("phone") String phone , @Field("address") String address , @Field("username") String username);


    @FormUrlEncoded
    @POST("products/add_review")
    Call<BaseResponse<Review.ReviewDetails>> addComment(@Field("comment") String comment, @Field("rate") int rate,@Field("productId") String productId);

    @GET("products/list_review")
    Call<BaseResponse<List<Review>>> getListReview(@Query("productId") String productId);

    //cate
    @GET("cates/list")
    Call<BaseResponse<List<Cate>>> getListCate();


    //order
    @FormUrlEncoded
    @POST("orders/add")
    Call<BaseResponse<String>> addOrder(@Field("order_details") String jsonOrderDetails, @Field("order") String jsonOrder);

    @GET("orders/check_gift/{id}")
    Call<BaseResponse<Gift>> checkGift(@Path("id") String codeGift);

    //banner
    @GET("cates/list_banner")
    Call<BaseResponse<List<Images>>> getBanner();
}
