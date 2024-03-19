package com.sparta.spartdelivery.external.email;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class EmailController {
    private final EmailService emailService;

    // 이메일 인증절차
    @PostMapping("/emailConfirm/{email}")
    public String emailConfirm(@PathVariable String email) throws Exception {

        String confirm = emailService.sendSimpleMessage(email);

        return confirm;
    }
}
