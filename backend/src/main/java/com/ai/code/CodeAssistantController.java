package com.ai.code;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/code")
@CrossOrigin(origins = "http://localhost:3000")
public class CodeAssistantController {

    private final String OPENAI_API_KEY = "sk-...";
    private final String OPENAI_URL = "https://api.openai.com/v1/completions";

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPENAI_API_KEY); // 🛡️ Automatically adds Authorization: Bearer ...

        // Build JSON payload
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "text-davinci-003");
        requestBody.put("prompt", "Write Java code for:\n" + prompt);
        requestBody.put("max_tokens", 1024);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("choices")) {
                String code = ((Map) ((java.util.List<?>) responseBody.get("choices")).get(0)).get("text").toString();
                return ResponseEntity.ok(code.trim());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("No response from OpenAI.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: " + e.getMessage());
        }
    }
}
