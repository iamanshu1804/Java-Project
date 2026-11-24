import test from "node:test";
import assert from "node:assert/strict";
import { FLOORS, findActiveEntry, SCHEDULE } from "../schedule.js";

test("all required floors and room counts are configured", () => {
  const map = Object.fromEntries(FLOORS.map(floor => [floor.name, floor.rooms?.length || 0]));
  assert.deepEqual(map, { Ground: 5, First: 0, Second: 4, Third: 6, Fourth: 5, Fifth: 4 });
});

test("ground rooms have capacity 150 and upper rooms have capacity 90", () => {
  for (const [name, capacity] of FLOORS[0].rooms) {
    assert.equal(capacity, 150);
  }
  for (const floor of FLOORS.slice(2)) {
    for (const [, capacity] of floor.rooms) assert.equal(capacity, 90);
  }
});

test("findActiveEntry returns room class only during scheduled time", () => {
  const mondayMorning = new Date(2026, 10, 23, 9, 30);
  const active = findActiveEntry(SCHEDULE, "CR-1", mondayMorning);
  assert.equal(active?.subject, "Data Structures");
  assert.equal(active?.professor, "Dr. Ananya Rao");

  const mondayNoon = new Date(2026, 10, 23, 12, 30);
  assert.equal(findActiveEntry(SCHEDULE, "CR-1", mondayNoon), null);
});

test("weekends have no scheduled entries", () => {
  const saturday = new Date(2026, 10, 28, 10, 0);
  assert.equal(findActiveEntry(SCHEDULE, "CR-1", saturday), null);
});
