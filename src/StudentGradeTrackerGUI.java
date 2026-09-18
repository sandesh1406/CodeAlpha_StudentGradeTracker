package src;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Professional GUI for the Student Grade Tracker application.
 * Final polish pass for internship evaluation.
 */
public class StudentGradeTrackerGUI extends JFrame {
    private final GradeManager gradeManager;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    // Professional Color Palette
    private final Color COLOR_SIDEBAR = new Color(33, 37, 41);
    private final Color COLOR_ACCENT = new Color(13, 110, 253);
    private final Color COLOR_DANGER = new Color(220, 53, 69);
    private final Color COLOR_BG = new Color(248, 249, 250);
    private final Color COLOR_CARD_BG = Color.WHITE;
    private final Color COLOR_TEXT_DARK = new Color(33, 37, 41);
    private final Color COLOR_TEXT_MUTED = new Color(108, 117, 125);
    private final Color COLOR_BORDER = new Color(230, 230, 230);
    private final Color COLOR_NAV_ACTIVE_BG = new Color(45, 49, 53);

    // Panels
    private JPanel dashboardPanel;
    private JPanel studentsPanel;
    private JPanel addStudentPanel;
    private JPanel addGradePanel;
    private JPanel detailsPanel;
    private JPanel summaryPanel;

    // Components for Dynamic Updates
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JTable summaryTable;
    private DefaultTableModel summaryTableModel;
    private JComboBox<String> studentSelector;
    private JComboBox<String> detailsStudentSelector;


    private JButton[] navButtons;
    private String currentCard = "Dashboard";

