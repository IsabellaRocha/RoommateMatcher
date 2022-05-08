
import java.util.concurrent.*;

public class ConnectDispatcher extends Thread {
	
	private String email;
	private int age; 
	Semaphore semaPhore; 
	
	public void Match(int age, String email) {
		this.age = age;
		this.email = email; 
    }
	
	//setter for semaphore
	public void setSemaphore(Semaphore semaPhore) {
		this.semaPhore = semaPhore;
	}
	
	// getter for company
	public int getAge() {
		return this.age;
	}
	
	//getter for time
	public String getEmail() {
		return this.email;
	}
	

	/**
	 * Matching function using locks
	 */
	
	
	@Override
	public void run() {
		try {
			semaPhore.acquire();
			System.out.println(Utility.getZeroTimestamp() + " Starting " + this);
		}
		
		catch (InterruptedException oof) {
			System.out.println("This process was interrupted, better luck next time!");
		}
		
		finally {
			System.out.print(Utility.getZeroTimestamp() + " Finishing " + this + "\n");
			semaPhore.release();
		}
	}
}
