import com.github.strattonbrazil.checklist.Action
import com.github.strattonbrazil.checklist.ChecklistContext
import java.util.concurrent.Callable;

class MyAction extends Action
{
    Callable<String> getWork(ChecklistContext ctx) {
        ctx.src("**sax.txt", [name: 'Gromit', likes: 'cheese', id: 1234]);
        return { "foo" } as Callable
    }
}

checklist.addAction("copy_images", ["compile", "clean"], new MyAction())

checklist.addAction("clean", new MyAction())

checklist.addAction("compile", ["clean"], new MyAction())

checklist.addAction("default", ["compile"], new MyAction())
