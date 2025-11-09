package com.reccurt.reccurt.repository;

import com.reccurt.reccurt.model.Feriado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FeriadoRepository extends JpaRepository<Feriado, Long> {

    @Query("SELECT f FROM Feriado f WHERE f.data = :data")
    List<Feriado> findByData(@Param("data") LocalDate data);

    @Query("SELECT f FROM Feriado f WHERE f.data = :data AND (f.tipo = 'nacional' OR f.regiao = :regiao)")
    List<Feriado> findByDataERegiao(@Param("data") LocalDate data, @Param("regiao") String regiao);

    List<Feriado> findByDataBetween(LocalDate data1, LocalDate data2);
}