import java.util.concurrent.Semaphore;

/**
 * Represents a friend request operation executed in a separate thread.
 * This class implements {@link Runnable} so that sending or processing
 * a friend request can run asynchronously without blocking other tasks.
 */
public class FriendRequestThread implements Runnable {
    private UniversityStudent sender;
    private UniversityStudent receiver;
    // Static semaphore to ensure thread-safe friend request operations.
    private static final Semaphore semaphore = new Semaphore(1);

    /**
     * Creates a new FriendRequestThread with a specified sender and receiver.
     *
     * @param sender   the student sending the friend request
     * @param receiver the student receiving the friend request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Executes the logic for processing the friend request when the thread starts.
     * This method is invoked automatically when a {@link Thread} object that wraps
     * this runnable is started.
     */
    @Override
    public void run() {
        try{
            semaphore.acquire();
            // Simulate sending a friend request. In a full implementation, you would update shared data.
            System.out.println("FriendRequest (Thread-Safe): " + sender.name + " sent a friend request to " + receiver.name);
            sender.addFriendRequestCount();
            //receiver.addFriendRequestCount();   //increment unistudent data

        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
            System.err.println("FriendRequest interrupted: " + e.getMessage());
        } finally{
            semaphore.release();
        }
    }
}