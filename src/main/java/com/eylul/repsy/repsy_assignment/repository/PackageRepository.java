package com.eylul.repsy.repsy_assignment.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.eylul.repsy.repsy_assignment.model.PackageEntity;

public interface PackageRepository extends JpaRepository<PackageEntity, Long> {

    
}
