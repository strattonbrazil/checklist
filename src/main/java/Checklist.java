package com.github.strattonbrazil.checklist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.HashSet;

public class Checklist
{
    private HashMap<String,ActionNode> _actionNodes = new HashMap<String,ActionNode>();

    public ActionNode addAction(String name, Action action) {
        return addAction(name, new ArrayList<String>(), action);
    }

    public ActionNode addAction(String name, ArrayList<String> dependencies, Action action) {
        ActionNode node = new ActionNode(name, dependencies, action);
        if (_actionNodes.containsKey(name)) {
            System.err.println("duplicate task name defined: " + name);
            System.exit(1);
        }
        _actionNodes.put(name, node);
        return node;
    }

    public ArrayList<ActionNode> getSortedTasks() {
        HashMap<String,ActionNode> nodes = new HashMap<String,ActionNode>();
        HashMap<String,HashSet<String>> dependentNodes = new HashMap<String,HashSet<String>>();
        HashMap<String,ArrayList<String>> nodeDependencies = new HashMap<String,ArrayList<String>>();

        // map action names to nodes
        for (String taskName : _actionNodes.keySet()) {
            nodes.put(taskName, _actionNodes.get(taskName));
            dependentNodes.put(taskName, new HashSet<String>());
            nodeDependencies.put(taskName, _actionNodes.get(taskName).dependencies());
        }

        // gather nodes with no dependencies
        Stack<ActionNode> noDependencyNodes = new Stack<ActionNode>();
        for (ActionNode node : _actionNodes.values()) {
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
        ArrayList<ActionNode> sortedNodes = new ArrayList<ActionNode>();
        while (!noDependencyNodes.isEmpty()) {
            ActionNode nNode = noDependencyNodes.pop();
            sortedNodes.add(nNode);

            // for each node "m" dependent on "n"
            for (String mNodeName : dependentNodes.get(nNode.name)) {
                ActionNode mNode = nodes.get(mNodeName);
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

    public void run(String[] taskNames) {
        taskNames = removeDuplicates(taskNames);

        // TODO: if empty, assume "default"

        // filter out tasks that don't need to be run
        //
        ArrayList<ActionNode> allTasks = getSortedTasks();
        HashSet<String> requiredTasks = new HashSet<String>();
        for (String taskName : taskNames) {
            addDependencies(taskName, _actionNodes, requiredTasks);
        }
    }

    // recursively traverse through task and its dependencies adding them to
    // the required set
    public void addDependencies(String taskName,
        HashMap<String,ActionNode> nodes, HashSet<String> requiredTasks) {

        if (!requiredTasks.contains(taskName)) {
            ActionNode node = nodes.get(taskName);
            requiredTasks.add(taskName);
            for (String dependency : node.dependencies()) {
                addDependencies(dependency, nodes, requiredTasks);
            }
        }
    }

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
