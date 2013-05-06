    package client.ui;

import client.core.ClientCommand;
import client.core.IEncryptionSessionListener;
import core.protocol.Request;
import core.protocol.Response;
import core.protocol.exceptions.MessageException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Edward Wilde
 */
public class Application extends JFrame implements IApplication, IEncryptionSessionListener
{
    public static final int FRAME_WIDTH = 590;
    public static final int FRAME_HEIGHT = 500;

    private Composer composer;
    private ResultViewer resultViewer;
    private UserInterfaceSession session;

    public Application()
    {
        super("Encryption client");
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        this.session = new UserInterfaceSession(this, this);
        this.session.connectToServer();
        this.composer = new Composer(this);
        this.resultViewer = new ResultViewer();

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                this.composer, this.resultViewer);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(150);

        this.add(splitPane);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    @Override
    public Component getTopLevelComponent()
    {
        return this;
    }

    @Override
    public String getRequestData()
    {
        return this.composer.getRequestData();
    }

    @Override
    public void ExecuteRequest(ClientCommand request)
    {
        try
        {
            this.session.doCommand(request);
        } catch (MessageException ex)
        {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDoQuit()
    {
        this.setVisible(false);
        this.dispose();
    }

    @Override
    public void beforeRequestSend(Request request)
    {
        this.resultViewer.addResult(request);
    }

    @Override
    public void afterResponseReceived(Response response)
    {
        this.resultViewer.updateResult(response);
    }

    @Override
    public void onWarn(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void onError(String message)
    {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
