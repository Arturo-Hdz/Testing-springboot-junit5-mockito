package com.api.rest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api.rest.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long>{
	Optional<Empleado> findByEmail(String email);
}
