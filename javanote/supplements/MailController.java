package kybmig.ssm.controller;

import kybmig.ssm.Utility;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


@Component
class AsyncMail {
    private MailSender sender;
    private MailProperties mailProperties;

    public AsyncMail(MailSender sender, MailProperties mailProperties) {
        this.sender = sender;
        this.mailProperties = mailProperties;
    }

    @Async
    public void sendMail(String address, String title, String content) {
        Utility.log("异步发送邮件");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());;
        mailMessage.setSubject(title);
        mailMessage.setTo(address);
        mailMessage.setText(content);

        sender.send(mailMessage);
    }
}


@Component
class SyncMail {
    private MailSender sender;
    private MailProperties mailProperties;

    public SyncMail(MailSender sender, MailProperties mailProperties) {
        this.sender = sender;
        this.mailProperties = mailProperties;
    }

    public void sendMail(String address, String title, String content) {
        Utility.log("同步发送邮件");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailProperties.getUsername());
        ;
        mailMessage.setSubject(title);
        mailMessage.setTo(address);
        mailMessage.setText(content);

        sender.send(mailMessage);
    }
}

@Controller
@RequestMapping("/mail")
public class MailController {
    private SyncMail syncMail;
    private AsyncMail asyncMail;

    public MailController(SyncMail syncMail, AsyncMail asyncMail) {
        this.syncMail = syncMail;
        this.asyncMail = asyncMail;
    }


    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("mail/index");
    }

    @GetMapping("/send")
    @ResponseBody
    public String send(String address, String title, String content) {
        boolean valid = address !=null && title != null && content != null;
        if (valid) {
            syncMail.sendMail(address, title, content);
            Utility.log("同步发送结束");
            return "发送成功";
        } else {
            return "发送失败, 格式不对";
        }
    }

    @GetMapping("/asySend")
    @ResponseBody
    public String asySend(String address, String title, String content) {
        boolean valid = address !=null && title != null && content != null;
        if (valid) {
            asyncMail.sendMail(address, title, content);
            Utility.log("异步发送结束");
            return "发送成功";
        } else {
            return "发送失败, 格式不对";
        }
    }
}
