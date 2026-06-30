package com.greet.service;

import com.greet.model.Greeting;

import java.util.List;

public interface GreetingService {

    List<Greeting> getAllGreetings();

    Greeting getGreetingById(int id);

    void createGreeting(Greeting greeting);

    void updateGreeting(Greeting greeting);

    void deleteGreeting(int id);
}
