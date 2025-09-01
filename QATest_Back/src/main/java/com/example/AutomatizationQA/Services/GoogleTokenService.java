package com.example.AutomatizationQA.Services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenService {

    private final GoogleIdTokenVerifier verifier;


    public GoogleTokenService() {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList("477604217339-rn91g5tds9e0rpl17lvqo5frrtocj8mj.apps.googleusercontent.com"))
                .build();
    }

    public GoogleIdToken.Payload verifyToken(String idTokenString) throws Exception {
        GoogleIdToken idToken = verifier.verify(idTokenString);
        return (idToken != null) ? idToken.getPayload() : null;
    }
}
