package com.sparta.spartdelivery.external.email;

public interface EmailService {
    String sendSimpleMessage(String to)throws Exception;
}