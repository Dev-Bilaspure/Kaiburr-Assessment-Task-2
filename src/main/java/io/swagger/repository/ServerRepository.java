package io.swagger.repository;

import io.swagger.model.Server;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ServerRepository extends MongoRepository<Server, String> {

    public List<Server> findAllByName(String name);

    public List<Server> findAll();

    public Optional<Object> findById(String serverId);

    public void delete(Object o);

    public Server save(Server existingServer);
}
