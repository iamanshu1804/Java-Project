export const FLOORS = [
  { name: "Ground", rooms: [["CR-1",150],["CR-2",150],["CR-3",150],["CR-4",150],["CR-5",150]] },
  { name: "First", staff: true },
  { name: "Second", rooms: [["201",90],["202",90],["203",90],["204",90]] },
  { name: "Third", rooms: [["301",90],["302",90],["303",90],["304",90],["305",90],["306",90]] },
  { name: "Fourth", rooms: [["401",90],["402",90],["403",90],["404",90],["405",90]] },
  { name: "Fifth", rooms: [["501",90],["502",90],["503",90],["504",90]] },
];

export const ACCOUNTS = {
  professor: { password: "professor123", role: "professor", name: "Dr. Ananya Rao" },
  student: { password: "student123", role: "student", name: "Student" }
};

const entries = [
  ["CR-1","CSE","A","Data Structures","Dr. Ananya Rao",9,10],
  ["CR-2","ECE","A","Digital Electronics","Prof. Vikram Shah",9,11],
  ["CR-3","ME","A","Thermodynamics","Prof. Rahul Mehta",10,12],
  ["201","CSE","B","Database Systems","Dr. Neha Kapoor",11,13],
  ["202","ECE","B","Signals and Systems","Prof. Isha Sen",8,10],
  ["203","IT","A","Web Technologies","Prof. Arjun Das",13,15],
  ["204","CSE","C","Operating Systems","Dr. Kavya Iyer",14,16],
  ["301","ME","B","Fluid Mechanics","Prof. Sanjay Bose",9,11],
  ["302","ECE","C","Microprocessors","Dr. Pooja Nair",12,14],
  ["303","IT","B","Computer Networks","Prof. Amit Jain",10,12],
  ["304","CSE","A","Artificial Intelligence","Dr. Rohan Singh",15,17],
  ["305","ME","C","Machine Design","Prof. Tara Menon",8,10],
  ["306","IT","C","Cloud Computing","Dr. Aditya Verma",13,15],
  ["401","CSE","D","Software Engineering","Prof. Leena Gupta",9,11],
  ["402","ECE","D","Communication Systems","Dr. Mohan Lal",11,13],
  ["403","ME","D","Manufacturing Systems","Prof. Sneha Roy",13,15],
  ["404","IT","D","Cyber Security","Dr. Farhan Ali",10,12],
  ["405","CSE","B","Compiler Design","Prof. Mira Patel",14,16],
  ["501","ECE","A","Embedded Systems","Dr. Harish Kumar",8,10],
  ["502","CSE","C","Computer Graphics","Prof. Nisha Shah",11,13],
  ["503","ME","A","Engineering Mechanics","Dr. Ravi Singh",15,17],
  ["504","IT","A","Mobile Application Development","Prof. Zoya Khan",12,14],
];

export const SCHEDULE = entries.map(([room, branch, section, subject, professor, startHour, endHour]) => ({
  room, branch, section, subject, professor,
  startHour, endHour,
  days: [1, 2, 3, 4, 5]
}));

export function findActiveEntry(schedule, room, date) {
  const day = date.getDay();
  const minute = date.getHours() * 60 + date.getMinutes();
  return schedule.find(entry =>
    entry.room === room &&
    entry.days.includes(day) &&
    minute >= entry.startHour * 60 &&
    minute < entry.endHour * 60
  ) || null;
}
