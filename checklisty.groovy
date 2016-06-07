import com.github.strattonbrazil.checklist.Task
import com.github.strattonbrazil.checklist.ChecklistContext
import java.util.concurrent.Callable;

class MyTask extends Task
{
    Callable<String> getWork(ChecklistContext ctx) {
        ctx.src("**sax.txt", [name: 'Gromit', likes: 'cheese', id: 1234]);
        return { "foo" } as Callable
    }
}

checklist.addTask("copy_images", ["compile", "clean"], new MyTask())

checklist.addTask("clean", new MyTask())

checklist.addTask("compile", ["clean"], new MyTask())

checklist.addTask("default", ["compile"], new MyTask())
