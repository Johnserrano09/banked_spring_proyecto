package com.example.icc_portafolio_ramon_serrano.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.notifications")
public class NotificationProperties {

    private long reminderMinutesBefore;

    public long getReminderMinutesBefore() {
        return reminderMinutesBefore;
    }

    public void setReminderMinutesBefore(long reminderMinutesBefore) {
        this.reminderMinutesBefore = reminderMinutesBefore;
    }
}
