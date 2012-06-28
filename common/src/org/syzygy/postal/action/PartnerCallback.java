package org.syzygy.postal.action;

public interface PartnerCallback
{
    void onReceive(String move);

    void onSent(String move);
}
