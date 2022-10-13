package com.example.batchpjt.job.pass;

import com.example.batchpjt.TestBatchConfig;
import com.example.batchpjt.domain.pass.PassEntity;
import com.example.batchpjt.domain.pass.PassStatus;
import com.example.batchpjt.repository.pass.PassRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {ExpirePassesJobConfig.class, TestBatchConfig.class})
class ExpirePassesJobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private PassRepository passRepositroy;

    @DisplayName("기간 만료 Batch 테스트")
    @Test
    public void givenEndedPass_whenBatchRun_thenExpirePass() throws Exception {
        //given
        addPassEntities(10);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        JobInstance jobInstance = jobExecution.getJobInstance();

        //then
        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals("expirePassesJob", jobInstance.getJobName());
    }

    private void addPassEntities(int size) {
        final LocalDateTime now = LocalDateTime.now();
        final Random random = new Random();

        List<PassEntity> passEntityList = new ArrayList<PassEntity>();
        for (int i = 0; i < size; i++) {
            PassEntity passEntity = new PassEntity();
            passEntity.setPackageSeq(1);
            passEntity.setUserId("A" + 10000 + i);
            passEntity.setStatus(PassStatus.PROGRESSED);
            passEntity.setRemainingCount(random.nextInt(11));
            passEntity.setStartedAt(now.minusDays(60));
            passEntity.setEndedAt(now.minusDays(1));
            passEntityList.add(passEntity);
        }

        passRepositroy.saveAll(passEntityList);
    }
}