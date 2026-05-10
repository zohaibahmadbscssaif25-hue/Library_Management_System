package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;

import db.ConnectionDb;
import dao.Book;
import dao.Member;
import dao.Borrowed;
import dao.Fine;
import dao.Reports;

public class main extends JFrame {

    // ─── DAO objects ───────────────────────────────────────────────────
    private Book     book     = new Book();
    private Member   member   = new Member();
    private Borrowed borrowed = new Borrowed();
    private Fine     fine     = new Fine();
    private Reports  reports  = new Reports();

    // ─── Colors ────────────────────────────────────────────────────────
    private static final Color BG        = new Color(240, 244, 248);
    private static final Color SIDEBAR   = new Color(30,  60,  114);
    private static final Color ACCENT    = new Color(52, 152, 219);
    private static final Color BTN_TEXT  = Color.WHITE;
    private static final Color PANEL_BG  = Color.WHITE;

    // ─── Main content area ────────────────────────────────────────────
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // ────────────────────────────────────────────────────────────────────
    public main() {
        setTitle("Library Management System");
        setSize(900, 620);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // ── Top bar ──────────────────────────────────────────────────
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        topBar.setBackground(SIDEBAR);
        JLabel title = new JLabel("📚  Library Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        topBar.add(title);
        add(topBar, BorderLayout.NORTH);

        // ── Sidebar ──────────────────────────────────────────────────
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR);
        sidebar.setPreferredSize(new Dimension(175, 0));
        sidebar.setBorder(new EmptyBorder(10, 5, 10, 5));

        addSection(sidebar, "📖 BOOKS");
        addSideBtn(sidebar, "All Books",       "allBooks");
        addSideBtn(sidebar, "Search by Name",  "searchName");
        addSideBtn(sidebar, "Search by ISBN",  "searchISBN");
        addSideBtn(sidebar, "Available Books", "available");
        addSideBtn(sidebar, "Add Book",        "addBook");
        addSideBtn(sidebar, "Update Copies",   "updateCopies");
        addSideBtn(sidebar, "Delete Book",     "deleteBook");

        addSection(sidebar, "👤 MEMBERS");
        addSideBtn(sidebar, "All Members",     "allMembers");
        addSideBtn(sidebar, "Add Member",      "addMember");
        addSideBtn(sidebar, "Update Status",   "updateMember");

        addSection(sidebar, "📦 BORROWED");
        addSideBtn(sidebar, "Issue Book",      "issueBook");
        addSideBtn(sidebar, "Return Book",     "returnBook");
        addSideBtn(sidebar, "All Borrowed",    "allBorrowed");
        addSideBtn(sidebar, "Overdue Books",   "overdueBooks");
        addSideBtn(sidebar, "Borrow History",  "borrowHistory");

        addSection(sidebar, "💰 FINES");
        addSideBtn(sidebar, "Unpaid Fines",    "unpaidFines");
        addSideBtn(sidebar, "Calculate Fine",  "calcFine");
        addSideBtn(sidebar, "Mark Fine Paid",  "markPaid");

        addSection(sidebar, "📊 REPORTS");
        addSideBtn(sidebar, "Books by Genre",  "byGenre");
        addSideBtn(sidebar, "Total Fines",     "totalFines");
        addSideBtn(sidebar, "Members by Type", "memberType");

        JScrollPane sideScroll = new JScrollPane(sidebar);
        sideScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sideScroll.setBorder(null);
        add(sideScroll, BorderLayout.WEST);

        // ── Content panel (CardLayout) ───────────────────────────────
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG);
        contentPanel.add(makeWelcome(),          "welcome");
        contentPanel.add(makeSimpleAction("allBooks",    "Get All Books",        this::doAllBooks),        "allBooks");
        contentPanel.add(makeInput1("searchName",  "Search Book by Name",   "Book Name:",     this::doSearchName),   "searchName");
        contentPanel.add(makeInput1("searchISBN",  "Search Book by ISBN",   "ISBN:",          this::doSearchISBN),   "searchISBN");
        contentPanel.add(makeSimpleAction("available",   "Available Books",       this::doAvailable),       "available");
        contentPanel.add(makeAddBook(),          "addBook");
        contentPanel.add(makeInput2("updateCopies","Update Book Copies","Book ID:","New Copies:", this::doUpdateCopies),"updateCopies");
        contentPanel.add(makeInput1("deleteBook",  "Delete Book",           "Book ID:",       this::doDeleteBook),   "deleteBook");
        contentPanel.add(makeSimpleAction("allMembers",  "All Members",           this::doAllMembers),      "allMembers");
        contentPanel.add(makeAddMember(),        "addMember");
        contentPanel.add(makeInput2("updateMember","Update Member Status","Member ID:","New Status (ACTIVE/INACTIVE/BANNED):", this::doUpdateMember),"updateMember");
        contentPanel.add(makeIssueBook(),        "issueBook");
        contentPanel.add(makeInput1("returnBook",  "Return Book",           "Borrow ID:",     this::doReturnBook),   "returnBook");
        contentPanel.add(makeSimpleAction("allBorrowed", "All Borrowed Books",    this::doAllBorrowed),     "allBorrowed");
        contentPanel.add(makeSimpleAction("overdueBooks","Overdue Books",         this::doOverdue),         "overdueBooks");
        contentPanel.add(makeInput1("borrowHistory","Member Borrow History","Member ID:",     this::doBorrowHistory),"borrowHistory");
        contentPanel.add(makeSimpleAction("unpaidFines", "Unpaid Fines",          this::doUnpaidFines),     "unpaidFines");
        contentPanel.add(makeSimpleAction("calcFine",    "Calculate Fines",       this::doCalcFine),        "calcFine");
        contentPanel.add(makeInput1("markPaid",    "Mark Fine Paid",         "Fine ID:",       this::doMarkPaid),    "markPaid");
        contentPanel.add(makeSimpleAction("byGenre",     "Books by Genre",         this::doByGenre),         "byGenre");
        contentPanel.add(makeSimpleAction("totalFines",  "Total Fines Report",    this::doTotalFines),      "totalFines");
        contentPanel.add(makeSimpleAction("memberType",  "Members by Type",       this::doMemberType),      "memberType");

