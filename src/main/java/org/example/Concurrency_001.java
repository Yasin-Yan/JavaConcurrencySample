package org.example;

/**
 * 缓存导致的可见性问题：对于多核cpu线程运行在不同的cpu上时，每个cpu都有各自的缓存，写入内存时会出现覆盖情况
 * 线程切换带来的原子性问题：count+=1在cpu级别是三条指令 1.读取主存count到cpu缓存 2.在寄存器进行加法运算 3.写回到主存 一个任务在执行三条指令时可能在第三步执行前切换到其他线程
 * 编译优化带来的有序性问题：编译器会对指令进行重排，最典型的例子是单例模式的双重校验锁，getInstance时由于指令重排致使instance为空指针
 */
public class Concurrency_001 {
    private long count = 0;

    private void add10k() {
        int i = 0;
        while (i++ < 10000) {
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final Concurrency_001 cor = new Concurrency_001();
        final Thread task1 = new Thread() {
            public void run() {
                cor.add10k();
                System.out.println("====task1====:"+cor.count);
            }
        };

        Thread task2 = new Thread() {
            public void run() {
                cor.add10k();
                System.out.println("====task2====:"+cor.count);
            }
        };

        task1.start();
        task2.start();
        task1.join();
        task2.join();
        System.out.println("====count====:" + cor.count);
    }
}
