
// Time object holds the number of seconds and the name of the winner associated with that time

public class Time implements Comparable<Time>
{
	String name;
	int seconds;
	
	public Time(String n, int s) {
		name = n;
		seconds = s;
	}
	
	public String getName() {
		return name;
	}
	
	public int getTime() {
		return seconds;
	}

	@Override
	public int compareTo(Time t) {
		if(this.getTime() > t.getTime()) {
			return 1;
		} else if(this.getTime() == t.getTime()) {
			return 0;
		} else {
			return -1;
		}
	}
}
