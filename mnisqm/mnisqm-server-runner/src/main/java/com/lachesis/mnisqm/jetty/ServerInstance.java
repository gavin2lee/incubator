package com.lachesis.mnisqm.jetty;

import java.io.File;

import com.lachesis.mnisqm.core.CommRuntimeException;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class ServerInstance {
    private static ServerInstance instance = new ServerInstance();
    private GenericServerRunner server;

    private boolean isFirstTimeRunner = false;
    private boolean isUpgrading = false;

    private ServerInstance() {
    }

    public void registerInstance(GenericServerRunner serverProcess) {
        if (server != null) {
            throw new CommRuntimeException("There is a running server instance already");
        }
        this.server = serverProcess;
    }

    public void preUpgrade() {
        isUpgrading = true;
    }

    public void upgrade(File upgradeFile) {
        server.upgrade(upgradeFile);
    }

    public boolean isUpgrading() {
        return isUpgrading;
    }

    void setIsUpgrading(boolean isUpgrading) {
        this.isUpgrading = isUpgrading;
    }

    public static ServerInstance getInstance() {
        return instance;
    }

    public boolean isFirstTimeRunner() {
        return isFirstTimeRunner;
    }

    public void setIsFirstTimeRunner(boolean isFirstTimeRunner) {
        this.isFirstTimeRunner = isFirstTimeRunner;
    }
}
