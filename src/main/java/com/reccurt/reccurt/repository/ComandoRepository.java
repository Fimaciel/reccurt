package com.reccurt.reccurt.repository;

import com.reccurt.reccurt.model.Comando;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface ComandoRepository extends JpaRepository<Comando, Long> {

    List<Comando> findByUnidadeConsumidoraIdOrderByTimestampDesc(String ucId);

    List<Comando> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime inicio, LocalDateTime fim);
}