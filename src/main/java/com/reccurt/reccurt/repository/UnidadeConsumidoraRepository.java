package com.reccurt.reccurt.repository;

import com.reccurt.reccurt.model.UnidadeConsumidora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface UnidadeConsumidoraRepository extends JpaRepository<UnidadeConsumidora, String> {

    boolean existsByIdAndTipo(String id, UnidadeConsumidora.Tipo tipo);
}