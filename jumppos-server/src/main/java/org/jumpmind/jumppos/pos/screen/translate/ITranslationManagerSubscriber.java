package org.jumpmind.jumppos.pos.screen.translate;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.jumpmind.jumppos.core.flow.Action;
import org.jumpmind.jumppos.core.screen.DefaultScreen;

public interface ITranslationManagerSubscriber extends Remote {

    public void showScreen(DefaultScreen screen) throws RemoteException;

    public void doAction(Action action) throws RemoteException;

    public boolean isInTranslateState() throws RemoteException;

    public String getNodeId() throws RemoteException;

}
