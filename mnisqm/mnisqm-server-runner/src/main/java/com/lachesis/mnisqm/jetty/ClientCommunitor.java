package com.lachesis.mnisqm.jetty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author Paul Xu.
 * @since 1.0.0
 */
public class ClientCommunitor {
    private static Logger LOG = LoggerFactory.getLogger(ClientCommunitor.class);

    private int port;

    public ClientCommunitor(int port) {
        this.port = port;
    }

    public void reloadRequest(File file) {
        LOG.info(String.format("Send update request to the main process at port %d with file %s",
                port, file.getAbsolutePath()));
        try (Socket socket = new Socket("localhost", port);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF("RELOAD:" + file.getAbsolutePath());
        } catch (Exception e) {
            LOG.error("Error while send RELOAD request to the host process", e);
        }
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 53631);
             OutputStream outputStream = socket.getOutputStream();
             DataOutputStream dataOutputStream = new DataOutputStream(outputStream)) {
            dataOutputStream.writeUTF("RELOAD mnisqm");
        } catch (Exception e) {
            LOG.error("Error while send reload request to the host process", e);
        }
    }
}
