/**
 * Represents a chat thread between two university students. 
 * This class implements Runnable so that message handling 
 * can be executed on a separate thread.
 */
public class ChatThread implements Runnable {

    /**
     * Creates a new ChatThread with a specified sender, receiver, and message.
     *
     * @param sender   the student sending the message
     * @param receiver the student receiving the message
     * @param message  the content of the message to be sent
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        // Constructor
    }

    /**
     * Executes the logic for handling the chat interaction when 
     * the thread is started. This method is invoked when the 
     * thread's start() method is called.
     */
    @Override
    public void run() {
        // Method signature only
    }
}
