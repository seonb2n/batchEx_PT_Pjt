package com.example.batchpjt.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.batchpjt.domain.pass.BulkPassEntity;
import com.example.batchpjt.domain.pass.BulkPassStatus;
import com.example.batchpjt.domain.pass.PassEntity;
import com.example.batchpjt.domain.pass.PassModelMapper;
import com.example.batchpjt.domain.pass.PassStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class PassModelMapperTest {

    @Test
    public void test_toPassEntity() {
        // given
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A1000000";

        BulkPassEntity bulkPassEntity = new BulkPassEntity();
        bulkPassEntity.setPackageSeq(1);
        bulkPassEntity.setUserGroupId("GROUP");
        bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        bulkPassEntity.setCount(10);
        bulkPassEntity.setStartedAt(now.minusDays(60));
        bulkPassEntity.setEndedAt(now);

        // when
        final PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);

        // then
        assertEquals(1, passEntity.getPackageSeq());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(10, passEntity.getRemainingCount());
        assertEquals(now.minusDays(60), passEntity.getStartedAt());
        assertEquals(now, passEntity.getEndedAt());

    }
}