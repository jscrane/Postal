package org.syzygy.postal.io;

import java.io.IOException;

public interface AbstractTransport
{
    void setRemoteAddress(String remoteAddress);

    boolean hasPeer();

    void connect(String message) throws IOException;

    void send(String message) throws IOException;

    String receive() throws IOException;

    void listen() throws IOException;

    void close();
}
