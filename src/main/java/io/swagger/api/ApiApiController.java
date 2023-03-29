package io.swagger.api;

import io.swagger.model.Server;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.repository.ServerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2023-03-29T13:16:33.723429343Z[GMT]")
@RestController
public class ApiApiController implements ApiApi {

    @Autowired
    private ServerRepository serverRepository;
    private static final Logger log = LoggerFactory.getLogger(ApiApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ApiApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    @Override
    public ResponseEntity<Server> addServer(Server server) {
        try {
            Server addedServer = serverRepository.save(server); // Add the Server object to the repository and return it
            return ResponseEntity.ok(addedServer);
        } catch (Exception e) {
            // handling exception
            System.out.println("An error occurred while adding the server: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<String> deleteServer(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("serverId") String serverId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                Optional<Object> existingServer = serverRepository.findById(serverId); // Retrieve the Server object with the specified ID from the repository
                if (existingServer.isPresent()) {
                    serverRepository.delete(existingServer.get()); // Delete the Server object from the repository
                    return ResponseEntity.ok("Server with ID " + serverId + " has been deleted"); // Return a success message
                } else {
                    return ResponseEntity.notFound().build(); // Return a failure message
                }
            } catch (Exception e) {
                // handling exception
                System.out.println("An error occurred while deleting the server: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return new ResponseEntity<String>(HttpStatus.NOT_IMPLEMENTED);
    }
    public ResponseEntity<List<Server>> getAllServers() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                List<Server> servers = serverRepository.findAll();
                if (!servers.isEmpty()) {
                    return ResponseEntity.ok(servers); // Return the list of Server objects with a success status code
                } else {
                    return ResponseEntity.notFound().build(); // Return a failure status code if no servers are found
                }
            } catch (Exception e) {
                // handling exception
                System.out.println("An error occurred while retrieving the list of servers: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return an error status code
            }
        }

        return new ResponseEntity<List<Server>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Server> getServerById(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("serverId") String serverId) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                Server server = (Server) serverRepository.findById(serverId).orElse(null);
                if (server != null) {
                    return ResponseEntity.ok(server); // Return the Server object with a success status code
                } else {
                    return ResponseEntity.notFound().build(); // Return a failure status code if no server with the specified ID is found
                }
            } catch (Exception e) {
                // handling exception
                System.out.println("An error occurred while retrieving the server: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Return an error status code
            }
        }

        return new ResponseEntity<Server>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Server>> getServersByName(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("name") String name) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                // Retrieve all Server objects with the specified name from the repository
                List<Server> servers = serverRepository.findAllByName(name);
                if (!servers.isEmpty()) {
                    return ResponseEntity.ok(servers);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception e) {
                // Handle any exceptions
                System.err.println("An error occurred while retrieving the server: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return new ResponseEntity<List<Server>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Server> updateServer(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Server server) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            Server existingServer = null;
            try {
                // Try to find the existing server object in the repository by its ID
                existingServer = (Server) serverRepository.findById(server.getServerId()).orElse(null);
                if (existingServer != null) { // If the existing server is found, update its properties
                    existingServer.setName(server.getName());
                    existingServer.setFramework(server.getFramework());
                    existingServer.setLanguage(server.getLanguage());
                    serverRepository.save(existingServer); // Save the updated server object to the repository
                    return ResponseEntity.ok(existingServer);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch(Exception e) { // If an error occurs, catch it and log a message
                // handle exception
                System.out.println("An error occurred while updating the server: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        return new ResponseEntity<Server>(HttpStatus.NOT_IMPLEMENTED);
    }

}
