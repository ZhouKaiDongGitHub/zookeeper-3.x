package com.luban.client.application.distributionLock;

public class DistributionLockTest {

    public static void main(String[] args) {
        DistributionLock lock = new DistributionLock();
        String lockName = lock.getLock();
        /**
         * 执行我们的业务逻辑
         */
        if(lockName != null) {
            lock.releaseLock(lockName);
        }

        lock.closeZkClient();
    }
}
