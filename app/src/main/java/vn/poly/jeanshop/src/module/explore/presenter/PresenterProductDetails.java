package vn.poly.jeanshop.src.module.explore.presenter;

import java.util.ArrayList;
import java.util.List;

import vn.poly.jeanshop.src.model.Images;
import vn.poly.jeanshop.src.model.Review;
import vn.poly.jeanshop.src.module.explore.model.ModelProduct;
import vn.poly.jeanshop.src.network.EndPoint;

public class PresenterProductDetails implements IProductDetails.IPresenterProductTDetails {
    private IProductDetails.IViewProductTDetails iViewProductTDetails;
    private ModelProduct modelProduct;

//    public static List<Review> reviewList = new ArrayList<>() ;

    public PresenterProductDetails(IProductDetails.IViewProductTDetails iViewProductTDetails) {
        this.iViewProductTDetails = iViewProductTDetails;
        this.modelProduct = new ModelProduct();
    }

    @Override
    public void addCommentToServer(String comment, String productId, int rate) {
        modelProduct.addComment(comment, productId, rate, this);
    }

    @Override
    public void resultAddComment(boolean result, String msg) {
        if (result) {
            iViewProductTDetails.onSuccessAddComment(msg);
        } else {
            iViewProductTDetails.onFailedAddComment(msg);

        }
    }

    @Override
    public void getComment(String productId) {
        modelProduct.getComment(productId, this);
    }

    @Override
    public void resultGetComment(boolean result, List<Review> reviews) {
        if (result && reviews != null) {
            iViewProductTDetails.onSuccessGetComment(reviews);
        } else {
            iViewProductTDetails.onFailedGetComment("");

        }
    }

    @Override
    public void getImages(String productId) {
        modelProduct.getImages(productId, this);
    }

    @Override
    public void resultGetImages(boolean result, List<Images> imagesList) {
        if (result) {
            if (imagesList.size() > 0) {

                String[] listImages = new String[imagesList.size()];
                for (int i = 0; i < imagesList.size(); i++) {
                    listImages[i] = EndPoint.BASE_URL_PUBLIC + imagesList.get(i).getUrl();
                }
                iViewProductTDetails.onSuccessGetImages(listImages);
            }else{
                iViewProductTDetails.onSuccessGetImages(null);
            }

        } else {
            iViewProductTDetails.onFailGetImages("Có lỗi xảy ra");
        }
    }

    @Override
    public List<Review> getReviewList(List<Review> reviews) {
//        reviewList = new ArrayList<>();
//        reviewList.addAll(reviews);
        return reviews;
    }
}
