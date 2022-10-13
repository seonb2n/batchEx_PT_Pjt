package com.example.batchpjt.job.pass;

import com.example.batchpjt.domain.pass.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PassModelMapperTest {

    @DisplayName("Pass Model Mapper 생성 test")
    @Test
    public void givenPassModelParam_whenModelToPassEntity_thenReturnPassEntity() throws Exception {
        //given
        final LocalDateTime nowTime = LocalDateTime.now();
        final String userId = "A100000";

        BulkPassEntity bulkPassEntity = new BulkPassEntity();
        bulkPassEntity.setPackageSeq(1);
        bulkPassEntity.setUserGroupId("GROUP");
        bulkPassEntity.setStatus(BulkPassStatus.COMPLETED);
        bulkPassEntity.setCount(10);
        bulkPassEntity.setStartedAt(nowTime.minusDays(60));
        bulkPassEntity.setEndedAt(nowTime);

        //when
        final PassEntity passEntity = PassModelMapper.INSTANCE.toPassEntity(bulkPassEntity, userId);

        //then
        assertEquals(1, passEntity.getPackageSeq());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(10, passEntity.getRemainingCount());
        assertEquals(nowTime.minusDays(60), passEntity.getStartedAt());
        assertEquals(nowTime, passEntity.getEndedAt());
    }
}
