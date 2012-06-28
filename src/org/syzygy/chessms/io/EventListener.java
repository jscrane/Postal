package org.syzygy.chessms.io;

public interface EventListener 
{
	void onEvent(String eventClass, Object event);
}
