import java.util.HashSet;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskRunner {

	private ScheduledThreadPoolExecutor executor;
	private HashSet<ScheduledFuture<?>> runningTasks = new HashSet<ScheduledFuture<?>>();

	public TaskRunner(int cores) {
		executor = new ScheduledThreadPoolExecutor(cores);
		executor.setRemoveOnCancelPolicy(true);
	}

	public void runTask(Runnable task) {
		executor.submit(task);
	}

	public void scheduleTask(Runnable task, int rate, boolean killable) {
		ScheduledFuture<?> future = executor.scheduleWithFixedDelay(task, 0, rate, TimeUnit.MILLISECONDS);
		if (killable) {
			runningTasks.add(future);
		}
	}

	public void scheduleTaskSet(HashSet<Runnable> tasks, int rate, boolean killable) {
		tasks.forEach(task -> scheduleTask(task, rate, killable));
	}

	public void clearTasks() {
		runningTasks.forEach(task -> task.cancel(false));
		runningTasks.clear();
		executor.purge();
	}
}
