package photo.hub.service;

import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
@Service
public class ImageService {

    @Value("${imgur.token}")
    private String imgurToken;

    public String uploadToImgur(File file, String title, String description) {
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


            out.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\"" + file.getName() + "\"\r\n");
            out.writeBytes("Content-Type: " + Files.probeContentType(Paths.get(file.toURI())) + "\r\n\r\n");
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.writeBytes("\r\n");
            fileInputStream.close();


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
