package com.park.optech.parking.model;

public class callFirebaseModel {
    public String file_upload_url;
    public String restful_api_url;
    public String printer_soap_api_url;
    public String base_soap_api_url;

    public callFirebaseModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public callFirebaseModel(String file_upload_url, String restful_api_url, String printer_soap_api_url, String base_soap_api_url) {
        this.file_upload_url = file_upload_url;
        this.restful_api_url = restful_api_url;
        this.printer_soap_api_url = printer_soap_api_url;
        this.base_soap_api_url = base_soap_api_url;
    }


}
