/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.event.utils;

/**
 *
 * @author admin
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import service.event.model.EventTicket;

public class QRUtils {
    public static byte[] generateQRCodeBytes(Long id, String userId) {
        String qrContent = id + ":" + userId; // Nội dung QR Code
        int width = 300, height = 300;

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrContent, BarcodeFormat.QR_CODE, width, height);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            // Chuyển thành byte[]
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(qrImage, "png", outputStream);
            return outputStream.toByteArray();
        } catch (WriterException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static byte[] generateQRCode(EventTicket ticket) throws WriterException, IOException {
        Map<String, Object> qrData = new HashMap<>();
        qrData.put("ticketId", ticket.getTicketId());
        qrData.put("userId", ticket.getTicketUserId());
        qrData.put("email", ticket.getTicketUserEmail());
        qrData.put("price", ticket.getTicketPrice());
        qrData.put("status", ticket.getTicketStatus());
        qrData.put("position", ticket.getTicketPosition());
        qrData.put("validFrom", ticket.getTicketDayActive().toString());
        qrData.put("eventId", ticket.getEvent().getEventId()); // Lấy ID sự kiện

        // Chuyển thành JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(qrData);

        // Tạo QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(json, BarcodeFormat.QR_CODE, 300, 300);

        // Chuyển thành byte[]
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
