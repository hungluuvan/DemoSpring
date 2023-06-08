package com.mor.backend.services.impl;

import com.github.seratch.jslack.api.methods.MethodsClient;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.request.conversations.ConversationsListRequest;

import com.github.seratch.jslack.api.methods.request.files.FilesUploadRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.github.seratch.jslack.api.methods.response.conversations.ConversationsListResponse;
import com.github.seratch.jslack.api.methods.response.files.FilesUploadResponse;
import com.mor.backend.services.SlackService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class SlackServiceImpl implements SlackService {
    private final MethodsClient slackMethodsClient;

    @Override
    public void sendMessage(String channel, String message) throws SlackApiException, IOException {
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(channel)
                .text(message)
                .build();
//        List<String> listChannels = List.of(new String[]{"C059WAL6PT4", "C05A40B52VA"});
//        FilesUploadRequest filesUploadRequest = FilesUploadRequest.builder().file(new File("D:\\WorkSpace\\ht0050.txt")).filename("ht0050.txt").initialComment("Here's my file :smile:").channels(listChannels).build();
//        FilesUploadResponse filesUploadResponse = slackMethodsClient.filesUpload(filesUploadRequest);
        ChatPostMessageResponse response = slackMethodsClient.chatPostMessage(request);
//        ConversationsListRequest channelsRequest = ConversationsListRequest.builder().build();
//
//        try {
//            ConversationsListResponse channelsResponse = slackMethodsClient.conversationsList(channelsRequest);
//            if (channelsResponse.isOk()) {
//                log.info(String.valueOf(channelsResponse.getChannels()));
//            }  // Handle error response if needed
//
//        } catch (IOException | SlackApiException e) {
//            // Handle exceptions
//        }

    }
}
