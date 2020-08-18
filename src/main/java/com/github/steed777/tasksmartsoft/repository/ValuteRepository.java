package com.github.steed777.tasksmartsoft.repository;

import com.github.steed777.tasksmartsoft.model.Valute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ValuteRepository extends JpaRepository<Valute, Integer> {
    @Query("from Valute")
    List<Valute> findByValutes();
}
