package client.ui;

import client.core.*;
import core.protocol.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author Edward Wilde
 */
public class Composer extends JPanel
{
    private JComboBox commands;
    private JTextArea requestData;
    private JTextArea requestEditor;
    private JTabbedPane requestTab;
    private JButton executeButton;
    private IRequestFactory requestFactory;
    private Request request;
    private final IApplication application;

    public Composer(IApplication application)
    {
        this.requestFactory = new RequestMessageFactory();
        this.InitializeComponents();
        this.updateRequestInformation();
        this.application = application;
    }

    public String getRequestData()
    {
        return this.requestData.getText();
    }
    
    private void InitializeComponents()
    {
        this.setMinimumSize(new Dimension(Application.FRAME_WIDTH, 150));
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints bagContraints = new GridBagConstraints();

        // Combo
        RequestItemUpdated itemUpdated = new RequestItemUpdated();
        this.commands = new JComboBox(ClientCommand.values());
        this.commands.setAlignmentX(TOP_ALIGNMENT);
        this.commands.addActionListener(itemUpdated);
        bagContraints.anchor = GridBagConstraints.NORTH;
        bagContraints.insets = new Insets(5, 5, 5, 5);
        bagContraints.gridx = 0;
        bagContraints.gridy = 0;
        container.add(this.commands, bagContraints);

        // Request
        this.requestTab = new JTabbedPane(JTabbedPane.TOP);
        JPanel requestPanel = new JPanel();
        this.requestTab.addTab("Request data", requestPanel);
        this.requestData = new JTextArea();
        this.requestData.setColumns(30);
        this.requestData.setRows(5);
        this.requestData.getDocument().addDocumentListener(itemUpdated);
        requestPanel.add(new JScrollPane(this.requestData));
        JPanel previewPanel = new JPanel();
        this.requestTab.addTab("Request editor", previewPanel);

        this.requestEditor = new JTextArea();
        this.requestEditor.setColumns(30);
        this.requestEditor.setRows(5);
        previewPanel.add(new JScrollPane(this.requestEditor));

        // Add tabbed pane.
        bagContraints.gridx = 1;
        container.add(this.requestTab, bagContraints);

        // Button
        this.executeButton = new JButton("Execute");
        this.executeButton.addActionListener(new ExecuteButtonListener());
        bagContraints.gridx = 3;
        container.add(this.executeButton, bagContraints);
        this.add(container);
    }

    private void updateRequestInformation()
    {
        if (this.getSelectedVerb() == null)
        {
            return;
        }

        this.request = new Request();
        this.request.setVerb(this.getSelectedVerb());
        this.request.setBody(this.requestData.getText());
        this.requestEditor.setText(this.request.toString());
    }

    private ClientCommand getSelectedCommand()
    {
        return (ClientCommand)this.commands.getSelectedItem();
    }

    private Verb getSelectedVerb()
    {
        try
        {
            return Verb.getVerb(this.getSelectedCommand().getName());
        }
        catch (IllegalArgumentException argumentException)
        {
            return null;
        }
    }

    private void ExecuteRequest()
    {
        this.application.ExecuteRequest(this.getSelectedCommand());
    }

    private class ExecuteButtonListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Composer.this.ExecuteRequest();
        }
    }

    private class RequestItemUpdated implements ActionListener, DocumentListener
    {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            Composer.this.updateRequestInformation();
        }

        @Override
        public void insertUpdate(DocumentEvent e)
        {
            Composer.this.updateRequestInformation();
        }

        @Override
        public void removeUpdate(DocumentEvent e)
        {
            Composer.this.updateRequestInformation();
        }

        @Override
        public void changedUpdate(DocumentEvent e)
        {
            Composer.this.updateRequestInformation();
        }

    }
}
