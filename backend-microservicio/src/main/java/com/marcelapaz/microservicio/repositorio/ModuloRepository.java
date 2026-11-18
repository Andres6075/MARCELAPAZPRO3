package com.marcelapaz.microservicio.repositorio;

import com.marcelapaz.microservicio.modelo.ModuloEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuloRepository extends JpaRepository<ModuloEntity, Integer> {
}
