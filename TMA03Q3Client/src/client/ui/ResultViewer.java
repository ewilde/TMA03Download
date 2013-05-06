/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.ui;

import core.protocol.Request;
import core.protocol.Response;
import core.protocol.StatusCode;
import java.awt.*;
import java.util.logging.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;

/**
 *
 * @author Edward Wilde
 */
public class ResultViewer extends JPanel
{
    private static final Logger logger = Logger.getLogger(ResultViewer.class.getName());

    private JTable resultTable;
    private JTextPane resultDetail;
    private final DefaultTableModel tableModel;

    private int verbIndex = 0, statusIndex = 1, timeIndex = 2;
    private int tokenIndex = 3, requestIndex = 4, responseIndex = 5;

    public ResultViewer()
    {
        this.setLayout(new BorderLayout());
        // table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Verb");
        tableModel.addColumn("Status");
        tableModel.addColumn("Time");
        tableModel.addColumn("Token");
        tableModel.addColumn("Request");
        tableModel.addColumn("Response");

        this.resultTable = new JTable(tableModel);

        this.resultTable.removeColumn(this.resultTable.getColumnModel().getColumn(tokenIndex));
        this.resultTable.removeColumn(this.resultTable.getColumnModel().getColumn(requestIndex - 1));
        this.resultTable.removeColumn(this.resultTable.getColumnModel().getColumn(responseIndex - 2));

        JTableHeader header = this.resultTable.getTableHeader();
        header.setBackground(Color.yellow);

        this.resultTable.setColumnSelectionAllowed(false);
        this.resultTable.setRowSelectionAllowed(true);
        this.resultTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.resultTable.getSelectionModel().addListSelectionListener(new TableListSelectionListener());

        // detail
        this.resultDetail = new JTextPane();
        this.resultDetail.setPreferredSize(new Dimension(Application.FRAME_WIDTH, 200));
        StyledDocument document = this.resultDetail.getStyledDocument();
         //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = document.addStyle("Regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style style = document.addStyle("Heading", def);
        StyleConstants.setFontSize(style, 16);
        StyleConstants.setBold(style, true);
        StyleConstants.setSpaceBelow(style, 10);
        StyleConstants.setForeground(style, new Color(38, 127, 0));


        style = document.addStyle("ErrorHeading", def);
        StyleConstants.setFontSize(style, 16);
        StyleConstants.setBold(style, true);
        StyleConstants.setSpaceBelow(style, 10);
        StyleConstants.setForeground(style, new Color(195, 0, 0));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(this.resultTable), this.resultDetail);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(Application.FRAME_WIDTH / 2);

        this.add(splitPane, BorderLayout.CENTER);
    }

    void addResult(Request request)
    {
          this.tableModel.addRow(
                new Object[] {
                    request.getVerb().toString(),
                    null,
                    request.getMessageCreatedDate(),
                    request.getCorrelationToken(),
                    request,
                    null});

          this.resultTable.setRowSelectionInterval(
                  this.resultTable.getRowCount()- 1,
                  this.resultTable.getRowCount() - 1);
    }

    void updateResult(Response response)
    {
        for (int row = 0; row <= tableModel.getRowCount() - 1; row++)
        {
            Object token = tableModel.getValueAt(row, tokenIndex);
            if (token.equals(response.getCorrelationToken()))
            {
                tableModel.setValueAt(response.getStatusCode(), row, statusIndex);
                tableModel.setValueAt(response, row, responseIndex);
            }
        }

        this.UpdateDetail();
    }

    private void UpdateDetail()
    {
        try
        {
            int index = this.resultTable.convertRowIndexToModel(this.resultTable.getSelectedRow());
            logger.info(String.format("Update detail entered. e.getFirstIndex():%s.", index));
            Request request = (Request) this.tableModel.getValueAt(index, requestIndex);
            Response response = (Response) this.tableModel.getValueAt(index, responseIndex);
            this.resultDetail.setText("");
            StyledDocument document = this.resultDetail.getStyledDocument();
            document.insertString(0, String.format("Request%n"), document.getStyle("Heading"));
            document.insertString(document.getLength(), String.format("%s%n", request.toString()), document.getStyle("Regular"));
            if (response != null)
            {
                document.insertString(document.getLength(), String.format("Response%n"), response.getStatusCode() == StatusCode.OK ? document.getStyle("Heading") : document.getStyle("ErrorHeading"));
                document.insertString(document.getLength(), String.format("%s%n", response.toString()), document.getStyle("Regular"));
            }
        } catch (BadLocationException ex)
        {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    private class TableListSelectionListener implements ListSelectionListener
    {
        @Override
        public void valueChanged(final ListSelectionEvent e)
        {
            ResultViewer.this.UpdateDetail();
        }

    }
}
