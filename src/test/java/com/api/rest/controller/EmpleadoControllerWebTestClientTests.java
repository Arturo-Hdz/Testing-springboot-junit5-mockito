package com.api.rest.controller;

import static org.springframework.boot.test.context.SpringBootTest.*;

import java.util.List;

import static org.hamcrest.Matchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.api.rest.model.Empleado;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmpleadoControllerWebTestClientTests {

	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	@Order(1)
	void testGuardarEmpleado() {
		//given
		Empleado empleado = Empleado.builder()
				.id(1L)
				.nombre("Juan")
				.apellido("Perez")
				.email("juan@gmail.com")
				.build();
		
		//when
		webTestClient.post().uri("http://localhost:8080/api/empleados")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(empleado)
			.exchange()
		//then
			.expectStatus().isCreated()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody() 
			.jsonPath("$.id").isEqualTo(empleado.getId())
			.jsonPath("$.nombre").isEqualTo(empleado.getNombre())
			.jsonPath("$.apellido").isEqualTo(empleado.getApellido())
			.jsonPath("$.email").isEqualTo(empleado.getEmail());
	}
	
	@Test
	@Order(2)
	void testObtenerEmpleadoPorId() {
		webTestClient.get().uri("http://localhost:8080/api/empleados/1").exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$.id").isEqualTo(1L)
			.jsonPath("$.nombre").isEqualTo("Juan")
			.jsonPath("$.apellido").isEqualTo("Perez")
			.jsonPath("$.email").isEqualTo("juan@gmail.com");
	}
	
	@Test
	@Order(3)
	void testListarEmpleados() {
		webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBody()
			.jsonPath("$[0].nombre").isEqualTo("Juan")
			.jsonPath("$[0].apellido").isEqualTo("Perez")
			.jsonPath("$[0].email").isEqualTo("juan@gmail.com")
			.jsonPath("$").isArray()
			.jsonPath("$").value(hasSize(1)); //cantidad de empleados
	}
	
	@Test
	@Order(4)
	void testObtenerListadoDeEmpleados() {
		webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Empleado.class)
			.consumeWith(response -> {
				List<Empleado> empleados = response.getResponseBody();
				Assertions.assertEquals(1,empleados.size());
				Assertions.assertNotNull(empleados);
			});
	}
	
	@Test
	@Order(5)
	void testActualizarEmpleado() {
		Empleado empleadoActualizado = Empleado.builder()
				.nombre("Juan2")
				.apellido("Perez2")
				.email("juan2@gmail.com")
				.build();
		
		webTestClient.put().uri("http://localhost:8080/api/empleados/1")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(empleadoActualizado)
			.exchange() //enviar request
			
			//then
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON);
	}
	
	@Test
	@Order(6)
	void testEliminarEmpleado() {
		//comprobar tamano de la lista
		webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Empleado.class)
			.hasSize(1);
		
		//eliminar valor
		webTestClient.delete().uri("http://localhost:8080/api/empleados/1")
			.exchange()
			.expectStatus().isOk();
		
		//comprueba el nuevo tamano
		webTestClient.get().uri("http://localhost:8080/api/empleados").exchange()
			.expectStatus().isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON)
			.expectBodyList(Empleado.class)
			.hasSize(0);
		
		//comprobar que ya no existe
		webTestClient.get().uri("http://localhost:8080/api/empleados/1").exchange()
			.expectStatus().is4xxClientError();
	
	}
	
}
