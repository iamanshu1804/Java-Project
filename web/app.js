import { ACCOUNTS, FLOORS, SCHEDULE, findActiveEntry } from "./schedule.js";

const state = {
  role: "student",
  selectedRoom: null,
  userName: "Guest",
  overrides: new Map()
};

const dateTimeInput = document.querySelector("#dateTime");
const buildingMap = document.querySelector("#buildingMap");
const detailsName = document.querySelector("#detailsName");
const detailsStatus = document.querySelector("#detailsStatus");
const detailsGrid = document.querySelector("#detailsGrid");
const professorActions = document.querySelector("#professorActions");
const loginDialog = document.querySelector("#loginDialog");

function toInputDate(date) {
  const pad = value => String(value).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth()+1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
}

function selectedDate() {
  return dateTimeInput.value ? new Date(dateTimeInput.value) : new Date();
}

function getCapacity(roomName) {
  for (const floor of FLOORS) {
    const found = floor.rooms?.find(([name]) => name === roomName);
    if (found) return found[1];
  }
  return null;
}

function getFloor(roomName) {
  return FLOORS.find(floor => floor.rooms?.some(([name]) => name === roomName))?.name || "";
}

function renderClock() {
  document.querySelector("#liveClock").textContent =
    new Intl.DateTimeFormat(undefined, {
      weekday: "short", year: "numeric", month: "short", day: "numeric",
      hour: "2-digit", minute: "2-digit", second: "2-digit"
    }).format(new Date());
}

function renderMap() {
  const date = selectedDate();
  document.querySelector("#viewHeading").textContent =
    `Classroom map - ${new Intl.DateTimeFormat(undefined, {
      weekday: "short", month: "short", day: "numeric",
      hour: "2-digit", minute: "2-digit"
    }).format(date)}`;

  buildingMap.innerHTML = "";
  for (const floor of [...FLOORS].reverse()) {
    const section = document.createElement("section");
    section.className = "floor";
    section.innerHTML = `<div class="floor-title">${floor.name}<br>Floor</div>`;
    const rooms = document.createElement("div");
    rooms.className = "floor-rooms";

    if (floor.staff) {
      rooms.innerHTML = '<div class="staff-room">STAFF ROOM AREA - NO CLASSROOMS</div>';
    } else {
      floor.rooms.forEach(([name]) => {
        const key = `${date.toDateString()}-${name}-${date.getHours()}:${date.getMinutes()}`;
        const override = state.overrides.get(key);
        const entry = findActiveEntry(SCHEDULE, name, date);
        const occupied = override ?? Boolean(entry);
        const button = document.createElement("button");
        button.type = "button";
        button.className = `room ${occupied ? "occupied" : "free"}${state.selectedRoom === name ? " selected" : ""}`;
        button.textContent = name;
        button.title = occupied
          ? `${name}: Occupied - ${entry?.subject || "Manual override"}`
          : `${name}: Free`;
        button.addEventListener("click", () => selectRoom(name));
        rooms.appendChild(button);
      });
    }
    section.appendChild(rooms);
    buildingMap.appendChild(section);
  }
  if (state.selectedRoom) selectRoom(state.selectedRoom, false);
}

function selectRoom(name, rerender = true) {
  state.selectedRoom = name;
  const date = selectedDate();
  const entry = findActiveEntry(SCHEDULE, name, date);
  const key = `${date.toDateString()}-${name}-${date.getHours()}:${date.getMinutes()}`;
  const occupied = state.overrides.get(key) ?? Boolean(entry);
  detailsName.textContent = name;
  detailsStatus.textContent = occupied ? "Occupied" : "Free";
  detailsStatus.style.color = occupied ? "#dc4646" : "#45af60";
  detailsGrid.innerHTML = "";

  const rows = [
    ["Floor", getFloor(name)],
    ["Capacity", `${getCapacity(name)} students`],
    ["Status", occupied ? "Occupied" : "Free"],
  ];
  if (entry) {
    rows.push(
      ["Branch", `${entry.branch} - Section ${entry.section}`],
      ["Subject", entry.subject],
      ["Professor", entry.professor],
      ["Time", `${String(entry.startHour).padStart(2,"0")}:00 - ${String(entry.endHour).padStart(2,"0")}:00`]
    );
  } else {
    rows.push(["Schedule", "No class at selected time"]);
  }
  rows.forEach(([label, value]) => {
    const row = document.createElement("div");
    row.className = "detail-row";
    row.innerHTML = `<span>${label}</span><strong>${value}</strong>`;
    detailsGrid.appendChild(row);
  });
  professorActions.hidden = state.role !== "professor";
  if (rerender) renderMap();
}

function updateLoginUi() {
  document.querySelector("#loginButton").textContent =
    state.role === "student" && state.userName === "Guest"
      ? "Professor / Student Login"
      : `${state.userName} - Logout`;
  document.querySelector("#roleBadge").textContent =
    state.role === "professor" ? "Professor controls" : "Student view";
  professorActions.hidden = state.role !== "professor";
}

dateTimeInput.value = toInputDate(new Date());
dateTimeInput.addEventListener("input", renderMap);
document.querySelector("#nowButton").addEventListener("click", () => {
  dateTimeInput.value = toInputDate(new Date());
  renderMap();
});
document.querySelectorAll("[data-offset]").forEach(button => {
  button.addEventListener("click", () => {
    const date = selectedDate();
    date.setMinutes(date.getMinutes() + Number(button.dataset.offset));
    dateTimeInput.value = toInputDate(date);
    renderMap();
  });
});

document.querySelector("#loginButton").addEventListener("click", () => {
  if (state.userName !== "Guest") {
    state.role = "student";
    state.userName = "Guest";
    updateLoginUi();
    renderMap();
  } else {
    loginDialog.showModal();
  }
});

document.querySelector("#dialogClose").addEventListener("click", () => {
  document.querySelector("#loginError").textContent = "";
  loginDialog.close();
});

document.querySelector("#loginForm").addEventListener("submit", event => {
  event.preventDefault();
  const username = document.querySelector("#username").value.trim().toLowerCase();
  const password = document.querySelector("#password").value;
  const account = ACCOUNTS[username];
  if (!account || account.password !== password) {
    document.querySelector("#loginError").textContent = "Invalid demo username or password.";
    return;
  }
  state.role = account.role;
  state.userName = account.name;
  document.querySelector("#loginError").textContent = "";
  loginDialog.close();
  updateLoginUi();
  renderMap();
});

document.querySelector("#toggleButton").addEventListener("click", () => {
  if (!state.selectedRoom || state.role !== "professor") return;
  const date = selectedDate();
  const key = `${date.toDateString()}-${state.selectedRoom}-${date.getHours()}:${date.getMinutes()}`;
  const entry = findActiveEntry(SCHEDULE, state.selectedRoom, date);
  const current = state.overrides.get(key) ?? Boolean(entry);
  state.overrides.set(key, !current);
  renderMap();
});

document.querySelector("#resetButton").addEventListener("click", () => {
  state.overrides.clear();
  renderMap();
});

renderClock();
setInterval(renderClock, 1000);
renderMap();
updateLoginUi();
