package com.flexidesk.repository;

import com.flexidesk.entity.Desk;
import com.flexidesk.entity.enums.DeskType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeskRepository extends JpaRepository<Desk,Long> {
    List<Desk> findByDeskType(DeskType deskType);
    List<Desk> findByAvailableGreaterThan(Integer available);
}
