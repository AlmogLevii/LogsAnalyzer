package Filtering;

import DataModels.FirewallLog;

public class UserFilter extends FilterLog{
    private String userPattern;

    public UserFilter(String userPattern) {
        //TODO: add validation on the regex?
        this.userPattern = userPattern;
    }

    public UserFilter(String userPattern, boolean isInclude) {
        this(userPattern);
        this.includeFilter = isInclude;
    }

    @Override
    boolean process(FirewallLog log) {
        return !log.getUser().isEmpty() && log.getUser().matches(userPattern);
    }
}
