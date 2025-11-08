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
    List<Feriado> findByData(LocalDate data);

    @Query("SELECT f FROM Feriado f WHERE f.data = :data AND (f.tipo = 'NACIONAL' OR f.regiao = :regiao)")
    List<Feriado> findByDataAndRegiaoOuNacional(@Param("data") LocalDate data,
                                                @Param("regiao") String regiao);
}