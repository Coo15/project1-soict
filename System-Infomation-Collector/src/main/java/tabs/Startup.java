package tabs;

import startup.CronTab;
import javax.swing.*;
import java.awt.*;
import startup.Apps;

public class Startup extends JPanel {
    public Startup() {
        setLayout(new BorderLayout());
        JTabbedPane startupTabs = new JTabbedPane();
        
        String osName = System.getProperty("os.name").toLowerCase();
        
        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            startupTabs.addTab("Cron", new CronTab());
        } else {
            startupTabs.addTab("Task", new TaskSchedule());
        }
        startupTabs.addTab("App", new Apps());
        
        
        add(startupTabs, BorderLayout.CENTER);
    }
}
