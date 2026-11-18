package com.iamanshu.university.ui;

import com.iamanshu.university.model.Classroom;
import com.iamanshu.university.model.UniversityMapModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Set;

public class UniversityMapFrame extends JFrame {
    private static final Set<String> PROFESSOR_IDS = Set.of("PROF001", "PROF2026");
    private static final Color OCCUPIED = new Color(220, 70, 70);
    private static final Color FREE = new Color(70, 175, 95);

    private final UniversityMapModel model;
    private final JLabel detailLabel;
    private Classroom selectedClassroom;

    public UniversityMapFrame(UniversityMapModel model) {
        super("University Classroom Map");
        this.model = model;
        this.detailLabel = new JLabel("Select a classroom", SwingConstants.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(900, 650));
        setLayout(new BorderLayout(10, 10));
        add(createHeader(), BorderLayout.NORTH);
        add(createBuildingView(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("University Front View - Classroom Availability", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        header.add(title, BorderLayout.NORTH);
        JLabel legend = new JLabel("  Red = Occupied   |   Green = Free", SwingConstants.CENTER);
        legend.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        header.add(legend, BorderLayout.SOUTH);
        return header;
    }

    private JScrollPane createBuildingView() {
        JPanel building = new JPanel();
        building.setLayout(new GridLayout(UniversityMapModel.FLOORS.length, 1, 8, 8));
        building.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        for (String floor : UniversityMapModel.FLOORS) {
            building.add(createFloorPanel(floor));
        }
        JScrollPane scrollPane = new JScrollPane(building);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        return scrollPane;
    }

    private JPanel createFloorPanel(String floor) {
        JPanel floorPanel = new JPanel(new BorderLayout(10, 5));
        floorPanel.setBorder(BorderFactory.createTitledBorder(floor + " Floor"));
        JLabel label = new JLabel(floor + " floor", SwingConstants.CENTER);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        floorPanel.add(label, BorderLayout.NORTH);

        JPanel classrooms = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        if ("First".equals(floor)) {
            JPanel staffArea = new JPanel(new GridLayout(1, 1));
            staffArea.setPreferredSize(new Dimension(520, 70));
            staffArea.setBackground(new Color(180, 195, 220));
            staffArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
            JLabel staffLabel = new JLabel("STAFF ROOM AREA - No classrooms", SwingConstants.CENTER);
            staffLabel.setFont(staffLabel.getFont().deriveFont(Font.BOLD, 14f));
            staffArea.add(staffLabel);
            classrooms.add(staffArea);
        } else {
            for (Classroom classroom : model.getClassrooms(floor)) {
                classrooms.add(createClassroomBox(classroom));
            }
        }
        floorPanel.add(classrooms, BorderLayout.CENTER);
        return floorPanel;
    }

    private JButton createClassroomBox(Classroom classroom) {
        JButton box = new JButton(classroom.getName());
        box.setOpaque(true);
        box.setContentAreaFilled(true);
        box.setPreferredSize(new Dimension(90, 58));
        box.setFont(box.getFont().deriveFont(Font.BOLD, 13f));
        box.setForeground(Color.WHITE);
        box.setBackground(classroom.isOccupied() ? OCCUPIED : FREE);
        box.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        box.setToolTipText(classroom.getName() + " - " + classroom.getStatus());
        box.addActionListener(event -> selectClassroom(classroom));
        box.putClientProperty("classroom", classroom);
        return box;
    }

    private JPanel createControlPanel() {
        JPanel control = new JPanel(new BorderLayout(10, 5));
        control.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        detailLabel.setFont(detailLabel.getFont().deriveFont(Font.BOLD, 14f));
        control.add(detailLabel, BorderLayout.NORTH);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JTextField professorId = new JTextField(10);
        professorId.setToolTipText("Demo IDs: PROF001 or PROF2026");
        JButton toggleButton = new JButton("Toggle Selected");
        JButton resetButton = new JButton("Reset All to Free");

        toggleButton.addActionListener(event -> {
            String id = professorId.getText().trim().toUpperCase();
            if (!PROFESSOR_IDS.contains(id)) {
                JOptionPane.showMessageDialog(this,
                        "Unauthorized. Enter a demo professor ID: PROF001 or PROF2026.",
                        "Professor Authorization", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (selectedClassroom == null) {
                JOptionPane.showMessageDialog(this, "Select a classroom first.",
                        "No Classroom Selected", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            model.toggle(selectedClassroom.getName());
            refreshSelectedAndBoxes();
        });

        resetButton.addActionListener(event -> {
            model.resetAllToFree();
            refreshSelectedAndBoxes();
        });

        actions.add(new JLabel("Professor ID:"));
        actions.add(professorId);
        actions.add(toggleButton);
        actions.add(resetButton);
        control.add(actions, BorderLayout.SOUTH);
        return control;
    }

    private void selectClassroom(Classroom classroom) {
        selectedClassroom = classroom;
        detailLabel.setText(classroom.getFloor() + " Floor - " +
                classroom.getName() + " | Status: " + classroom.getStatus() +
                " | Capacity: " + classroom.getCapacity());
    }

    private void refreshSelectedAndBoxes() {
        for (Component floorComponent : getBuildingComponents()) {
            refreshComponent(floorComponent);
        }
        if (selectedClassroom != null) {
            selectClassroom(selectedClassroom);
        }
    }

    private Component[] getBuildingComponents() {
        JScrollPane pane = (JScrollPane) getContentPane().getComponent(1);
        JPanel building = (JPanel) pane.getViewport().getView();
        return building.getComponents();
    }

    private void refreshComponent(Component component) {
        if (component instanceof JButton button) {
            Object value = button.getClientProperty("classroom");
            if (value instanceof Classroom classroom) {
                button.setBackground(classroom.isOccupied() ? OCCUPIED : FREE);
                button.setToolTipText(classroom.getName() + " - " + classroom.getStatus());
            }
        } else if (component instanceof JPanel panel) {
            for (Component child : panel.getComponents()) {
                refreshComponent(child);
            }
        }
    }
}
