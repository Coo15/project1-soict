package startup;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.text.ParseException;
import net.redhogs.cronparser.*;

public class CronTab extends JPanel {

    private JTable startupTable;
    private DefaultTableModel tableModel;

    public CronTab() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Schedule", "User", "Command"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        startupTable = new JTable(tableModel);
        startupTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        startupTable.setCellSelectionEnabled(true);

        JScrollPane scrollPane = new JScrollPane(startupTable);
        add(scrollPane, BorderLayout.CENTER);

        fetchCrontabEntries();
    }

    private void fetchCrontabEntries() {
        try {
            Process process = Runtime.getRuntime().exec("cat /etc/crontab");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                
                String[] parts = line.split("\\s+");
                if (parts.length < 6) {
                    continue;
                }

                String schedule = String.join(" ", parts[0], parts[1], parts[2], parts[3], parts[4]);
                String formattedSchedule = CronExpressionDescriptor.getDescription(schedule);
                String user = parts[5];

                StringBuilder commandBuilder = new StringBuilder();
                for (int i = 6; i < parts.length; i++) {
                    commandBuilder.append(parts[i]).append(" ");
                }
                String command = commandBuilder.toString().trim();


                tableModel.addRow(new Object[]{formattedSchedule, user, command});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}
