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
    private final Component parent;
    public UserInterfaceSession(Component parent, IEncryptionSessionListener listener)
    {
        super(listener);
        this.parent = parent;
    }

    @Override
    public void doHelp()
    {
        JOptionPane.showMessageDialog(this.parent, ClientCommand.listCommands());
    }

    @Override
    public String GetInputFromUser()
    {
    }

}
