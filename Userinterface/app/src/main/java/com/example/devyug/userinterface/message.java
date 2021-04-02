package com.example.devyug.userinterface;

import java.util.Date;

/**
 * Created by dell on 23/9/17.
 */

public class message {
    String messageText;
    String messageUser;
    String  messageTime;
    message()
    {}
    message(String m,String t)
    {
        messageText=m;
        messageUser=t;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}