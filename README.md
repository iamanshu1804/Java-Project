# University Classroom Map

A Java Swing desktop application for viewing a university building's front view and classroom occupancy.

## Requirements

- Java 17 or newer
- Maven 3.8 or newer

## Run

```powershell
mvn clean test
mvn clean package
java -jar target\university-classroom-map-1.0-SNAPSHOT.jar
```

Students can browse and select classroom boxes. Professor status changes require a demo professor ID:
`PROF001` or `PROF2026`. Select a classroom, enter an authorized ID, then click **Toggle Selected**.
