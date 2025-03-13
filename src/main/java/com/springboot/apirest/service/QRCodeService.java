package com.springboot.apirest.service;

import com.google.zxing.BarcodeFormat;
//import com.google.zxing.BitMatrix;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Service
public class QRCodeService {

    public byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(image, "png", baos);

        return baos.toByteArray();
    }
}

