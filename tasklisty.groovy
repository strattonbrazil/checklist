import com.github.strattonbrazil.checklist.Task
import com.github.strattonbrazil.checklist.TaskContext
import java.util.concurrent.Callable;

class MyTask extends Task
{
    Callable<String> getWork(TaskContext ctx) {
        ctx.src("**sax.txt", [name: 'Gromit', likes: 'cheese', id: 1234]);
        return { "foo" } as Callable
    }
}

tasklist.addTask("copy_images", ["compile", "clean"], new MyTask())

tasklist.addTask("clean", new MyTask())

tasklist.addTask("compile", ["clean"], new MyTask())

tasklist.addTask("default", ["compile"], new MyTask())
