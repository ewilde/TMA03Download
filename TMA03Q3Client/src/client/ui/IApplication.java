package client.ui;

import client.core.ClientCommand;
import java.awt.Component;

/**
 *
 * @author EddieDesk
 */
public interface IApplication
{
    Component getTopLevelComponent();
    String getRequestData();
    void ExecuteRequest(ClientCommand request);
}
