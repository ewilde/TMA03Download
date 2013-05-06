package client.ui;

import client.core.*;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Edward Wilde
 */
public class UserInterfaceSession extends EncryptionSession
{
    private final Application application;
    public UserInterfaceSession(Application parent, IEncryptionSessionListener listener)
    {
        super(listener);
        this.application = parent;
    }

    @Override
    public void doHelp()
    {
        JOptionPane.showMessageDialog(this.application, ClientCommand.listCommands());
    }

    @Override
    public String GetInputFromUser()
    {
        return this.application.getRequestData();
    }

}
