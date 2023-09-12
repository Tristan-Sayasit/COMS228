package edu.iastate.cs228.hw1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnimalTest {

    @Test
    void myAge() {
        Plain p = new Plain(3);
        Living animal = new Badger(p, 0, 0, 2);
        assertEquals(((Animal) animal).myAge(), 2);
    }
}