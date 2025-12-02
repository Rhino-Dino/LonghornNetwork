import java.util.concurrent.Semaphore;

/**
 * Represents a chat thread between two university students. 
 * This class implements Runnable so that message handling 
 * can be executed on a separate thread.
 */
public class ChatThread implements Runnable {
    private UniversityStudent sender;
    private UniversityStudent receiver;
    private String message;
    //static semaphore to ensure thread-safe chat operations.

    public static final Semaphore semaphore = new Semaphore(1);

    /**
     * Creates a new ChatThread with a specified sender, receiver, and message.
     *
     * @param sender   the student sending the message
     * @param receiver the student receiving the message
     * @param message  the content of the message to be sent
     */
    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    /**
     * Executes the logic for handling the chat interaction when 
     * the thread is started. This method is invoked when the 
     * thread's start() method is called.
     */
    @Override
    public void run() {
        try{
            semaphore.acquire();
            // Simulate sending a chat message. A real implementation would update a shared chat history.
            System.out.println("Chat (Thread-Safe): " + sender.name + " to " + receiver.name + ": " + message);
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("Chat interrupted: " + e.getMessage());
        } finally{
            semaphore.release();
        }
    }
}