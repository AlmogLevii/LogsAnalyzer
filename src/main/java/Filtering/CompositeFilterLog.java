package Filtering;

import DataModels.FirewallLog;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompositeFilterLog extends FilterLog {

    private final List<FilterLog> filters;

    public CompositeFilterLog(FilterLog... filtersLogs) {
        filters = new ArrayList<>();
        this.add(filtersLogs);
    }

    public void add(FilterLog... filtersLogs) {
        filters.addAll(Arrays.asList(filtersLogs));
    }

    public void remove(FilterLog... filtersLogs) {
        filters.removeAll(Arrays.asList(filtersLogs));
    }

    @Override
    boolean process(FirewallLog log) {
        for(FilterLog filterLog : filters){
            //TODO: decide what stronger- include or exclude;
            if(!filterLog.filter(log)){
                return false;
            }
        }
        return true;
    }
}
