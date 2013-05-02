/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client.ui;

import core.protocol.Request;
import core.protocol.Response;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Edward Wilde
 */
public class ResultViewer extends JPanel
{
    private JTable resultTable;
    private JTextArea resultDetail;
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

        // detail
        this.resultDetail = new JTextArea();
        this.resultDetail.setPreferredSize(new Dimension(Application.FRAME_WIDTH, 200));

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
    }    
}
