package com.example.batchpjt.job.notification;

import com.example.batchpjt.domain.notification.NotificationEntity;
import com.example.batchpjt.repository.notification.NotificationRepository;
import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class SendNotificationItemWriter implements ItemWriter<NotificationEntity> {

    private final NotificationRepository notificationRepository;

    public SendNotificationItemWriter(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void write(List<? extends NotificationEntity> items) throws Exception {

    }
}
