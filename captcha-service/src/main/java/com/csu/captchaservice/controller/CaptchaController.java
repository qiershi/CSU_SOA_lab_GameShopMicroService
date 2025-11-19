package com.csu.captchaservice.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
* @ProjectName: captcha-service
* @Title: CaptchaController
* @Package: com.csu.captchaservice.controller
* @Description: captcha's controller
* @author qiershi
* @date 2025/11/19 14:45
* @version V1.0
* Copyright (c) 2025, qiershi2006@h163.com All Rights Reserved.
*/
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {
    private String currentCaptcha = "";

    // 生成验证码图片
    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateCaptcha(@RequestParam(value = "username", required = false) String username, HttpSession session) throws IOException {
        // 如果没有提供用户名，使用默认值
        if (username == null || username.isEmpty()) {
            username = "defaultUser";
        }

        // 生成验证码
        int width = 160, height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) image.getGraphics();

        Random random = new Random();
        String captchaStr = "";
        for (int i = 0; i < 5; i++) {
            char ch = (char) (random.nextInt(26) + 65); // 生成大写字母
            captchaStr += ch;
        }

        // 存储验证码到 HttpSession
        currentCaptcha = captchaStr;
        session.setAttribute("captcha:" + username, captchaStr);
        session.setMaxInactiveInterval(300); // 5 分钟过期

        // 绘制验证码图片
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 添加干扰线
        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 10; i++) {
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 绘制验证码文字
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.setColor(Color.BLACK);
        for (int i = 0; i < captchaStr.length(); i++) {
            // 随机旋转和偏移文字
            g.rotate(random.nextInt(15) * Math.PI / 180, 20 + i * 20, 30);
            g.drawString(String.valueOf(captchaStr.charAt(i)), 20 + i * 20, 30);
            g.rotate(-random.nextInt(15) * Math.PI / 180, 20 + i * 24, 30);
        }
        g.dispose();

        // 将图片转换为字节数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    // 验证用户输入的验证码
    @PostMapping("/verify")
    public VerificationResponse verifyCaptcha(@RequestBody VerificationRequest request, HttpSession session) {
        System.out.println('\n' + currentCaptcha + '\n'+ request.getCaptcha() + '\n');
        String storedCaptcha = (String) session.getAttribute("captcha:" + request.getUsername());
        boolean isValid = (currentCaptcha.equals(request.getCaptcha()) || storedCaptcha.equalsIgnoreCase(request.getCaptcha()));
        if (isValid) {
            // 可选：验证成功后移除验证码
            session.removeAttribute("captcha:" + request.getUsername());
            currentCaptcha = "";
        }
        return new VerificationResponse(isValid);
    }

    // 请求 DTO
    static class VerificationRequest {
        private String username;
        private String captcha;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getCaptcha() { return captcha; }
        public void setCaptcha(String captcha) { this.captcha = captcha; }
    }

    // 响应 DTO
    static class VerificationResponse {
        private boolean valid;
        public VerificationResponse(boolean valid) { this.valid = valid; }
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
    }
}