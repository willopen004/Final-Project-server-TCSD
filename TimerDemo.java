package Drone_System;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
public class TimerDemo extends TimerTask{
	static Timer timer;
	static int cnt = 10800;
	static boolean is_over = false;
	public void run() {
	if(cnt!=0)
	{
	System.out.println("timer working: "+ cnt);
	cnt--;
	}
	else if(cnt == 0)
	{
		timer.cancel();
		TimerDemo.is_over = true;
		System.out.println("Your time is up");
		try {
			Service.check_drone_timer(System_Control.drone_index, System_Control.busy_drones,System_Control.lost_drones, System_Control.busy_drones.getFirst());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}
	}
}