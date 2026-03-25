package org.qainsights.jmeter.ai.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestMessage;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import com.azure.core.credential.AzureKeyCredential;
import org.apache.jmeter.util.JMeterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AzureOpenAiService implements AiService {
    private static final Logger log = LoggerFactory.getLogger(AzureOpenAiService.class);

    private final OpenAIClient client;
    private final String defaultDeploymentName;

    public AzureOpenAiService() {
        String endpoint = JMeterUtils.getPropDefault("jmeter.ai.azure.endpoint", "");
        String apiKey = JMeterUtils.getPropDefault("jmeter.ai.azure.api.key", "");
        this.defaultDeploymentName = JMeterUtils.getPropDefault("jmeter.ai.azure.deployment", "gpt-4");

        this.client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey))
                .buildClient();
    }

    /**
     * Implementation of AiService: generateResponse with current conversation
     */
    @Override
    public String generateResponse(List<String> conversation) {
        return generateResponse(conversation, defaultDeploymentName);
    }

    /**
     * Implementation of AiService: generateResponse with a specific model/deployment
     */
    @Override
    public String generateResponse(List<String> conversation, String model) {
        try {
            List<ChatRequestMessage> chatMessages = new ArrayList<>();
            for (String msg : conversation) {
                chatMessages.add(new ChatRequestUserMessage(msg));
            }

            // In Azure, 'model' usually refers to the Deployment ID
            ChatCompletionsOptions options = new ChatCompletionsOptions(chatMessages);
            ChatCompletions chatCompletions = client.getChatCompletions(model, options);

            if (chatCompletions.getChoices() != null && !chatCompletions.getChoices().isEmpty()) {
                return chatCompletions.getChoices().get(0).getMessage().getContent();
            }
        } catch (Exception e) {
            log.error("Azure OpenAI Error: {}", e.getMessage());
            return "Error: " + e.getMessage();
        }
        return "No response from Azure.";
    }

    /**
     * Implementation of AiService: Returns the provider name
     */
    @Override
    public String getName() {
        return "Azure OpenAI";
    }

    // Helper for simple string prompts used in AiChatPanel
    public String getResponse(String prompt) {
        List<String> conversation = new ArrayList<>();
        conversation.add(prompt);
        return generateResponse(conversation);
    }
    
    // Compatibility methods for AiChatPanel (if it still calls these)
    public void setModel(String model) {
        // Implementation if needed, otherwise leave empty
    }

    public String getCurrentModel() {
        return defaultDeploymentName;
    }
}