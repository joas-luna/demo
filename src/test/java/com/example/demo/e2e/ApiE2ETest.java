package com.example.demo.e2e;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiE2ETest {

    @LocalServerPort
    private int port;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private String baseUrl;
    private String jwtToken;

    @BeforeEach
    void setup() {
        baseUrl = "http://localhost:" + port;
        jwtToken = null;
    }

    private void autenticar(String email, String senha) {
        HttpResult login = request("POST", "/auth/login", Map.of("email", email, "senha", senha));
        if (login.statusCode() == HttpStatus.OK.value()) {
            this.jwtToken = login.body().get("token").asText();
        }
    }

    @Test
    void usuarioEEnderecoFluxoCompleto() {
        HttpResult criarUsuario = request(
                "POST",
                "/usuarios",
                Map.of(
                        "nome", "Ana",
                        "email", "ana@example.com",
                        "senha", "12345678",
                        "pais", "Brasil"
                )
        );

        assertEquals(HttpStatus.CREATED.value(), criarUsuario.statusCode());
        Long usuarioId = criarUsuario.body().get("id").asLong();
        assertNotNull(usuarioId);

        autenticar("ana@example.com", "12345678");

        HttpResult criarEndereco = request(
                "POST",
                "/usuarios/" + usuarioId + "/endereco",
                Map.of(
                        "rua", "Rua A",
                        "numero", "10",
                        "bairro", "Centro",
                        "cidade", "Joao Pessoa",
                        "estado", "PB",
                        "cep", "58000-000"
                )
        );

        assertEquals(HttpStatus.CREATED.value(), criarEndereco.statusCode());

        HttpResult buscarEndereco = request("GET", "/usuarios/" + usuarioId + "/endereco", null);

        assertEquals(HttpStatus.OK.value(), buscarEndereco.statusCode());
        assertEquals("Rua A", buscarEndereco.body().get("rua").asText());

        HttpResult atualizarEndereco = request(
                "PUT",
                "/usuarios/" + usuarioId + "/endereco",
                Map.of(
                        "rua", "Rua B",
                        "numero", "11",
                        "bairro", "Bairro B",
                        "cidade", "Joao Pessoa",
                        "estado", "PB",
                        "cep", "58001-000"
                )
        );

        assertEquals(HttpStatus.OK.value(), atualizarEndereco.statusCode());
        assertEquals("Rua B", atualizarEndereco.body().get("rua").asText());

        HttpResult deletarEndereco = request("DELETE", "/usuarios/" + usuarioId + "/endereco", null);

        assertEquals(HttpStatus.NO_CONTENT.value(), deletarEndereco.statusCode());
    }

    @Test
    void petFluxoCompleto() {
        HttpResult criarUsuario = request(
                "POST",
                "/usuarios",
                Map.of(
                        "nome", "Carlos",
                        "email", "carlos@example.com",
                        "senha", "12345678",
                        "pais", "Brasil"
                )
        );

        Long donoId = criarUsuario.body().get("id").asLong();

        autenticar("carlos@example.com", "12345678");

        HttpResult criarPet = request(
                "POST",
                "/pets",
                Map.of(
                        "nome", "Rex",
                        "tipo", "Cachorro",
                        "idade", 3,
                        "donoId", donoId
                )
        );

        assertEquals(HttpStatus.CREATED.value(), criarPet.statusCode());
        Long petId = criarPet.body().get("id").asLong();

        HttpResult buscarPet = request("GET", "/pets/" + petId, null);
        assertEquals(HttpStatus.OK.value(), buscarPet.statusCode());
        assertEquals("Rex", buscarPet.body().get("nome").asText());

        HttpResult atualizarPet = request(
                "PUT",
                "/pets/" + petId,
                Map.of(
                        "nome", "Rex Atualizado",
                        "tipo", "Cachorro",
                        "idade", 4,
                        "donoId", donoId
                )
        );

        assertEquals(HttpStatus.OK.value(), atualizarPet.statusCode());
        assertEquals("Rex Atualizado", atualizarPet.body().get("nome").asText());

        HttpResult listarPetsDoDono = request("GET", "/pets?donoId=" + donoId, null);
        assertEquals(HttpStatus.OK.value(), listarPetsDoDono.statusCode());
        assertEquals(1, listarPetsDoDono.body().size());

        HttpResult deletarPet = request("DELETE", "/pets/" + petId, null);

        assertEquals(HttpStatus.NO_CONTENT.value(), deletarPet.statusCode());
    }

    @Test
    void criarConsultaComIdGerado() {
        HttpResult criarUsuario = request(
                "POST",
                "/usuarios",
                Map.of(
                        "nome", "Maria",
                        "email", "maria@example.com",
                        "senha", "12345678",
                        "pais", "Brasil"
                )
        );
        Long donoId = criarUsuario.body().get("id").asLong();

        autenticar("maria@example.com", "12345678");

        HttpResult criarPet = request(
                "POST",
                "/pets",
                Map.of(
                        "nome", "Nina",
                        "tipo", "Gato",
                        "idade", 2,
                        "donoId", donoId
                )
        );
        Long petId = criarPet.body().get("id").asLong();

        HttpResult criarVeterinario = request(
                "POST",
                "/veterinarios",
                Map.of(
                        "nome", "Dr Silva",
                        "crmv", "PB-1234"
                )
        );

        assertEquals(HttpStatus.CREATED.value(), criarVeterinario.statusCode());
        Long veterinarioId = criarVeterinario.body().get("id").asLong();

        HttpResult criarConsulta = request(
                "POST",
                "/consultas",
                Map.of(
                        "veterinarioId", veterinarioId,
                        "petId", petId,
                        "data", "2026-06-10T10:00:00"
                )
        );

        assertEquals(HttpStatus.CREATED.value(), criarConsulta.statusCode());
        Long consultaId = criarConsulta.body().get("id").asLong();
        assertEquals("Dr Silva", criarConsulta.body().get("veterinario").get("nome").asText());
        assertEquals("Nina", criarConsulta.body().get("pet").get("nome").asText());
        assertEquals("2026-06-10T10:00", criarConsulta.body().get("data").asText());

        HttpResult criarConsultaDuplicada = request(
                "POST",
                "/consultas",
                Map.of(
                        "veterinarioId", veterinarioId,
                        "petId", petId,
                        "data", "2026-06-10T10:00:00"
                )
        );
        assertEquals(HttpStatus.CONFLICT.value(), criarConsultaDuplicada.statusCode());

        HttpResult listarConsultas = request("GET", "/consultas", null);
        assertEquals(HttpStatus.OK.value(), listarConsultas.statusCode());

        HttpResult buscarConsulta = request("GET", "/consultas/" + consultaId, null);
        assertEquals(HttpStatus.OK.value(), buscarConsulta.statusCode());
        assertEquals("Nina", buscarConsulta.body().get("pet").get("nome").asText());

        HttpResult atualizarConsulta = request(
                "PUT",
                "/consultas/" + consultaId,
                Map.of(
                        "veterinarioId", veterinarioId,
                        "petId", petId,
                        "data", "2026-06-11T11:30:00"
                )
        );

        assertEquals(HttpStatus.OK.value(), atualizarConsulta.statusCode());
        assertEquals("2026-06-11T11:30", atualizarConsulta.body().get("data").asText());

        HttpResult deletarConsulta = request("DELETE", "/consultas/" + consultaId, null);
        assertEquals(HttpStatus.NO_CONTENT.value(), deletarConsulta.statusCode());

        HttpResult buscarConsultaAposDelete = request("GET", "/consultas/" + consultaId, null);
        assertEquals(HttpStatus.NOT_FOUND.value(), buscarConsultaAposDelete.statusCode());
    }

    @Test
    void veterinarioFluxoCompleto() {
        request("POST", "/usuarios", Map.of("nome", "Admin", "email", "admin@example.com", "senha", "admin123", "pais", "Brasil"));
        autenticar("admin@example.com", "admin123");

        HttpResult criarVeterinario = request(
                "POST",
                "/veterinarios",
                Map.of(
                        "nome", "Dra Lima",
                        "crmv", "PB-7777"
                )
        );

        assertEquals(HttpStatus.CREATED.value(), criarVeterinario.statusCode());
        Long veterinarioId = criarVeterinario.body().get("id").asLong();

        HttpResult listarVeterinarios = request("GET", "/veterinarios", null);
        assertEquals(HttpStatus.OK.value(), listarVeterinarios.statusCode());

        HttpResult buscarVeterinario = request("GET", "/veterinarios/" + veterinarioId, null);
        assertEquals(HttpStatus.OK.value(), buscarVeterinario.statusCode());
        assertEquals("Dra Lima", buscarVeterinario.body().get("nome").asText());

        HttpResult atualizarVeterinario = request(
                "PUT",
                "/veterinarios/" + veterinarioId,
                Map.of(
                        "nome", "Dra Lima Atualizada",
                        "crmv", "PB-8888"
                )
        );

        assertEquals(HttpStatus.OK.value(), atualizarVeterinario.statusCode());
        assertEquals("Dra Lima Atualizada", atualizarVeterinario.body().get("nome").asText());

        HttpResult deletarVeterinario = request("DELETE", "/veterinarios/" + veterinarioId, null);
        assertEquals(HttpStatus.NO_CONTENT.value(), deletarVeterinario.statusCode());

        HttpResult buscarVeterinarioAposDelete = request("GET", "/veterinarios/" + veterinarioId, null);
        assertEquals(HttpStatus.NOT_FOUND.value(), buscarVeterinarioAposDelete.statusCode());
    }

    @Test
    void swaggerEndpointsDisponiveis() {
        HttpResult openApiDocs = request("GET", "/v3/api-docs", null);
        assertEquals(HttpStatus.OK.value(), openApiDocs.statusCode());
        assertEquals("3.1.0", openApiDocs.body().get("openapi").asText());
        assertNotNull(openApiDocs.body().get("paths").get("/usuarios"));
        assertNotNull(openApiDocs.body().get("paths").get("/pets"));
        assertNotNull(openApiDocs.body().get("paths").get("/consultas"));

        HttpResult swaggerUi = request("GET", "/swagger-ui/index.html", null);
        assertEquals(HttpStatus.OK.value(), swaggerUi.statusCode());
    }

    private HttpResult request(String method, String path, Object body) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(baseUrl + path))
                    .header("Content-Type", "application/json");

            if (this.jwtToken != null) {
                builder.header("Authorization", "Bearer " + this.jwtToken);
            }

            if (body == null) {
                builder.method(method, HttpRequest.BodyPublishers.noBody());
            } else {
                builder.method(method, HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(body)));
            }

            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            JsonNode json;
            if (response.body() == null || response.body().isBlank()) {
                json = objectMapper.nullNode();
            } else {
                try {
                    json = objectMapper.readTree(response.body());
                } catch (Exception ignored) {
                    json = objectMapper.getNodeFactory().textNode(response.body());
                }
            }
            return new HttpResult(response.statusCode(), json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private record HttpResult(int statusCode, JsonNode body) {
    }
}



