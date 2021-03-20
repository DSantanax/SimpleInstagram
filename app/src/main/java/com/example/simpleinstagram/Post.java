package com.example.simpleinstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

// The annotation ParseClassName should match with the Post entity
// in the Back4App and we must extend ParseObject which each object
// contains Key/Value pairs
// ParseModel to represent our data
@ParseClassName("Post")
public class Post extends ParseObject {

    // define the keys based on the keys of the columns
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_AT = "createdAt";

    // getters/setters for the keys (name for the columns in the class B4A)

    // use the ParseObject methods
    public String getDescription() {
        // getString is defined the ParseObject through inheritance
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        // put takes in the key and the value to insert
        put(KEY_DESCRIPTION, description);
    }
    // get/set a ParseFile object for the image
    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }
    // get/set a ParseUser
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }

}
