package net.sourceforge.pmd.swingui;

import net.sourceforge.pmd.PMDException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *
 * @author Donald A. Leckie
 * @since September 8, 2002
 * @version $Revision$, $Date$
 */
class PreferencesEditor extends JDialog
{
    private JTextArea m_currentPathToPMD;
    private JTextArea m_userPathToPMD;
    private JTextArea m_sharedPathToPMD;

    /**
     ********************************************************************************
     *
     * @pmdViewer
     */
    protected PreferencesEditor(PMDViewer pmdViewer) throws PMDException
    {
        super(pmdViewer, "Preferences Editor", true);

        setSize(ComponentFactory.adjustWindowSize(800, 450));
        setLocationRelativeTo(pmdViewer);
        setResizable(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        getContentPane().add(createContentPanel());
    }

    /**
     ********************************************************************************
     *
     * @return
     */
    private JScrollPane createContentPanel() throws PMDException
    {
        JPanel contentPanel = new JPanel(new BorderLayout());
        EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
        contentPanel.setBorder(emptyBorder);
        contentPanel.add(createDataPanel(), BorderLayout.CENTER);
        contentPanel.add(createButtonPanel(), BorderLayout.SOUTH);

        return ComponentFactory.createScrollPane(contentPanel);
    }

    /**
     ********************************************************************************
     *
     * @return
     */
    private JPanel createDataPanel() throws PMDException
    {
        JPanel dataPanel;
        String directory;
        int row;
        Preferences preferences;

        dataPanel = new JPanel(new GridBagLayout());
        preferences = Preferences.getPreferences();

        row = 0;
        createLabel("Current Path to PMD", dataPanel, row, 0);
        directory = preferences.getCurrentPathToPMD();
        m_currentPathToPMD = createTextArea(directory, dataPanel, row, 1);
        createFileButton(dataPanel, row, 2, m_currentPathToPMD);

        row++;
        createLabel("User Path to PMD", dataPanel, row, 0);
        directory = preferences.getUserPathToPMD();
        m_userPathToPMD = createTextArea(directory, dataPanel, row, 1);
        createFileButton(dataPanel, row, 2, m_userPathToPMD);

        row++;
        createLabel("Shared Path to PMD", dataPanel, row, 0);
        directory = preferences.getSharedPathToPMD();
        m_sharedPathToPMD = createTextArea(directory, dataPanel, row, 1);
        createFileButton(dataPanel, row, 2, m_sharedPathToPMD);

        return dataPanel;
    }

    /**
     *******************************************************************************
     *
     * @param text
     * @param dataPanel
     * @param row
     * @param column
     */
    private void createLabel(String text, JPanel dataPanel, int row, int column)
    {
        JLabel label = new JLabel(text);
        label.setFont(UIManager.getFont("labelFont"));
        label.setHorizontalAlignment(JLabel.RIGHT);
        label.setForeground(UIManager.getColor("pmdBlue"));

        GridBagLayout layout;
        GridBagConstraints constraints;

        layout = (GridBagLayout) dataPanel.getLayout();
        constraints = layout.getConstraints(label);
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = constraints.NORTHEAST;
        constraints.fill = constraints.NONE;
        constraints.insets = new Insets(2, 2, 2, 2);

        dataPanel.add(label, constraints);
    }

    /**
     *******************************************************************************
     *
     * @param text
     * @param dataPanel
     * @param row
     * @param column
     */
    private JTextArea createTextArea(String text, JPanel dataPanel, int row, int column)
    {
        JTextArea textArea;
        JScrollPane scrollPane;
        GridBagLayout layout;
        GridBagConstraints constraints;
        Font font;
        FontMetrics fontMetrics;
        int height;
        int width;
        Dimension size;

        textArea = ComponentFactory.createTextArea(text);

        scrollPane = ComponentFactory.createScrollPane(textArea);
        font = textArea.getFont();
        fontMetrics = textArea.getFontMetrics(font);
        width = 500;
        height = (3 * fontMetrics.getHeight()) + 5;
        size = new Dimension(width, height);
        scrollPane.setSize(size);
        scrollPane.setMinimumSize(size);
        scrollPane.setPreferredSize(size);

        layout = (GridBagLayout) dataPanel.getLayout();
        constraints = layout.getConstraints(scrollPane);
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = constraints.WEST;
        constraints.fill = constraints.BOTH;
        constraints.insets = new Insets(2, 2, 2, 2);

        dataPanel.add(scrollPane, constraints);

        return textArea;
    }

    /**
     *******************************************************************************
     *
     * @param dataPanel
     * @param row
     * @param column
     */
    private void createFileButton(JPanel dataPanel, int row, int column, JTextArea textArea)
    {
        JButton button;
        GridBagLayout layout;
        GridBagConstraints constraints;

        button = ComponentFactory.createButton("Find File");
        button.setBackground(UIManager.getColor("pmdBlue"));
        button.setForeground(Color.white);
        button.addActionListener(new FileButtonActionListener(textArea));
        layout = (GridBagLayout) dataPanel.getLayout();
        constraints = layout.getConstraints(button);
        constraints.gridx = column;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.anchor = constraints.NORTHWEST;
        constraints.fill = constraints.NONE;
        constraints.insets = new Insets(2, 2, 2, 2);

        dataPanel.add(button, constraints);
    }

    /**
     *******************************************************************************
     *
     */
    private JPanel createButtonPanel()
    {
        ActionListener saveActionListener = new SaveButtonActionListener();
        ActionListener cancelActionListener = new CancelButtonActionListener();

        return ComponentFactory.createSaveCancelButtonPanel(saveActionListener,
                                                            cancelActionListener);
    }

    /**
     *******************************************************************************
     *******************************************************************************
     *******************************************************************************
     */
    private class SaveButtonActionListener implements ActionListener
    {

        /**
         ********************************************************************
         *
         * @param event
         */
        public void actionPerformed(ActionEvent event)
        {
            try
            {
                Preferences preferences = Preferences.getPreferences();
                preferences.setCurrentPathToPMD(m_currentPathToPMD.getText());
                preferences.setUserPathToPMD(m_userPathToPMD.getText());
                preferences.setSharedPathToPMD(m_sharedPathToPMD.getText());
                preferences.save();
            }
            catch (PMDException exception)
            {
                MessageDialog.show(PMDViewer.getViewer(), exception.getMessage(), exception);
            }

            PreferencesEditor.this.setVisible(false);
        }
    }

    /**
     *******************************************************************************
     *******************************************************************************
     *******************************************************************************
     */
    private class CancelButtonActionListener implements ActionListener
    {

        /**
         ********************************************************************
         *
         * @param event
         */
        public void actionPerformed(ActionEvent event)
        {
            PreferencesEditor.this.setVisible(false);
        }
    }

    /**
     *******************************************************************************
     *******************************************************************************
     *******************************************************************************
     */
    private class FileButtonActionListener implements ActionListener
    {

        private JTextArea m_textArea;

        /**
         **************************************************************************
         *
         * @param directory
         */
        private FileButtonActionListener(JTextArea textArea)
        {
            m_textArea = textArea;
        }

        /**
         ********************************************************************
         *
         * @param event
         */
        public void actionPerformed(ActionEvent event)
        {
            File file = new File(m_textArea.getText());

            if (file.exists() == false)
            {
                file = new File(System.getProperty("user.home"));
            }
            else if (file.isDirectory() == false)
            {
                file = file.getParentFile();
            }

            JFileChooser fileChooser = new JFileChooser(file);
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fileChooser.setApproveButtonText("Select");
            fileChooser.setMinimumSize(new Dimension(500, 500));

            if (fileChooser.showOpenDialog(PMDViewer.getViewer()) == JFileChooser.APPROVE_OPTION)
            {
                file = fileChooser.getSelectedFile();

                m_textArea.setText(file.getPath());
            }
        }
    }
}