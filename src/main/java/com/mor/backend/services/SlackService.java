package com.mor.backend.services;

import com.github.seratch.jslack.api.methods.SlackApiException;

import java.io.IOException;

public interface SlackService {
    void sendMessage(String channel, String message) throws SlackApiException, IOException;
}
