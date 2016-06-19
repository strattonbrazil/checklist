@Grab(group='commons-io', module='commons-io', version='2.5')
import org.apache.commons.io.FileUtils;

tasklist.addTask("clean", {
    TaskContext ctx ->
        FileUtils.deleteDirectory(new File("./bin"));
        "cleaned"
})

tasklist.addTask("compile", ["clean"], {
    TaskContext ctx ->
        println "compiling"
        println ctx
        ctx.src("**sax.txt", [name: 'Gromit', likes: 'cheese', id: 1234])
           .pipe(ctx.dest("./bin/foo"))
           .pipe(ctx.dest("./bin/bar"))
        "compiled"
})

tasklist.addTask("default", ["compile"], {
    TaskContext ctx -> "foo"
})

