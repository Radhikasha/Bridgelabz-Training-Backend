package com.greet.repository;

import com.greet.model.Greeting;

import java.util.List;

public interface GreetingRepository {

    List<Greeting> findAll();

    Greeting findById(int id);

    void save(Greeting greeting);

    void update(Greeting greeting);

    void delete(int id);
}
