package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.model.Advisory;
import com.example.icc_portafolio_ramon_serrano.model.NotificationChannel;
import com.example.icc_portafolio_ramon_serrano.model.NotificationLog;
import com.example.icc_portafolio_ramon_serrano.model.NotificationStatus;
import com.example.icc_portafolio_ramon_serrano.repository.NotificationLogRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class NotificationDispatcher {

    private final EmailNotificationService emailNotificationService;
    private final WhatsAppNotificationService whatsAppNotificationService;
    private final NotificationLogRepository notificationLogRepository;

    public NotificationDispatcher(
            EmailNotificationService emailNotificationService,
            WhatsAppNotificationService whatsAppNotificationService,
            NotificationLogRepository notificationLogRepository) {
        this.emailNotificationService = emailNotificationService;
        this.whatsAppNotificationService = whatsAppNotificationService;
        this.notificationLogRepository = notificationLogRepository;
    }

    public void notifyAdvisoryCreated(Advisory advisory) {
        String subject = "Nueva asesoria solicitada";
        String message = "Se ha solicitado una asesoria para el " + advisory.getScheduledAt() + ".";
        sendAll(advisory, subject, message);
    }

    public void notifyAdvisoryStatusChanged(Advisory advisory) {
        String subject = "Actualizacion de asesoria";
        String message = "La asesoria ahora esta en estado: " + advisory.getStatus();
        sendAll(advisory, subject, message);
    }

    public void notifyAdvisoryReminder(Advisory advisory) {
        String subject = "Recordatorio de asesoria";
        String message = "Recordatorio: tu asesoria inicia el " + advisory.getScheduledAt() + ".";
        sendAll(advisory, subject, message);
    }

    private void sendAll(Advisory advisory, String subject, String message) {
        sendEmail(advisory, advisory.getProgrammer().getEmail(), subject, message);
        sendEmail(advisory, advisory.getUser().getEmail(), subject, message);
        sendWhatsApp(advisory, advisory.getProgrammer().getEmail(), message);
        sendWhatsApp(advisory, advisory.getUser().getEmail(), message);
    }

    private void sendEmail(Advisory advisory, String recipient, String subject, String message) {
        NotificationStatus status;
        try {
            emailNotificationService.sendEmail(recipient, subject, message);
            status = NotificationStatus.SENT;
        } catch (Exception ex) {
            status = NotificationStatus.FAILED;
        }
        log(advisory, NotificationChannel.EMAIL, recipient, status, message);
    }

    private void sendWhatsApp(Advisory advisory, String recipient, String message) {
        NotificationStatus status;
        try {
            whatsAppNotificationService.sendWhatsApp(recipient, message);
            status = NotificationStatus.SENT;
        } catch (Exception ex) {
            status = NotificationStatus.FAILED;
        }
        log(advisory, NotificationChannel.WHATSAPP, recipient, status, message);
    }

    private void log(
            Advisory advisory,
            NotificationChannel channel,
            String recipient,
            NotificationStatus status,
            String message) {
        NotificationLog log = NotificationLog.builder()
                .advisory(advisory)
                .channel(channel)
                .recipient(recipient)
                .status(status)
                .message(message)
                .createdAt(LocalDateTime.now())
                .build();
        notificationLogRepository.save(log);
    }
}
