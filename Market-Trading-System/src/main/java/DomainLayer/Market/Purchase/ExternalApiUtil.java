package DomainLayer.Market.Purchase;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExternalApiUtil {
    private static final String API_URL = "https://damp-lynna-wsep-1984852e.koyeb.app/";

    public static String sendPostRequest(Map<String, String> params) throws Exception {
        // Perform handshake before proceeding with any other request
        if (!performHandshake()) {
            throw new RuntimeException("Handshake failed, external systems may not be available.");
        }

        URL url = new URL(API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        // Construct URL-encoded data from the HashMap
        StringBuilder postData = new StringBuilder();
        for (HashMap.Entry<String, String> entry : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        // Enable output and set content type for the request
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", Integer.toString(postData.length()));

        // Send the POST data
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postData.toString());
            wr.flush();
        }
        return new String(con.getInputStream().readAllBytes());
    }

    // Private method to perform the handshake
    public static boolean performHandshake() throws Exception {
        Map<String, String> handshakeParams = Map.of("action_type", "handshake");
        URL url = new URL(API_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        // Construct URL-encoded data from the HashMap
        StringBuilder postData = new StringBuilder();
        for (HashMap.Entry<String, String> entry : handshakeParams.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        // Enable output and set content type for the request
        con.setDoOutput(true);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", Integer.toString(postData.length()));

        // Send the POST data
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.writeBytes(postData.toString());
            wr.flush();
        }
        boolean statusOK = con.getResponseCode() == HttpURLConnection.HTTP_OK;
        boolean codeOK  = new String(con.getInputStream().readAllBytes()).equals("OK");
        return statusOK && codeOK;
    }
}
