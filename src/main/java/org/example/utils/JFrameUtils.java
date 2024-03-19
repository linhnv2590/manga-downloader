package org.example.utils;

import org.example.Chapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class JFrameUtils extends JFrame {
    private static JCheckBox firstSelectedCheckBox;
    private static JCheckBox lastSelectedCheckBox;
    private static boolean shiftPressed;

    public static void updateCheckboxContent(int index, String content, JPanel chapterPanel) {
        SwingUtilities.invokeLater(() -> {
            Component[] components = chapterPanel.getComponents();
            if (index >= 0 && index < components.length) {
                Component component = components[index];
                if (component instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) component;
                    checkBox.setText(content);
                }
            }
        });
    }

    public static void selectCheckBoxesBetween(JCheckBox[] checkBoxes, JCheckBox checkBox1, JCheckBox checkBox2) {
        if (checkBox1 == checkBox2) {
            return;
        }
        boolean between = false;
        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox == checkBox1 || checkBox == checkBox2) {
                between = !between;
            }
            if (between) {
                checkBox.setSelected(true);
            }
        }
    }

    public static void startFetchChapter(JButton getChapterButton, JLabel loadingIcon, JButton downloadButton) {
        getChapterButton.setEnabled(false);
        downloadButton.setEnabled(false);
        loadingIcon.setVisible(true);
    }

    public static void endFetchChapter(JButton getChapterButton, JLabel loadingIcon, JButton downloadButton) {
        getChapterButton.setEnabled(true);
        downloadButton.setEnabled(true);
        loadingIcon.setVisible(false);
    }

    public static void endFetchChapterFailed(JButton getChapterButton, JLabel loadingIcon, JButton downloadButton) {
        getChapterButton.setEnabled(true);
        downloadButton.setEnabled(false);
        loadingIcon.setVisible(false);
    }

    public static void startDownloadChapter(JButton getChapterButton, JButton downloadButton, JButton chooseDirectoryButton) {
        getChapterButton.setEnabled(false);
        downloadButton.setEnabled(false);
        chooseDirectoryButton.setEnabled(false);
    }

    public static void endDownloadChapter(JButton getChapterButton, JButton downloadButton, JButton chooseDirectoryButton) {
        getChapterButton.setEnabled(true);
        downloadButton.setEnabled(false);
        chooseDirectoryButton.setEnabled(true);
    }

    public static JCheckBox settingCheckBox(List<Chapter> chapters, int i, JCheckBox[] checkBoxes) {
        Chapter chapter = chapters.get(i);
        JCheckBox checkBox = new JCheckBox(chapter.getTitle());
        checkBox.putClientProperty(Constant.CHAPTER, chapter);
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED && (e.getSource() instanceof JCheckBox)) {
                JCheckBox selectedCheckBox = (JCheckBox) e.getSource();
                if (shiftPressed) {
                    if (firstSelectedCheckBox == null) {
                        firstSelectedCheckBox = selectedCheckBox;
                    }
                    lastSelectedCheckBox = selectedCheckBox;
                    JFrameUtils.selectCheckBoxesBetween(checkBoxes, firstSelectedCheckBox, lastSelectedCheckBox);
                } else {
                    firstSelectedCheckBox = selectedCheckBox;
                }

            }
        });
        return checkBox;
    }

    public static void addShiftActionListener(JPanel chapterPanel) {
        for (Component component : chapterPanel.getComponents()) {
            component.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        shiftPressed = true;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                        shiftPressed = false;
                    }
                }
            });
        }
    }
}
