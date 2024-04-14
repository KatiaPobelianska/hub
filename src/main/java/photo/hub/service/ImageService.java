package photo.hub.service;

import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
@Service
public class ImageService { // TODO Код "в лоб". Можно сделать проще с помощью RestClient или хотя бы вынести шаблон телаз апроса в ресурсы

    @Value("${imgur.token}")
    private String imgurToken;

    public String uploadToImgur(byte[] fileBytes, String title, String description) {
        try {
            URL url = new URL("https://api.imgur.com/3/image");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + imgurToken);

            String boundary = "Boundary-" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"type\"\r\n\r\n");
            out.writeBytes("file\r\n");
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n");
            out.writeBytes(title + "\r\n");
            out.writeBytes("--" + boundary + "\r\n");
            out.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
            out.writeBytes(description + "\r\n");
            out.writeBytes("--" + boundary + "\r\n");

            out.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"image\"\r\n");
            out.writeBytes("Content-Type: image/jpeg\r\n\r\n");
            out.write(fileBytes);
            out.writeBytes("\r\n");

            out.writeBytes("--" + boundary + "--\r\n");
            out.flush();
            out.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject jsonResponse = new JSONObject(response.toString());
            String link = jsonResponse.getJSONObject("data").getString("link");
            return link;

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
