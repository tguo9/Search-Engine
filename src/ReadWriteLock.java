/**
 * A simple custom lock that allows simultaneously read operations, but
 * disallows simultaneously write and read/write operations.
 *
 * Does not implement any form or priority to read or write operations. The
 * first thread that acquires the appropriate lock should be allowed to
 * continue.
 */
public class ReadWriteLock {
	private int readers;
	private int writers;

	/**
	 * Initializes a multi-reader single-writer lock.
	 */
	public ReadWriteLock() {
		readers = 0;
		writers = 0;
	}

	/**
	 * Will wait until there are no active writers in the system, and then will
	 * increase the number of active readers.
	 */
	public synchronized void lockReadOnly() {

		// Will wait until there are no active writers in the system
		while (writers > 0) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("There is an error whlie waiting");
			}
		}

		// Increase the number of active readers
		readers++;
	}

	/**
	 * Will decrease the number of active readers, and notify any waiting threads if
	 * necessary.
	 */
	public synchronized void unlockReadOnly() {
		// Decrease the number of active readers
		readers--;

		// Notify any waiting threads if necessary
		this.notifyAll();

		// TODO Over-notification issue.
		// TODO Only call notifyAll when readers is 0
	
	}

	/**
	 * Will wait until there are no active readers or writers in the system, and
	 * then will increase the number of active writers.
	 */
	public synchronized void lockReadWrite() {
		// Will wait until there are no active readers or writers in the system
		while (readers > 0 || writers > 0) {

			try {
				this.wait();
			} catch (InterruptedException e) {
				System.out.println("There is an error whlie waiting");
			}
		}
		// Increase the number of active writers
		writers++;
	}

	/**
	 * Will decrease the number of active writers, and notify any waiting threads if
	 * necessary.
	 */
	public synchronized void unlockReadWrite() {

		// Decrease the number of active writers
		writers--;

		// Notify any waiting threads if necessary
		this.notifyAll();
	}
}
