package com.github.strattonbrazil.checklist;

import ro.fortsoft.pf4j.DefaultPluginManager;
import ro.fortsoft.pf4j.PluginManager;

import com.github.strattonbrazil.checklist.ui.ChecklistFrame;

public class App 
{
    public static void main( String[] args )
    {
        ChecklistFrame.showUi();

        PluginManager pluginManager = new DefaultPluginManager();
        pluginManager.loadPlugins();
        pluginManager.startPlugins();

        System.out.println( "Hello World!" );
    }
}
