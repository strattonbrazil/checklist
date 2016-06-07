package com.github.strattonbrazil.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.HashSet;
import java.util.concurrent.Callable;
import java.nio.file.Path;

public class TaskList
{

    public TaskList(Path cwd) {
        this.cwd = cwd;
    }

    private final Path cwd;
    private HashMap<String,TaskNode> _taskNodes = new HashMap<String,TaskNode>();

    /**
     * Adds a task to the checklist
     * @param name the name of the task
     * @param task the Task, which comprises the work to be done
     */
    public TaskNode addTask(String name, Task task) {
        return addTask(name, new ArrayList<String>(), task);
    }

    /**
     * Adds a task to the checklist
     * @param name the name of the task
     * @param dependencies list of tasks names the task is dependent on
     * @param task the Task, which comprises the work to be done
     */
    public TaskNode addTask(String name, ArrayList<String> dependencies, Task task) {
        TaskNode node = new TaskNode(name, dependencies, task);
        if (_taskNodes.containsKey(name)) {
            System.err.println("duplicate task name defined: " + name);
            System.exit(1);
        }
        _taskNodes.put(name, node);
        return node;
    }

    /**
     * Returns a list of the TaskNodes sorted by topological order, such that
     * dependent tasks are first.
     */
    public ArrayList<TaskNode> getSortedTasks() {
        HashMap<String,TaskNode> nodes = new HashMap<String,TaskNode>();
        HashMap<String,HashSet<String>> dependentNodes = new HashMap<String,HashSet<String>>();
        HashMap<String,ArrayList<String>> nodeDependencies = new HashMap<String,ArrayList<String>>();

        // map task names to nodes
        for (String taskName : _taskNodes.keySet()) {
            nodes.put(taskName, _taskNodes.get(taskName));
            dependentNodes.put(taskName, new HashSet<String>());
            nodeDependencies.put(taskName, _taskNodes.get(taskName).dependencies());
        }

        // gather nodes with no dependencies
        Stack<TaskNode> noDependencyNodes = new Stack<TaskNode>();
        for (TaskNode node : _taskNodes.values()) {
            if (node.dependencies().size() == 0) {
                noDependencyNodes.push(node);
            }
            // map nodes to dependencies
            for (String dependencyName : node.dependencies()) {
                dependentNodes.get(dependencyName).add(node.name);
            }
        }

        // add nodes with no dependencies to list; if all of a node's
        // dependencies have been added, it can be added
        //
        ArrayList<TaskNode> sortedNodes = new ArrayList<TaskNode>();
        while (!noDependencyNodes.isEmpty()) {
            TaskNode nNode = noDependencyNodes.pop();
            sortedNodes.add(nNode);

            // for each node "m" dependent on "n"
            for (String mNodeName : dependentNodes.get(nNode.name)) {
                TaskNode mNode = nodes.get(mNodeName);
                nodeDependencies.get(mNodeName).remove(nNode.name);
                if (nodeDependencies.get(mNodeName).isEmpty()) {
                    noDependencyNodes.push(mNode);
                }
            }
        }

        for (String taskName : nodeDependencies.keySet()) {
            if (!nodeDependencies.get(taskName).isEmpty()) {
                throw new UnsupportedOperationException("cyclic dependency detected on task \'" + taskName + "\'");
            }
        }

        return sortedNodes;
    }

    /**
     * Executes the tasks requested as well as their dependencies
     * @param taskNames the names of the tasks to run
     */
    public void run(String[] taskNames) {
        taskNames = removeDuplicates(taskNames);
        ArrayList<TaskNode> allTasks = getSortedTasks();

        // if no tasks specified, assume "default"
        if (taskNames.length == 0) {
            if (!_taskNodes.keySet().contains("default")) {
                System.err.println("no tasks specified and no default task found");
                System.exit(1);
            }

            taskNames = new String[] { "default" };
        }

        // filter out tasks that don't need to be run
        //
        HashSet<String> requiredTasks = new HashSet<String>();
        for (String taskName : taskNames) {
            addDependencies(taskName, _taskNodes, requiredTasks);
        }
        TaskContext ctx = new TaskContext(cwd);

        for (TaskNode node : allTasks) {
            if (requiredTasks.contains(node.name)) {
                System.out.println("executing task: " + node.name);

                Callable<String> work = node.task.getWork(ctx);
                try {
                    String response = work.call();
                    System.out.println(response);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
            }
        }
    }

    /**
     * For a give task name, goes through the task nodes and adds it and
     * required dependencies to the set.
     * @param taskName the name of the task
     * @param nodes map of available task nodes
     * @param requiredTasks set of nodes where dependencies will be added
     */
    public void addDependencies(String taskName,
        HashMap<String,TaskNode> nodes, HashSet<String> requiredTasks) {

        if (!requiredTasks.contains(taskName)) {
            TaskNode node = nodes.get(taskName);
            requiredTasks.add(taskName);
            for (String dependency : node.dependencies()) {
                addDependencies(dependency, nodes, requiredTasks);
            }
        }
    }

    /**
     * Returns a new array without any duplicates
     * @param array the array of strings
     */
    private String[] removeDuplicates(String[] array) {
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> out = new ArrayList<String>();
        for (String s : array) {
            if (!set.contains(s)) {
                set.add(s);
                out.add(s);
            }
        }
        return out.toArray(new String[out.size()]);
    }
}
