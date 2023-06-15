package Filtering;

import DataModels.FirewallLog;

public abstract class FilterLog {
    boolean includeFilter = true;

    public boolean filter(FirewallLog log) {
        boolean includeProcess = process(log);
        return includeFilter ? includeProcess : !includeProcess;
    }

    abstract boolean process(FirewallLog log);
}
