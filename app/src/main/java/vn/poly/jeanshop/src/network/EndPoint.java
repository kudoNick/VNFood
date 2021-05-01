package vn.poly.jeanshop.src.network;

public class EndPoint {
    static String BASE_URL = "http://192.168.1.218:8080/api/"; // cac ban thay dia chi ip o day, hoac link api dang https://vn-food.herokuapp.com/api/
   public static String BASE_URL_PUBLIC = "http://192.168.1.218:8080/public/photo/"; //https://vn-food.herokuapp.com/public/photo/

    //**** User ****//
    static
    String LOGIN = "/users/login";
         String REGISTER = "/users/register";
         String PROFILE = "/users/profile";

}
