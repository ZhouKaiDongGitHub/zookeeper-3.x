package org.apache.zookeeper.version;

public interface Info {
    int MAJOR=1;
    int MINOR=0;
    int MICRO=0;
    String QUALIFIER=null;
    int REVISION=-1; //TODO: remove as related to SVN VCS
    String REVISION_HASH="1";
    String BUILD_DATE="2019-3-4";
}
