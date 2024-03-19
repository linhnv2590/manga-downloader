package org.example;

import org.example.connectors.Connector;
import org.example.connectors.ConnectorFactory;
import org.example.utils.Constant;
import org.example.utils.JFrameUtils;
import org.example.utils.MangaUtils;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppFrame extends JFrame {
    private JPanel topLayoutPanel;
    private JPanel chapterPanel;
    private JScrollPane centerLayoutPanel;
    private JPanel bottomLayoutPanel;
    private JTextField urlField;
    private JTextField directoryField;
    private JButton getChapterButton;
    private JButton chooseDirectoryButton;
    private JButton downloadButton;
    private JCheckBox checkAllCheckBox;
    private JLabel sourceLabel;
    private JLabel mangaTitle;
    private transient Connector connector;
    private JCheckBox[] checkBoxes;
    private JTextArea logTextArea;
    private JLabel loadingIcon;
    private JComboBox<Integer> numberOfTaskDownloadOptions;

    public AppFrame() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Manga Downloader");
        setSize(1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        handleTopComponent();
        handleCenterComponent();
        handleBottomComponent();

        // Add components to panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(topLayoutPanel, BorderLayout.NORTH);
        panel.add(centerLayoutPanel, BorderLayout.CENTER);
        panel.add(bottomLayoutPanel, BorderLayout.SOUTH);

        // add action for components
        getChapterButtonAction();
        chooseDirectoryButtonAction();
        checkAllCheckboxAction();
        downloadButtonAction();

        add(panel);
    }

    private void handleTopComponent() {
        topLayoutPanel = new JPanel();
        topLayoutPanel.setLayout(new BoxLayout(topLayoutPanel, BoxLayout.Y_AXIS));
        JPanel getLinkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        getLinkPanel.setPreferredSize(new Dimension(getWidth(), 50));
        getLinkPanel.setBackground(Color.lightGray);

        getLinkPanel.add(new JLabel("URL:"));
        urlField = new JTextField(20);
        getLinkPanel.add(urlField);
        getChapterButton = new JButton("Get Chapters");
        getLinkPanel.add(getChapterButton);
        getLinkPanel.add(new JLabel("Save as:"));
        directoryField = new JTextField(MangaUtils.getDefaultDownloadDirectory(), 20);
        chooseDirectoryButton = new JButton("Choose Directory");
        getLinkPanel.add(directoryField);
        checkAllCheckBox = new JCheckBox("Check All");
        checkAllCheckBox.setVisible(false);
        getLinkPanel.add(chooseDirectoryButton);

        getLinkPanel.add(new JLabel("Task download:"));
        Integer[] options = Constant.NUMBER_OF_TASK_DOWNLOAD;
        numberOfTaskDownloadOptions = new JComboBox<>(options);
        getLinkPanel.add(numberOfTaskDownloadOptions);

        JPanel infoLinkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel sourceTitle = new JLabel("Source:");
        sourceLabel = new JLabel("");
        infoLinkPanel.add(sourceTitle);
        infoLinkPanel.add(sourceLabel);

        JPanel mangaTitlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel mangaLabel = new JLabel("Manga title: ");
        mangaTitle = new JLabel("");
        mangaTitlePanel.add(mangaLabel);
        mangaTitlePanel.add(mangaTitle);

        JPanel allCheckboxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        allCheckboxPanel.add(checkAllCheckBox);

        topLayoutPanel.add(getLinkPanel);
        topLayoutPanel.add(infoLinkPanel);
        topLayoutPanel.add(mangaTitlePanel);
        topLayoutPanel.add(allCheckboxPanel);
    }

    private void handleCenterComponent() {
        chapterPanel = new JPanel();
        centerLayoutPanel = new JScrollPane(chapterPanel);
    }

    private void handleBottomComponent() {
        bottomLayoutPanel = new JPanel();
        bottomLayoutPanel.setLayout(new BoxLayout(bottomLayoutPanel, BoxLayout.Y_AXIS));
        downloadButton = new JButton("Download");
        downloadButton.setEnabled(false);
        JPanel downloadPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        downloadPanel.add(downloadButton);

        JPanel logPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        logTextArea.setRows(10);
        logTextArea.setColumns(90);
        logPanel.add(new JScrollPane(logTextArea));

        bottomLayoutPanel.add(downloadPanel);
        bottomLayoutPanel.add(logPanel);
    }

    private void chooseDirectoryButtonAction() {
        chooseDirectoryButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                directoryField.setText(selectedFile.getAbsolutePath());
            }
        });
    }

    private void getChapterButtonAction() {
        getChapterButton.addActionListener(e -> {
            SwingWorker<Void, String> worker = fetchChaptersWorker();
            worker.execute();
        });
    }

    private void checkAllCheckboxAction() {
        checkAllCheckBox.addActionListener(e -> {
            boolean isSelected = checkAllCheckBox.isSelected();
            Component[] components = chapterPanel.getComponents();
            for (Component component : components) {
                if (component instanceof JCheckBox) {
                    ((JCheckBox) component).setSelected(isSelected);
                }
            }
        });
    }

    private void downloadButtonAction() {
        downloadButton.addActionListener(e -> {
            JFrameUtils.startDownloadChapter(getChapterButton, downloadButton, chooseDirectoryButton);
            Component[] components = chapterPanel.getComponents();
            if (components.length == 0) {
                SwingWorker<Void, String> stringSwingWorker = downloadSingleChapterWorker();
                stringSwingWorker.execute();
            } else {
                SwingWorker<Void, String> multipleWingWorker = downloadMultipleChaptersWorker(components);
                multipleWingWorker.execute();
            }
        });
    }

    private SwingWorker<Void, String> fetchChaptersWorker() {
        return new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                // Display loading icon
                chapterPanel.removeAll();
                chapterPanel.setLayout(new BorderLayout());
                loadingIcon = new JLabel(new ImageIcon("loading.gif"));
                JFrameUtils.startFetchChapter(getChapterButton, loadingIcon, downloadButton);
                chapterPanel.add(loadingIcon);
                chapterPanel.revalidate();
                chapterPanel.repaint();

                String url = urlField.getText();
                connector = ConnectorFactory.getConnector(url);
                if (connector == null) {
                    JFrameUtils.endFetchChapterFailed(getChapterButton, loadingIcon, downloadButton);
                    JOptionPane.showMessageDialog(null, "Error: Cannot support this site!", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }
                Document document = connector.connectToServer(url);
                if (document == null) {
                    JFrameUtils.endFetchChapterFailed(getChapterButton, loadingIcon, downloadButton);
                    JOptionPane.showMessageDialog(null, "Error: Cannot connect to server!", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                connector.setDocument(document);
                Manga manga = connector.fetchManga();
                List<Chapter> chapters = connector.fetchChapters();
                manga.setChapterList(chapters);
                connector.setManga(manga);
                if (!chapters.isEmpty()) {
                    mangaTitle.setText(manga.getTitle() + " (have " + manga.getChapterList().size() + " chapters)");
                } else {
                    mangaTitle.setText(manga.getTitle());
                }
                checkAllCheckBox.setVisible(!chapters.isEmpty());

                sourceLabel.setText(connector.getLabel());
                chapterPanel.removeAll();
                checkBoxes = new JCheckBox[chapters.size()];
                for (int i = 0; i < chapters.size(); i++) {
                    JCheckBox checkBox = JFrameUtils.settingCheckBox(chapters, i, checkBoxes);
                    checkBoxes[i] = checkBox;
                    chapterPanel.add(checkBox);
                }

                JFrameUtils.addShiftActionListener(chapterPanel);
                JFrameUtils.endFetchChapter(getChapterButton, loadingIcon, downloadButton);
                chapterPanel.setLayout(new BoxLayout(chapterPanel, BoxLayout.Y_AXIS));
                chapterPanel.revalidate();
                chapterPanel.repaint();

                return null;
            }
        };
    }

    private SwingWorker<Void, String> downloadMultipleChaptersWorker(Component[] components) {
        return new SwingWorker<Void, String>() {
            int tasksCompleted = 0;
            @Override
            protected Void doInBackground() {
                Object numberOfTask = numberOfTaskDownloadOptions.getSelectedItem();
                int numberOfThreads = (numberOfTask instanceof Integer) ? (int) numberOfTask : 1;
                ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

                for (int i = 0; i < components.length; i++) {
                    int finalI = i;
                    Runnable task = () -> {
                        Component component = components[finalI];
                        if (component instanceof JCheckBox) {
                            JCheckBox checkBox = (JCheckBox) component;
                            if (checkBox.isSelected()) {
                                Chapter selectedChapter = (Chapter) checkBox.getClientProperty(Constant.CHAPTER);
                                String checkBoxTitle = selectedChapter.getTitle();
                                publish("Downloading chapter: " + checkBoxTitle);
                                JFrameUtils.updateCheckboxContent(finalI, checkBoxTitle + " downloading...", chapterPanel);
                                connector.downloadFromURL(connector, selectedChapter, directoryField.getText(), logTextArea);
                                JFrameUtils.updateCheckboxContent(finalI, checkBoxTitle + " done", chapterPanel);
                            }
                            synchronized (this) {
                                tasksCompleted++;
                                if (tasksCompleted == components.length) {
                                    JOptionPane.showMessageDialog(null, Constant.DOWNLOAD_COMPLETED,
                                            Constant.DOWNLOAD_STATUS_TITLE, JOptionPane.INFORMATION_MESSAGE);
                                    JFrameUtils.endDownloadChapter(getChapterButton, downloadButton, chooseDirectoryButton);
                                    logTextArea.setText("");
                                }
                            }
                        }
                    };
                    executor.execute(task);
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    logTextArea.append(message + "\n");
                }
            }
        };
    }

    private SwingWorker<Void, String> downloadSingleChapterWorker() {
        return new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                publish("Downloading chapter: " + mangaTitle.getText());
                Chapter chapter = new Chapter(connector.getUrl(), "");
                connector.downloadFromURL(connector, chapter, directoryField.getText(), logTextArea);
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    logTextArea.append(message + "\n");
                }
            }

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null, Constant.DOWNLOAD_COMPLETED,
                        Constant.DOWNLOAD_STATUS_TITLE, JOptionPane.INFORMATION_MESSAGE);
                JFrameUtils.endDownloadChapter(getChapterButton, downloadButton, chooseDirectoryButton);
                logTextArea.setText("");
            }
        };
    }
}
