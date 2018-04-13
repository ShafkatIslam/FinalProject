package com.example.shafkatislam.finalproject;

/**
 * Created by ShafkatIslam on 20/03/2018.
 */

public class Key {

    //main url or address
    public static final String MAIN_URL = "http://192.168.43.131";

    //access db from device
    public static  final String SIGNUP_URL = MAIN_URL+"/final/signup.php";
    public static  final String LOGIN_URL = MAIN_URL+"/final/login.php";
    public static final String SAVE_URL = MAIN_URL+"/final/save_contact.php";
    public static final String UPDATE_URL = MAIN_URL+"/final/update_contact.php";
    public static final String DELETE_URL = MAIN_URL+"/final/delete_contact.php";

    //url for contacts view
    public static final String CONTACT_VIEW_URL = MAIN_URL+"/final/view_contact.php?cell=";


    //keys for email and password as defined in our $_POST['key'] in login.php



    //Keys for server communications
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CELL = "cell";
    public static final String KEY_USER_CELL = "user_cell";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    //public static final String KEY_IMAGE_NAME = "image_name";
    public static final String KEY_IMAGE = "image";

    //share preference
    //We will use this to store the user cell number into shared preference
    public static final String SHARED_PREF_NAME = "com.example.shafkatislam.finalproject.userlogin"; //pcakage name+ id(any name)

    //This would be used to store the cell of current logged in user
    public static final String CELL_SHARED_PREF = "cell";


    //json array name.We will received data in this array
    public static final String JSON_ARRAY = "result";


}
