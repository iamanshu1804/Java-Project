package com.iamanshu.university.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UniversityMapModelTest {
    @Test
    void initializesEveryRequiredFloorAndClassroomSet() {
        UniversityMapModel model = new UniversityMapModel();
        assertEquals(5, model.getClassrooms("Ground").size());
        assertEquals(0, model.getClassrooms("First").size());
        assertEquals(4, model.getClassrooms("Second").size());
        assertEquals(6, model.getClassrooms("Third").size());
        assertEquals(5, model.getClassrooms("Fourth").size());
        assertEquals(4, model.getClassrooms("Fifth").size());
        assertEquals(24, model.getClassroomCount());
    }

    @Test
    void groundAndSecondFloorClassroomsHaveCorrectCapacity() {
        UniversityMapModel model = new UniversityMapModel();
        assertEquals(150, model.getClassroom("CR-1").getCapacity());
        assertEquals(90, model.getClassroom("201").getCapacity());
    }

    @Test
    void togglesSelectedClassroomAndResetMakesAllFree() {
        UniversityMapModel model = new UniversityMapModel();
        Classroom classroom = model.getClassroom("CR-2");
        boolean original = classroom.isOccupied();
        assertTrue(model.toggle("CR-2"));
        assertEquals(!original, classroom.isOccupied());
        model.resetAllToFree();
        for (String floor : UniversityMapModel.FLOORS) {
            for (Classroom item : model.getClassrooms(floor)) {
                assertFalse(item.isOccupied());
            }
        }
    }

    @Test
    void unknownClassroomCannotBeToggled() {
        UniversityMapModel model = new UniversityMapModel();
        assertFalse(model.toggle("NOT-A-ROOM"));
    }
}
