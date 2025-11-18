/**
 * Represents a friend request operation executed in a separate thread.
 * This class implements {@link Runnable} so that sending or processing
 * a friend request can run asynchronously without blocking other tasks.
 */
public class FriendRequestThread implements Runnable {

    /**
     * Creates a new FriendRequestThread with a specified sender and receiver.
     *
     * @param sender   the student sending the friend request
     * @param receiver the student receiving the friend request
     */
    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
    }

    /**
     * Executes the logic for processing the friend request when the thread starts.
     * This method is invoked automatically when a {@link Thread} object that wraps
     * this runnable is started.
     */
    @Override
    public void run() {
        // Method signature only
    }
}
