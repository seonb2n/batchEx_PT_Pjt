package com.example.batchpjt.repository;

import com.example.batchpjt.domain.packaze.PackageEntity;
import com.example.batchpjt.repository.packagze.PackageRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@SpringBootTest
@ActiveProfiles("test")
public class PackageRepositoryTest {

    @Autowired
    private PackageRepository packageRepository;

    @DisplayName("package 저장 로직 테스트")
    @Test
    public void givenPackageDto_whenSavePackage_thenSavePackage() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디 PT 12주");
        packageEntity.setPeriod(12 * 7);

        //when
        packageRepository.save(packageEntity);

        //then
        assertNotNull(packageEntity.getPackageSeq());
    }

    @DisplayName("package 생성 날짜로 find 테스트")
    @Test
    public void givenCreatedDateTime_whenFindByCreated_thenReturnPackages() throws Exception {
        //given
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
        PackageEntity packageEntity1 = new PackageEntity();
        packageEntity1.setPackageName("학생 전용 3개월");
        packageEntity1.setPeriod(30 * 3);
        packageRepository.save(packageEntity1);

        PackageEntity packageEntity2 = new PackageEntity();
        packageEntity2.setPackageName("학생 전용 6개월");
        packageEntity2.setPeriod(30 * 6);
        packageRepository.save(packageEntity2);

        //when
        var result = packageRepository.findByCreatedAtAfter(dateTime,
                PageRequest.of(0, 1, Sort.by("packageSeq").descending()));

        //then
        assertEquals(1, result.size());
        assertEquals(packageEntity2.getPackageSeq(), result.get(0).getPackageSeq());
    }

    @DisplayName("packageEntity 업데이트 테스트")
    @Test
    public void givenPackageEntity_whenUpdatePackageEntity_thenUpdate() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("바디프로필 4개월");
        packageEntity.setPeriod(3 * 30);
        packageRepository.save(packageEntity);

        //when
        int updatedCount = packageRepository.updateCountAndPeriod(packageEntity.getPackageSeq(), 30, 120);
        PackageEntity foundEntity = packageRepository.findById(packageEntity.getPackageSeq()).get();
        foundEntity.setPeriod(4 * 30);

        //then
        assertEquals(1, updatedCount);
        assertEquals(120, (int) foundEntity.getPeriod());

    }

    @DisplayName("Package Delete 테스트")
    @Test
    public void givenPackageEntity_whenDeletePackage_thenDelete() throws Exception {
        //given
        PackageEntity packageEntity = new PackageEntity();
        packageEntity.setPackageName("제거할 패키지");
        packageEntity.setCount(1);
        PackageEntity savedEntity = packageRepository.save(packageEntity);

        //when
        packageRepository.deleteById(savedEntity.getPackageSeq());

        //then
        assertTrue(packageRepository.findById(packageEntity.getPackageSeq()).isEmpty());
    }

}
