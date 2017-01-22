package edu.utd.s3.cszuo.methodmonitorui.UI;

import edu.utd.s3.cszuo.methodmonitorui.adb.ILogcatWatcher;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

/**
 * Created by cszuo on 1/8/17.
 */

public class RawLogCatPane extends JPanel implements ILogcatWatcher {
    JTextArea textArea;

    public RawLogCatPane() {
        this.setLayout(new BorderLayout(0, 0));
        textArea = new JTextArea();
        textArea.setEditable(false);
        this.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS), BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton clearb = new JButton("clear");
        clearb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.removeAll();
                textArea.setText("");
            }
        });
        toolBar.add(clearb);

        this.add(toolBar, BorderLayout.NORTH);

    }

    @Override
    public void newLine(final String line) {
        // SwingUtilities.invokeLater(new Runnable() {
        // @Override
        // public void run () {
        //     textArea.append(line + "\n");
        // }
        // }

        // );
    }
}