        add(contentPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    // ─── Sidebar helpers ───────────────────────────────────────────────
    private void addSection(JPanel p, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(180, 200, 230));
        lbl.setBorder(new EmptyBorder(12, 8, 3, 0));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(lbl);
    }

    private void addSideBtn(JPanel p, String label, String card) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(ACCENT); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(SIDEBAR); }
        });
        btn.addActionListener(e -> cardLayout.show(contentPanel, card));
        p.add(btn);
        p.add(Box.createVerticalStrut(2));
    }

    // ─── Panel builders ────────────────────────────────────────────────
    private JPanel makeWelcome() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(BG);
        JLabel lbl = new JLabel("<html><center><h1>📚 Welcome to LMS</h1><p>Select an option from the left menu.</p></center></html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        p.add(lbl);
        return p;
    }

    private JPanel makeSimpleAction(String id, String title, Runnable action) {
        JPanel p = makeBasePanel(title);
        JButton run = makeRunBtn("Run");
        redirectOutput(p);
        run.addActionListener(e -> {
            activateOutput(p);
            action.run();
        });
        getFormPanel(p).add(run, makeGbc(0, 0, 2));
        return p;
    }

    private JPanel makeInput1(String id, String title, String label, Consumer1 action) {
        JPanel p = makeBasePanel(title);
        JPanel form = getFormPanel(p);
        JTextField tf = addField(form, label, 0);
        JButton run = makeRunBtn("Run");
        redirectOutput(p);
        run.addActionListener(e -> {
            activateOutput(p);
            action.accept(tf.getText().trim());
        });
        form.add(run, makeGbc(0, 1, 2));
        return p;
    }

    private JPanel makeInput2(String id, String title, String l1, String l2, Consumer2 action) {
        JPanel p = makeBasePanel(title);
        JPanel form = getFormPanel(p);
        JTextField tf1 = addField(form, l1, 0);
        JTextField tf2 = addField(form, l2, 1);
        JButton run = makeRunBtn("Run");
        redirectOutput(p);
        run.addActionListener(e -> {
            activateOutput(p);
            action.accept(tf1.getText().trim(), tf2.getText().trim());
        });
        form.add(run, makeGbc(0, 2, 2));
        return p;
    }

    private JPanel makeAddBook() {
        JPanel p = makeBasePanel("Add New Book");
        JPanel form = getFormPanel(p);
        JTextField tISBN   = addField(form, "ISBN:",            0);
        JTextField tTitle  = addField(form, "Title:",           1);
        JTextField tAuthor = addField(form, "Author:",          2);
        JTextField tGenre  = addField(form, "Genre:",           3);
        JTextField tYear   = addField(form, "Publish Year:",    4);
        JTextField tAvail  = addField(form, "Available Copies:",5);
        JTextField tTotal  = addField(form, "Total Copies:",    6);
        JButton run = makeRunBtn("Add Book");
        redirectOutput(p);
        run.addActionListener(e -> {
            activateOutput(p);
            try {
                book.AddBooks(tISBN.getText(), tTitle.getText(), tAuthor.getText(),
                        tGenre.getText(), Integer.parseInt(tYear.getText()),
                        Integer.parseInt(tAvail.getText()), Integer.parseInt(tTotal.getText()));
            } catch (NumberFormatException ex) {
                System.out.println("⚠ Year and copies must be numbers.");
            }
        });
        form.add(run, makeGbc(0, 7, 2));
        return p;
    }

    private JPanel makeAddMember() {
        JPanel p = makeBasePanel("Add New Member");
        JPanel form = getFormPanel(p);
        JTextField tID     = addField(form, "Member ID:",   0);
        JTextField tName   = addField(form, "Full Name:",   1);
        JTextField tEmail  = addField(form, "Email:",       2);
        JTextField tType   = addField(form, "Type (STUDENT/TEACHER/STAFF):", 3);
        JTextField tStatus = addField(form, "Status (ACTIVE/INACTIVE):",     4);
        JButton run = makeRunBtn("Add Member");
        redirectOutput(p);
        run.addActionListener(e -> {
            activateOutput(p);
            try {
                member.AddMember(Integer.parseInt(tID.getText()), tName.getText(),
                        tEmail.getText(), tType.getText(), tStatus.getText());
            } catch (NumberFormatException ex) {
                System.out.println("⚠ Member ID must be a number.");
            }
        });
        form.add(run, makeGbc(0, 5, 2));
        return p;
    }

    private JPanel makeIssueBook() {
        JPanel p = makeBasePanel("Issue Book");
        JPanel form = getFormPanel(p);
        JTextField tBook   = addField(form, "Book ID:",      0);
        JTextField tMember = addField(form, "Member ID:",    1);
        JTextField tLib    = addField(form, "Librarian ID:", 2);
        JButton run = makeRunBtn("Issue Book");
        redirectOutput(p);
        run.addActionListener(e -> {
            activateOutput(p);
            try {
                borrowed.issueBook(Integer.parseInt(tBook.getText()),
                        Integer.parseInt(tMember.getText()),
                        Integer.parseInt(tLib.getText()));
            } catch (NumberFormatException ex) {
                System.out.println("⚠ All IDs must be numbers.");
            }
        });
        form.add(run, makeGbc(0, 3, 2));
        return p;
    }

    // ─── DAO actions ───────────────────────────────────────────────────
    private void doAllBooks()     { book.getAllBooks(); }
    private void doSearchName(String n) { book.searchBookByName(n); }
    private void doSearchISBN(String s) { book.searchBookByISBN(s); }
    private void doAvailable()    { book.AvailableBooks(); }
    private void doUpdateCopies(String id, String copies) {
        try { book.UpdateBookcopies(Integer.parseInt(id), Integer.parseInt(copies)); }
        catch(NumberFormatException e){ System.out.println("⚠ IDs must be numbers."); }
    }
    private void doDeleteBook(String id) {
        int confirm = JOptionPane.showConfirmDialog(this, "Delete book ID " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION)
            try { book.DeleteBook(Integer.parseInt(id)); }
            catch(NumberFormatException e){ System.out.println("⚠ ID must be a number."); }
    }
    private void doAllMembers()   { member.GetAllMembers(); }
    private void doUpdateMember(String id, String status) {
        try { member.UpdateMemberStatus(status, Integer.parseInt(id)); }
        catch(NumberFormatException e){ System.out.println("⚠ ID must be a number."); }
    }
    private void doAllBorrowed()  { borrowed.getAllBorrowedBooks(); }
    private void doOverdue()      { borrowed.getOverdueBooks(); }
    private void doBorrowHistory(String id) {
        try { borrowed.getMemberBorrowHistory(Integer.parseInt(id)); }
        catch(NumberFormatException e){ System.out.println("⚠ ID must be a number."); }
    }
    private void doReturnBook(String id) {
        try { borrowed.returnBook(Integer.parseInt(id)); }
        catch(NumberFormatException e){ System.out.println("⚠ ID must be a number."); }
    }
    private void doUnpaidFines()  { fine.getUnpaidFines(); }
    private void doCalcFine()     { fine.calculateFine(); }
    private void doMarkPaid(String id) {
        try { fine.markFinePaid(Integer.parseInt(id)); }
        catch(NumberFormatException e){ System.out.println("⚠ ID must be a number."); }
    }
    private void doByGenre()      { reports.getTotalBooksByGenre(); }
    private void doTotalFines()   { reports.getTotalFines(); }
    private void doMemberType()   { reports.getMemberCountByType(); }

    // ─── Output helpers ────────────────────────────────────────────────

    /**
     * Attaches a JTextArea (inside a JScrollPane) to the SOUTH of the card panel.
     */
    private void redirectOutput(JPanel p) {
        JTextArea textArea = new JTextArea(8, 40);
        textArea.setEditable(false);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textArea.setBackground(new Color(245, 247, 250));
        textArea.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 235)));
        scroll.setPreferredSize(new Dimension(0, 200));

        JPanel card = (JPanel) ((BorderLayout) p.getLayout())
                        .getLayoutComponent(BorderLayout.CENTER);
        card.add(scroll, BorderLayout.SOUTH);
    }

    /**
     * Before each action runs: point dao.Output at this panel's JTextArea
     * and clear it so each run starts fresh.
     */
    private void activateOutput(JPanel p) {
    JPanel card = (JPanel) ((BorderLayout) p.getLayout())
                    .getLayoutComponent(BorderLayout.CENTER);
    for (Component c : card.getComponents()) {
        if (c instanceof JScrollPane) {
            JScrollPane sp = (JScrollPane) c;
            if (sp.getViewport().getView() instanceof JTextArea) {
                JTextArea ta = (JTextArea) sp.getViewport().getView();
                dao.Output.setArea(ta);
                dao.Output.clear();
                dao.Output.println("✅ Output connected!"); // debug line
                return;
            }
        }
    }
    System.out.println("❌ activateOutput: JTextArea NOT found!"); // debug line
}

    // ─── Small UI helpers ──────────────────────────────────────────────
    private JPanel makeBasePanel(String title) {
        JPanel outer = new JPanel(new BorderLayout(10, 10));
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel h = new JLabel(title);
        h.setFont(new Font("Segoe UI", Font.BOLD, 18));
        h.setForeground(SIDEBAR);
        outer.add(h, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(PANEL_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 225, 235), 1, true),
                new EmptyBorder(16, 16, 16, 16)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(PANEL_BG);
        form.setName("form");
        card.add(form, BorderLayout.CENTER);
        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    private JPanel getFormPanel(JPanel outer) {
        JPanel card = (JPanel) ((BorderLayout) outer.getLayout()).getLayoutComponent(BorderLayout.CENTER);
        return (JPanel) card.getComponent(0);
    }

    private JTextField addField(JPanel form, String label, int row) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextField tf = new JTextField(20);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 210, 225), 1),
                new EmptyBorder(5, 8, 5, 8)));
        GridBagConstraints g1 = makeGbc(0, row, 1);
        g1.anchor = GridBagConstraints.EAST;
        g1.fill   = GridBagConstraints.NONE;
        GridBagConstraints g2 = makeGbc(1, row, 1);
        form.add(lbl, g1);
        form.add(tf,  g2);
        return tf;
    }

    private JButton makeRunBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(ACCENT);
        b.setForeground(BTN_TEXT);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(150, 36));
        return b;
    }

    private GridBagConstraints makeGbc(int col, int row, int span) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = col; g.gridy = row; g.gridwidth = span;
        g.fill  = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 6, 5, 6);
        g.weightx = (col == 1) ? 1.0 : 0;
        return g;
    }

    // ─── Functional interfaces (Java 8+) ──────────────────────────────
    @FunctionalInterface interface Consumer1 { void accept(String a); }
    @FunctionalInterface interface Consumer2 { void accept(String a, String b); }

    // ─── Entry point ──────────────────────────────────────────────────
    public static void main(String[] args) {
        Connection conn = ConnectionDb.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null, "❌ Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}
