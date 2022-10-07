package com.api.rest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.api.rest.exception.ResourceNotFoundException;
import com.api.rest.model.Empleado;
import com.api.rest.repository.EmpleadoRepository;
import com.api.rest.service.impl.EmpleadoServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

	@Mock //interfaz o clase, crea simulacros
	private EmpleadoRepository empleadoRepository;
	
	@InjectMocks
	private EmpleadoServiceImpl empleadoService;
		
	private Empleado empleado;
	
	@BeforeEach
	void setUp() {
		empleado = Empleado.builder()
				.id(1L)
				.nombre("Juan")
				.apellido("Perez")
				.email("juan@gmail.com")
				.build();
	}
	
	@DisplayName("test para guardar un empleado")
	@Test
	void testGuardarEmpleado() {
		//given
		given(empleadoRepository.findByEmail(empleado.getEmail()))
			.willReturn(Optional.empty());
		given(empleadoRepository.save(empleado)).willReturn(empleado);
		
		//when
		Empleado empleadoGuardado = empleadoService.saveEmpleado(empleado);
		
		//then
		assertThat(empleadoGuardado).isNotNull();
	}
	
	
	@DisplayName("test para guardar un empleado con Throw Exception")
	@Test
	void testGuardarEmpleadoConThrowException() {
	//given
	given(empleadoRepository.findByEmail(empleado.getEmail()))
		.willReturn(Optional.of(empleado));
	
	//when
	assertThrows(ResourceNotFoundException.class, () -> {
		empleadoService.saveEmpleado(empleado);
	});		
	//then
	verify(empleadoRepository,never()).save(any(Empleado.class));
	}
	
	@DisplayName("test para listar a los empleados")
	@Test
	void testListarEmpleados() {
		//given
		Empleado empleado1 = Empleado.builder()
				.id(1L)
				.nombre("Daniel")
				.apellido("Oliva")
				.email("daniel@gmail.com")
				.build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));

		//when
		List<Empleado> empleados = empleadoService.getAllEmpleados();
		
		//then
		assertThat(empleados).isNotNull();
		assertThat(empleados.size()).isEqualTo(2);
	}
	
	
	@DisplayName("test para retonar una lista vacia")
	@Test
	void testListarCollecionEmpleadosVacia() {
		//given
		Empleado empleado1 = Empleado.builder()
				.id(1L)
				.nombre("Daniel")
				.apellido("Oliva")
				.email("daniel@gmail.com")
				.build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());
		
        //when
		List<Empleado> listaEmpleados = empleadoService.getAllEmpleados();
		//then
		assertThat(listaEmpleados).isEmpty();
		assertThat(listaEmpleados.size()).isEqualTo(0);
	}
	
	@DisplayName("test para obtener un empleado por ID")
	@Test
	void testObtenerEmpleadoPorId() {
		//given
		given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));
		//when
		Empleado empleadoGuardado = empleadoService.getEmpleadoById(empleado.getId()).get();
		//then
		assertThat(empleadoGuardado).isNotNull();
	}
	
	@DisplayName("test para actualizar un empleado")
	@Test
	void testActualizarEmpleado() {
		//given
		given(empleadoRepository.save(empleado)).willReturn(empleado);
		empleado.setEmail("daniel@gmail.com");
		empleado.setNombre("Daniel");
		//when
		Empleado empleadoActualizado = empleadoService.updateEmpleado(empleado);		
		//then
		assertThat(empleadoActualizado.getEmail()).isEqualTo("daniel@gmail.com");
		assertThat(empleadoActualizado.getNombre()).isEqualTo("Daniel");
	}
	
	@DisplayName("test para eliminar un empleado")
	@Test
	void testEliminarEmpleado() {
		//given
		long empleadoId = 1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);
		//when
		empleadoService.deleteEmpleado(empleadoId);
		//then
		verify(empleadoRepository,times(1)).deleteById(empleadoId);
	}
	
}
