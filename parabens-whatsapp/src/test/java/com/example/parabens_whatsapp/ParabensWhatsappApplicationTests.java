import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenAIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String chamarApiOpenAI(String prompt) {
        String url = "https://api.openai.com/v1/completions";
        // Configurar a requisição e chamar a API
        return restTemplate.postForObject(url, request, String.class);
    }
}
