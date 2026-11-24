# University Classroom Map

## 1. Project Overview

The University Classroom Map project provides two complementary interfaces for checking university classroom availability:

- A Java Swing desktop application with an in-memory classroom model.
- A responsive browser portal under `web/` with schedule-based availability, date/time inspection, and demo role-based controls.

Both interfaces model the same six-floor building and classroom capacities. Neither interface requires a database or a production backend.

## 2. Current Implementation and Recent Changes

The current project includes:

- The original Java Swing map with classroom selection, capacity/status details, professor authorization, and reset controls.
- A responsive web portal with desktop and mobile layouts.
- Weekday schedule data for CSE, ECE, ME, and IT branches, including sections, subjects, and professors.
- Occupancy calculated for the selected date and time, plus temporary professor overrides for the current browser session.
- Demo professor and student login flows. The login dialog includes a working close control.

The web portal is a dependency-free Node.js static server and browser application. Its schedule and credentials are demonstration data only; no data is persisted after the page is reloaded.

## 3. Objectives

- Display the university building from the ground floor through the fifth floor.
- Show every required classroom in a clearly identifiable colored box.
- Use red to represent an occupied classroom and green to represent a free classroom.
- Let students browse classrooms and view status and capacity.
- Let authorized professors change the selected classroom's status.
- Provide a reset action that makes every classroom free.

## 4. Functional Requirements

| Floor | Rooms or area | Capacity |
|---|---|---|
| Ground | CR-1, CR-2, CR-3, CR-4, CR-5 | 150 |
| First | Staff room area only; no classroom boxes | Not applicable |
| Second | 201, 202, 203, 204 | 90 |
| Third | 301, 302, 303, 304, 305, 306 | 90 |
| Fourth | 401, 402, 403, 404, 405 | 90 |
| Fifth | 501, 502, 503, 504 | 90 |

The status colors are applied to the complete classroom box:

- Red: occupied
- Green: free

## 5. System Architecture

The project follows a simple separation of concerns:

```text
src/main/java/com/iamanshu/university
|-- App.java
|-- model
|   |-- Classroom.java
|   `-- UniversityMapModel.java
`-- ui
    `-- UniversityMapFrame.java
```

This structure separates application startup, data management, and user-interface behavior.

The web portal is organized as:

```text
web
|-- index.html
|-- styles.css
|-- app.js
|-- schedule.js
|-- server.js
`-- tests
    `-- schedule.test.js
```

`index.html` and `styles.css` define the portal layout, `app.js` manages rendering and interaction, `schedule.js` contains floor/account/schedule data and availability lookup, and `server.js` serves the static files locally.

## 6. Code Explanation

### `App.java`

`App` is the application entry point. Its `main` method starts a Swing event on the Event Dispatch Thread using `SwingUtilities.invokeLater`. It selects the system look-and-feel and creates `UniversityMapFrame`.

### `Classroom.java`

`Classroom` represents one room. It stores:

- The room name, such as `CR-1` or `302`
- The floor name
- The seating capacity
- Whether the room is currently occupied

It exposes methods to read room details, change occupancy, toggle occupancy, and produce the display text `Occupied` or `Free`.

### `UniversityMapModel.java`

`UniversityMapModel` creates and owns all classroom objects. Its constructor initializes the floors and required rooms. It provides methods to:

- Retrieve rooms by floor
- Retrieve a room by name
- Toggle a room
- Reset every room to free
- Report the total room count

The initial statuses are sensible demo values. Reopening the application restores these statuses.

### `UniversityMapFrame.java`

`UniversityMapFrame` builds the desktop GUI:

- A title and color legend appear at the top.
- A scrollable building view displays the floors from fifth to ground.
- The first floor contains a clearly labeled staff-room area.
- Classroom buttons use fully painted red or green backgrounds.
- Clicking a classroom updates the detail panel with floor, room, status, and capacity.
- The control area accepts a professor ID and provides toggle/reset actions.

The classroom button override paints the complete button area, which prevents some Swing look-and-feels from reducing the status color to a border or text area.

### Web portal files

- `web/app.js` renders the selected date/time, building map, room details, login state, and professor actions.
- `web/schedule.js` defines the floor map, capacities, demo accounts, weekday schedule entries, and `findActiveEntry`.
- `web/server.js` serves the portal without a framework or external service.

## 7. Professor Authorization

Students can browse and select rooms without entering an ID. A status change requires a demo professor ID.

Authorized IDs:

- `PROF001`
- `PROF2026`

To change occupancy:

1. Click a classroom box.
2. Enter an authorized professor ID.
3. Click **Toggle Selected**.

The toggling code changes only the selected room's state. Unauthorized IDs display a warning and do not alter room status.

## 8. Usage

### Select a classroom

Click any green or red classroom box. The lower detail panel shows its floor, name, status, and capacity.

### Interpret status

- Green fill: the room is free.
- Red fill: the room is occupied.

### Reset the building

Click **Reset All to Free** to set every classroom to free without requiring authorization.

## 9. Requirements

- Java 17 or newer
- Maven 3.8 or newer for Maven builds
- A desktop environment that supports Swing
- Node.js 18 or newer for the web portal

## 10. Build and Run

Run the test suite:

```powershell
mvn clean test
```

Build the executable JAR:

```powershell
mvn clean package
```

Run the application:

```powershell
java -jar target\university-classroom-map-1.0-SNAPSHOT.jar
```

For a JDK-only build, the main sources can also be compiled with:

```powershell
javac -d out src/main/java/com/iamanshu/university/model/*.java src/main/java/com/iamanshu/university/ui/*.java src/main/java/com/iamanshu/university/App.java
java -cp out com.iamanshu.university.App
```

### Run the web portal

```powershell
cd web
npm start
```

Open `http://localhost:4173`. The server uses the `PORT` environment variable when one is provided.

## 11. Testing

The focused JUnit tests verify:

- Every floor has the correct number of classroom boxes.
- The first floor has no classroom boxes.
- Ground classrooms use capacity 150.
- Second-floor classrooms use capacity 90.
- A valid classroom toggle changes its occupancy state.
- Reset changes every classroom to free.
- An unknown room cannot be toggled.

## 12. Web Portal

The repository also includes a responsive website under `web/`. It uses the same classroom and floor requirements while adding schedule data and role-based login.

### Web features

- Real-time date and clock display
- Date/time picker for past or future inspection
- One-hour backward and forward controls
- Multiple branches and sections with professor and subject details
- Dynamic red/green building map at the selected date and time
- Professor and student login roles
- Professor-only occupancy override and reset controls
- Student view-only classroom browsing
- Responsive layout for desktop and mobile screens

### Demo accounts

| Role | Username | Password |
|---|---|---|
| Professor | `professor` | `professor123` |
| Student | `student` | `student123` |

The portal is dependency-free. It uses browser JavaScript modules and a minimal Node.js static-file server. The schedule is imaginary demo data with weekday entries across branches CSE, ECE, ME, and IT.

### Fast-forward through time

Use **+1 hr** or **-1 hr** to move the selected date and time. The datetime picker can inspect any past or future date. Classroom occupancy is recalculated at every control change:

- Red: an active schedule entry occupies the room, or a professor manually overrides it occupied.
- Green: no active schedule entry is assigned at the selected time.

Manual professor overrides are temporary browser-session changes and reset when the page reloads or by clicking **Reset Manual Overrides**.

### Web tests

```powershell
cd web
npm test
```

The tests cover all floor and room counts, capacity values, active schedule lookup, and weekend behavior.
