package com.api.rest.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.api.rest.model.Empleado;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@DataJpaTest // prueba solo a la capa jpa entity
public class EmpleadoRepositoryTests {

	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	private Empleado empleado;
	
	@BeforeEach
	void setUp() {
		empleado = Empleado.builder()
				.nombre("Pepe")
				.apellido("Lopez")
				.email("pepe@gmail.com")
				.build();
		System.out.println("Hola mundo");
	}
	
	@Test
	void testGuardarEmpleado() {
		//BDD Behavior Driven Development
		//metodologia de equipo como debe comportarse le codigo
		//given - dado o condicion previa o configuracion PathVariable
		//se especifica el escenario, precondiciones
		Empleado empleado1 = Empleado.builder()
				.nombre("Pepe")
				.apellido("Lopez")
				.email("pepe@gmail.com")
				.build();
		// when - accion o el comportamiento que vamos a probar
		Empleado empleadoGuardado = empleadoRepository.save(empleado1);
		
		//then - verificar la salida
		assertThat(empleadoGuardado).isNotNull();
		assertThat(empleadoGuardado.getId()).isGreaterThan(0);
	}
	
	@DisplayName("Test para listar a los empleados")
	@Test
	void testListarEmpleados() {
		//given
		Empleado empleado1 = Empleado.builder()
				.nombre("Pepe2")
				.apellido("Lopez2")
				.email("pepe2@gmail.com")
				.build();
		empleadoRepository.save(empleado1);
		empleadoRepository.save(empleado);
		
		// when
		List<Empleado> listaEmpleados = empleadoRepository.findAll();
		
		//then
		assertThat(listaEmpleados).isNotNull();
		assertThat(listaEmpleados.size()).isEqualTo(2);
	}
	
	@DisplayName("Test para obtener un empleado por ID")
	@Test
	void testObtenerEmpleadoPorId() {
		empleadoRepository.save(empleado);
		
		//when - comportamiento o accion que avamos a probar
		Empleado empleadoBD = empleadoRepository.findById(empleado.getId()).get();
		
		//then - verificar la salida
		assertThat(empleadoBD).isNotNull();	
	}
	
	@DisplayName("Test para actualizar un empleado")
	@Test
	void testActualizarEmpleado() {
		empleadoRepository.save(empleado);
		
		//when - comportamiento o accion que avamos a probar
		Empleado empleadoGuardado = empleadoRepository.findById(empleado.getId()).get();
		empleadoGuardado.setEmail("juan@gmail.com");
		empleadoGuardado.setNombre("Juan");
		empleadoGuardado.setApellido("Perez");
		Empleado empleadoActualizado = empleadoRepository.save(empleadoGuardado);
		
		//then - verificar la salida
		assertThat(empleadoActualizado.getEmail()).isEqualTo("juan@gmail.com");
		assertThat(empleadoActualizado.getNombre()).isEqualTo("Juan");	
	}
	
	
	@DisplayName("Test para eliminar un empleado")
	@Test
	void EliminarEmpleado() {
		empleadoRepository.save(empleado);

		//when - comportamiento o accion que avamos a probar
		empleadoRepository.deleteById(empleado.getId());
		Optional<Empleado> empleadoOptional = empleadoRepository.findById(empleado.getId());
		
		//then - verificar la salida
		assertThat(empleadoOptional).isEmpty();
		
	}
	
}
