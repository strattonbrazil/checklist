@Grab(group='commons-io', module='commons-io', version='2.5')
import org.apache.commons.io.FileUtils;

import com.github.strattonbrazil.checklist.JavacPlugin;

tasklist.addTask("clean", {
    TaskContext ctx ->
        FileUtils.deleteQuietly(new File("./bin"));
        "cleaned"
})

tasklist.addTask("compile", ["clean"], {
    TaskContext ctx ->
        println "compiling"
        //println ctx
        ctx.src("test/*.java", [verbose: true])
            .pipe(new JavacPlugin())
            .pipe(ctx.dest("./bin"))
           //.pipe(ctx.dest("./bin/foo"))
           //.pipe(ctx.dest("./bin/bar"))
        "compiled"
})

tasklist.addTask("create_manifest", { //META-INF/MANIFEST.MF
    TaskContext ctx ->
        println "creating manifest"
        ctx.src("./bin")
        "created manifest"
})

tasklist.addTask("jar", ["compile"], {
    TaskContext ctx ->
        println "jarring"
        ctx.src()
        "jarred"
})

tasklist.addTask("default", ["jar"], {
    TaskContext ctx -> "foo"
})

