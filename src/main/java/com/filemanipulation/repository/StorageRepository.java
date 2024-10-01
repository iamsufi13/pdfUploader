package com.filemanipulation.repository;

import com.filemanipulation.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.util.Optional;

@Repository
public interface StorageRepository extends JpaRepository<FileData, Long> {


    Optional<FileData>findByName(String name);

}
