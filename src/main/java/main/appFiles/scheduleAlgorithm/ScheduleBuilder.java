package main.appFiles.scheduleAlgorithm;

import com.google.ortools.Loader;
import com.google.ortools.sat.CpModel;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.CpSolverStatus;
import com.google.ortools.sat.IntVar;
import com.google.ortools.sat.CpSolver;
import com.google.ortools.sat.LinearExpr;

import main.appFiles.schedulingData.Availability;
import main.appFiles.schedulingData.Employee;
import main.appFiles.schedulingData.Schedule;
import main.appFiles.schedulingData.TimeRange;

import com.google.ortools.sat.BoolVar;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

/**
 * Builds schedules for a single day using OR-Tools to balance load and obey
 * employee availability.
 */
public class ScheduleBuilder {
    static { Loader.loadNativeLibraries(); }
    
    /**
     * Generate a schedule for a specific day.
     *
     * @param s base schedule containing employees and time range
     * @param scheduleDay day of the week to schedule
     * @return the populated schedule instance
     */
    public static Schedule scheduleBuildDay(Schedule s, DayOfWeek scheduleDay) {
        LocalTime startTime = s.getStartTime();
        LocalTime endTime = s.getEndTime();
        int startHour = startTime.getHour();
        int endHour = endTime.getHour();
        int numSlots = endHour - startHour;  // whole-hour slots

        CpModel model = new CpModel();
        
        List<Employee> employees = s.getEmployees();
        Map<Integer, BoolVar[]> assignments = new HashMap<>();

        // Create assignment vars and enforce availability
        for (Employee e : employees) {
          BoolVar[] vars = new BoolVar[numSlots];
          Availability unavail = e.getAvailabilities().get(scheduleDay.toString());
          for (int t = 0; t < numSlots; t++) {
            vars[t] = model.newBoolVar("emp_" + e.getEmployeeId() + "_slot_" + t);
            if (unavail != null) {
              LocalTime slotStart = LocalTime.of(startHour + t, 0);
              LocalTime slotEnd   = slotStart.plusHours(1);
              for (TimeRange tr : unavail.getTimeRanges()) {
                if (slotStart.isBefore(tr.getEnd()) && slotEnd.isAfter(tr.getStart())) {
                  model.addEquality(vars[t], 0);
                  break;
                }
              }
            }
          }
          assignments.put(e.getEmployeeId(), vars);
        }

        // Coverage: exactly one employee per slot
        for (int t = 0; t < numSlots; t++) {
          List<BoolVar> slotVars = new ArrayList<>();
          for (Employee e : employees) {
            slotVars.add(assignments.get(e.getEmployeeId())[t]);
          }
          model.addEquality(LinearExpr.sum(slotVars.toArray(new BoolVar[0])), 1);
        }

        // Load balancing: track each employee's total slots
        Map<Integer, IntVar> loadVars = new HashMap<>();
        for (Employee e : employees) {
          BoolVar[] empSlots = assignments.get(e.getEmployeeId());
          IntVar load = model.newIntVar(0, numSlots, "load_emp_" + e.getEmployeeId());
          model.addEquality(load, LinearExpr.sum(empSlots));
          loadVars.put(e.getEmployeeId(), load);
        }
        IntVar maxLoad = model.newIntVar(0, numSlots, "maxLoad");
        IntVar minLoad = model.newIntVar(0, numSlots, "minLoad");
        for (IntVar load : loadVars.values()) {
          model.addLessOrEqual(load, maxLoad);
          model.addGreaterOrEqual(load, minLoad);
        }
        model.minimize(LinearExpr.newBuilder()
                          .addTerm(maxLoad,  1)
                          .addTerm(minLoad, -1)
                          .build());

        // Ensure everyone works at least once
        for (Employee e : employees) {
          model.addGreaterOrEqual(
              LinearExpr.sum(assignments.get(e.getEmployeeId())), 1);
        }

        // Solve
        CpSolver solver = new CpSolver();
        CpSolverStatus status = solver.solve(model);
        List<Shift> shifts = new ArrayList<>();
        if (status == CpSolverStatus.OPTIMAL || status == CpSolverStatus.FEASIBLE) {
          for (int t = 0; t < numSlots; t++) {
            for (Employee e : employees) {
              if (solver.value(assignments.get(e.getEmployeeId())[t]) == 1) {
                LocalTime slotStart = LocalTime.of(startHour + t, 0);
                shifts.add(new Shift(e.getEmployeeId(), scheduleDay,
                                     slotStart, slotStart.plusHours(1)));
                break;
              }
            }
          }
        } else {
          System.out.println("No solution found for " + scheduleDay);
        }
        Collections.sort(shifts, Comparator.comparing(Shift::getStart));
        s.setShifts(shifts);
        return s;
    }
}
