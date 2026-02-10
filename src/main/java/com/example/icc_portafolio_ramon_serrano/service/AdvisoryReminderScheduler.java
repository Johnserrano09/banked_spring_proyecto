package com.example.icc_portafolio_ramon_serrano.service;

import com.example.icc_portafolio_ramon_serrano.config.NotificationProperties;
import com.example.icc_portafolio_ramon_serrano.model.Advisory;
import com.example.icc_portafolio_ramon_serrano.model.AdvisoryStatus;
import com.example.icc_portafolio_ramon_serrano.repository.AdvisoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AdvisoryReminderScheduler {

    private final AdvisoryRepository advisoryRepository;
    private final NotificationDispatcher notificationDispatcher;
    private final AdvisoryService advisoryService;
    private final NotificationProperties notificationProperties;

    public AdvisoryReminderScheduler(
            AdvisoryRepository advisoryRepository,
            NotificationDispatcher notificationDispatcher,
            AdvisoryService advisoryService,
            NotificationProperties notificationProperties) {
        this.advisoryRepository = advisoryRepository;
        this.notificationDispatcher = notificationDispatcher;
        this.advisoryService = advisoryService;
        this.notificationProperties = notificationProperties;
    }

    @Scheduled(fixedDelayString = "${app.notifications.scheduler-delay-ms:60000}")
    public void sendReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.plusMinutes(notificationProperties.getReminderMinutesBefore());
        List<Advisory> advisories = advisoryRepository
                .findByStatusAndReminderSentFalseAndScheduledAtBetween(AdvisoryStatus.CONFIRMED, now, end);
        for (Advisory advisory : advisories) {
            notificationDispatcher.notifyAdvisoryReminder(advisory);
            advisoryService.markReminderSent(advisory);
        }
    }
}
