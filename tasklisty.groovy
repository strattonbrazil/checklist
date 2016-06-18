@Grab(group='commons-io', module='commons-io', version='2.5')
import org.apache.commons.io.FileUtils;

class CleanTask extends Task
{
    Callable<String> getWork(TaskContext ctx) {

        //println("creating file")
        //FileUtils.deleteDirectory("./bin");

        return { "foo" } as Callable
    }
}

//tasklist.addTask("foo", ["compile", "clean"], { TaskContext ctx -> })


class CompileTask extends Task
{
    Callable<String> getWork(TaskContext ctx) {
        //ctx.src("")

        return { "foo" } as Callable
    }
}

class MyTask extends Task
{
    Callable<String> getWork(TaskContext ctx) {
        ctx.src("**sax.txt", [name: 'Gromit', likes: 'cheese', id: 1234])
           .pipe(ctx.dest("/tmp/foo"))
           .pipe(ctx.dest("/tmp/bar"));
        return { "foo" } as Callable
    }
}

tasklist.addTask("copy_images", ["compile", "clean"], new MyTask())

tasklist.addTask("clean", new CleanTask())

tasklist.addTask("compile", ["clean"], new CompileTask())

tasklist.addTask("default", ["compile"], new MyTask())
