package com.iamanshu.university.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class UniversityMapModel {
    public static final String[] FLOORS =
            {"Fifth", "Fourth", "Third", "Second", "First", "Ground"};

    private final Map<String, List<Classroom>> classroomsByFloor = new LinkedHashMap<>();
    private final Map<String, Classroom> classroomsByName = new LinkedHashMap<>();

    public UniversityMapModel() {
        initialize();
    }

    private void initialize() {
        for (String floor : FLOORS) {
            classroomsByFloor.put(floor, new ArrayList<>());
        }

        add("CR-1", "Ground", 150, true);
        add("CR-2", "Ground", 150, false);
        add("CR-3", "Ground", 150, true);
        add("CR-4", "Ground", 150, false);
        add("CR-5", "Ground", 150, false);

        add("201", "Second", 90, true);
        add("202", "Second", 90, false);
        add("203", "Second", 90, true);
        add("204", "Second", 90, false);

        add("301", "Third", 90, false);
        add("302", "Third", 90, true);
        add("303", "Third", 90, false);
        add("304", "Third", 90, false);
        add("305", "Third", 90, true);
        add("306", "Third", 90, false);

        add("401", "Fourth", 90, true);
        add("402", "Fourth", 90, false);
        add("403", "Fourth", 90, false);
        add("404", "Fourth", 90, true);
        add("405", "Fourth", 90, false);

        add("501", "Fifth", 90, false);
        add("502", "Fifth", 90, true);
        add("503", "Fifth", 90, false);
        add("504", "Fifth", 90, false);
    }

    private void add(String name, String floor, int capacity, boolean occupied) {
        Classroom classroom = new Classroom(name, floor, capacity, occupied);
        classroomsByFloor.get(floor).add(classroom);
        classroomsByName.put(name, classroom);
    }

    public List<Classroom> getClassrooms(String floor) {
        return Collections.unmodifiableList(classroomsByFloor.get(floor));
    }

    public Classroom getClassroom(String name) {
        return classroomsByName.get(name);
    }

    public boolean toggle(String classroomName) {
        Classroom classroom = classroomsByName.get(classroomName);
        if (classroom == null) {
            return false;
        }
        classroom.toggleOccupied();
        return true;
    }

    public void resetAllToFree() {
        for (Classroom classroom : classroomsByName.values()) {
            classroom.setOccupied(false);
        }
    }

    public int getClassroomCount() {
        return classroomsByName.size();
    }
}
