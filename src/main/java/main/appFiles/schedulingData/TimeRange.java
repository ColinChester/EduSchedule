package main.appFiles.schedulingData;

import java.time.LocalTime;

public class TimeRange {
	private LocalTime start;
	private LocalTime end;
	
	public TimeRange(String start, String end) {
	    LocalTime s = LocalTime.parse(start);
	    LocalTime e = LocalTime.parse(end);
	    if (s.compareTo(e) >= 0) {
	        throw new IllegalArgumentException("Start time must be before end time.");
	    }
	    this.start = s;
	    this.end = e;
	}

	public TimeRange(LocalTime start, LocalTime end) {
	    if (start.compareTo(end) >= 0) {
	        throw new IllegalArgumentException("Start time must be before end time.");
	    }
	    this.start = start;
	    this.end = end;
	}

	public LocalTime getStart() {
	    return start;
	}

	public LocalTime getEnd() {
	    return end;
	}

	@Override
	public String toString() {
	    return start.toString() + "-" + end.toString();
	}

	
	
}
