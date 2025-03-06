import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ParabensWhatsappApplicationTests {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiKey = "k-proj-WNzw71AiGcgyXGgzAYzlFotoi4bG9LRKhgDb09OeurHI-74rksu2rEVFmlCbFpiBYKfWYK8cH7T3BlbkFJE-dmD8CmY4hS9yaWLGS9--eSi2T8A7Sx3vdpD-p9TZN7ssf2R3EOACYe7Yy_tY58B7R1THTPUA";
    
    public String chamarApiOpenAI(String prompt) {
        String url = "https://api.openai.com/v1/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");  // Ajuste conforme necessário
        requestBody.put("prompt", prompt);
        requestBody.put("max_tokens", 100);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return response.getBody();
    }
}
