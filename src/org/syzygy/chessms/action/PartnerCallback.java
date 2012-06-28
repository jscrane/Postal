package org.syzygy.chessms.action;

public interface PartnerCallback
{
    void onReceive(String move);
    
    void onSent(String move);
}
