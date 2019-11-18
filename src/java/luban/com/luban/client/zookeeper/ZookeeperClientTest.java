package com.luban.client.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperClientTest {

    public static final String connect = "192.168.124.130:2181";
    private static ZooKeeper zookeeper = null;
    private static CountDownLatch cdl = new CountDownLatch(0);
    private static String nodePath = "/native1";
    private static String nodeChildPath = "/native1/n1/n11/n111/n1111";

    public static void main(String[] args) throws Exception {
        //初始化
        init(connect,5000);
        //新增
        create(nodePath,"n1");
        //递归新增
        createRecursion(nodeChildPath,"n1");
        //查询
        query(nodePath);
        //修改
        update(nodePath,"n11");
        //单个节点删除
        delete(nodePath);
        //递归删除
        deleteRecursion(nodePath);
    }
    private static void deleteRecursion(String nodePath) throws KeeperException, InterruptedException {
        Stat exists = zookeeper.exists(nodePath, true);
        if(null == exists){
            System.out.println(nodePath+"不存在");
            return ;
        }

        List<String> list = zookeeper.getChildren(nodePath, true);
        if(null == list || list.size() == 0){
            delete(nodePath);
            String parentPath = nodePath.substring(0,nodePath.lastIndexOf("/"));
            System.out.println("parentPath="+parentPath);
            if(!"".equals(parentPath)){
                deleteRecursion(parentPath);
            }
        }else{
            for(String child : list){
                deleteRecursion(nodePath+"/"+child);
            }
        }
    }

    private static void delete(String path) throws KeeperException, InterruptedException {
        query(path);//为了让watcher能被监听，在这里查询一次
        zookeeper.delete(path,-1);
        System.out.println("delete:"+"["+path+"]");
    }

    private static void update(String path, String data) throws KeeperException, InterruptedException {
        Stat stat = zookeeper.setData(path, data.getBytes(), -1);//versoin=-1代表不记录版本
        System.out.println("setData:"+"["+path+"],stat:"+stat);
    }

    private static void query(String path) throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        byte[] data = zookeeper.getData(path, true, stat);
        System.out.println("query:"+"["+path+"],result:"+new String(data) + ",stat:"+stat);
    }

    private static void createRecursion(String path,String data) throws KeeperException, InterruptedException {
        if(null == path || "".equals(path)){
            System.out.println("节点["+path+"]为空");
            return;
        }
        String paths[] = path.substring(1,path.length()).split("/");
        for(int i=0;i<paths.length;i++){
            String childPath = "";
            for(int j=0;j<=i;j++){
                childPath += "/" + paths[j];
            }
            create(childPath,data);
        }

        Stat exists = zookeeper.exists(path, true);
        if(null != exists){
            System.out.println("节点["+path+"]已存在，不能新增");
            return;
        }
        String result = zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create:"+"["+path+"-->"+data+"],result:"+result);
    }

    private static void create(String path,String data) throws KeeperException, InterruptedException {
        Stat exists = zookeeper.exists(path, true);
        if(null != exists){
            System.out.println("节点["+path+"]已存在，不能新增");
            return;
        }
        String result = zookeeper.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create:"+"["+path+"-->"+data+"],result:"+result);
    }

    private static void init(final String connectStr, int sessionTimeout) throws Exception {
        //zookeeper = new ZooKeeper(connectStr, sessionTimeout, null);
        //由于zookeeper连接是异步的，如果new ZooKeeper(connectStr, sessionTimeout, null)完之后马上使用，有可能会报错。
        //解决办法：增加watcher，监听事件如果为SyncConnected,那么才做其他的操作。（利用CountDownLatch控制）
        zookeeper = new ZooKeeper(connectStr, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                    cdl.countDown();
                }
                if(watchedEvent.getType() == Event.EventType.NodeCreated){
                    System.out.println("zookeeper有新节点创建"+watchedEvent.getPath());
                }
                if(watchedEvent.getType() == Event.EventType.NodeDataChanged){
                    System.out.println("zookeeper有节点数据变化"+watchedEvent.getPath());
                }
                if(watchedEvent.getType() == Event.EventType.NodeDeleted){
                    System.out.println("zookeeper有节点被删除"+watchedEvent.getPath());
                }
                if(watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                    System.out.println("zookeeper有子节点变化"+watchedEvent.getPath());
                }
            }
        });
        cdl.await();
        System.out.println("init start :" +zookeeper);
    }
}
