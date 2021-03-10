package com.example.simpleinstagram;

import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

//This class is used to initialize the ParseSDK with the credentials
public class ParseApplication extends Application {
    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();
        // Register parse models to query/set data on our model
        ParseObject.registerSubclass(Post.class);
        // Initialize parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("WQ5OIhQI4D0RGVMVHCNqirvCavMVUIagnLF2l5V8")
                .clientKey("FubBCYG2lBbaPn6soRwX1p6fScP8rOyldsF73m2U")
                .server("https://parseapi.back4app.com")
                .build()
        );

    }
}
