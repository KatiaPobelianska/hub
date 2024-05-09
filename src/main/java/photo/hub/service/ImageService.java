package photo.hub.service;

import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ImageService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${imgur.token}")
    private String imgurToken;

    public String uploadToImgur(byte[] fileBytes, String title, String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(imgurToken);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", new HttpEntity<>(fileBytes));
            body.add("title", title);
            body.add("description", description);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://api.imgur.com/3/image", requestEntity, String.class);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                JSONObject jsonResponse = new JSONObject(responseEntity.getBody());
                return jsonResponse.getJSONObject("data").getString("link");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
