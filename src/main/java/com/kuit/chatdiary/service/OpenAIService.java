package com.kuit.chatdiary.service;

import com.kuit.chatdiary.domain.Chat;
import com.kuit.chatdiary.domain.ChatType;
import com.kuit.chatdiary.domain.Member;
import com.kuit.chatdiary.domain.Sender;
import com.kuit.chatdiary.repository.ChatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class OpenAIService {

    @Value("${OPEN_AI_KEY}")
    private String OPEN_AI_KEY;

    @Value("${AIPROMPT_CHICHI}")
    private String AIPROMPT_CHICHI;

    @Value("${AIPROMPT_DADA}")
    private String AIPROMPT_DADA;

    @Value("${AIPROMPT_LULU}")
    private String AIPROMPT_LULU;

    @Autowired
    private ChatRepository chatRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getCompletion(Long userId, Integer model, Member member) {

        String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(OPEN_AI_KEY);

        List<Map<String, Object>> messages = new ArrayList<>();

        String nickname = member.getNickname();
        String gender = member.getGender();
        String age = String.valueOf(member.getAge());
        String userPrompt = createUserPrompt(nickname, gender, age);
        String prompt = switch (model) {
            case 1 -> AIPROMPT_DADA;
            case 2 -> AIPROMPT_CHICHI;
            case 3 -> AIPROMPT_LULU;
            default -> throw new IllegalStateException("Unexpected value: " + model);
        };

        Map<String, Object> systemMessage = new HashMap<>();
        systemMessage.put("role", "system");
        systemMessage.put("content", userPrompt + prompt);
        messages.add(systemMessage);

        List<Map<String, Object>> previousMessages = getRecentChats(userId);
        messages.addAll(previousMessages);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("model", "gpt-4-vision-preview");
        requestBody.put("max_tokens", 200);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        log.info(request.toString());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Error calling OpenAI API: ", e);
            throw new RuntimeException("Error calling OpenAI API", e);
        }
    }


    public List<Map<String, Object>> getRecentChats(Long userId) {
        List<Chat> recentChats = chatRepository.findTop10ByMember_UserIdOrderByChatIdDesc(userId);

        Collections.reverse(recentChats);

        String ChatIdsFromRecentChats = recentChats.stream()
                .map(chat -> String.valueOf(chat.getChatId()))
                .collect(Collectors.joining(", "));

        log.info("OpenAIService.getRecentChats, recentChats chatIds: {}", ChatIdsFromRecentChats);


        List<Map<String, Object>> previousMessages = new ArrayList<>();
        for (Chat chat : recentChats) {
            Map<String, Object> message = new HashMap<>();

            if (Sender.USER.equals(chat.getSender())) {
                message.put("role", "user");

                if (ChatType.CHAT.equals(chat.getChatType())) {
                    message.put("content", List.of(getTextPart(chat.getContent())));
                } else if (ChatType.IMG.equals(chat.getChatType())) {
                    log.info("OpenAIService.getRecentChats, IMG chatId: {}", chat.getChatId());
                    if(chat.getContent().equals(recentChats.get(recentChats.size()-1).getContent())){
                        message.put("content", List.of(getImagePart(chat.getContent())));
                    }else{
                        continue;
                    }


                }
            } else {
                message.put("role", "assistant");
                message.put("content", List.of(getTextPart(chat.getContent())));
            }

            previousMessages.add(message);
        }
        return previousMessages;
    }

    private Map<String, Object> getTextPart(String content) {
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("type", "text");
        textPart.put("text", content);
        return textPart;
    }

    private Map<String, Object> getImagePart(String content) {
        Map<String, Object> imagePart = new HashMap<>();
        Map<String, String> imageUrl = new HashMap<>();
        imagePart.put("type", "image_url");
        imageUrl.put("url", content);
        imagePart.put("image_url", imageUrl);
        return imagePart;
    }

    public String createUserPrompt(String nickname, String gender, String age) {
        String prompt = "You talk with a friend named '{nickname}' who is a {gender}, {age} years old.\n";
        prompt = prompt.replace("{nickname}", defaultValue(nickname, "사용자"))
                .replace("{gender}", defaultValue(gender, "Unknown"))
                .replace("{age}", defaultValue(age, "Unknown"));
        return prompt;
    }

    private String defaultValue(String value, String defaultValue) {
        return (value != null) ? value : defaultValue;
    }


}
