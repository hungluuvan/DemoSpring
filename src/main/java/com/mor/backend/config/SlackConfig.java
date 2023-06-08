package com.mor.backend.config;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.MethodsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackConfig {
    @Value("${slack.oauth.accessToken}")
    private String slackAccessToken;

    @Bean
    public Slack slackClient() {
        return Slack.getInstance();
    }

    @Bean
    public MethodsClient slackMethodsClient(Slack slackClient) {
        return slackClient.methods(slackAccessToken);
    }
}