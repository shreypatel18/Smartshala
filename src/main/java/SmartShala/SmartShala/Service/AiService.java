package SmartShala.SmartShala.Service;

import SmartShala.SmartShala.Entities.Answer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AiService{


    @Value("${gemini.api.key}")
    String apiKey;

    @Value("${gemini.api.url}")
    String apiUrl;


    WebClient webClient;

    AiService() {
        webClient = WebClient.builder().build();
    }

    public String generateReply(String answer, String answerKey) {
        String prompt = getPrompt(answer, answerKey);


        // Craft a request
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );

        // Do request and get response
        String response = webClient.post()
                .uri(apiUrl + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractResponseContent(response);
    }

    private String extractResponseContent(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }



    }

    public String getPrompt(String answer, String answerKey){

        StringBuilder prompt = new StringBuilder();
        prompt.append("Here are two answers one is answer key and second is answer given by student");
        prompt.append("answer key is ").append(answerKey);

        prompt.append("the answer given by student is").append(answer).append("give the errors in students answer");
        prompt.append("common mistakes like spellings grammars ect as well as conceptual issues or overwritting or underwritting");
        return prompt.toString();
    }
}