    public StudentGradeTrackerGUI() {
        this.gradeManager = new GradeManager();
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(cardLayout);

        initUI();
        loadInitialData();

        setTitle("Student Grade Tracker | Academic Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 850);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initUI() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(COLOR_BG);

        // Sidebar Navigation
        JPanel sidebar = createSidebar();
        mainContainer.add(sidebar, BorderLayout.WEST);

        // Content area initialization
        dashboardPanel = createDashboardPanel();
        studentsPanel = createStudentsPanel();
        addStudentPanel = createAddStudentPanel();
        addGradePanel = createAddGradePanel();
        detailsPanel = createDetailsPanel();
        summaryPanel = createSummaryPanel();

        contentPanel.add(dashboardPanel, "Dashboard");
        contentPanel.add(studentsPanel, "Students");
        contentPanel.add(addStudentPanel, "AddStudent");
        contentPanel.add(addGradePanel, "AddGrade");
        contentPanel.add(detailsPanel, "Details");
        contentPanel.add(summaryPanel, "Summary");

        mainContainer.add(contentPanel, BorderLayout.CENTER);
        add(mainContainer);

        updateNavState();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(260, 0));
        sidebar.setBorder(new javax.swing.border.EmptyBorder(30, 20, 30, 20));

        // Logo Area
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setOpaque(false);
        logoPanel.setMaximumSize(new Dimension(260, 100));

        JLabel appTitle = new JLabel("GradeTracker");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JLabel appSubtitle = new JLabel("Academic Management");
        appSubtitle.setForeground(new Color(150, 150, 150));
        appSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        GridBagConstraints gbcLogo = new GridBagConstraints();
        gbcLogo.gridx = 0; gbcLogo.gridy = 0; gbcLogo.anchor = GridBagConstraints.WEST;
        logoPanel.add(appTitle, gbcLogo);
        gbcLogo.gridy = 1;
        logoPanel.add(appSubtitle, gbcLogo);

        sidebar.add(logoPanel);
        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Navigation Items
        navButtons = new JButton[6];
        navButtons[0] = createNavButton("Dashboard", "Dashboard");
        navButtons[1] = createNavButton("Students", "Students");
        navButtons[2] = createNavButton("Add Student", "AddStudent");
        navButtons[3] = createNavButton("Add Grade", "AddGrade");
        navButtons[4] = createNavButton("Student Details", "Details");
        navButtons[5] = createNavButton("Summary Report", "Summary");

        for (JButton btn : navButtons) {
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        sidebar.add(Box.createVerticalGlue());

        // Bottom Actions
        JButton btnSave = new ModernButton("Save Data", COLOR_ACCENT, Color.WHITE);
        btnSave.addActionListener(e -> handleSaveData());
        sidebar.add(btnSave);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnLoad = new ModernButton("Load Data", new Color(60, 63, 65), Color.WHITE);
        btnLoad.addActionListener(e -> handleLoadData());
        sidebar.add(btnLoad);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnExit = new ModernButton("Exit Application", COLOR_DANGER, Color.WHITE);
        btnExit.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        sidebar.add(btnExit);

        return sidebar;
    }

    private void updateNavState() {
        if (navButtons == null) return;
        for (JButton btn : navButtons) {
            boolean isActive = btn.getText().equals(currentCard.substring(0, 1).toUpperCase() + currentCard.substring(1).toLowerCase())
                               || (currentCard.equals("AddStudent") && btn.getText().equals("Add Student"))
                               || (currentCard.equals("AddGrade") && btn.getText().equals("Add Grade"))
                               || (currentCard.equals("Summary") && btn.getText().equals("Summary Report"))
                               || (currentCard.equals("Details") && btn.getText().equals("Student Details"));

            if (isActive) {
                btn.setBackground(COLOR_NAV_ACTIVE_BG);
                btn.setForeground(Color.WHITE);
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            } else {
                btn.setBackground(COLOR_SIDEBAR);
                btn.setForeground(new Color(180, 180, 180));
                btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
        }
    }

    private JButton createNavButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 40));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> {
            currentCard = cardName;
            cardLayout.show(contentPanel, cardName);
            updateDynamicContent();
            updateNavState();
        });
        return btn;
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new javax.swing.border.EmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel title = new JLabel("Good evening,");
        title.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        title.setForeground(COLOR_TEXT_MUTED);
        JLabel subtitle = new JLabel("Student Academic Overview");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        subtitle.setForeground(COLOR_TEXT_DARK);
        header.add(title);
        header.add(subtitle);
        panel.add(header, BorderLayout.NORTH);

        // Stats Grid
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);
        centerContainer.setBorder(new javax.swing.border.EmptyBorder(30, 0, 0, 0));

        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 25, 25));
        statsGrid.setBackground(COLOR_BG);

        statsGrid.add(createStatCard("Total Students", "0", "totalStudents", "Current enrollment"));
        statsGrid.add(createStatCard("Total Grades", "0", "totalGrades", "All recorded entries"));
        statsGrid.add(createStatCard("Overall Average", "0.00", "overallAvg", "Across all students"));
        statsGrid.add(createStatCard("Highest Grade", "0.00", "topGrade", "Top performance"));
        statsGrid.add(createStatCard("Lowest Grade", "0.00", "bottomGrade", "Minimum performance"));

        // Replace empty card with Grade Distribution / Stats Card
        statsGrid.add(createStatCard("System Status", "Active", "sysStatus", "Local storage active"));

        centerContainer.add(statsGrid, BorderLayout.NORTH);

        // Performance Overview Section
        JPanel performancePanel = new RoundedPanel(COLOR_CARD_BG, 15);
        performancePanel.setLayout(new BorderLayout());
        performancePanel.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        JLabel perfTitle = new JLabel("Top Performers");
        perfTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        perfTitle.setForeground(COLOR_TEXT_DARK);
        performancePanel.add(perfTitle, BorderLayout.NORTH);

        String[] cols = {"Student ID", "Name", "Average"};
        DefaultTableModel perfModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable perfTable = new JTable(perfModel);
        styleTable(perfTable);

        // Constraint table height so it doesn't take over the screen
        JScrollPane scrollPane = new JScrollPane(perfTable);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        performancePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPadding = new JPanel();
        bottomPadding.setOpaque(false);
        bottomPadding.setBorder(new javax.swing.border.EmptyBorder(30, 0, 0, 0));

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(bottomPadding, BorderLayout.NORTH);
        footer.add(performancePanel, BorderLayout.CENTER);

        centerContainer.add(footer, BorderLayout.CENTER);
        panel.add(centerContainer, BorderLayout.CENTER);

        this.dashboardPerformanceTable = perfTable;
        this.dashboardPerformanceModel = perfModel;

        return panel;
    }

    private JTable dashboardPerformanceTable;
    private DefaultTableModel dashboardPerformanceModel;

    private JPanel createStatCard(String label, String value, String refName, String desc) {
        RoundedPanel card = new RoundedPanel(COLOR_CARD_BG, 15);
        card.setLayout(new BorderLayout());
        card.setBorder(new javax.swing.border.EmptyBorder(20, 20, 20, 20));

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLabel.setForeground(COLOR_TEXT_MUTED);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblValue.setForeground(COLOR_TEXT_DARK);
        lblValue.setName(refName);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(COLOR_TEXT_MUTED);

        JPanel textContainer = new JPanel(new GridLayout(3, 1));
        textContainer.setOpaque(false);
        textContainer.add(lblLabel);
        textContainer.add(lblValue);
        textContainer.add(lblDesc);

        card.add(textContainer, BorderLayout.CENTER);
        return card;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new javax.swing.border.EmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Students");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT_DARK);
        JLabel subtitle = new JLabel("Manage and review student academic records");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(COLOR_TEXT_MUTED);

        JPanel headerText = new JPanel(new GridLayout(2, 1));
        headerText.setOpaque(false);
        headerText.add(title);
        headerText.add(subtitle);
        header.add(headerText, BorderLayout.WEST);

        // Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchBar.setOpaque(false);
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnSearch = new ModernButton("Search", COLOR_ACCENT, Color.WHITE);
        btnSearch.addActionListener(e -> filterStudents(searchField.getText().trim()));

        searchBar.add(new JLabel("Search ID/Name: "));
        searchBar.add(searchField);
        searchBar.add(btnSearch);
        header.add(searchBar, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Grades Count", "Average"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        styleTable(studentTable);

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Actions
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setOpaque(false);
        JButton btnDetails = new ModernButton("View Details", COLOR_ACCENT, Color.WHITE);
        JButton btnRemove = new ModernButton("Remove Student", COLOR_DANGER, Color.WHITE);

        btnDetails.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                String id = (String) studentTableModel.getValueAt(row, 0);
                showStudentDetails(id);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRemove.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row != -1) {
                String id = (String) studentTableModel.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove student " + id + "?", "Confirm Removal", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    gradeManager.removeStudent(id);
                    refreshStudentsTable();
                    updateDynamicContent();
                    JOptionPane.showMessageDialog(this, "Student removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a student.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomBar.add(btnDetails);
        bottomBar.add(btnRemove);
        panel.add(bottomBar, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddStudentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        RoundedPanel form = new RoundedPanel(COLOR_CARD_BG, 20);
        form.setLayout(new BorderLayout());
        form.setPreferredSize(new Dimension(500, 350));
        form.setBorder(new javax.swing.border.EmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel title = new JLabel("Add New Student");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Create a student record in the system");
        subtitle.setForeground(COLOR_TEXT_MUTED);
        header.add(title);
        header.add(subtitle);
        form.add(header, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        fields.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        JTextField idField = new JTextField(20);
        idField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fields.add(idField, gbc);

        gbc.gridx = 0; gbc.gridy++;
        fields.add(new JLabel("Student Name:"), gbc);
        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fields.add(nameField, gbc);

        form.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton btnAdd = new ModernButton("Add Student", COLOR_ACCENT, Color.WHITE);
        JButton btnClear = new ModernButton("Clear", new Color(220, 220, 220), COLOR_TEXT_DARK);

        btnAdd.addActionListener(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both ID and Name are required.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                gradeManager.addStudent(id, name);
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                idField.setText("");
                nameField.setText("");
                updateDynamicContent();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClear.addActionListener(e -> {
            idField.setText("");
            nameField.setText("");
        });

        buttons.add(btnClear);
        buttons.add(btnAdd);
        form.add(buttons, BorderLayout.SOUTH);

        panel.add(form);
        return panel;
    }

    private JPanel createAddGradePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_BG);

        RoundedPanel form = new RoundedPanel(COLOR_CARD_BG, 20);
        form.setLayout(new BorderLayout());
        form.setPreferredSize(new Dimension(500, 350));
        form.setBorder(new javax.swing.border.EmptyBorder(30, 30, 30, 30));

        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel title = new JLabel("Add Grade");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        JLabel subtitle = new JLabel("Assign a grade to an existing student");
        subtitle.setForeground(COLOR_TEXT_MUTED);
        header.add(title);
        header.add(subtitle);
        form.add(header, BorderLayout.NORTH);

        JPanel fields = new JPanel(new GridBagLayout());
        fields.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        fields.add(new JLabel("Select Student:"), gbc);
        gbc.gridx = 1;
        studentSelector = new JComboBox<>();
        fields.add(studentSelector, gbc);

        gbc.gridx = 0; gbc.gridy++;
        fields.add(new JLabel("Grade (0-100):"), gbc);
        gbc.gridx = 1;
        JTextField gradeField = new JTextField(20);
        gradeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fields.add(gradeField, gbc);

        gbc.gridx = 1; gbc.gridy++;
        JLabel helper = new JLabel("Enter a grade between 0 and 100");
        helper.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        helper.setForeground(COLOR_TEXT_MUTED);
        fields.add(helper, gbc);

        form.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        JButton btnAdd = new ModernButton("Add Grade", COLOR_ACCENT, Color.WHITE);

        btnAdd.addActionListener(e -> {
            String id = getStudentIdFromSelection((String) studentSelector.getSelectedItem());
            String gStr = gradeField.getText().trim();
            if (id == null || gStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select a student and enter a grade.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                double grade = Double.parseDouble(gStr);
                gradeManager.addGrade(id, grade);
                JOptionPane.showMessageDialog(this, "Grade recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                gradeField.setText("");
                updateDynamicContent();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric grade.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttons.add(btnAdd);
        form.add(buttons, BorderLayout.SOUTH);

        panel.add(form);
        return panel;
    }

    private JPanel createDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_BG);
        panel.setBorder(new javax.swing.border.EmptyBorder(40, 40, 40, 40));

        // Top Section: Title and Selector
        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setOpaque(false);

        JLabel title = new JLabel("Student Profile");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT_DARK);

        JPanel selectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        selectionPanel.setOpaque(false);
        JLabel selectLabel = new JLabel("Select Student: ");
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        selectLabel.setForeground(COLOR_TEXT_DARK);

        detailsStudentSelector = new JComboBox<>();
        detailsStudentSelector.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        detailsStudentSelector.addActionListener(e -> updateDetailsView());

        selectionPanel.add(selectLabel);
        selectionPanel.add(detailsStudentSelector);

        JPanel headerContainer = new JPanel(new GridLayout(2, 1));
        headerContainer.setOpaque(false);
        headerContainer.add(title);
        headerContainer.add(selectionPanel);
        topPanel.add(headerContainer, BorderLayout.NORTH);

        panel.add(topPanel, BorderLayout.NORTH);

        // Content Area
        JPanel container = new JPanel(new BorderLayout(20, 20));
        container.setOpaque(false);
        container.setName("detailsContainer");
        panel.add(container, BorderLayout.CENTER);

        return panel;
    }

    private void updateDetailsView() {
        String selected = (String) detailsStudentSelector.getSelectedItem();
        JPanel container = (JPanel) findComponentInPanel(detailsPanel, "detailsContainer");
        if (container == null) return;

        container.removeAll();
        if (selected == null || selected.isEmpty()) {
            JLabel emptyMsg = new JLabel("No students available. Add a student to view details.");
            emptyMsg.setHorizontalAlignment(JLabel.CENTER);
            emptyMsg.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyMsg.setForeground(COLOR_TEXT_MUTED);
            container.add(emptyMsg);
        } else {
            // Extract ID from "ID - Name" format
            String id = selected.split(" - ")[0];
            renderStudentDetails(id);
        }
        container.revalidate();
        container.repaint();
    }

    private void renderStudentDetails(String id) {
        Optional<Student> opt = gradeManager.findStudent(id);
        JPanel container = (JPanel) findComponentInPanel(detailsPanel, "detailsContainer");
        if (container == null) return;

        if (opt.isPresent()) {
            Student s = opt.get();

            // Profile Header Card
            RoundedPanel profileCard = new RoundedPanel(COLOR_CARD_BG, 15);
            profileCard.setLayout(new BorderLayout());
            profileCard.setBorder(new javax.swing.border.EmptyBorder(25, 25, 25, 25));

            JPanel profileInfo = new JPanel(new GridLayout(2, 3, 20, 20));
            profileInfo.setOpaque(false);
            profileInfo.add(createDetailField("Student ID", s.getStudentId()));
            profileInfo.add(createDetailField("Name", s.getName()));

            ArrayList<Double> grades = s.getGrades();
            String avg = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.calculateAverage(grades));
            String high = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.findHighest(grades));
            String low = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.findLowest(grades));

            profileInfo.add(createDetailField("Total Grades", String.valueOf(grades.size())));
            profileInfo.add(createDetailField("Average", avg));
            profileInfo.add(createDetailField("Highest", high));
            profileInfo.add(createDetailField("Lowest", low));

            profileCard.add(profileInfo, BorderLayout.CENTER);

            // Grade History
            JPanel historyPanel = new JPanel(new BorderLayout());
            historyPanel.setOpaque(false);
            historyPanel.setBorder(new javax.swing.border.EmptyBorder(30, 0, 0, 0));

            JLabel historyTitle = new JLabel("Grade History");
            historyTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            historyTitle.setForeground(COLOR_TEXT_DARK);
            historyPanel.add(historyTitle, BorderLayout.NORTH);

            if (grades.isEmpty()) {
                JLabel noGradesMsg = new JLabel("No grades recorded for this student.");
                noGradesMsg.setHorizontalAlignment(JLabel.CENTER);
                noGradesMsg.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                noGradesMsg.setForeground(COLOR_TEXT_MUTED);
                historyPanel.add(noGradesMsg, BorderLayout.CENTER);
            } else {
                String[] cols = {"Grade Index", "Score"};
                DefaultTableModel historyModel = new DefaultTableModel(cols, 0) {
                    @Override public boolean isCellEditable(int r, int c) { return false; }
                };
                JTable historyTable = new JTable(historyModel);
                styleTable(historyTable);
                for (int i = 0; i < grades.size(); i++) {
                    historyModel.addRow(new Object[]{i + 1, grades.get(i)});
                }

                JScrollPane scrollPane = new JScrollPane(historyTable);
                scrollPane.setPreferredSize(new Dimension(0, 300));
                historyPanel.add(scrollPane, BorderLayout.CENTER);
            }

            // Action Bar
            JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
            actionBar.setOpaque(false);
            JButton btnBack = new ModernButton("Back to Students", COLOR_ACCENT, Color.WHITE);
            btnBack.addActionListener(e -> {
                cardLayout.show(contentPanel, "Students");
                updateNavState();
            });
            actionBar.add(btnBack);

            JPanel detailsContent = new JPanel(new BorderLayout(20, 20));
            detailsContent.setOpaque(false);
            detailsContent.add(profileCard, BorderLayout.NORTH);
            detailsContent.add(historyPanel, BorderLayout.CENTER);
            detailsContent.add(actionBar, BorderLayout.SOUTH);

            container.add(detailsContent);
        } else {
            JLabel emptyMsg = new JLabel("Student not found. Please select a valid student from the list.");
            emptyMsg.setHorizontalAlignment(JLabel.CENTER);
            emptyMsg.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            emptyMsg.setForeground(COLOR_TEXT_MUTED);
            container.add(emptyMsg);
        }
    }

    private void showStudentDetails(String id) {
        // Update selector and switch view
        for (int i = 0; i < detailsStudentSelector.getItemCount(); i++) {
            if (detailsStudentSelector.getItemAt(i).startsWith(id + " - ")) {
                detailsStudentSelector.setSelectedIndex(i);
                break;
            }
        }
        updateDetailsView();
        cardLayout.show(contentPanel, "Details");
    }

    private JPanel createDetailField(String label, String value) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(COLOR_TEXT_MUTED);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 16));
        val.setForeground(COLOR_TEXT_DARK);
        p.add(lbl);
        p.add(val);
        return p;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(COLOR_BG);
        panel.setBorder(new javax.swing.border.EmptyBorder(40, 40, 40, 40));

        // Header
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setOpaque(false);
        JLabel title = new JLabel("Academic Summary");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(COLOR_TEXT_DARK);
        JLabel subtitle = new JLabel("Performance overview across all students");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(COLOR_TEXT_MUTED);
        header.add(title);
        header.add(subtitle);
        panel.add(header, BorderLayout.NORTH);

        // Global Stats
        JPanel statsBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 20));
        statsBar.setOpaque(false);

        JLabel totalStudents = new JLabel("Total Students: 0");
        JLabel totalGrades = new JLabel("Total Grades: 0");
        JLabel overallAvg = new JLabel("Overall Average: 0.00");

        totalStudents.setName("summaryTotalStudents");
        totalGrades.setName("summaryTotalGrades");
        overallAvg.setName("summaryOverallAvg");

        for (JLabel l : new JLabel[]{totalStudents, totalGrades, overallAvg}) {
            l.setFont(new Font("Segoe UI", Font.BOLD, 14));
            l.setForeground(COLOR_TEXT_DARK);
            statsBar.add(l);
        }

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setOpaque(false);
        topContainer.add(statsBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Grade Count", "Average", "Highest", "Lowest"};
        summaryTableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        summaryTable = new JTable(summaryTableModel);
        styleTable(summaryTable);

        JScrollPane scrollPane = new JScrollPane(summaryTable);
        scrollPane.setPreferredSize(new Dimension(0, 400));
        topContainer.add(scrollPane, BorderLayout.CENTER);

        // Bottom Action
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomBar.setOpaque(false);
        JButton btnRefresh = new ModernButton("Refresh Report", COLOR_ACCENT, Color.WHITE);
        btnRefresh.addActionListener(e -> refreshSummaryTable());
        bottomBar.add(btnRefresh);

        JPanel mainCenter = new JPanel(new BorderLayout());
        mainCenter.setOpaque(false);
        mainCenter.add(topContainer, BorderLayout.CENTER);
        mainCenter.add(bottomBar, BorderLayout.SOUTH);

        panel.add(mainCenter, BorderLayout.CENTER);

        return panel;
    }

    private void updateDynamicContent() {
        refreshDashboard();
        refreshStudentsTable();
        refreshSummaryTable();
        refreshStudentSelector();
        refreshDetailsStudentSelector();
        refreshDashboardPerformanceTable();
        if ("Details".equals(currentCard)) {
            updateDetailsView();
        }
    }

    private void refreshDashboard() {
        ArrayList<Student> students = gradeManager.getStudents();
        int totalStudents = students.size();
        int totalGrades = 0;
        ArrayList<Double> allGrades = new ArrayList<>();

        for (Student s : students) {
            totalGrades += s.getGrades().size();
            allGrades.addAll(s.getGrades());
        }

        double overallAvg = GradeStatistics.calculateAverage(allGrades);
        double topGrade = GradeStatistics.findHighest(allGrades);
        double bottomGrade = GradeStatistics.findLowest(allGrades);

        updateDashboardValue("totalStudents", String.valueOf(totalStudents));
        updateDashboardValue("totalGrades", String.valueOf(totalGrades));
        updateDashboardValue("overallAvg", String.format("%.2f", overallAvg));
        updateDashboardValue("topGrade", String.format("%.2f", topGrade));
        updateDashboardValue("bottomGrade", String.format("%.2f", bottomGrade));

        updateSummaryHeader(totalStudents, totalGrades, overallAvg);
    }

    private void updateSummaryHeader(int students, int grades, double avg) {
        findLabelRecursively(summaryPanel, "summaryTotalStudents", "Total Students: " + students);
        findLabelRecursively(summaryPanel, "summaryTotalGrades", "Total Grades: " + grades);
        findLabelRecursively(summaryPanel, "summaryOverallAvg", String.format("Overall Average: %.2f", avg));
    }

    private void updateDashboardValue(String refName, String value) {
        findLabelRecursively(dashboardPanel, refName, value);
    }

    private void findLabelRecursively(Container container, String name, String value) {
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel && name.equals(c.getName())) {
                ((JLabel) c).setText(value);
                return;
            }
            if (c instanceof Container) {
                findLabelRecursively((Container) c, name, value);
            }
        }
    }

    private void refreshStudentsTable() {
        studentTableModel.setRowCount(0);
        for (Student s : gradeManager.getStudents()) {
            double avg = GradeStatistics.calculateAverage(s.getGrades());
            String avgStr = s.getGrades().isEmpty() ? "N/A" : String.format("%.2f", avg);
            studentTableModel.addRow(new Object[]{
                s.getStudentId(), s.getName(), s.getGrades().size(), avgStr
            });
        }
    }

    private void filterStudents(String query) {
        studentTableModel.setRowCount(0);
        for (Student s : gradeManager.getStudents()) {
            if (s.getStudentId().toLowerCase().contains(query.toLowerCase()) ||
                s.getName().toLowerCase().contains(query.toLowerCase())) {
                double avg = GradeStatistics.calculateAverage(s.getGrades());
                String avgStr = s.getGrades().isEmpty() ? "N/A" : String.format("%.2f", avg);
                studentTableModel.addRow(new Object[]{
                    s.getStudentId(), s.getName(), s.getGrades().size(), avgStr
                });
            }
        }
    }

    private void refreshSummaryTable() {
        summaryTableModel.setRowCount(0);
        for (Student s : gradeManager.getStudents()) {
            ArrayList<Double> grades = s.getGrades();
            String avg = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.calculateAverage(grades));
            String high = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.findHighest(grades));
            String low = grades.isEmpty() ? "N/A" : String.format("%.2f", GradeStatistics.findLowest(grades));
            summaryTableModel.addRow(new Object[]{
                s.getStudentId(), s.getName(), grades.size(), avg, high, low
            });
        }
    }

    private void refreshDashboardPerformanceTable() {
        if (dashboardPerformanceModel == null) return;
        dashboardPerformanceModel.setRowCount(0);

        ArrayList<Student> students = new ArrayList<>(gradeManager.getStudents());
        students.sort((s1, s2) -> Double.compare(
            GradeStatistics.calculateAverage(s2.getGrades()),
            GradeStatistics.calculateAverage(s1.getGrades())
        ));

        for (Student s : students) {
            double avg = GradeStatistics.calculateAverage(s.getGrades());
            dashboardPerformanceModel.addRow(new Object[]{
                s.getStudentId(), s.getName(), String.format("%.2f", avg)
            });
        }
    }

    private String getStudentIdFromSelection(String selected) {
        if (selected == null || selected.isEmpty()) return null;
        return selected.split(" - ")[0];
    }

    private void refreshStudentSelector() {
        studentSelector.removeAllItems();
        for (Student s : gradeManager.getStudents()) {
            studentSelector.addItem(s.getStudentId() + " - " + s.getName());
        }
    }

    private void refreshDetailsStudentSelector() {
        detailsStudentSelector.removeAllItems();
        for (Student s : gradeManager.getStudents()) {
            detailsStudentSelector.addItem(s.getStudentId() + " - " + s.getName());
        }
    }

    private void handleSaveData() {
        try {
            FileManager.saveData(gradeManager.getStudents());
            JOptionPane.showMessageDialog(this, "Data saved successfully to disk!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Save failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleLoadData() {
        try {
            ArrayList<Student> students = FileManager.loadData();
            gradeManager.setStudents(students);
            updateDynamicContent();
            JOptionPane.showMessageDialog(this, "Data loaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Load failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInitialData() {
        try {
            gradeManager.setStudents(FileManager.loadData());
            updateDynamicContent();
        } catch (Exception e) {
            // Silent ignore
        }
    }

    private Component findComponentInPanel(Container container, String name) {
        for (Component c : container.getComponents()) {
            if (name.equals(c.getName())) return c;
            if (c instanceof Container) {
                Component found = findComponentInPanel((Container) c, name);
                if (found != null) return found;
            }
        }
        return null;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(38);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(232, 242, 254));
        table.setSelectionForeground(COLOR_TEXT_DARK);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.WHITE);
        table.getTableHeader().setForeground(COLOR_TEXT_DARK);
        table.getTableHeader().setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 247, 249));
                }
                return c;
            }
        });
    }

    // --- Custom Components ---

    private static class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Color bgColor;

        public RoundedPanel(Color bgColor, int radius) {
            this.bgColor = bgColor;
            this.cornerRadius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(bgColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

            g2d.setColor(new Color(230, 230, 230));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);
        }
    }

    private static class ModernButton extends JButton {
        private Color baseColor;
        private Color foregroundColor;
        private Color hoverColor;

        public ModernButton(String text, Color baseColor, Color foregroundColor) {
            super(text);
            this.baseColor = baseColor;
            this.foregroundColor = foregroundColor;
            this.hoverColor = lightenColor(baseColor);

            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(true);
            setBackground(baseColor);
            setForeground(foregroundColor);
            setFont(new Font("Segoe UI", Font.BOLD, 13));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(baseColor);
                }
            });
        }

        private Color lightenColor(Color color) {
            float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
            hsb[2] = Math.min(1.0f, hsb[2] * 1.1f);
            return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(StudentGradeTrackerGUI::new);
    }
}
