package com.springboot.apirest.service;

import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Autowired
    private QRCodeService qrCodeService;

    private static final String API_URL = "https://api.resend.com/emails";
    private static final String API_KEY = "re_D3HB7VS3_MbShxEf7DpRLPMWdm4K18PDr"; // Reemplázalo con tu clave de Resend

    public void enviarCorreoConQR(String emailDestino, String informacionReserva) throws MessagingException, IOException, WriterException {
        byte[] qrImage = qrCodeService.generateQRCodeImage(informacionReserva, 200, 200);

    }

/**
 *
 */
public void enviarCorreo(String destinatario, String asunto, String mensaje) throws IOException, WriterException {
    RestTemplate restTemplate = new RestTemplate();

    // Crear encabezados HTTP
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + API_KEY);
    headers.set("Content-Type", "application/json");


    byte[] qrImage = qrCodeService.generateQRCodeImage(mensaje, 200, 200);

    // Crear el cuerpo del correo
    Map<String, Object> emailData = new HashMap<>();
    emailData.put("from", "antonio@padelcordoba.com"); // Debe ser un dominio validado en Resend
    emailData.put("to", destinatario);
    emailData.put("subject", asunto);
    emailData.put("html", qrImage);
    //emailData.put("html", mensaje);

    // Crear la petición HTTP
    HttpEntity<Map<String, Object>> request = new HttpEntity<>(emailData, headers);

    // Enviar la solicitud POST
    ResponseEntity<String> response = restTemplate.exchange(API_URL, HttpMethod.POST, request, String.class);

    // Mostrar respuesta en la consola
    System.out.println("Respuesta de Resend: " + response.getBody());
}



    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            byte[] qrImage = qrCodeService.generateQRCodeImage(body, 200, 200);

            // Configura el remitente, el destinatario, el asunto y el cuerpo del mensaje
            helper.setFrom("acobosscabello@gmail.com");  // Dirección del remitente
            helper.setTo(to);  // Dirección del destinatario
            helper.setSubject(subject);  // Asunto
            helper.setText(qrImage.toString(), true);  // Cuerpo del mensaje (true indica que es HTML)
            //helper.setText(body, true);  // Cuerpo del mensaje (true indica que es HTML)

            // Enviar el mensaje
            javaMailSender.send(message);

            System.out.println("Correo enviado exitosamente.");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Hubo un error al enviar el correo.");
        }
    }





}



