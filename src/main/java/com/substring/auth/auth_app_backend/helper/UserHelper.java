package com.substring.auth.auth_app_backend.helper;

import java.util.UUID;

public class UserHelper {

    public static UUID parseUUID(String uuid){      //Parsing means converting data from one format into another usable format.
        return UUID.fromString(uuid);               //You are converting a String representation of a UUID  Into a real UUID object
    }
}
