package com.example.batchpjt.repository.notification;

import com.example.batchpjt.domain.notification.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {

}
