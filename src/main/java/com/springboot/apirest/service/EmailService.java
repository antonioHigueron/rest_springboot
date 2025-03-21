package com.springboot.apirest.service;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final QRCodeService qrCodeService;

    @Autowired
    public EmailService(JavaMailSender javaMailSender, TemplateEngine templateEngine, QRCodeService qrCodeService) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.qrCodeService = qrCodeService;
    }

    public void enviarCorreo(String destinatario, String asunto, String mensaje, String contactoClub, String uuid) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject(asunto);
            //helper.setText("texto del correo, permite html", true);

            // Procesar la plantilla Thymeleaf
            // 📌 Generamos el QR como archivo
            //Con esto al escanear el qr aparece el email y ya te deja desde el movil enviar un correo
            //File qrFile = qrCodeService.generateQRCodeImage2("Confirmacion de reserva a nombre del usuario con correo: "+destinatario+"<br>Mostrar en recepción del club para confirmación.", 300, 300);
            File qrFile = qrCodeService.generateQRCodeImage2(uuid, 300, 300);
            // 📌 Adjuntar el archivo correctamente
            FileSystemResource fileResource = new FileSystemResource(qrFile);
            helper.addAttachment("QR_Code.png", fileResource);

            Context context = new Context();
            context.setVariable("mensaje", mensaje);
            //context.setVariable("mensaje", mensaje);
            String contenidoHtml = templateEngine.process("email", context);
            //Con esto se puede formatear el contenido con formato html
            //message.setContent(mensaje, "text/html");
            helper.setText(contenidoHtml, true);

            javaMailSender.send(message);

            // 📌 Eliminar el archivo temporal después de enviarlo
            qrFile.delete();

        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }








}



