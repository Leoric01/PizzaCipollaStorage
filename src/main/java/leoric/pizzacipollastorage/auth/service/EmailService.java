package leoric.pizzacipollastorage.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendEmail(String to,
                          String username,
                          EmailTemplateName emailTemplate,
                          String confirmationUrl,
                          String activationCode,
                          String subject
    ) throws MessagingException {
        String templateName;
        if (emailTemplate != null) {
            templateName = emailTemplate.getName();
        } else {
            templateName = "confirm-template";
        }

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                mimeMessage,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                StandardCharsets.UTF_8.name()
        );

        Map<String, Object> model = new HashMap<>();
        model.put("username", username);
        model.put("confirmationUrl", confirmationUrl);
        model.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(model);

        mimeMessageHelper.setFrom("gwatever74@gmail.com");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject + ": " + activationCode);

        String template = templateEngine.process(templateName, context);
        mimeMessageHelper.setText(template, true);
        mailSender.send(mimeMessage);
    }
}