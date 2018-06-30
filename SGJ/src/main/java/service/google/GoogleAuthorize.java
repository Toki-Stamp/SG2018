package service.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class GoogleAuthorize {
    private static final String CREDENTIALS_FOLDER = "credentials";

    public static Credential authorize() throws IOException, GeneralSecurityException {
        InputStream in = GoogleAuthorize.class.getResourceAsStream("/client-secret.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JacksonFactory.getDefaultInstance(), new InputStreamReader(in));
        List<String> scopes = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                .Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new File(CREDENTIALS_FOLDER)))
                .setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver())
                .authorize("user");
    }
}
