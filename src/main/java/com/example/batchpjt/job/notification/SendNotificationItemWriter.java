package com.example.batchpjt.job.notification;

import com.example.batchpjt.domain.notification.NotificationEntity;
import com.example.batchpjt.repository.notification.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendNotificationItemWriter implements ItemWriter<NotificationEntity> {

    private final NotificationRepository notificationRepository;

    @Override
    public void write(List<? extends NotificationEntity> items) throws Exception {

    }
}
