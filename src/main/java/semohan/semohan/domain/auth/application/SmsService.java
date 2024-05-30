package semohan.semohan.domain.auth.application;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    @Value("${coolsms.api.key}")
    private String apiKey;
    @Value("${coolsms.api.secret}")
    private String apiSecretKey;
    @Value("${sms.senderNumber}")
    String senderNumber;

    private DefaultMessageService messageService;

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
    }

    // 인증 번호 메시지 발송
    public SingleMessageSentResponse sendVerifySms(String to, String verificationCode) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText("[세모한] 아래의 인증번호를 입력해주세요.\n" + verificationCode);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }

    // 임시 비밀번호 sms 전송
    public SingleMessageSentResponse sendTemporaryPassword(String to, String verificationCode) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(senderNumber);
        message.setTo(to);
        message.setText("[세모한] 임시 비밀번호가 발급되었습니다.\n" + verificationCode);

        return this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}