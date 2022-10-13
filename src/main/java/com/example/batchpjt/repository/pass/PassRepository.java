package com.example.batchpjt.repository.pass;

import com.example.batchpjt.domain.pass.PassEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<PassEntity, Integer> {
}
