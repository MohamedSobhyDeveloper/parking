package com.park.optech.parking.service;

import com.park.optech.parking.myApplication;
import com.park.optech.parking.sharedpref.MySharedPref;

/**
 * Created by mohamed on 1/24/2018.
 */

public class Config {
    // File upload url (replace the ip with your server address)
    public static final String FILE_UPLOAD_URL = MySharedPref.getData(myApplication.getAppContext(), "file_upload_url", "");

    // Directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
}
