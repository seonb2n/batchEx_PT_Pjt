package com.example.batchpjt.job.notification;

import com.example.batchpjt.domain.booking.BookingEntity;
import com.example.batchpjt.domain.booking.BookingStatus;
import com.example.batchpjt.domain.notification.NotificationEntity;
import com.example.batchpjt.domain.notification.NotificationEvent;
import com.example.batchpjt.domain.notification.NotificationModelMapper;
import java.time.LocalDateTime;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendNotificationBeforeClassJobConfig {

    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SendNotificationItemWriter sendNotificationItemWriter;

    public SendNotificationBeforeClassJobConfig(JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory, EntityManagerFactory entityManagerFactory,
        SendNotificationItemWriter sendNotificationItemWriter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.sendNotificationItemWriter = sendNotificationItemWriter;
    }

    @Bean
    public Job sendNotificationBeforeClassJob() {
        return this.jobBuilderFactory.get("sendNotificationBeforeClassJob")
            .start(addNotificationStep())
            .build();
    }

    @Bean
    public Step addNotificationStep() {
        return this.stepBuilderFactory.get("addNotificationStep")
            .<BookingEntity, NotificationEntity>chunk(CHUNK_SIZE)
            .reader(addNotificationItemReader())
            .processor(addNotificationItemProcessor())
            .writer(addNotificationItemWriter())
            .build();

    }

    /**
     * JpaPagingItemReader: JPA에서 사용하는 페이징 기법입니다. 쿼리 당 pageSize만큼 가져오며 다른 PagingItemReader와 마찬가지로
     * Thread-safe 합니다.
     */
    @Bean
    public JpaPagingItemReader<BookingEntity> addNotificationItemReader() {
        return new JpaPagingItemReaderBuilder<BookingEntity>()
            .name("addNotificationItemReader")
            .entityManagerFactory(entityManagerFactory)
            // pageSize: 한 번에 조회할 row 수
            .pageSize(CHUNK_SIZE)
            // 상태(status)가 준비중이며, 시작일시(startedAt)이 10분 후 시작하는 예약이 알람 대상이 됩니다.
            .queryString(
                "select b from BookingEntity b join fetch b.userEntity where b.status = :status and b.startedAt <= :startedAt order by b.bookingSeq")
            .parameterValues(Map.of("status", BookingStatus.READY, "startedAt",
                LocalDateTime.now().plusMinutes(10)))
            .build();
    }

    @Bean
    public ItemProcessor<BookingEntity, NotificationEntity> addNotificationItemProcessor() {
        return bookingEntity -> NotificationModelMapper.INSTANCE.toNotificationEntity(bookingEntity,
            NotificationEvent.BEFORE_CLASS);
    }

    @Bean
    public JpaItemWriter<NotificationEntity> addNotificationItemWriter() {
        return new JpaItemWriterBuilder<NotificationEntity>()
            .entityManagerFactory(entityManagerFactory)
            .build();
    }

    @Bean
    public Step sendNotificationStep() {
        return this.stepBuilderFactory.get("sendNotificationStep")
            .<BookingEntity, NotificationEntity>chunk(CHUNK_SIZE)
            .reader(addNotificationItemReader())
            .writer(sendNotificationItemWriter)
            .build();
    }

    @Bean
    public JpaCursorItemReader<NotificationEntity> sendNotificationItemReader() {
        return new JpaCursorItemReaderBuilder<NotificationEntity>()
            .name("addNotificationItemReader")
            .entityManagerFactory(entityManagerFactory)
            // 상태(status)가 준비중이며, 시작일시(startedAt)이 10분 후 시작하는 예약이 알람 대상이 됩니다.
            .queryString(
                "select n from NotificationEntity n where n.event = :event and n.sent = :sent order by n.notificationSeq")
            .parameterValues(Map.of("status", BookingStatus.READY, "startedAt",
                LocalDateTime.now().plusMinutes(10)))
            .build();
    }

}
