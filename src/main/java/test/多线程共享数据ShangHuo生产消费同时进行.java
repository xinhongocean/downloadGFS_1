package test;

/**
 * 上货，一进一出同时进行，且有共享数据
 */
public class 多线程共享数据ShangHuo生产消费同时进行 {
    public static void main(String[] args){
        Data data=new Data();
        Producter2Clerk p1=new Producter2Clerk(data);
        Clerk2Customer p2=new Clerk2Customer(data);
        Thread tp= new Thread(p1);
        Thread tc= new Thread(p2);

        tp.start();
        tc.start();
    }
}
/**
 * 生产商品线程类（构造器为了调用引用变量（指针的作用），实现公用的对象data）
 */
class Producter2Clerk implements Runnable{
    Data data;
    public Producter2Clerk(Data data){
        this.data=data;
    }
    public void run(){
        while (true) {
            synchronized (data) {   //在run方法里上锁，包住需要独自执行的部分
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.notifyAll();            //通信的唤醒
                if (data.sailNumber < 20&&data.sailNumber>=0) {
                    data.sailNumber = data.sailNumber + 1;
                    System.out.println("生产后店内产品个数： " + data.sailNumber);
                }else  if(data.sailNumber>=20){System.out.println("店内产品满了");
                    try {
                        data.wait();         //通信的挂起
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
/**
 * 消费商品线程类
 */
class Clerk2Customer implements Runnable {
    Data data;
    public Clerk2Customer(Data data){
        this.data=data;
    }
    public void run(){
//        while (true) {
            synchronized (data) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.notify();
                if (data.sailNumber>0) {
                    data.sailNumber = data.sailNumber - 1;
                    System.out.println("消费后店内产品个数： " + data.sailNumber);
                    data.notifyAll();
                }else if (data.sailNumber<=0){System.out.println("店内产品没了");
                    try {
                        data.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
//        }
    }
}

/**
 * 共享数据类，方便调用共享对象和数据
 */
class Data {
    int sailNumber;
}