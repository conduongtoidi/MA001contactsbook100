package com.whatsoft.contactbook.utils;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Message Handler class that supports buffering up of messages when the
 * activity is paused i.e. in the background.
 * @see http://stackoverflow.com/questions/8040280/how-to-handle-handler-messages-when-activity-fragment-is-paused/8122789#8122789
 */
public abstract class PauseHandler extends Handler {

    /**
     * Message Queue Buffer
     */
    final List<Message> messageQueueBuffer = new ArrayList<Message>();

    /**
     * Flag indicating the pause state
     */
    private boolean paused = true;

    /**
     * Resume the handler
     */
    final public void resume() {
        paused = false;
        while (messageQueueBuffer.size() > 0) {
            final Message msg = messageQueueBuffer.remove(0);
            sendMessage(msg);
        }
    }

    /**
     * Pause the handler
     */
    final public void pause() {
        paused = true;
    }

    /**
     * Notification that the message is about to be stored as the activity is
     * paused. If not handled the message will be saved and replayed when the
     * activity resumes.
     * 
     * @param message
     *            the message which optional can be handled
     * @return true if the message is to be stored
     */
    protected abstract boolean storeMessage(Message message);

    /**
     * Notification message to be processed. This will either be directly from
     * handleMessage or played back from a saved message when the activity was
     * paused.
     * 
     * @param message
     *            the message to be handled
     */
    protected abstract void processMessage(Message message);

    /** {@inheritDoc} */
    @Override
    final public void handleMessage(Message msg) {
        if (paused) {
            if (storeMessage(msg)) {
                Message msgCopy = new Message();
                msgCopy.copyFrom(msg);
                messageQueueBuffer.add(msgCopy);
            }
        } else {
            processMessage(msg);
        }
    }
}
