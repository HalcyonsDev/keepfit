package com.halcyon.keepfit.util;

import jakarta.servlet.http.Cookie;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class DeserializationUtil {
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            ois.close();
            return cls.cast(obj);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
