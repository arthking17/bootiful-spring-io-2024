package com.example.service.joke;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@ResponseBody
@Controller
class JokeController {
    private final ChatClient chatClient;

    JokeController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/joke")
    Map<String, String> joke() {
        var prompt = """
                Tell me a joke about programmer.
                """;
        System.out.println(prompt);

        var response = this.chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
        return Map.of("joke", response);
    }
}
