package service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsRequestInitializer;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class GoogleService {
    public static List getRawData() {
        List rawData = null;

        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            final String apiKey = "AIzaSyBsyp4iwLdmz3wlKIXwspWjVzdoHXoYWko";
            final String applicationName = "Google Sheets Service";
            final String spreadsheetId = "1ETeswIr2RQb06lYlhqWDCa8cb0ugaSKDvG8-GzEE46s";
            final String range = "Ответы на форму (1)!A:CO";

            Sheets sheets = new Sheets
                    .Builder(httpTransport, jsonFactory, null)
                    .setApplicationName(applicationName)
                    .setGoogleClientRequestInitializer(new SheetsRequestInitializer(apiKey))
                    .build();

            ValueRange values = sheets.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();

            rawData = values.getValues();

            if (rawData.size() > 1) rawData.remove(0);
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        return rawData;
    }
}

