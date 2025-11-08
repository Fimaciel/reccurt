package com.reccurt.reccurt.repository;

import com.reccurt.reccurt.model.Comando;
import com.reccurt.reccurt.model.LogValidacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogValidacaoRepository extends JpaRepository<LogValidacao, Long> {

    @Query("SELECT l FROM LogValidacao l WHERE l.comando.unidadeConsumidora.id = :ucId ORDER BY l.dataHoraValidacao DESC")
    List<LogValidacao> findByUcId(@Param("ucId") String ucId);

    List<LogValidacao> findByDataHoraValidacaoBetween(LocalDateTime inicio, LocalDateTime fim);
}