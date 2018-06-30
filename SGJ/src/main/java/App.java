import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import service.google.GoogleSheetsService;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class App {
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Sheets service = GoogleSheetsService.getSheetsService();
        final String spreadsheetId = "1ETeswIr2RQb06lYlhqWDCa8cb0ugaSKDvG8-GzEE46s";
        final String range = "Ответы на форму (1)!A:CO";

        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
    }
}
